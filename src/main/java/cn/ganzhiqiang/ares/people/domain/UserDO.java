package cn.ganzhiqiang.ares.people.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author nanxuan
 * @since 2018/1/28
 **/

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDO {

  private Integer id;
  private String openId;
  private String unionId;
  private String telephone;
  private String email;
  private Integer status;
  private Integer isAuth;
  private Date creatDateTime;
  private Date gmtUpdated;

}
