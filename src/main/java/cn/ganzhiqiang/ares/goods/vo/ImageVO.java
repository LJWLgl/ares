package cn.ganzhiqiang.ares.goods.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nanxuan
 * @since 2018/1/31
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageVO {

    private Integer id;
    private String path;
    private String width;
    private String height;

    public ImageVO(String path) {
        this.path = path;
    }
}
