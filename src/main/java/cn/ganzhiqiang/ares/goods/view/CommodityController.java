package cn.ganzhiqiang.ares.goods.view;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ganzhiqiang.ares.common.enums.NapiRespStatus;
import cn.ganzhiqiang.ares.common.warpper.NapiRespWrapper;
import cn.ganzhiqiang.ares.common.warpper.NapiRespWrapper.ScrollingData;
import cn.ganzhiqiang.ares.favorite.domain.FavoriteDO;
import cn.ganzhiqiang.ares.favorite.service.FavoriteService;
import cn.ganzhiqiang.ares.goods.assembler.NapiGoodsAssembler;
import cn.ganzhiqiang.ares.goods.vo.GoodsSimpleVO;
import cn.ganzhiqiang.ares.goods.vo.GoodsVO;
import cn.ganzhiqiang.ares.goods.vo.ImageVO;
import cn.ganzhiqiang.ares.goods.enums.QueryTypeEnum;
import cn.ganzhiqiang.ares.goods.model.BaseGoods;
import cn.ganzhiqiang.ares.goods.service.CommodityService;
import cn.ganzhiqiang.ares.like.domain.LikeDO;
import cn.ganzhiqiang.ares.like.service.LikeService;
import cn.ganzhiqiang.ares.people.dto.UserVO;
import cn.ganzhiqiang.ares.goods.enums.QueryPublishType;
import cn.ganzhiqiang.ares.people.service.PersonService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author nanxuan
 * @since 2017/12/10
 **/

@RequestMapping("goods")
@ResponseBody
@Controller
public class CommodityController {

  @Resource
  private CommodityService commodityService;

  @Resource
  private PersonService personService;

  @Resource
  private LikeService likeService;

  @Resource
  private FavoriteService favoriteService;

  @Resource
  private ObjectMapper objectMapper;

  private static final String QINIU_UPLOAD_TOKEN_NAME = "uptoken";

