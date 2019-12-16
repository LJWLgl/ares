package cn.ganzhiqiang.ares.goods.service;

import cn.ganzhiqiang.ares.common.enums.StatusEnum;
import cn.ganzhiqiang.ares.search.domain.GoodsHit;
import cn.ganzhiqiang.ares.search.util.ESSearchResult;
import cn.ganzhiqiang.ares.search.util.ESSearchUtil;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import cn.ganzhiqiang.ares.common.enums.TypeEnum;
import cn.ganzhiqiang.ares.count.model.BaseGoodsCount;
import cn.ganzhiqiang.ares.count.service.CounterService;
import cn.ganzhiqiang.ares.favorite.domain.FavoriteDO;
import cn.ganzhiqiang.ares.favorite.service.FavoriteService;
import cn.ganzhiqiang.ares.goods.assembler.BaseGoodsAssembler;
import cn.ganzhiqiang.ares.goods.dao.CommodityDAO;
import cn.ganzhiqiang.ares.goods.domian.GoodsDO;
import cn.ganzhiqiang.ares.goods.domian.ImageSimpleDO;
import cn.ganzhiqiang.ares.goods.model.BaseGoods;
import cn.ganzhiqiang.ares.like.domain.LikeDO;
import cn.ganzhiqiang.ares.like.service.LikeService;
import cn.ganzhiqiang.ares.qiniu.util.Auth;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cn.ganzhiqiang.ares.search.constans.ESConstans.SCORE;

/**
 * @author nanxuan
 * @since 2017/12/16
 **/
@Service
public class CommodityService {

    @Resource
    private CommodityDAO commodityDAO;

    @Resource
    private TransactionTemplate transactionTpl;

    @Resource
    private CounterService counterService;

    @Resource
    private LikeService likeService;

    @Resource
    private FavoriteService favoriteService;

    // 七牛云秘钥，建议周期性更换一次
    private static final String ACCESS_KEY = "qVwP0HV-VC7jGGmREBUZwgKrdJNs9ib5_rA-6OSs";

    private static final String SECRET_KEY = "4qiNwcXVLWtKZ3BY9GKKIFLVv4KukWxd7xmSIsG0";

    private static final String BUCKET_NAME = "ayy-file";

    public List<BaseGoods> queryLastedGoods(int start, int limit) {

        List<GoodsDO> goodsDOList = commodityDAO.queryGoods(start, limit);
        if (CollectionUtils.isEmpty(goodsDOList)) {
            return new ArrayList<>();
        }

        List<Integer> goodsIds = goodsDOList.stream().map(GoodsDO::getId).collect(Collectors.toList());
        List<ImageSimpleDO> imageList = commodityDAO.queryImagesByGoodsIdList(goodsIds);
        if (CollectionUtils.isEmpty(imageList)) {
            return new ArrayList<>();
        }
        List<BaseGoodsCount> baseGoodsCounts = counterService.queryGoodsCount(goodsIds);

        return BaseGoodsAssembler.toBaseList(goodsDOList, imageList, baseGoodsCounts);
    }

    public List<BaseGoods> queryRecommendGoods(int uid, int limit) {

        // 临时推荐策略，随机
        int count = commodityDAO.countGoods();
        int start = ((count + uid * uid) / 2) % count;

        List<GoodsDO> goodsDOList = commodityDAO.queryGoods(start, limit);
        if (CollectionUtils.isEmpty(goodsDOList)) {
            return new ArrayList<>();
        }

        List<Integer> goodsIds = goodsDOList.stream().map(GoodsDO::getId).collect(Collectors.toList());
        List<ImageSimpleDO> imageList = commodityDAO.queryImagesByGoodsIdList(goodsIds);
        if (CollectionUtils.isEmpty(imageList)) {
            return new ArrayList<>();
        }
        List<BaseGoodsCount> baseGoodsCounts = counterService.queryGoodsCount(goodsIds);

        return BaseGoodsAssembler.toBaseList(goodsDOList, imageList, baseGoodsCounts);
    }

