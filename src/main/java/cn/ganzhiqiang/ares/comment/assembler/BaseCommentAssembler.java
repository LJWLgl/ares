package cn.ganzhiqiang.ares.comment.assembler;

import org.apache.commons.collections.CollectionUtils;

import cn.ganzhiqiang.ares.comment.domain.CommentDO;
import cn.ganzhiqiang.ares.comment.model.BaseComment;
import cn.ganzhiqiang.ares.common.UserBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author nanxuan
 * @since 2018/2/12
 **/

public class BaseCommentAssembler {

  public static BaseComment toBase(CommentDO commentDO, UserBase userBase) {

    if (commentDO == null || userBase == null) {
      return null;
    }

    return BaseComment.builder()
        .id(commentDO.getId())
        .userBase(userBase)
        .content(commentDO.getContent())
        .resourceId(commentDO.getResourceId())
        .resourceType(commentDO.getResourceType())
        .gmtCreate(commentDO.getGmtCreate())
        .build();
  }

  public static List<BaseComment> toBaseList(List<CommentDO> commentDOS, List<UserBase> userBaseList) {
    if (CollectionUtils.isEmpty(commentDOS) ||
        CollectionUtils.isEmpty(userBaseList)) {
      return new ArrayList<>();
    }

    Map<Integer, UserBase> userMap = userBaseList.stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toMap(UserBase::getId, Function.identity()));

    return commentDOS.stream()
        .map(item -> toBase(item, userMap.get(item.getUserId())))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

//  public static CommentDO toDO(BaseComment baseComment) {
//    if (baseComment == null || baseComment.getUserBase().getId() == null) {
//      return null;
//    }
//    return CommentDO.builder()
//        .userId(baseComment.getUserBase().getId())
//        .content(baseComment.getContent())
//        .resourceId(baseComment.getResourceId())
//        .resourceType(baseComment.getResourceType())
//        .build();
//  }
}
