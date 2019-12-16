package cn.ganzhiqiang.ares.goods.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import cn.ganzhiqiang.ares.people.dto.UserInfoDTO;
import cn.ganzhiqiang.ares.people.dto.UserVO;

import java.util.List;

/**
 * @author nanxuan
 * @since 2018/2/4
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class
GoodsVO {

    private Integer id;

    @JsonProperty(value = "user_vo")
    private UserVO userVO;

    private String title;

    private String descible;

    private List<ImageVO> photos;

    private Double price;

    @JsonProperty(value = "old_price")
    private Double oldPrice;

    private Double freight;

    @JsonProperty(value = "publish_address")
    private String publishAddress;

    @JsonProperty(value = "is_donation")
    private Integer isDonation;

    @JsonProperty(value = "publish_date")
    private String publishDate;

    @JsonProperty(value = "look_count")
    private Long lookCount; // 商品浏览数

    @JsonProperty(value = "comment_count")
    private Long commentCount; // 评论数

    @JsonProperty(value = "like_count")
    private Long likeCount;

    @JsonProperty(value = "favorite_count")
    private Long favoriteCount;

    private List<String> tags;

    private Integer likeId;

    private Integer favoriteId;

}