    public List<BaseGoods> queryDonationGoods(int start, int limit) {

        List<GoodsDO> goodsDOList = commodityDAO.queryDonationGoods(start, limit);
        if (CollectionUtils.isEmpty(goodsDOList)) {
            return new ArrayList<>();
        }

        List<Integer> goodsIds = goodsDOList.stream().map(GoodsDO::getId).collect(Collectors.toList());
        List<ImageSimpleDO> imageList = commodityDAO.queryImagesByGoodsIdList(goodsIds);
        if (CollectionUtils.isEmpty(imageList)) {
            return new ArrayList<>();
        }
        List<BaseGoodsCount> baseGoodsCounts = counterService.queryGoodsCount(goodsIds);

        return BaseGoodsAssembler.toBaseList(goodsDOList, imageList, baseGoodsCounts);
    }

    public List<BaseGoods> queryHottest(int start, int limit) {

        List<GoodsDO> goodsDOList = commodityDAO.queryHottest(start, limit);
        if (CollectionUtils.isEmpty(goodsDOList)) {
            return new ArrayList<>();
        }

        List<Integer> goodsIds = goodsDOList.stream().map(GoodsDO::getId).collect(Collectors.toList());
        List<ImageSimpleDO> imageList = commodityDAO.queryImagesByGoodsIdList(goodsIds);
        if (CollectionUtils.isEmpty(imageList)) {
            return new ArrayList<>();
        }
        List<BaseGoodsCount> baseGoodsCounts = counterService.queryGoodsCount(goodsIds);

        return BaseGoodsAssembler.toBaseList(goodsDOList, imageList, baseGoodsCounts);
    }

    public BaseGoods findGoods(Integer goodsId) {
        GoodsDO goodsDO = commodityDAO.findGoods(goodsId);
        if (goodsDO == null) {
            return null;
        }

        List<ImageSimpleDO> images = commodityDAO.queryImagesByGoodsId(goodsId);
        if (CollectionUtils.isEmpty(images)) {
            return null;
        }

        BaseGoodsCount baseGoodsCount = counterService.findGoodsCount(goodsId);
        BaseGoods baseGoods = BaseGoodsAssembler.toBase(goodsDO, images, baseGoodsCount);
        LikeDO likeDO = likeService.findLikeByResourceId(goodsId, TypeEnum.GOODS);
        if (likeDO != null) {
            baseGoods.setLikeId(likeDO.getId());
        }
        FavoriteDO favoriteDO = favoriteService.findByResourceId(goodsId, TypeEnum.GOODS);
        if (favoriteDO != null) {
            baseGoods.setFavoriteId(favoriteDO.getId());
        }
        return baseGoods;
    }

    public List<BaseGoods> queryGoodsByIds(List<Integer> goodsIds) {
        List<GoodsDO> goodsDOList = commodityDAO.queryGoodsByIds(goodsIds);
        if (CollectionUtils.isEmpty(goodsDOList)) {
            return new ArrayList<>();
        }
        List<ImageSimpleDO> imageList = commodityDAO.queryImagesByGoodsIdList(goodsIds);
        if (CollectionUtils.isEmpty(imageList)) {
            return new ArrayList<>();
        }
        List<BaseGoodsCount> baseGoodsCounts = counterService.queryGoodsCount(goodsIds);

        return BaseGoodsAssembler.toBaseList(goodsDOList, imageList, baseGoodsCounts);
    }

    public List<BaseGoods> queryGoodsByUid(Integer userId, int start, int limit) {
        List<GoodsDO> goodsDOS = commodityDAO.queryGoodsByUid(userId, start, limit);
        if (CollectionUtils.isEmpty(goodsDOS)) {
            return new ArrayList<>();
        }
        List<Integer> goodsIds = goodsDOS.stream().map(GoodsDO::getId).collect(Collectors.toList());
        List<ImageSimpleDO> imageList = commodityDAO.queryImagesByGoodsIdList(goodsIds);
        if (CollectionUtils.isEmpty(imageList)) {
            return new ArrayList<>();
        }
        List<BaseGoodsCount> baseGoodsCounts = counterService.queryGoodsCount(goodsIds);
        return BaseGoodsAssembler.toBaseList(goodsDOS, imageList, baseGoodsCounts);
    }

