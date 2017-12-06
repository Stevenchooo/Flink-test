/*
 * 文 件 名:  ParamCheckerConfigService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-10
 */

package com.huawei.devicecloud.platform.bi.common.validate.config.domain;

import java.io.Serializable;

/**
 * 
 * 字段校验规则配置项信息
 * 
 * @author  q00107831
 * @version [Open Data Platform Service, 2011-8-10]
 * @see  [相关类/方法]
 */
public class ParamValidateConfig implements Serializable
{
    
    private static final long serialVersionUID = 801276408651890741L;
    
    /**
     * 字段名称
     */
    private String fieldName;
    
    /**
     * 字段有效性检查的正则表达式
     */
    private String validRgex;
    
    /**
     * 是否可以为空
     */
    private Boolean blankAble;
    
    public String getFieldName()
    {
        return fieldName;
    }
    
    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }
    
    public String getValidRgex()
    {
        return validRgex;
    }
    
    public void setValidRgex(String validRgex)
    {
        this.validRgex = validRgex;
    }
    
    public Boolean getBlankAble()
    {
        return blankAble;
    }

    public void setBlankAble(Boolean blankAble)
    {
        this.blankAble = blankAble;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ValidateConfig [fieldName=");
        builder.append(fieldName);
        builder.append(", nullAble=");
        builder.append(blankAble);
        builder.append(", validRgex=");
        builder.append(validRgex);
        builder.append("]");
        return builder.toString();
    }
    
}
