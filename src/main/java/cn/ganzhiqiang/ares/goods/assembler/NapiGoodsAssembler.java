package cn.ganzhiqiang.ares.goods.assembler;

import org.apache.commons.collections.CollectionUtils;

import cn.ganzhiqiang.ares.goods.vo.GoodsSimpleVO;
import cn.ganzhiqiang.ares.goods.model.BaseGoods;
import cn.ganzhiqiang.ares.goods.vo.GoodsVO;
import cn.ganzhiqiang.ares.common.utils.DateUtil;
import cn.ganzhiqiang.ares.people.dto.UserVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nanxuan
 * @since 2018/2/4
 **/

public class NapiGoodsAssembler {

    public static GoodsVO toApi(BaseGoods baseGoods, UserVO user) {
        return GoodsVO.builder()
                .id(baseGoods.getId())
                .userVO(user)
                .title(baseGoods.getTitle())
                .descible(baseGoods.getDescible())
                .photos(baseGoods.getPhotos())
                .price(baseGoods.getPrice())
                .oldPrice(baseGoods.getOldPrice())
                .freight(baseGoods.getFreight())
                .publishAddress(baseGoods.getPublishAddress())
                .isDonation(baseGoods.getIsDonation())
                .publishDate(DateUtil.standardNapiDate(baseGoods.getCreatDatetime()))
                .lookCount(baseGoods.getLookCount())
                .commentCount(baseGoods.getBaseGoodsCount().getCommentCount())
                .favoriteCount(baseGoods.getBaseGoodsCount().getFavoriteCount())
                .likeCount(baseGoods.getBaseGoodsCount().getLikeCount())
                .tags(Arrays.asList("热卖", "时尚"))
                .favoriteId(baseGoods.getFavoriteId())
                .likeId(baseGoods.getLikeId())
                .build();
    }

    public static GoodsSimpleVO toSimpleApi(BaseGoods baseGoods) {

        return GoodsSimpleVO.builder()
                .id(baseGoods.getId())
                .publishUserId(baseGoods.getPublishUserId())
                .title(baseGoods.getTitle())
                .descible(baseGoods.getDescible())
                .cover(baseGoods.getPhotos().get(0)) // 默认取第一张 FIXME
                .price(String.valueOf(baseGoods.getPrice()))
                .publishAddress(baseGoods.getPublishAddress())
                .isDonation(baseGoods.getIsDonation()) // 1 捐赠，0 不捐赠
                .publishDate(DateUtil.standardNapiDate(baseGoods.getCreatDatetime()))
                .lookCount(baseGoods.getLookCount())
                .tags(Arrays.asList("实用", "精美"))
                .build();

    }

    public static List<GoodsSimpleVO> toSimpleApiList(List<BaseGoods> baseGoodsList) {

        if (CollectionUtils.isEmpty(baseGoodsList)) {
            return new ArrayList<>();
        }

        return baseGoodsList.stream()
                .map(NapiGoodsAssembler::toSimpleApi)
                .collect(Collectors.toList());
    }

}
