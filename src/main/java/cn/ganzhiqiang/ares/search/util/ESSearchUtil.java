package cn.ganzhiqiang.ares.search.util;

import cn.ganzhiqiang.ares.common.utils.GConfigUtil;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zq_gan
 * @since 2019/12/15
 **/

public class ESSearchUtil {

    private static final Logger logger = LoggerFactory.getLogger(ESSearchUtil.class);

    private final String host = GConfigUtil.getValue("es.host", String.class);
    private final int port = GConfigUtil.getValue("es.port", Integer.class);
    private final String SCHEMA = "http";

    private static RestHighLevelClient restHighLevelClient;
    private static ESSearchUtil instance;

    private ESSearchUtil() {
        init();
    }

    public static ESSearchUtil get() {
        if (instance == null) {
            synchronized (ESSearchUtil.class) {
                if (instance == null) {
                    instance = new ESSearchUtil();
                }
            }
        }

        return instance;
    }

    public static RestHighLevelClient getHighLevelClient() {
        if (restHighLevelClient == null) {
            synchronized (ESSearchUtil.class) {
                if (restHighLevelClient == null) {
                    instance = new ESSearchUtil();
                }
            }
        }
        return restHighLevelClient;
    }

    private void init() {
        RestClientBuilder restClient = RestClient.builder(new HttpHost(host, port, SCHEMA));
        restHighLevelClient = new RestHighLevelClient(restClient);
    }

    public <T> ESSearchResult<T> search(QueryBuilder queryBuilder, Class<T> clz,
                                               int pageNum, int pageSize, SortBuilder... sortBuilders) {
        SearchRequest searchRequest = new SearchRequest("ayy_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from((pageNum - 1) * pageSize);
        sourceBuilder.size(pageSize);
        sourceBuilder.query(queryBuilder);
        if (Objects.nonNull(sortBuilders)) {
            for (SortBuilder sortBuilder : sortBuilders) {
                sourceBuilder.sort(sortBuilder);
            }
        }
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error("es search error", e);
        }
        List<T> results = new ArrayList<>(pageSize);

        if (searchResponse != null && searchResponse.getHits() != null && searchResponse.getHits() != null
                && searchResponse.getHits().getHits() != null) {
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                results.add(JSON.parseObject(hit.getSourceAsString(), clz));
            }
            return new ESSearchResult<>(results.size(), results);
        }
        return new ESSearchResult<>(0, null);
    }

}