    public List<Integer> queryGoodsIdsByUid(Integer userId, int start, int limit) {
        return commodityDAO.queryGoodsIdsByUid(userId, start, limit);
    }

    public List<BaseGoods> queryGoodsByKeyword(String keyword, int start, int limit) {

        List<GoodsDO> goodsDOS = commodityDAO.queryGoodsByKeyword(keyword, start, limit);
        if (CollectionUtils.isEmpty(goodsDOS)) {
            return new ArrayList<>();
        }
        List<Integer> goodsIds = goodsDOS.stream().map(GoodsDO::getId).collect(Collectors.toList());
        List<ImageSimpleDO> imageList = commodityDAO.queryImagesByGoodsIdList(goodsIds);
        if (CollectionUtils.isEmpty(imageList)) {
            return new ArrayList<>();
        }
        List<BaseGoodsCount> baseGoodsCounts = counterService.queryGoodsCount(goodsIds);
        List<BaseGoods> baseGoods = BaseGoodsAssembler.toBaseList(goodsDOS, imageList, baseGoodsCounts);
        // 搜索词高亮
//    return highlightKeyword(keyword, baseGoods);
        return baseGoods;
    }

    public List<BaseGoods> searchGoodsByKeyWord(String keyword, int pageIndex, int pageSize) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("title_cn", keyword));
        boolQueryBuilder.filter(QueryBuilders.termQuery("status", StatusEnum.PUBLISH.getStatus()));
        ESSearchResult<GoodsHit> result = ESSearchUtil.get().search(boolQueryBuilder, GoodsHit.class, pageIndex + 1, pageSize);
        if (CollectionUtils.isEmpty(result.getResults())) {
            return Collections.emptyList();
        }
        List<Integer> ids = result.getResults().stream().map(GoodsHit::getId).collect(Collectors.toList());
        return queryGoodsByIds(ids);
    }

    private List<BaseGoods> highlightKeyword(String keyword, List<BaseGoods> baseGoods) {
        List<BaseGoods> baseGoodsList = new ArrayList<>(baseGoods);
        baseGoodsList.forEach(item -> {
            if (item.getTitle() != null && item.getDescible() != null) {
                item.setTitle(item.getTitle().replace(keyword, "<text style=\"color:#ef0418\">" + keyword + "</text>"));
                item.setDescible(item.getDescible().replace(keyword, "<text style=\"color:#ef0418\">" + keyword + "</text>"));
            }
        });
        return baseGoodsList;
    }

    public Integer addGoods(BaseGoods baseGoods) {
        if (baseGoods == null) {
            return 0;
        }
        Integer goodsId = transactionTpl.execute(new TransactionCallback<Integer>() {
            @Override
            public Integer doInTransaction(TransactionStatus transactionStatus) {
                try {
                    GoodsDO goodsDO = BaseGoodsAssembler.toGoodsDO(baseGoods);
                    int goodsId = commodityDAO.insertGoods(goodsDO);
                    if (goodsId == 0) {
                        return 0;
                    }

                    List<ImageSimpleDO> images = BaseGoodsAssembler.toImageDO(baseGoods.getPhotos(), goodsId);
                    commodityDAO.insertImageList(images);

                    return goodsId;
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    return 0;
                }
            }
        });
        counterService.incrCount(baseGoods.getPublishUserId(), 6);
        return goodsId;
    }

    public String generateToken() {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        return auth.uploadToken(BUCKET_NAME);
    }

    public int findMaxId() {
        return commodityDAO.findMaxId();
    }

    public int findMinId() {
        return commodityDAO.findMinId();
    }

    public void incrLookCount(Integer goodsId) {
        commodityDAO.incrLookLike(goodsId);
    }

}
