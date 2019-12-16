package cn.ganzhiqiang.ares.comment.view;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ganzhiqiang.ares.comment.assembler.NapiCommentAssembler;
import cn.ganzhiqiang.ares.comment.model.BaseComment;
import cn.ganzhiqiang.ares.comment.service.CommentService;
import cn.ganzhiqiang.ares.comment.vo.CommentVO;
import cn.ganzhiqiang.ares.common.enums.NapiRespStatus;
import cn.ganzhiqiang.ares.common.warpper.NapiRespWrapper;
import cn.ganzhiqiang.ares.common.enums.TypeEnum;

import javax.annotation.Resource;

import java.util.List;

/**
 * @author nanxuan
 * @since 2018/2/12
 **/

@RequestMapping("comment")
@Controller
public class CommentController {

    @Resource
    private CommentService commentService;

    @RequestMapping(value = "query/", method = RequestMethod.GET)
    @ResponseBody
    public NapiRespWrapper<List<CommentVO>> queryList(
            @RequestParam(value = "resource_id") Integer resourceId,
            @RequestParam(value = "resource_type", defaultValue = "2") Integer type,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "limit", defaultValue = "24") Integer limit) {

        TypeEnum typeEnum = TypeEnum.of(type);
        List<BaseComment> comments = commentService.queryCommentsByResourceIds(resourceId, typeEnum, start, limit);
        if (CollectionUtils.isEmpty(comments)) {
            return new NapiRespWrapper<>(NapiRespStatus.SUCCESS, "非法参数");
        }
        return new NapiRespWrapper<>(NapiCommentAssembler.toVOList(comments));
    }

    @RequestMapping(value = "add/", method = RequestMethod.POST)
    @ResponseBody
    public NapiRespWrapper<Integer> add(
            @RequestParam(value = "user_id") Integer userId,
            @RequestParam(value = "resource_id") Integer resourceId,
            @RequestParam(value = "resource_type", defaultValue = "2") Integer type,
            @RequestParam(value = "content") String content) {
        Integer commentId = commentService.insertComment(userId, content, resourceId, type);
        return new NapiRespWrapper<>(commentId);
    }

}
