package cn.ganzhiqiang.ares.admin.assembler;

import cn.ganzhiqiang.ares.admin.domain.AdminGoodsVO;
import cn.ganzhiqiang.ares.common.Assembler;
import cn.ganzhiqiang.ares.goods.model.BaseGoods;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zq_gan
 * @since 2019/12/22
 **/

public class NapiAdminGoodsAssembler{

    public static AdminGoodsVO toVo(BaseGoods baseGoods) {
        if (baseGoods == null) {
            return null;
        }
        return AdminGoodsVO.builder()
                .goodsId(baseGoods.getId())
                .goodsTitle(baseGoods.getTitle())
                .goodsDescribe(baseGoods.getDescible())
                .goodsPrice(String.valueOf(baseGoods.getPrice()))
                .build();
    }

    public static List<AdminGoodsVO> toVos(List<BaseGoods> baseGoodsList) {
        if (CollectionUtils.isEmpty(baseGoodsList)) {
            return Collections.emptyList();
        }
        return baseGoodsList.stream()
                .map(NapiAdminGoodsAssembler::toVo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
