package cn.ganzhiqiang.ares.staticdata.service;

import org.springframework.stereotype.Service;

import cn.ganzhiqiang.ares.staticdata.dao.StaticDataDao;
import cn.ganzhiqiang.ares.staticdata.domain.SchoolDO;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author nanxuan
 * @since 2018/2/25
 **/

@Service
public class StaticDataService {

  @Resource
  private StaticDataDao staticDataDao;

  public Map<Integer, String> querySchoolMap() {
    List<SchoolDO> schoolDOS = staticDataDao.querySchool();
    return schoolDOS.stream()
        .filter(Objects::nonNull)
        .filter(item -> Objects.nonNull(item.getName()))
        .collect(Collectors.toMap(SchoolDO::getId, SchoolDO::getName));
  }

  public List<String> querySchool() {
    List<SchoolDO> schoolDOS = staticDataDao.querySchool();
    return schoolDOS.stream()
        .map(SchoolDO::getName)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
