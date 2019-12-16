package cn.ganzhiqiang.ares.people.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.ganzhiqiang.ares.common.UserBase;
import cn.ganzhiqiang.ares.people.enums.UserResultEnum;

import javax.annotation.Resource;

import java.util.regex.Pattern;

/**
 * @author nanxuan
 * @since 2018/5/12
 **/

@Service
public class ManagerUserService {

    @Resource
    private PersonService personService;
    // 正则表达式：验证手机号
    public static final String REGEX_TELEPHONE = "^(1[0-9])\\d{9}$";
    // 正则表达式：验证邮箱
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    public UserResultEnum checkTelephone(String telephone) {
        if (StringUtils.isEmpty(telephone) || !Pattern.matches(REGEX_TELEPHONE, telephone)) {
            return UserResultEnum.TELEPHONE_ILLEGALLY;
        }
        UserBase userBase = personService.findUserByTelephone(telephone);
        if (userBase != null) {
            return UserResultEnum.TELEPHONE_ALREADY_REGISTER;
        }
        return UserResultEnum.SUCCESS;
    }

    public UserResultEnum checkEmail(String email) {
        if (StringUtils.isEmpty(email) || !Pattern.matches(REGEX_EMAIL, email)) {
            return UserResultEnum.EMAIL_ILLEGALLY;
        }
        UserBase userBase = personService.findUserByEmail(email);
        if (userBase != null) {
            return UserResultEnum.CAN_FIND_USER_BY_EMAIL;
        }
        return UserResultEnum.SUCCESS;
    }

//  public static void main(String[] args) {
//    System.out.println(Pattern.matches(REGEX_TELEPHONE, "17701762115"));
//    System.out.println(Pattern.matches(REGEX_TELEPHONE, "15255362527"));
//    System.out.println(Pattern.matches(REGEX_TELEPHONE, "15255332768"));
//    System.out.println(Pattern.matches(REGEX_TELEPHONE, "18124183569"));
//  }

}
