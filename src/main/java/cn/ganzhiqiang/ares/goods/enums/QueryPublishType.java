package cn.ganzhiqiang.ares.goods.enums;

public enum QueryPublishType {

  PUBLISH(1), // 我的发布
  LIKE(3), // 我的喜欢
  FAVORITE(4), // 我的收藏
  UNKNOWN(5);

  private int value;

  QueryPublishType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static QueryPublishType of (int type) {
    for (QueryPublishType item : QueryPublishType.values()) {
      if (item.value == type) {
        return item;
      }
    }
    return UNKNOWN;
  }

}
