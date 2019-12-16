package cn.ganzhiqiang.ares.goods.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import cn.ganzhiqiang.ares.count.model.BaseGoodsCount;
import cn.ganzhiqiang.ares.goods.vo.ImageVO;

import java.util.Date;
import java.util.List;

/**
 * @author nanxuan
 * @since 2018/1/31
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseGoods {

    private Integer id;
    private Integer publishUserId;
    private String title;
    private String descible;
    private List<ImageVO> photos;
    private Double price;
    private Double oldPrice;
    private Double freight;
    private Integer category;
    private String publishAddress;
    private Integer isDonation;
    private Integer status;
    private Long lookCount;
    private BaseGoodsCount baseGoodsCount;
    private Integer likeId;
    private Integer favoriteId;
    private Date creatDatetime;
    private Date gmtUpdated;

}
