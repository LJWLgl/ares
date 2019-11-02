package cn.ganzhiqiang.ares.common.enums;

public enum TypeEnum {

  USER(1),
  GOODS(2),
  LIKE(3), // 喜欢
  FAVORITE(4), // 收藏
  COMMENT(5), // 评论
  UNKNOWN(0);

  private int value;

  public int getValue() {
    return value;
  }

  TypeEnum(int value) {
    this.value = value;
  }

  public static TypeEnum of (int value) {
    for (TypeEnum type : TypeEnum.values()) {
      if (type.value == value) {
        return type;
      }
    }
    return UNKNOWN;
  }

}
