package cn.ganzhiqiang.ares.people.view;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ganzhiqiang.ares.common.enums.NapiRespStatus;
import cn.ganzhiqiang.ares.common.warpper.NapiRespWrapper;
import cn.ganzhiqiang.ares.inform.service.CaptchaService;
import cn.ganzhiqiang.ares.inform.service.EmailService;
import cn.ganzhiqiang.ares.inform.service.MsgService;
import cn.ganzhiqiang.ares.people.dto.UserInfoDTO;
import cn.ganzhiqiang.ares.people.dto.UserVO;
import cn.ganzhiqiang.ares.people.dto.WxSessionDTO;
import cn.ganzhiqiang.ares.people.enums.UserResultEnum;
import cn.ganzhiqiang.ares.people.enums.UserRoleEnum;
import cn.ganzhiqiang.ares.people.service.ManagerUserService;
import cn.ganzhiqiang.ares.people.service.PersonService;
import cn.ganzhiqiang.ares.qiniu.util.Base64;
import cn.ganzhiqiang.ares.staticdata.service.StaticDataService;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.UUID;

/**
 * @author nanxuan
 * @since 2018/1/21
 **/

@Controller
@RequestMapping("people")
@ResponseBody
public class PersonController {

  private Logger logger = LoggerFactory.getLogger(PersonController.class);

  @Resource
  private PersonService personService;
  @Resource
  private StaticDataService staticDataService;
  @Resource
  private ManagerUserService managerUserService;
  @Resource
  private MsgService msgService;
  @Resource
  private EmailService emailService;
  @Resource
  private CaptchaService captchaService;

  @RequestMapping(value = "detail/", method = RequestMethod.GET)
  public NapiRespWrapper<UserVO> detail(@RequestParam(value = "uid") Integer uid) {
    if (uid == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
    }
    UserVO userVO = personService.findUserDetailByUid(uid);
    if (userVO == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
    }
    return new NapiRespWrapper<>(userVO);
  }

