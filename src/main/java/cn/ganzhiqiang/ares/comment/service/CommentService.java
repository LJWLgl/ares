package cn.ganzhiqiang.ares.comment.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.ganzhiqiang.ares.comment.assembler.BaseCommentAssembler;
import cn.ganzhiqiang.ares.comment.dao.CommentDao;
import cn.ganzhiqiang.ares.comment.domain.CommentDO;
import cn.ganzhiqiang.ares.comment.model.BaseComment;
import cn.ganzhiqiang.ares.common.enums.TypeEnum;
import cn.ganzhiqiang.ares.common.UserBase;
import cn.ganzhiqiang.ares.count.service.CounterService;
import cn.ganzhiqiang.ares.people.service.PersonService;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nanxuan
 * @since 2018/2/12
 **/

@Service
public class CommentService {

  @Resource
  private CommentDao commentDao;

  @Resource
  private PersonService personService;

  @Resource
  private CounterService counterService;

  public List<BaseComment> queryCommentsByResourceIds(Integer resourceId, TypeEnum type, int start , int limit) {
    List<CommentDO> commentDOS = commentDao.queryCommentByResourceId(resourceId, type);
    if (CollectionUtils.isEmpty(commentDOS)) {
      return new ArrayList<>();
    }
    List<Integer> userIds = commentDOS.stream().map(CommentDO::getUserId).collect(Collectors.toList());
    List<UserBase> userBases = personService.queryUserBaseByIds(userIds);
    if (CollectionUtils.isEmpty(userBases)) {
      return new ArrayList<>();
    }
    return BaseCommentAssembler.toBaseList(commentDOS, userBases);
  }

  public int insertComment(Integer userId, String content, Integer resourceId, Integer type) {
    if (userId == null || content == null || resourceId == null) {
      return 0;
    }

    counterService.incrCount(resourceId, 1);

    CommentDO commentDO = new CommentDO();
    commentDO.setUserId(userId);
    commentDO.setContent(content);
    commentDO.setResourceId(resourceId);
    commentDO.setResourceType(type);
    return commentDao.insertComment(commentDO);
  }
}
