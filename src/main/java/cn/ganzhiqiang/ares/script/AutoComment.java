package cn.ganzhiqiang.ares.script;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.ganzhiqiang.ares.comment.service.CommentService;
import cn.ganzhiqiang.ares.common.warpper.NapiRespWrapper;
import cn.ganzhiqiang.ares.common.enums.TypeEnum;
import cn.ganzhiqiang.ares.goods.model.BaseGoods;
import cn.ganzhiqiang.ares.goods.service.CommodityService;
import cn.ganzhiqiang.ares.people.service.PersonService;

import javax.annotation.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author nanxuan
 * @since 2018/4/11
 **/

@RestController
public class AutoComment {

  private static String PATH_DATA = "/gzq/data/commment.txt";

  @Resource
  private CommentService commentService;

  @Resource
  private CommodityService commodityService;

  @Resource
  private PersonService personService;

  @RequestMapping(value = "script/comment/")
  public NapiRespWrapper<String> autoComment() {
    int maxUid = personService.findMaxId();
    int minUid = personService.findMinId();
    Random random = new Random();
    int randomUid = random.nextInt(maxUid);
    int uid = randomUid <= minUid ? minUid : randomUid;
    while (personService.findUserDetailByUid(uid) == null) {
      randomUid = random.nextInt(maxUid);
      uid = randomUid <= minUid ? minUid :randomUid;
    }

    int goodsMinId = commodityService.findMinId();
    int goodsMaxId = commodityService.findMaxId();
    int goodsRandomId = random.nextInt(goodsMaxId);
    int goodsId = goodsRandomId <= goodsMinId ? goodsMinId : goodsRandomId;
    BaseGoods goods = commodityService.findGoods(goodsId);
    while ( goods == null || goods.getPublishUserId() == uid) {
      goodsRandomId = random.nextInt(goodsMaxId);
      goodsId = goodsRandomId <= goodsMinId ? goodsMinId : goodsRandomId;
      goods = commodityService.findGoods(goodsId);
    }


    String comment = orderComment();
    if (StringUtils.isEmpty(comment)) {
      return new NapiRespWrapper<>("failed, comment is null");
    }
    comment = comment.replace("${}", goods.getTitle());
    commentService.insertComment(uid, comment, goodsId, TypeEnum.GOODS.getValue());

    return new NapiRespWrapper<>("success, goods id :" + String.valueOf(goodsId));
  }

  private String orderComment() {
    List<String> commmets = new ArrayList<>();
    File file = new File(PATH_DATA);
    try {
      InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
      BufferedReader br = new BufferedReader(reader);
      String line = "";
      line = br.readLine();
      while (line != null) {
        commmets.add(line);
        line = br.readLine();
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    if (CollectionUtils.isEmpty(commmets)) {
      return null;
    }
    Random random = new Random();
    return commmets.get(random.nextInt(commmets.size()));
  }
}
