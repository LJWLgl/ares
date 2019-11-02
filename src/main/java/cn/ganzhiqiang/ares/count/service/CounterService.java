package cn.ganzhiqiang.ares.count.service;

import org.springframework.stereotype.Service;

import cn.ganzhiqiang.ares.comment.cache.CommentCounter;
import cn.ganzhiqiang.ares.common.CounterDomain;
import cn.ganzhiqiang.ares.count.model.BaseGoodsCount;
import cn.ganzhiqiang.ares.count.model.BaseUserCount;
import cn.ganzhiqiang.ares.favorite.cache.FavoriteCount;
import cn.ganzhiqiang.ares.like.cache.LikeCounter;
import cn.ganzhiqiang.ares.people.cache.PeopleFavoriteCounter;
import cn.ganzhiqiang.ares.people.cache.PeopleLikeCounter;
import cn.ganzhiqiang.ares.people.cache.PublishCounter;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nanxuan
 * @since 2018/2/21
 **/

@Service
public class CounterService {

  @Resource
  private CommentCounter commentCounter;

  @Resource
  private LikeCounter likeCounter;

  @Resource
  private FavoriteCount favoriteCount;

  @Resource
  private PeopleFavoriteCounter peopleFavoriteCounter;

  @Resource
  private PeopleLikeCounter peopleLikeCounter;

  @Resource
  private PublishCounter publishCounter;

  public void incrCount(Integer resourceId, int type) {
    if (type == 1) {
      commentCounter.incrCount(resourceId, CounterDomain.GOODS);
    } else if (type == 2) {
      likeCounter.incrCount(resourceId, CounterDomain.GOODS);
    } else if (type == 3) {
      favoriteCount.incrCount(resourceId, CounterDomain.GOODS);
    } else if (type == 4) {
      peopleFavoriteCounter.incrCount(resourceId, CounterDomain.USER);
    } else if (type == 5) {
      peopleLikeCounter.incrCount(resourceId, CounterDomain.USER);
    } else if (type == 6) {
      publishCounter.incrCount(resourceId, CounterDomain.USER);
    }
  }

  public void decrCount(Integer resourceId, int type) {
    if (type == 1) {
      commentCounter.decrCount(resourceId, CounterDomain.GOODS);
    } else if (type == 2) {
      likeCounter.decrCount(resourceId, CounterDomain.GOODS);
    } else if (type == 3) {
      favoriteCount.decrCount(resourceId, CounterDomain.GOODS);
    } else if (type == 4) {
      peopleFavoriteCounter.decrCount(resourceId, CounterDomain.USER);
    } else if (type == 5) {
      peopleLikeCounter.decrCount(resourceId, CounterDomain.USER);
    } else if (type == 6) {
      publishCounter.decrCount(resourceId, CounterDomain.USER);
    }
  }

  public Map<Integer, Long> queryCount(List<Integer> resourceIds, int type) {
    if (type == 1) {
      return commentCounter.queryCount(resourceIds, CounterDomain.GOODS);
    } else if (type == 2) {
      return likeCounter.queryCount(resourceIds, CounterDomain.GOODS);
    } else if (type == 3) {
      return favoriteCount.queryCount(resourceIds, CounterDomain.GOODS);
    } else if (type == 4) {
      return peopleFavoriteCounter.queryCount(resourceIds, CounterDomain.USER);
    } else if (type == 5) {
      return peopleLikeCounter.queryCount(resourceIds, CounterDomain.USER);
    } else if (type == 6) {
      return publishCounter.queryCount(resourceIds, CounterDomain.USER);
    }
    return new HashMap<>();
  }

  public Long findCount(Integer resourceId, int type) {
    if (type == 1) {
      return commentCounter.findCount(resourceId, CounterDomain.GOODS);
    } else if (type == 2) {
      return likeCounter.findCount(resourceId, CounterDomain.GOODS);
    } else if (type == 3) {
      return favoriteCount.findCount(resourceId, CounterDomain.GOODS);
    } else if (type == 4) {
      return peopleFavoriteCounter.findCount(resourceId, CounterDomain.USER);
    } else if (type == 5) {
      return peopleLikeCounter.findCount(resourceId, CounterDomain.USER);
    } else if (type == 6) {
      return publishCounter.findCount(resourceId, CounterDomain.USER);
    }
    return 0L;
  }

  public BaseGoodsCount findGoodsCount(Integer resourceId) {
    return BaseGoodsCount.builder()
        .resourceId(resourceId)
        .commentCount(findCount(resourceId, 1))
        .likeCount(findCount(resourceId, 2))
        .favoriteCount(findCount(resourceId, 3))
        .build();
  }

  public List<BaseGoodsCount> queryGoodsCount(List<Integer> resourceIds) {

    Map<Integer, Long> commentCountMap = queryCount(resourceIds, 1);
    Map<Integer, Long> likeCountMap = queryCount(resourceIds, 2);
    Map<Integer, Long> favoriteCountMap = queryCount(resourceIds, 3);

    return resourceIds.stream().map(item -> {
      return BaseGoodsCount.builder()
          .resourceId(item)
          .commentCount(commentCountMap.get(item))
          .likeCount(likeCountMap.get(item))
          .favoriteCount(favoriteCountMap.get(item))
          .build();
    }).collect(Collectors.toList());
  }

  public BaseUserCount findUserCount(Integer userId) {
    return BaseUserCount.builder()
        .favoriteCount(findCount(userId, 4))
        .likeCount(findCount(userId, 5))
        .publishCount(findCount(userId, 6))
        .build();
  }

}
