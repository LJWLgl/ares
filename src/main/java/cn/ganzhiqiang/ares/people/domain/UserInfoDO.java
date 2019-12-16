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
public class UserInfoDO {

    private Integer id;
    private String nickName;
    private String avatarUrl;
    private String gender;
    private Integer integral;
    private String city;
    private String province;
    private String country;
    private String language;
    private Integer school;
    private String shipAddress;
    private Date creatDateTime;
    private Date gmtUpdated;

    public UserInfoDO(Integer id) {
        this.id = id;
    }
}
