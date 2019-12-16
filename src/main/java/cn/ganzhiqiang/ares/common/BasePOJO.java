package cn.ganzhiqiang.ares.common;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by nanxuan on 17-9-16.
 */
public class BasePOJO implements Serializable {

    private static final long serialVersionUID = -7110434689134606361L;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
