package cn.ganzhiqiang.ares.search.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

/**
 * @author zq_gan
 * @since 2019/12/15
 **/

public class GoodsHit {

    @JSONField(name = "id")
    private Integer id;

    @JSONField(name = "title_en")
    private String titleEN;

    @JSONField(name = "title_cn")
    private String titleCN;

    @JSONField(name = "title_py")
    private String titlePY;

    @JSONField(name = "describe_en")
    private String describeEN;

    @JSONField(name = "describe_cn")
    private String describeCN;

    @JSONField(name = "price")
    private Double price;

    @JSONField(name = "status")
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitleEN() {
        return titleEN;
    }

    public void setTitleEN(String titleEN) {
        this.titleEN = titleEN;
    }

    public String getTitleCN() {
        return titleCN;
    }

    public void setTitleCN(String titleCN) {
        this.titleCN = titleCN;
    }

    public String getTitlePY() {
        return titlePY;
    }

    public void setTitlePY(String titlePY) {
        this.titlePY = titlePY;
    }

    public String getDescribeEN() {
        return describeEN;
    }

    public void setDescribeEN(String describeEN) {
        this.describeEN = describeEN;
    }

    public String getDescribeCN() {
        return describeCN;
    }

    public void setDescribeCN(String describeCN) {
        this.describeCN = describeCN;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
