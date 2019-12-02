package com.glitter.spring.boot.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by MaJingcao on 2018/3/9.
 * Copyright by syswin
 */
public class BaseBean {

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * 可以详细显示对象的各个属性值
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}