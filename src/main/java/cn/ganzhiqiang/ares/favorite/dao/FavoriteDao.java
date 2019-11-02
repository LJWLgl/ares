package cn.ganzhiqiang.ares.favorite.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import cn.ganzhiqiang.ares.favorite.domain.FavoriteDO;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nanxuan
 * @since 2018/2/18
 **/

@Repository
public class FavoriteDao {

  private static final String MAPPING_PREFIX = "favorite.";

  @Resource
  private SqlSessionTemplate readTpl;

  @Resource
  private SqlSessionTemplate writeTpl;

  public List<FavoriteDO> queryFavoriteByUid(Integer userId, int start, int limit) {
    Map params = new HashMap();
    params.put("userId", userId);
    params.put("start", start);
    params.put("limit", limit);
    return readTpl.selectList(MAPPING_PREFIX + "queryFavoriteByUserId", params);
  }

  public int insertFavorite(FavoriteDO favoriteDO) {
    int result = writeTpl.insert(MAPPING_PREFIX + "insertFavorite", favoriteDO);
    return result == 0 ? 0 : favoriteDO.getId();
  }

  public FavoriteDO findFavoriteById(Integer favoriteId) {
    Map params = new HashMap();
    params.put("favoriteId", favoriteId);
    return readTpl.selectOne(MAPPING_PREFIX + "findFavoriteById", params);
  }

  public FavoriteDO findFavoriteByResourceId(Integer resourceId, Integer resourceType) {
    Map params = new HashMap();
    params.put("resourceId", resourceId);
    params.put("resourceType", resourceType);
    return readTpl.selectOne(MAPPING_PREFIX + "findFavoriteByResourceId", params);
  }
  public boolean deleteFavorite(Integer favoriteId) {
    writeTpl.delete(MAPPING_PREFIX + "deleteFavorite", favoriteId);
    return true;
  }

}
