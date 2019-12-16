package cn.ganzhiqiang.ares.goods.dao;


import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import cn.ganzhiqiang.ares.goods.domian.GoodsDO;
import cn.ganzhiqiang.ares.goods.domian.ImageSimpleDO;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nanxuan
 * @since 2017/12/10
 **/

@Repository
public class CommodityDAO {

    private static final String MAPPING_PREFIX = "goods.";

    @Resource
    private SqlSessionTemplate readTpl;

    @Resource
    private SqlSessionTemplate writeTpl;

    public GoodsDO findGoods(Integer goodsId) {
        Map params = new HashMap();
        params.put("goodsId", goodsId);
        return readTpl.selectOne(MAPPING_PREFIX + "findGoods", params);
    }

    public List<GoodsDO> queryGoods(int start, int limit) {
        Map params = new HashMap();
        params.put("start", start);
        params.put("limit", limit);
        return readTpl.selectList(MAPPING_PREFIX + "queryGoods", params);
    }

    public List<GoodsDO> queryDonationGoods(int start, int limit) {
        Map params = new HashMap();
        params.put("start", start);
        params.put("limit", limit);
        return readTpl.selectList(MAPPING_PREFIX + "queryDonationGoods", params);
    }

    public List<GoodsDO> queryHottest(int start, int limit) {
        Map params = new HashMap();
        params.put("start", start);
        params.put("limit", limit);
        return readTpl.selectList(MAPPING_PREFIX + "queryHottest", params);
    }

    public List<GoodsDO> queryGoodsByIds(List<Integer> goodsIds) {
        Map params = new HashMap();
        params.put("goodsIds", goodsIds);
        return readTpl.selectList(MAPPING_PREFIX + "queryGoodsByIds", params);
    }

    public List<GoodsDO> queryGoodsByUid(Integer userId, int start, int limit) {
        Map params = new HashMap();
        params.put("userId", userId);
        params.put("start", start);
        params.put("limit", limit);
        return readTpl.selectList(MAPPING_PREFIX + "queryGoodsByUid", params);
    }

    public List<Integer> queryGoodsIdsByUid(Integer userId, int start, int limit) {
        Map params = new HashMap();
        params.put("userId", userId);
        params.put("start", start);
        params.put("limit", limit);
        return readTpl.selectList(MAPPING_PREFIX + "queryGoodsIdsByUid", params);
    }

    public List<GoodsDO> queryGoodsByKeyword(String keyword, int start, int limit) {
        Map params = new HashMap();
        params.put("keyword", keyword);
        params.put("start", start);
        params.put("limit", limit);
        return readTpl.selectList(MAPPING_PREFIX + "queryGoodsByKeyWord", params);
    }

    public List<GoodsDO> queryGoodsGtId(int beginId, int limit) {
        Map params = new HashMap();
        params.put("beginId", beginId);
        params.put("limit", limit);
        return readTpl.selectList(MAPPING_PREFIX + "queryGoodsGtId", params);
    }

    public List<ImageSimpleDO> queryImagesByGoodsId(Integer goodsId) {
        Map params = new HashMap();
        params.put("goodsId", goodsId);
        return readTpl.selectList(MAPPING_PREFIX + "queryImageByGoodsId", params);
    }

    public List<ImageSimpleDO> queryImagesByGoodsIdList(List<Integer> goodsIds) {
        Map params = new HashMap();
        params.put("goodsIds", goodsIds);
        return readTpl.selectList(MAPPING_PREFIX + "queryImageByGoodsIdList", params);
    }

    public ImageSimpleDO findImage(Integer imageId) {
        Map params = new HashMap();
        params.put("imageId", imageId);
        return readTpl.selectOne(MAPPING_PREFIX + "findImageById", params);
    }

    public int insertGoods(GoodsDO goodsDO) {
        int result = writeTpl.insert(MAPPING_PREFIX + "insertGoods", goodsDO);
        return result == 1 ? goodsDO.getId() : 0;
    }

    public int insertImageList(List<ImageSimpleDO> images) {
        return writeTpl.insert(MAPPING_PREFIX + "insertImageList", images);
    }

    public boolean incrLookLike(Integer goodsId) {
        Map params = new HashMap();
        params.put("goodsId", goodsId);
        writeTpl.update(MAPPING_PREFIX + "incrLookCount", params);
        return true;
    }

    public int countGoods() {
        return readTpl.selectOne(MAPPING_PREFIX + "countGoods");
    }

    public int findMinId() {
        return readTpl.selectOne(MAPPING_PREFIX + "findMinId");
    }

    public int findMaxId() {
        return readTpl.selectOne(MAPPING_PREFIX + "findMaxId");
    }

}
