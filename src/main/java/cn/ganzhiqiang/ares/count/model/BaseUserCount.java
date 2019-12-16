package cn.ganzhiqiang.ares.count.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nanxuan
 * @since 2018/2/21
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseUserCount {

    private Long publishCount; // 发布数
    private Long likeCount; // 用户喜欢数
    private Long favoriteCount; // 收藏数

}
