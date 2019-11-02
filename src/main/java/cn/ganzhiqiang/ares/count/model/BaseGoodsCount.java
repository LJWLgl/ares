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
public class BaseGoodsCount {

  private Integer resourceId;
  private Long favoriteCount;
  private Long likeCount;
  private Long commentCount;

}
