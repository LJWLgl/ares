package cn.ganzhiqiang.ares.search;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.ganzhiqiang.ares.Ares;
import cn.ganzhiqiang.ares.search.service.ESSearchService;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Ares.class)
public class ElasticSearchUtilTest {

    @Resource
    private ElasticSearchHelper elasticSearch;
    @Resource
    private RestHighLevelClient client;
    @Resource
    private ESSearchService esSearchService;


    @Test
    public void test() throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("title_cn", "罗小黑水杯");
        jsonMap.put("title_en", "luxiaohe beizi");
        jsonMap.put("price", 59);
        jsonMap.put("status", 6);
        IndexRequest indexRequest = new IndexRequest("ayy_goods", "ayy_goods", "3")
                .source(jsonMap);
        IndexResponse indexResponse = client.index(indexRequest);
        System.out.println("ee");
    }

    @Test
    public void test1() {
//        esSearchService.searchGoodsByKeyWord("水壶", 1, 10);
    }

}
