package com.huawei.ide.portal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.ide.controllers.res.UserMvcController;

/**
 * 
 * 配置文件操作抽象类
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月15日]
 * @see  [相关类/方法]
 */
public abstract class AbstractConfigService
{
    static final Set<AbstractConfigService> CONFIGSERVICE_SET = new HashSet<AbstractConfigService>();
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserMvcController.class);
    
    /**
     * 配置文件修改监听器
     */
    private static List<IConfigFileModifyListener> configFileModifyListenerList =
        new ArrayList<IConfigFileModifyListener>();
    
    /**
     * 存储资源文件中配置信息
     */
    private Properties cfgDefinition = new Properties();
    
    /**
     * 初始化入口，加载资源文件
     */
    protected void initConfigInfo()
    {
        BufferedReader confPath = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        try
        {
            String filePath = getConfigFilePath();
            File configFile = new File(filePath);
            fis = new FileInputStream(configFile);
            isr = new InputStreamReader(fis, CommonUtils.DEFUALT_ENCODING);
            confPath = new BufferedReader(isr);
            cfgDefinition.load(confPath);
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("initConfigInfo failed: FileNotFoundException");
            LOGGER.debug("FileNotFoundException, {}", e);
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.warn("initConfigInfo failed: UnsupportedEncodingException");
        }
        catch (IOException e)
        {
            LOGGER.warn("initConfigInfo failed: IOException");
        }
        finally
        {
            // 关闭系统配置文件流
            CommonUtils.close(fis);
            CommonUtils.close(isr);
            CommonUtils.close(confPath);
        }
    }
    
    /**
     * 根据配置key值获取配置项信息
     * 
     * @param key
     *            配置key值
     * @return 配置信息
     */
    public String getConfigKey(String key)
    {
        return (null == cfgDefinition) ? null
            : (null == cfgDefinition.getProperty(key)) ? "" : cfgDefinition.getProperty(key);
    }
    
    /**
     * 添加监听器
     * 
     * @param l
     *            监听器
     */
    public static void addConfigFileModifyListener(IConfigFileModifyListener l)
    {
        if (!configFileModifyListenerList.contains(l))
        {
            configFileModifyListenerList.add(l);
        }
    }
    
    /**
     * 删除监听器
     * 
     * @param l
     *            监听器
     */
    public static void deleteConfigFileModifyListener(IConfigFileModifyListener l)
    {
        if (configFileModifyListenerList.contains(l))
        {
            configFileModifyListenerList.remove(l);
        }
    }
    
    /**
     * 通知所有的监听器
     */
    public static void fireListener()
    {
        for (AbstractConfigService configService : CONFIGSERVICE_SET)
        {
            configService.initConfigInfo();
        }
        
        for (IConfigFileModifyListener l : configFileModifyListenerList)
        {
            l.configFileChanged();
        }
    }
    
    /**
     * 获取配置文件路径
     * 
     * @return String
     */
    protected abstract String getConfigFilePath();
}
