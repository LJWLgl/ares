package cn.ganzhiqiang.ares.like.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import cn.ganzhiqiang.ares.like.domain.LikeDO;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nanxuan
 * @since 2018/2/11
 **/

@Repository
public class LikeDao {

  private static final String MAPPING_PREFIX = "like.";

  @Resource
  private SqlSessionTemplate readTpl;

  @Resource
  private SqlSessionTemplate writeTpl;


  public int insertLike(LikeDO likeDO) {
    int result = writeTpl.insert(MAPPING_PREFIX + "insertLike", likeDO);
    return result == 0 ? 0 : likeDO.getId();
  }

  public LikeDO findLikeById(Integer likeId) {
    Map params = new HashMap();
    params.put("likeId", likeId);
    return readTpl.selectOne(MAPPING_PREFIX + "findLikeById", params);
  }

  public LikeDO findLikeByResourceId(Integer resourceId, Integer resourceType) {
    Map params = new HashMap();
    params.put("resourceId", resourceId);
    params.put("resourceType", resourceType);
    return readTpl.selectOne(MAPPING_PREFIX + "findLikeByResourceId", params);
  }

  public List<LikeDO> queryLikeByUid(Integer uid, int start, int limit) {
    Map params = new HashMap();
    params.put("userId", uid);
    params.put("start", start);
    params.put("limit", limit);
    return readTpl.selectList(MAPPING_PREFIX + "queryLikeByUserId", params);
  }

  public int deleteLike(Integer likeId) {
    Map params = new HashMap();
    params.put("likeId", likeId);
    return writeTpl.delete(MAPPING_PREFIX + "deleteLike", params);
  }
}
