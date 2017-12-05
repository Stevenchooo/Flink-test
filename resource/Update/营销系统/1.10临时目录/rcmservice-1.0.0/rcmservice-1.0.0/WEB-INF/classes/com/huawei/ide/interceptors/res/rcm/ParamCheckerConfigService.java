/*
 * 文 件 名:  ParamCheckerConfigService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-10
 */
package com.huawei.ide.interceptors.res.rcm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 初始化字段校验检查配置信息服务
 * 
 * @author  q00107831
 * @version [Internet Business Service Platform SP V100R100, 2011-8-10]
 * @see  [相关类/方法]
 */
public class ParamCheckerConfigService
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ParamCheckerConfigService.class);
    
    /**
     * 处理验证配置信息
     */
    private static Map<String, ParamValidateConfig> validCfg = new HashMap<String, ParamValidateConfig>();
    
    /**
     * 字段校验配置文件路径
     */
    private static final String FIELD_VALID_CFG_PATH =
        ParamCheckerConfigService.class.getResource("/").getFile() + "../conf/validate/rcm.service.field.validate.xml";
    
    /**
     * 配置文件document对象
     */
    private static Document cfgDocument;
    
    /**
     * 配置文件的根元素
     */
    private static Element configRoot;
    
    //加载配置文件资源
    static
    {
        SAXReader saxReader = new SAXReader();
        
        try
        {
            cfgDocument = saxReader.read(FIELD_VALID_CFG_PATH);
            configRoot = cfgDocument.getRootElement();
        }
        catch (DocumentException e)
        {
            LOGGER.error("load config file is error.");
            LOGGER.debug("load config file is error. Exception is {}", e);
        }
    }
    
    /** 
     * 初始化参数校验部分配置
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public void initFieldValidInfo()
    {
        Element paramVlidElement = configRoot.element("ParamValid");
        
        Iterator<Element> itField = paramVlidElement.elementIterator("Field");
        
        ParamValidateConfig curCfg = null;
        
        Element fieldElement = null;
        
        for (; itField.hasNext();)
        {
            fieldElement = itField.next();
            curCfg = new ParamValidateConfig();
            curCfg.setFieldName(fieldElement.attributeValue("name"));
            curCfg.setBlankAble(BooleanUtils.toBoolean(fieldElement.attributeValue("nullable")));
            curCfg.setValidRgex(fieldElement.getText());
            
            validCfg.put(curCfg.getFieldName(), curCfg);
        }
    }
    
    /**
     * 检查字段是否可以为空
     * @param fieldName 字段名
     * @return 是否可以为空
     * @see [类、类#方法、类#成员]
     */
    public boolean isNullable(String fieldName)
    {
        return (null == validCfg.get(fieldName)) ? true : validCfg.get(fieldName).getBlankAble();
    }
    
    /**
     * 获取指定字段的正则表达式
     * @param fieldName 字段名
     * @return 校验字段有效性的正则表达式
     * @throws CException 
     *          CException
     * @see [类、类#方法、类#成员]
     */
    public String getFieldRegex(String fieldName)
        throws CException
    {
        if (null == validCfg.get(fieldName))
        {
            throw new CException(RCMResultCodeConstants.SYSTEM_ERROR);
        }
        else
        {
            return validCfg.get(fieldName).getValidRgex();
        }
    }
}
