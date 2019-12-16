package cn.ganzhiqiang.ares.like.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.ganzhiqiang.ares.common.enums.TypeEnum;
import cn.ganzhiqiang.ares.count.service.CounterService;
import cn.ganzhiqiang.ares.like.dao.LikeDao;
import cn.ganzhiqiang.ares.like.domain.LikeDO;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nanxuan
 * @since 2018/2/11
 **/

@Service
public class LikeService {

    @Resource
    private LikeDao likeDao;

    @Resource
    private CounterService counterService;

    public List<LikeDO> queryLikeByUid(Integer userId, int start, int limit) {
        List<LikeDO> likes = likeDao.queryLikeByUid(userId, start, limit);
        if (CollectionUtils.isEmpty(likes)) {
            return new ArrayList<>();
        }
        return likes;
    }

    public LikeDO findLikeByResourceId(Integer resourceId, TypeEnum type) {
        return likeDao.findLikeByResourceId(resourceId, type.getValue());
    }

    public Integer like(Integer uid, Integer resourceId, TypeEnum type) {

        counterService.incrCount(resourceId, 2);
        counterService.incrCount(uid, 5);

        LikeDO likeDO = new LikeDO();
        likeDO.setUserId(uid);
        likeDO.setResourceId(resourceId);
        likeDO.setResourceType(type.getValue());
        return likeDao.insertLike(likeDO);
    }

    public boolean unLike(Integer likeId) {
        LikeDO likeDO = likeDao.findLikeById(likeId);
        if (likeDO == null) {
            return false;
        }
        counterService.decrCount(likeDO.getResourceId(), 2);
        counterService.decrCount(likeDO.getUserId(), 5);
        likeDao.deleteLike(likeId);
        return true;
    }

}
