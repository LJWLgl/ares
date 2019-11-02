package cn.ganzhiqiang.ares.sight.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ganzhiqiang.ares.common.enums.NapiRespStatus;
import cn.ganzhiqiang.ares.common.warpper.NapiRespWrapper;
import cn.ganzhiqiang.ares.sight.service.SightService;

import javax.annotation.Resource;

/**
 * @author zq_gan
 * @since 2019/10/19
 **/
@Controller
@RequestMapping("sight")
@ResponseBody
public class SightController {

  @Resource
  private SightService sightService;

  @RequestMapping(value = "getLinkById/", method = RequestMethod.GET)
  public NapiRespWrapper<String> getLinkById(@RequestParam(value = "sightId") Long sightId) {
    if (sightId == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
    }
    return new NapiRespWrapper<>(sightService.getByLink(sightId));
  }

}
