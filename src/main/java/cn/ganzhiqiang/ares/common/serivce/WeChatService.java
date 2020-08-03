package cn.ganzhiqiang.ares.common.serivce;

import cn.ganzhiqiang.ares.common.dto.WxAccessTokenDto;
import cn.ganzhiqiang.ares.common.dto.WxSimpleDto;
import cn.ganzhiqiang.ares.common.helper.GConfigHelper;
import cn.ganzhiqiang.ares.common.utils.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ljwlgl.fileutil.FastJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zq_gan
 * @since 2020/8/3
 **/

@Service
public class WeChatService {

    private Logger logger = LoggerFactory.getLogger(WeChatService.class);

    private String GET_ACCESS_TOKEN_URL_PREFIX = "https://api.weixin.qq.com/cgi-bin/token";

    private String MSG_SEC_CHECK_URL_PREFIX = "https://api.weixin.qq.com/wxa/msg_sec_check";

    @Resource
    private GConfigHelper configHelper;
    @Resource
    private ObjectMapper objectMapper;

    public String getAccessToken() {
        Map<String, String> params = new HashMap<>();
        String appId = configHelper.getValueByKey("wx.app.id", String.class);
        String appSecret = configHelper.getValueByKey("wx.app.secret", String.class);
        params.put("grant_type",  "client_credential");
        params.put("appid", appId);
        params.put("secret", appSecret);

        String response = HttpUtil.doGet(GET_ACCESS_TOKEN_URL_PREFIX, params);
        if (response == null) {
            return null;
        }
        WxAccessTokenDto accessTokenDto = null;
        try {
            accessTokenDto = objectMapper.readValue(response, WxAccessTokenDto.class);
        } catch (Exception e) {
            logger.error("query access token error", e);
            return null;
        }
        if (accessTokenDto != null && StringUtils.isNotBlank(accessTokenDto.getAccess_token())) {
            return accessTokenDto.getAccess_token();
        }
        return null;
    }

    public Integer msgSecCheck(String content) {
        String accessToken = getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("access_token", accessToken);
        Map<String, String> params = new HashMap<>();
        params.put("content", content);
        String response = HttpUtil.doPost(MSG_SEC_CHECK_URL_PREFIX, urlParams, FastJsonUtil.toJsonString(params));
        if (response == null) {
            return null;
        }
        WxSimpleDto wxSimpleDto = null;
        try {
            wxSimpleDto = objectMapper.readValue(response, WxSimpleDto.class);
        } catch (Exception e) {
            logger.error("query access token error", e);
            return null;
        }
        if (wxSimpleDto == null || wxSimpleDto.getErrcode() == null) {
            return null;
        }
        return wxSimpleDto.getErrcode();
    }
}
