package cn.ganzhiqiang.ares.goods.enums;

public enum QueryTypeEnum {

    LASTEST(1), // 最新
    HOTTEST(2), // 最热
    RECOMMEND(3), // 推荐
    DONATION(4); // 捐赠

    private int value;

    QueryTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