  /**
   * login接口，拿到用户的uid
   * @param code 凭证
   * @return uid
   */
  @RequestMapping(value = "login/", method = RequestMethod.GET)
  public NapiRespWrapper<Integer> login(
      @RequestParam(value = "code") String code) {
     if (StringUtils.isEmpty(code) ) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
    }
    // 调用code2Session 接口，拿到用户唯一标识 OpenID 和 会话密钥 session_key。
    WxSessionDTO wxSessionDTO = personService.doRequest(code);
    if (wxSessionDTO == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "未取到Open ID");
    }

    // 生成session 保存到 Redis
    String sessionId = generateSessionId();
    personService.saveSession(sessionId, wxSessionDTO);

    if (! personService.isRegsiter(wxSessionDTO.getOpenid())) {
      return new NapiRespWrapper<>(personService.insert(wxSessionDTO.getOpenid()));
    }
    return new NapiRespWrapper<>(personService.findUserIdByOpenId(wxSessionDTO.getOpenid()));
  }

  @RequestMapping("registerinfo/")
  public NapiRespWrapper<Boolean> registerInfo(@RequestParam Integer uid, @RequestParam(defaultValue = "") String userInfo) {
    UserInfoDTO userInfoDTO = personService.mapperUserInfo(userInfo);
    Assert.notNull(uid, "uid cannot null");
    Assert.state(StringUtils.isNotEmpty(userInfo), "userInfoDto cannot null");
    return new NapiRespWrapper<>(personService.updateUserProfile(uid, userInfoDTO));
  }

  @RequestMapping("register/")
  public NapiRespWrapper<Long> register() {
    return new NapiRespWrapper<>(10L);
  }

  @RequestMapping(value = "school/query/", method = RequestMethod.GET)
  public NapiRespWrapper<List<String>> querySchool() {
    return new NapiRespWrapper<>(staticDataService.querySchool());
  }

  @RequestMapping(value = "school/change/", method = RequestMethod.GET)
  public NapiRespWrapper<Boolean> changeSchool(
      @RequestParam Integer uid,
      @RequestParam Integer index) {
    if (uid == null || index == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "缺少必要参数");
    }
    return new NapiRespWrapper<>(personService.changeSchool(uid, index));
  }

  @RequestMapping(value = "telephone/update/", method = RequestMethod.GET)
  public NapiRespWrapper<Boolean> changeSchool(
      @RequestParam Integer uid,
      @RequestParam String telephone) {
    if (uid == null || telephone == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "缺少必要参数");
    }
    return new NapiRespWrapper<>(personService.changeTelePhone(uid, telephone));
  }

  @RequestMapping(value = "msg/send/",method = RequestMethod.GET)
  public NapiRespWrapper<String> sendMsg(
      @RequestParam String uid,
      @RequestParam String telephone
  ) {
    if (uid == null || StringUtils.isEmpty(telephone)) {
      return new NapiRespWrapper<>("手机号不能为空");
    }
    UserResultEnum restTel = managerUserService.checkTelephone(telephone);
    if (! restTel.equals(UserResultEnum.SUCCESS)) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, restTel.getDesc());
    }
    UserResultEnum  restSend = msgService.sendMsgCode(uid, telephone);
    if (! restSend.equals(UserResultEnum.VALIDATION_CODE_HAS_SENT)) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, restSend.getDesc());
    }
    return new NapiRespWrapper<>("验证码发送成功");
  }

  @RequestMapping(value = "email/send/", method = RequestMethod.GET)
  public NapiRespWrapper<String> sendCheckEmail(
      @RequestParam String uid,
      @RequestParam String email) {
    UserResultEnum result = managerUserService.checkEmail(email);
    if (! result.equals(UserResultEnum.SUCCESS)) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, result.getDesc());
    }
    UserResultEnum checkRest = emailService.sendCheckEmail(uid, email);
    if (! checkRest.equals(UserResultEnum.SUCCESS)) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, checkRest.getDesc());
    }
    return new NapiRespWrapper<>(checkRest.getDesc());
  }

  @RequestMapping(value = "email/check/", method = RequestMethod.GET)
  public NapiRespWrapper<String> checkEmail(
      @RequestParam String i,
      @RequestParam String e,
      @RequestParam String t) {
    if (StringUtils.isNotEmpty(e) || StringUtils.isNotEmpty(t) || StringUtils.isNotEmpty(i)) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
    }
    String email = new String(Base64.decode(e, Base64.DEFAULT));
    String uid = new String(Base64.decode(i, Base64.DEFAULT));

    UserResultEnum result = managerUserService.checkEmail(email);
    if (! result.equals(UserResultEnum.SUCCESS)) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, result.getDesc());
    }
    UserResultEnum checkRest = emailService.checkEmail(email, t);
    if (! checkRest.equals(UserResultEnum.SUCCESS)) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, checkRest.getDesc());
    }
    // 修改邮箱
    personService.updateEmail(Integer.valueOf(uid), email);
    // 用户状态更新认证用户
    personService.updateAuthStatus(Integer.valueOf(uid), UserRoleEnum.AUTH_USER.getCode());
    return new NapiRespWrapper<>("邮箱验证成功");
  }

  @RequestMapping(value = "telephone/bind/", method = RequestMethod.GET)
  public NapiRespWrapper<String> bindTelephone(
      @RequestParam String uid,
      @RequestParam String telephone,
      @RequestParam String vcode) {
    if (uid == null || StringUtils.isEmpty(telephone)) {
      return new NapiRespWrapper<>("手机号不能为空");
    }
    UserResultEnum restTel = managerUserService.checkTelephone(telephone);
    if (! restTel.equals(UserResultEnum.SUCCESS)) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, restTel.getDesc());
    }
    UserResultEnum restVcode = msgService.checkMsgCode(uid, vcode);
    if(! restTel.equals(UserResultEnum.SUCCESS)) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, restVcode.getDesc());
    }
    // 修改手机号
    personService.changeTelePhone(Integer.valueOf(uid), telephone);
    // 用户状态更新认证用户
    personService.updateAuthStatus(Integer.valueOf(uid), UserRoleEnum.AUTH_USER.getCode());
    return new NapiRespWrapper<>("手机号绑定成功");
  }

  @RequestMapping(value = "telephone/order/", method = RequestMethod.GET)
  public NapiRespWrapper<String> orderTelephone(@RequestParam Integer uid) {
    if (uid == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "缺少必要参数");
    }
    UserVO userVO = personService.findUserDetailByUid(uid);
    if (userVO == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "该用户不存在");
    }
    if (StringUtils.isEmpty(userVO.getTelephone())) {
      return new NapiRespWrapper<>(NapiRespStatus.NOT_BINDED, "该用户还未绑定手机号");
    }
    return new NapiRespWrapper<>(NapiRespStatus.SUCCESS, userVO.getTelephone());
  }

  @RequestMapping(value = "address/update/", method = RequestMethod.GET)
  public NapiRespWrapper<Boolean> updateShipAddress(
      @RequestParam Integer uid,
      @RequestParam String shipAddress) {
    if (uid == null || shipAddress == null) {
      return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "缺少必要参数");
    }
    return new NapiRespWrapper<>(personService.updateShipAddress(uid, shipAddress));
  }

  @RequestMapping(value = "captcha/")
  public void captcha(HttpServletRequest request, HttpServletResponse response,
  @RequestParam Integer uid) {
    BufferedImage image = captchaService.genCaptchaImage(response, uid);
    response.setContentType("image/jpeg");
    try {
      //输出图像
      ServletOutputStream outStream = response.getOutputStream();
      ImageIO.write(image, "jpeg", outStream);
      outStream.close();
    } catch (Exception ex) {
    }
  }

  @RequestMapping(value = "captcha/check/")
  public NapiRespWrapper<Boolean> checkCaptcha(@RequestParam String uid, @RequestParam String captchaCode) {
    if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(captchaCode)) {
      return new NapiRespWrapper<>(false);
    }
    boolean result =  captchaService.checkCaptcha(Integer.parseInt(uid), captchaCode);
    return new NapiRespWrapper<>(result);
  }

  @RequestMapping(value = "getValue/")
  public String getValue() {
    return "success";
  }

  private String generateSessionId() {
    return UUID.randomUUID().toString();
  }

}
