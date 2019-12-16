package cn.ganzhiqiang.ares.comment.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import cn.ganzhiqiang.ares.comment.domain.CommentDO;
import cn.ganzhiqiang.ares.common.CommonStatus;
import cn.ganzhiqiang.ares.common.enums.TypeEnum;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nanxuan
 * @since 2018/2/12
 **/

@Repository
public class CommentDao {

    private static final String MAPPING_PREFIX = "comment.";

    @Resource
    private SqlSessionTemplate readTpl;

    @Resource
    private SqlSessionTemplate writeTpl;

    public int insertComment(CommentDO commentDO) {
        int result = writeTpl.insert(MAPPING_PREFIX + "insertComment", commentDO);
        return result == 0 ? 0 : commentDO.getId();
    }

    public List<CommentDO> queryCommentByResourceId(Integer resourceId, TypeEnum type) {
        Map params = new HashMap();
        params.put("resourceId", resourceId);
        params.put("resourceType", type.getValue());
        return readTpl.selectList(MAPPING_PREFIX + "queryCommentByResourceId", params);
    }

    public boolean deleteComment(Integer commentId) {
        Map params = new HashMap();
        params.put("commentId", commentId);
        params.put("status", CommonStatus.DELETED);
        writeTpl.update(MAPPING_PREFIX + "updateCommentStatus", params);
        return true;
    }

}
