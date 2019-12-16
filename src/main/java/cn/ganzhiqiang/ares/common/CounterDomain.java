package cn.ganzhiqiang.ares.common;

public enum CounterDomain {

    GOODS(1),
    USER(2);

    private int value;

    CounterDomain(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
