package cn.ganzhiqiang.ares.staticdata.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import cn.ganzhiqiang.ares.staticdata.domain.SchoolDO;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

/**
 * @author nanxuan
 * @since 2018/2/25
 **/

@Repository
public class StaticDataDao {

    private static final String MAPPING_PREFIX = "staticdata.";

    @Resource
    private SqlSessionTemplate readTpl;

    @Resource
    private SqlSessionTemplate writeTpl;

    public List<SchoolDO> querySchool() {
        return readTpl.selectList(MAPPING_PREFIX + "querySchool");
    }
}
