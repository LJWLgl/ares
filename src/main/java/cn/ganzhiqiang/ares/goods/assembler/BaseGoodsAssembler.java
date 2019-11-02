package cn.ganzhiqiang.ares.goods.assembler;

import org.apache.commons.collections.CollectionUtils;

import cn.ganzhiqiang.ares.common.Converter;
import cn.ganzhiqiang.ares.count.model.BaseGoodsCount;
import cn.ganzhiqiang.ares.goods.domian.GoodsDO;
import cn.ganzhiqiang.ares.goods.domian.ImageSimpleDO;
import cn.ganzhiqiang.ares.goods.vo.ImageVO;
import cn.ganzhiqiang.ares.goods.model.BaseGoods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author nanxuan
 * @since 2018/1/31
 **/

public class BaseGoodsAssembler {

  public static BaseGoods toBase(GoodsDO goodsDO, List<ImageSimpleDO> images, BaseGoodsCount count) {

    return BaseGoods.builder()
        .id(goodsDO.getId())
        .publishUserId(goodsDO.getPublishUserId())
        .title(goodsDO.getTitle())
        .descible(goodsDO.getDescible())
        .photos(Converter.toDtos(images, ImageVO.class))
        .price(goodsDO.getPrice())
        .oldPrice(goodsDO.getOldPrice())
        .freight(goodsDO.getFreight())
        .category(goodsDO.getCategory())
        .publishAddress(goodsDO.getPublishAddress())
        .isDonation(goodsDO.getIsDonation())
        .status(goodsDO.getStatus())
        .lookCount(goodsDO.getLookCount())
        .likeId(0)
        .favoriteId(0)
        .baseGoodsCount(count)
        .creatDatetime(goodsDO.getCreatDatetime())
        .gmtUpdated(goodsDO.getGmtUpdated())
        .build();
  }

  public static List<BaseGoods> toBaseList(List<GoodsDO> goodsDOS, List<ImageSimpleDO> imageList, List<BaseGoodsCount> baseGoodsCounts) {

    if (CollectionUtils.isEmpty(goodsDOS) || CollectionUtils.isEmpty(imageList)) {
      return new ArrayList<>();
    }

    Map<Integer, List<ImageSimpleDO>> imageMap = imageList.stream()
        .collect(Collectors.groupingBy(ImageSimpleDO::getGoodsId));

    Map<Integer, BaseGoodsCount> countMap = baseGoodsCounts.stream()
        .collect(Collectors.toMap(BaseGoodsCount::getResourceId, Function.identity()));

    return goodsDOS.stream()
        .map(item -> toBase(item, imageMap.get(item.getId()), countMap.get(item.getId())))
        .collect(Collectors.toList());
  }
  
  public static List<ImageSimpleDO> toImageDO(List<ImageVO> imageVOS, int goodsId)  {
    
    return imageVOS.stream().map(item -> {
          return ImageSimpleDO.builder()
              .goodsId(goodsId)
              .path(item.getPath())
              .build();
        })
        .collect(Collectors.toList());
    
  }
  
  public static GoodsDO toGoodsDO(BaseGoods baseGoods) {
    
    return GoodsDO.builder()
        .publishUserId(baseGoods.getPublishUserId())
        .title(baseGoods.getTitle())
        .descible(baseGoods.getDescible())
        .price(baseGoods.getPrice())
        .oldPrice(baseGoods.getOldPrice())
        .freight(baseGoods.getFreight())
        .category(baseGoods.getCategory())
        .publishAddress(baseGoods.getPublishAddress())
        .isDonation(baseGoods.getIsDonation())
        .build();
  }
  
}
