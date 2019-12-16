package cn.ganzhiqiang.ares.inform.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.ganzhiqiang.ares.inform.cache.InformCache;
import cn.ganzhiqiang.ares.common.utils.CaptchaUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;

/**
 * @author nanxuan
 * @since 2018/5/26
 * 图形验证码
 **/

@Service
public class CaptchaService {

    private final String CAPTCHA_PATTERN = "captcha:%s";

    private final long CAPTCHA_URL_TTL = 60 * 60 * 1000L; // 10分钟

    @Resource
    private InformCache informCache;

    public BufferedImage genCaptchaImage(HttpServletResponse response, Integer uid) {
        String code = CaptchaUtil.genCaptcha(5);
        informCache.put(CAPTCHA_PATTERN, String.valueOf(code), code, CAPTCHA_URL_TTL);
        //把校验码转为图像
        BufferedImage image = CaptchaUtil.genCaptchaImg(code);
        return image;
    }

    public boolean checkCaptcha(Integer uid, String code) {
        String value = informCache.get(CAPTCHA_PATTERN, String.valueOf(uid));
        if (StringUtils.isEmpty(value) || !value.equals(code.trim())) {
            return false;
        }
        return true;
    }

}
