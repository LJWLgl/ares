package cn.ganzhiqiang.ares.admin.domain;

import cn.ganzhiqiang.ares.goods.vo.ImageVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zq_gan
 * @since 2019/12/22
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminGoodsVO {

    private Integer goodsId;

    private Integer publishUserId;

    private String goodsTitle;

    private String goodsDescribe;

    private ImageVO cover;

    private String goodsPrice;

    private String publishAddress;

    private Integer isDonation;

    private String publishDate;

    private Long lookCount;

    private List<String> tags;

}
