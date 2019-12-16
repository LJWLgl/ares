package cn.ganzhiqiang.ares.sight.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zq_gan
 * @since 2019/10/19
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SightDo {

    private Long id;
    private String link;

}
