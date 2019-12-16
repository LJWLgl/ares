package cn.ganzhiqiang.ares.favorite.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.ganzhiqiang.ares.common.enums.TypeEnum;
import cn.ganzhiqiang.ares.count.service.CounterService;
import cn.ganzhiqiang.ares.favorite.dao.FavoriteDao;
import cn.ganzhiqiang.ares.favorite.domain.FavoriteDO;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nanxuan
 * @since 2018/2/18
 **/

@Service
public class FavoriteService {

    @Resource
    private FavoriteDao favoriteDao;

    @Resource
    private CounterService counterService;

    public List<FavoriteDO> queryFavoriteByUid(Integer userId, int start, int limit) {
        List<FavoriteDO> favoriteDOS = favoriteDao.queryFavoriteByUid(userId, start, limit);
        if (CollectionUtils.isEmpty(favoriteDOS)) {
            return new ArrayList<>();
        }
        return favoriteDOS;
    }

    public FavoriteDO findByResourceId(Integer resourceId, TypeEnum type) {
        return favoriteDao.findFavoriteByResourceId(resourceId, type.getValue());
    }

    public Integer favorite(Integer uid, Integer resourceId, TypeEnum type) {
        // 计数加1
        counterService.incrCount(resourceId, 3);
        counterService.incrCount(uid, 4);

        FavoriteDO favoriteDO = new FavoriteDO();
        favoriteDO.setUserId(uid);
        favoriteDO.setResourceId(resourceId);
        favoriteDO.setResourceType(type.getValue());
        return favoriteDao.insertFavorite(favoriteDO);
    }

    public boolean unfavorite(Integer favoriteId) {
        FavoriteDO favoriteDO = favoriteDao.findFavoriteById(favoriteId);
        if (favoriteDO == null) {
            return false;
        }
        counterService.decrCount(favoriteDO.getResourceId(), 3);
        counterService.decrCount(favoriteDO.getUserId(), 4);
        return favoriteDao.deleteFavorite(favoriteId);
    }

}