  @RequestMapping(value = "detail/", method = RequestMethod.GET)
  public NapiRespWrapper<GoodsVO> detail(@RequestParam Integer goods_id) {
    if (goods_id == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "缺少必要参数");
    }
    BaseGoods baseGoods = commodityService.findGoods(goods_id);
    if (baseGoods == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
    }
    UserVO user = personService.findUserDetailByUid(baseGoods.getPublishUserId());
    if (user == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
    }
    // lookCount + 1
    commodityService.incrLookCount(goods_id);
    return new NapiRespWrapper<>(NapiGoodsAssembler.toApi(baseGoods, user));
  }

  @RequestMapping(value = "publish/", method = RequestMethod.GET)
  public NapiRespWrapper queryPublish(
      @RequestParam(value = "uid") Integer uid,
      @RequestParam(value = "type") Integer type,
      @RequestParam(value = "start", defaultValue = "0") Integer start,
      @RequestParam(value = "limit", defaultValue = "24") Integer limit) {
    if (uid == null || type == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
    }

    QueryPublishType queryType = QueryPublishType.of(type);
    if (queryType.getValue() == QueryPublishType.UNKNOWN.getValue()) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
    }

    List<Integer> goodsIds = new ArrayList<>();
    if (queryType.getValue() == QueryPublishType.FAVORITE.getValue()) {
      List<FavoriteDO> favoriteDOS = favoriteService.queryFavoriteByUid(uid, start, limit + 1);
      if (CollectionUtils.isEmpty(favoriteDOS)) {
        return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
      }
      goodsIds = favoriteDOS.stream().map(FavoriteDO::getResourceId).filter(Objects::nonNull)
          .collect(Collectors.toList());
    } else if (queryType.getValue() == QueryPublishType.LIKE.getValue()) {
      List<LikeDO> likeDOS = likeService.queryLikeByUid(uid, start, limit);
      if (CollectionUtils.isEmpty(likeDOS)) {
        return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
      }
      goodsIds = likeDOS.stream().map(LikeDO::getResourceId).filter(Objects::nonNull)
          .collect(Collectors.toList());
    } else if (queryType.getValue() == QueryPublishType.PUBLISH.getValue()) {
      goodsIds = commodityService.queryGoodsIdsByUid(uid, start, limit);
      if (CollectionUtils.isEmpty(goodsIds)) {
        return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
      }
    } else {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
    }

    List<BaseGoods> baseGoods = commodityService.queryGoodsByIds(goodsIds);
    if (CollectionUtils.isEmpty(baseGoods)) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
    }

    List<GoodsSimpleVO> goodsList = NapiGoodsAssembler.toSimpleApiList(baseGoods);

    boolean hasMore = false;
    if (goodsList.size() > limit) {
      hasMore = true;
      goodsList = new ArrayList<>(goodsList.subList(0, limit));
    }
    NapiRespWrapper.ScrollingData<GoodsSimpleVO> scrollingData = new ScrollingData<>(hasMore,
        start + limit, goodsList);

    return new NapiRespWrapper<>(scrollingData);
  }

  @RequestMapping(value = "query/", method = RequestMethod.GET)
  public NapiRespWrapper queryGoods(
      @RequestParam(value = "type", defaultValue = "1") Integer type,
      @RequestParam(value = "start", defaultValue = "0") Integer start,
      @RequestParam(value = "limit", defaultValue = "24") Integer limit,
      @RequestParam(value = "uid", defaultValue = "0") Integer uid) {

    if (type == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "缺少必要参数");
    }

    List<BaseGoods> baseGoodsList = new ArrayList<>();
    if (type == QueryTypeEnum.LASTEST.getValue()) {
      baseGoodsList = commodityService.queryLastedGoods(start, limit + 1);
    } else if (type == QueryTypeEnum.HOTTEST.getValue()) {
      baseGoodsList = commodityService.queryHottest(start, limit + 1);
    } else if (type == QueryTypeEnum.RECOMMEND.getValue()) {
      baseGoodsList = commodityService.queryRecommendGoods(uid, limit + 1);
      Collections.shuffle(baseGoodsList);
    } else if (type == QueryTypeEnum.DONATION.getValue()) {
      baseGoodsList = commodityService.queryDonationGoods(start, limit + 1);
    } else {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
    }

    if (CollectionUtils.isEmpty(baseGoodsList)) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
    }

    List<GoodsSimpleVO> goodsList = NapiGoodsAssembler.toSimpleApiList(baseGoodsList);

    boolean hasMore = false;
    if (goodsList.size() > limit) {
      hasMore = true;
      goodsList = new ArrayList<>(goodsList.subList(0, limit));
    }
    NapiRespWrapper.ScrollingData<GoodsSimpleVO> scrollingData = new ScrollingData<>(hasMore,
        start + limit, goodsList);

    return new NapiRespWrapper<>(scrollingData);
  }

  @RequestMapping(value = "add/", method = RequestMethod.POST)
  public NapiRespWrapper<Integer> add(HttpServletRequest request,
      @RequestParam(value = "user_id") Integer userId,
      @RequestParam(value = "title") String title,
      @RequestParam(value = "descible") String descible,
      @RequestParam(value = "price") Double price,
      @RequestParam(value = "old_price") Double oldPrice,
      @RequestParam(value = "freight") Double freight,
      @RequestParam(value = "image_list") List<String> imageList,
      @RequestParam(value = "publish_address") String publishAddress,
      @RequestParam(value = "category", defaultValue = "5") Integer category,
      @RequestParam(value = "is_donation", defaultValue = "1") Integer isDonation) {

    if (StringUtils.isEmpty(title) || StringUtils.isEmpty(descible) ||
        StringUtils.isEmpty(publishAddress) || CollectionUtils.isEmpty(imageList)) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "缺少必要参数");
    }

    BaseGoods baseGoods = new BaseGoods();
    baseGoods.setPublishUserId(userId);
    baseGoods.setTitle(title);
    baseGoods.setDescible(descible);
    baseGoods.setPrice(price);
    baseGoods.setOldPrice(oldPrice);
    baseGoods.setFreight(freight);
    baseGoods.setCategory(category);
    baseGoods.setPublishAddress(publishAddress);
    baseGoods.setIsDonation(isDonation);
    baseGoods
        .setPhotos(imageList.stream().map(item -> new ImageVO(item)).collect(Collectors.toList()));

    return new NapiRespWrapper<>(commodityService.addGoods(baseGoods));
  }

  @RequestMapping(value = "image/uptoken", method = RequestMethod.GET)
  public Map<String, String> obtainToken() {
    Map<String, String> map = new HashMap<>();
    map.put(QINIU_UPLOAD_TOKEN_NAME, commodityService.generateToken());
    return map;
  }

  @RequestMapping(value = "search/", method = RequestMethod.GET)
  public NapiRespWrapper search(
      @RequestParam(value = "keyword", defaultValue = "") String keyword,
      @RequestParam(value = "start", defaultValue = "0") Integer start,
      @RequestParam(value = "limit", defaultValue = "24") Integer limit) {
    if (StringUtils.isEmpty(keyword)) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "搜索词不能为空");
    }
    // regex, 只保留数字、汉字、字母
    Matcher matcher = Pattern.compile("[^a-zA-Z0-9\\u4e00-\\u9fa5]").matcher(keyword);
    keyword = matcher.replaceAll("").trim();
    if (StringUtils.isEmpty(keyword)) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "搜索词不能只包含特殊字符");
    }

    List<BaseGoods> baseGoods = commodityService.queryGoodsByKeyword(keyword, start, limit + 1);
    if (CollectionUtils.isEmpty(baseGoods)) {
      return new NapiRespWrapper(NapiRespStatus.INVALID_PARAM, "未搜索到结果");
    }

    List<GoodsSimpleVO> goodsList = NapiGoodsAssembler.toSimpleApiList(baseGoods);
    boolean hasMore = false;
    if (goodsList.size() > limit) {
      hasMore = true;
      goodsList = new ArrayList<>(goodsList.subList(0, limit));
    }
    NapiRespWrapper.ScrollingData<GoodsSimpleVO> scrollingData = new ScrollingData<>(hasMore,
        start + limit, goodsList);
    return new NapiRespWrapper<>(scrollingData);
  }

}
