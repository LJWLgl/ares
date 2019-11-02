package cn.ganzhiqiang.ares.inform.service;


import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import cn.ganzhiqiang.ares.common.UserBase;
import cn.ganzhiqiang.ares.inform.cache.InformCache;
import cn.ganzhiqiang.ares.common.serivce.EmailSendService;
import cn.ganzhiqiang.ares.people.enums.UserResultEnum;
import cn.ganzhiqiang.ares.people.service.PersonService;
import cn.ganzhiqiang.ares.qiniu.util.Base64;

import javax.annotation.Resource;
import javax.mail.MessagingException;

/**
 * @author nanxuan
 * @since 2018/5/13
 **/

@Service
public class EmailService {

  private final String EMAIL_BIND_TEMPLATE = "尊敬的%s，请点击以下的链接%s，进行邮箱验证，链接的有效期为24小时";

  private final String CHECK_EMAIL_URL = "https://ganzhiqiang.wang/people/check/email?t=%s&e=%s&i=%s";

  private final String EMAIL_URL_PATTERN = "emailurl:%s";

  private final long EMAIL_URL_TTL = 10 * 60 * 1000L; // 10分钟

  @Resource
  private EmailSendService emailSendService;
  @Resource
  private PersonService personService;
  @Resource
  private InformCache informCache;

  public UserResultEnum sendCheckEmail(String uid, String email) {
    String token = informCache.get(EMAIL_URL_PATTERN, uid);
    if (token != null) {
      return UserResultEnum.EMAIL_URL_HAS_SENT;
    }
    token = creatToken();
    email = Base64.encodeToString(email.getBytes(), Base64.DEFAULT);
    uid = Base64.encodeToString(uid.getBytes(), Base64.DEFAULT);
    
    String checkUrl = String.format(CHECK_EMAIL_URL, token, email, uid);
    UserBase userBase = personService.findUserByEmail(email);
    String sendInfo = String.format(EMAIL_BIND_TEMPLATE, userBase.getNickname(), checkUrl);
    sendEmail("邮件验证", email, sendInfo, false);
    informCache.put(EMAIL_URL_PATTERN, uid, token, EMAIL_URL_TTL);
    return UserResultEnum.SUCCESS;
  }

  public UserResultEnum checkEmail(String email, String token) {
    UserBase userBase = personService.findUserByEmail(email);
    String eToken = informCache.get(EMAIL_URL_PATTERN, String.valueOf(userBase.getId()));
    if (! eToken.equals(token)) {
      return UserResultEnum.EMAIL_CODE_CHECK_ERROR;
    }
    return UserResultEnum.SUCCESS;
  }

  private void sendEmail(String subject, String to, String content, boolean isHtml) {
    try {
      emailSendService.subject(subject);
      emailSendService.from("爱易园团队");
      emailSendService.to(to);
      if (isHtml) {
        emailSendService.html(content);
      } else {
        emailSendService.text(content);
      }
      emailSendService.send();
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    String email = "2715815264@qq.com";
    String encode  = Base64.encodeToString(email.getBytes(), Base64.DEFAULT);
    System.out.println(encode);
    System.out.println(new String(Base64.decode(encode, Base64.DEFAULT)));
    System.out.println(creatToken());
  }


  private static String creatToken() {
    return RandomStringUtils.randomAlphanumeric(32);
  }

}
