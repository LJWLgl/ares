package cn.ganzhiqiang.ares.like.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ganzhiqiang.ares.common.enums.NapiRespStatus;
import cn.ganzhiqiang.ares.common.warpper.NapiRespWrapper;
import cn.ganzhiqiang.ares.common.enums.TypeEnum;
import cn.ganzhiqiang.ares.like.service.LikeService;

import javax.annotation.Resource;

/**
 * @author nanxuan
 * @since 2018/2/19
 **/

@Controller
@ResponseBody
public class LikeController {

  @Resource
  private LikeService likeService;

  @RequestMapping(value = "like/", method = RequestMethod.GET)
  public NapiRespWrapper<Integer> like(
      @RequestParam(value = "user_id") Integer userId,
      @RequestParam(value = "resource_id") Integer resourceId,
      @RequestParam(value = "resource_type") Integer resourceType) {
    if (userId == null || resourceId == null || resourceType == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "缺少必要参数");
    }
    TypeEnum type = TypeEnum.of(resourceType);
    return new NapiRespWrapper<>(likeService.like(userId, resourceId, type));
  }

  @RequestMapping(value = "unlike/", method = RequestMethod.GET)
  public NapiRespWrapper<Boolean> unlike(@RequestParam(value = "like_id") Integer likeId) {
    if (likeId == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "缺少必要参数");
    }
    return new NapiRespWrapper<>(likeService.unLike(likeId));
  }

}
