package cn.ganzhiqiang.ares.people.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import cn.ganzhiqiang.ares.common.UserBase;

/**
 * @author nanxuan
 * @since 2018/2/5
 **/

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

    @JsonProperty(value = "user_base")
    private UserBase userBase;

    private String telephone;

    private String email;

    private Integer integral; // 积分

    private Integer school; // 1 安徽师范大学

    @JsonProperty(value = "ship_address")
    private String shipAddress;

    @JsonProperty(value = "publish_count")
    private Long publishCount;

    @JsonProperty(value = "like_count")
    private Long likeCount;

    @JsonProperty(value = "favorite_count")
    private Long favoriteCount;

    @JsonProperty(value = "look_count")
    private Long lookCount;

}
