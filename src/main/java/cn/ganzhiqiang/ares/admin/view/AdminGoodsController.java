package cn.ganzhiqiang.ares.admin.view;

import cn.ganzhiqiang.ares.admin.assembler.NapiAdminGoodsAssembler;
import cn.ganzhiqiang.ares.admin.domain.AdminGoodsVO;
import cn.ganzhiqiang.ares.common.enums.NapiRespStatus;
import cn.ganzhiqiang.ares.common.warpper.NapiRespWrapper;
import cn.ganzhiqiang.ares.goods.assembler.NapiGoodsAssembler;
import cn.ganzhiqiang.ares.goods.model.BaseGoods;
import cn.ganzhiqiang.ares.goods.service.CommodityService;
import cn.ganzhiqiang.ares.goods.vo.GoodsSimpleVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zq_gan
 * @since 2019/12/22
 **/

@RestController
@RequestMapping("/api/admin/goods/")
public class AdminGoodsController {

    @Resource
    private CommodityService commodityService;

    @RequestMapping(value = "list/", method = RequestMethod.POST)
    public NapiRespWrapper<NapiRespWrapper.ScrollingData> goodsList(Integer pageIndex, Integer pageSize) {

        List<BaseGoods> baseGoodsList = commodityService.queryLastedGoods(pageIndex, pageSize);

        if (CollectionUtils.isEmpty(baseGoodsList)) {
            return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "非法参数");
        }

        List<AdminGoodsVO> goodsList = NapiAdminGoodsAssembler.toVos(baseGoodsList);

        boolean hasMore = goodsList.size() >= pageSize;
        NapiRespWrapper.ScrollingData<AdminGoodsVO> scrollingData = new NapiRespWrapper.ScrollingData<>(hasMore,
                pageIndex + pageSize, goodsList);
        return new NapiRespWrapper<>(scrollingData);
    }
}
