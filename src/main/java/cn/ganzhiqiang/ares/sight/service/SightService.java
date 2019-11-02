package cn.ganzhiqiang.ares.sight.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.ganzhiqiang.ares.common.utils.FileUtil;
import cn.ganzhiqiang.ares.sight.domain.SightDo;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zq_gan
 * @since 2019/10/19
 **/

@Service
public class SightService {

  public String getByLink(Long sightId) {
    List<SightDo> sightList = FileUtil.readJsonList("sight-link-config.json", SightDo.class);
    if (CollectionUtils.isEmpty(sightList)) {
      return null;
    }
    Map<Long, SightDo> sightDoMap = sightList.stream().collect(Collectors.toMap(SightDo::getId, Function.identity()));
    return sightDoMap.get(sightId).getLink();
  }

}
