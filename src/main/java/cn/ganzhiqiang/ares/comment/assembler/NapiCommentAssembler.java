package cn.ganzhiqiang.ares.comment.assembler;

import cn.ganzhiqiang.ares.comment.model.BaseComment;
import cn.ganzhiqiang.ares.comment.vo.CommentVO;
import cn.ganzhiqiang.ares.common.utils.DateUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author nanxuan
 * @since 2018/2/12
 **/

public class NapiCommentAssembler {

    public static CommentVO toVO(BaseComment baseComment) {
        if (baseComment == null) {
            return null;
        }
        return CommentVO.builder()
                .id(baseComment.getId())
                .userBase(baseComment.getUserBase())
                .content(baseComment.getContent())
                .commentDate(DateUtil.standardNapiDate(baseComment.getGmtCreate()))
                .build();
    }

    public static List<CommentVO> toVOList(List<BaseComment> baseComments) {
        return baseComments.stream()
                .map(NapiCommentAssembler::toVO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
