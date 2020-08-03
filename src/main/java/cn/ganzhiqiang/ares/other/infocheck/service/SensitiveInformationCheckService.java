package cn.ganzhiqiang.ares.other.infocheck.service;

import cn.ganzhiqiang.ares.common.serivce.WeChatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zq_gan
 * @since 2020/8/3
 **/

@Service
public class SensitiveInformationCheckService {

    @Resource
    private WeChatService weChatService;

    public boolean checkText(String content) {
        String token = weChatService.getAccessToken();
        return false;
    }



}
