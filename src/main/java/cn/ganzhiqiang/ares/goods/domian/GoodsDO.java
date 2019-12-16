package cn.ganzhiqiang.ares.goods.domian;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import cn.ganzhiqiang.ares.common.BasePOJO;

import java.util.Date;

/**
 * @author nanxuan
 * @since 2017/12/10
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDO extends BasePOJO {

    private Integer id;
    private Integer publishUserId;
    private String title;
    private String descible;
    private Double price;
    private Double oldPrice;
    private Double freight;
    private Integer category;
    private String publishAddress;
    private Integer isDonation;
    private Integer status;
    private Long lookCount;
    private Date creatDatetime;
    private Date gmtUpdated;

}
