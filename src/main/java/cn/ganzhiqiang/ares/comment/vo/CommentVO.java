package cn.ganzhiqiang.ares.comment.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import cn.ganzhiqiang.ares.common.UserBase;
import cn.ganzhiqiang.ares.goods.vo.ImageVO;

import java.util.Date;

/**
 * @author nanxuan
 * @since 2018/2/12
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {

    private Integer id;

    @JsonProperty(value = "user_base")
    private UserBase userBase;

    private String content;

    @JsonProperty(value = "comment_date")
    private String commentDate;
}
