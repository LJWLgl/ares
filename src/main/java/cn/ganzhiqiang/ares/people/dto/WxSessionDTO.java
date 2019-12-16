package cn.ganzhiqiang.ares.people.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nanxuan
 * @since 2018/1/21
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxSessionDTO {
    private String openid;

    @JsonProperty(value = "session_key")
    private String sessionKey;

    private String unionid;
}
