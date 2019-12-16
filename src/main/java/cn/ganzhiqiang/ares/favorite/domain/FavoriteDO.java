package cn.ganzhiqiang.ares.favorite.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author nanxuan
 * @since 2018/2/11
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDO {

    private Integer id;

    private Integer userId;

    private Integer resourceId;

    private Integer resourceType;

    private Date gmtCreate;

}
