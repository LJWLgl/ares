package cn.ganzhiqiang.ares.search.util;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ESSearchUtilTest {

    @Test
    public void testGet() {
        // Setup

        // Run the test
        final ESSearchUtil result = ESSearchUtil.get();

        // Verify the results
    }

    @Test
    public void testIndex() throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("posts").id("1").source(jsonMap);
        ESSearchUtil.getHighLevelClient().index(indexRequest, RequestOptions.DEFAULT);
    }

}
