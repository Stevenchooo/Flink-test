/*
 * 文 件 名:  ValidateConfig.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-9
 */
package com.huawei.devicecloud.platform.bi.common.validate.config.domain;

import java.io.Serializable;

/**
 * 
 * 参数校验配置项信息
 * 
 * @author  q00107831
 * @version [Open Data Platform Service, 2011-8-9]
 * @see  [相关类/方法]
 */
public class ValidateConfig implements Serializable
{

    private static final long serialVersionUID = -1259523267211751700L;

    /**
     * 待校验的实现方法签名，格式为：类全路径.方法名
     */
    private String target;
    
    /**
     * 校验器Bean ID
     */
    private String excutorRefBean;
    
    /**
     * 是否执行参数校验操作
     */
    private Boolean isEnable;

    public String getTarget()
    {
        return target;
    }

    public void setTarget(String target)
    {
        this.target = target;
    }

    public String getExcutorRefBean()
    {
        return excutorRefBean;
    }

    public void setExcutorRefBean(String excutorRefBean)
    {
        this.excutorRefBean = excutorRefBean;
    }

    public Boolean getIsEnable()
    {
        return isEnable;
    }

    public void setIsEnable(Boolean isEnable)
    {
        this.isEnable = isEnable;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ValidateConfig [target=");
        builder.append(target);
        builder.append(", excutorRefBean=");
        builder.append(excutorRefBean);
        builder.append(", isEnable=");
        builder.append(isEnable);
        builder.append("]");
        return builder.toString();
    }
    
}
