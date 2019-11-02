package cn.ganzhiqiang.ares.people.enums;

public enum UserRoleEnum {

  USER(0, "普通用户"),
  AUTH_USER(1, "认证用户"),
  ADMIN(2, "管理员");

  private int code;
  private String desc;

  UserRoleEnum (int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public int getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

}
