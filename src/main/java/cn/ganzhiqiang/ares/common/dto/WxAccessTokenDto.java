package cn.ganzhiqiang.ares.common.dto;

import lombok.Data;

/**
 * @author zq_gan
 * @since 2020/8/3
 **/

@Data
public class WxAccessTokenDto {

    private String access_token;

    private Integer expires_in;

    private Integer errcode;

    private String errmsg;

}
