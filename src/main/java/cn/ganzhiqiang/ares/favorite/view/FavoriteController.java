package cn.ganzhiqiang.ares.favorite.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ganzhiqiang.ares.common.enums.NapiRespStatus;
import cn.ganzhiqiang.ares.common.warpper.NapiRespWrapper;
import cn.ganzhiqiang.ares.common.enums.TypeEnum;
import cn.ganzhiqiang.ares.favorite.service.FavoriteService;

import javax.annotation.Resource;

/**
 * @author nanxuan
 * @since 2018/2/19
 **/

@Controller
@ResponseBody
public class FavoriteController {

    @Resource
    private FavoriteService favoriteService;

    @RequestMapping(value = "favorite/", method = RequestMethod.GET)
    public NapiRespWrapper<Integer> favorite(
            @RequestParam(value = "user_id") Integer userId,
            @RequestParam(value = "resource_id") Integer resourceId,
            @RequestParam(value = "resource_type") Integer resourceType) {

        if (userId == null || resourceId == null || resourceType == null) {
            return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "缺少必要参数");
        }
        TypeEnum type = TypeEnum.of(resourceType);
        return new NapiRespWrapper<>(favoriteService.favorite(userId, resourceId, type));
    }

    @RequestMapping(value = "unfavorite/", method = RequestMethod.GET)
    public NapiRespWrapper<Boolean> unfavorite(@RequestParam(value = "favorite_id") Integer favoriteId) {
        if (favoriteId == null) {
            return new NapiRespWrapper<>(NapiRespStatus.INVALID_PARAM, "缺少必要参数");
        }
        return new NapiRespWrapper<>(favoriteService.unfavorite(favoriteId));
    }

}
