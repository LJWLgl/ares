package cn.ganzhiqiang.ares.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author nanxuan
 * @since 2018/2/11
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDO {

  private Integer id;

  private Integer userId;

  private String content;

  private Integer resourceId;

  private Integer resourceType;

  private Integer status;

  private Date gmtCreate;

}
