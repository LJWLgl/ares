package cn.ganzhiqiang.ares.goods.domian;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import cn.ganzhiqiang.ares.common.BasePOJO;

import java.util.Date;

/**
 * @author nanxuan
 * @since 2018/1/31
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageSimpleDO extends BasePOJO {

    private Integer id;

    private Integer goodsId;

    private String path;

    private String thumPath;

}
