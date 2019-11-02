package cn.ganzhiqiang.ares.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nanxuan
 * @since 2018/2/5
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBase {

  private Integer id;

  private String nickname;

  private Integer gender; // 0 女性， 1 男性

  private String avatar;

  private String address;

}
