package cn.ganzhiqiang.ares.inform.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.ganzhiqiang.ares.inform.cache.InformCache;
import cn.ganzhiqiang.ares.people.enums.UserResultEnum;

import javax.annotation.Resource;

/**
 * @author nanxuan
 * @since 2018/5/9
 **/

@Service
public class MsgService {

    private final String VALIDATION_PATTERN = "msgcode:%s";

    private final long VALIDATION_CODE_TTL = 10 * 60 * 1000L; // 10分钟

    private final String VALIDATION_CODE_LIMITED_PATTERN = "msgcode.limit:%s";

    private final int VALIDATION_CODE_LIMITED_COUNT = 5; // 验证次数上限

    private final long VALIDATION_CODE_LIMITED_TTL = 60 * 60 * 1000L; // 若超过验证次数上限，限制1小时

    @Resource
    private InformCache informCache;

    public UserResultEnum sendMsgCode(String uid, String phoneNumber) {
        int sendCount = informCache.getCount(VALIDATION_CODE_LIMITED_PATTERN, uid);
        if (sendCount > VALIDATION_CODE_LIMITED_COUNT) {
            return UserResultEnum.VALIDATION_SEND_LIMITED_WITH_INTERVAL;
        }
        informCache.inCrCount(VALIDATION_CODE_LIMITED_PATTERN, uid, VALIDATION_CODE_LIMITED_TTL);
        String code = informCache.get(VALIDATION_PATTERN, uid);
        if (StringUtils.isNotEmpty(code)) {
            return UserResultEnum.VALIDATION_CODE_HAS_SENT;
        }
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        final String product = "Dysmsapi";
        final String domain = "dysmsapi.aliyuncs.com";

        final String accessKeyId = "LTAI5slWfGkQjEIt";
        final String accessKeySecret = "kp3lN6V8LlaBFE4o08qrjhNsjKsm53";
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            return UserResultEnum.SYSTEM_ERROR;
        }
        String msgCode = createRandomMsgCode();
        String templateParam = "{\"code\":\"" + msgCode + "\"}";
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(phoneNumber);
        request.setSignName("爱易园");
        request.setTemplateCode("SMS_134315817");
        request.setTemplateParam(templateParam);

        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            // 验证次数+1
        } catch (ClientException e) {
            return UserResultEnum.SYSTEM_ERROR;
        }
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            return UserResultEnum.VALIDATION_CODE_HAS_SENT;
        }
        return UserResultEnum.SUCCESS;
    }

    public UserResultEnum checkMsgCode(String uid, String vCode) {
        String code = informCache.get(VALIDATION_PATTERN, uid);
        if (StringUtils.isEmpty(code) || !code.equals(vCode.trim())) {
            return UserResultEnum.SMS_CODE_CHECK_ERROR;
        }
        return UserResultEnum.SUCCESS;
    }

    // 随机生成六位短信验证码
    private String createRandomMsgCode() {
        String msgCode = "";
        for (int i = 0; i < 6; i++) {
            msgCode += (int) (Math.random() * 9);
        }
        return msgCode;
    }

}
