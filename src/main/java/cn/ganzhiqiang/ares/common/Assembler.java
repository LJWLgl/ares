package cn.ganzhiqiang.ares.common;

import java.util.List;

/**
 * @author zq_gan
 * @since 2019/12/22
 **/

public interface Assembler<U, R> {

    U toVo(R r);

    List<U> toVos(List<R> rs);

}
