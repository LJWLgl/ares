package cn.ganzhiqiang.ares.comment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import cn.ganzhiqiang.ares.common.UserBase;

import java.util.Date;

/**
 * @author nanxuan
 * @since 2018/2/12
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseComment {

    private Integer id;

    private UserBase userBase;

    private String content;

    private Integer resourceId;

    private Integer resourceType;

    private Date gmtCreate;

}
