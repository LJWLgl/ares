package cn.ganzhiqiang.ares.people.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nanxuan
 * @since 2018/1/21
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    private String nickName;

    private String avatarUrl;

    private String gender;

    private String city;

    private String province;

    private String country;

    private String language;

}
