package cn.ganzhiqiang.ares.goods.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author nanxuan
 * @since 2017/12/16
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsSimpleVO {
  
  private Integer id;

  @JsonProperty(value = "publish_user_id")
  private Integer publishUserId;

  private String title;

  private String descible;

  private ImageVO cover;

  private String price;

  @JsonProperty(value = "publish_address")
  private String publishAddress;

  @JsonProperty(value = "is_donation")
  private Integer isDonation;

  @JsonProperty(value = "publish_date")
  private String publishDate;

  @JsonProperty(value = "look_count")
  private Long lookCount;

  private List<String> tags;
}
