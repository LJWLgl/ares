package cn.ganzhiqiang.ares.people.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zqgan
 * @since 2019/4/8
 **/

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleDto {

    private Integer id;
    private String telephone;
    private String email;
    private Integer isAuth;

}
