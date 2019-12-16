package cn.ganzhiqiang.ares.search.service;

import cn.ganzhiqiang.ares.goods.dao.CommodityDAO;
import cn.ganzhiqiang.ares.goods.domian.GoodsDO;
import cn.ganzhiqiang.ares.search.ElasticSearchHelper;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zq_gan
 * @since 2019/12/2
 **/

@Service
public class ESSearchService {

    private Logger logger = LoggerFactory.getLogger(ESSearchService.class);

    @Resource
    private CommodityDAO commodityDAO;
    @Resource
    private ElasticSearchHelper searchHelper;

    public boolean fullIndexGoodsIndex(int beginId, int limit) {
        logger.info("fullUpsertGoodsIndex begin, beginId:{}, limit", beginId, limit);
        List<GoodsDO> goodsDOS = commodityDAO.queryGoodsGtId(beginId, limit);
        while (CollectionUtils.isNotEmpty(goodsDOS)) {
            for (GoodsDO goods : goodsDOS) {
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("id", goods.getId());
                jsonMap.put("title_cn", goods.getTitle());
                jsonMap.put("describe_cn", goods.getDescible());
                jsonMap.put("price", goods.getPrice());
                jsonMap.put("status", goods.getStatus());
                IndexRequest indexRequest = new IndexRequest("ayy_goods", "ayy_goods", String.valueOf(goods.getId()))
                        .source(jsonMap);
                searchHelper.insert(indexRequest);
            }
            beginId = goodsDOS.get(goodsDOS.size() - 1).getId();
            goodsDOS = commodityDAO.queryGoodsGtId(beginId, limit);
        }
        logger.info("fullUpsertGoodsIndex end");
        return true;
    }



}
