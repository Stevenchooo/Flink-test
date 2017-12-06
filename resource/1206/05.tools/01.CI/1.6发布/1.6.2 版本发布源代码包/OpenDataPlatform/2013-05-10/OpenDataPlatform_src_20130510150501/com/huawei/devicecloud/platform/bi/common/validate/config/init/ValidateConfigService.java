/*
 * 文 件 名:  ValidateConfigInit.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  q00107831
 * 创建时间:  2011-8-9
 */
package com.huawei.devicecloud.platform.bi.common.validate.config.init;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.validate.config.domain.ValidateConfig;

/**
 * 
 * 校验配置信息初始化工具
 * 
 * @author  q00107831
 * @version [Open Data Platform Service, 2011-8-9]
 * @see  [相关类/方法]
 */
public final class ValidateConfigService
{
    /**
     *日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateConfigService.class);
     
    /**
     * 校验器配置信息
     */
    private static Map<String, ValidateConfig> validatorsConfig = new HashMap<String, ValidateConfig>();
    
    /**
     * 校验器配置文件路径
     */
    private static final String CONFIG_ROOT =
        ValidateConfigService.class.getResource("/").getFile() + "../conf/validate/";
    
    /**配置文件的根元素*/
    private static Element configRoot;
    
    //加载配置文件资源
    static
    {
        SAXReader saxReader = new SAXReader();
        
        try
        {
            //配置文件document对象
            Document cfgDocument = saxReader.read(CONFIG_ROOT + "common.validators.config.xml");
            configRoot = cfgDocument.getRootElement();
        }
        catch (DocumentException e)
        {
           // e.printStackTrace();
            LOGGER.error("load config file is error.",e);
        }
    }
    
    /** 
     * 初始化参数校验部分配置
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public void initParamValidConfig()
    {
        //如果全局配置不进行参数校验处理
        if (!BooleanUtils.toBoolean(String.valueOf(configRoot.attributeValue("enable"))))
        {
            validatorsConfig.clear();
            validatorsConfig = null;
            
            return;
        }
        
        //获取所有的校验器
        Iterator<Element> itValidatorElement = configRoot.elementIterator("validator");
        
        ValidateConfig curCfg = null;
        
        Element validatorElement = null;
        
        //将所有校验器配置加入全局配置
        while (itValidatorElement.hasNext())
        {
            validatorElement = itValidatorElement.next();
            curCfg = new ValidateConfig();
            //初始化校验配置
            curCfg.setTarget(validatorElement.attributeValue("target"));
            curCfg.setIsEnable(BooleanUtils.toBooleanObject(validatorElement.attributeValue("enable")));
            curCfg.setExcutorRefBean(validatorElement.attributeValue("excutorRef"));
            
            validatorsConfig.put(curCfg.getTarget(), curCfg);
        }
    }
    
    /**
     * 检查指定方法签名是否支持参数校验
     * @param signature 方法签名，包括所述的类全路径
     * @return 是否支持参数校验
     * @see [类、类#方法、类#成员]
     */
    public Boolean isTargetSupportValidate(String signature)
    {
        //校验器集合为空的话，直接返回
        if ((null == validatorsConfig) || (validatorsConfig.isEmpty()))
        {
            return false;
        }
        
        ValidateConfig curConfig = validatorsConfig.get(signature);
        
        return (null == curConfig) ? false : curConfig.getIsEnable();
    }
    
    /**
     * 获取指定方法参数校验器Bean名称
     * @param signature 方法签名
     * @return 校验器Bean名称
     * @see [类、类#方法、类#成员]
     */
    public String getTargetValidatorRefBean(String signature)
    {
        if (!isTargetSupportValidate(signature))
        {
            return null;
        }
        
        ValidateConfig curConfig = validatorsConfig.get(signature);
        
        return (null == curConfig) ? null : curConfig.getExcutorRefBean();
        
    }
}
