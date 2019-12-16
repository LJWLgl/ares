package cn.ganzhiqiang.ares.common.enums;

/**
 * @author zq_gan
 * @since 2019/12/15
 **/

public enum StatusEnum {

    PUBLISH(0),   // 已发布
    DELETE(5),    // 已删除
    REVIEW(6),    // 审核中
    DRAFT(10);    // 草稿

    private int status;

    StatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
