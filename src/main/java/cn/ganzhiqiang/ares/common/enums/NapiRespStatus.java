package cn.ganzhiqiang.ares.common.enums;

public enum NapiRespStatus {

    FAIL(0, "request failed"),

    SUCCESS(1, "success"),

    NOT_LOGIN(2, "require login"),

    INVALID_REQUEST(3, "invalid request"),

    INVALID_PARAM(4, "invalid param"),

    INNER_ERROR(5, "repo busy"),

    NOT_BINDED(6, "not binded"),

    NOT_REGISTERED(7, "not registered"),

    ACCOUNT_ABNORMAL(8, "account abnormal");

    public int apiCode;
    public String commonMsg;

    private NapiRespStatus(int value, String msg) {
        this.apiCode = value;
        this.commonMsg = msg;
    }

}
