package cn.ganzhiqiang.ares.script;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.ganzhiqiang.ares.common.warpper.NapiRespWrapper;
import cn.ganzhiqiang.ares.goods.model.BaseGoods;
import cn.ganzhiqiang.ares.goods.service.CommodityService;

import javax.annotation.Resource;

import java.util.Random;

/**
 * @author nanxuan
 * @since 2018/4/11
 **/

@RestController
public class AutoLook {

  @Resource
  private CommodityService commodityService;

  @RequestMapping(value = "script/look/")
  public NapiRespWrapper<Integer> autoLook() {

    Random random = new Random();
    int goodsMinId = commodityService.findMinId();
    int goodsMaxId = commodityService.findMaxId();
    int goodsRandomId = random.nextInt(goodsMaxId);
    int goodsId = goodsRandomId <= goodsMinId ? goodsMinId : goodsRandomId;
    BaseGoods goods = commodityService.findGoods(goodsId);
    while ( goods == null) {
      goodsRandomId = random.nextInt(goodsMaxId);
      goodsId = goodsRandomId <= goodsMinId ? goodsMinId : goodsRandomId;
      goods = commodityService.findGoods(goodsId);
    }

    commodityService.incrLookCount(goodsId);
    return new NapiRespWrapper<>(goodsId);
  }
}
