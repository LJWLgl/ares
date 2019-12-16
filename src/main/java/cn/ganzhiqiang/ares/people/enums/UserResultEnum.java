package cn.ganzhiqiang.ares.people.enums;

public enum UserResultEnum {

    SUCCESS("操作成功"),
    ILLEGAL_PARAMS("参数异常"),
    SYSTEM_ERROR("系统发生异常，请稍后重试"),
    PARAMS_CAN_NOT_BE_NULL("参数不能为空"),
    EXT_SITE_ALREADY_BIND("第三方已和其他堆糖账号绑定过"),
    EXT_SITE_ALREADY_BIND_SINA("抱歉，您的微博已与其他堆糖账号绑定"),
    EXT_SITE_ALREADY_BIND_WEIXIN("抱歉，您的微信已与其他堆糖账号绑定"),
    EXT_SITE_ALREADY_BIND_QQ("抱歉，您的QQ已与其他堆糖账号绑定"),
    CAN_NOT_FIND_USER_BY_EMAIL("该邮箱未注册"),
    CAN_NOT_FIND_PASSWORD_BY_PHONE("该手机不支持找回密码"),
    CAN_NOT_FIND_PASSWORD_BY_EMAIL("该邮箱不支持找回密码"),
    USER_HAS_NO_PASSWORD("账户没有设置密码"),
    USERNAME_IS_NULL("用户名不能为空"),
    USERNAME_BE_HAVE("此用户名已存在"),
    USERNAME_ILLEGALLY_CH("用户名仅支持中文,数字,字母,_与-"),
    USERNAME_ILLEGALLY("您的用户名存在不被支持的字符"),
    USERNAME_LENGTH_ILLEGALLY("用户名长度须在2-15个字符之间"),
    USERNAME_IS_PHONE_NUMBER("不允许使用此类用户名"),
    USERNAME_BE_PROTECTED("不允许使用此用户名"),
    EMAIL_ILLEGALLY("您的邮箱地址非法"),
    TELEPHONE_ILLEGALLY("您的手机号码非法"),
    TELEPHONE_NOT_BIND_YET("您的绑定手机验证失败"),
    TELEPHONE_ALREADY_BIND("手机号码已经被绑定，请更换后重试"),
    TELEPHONE_ALREADY_REGISTER("手机号码已经被注册了, 请更换后重试"),
    CAN_FIND_USER_BY_EMAIL("该邮箱已存在"),
    PASSWORD_IS_NULL("密码不能为空"),
    PASSWORD_LENGTH_ILLEGALLY("密码长度为6~18个字符"),
    SHORT_DESCRIPTION_LENGTH_ILLEGALLY("描述不超过70个字符"),
    REGISTER_ERROR("注册失败"),
    SEND_EMAIL_ERROR("邮件发送失败，请稍后再试"),
    EMAIL_CODE_CHECK_ERROR("邮箱链接验证失败，请重新尝试"),
    EMAIL_URL_HAS_SENT("邮件验证链接已发送, 10分钟内有效"),
    SHORT_MESSAGE_SEND_ERROR("短信发送失败，请重试"),
    SMS_CODE_CHECK_ERROR("短信验证码错误，请重试"),
    VALIDATION_SUCCESS("验证成功"),
    VALIDATION_CODE_CHECK_ERROR("验证码错误，请重试"),
    VALIDATION_CODE_CHECK_ERROR_LIMITED("验证码错误次数过多，请1小时后重试"),
    VALIDATION_TARGET_LOCKED("验证码发送次数过多，请1小时后重试"),
    VALIDATION_CODE_HAS_SENT("验证码已发送，10分钟内有效"),
    VALIDATION_SEND_LIMITED_WITH_INTERVAL("验证码发送频繁，请1 小时稍后再试"),
    VALIDATION_CREATED_FAUILED("验证码创建失败"),
    SMS_LIMIT("请不要频繁操作"),
    USER_NOT_EXIST("用户信息不存在"),
    LOGIN_ERROR("用户名密码有误"),
    LOGIN_ERROR_BY_SITE("未与第三方账号绑定"),
    CCODE_ERROR("您输入的验证码有误");

    String desc;

    private UserResultEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

}
