package cn.ganzhiqiang.ares;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zq_gan
 * @since 2020/6/7
 **/

public class HighLevelClientTest {

    private RestHighLevelClient client = null;

    @Before
    public void init() {
         client = new RestHighLevelClient(RestClient.builder(new HttpHost("140.143.144.28", 9200, "http")));
    }

    @Test
    public void testIndex() throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("posts", "posts")
                .id("1").source(jsonMap);
        client.index(indexRequest, RequestOptions.DEFAULT);
    }

}
