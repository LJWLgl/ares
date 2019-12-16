package cn.ganzhiqiang.ares.search.view;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ganzhiqiang.ares.common.warpper.NapiRespWrapper;
import cn.ganzhiqiang.ares.search.service.ESSearchService;

import javax.annotation.Resource;

/**
 * @author zq_gan
 * @since 2019/12/15
 **/

@RequestMapping("es")
@RestController
public class ESSearchController {

    @Resource
    private ESSearchService esSearchService;

    @RequestMapping(value = "fullindex/", method = RequestMethod.POST)
    public NapiRespWrapper<Boolean> fullIndex(Integer beginId, Integer limit) {
        Assert.notNull(beginId, "beginId cannot null");
        Assert.notNull(limit, "limit cannot null");
        return new NapiRespWrapper<Boolean>(esSearchService.fullIndexGoodsIndex(beginId, limit));
    }

}
