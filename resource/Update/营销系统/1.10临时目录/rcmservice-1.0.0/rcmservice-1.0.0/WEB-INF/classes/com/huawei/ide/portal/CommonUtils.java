package com.huawei.ide.portal;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.ide.controllers.res.BusinessMvcController;

import it.sauronsoftware.base64.Base64;

/**
 * 
 * 工具类
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月15日]
 * @see  [相关类/方法]
 */
public class CommonUtils
{
    
    /**
     * 默认编码格式UTF-8
     */
    public static final String DEFUALT_ENCODING = "utf-8";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessMvcController.class);
    
    /**
     * 获取配置文件sysconfig.properties中res.userManger.serverUrl对应的值
     * 
     * @return String
     */
    public static String getUmServerUrl()
    {
        String value = null;
        value = CommonUtils.getSysConfig("res.userManger.serverUrl");
        return value;
    }
    
    /**
     * 获取配置文件sysconfig.properties中res.userManger.appId对应的值
     * 
     * @return String
     */
    public static String getUmAppId()
    {
        String value = null;
        value = CommonUtils.getSysConfig("res.userManger.appId");
        return value;
    }
    
    /**
     * 获取配置文件sysconfig.properties中res.userManger.key对应的值
     * 
     * @return String
     */
    public static String getUmKey()
    {
        String value = null;
        value = CommonUtils.getSysConfig("res.userManger.key");
        return value;
    }
    
    /**
     * 获取配置项值
     * 
     * @param key
     *            键
     * @return 配置项值
     */
    public static String getSysConfig(String key)
    {
        AbstractConfigService sysConfigService = SystemConfigService.createConfigService();
        return (sysConfigService != null) ? sysConfigService.getConfigKey(key) : null;
    }
    
    /**
     * 判断字符串是否为null、空或者只包换空格
     * 
     * @param str
     *            String
     * @return boolean （true 字符串为null、空或者只包换空格；反之则false)
     */
    public static boolean isStringNullOrEmpty(String str)
    {
        return null == str || str.isEmpty() || str.matches("^\\s+$");
    }
    
    /**
     * 关闭流
     * 
     * @param stream
     *            流
     */
    public static void close(Closeable stream)
    {
        // 流关闭方法
        if (null != stream)
        {
            try
            {
                stream.close();
            }
            catch (IOException e)
            {
                LOGGER.error("close IOException");
                LOGGER.debug("close stream failed!", e);
            }
        }
    }
    
    /**
     * 执行AES128 CBC解密操作
     * 
     * @param content
     *            待解密内容(字符串)
     * @param keyWord
     *            解密密钥
     * @return String 解密后的字符串
     */
    public static String decryptForAESCBCStr(final String content, final String keyWord)
    {
        if (null == content || "".equals(content) || null == keyWord || "".equals(keyWord))
        {
            LOGGER.warn("content or keyWord is null or empty string");
            return "";
        }
        
        String decodeStr = null;
        
        byte[] resultBytes = null;
        
        try
        {
            resultBytes =
                AES128CBC.decode(Base64.decode(content.getBytes(CommonUtils.DEFUALT_ENCODING)),
                    0,
                    keyWord.getBytes(CommonUtils.DEFUALT_ENCODING),
                    0);
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.debug("", e);
            return "";
        }
        catch (RuntimeException e)
        {
            LOGGER.debug("", e);
            return "";
        }
        catch (Exception e)
        {
            LOGGER.debug("", e);
            return "";
        }
        if (null == resultBytes)
        {
            return "";
        }
        try
        {
            decodeStr = new String(resultBytes, CommonUtils.DEFUALT_ENCODING);
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.debug("", e);
            return "";
        }
        
        return decodeStr;
    }
    
    /**
     * hashCode
     * @return  int
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
    
    /**
     * equals
     * @param obj
     *        obj
     * @return   boolean
     */
    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }
    
}
