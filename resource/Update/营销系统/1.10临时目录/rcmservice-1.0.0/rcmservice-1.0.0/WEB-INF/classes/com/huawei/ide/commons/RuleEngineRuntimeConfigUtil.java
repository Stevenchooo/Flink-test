package com.huawei.ide.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * RuleEngineRuntimeConfigUtil
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年6月27日]
 * @see  [相关类/方法]
 */
public class RuleEngineRuntimeConfigUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleEngineRuntimeConfigUtil.class);
    
    private static final String RULE_ENGINE_CONFIG_FILE = "rule_engine_runtime_config";
    
    /**
     * getBusinessRulesToPublish
     * <功能详细描述>
     * @return  List<String>
     * @see [类、类#方法、类#成员]
     */
    public static final List<String> getBusinessRulesToPublish()
    {
        List<String> result = new ArrayList<String>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File configFile = null;
        try
        {
            URL url = classLoader.getResource(RULE_ENGINE_CONFIG_FILE);
            if (null != url)
            {
                configFile = new File(url.toURI());
            }
        }
        catch (URISyntaxException e)
        {
            LOGGER.error("Rule Engine Config File is not under /WEB-INF/classes, please check the path.");
        }
        
        if (null == configFile)
        {
            LOGGER.error("Rule Engine Config File is not under /WEB-INF/classes, please check the path.");
        }
        else if (!configFile.exists() || !configFile.isFile())
        {
            LOGGER.error("Rule Engine Config File is not exist, please check the config file:'"
                + RULE_ENGINE_CONFIG_FILE + "'.");
            return result;
        }
        InputStream in = null;
        try
        {
            in = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(in);
            String value = properties.getProperty("businessRules");
            if (null != value && !value.isEmpty())
            {
                String[] strs = value.split(",");
                for (String str : strs)
                {
                    result.add(str.trim());
                }
            }
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("Rule Engine Config File is not exist, please check the config file:'"
                + RULE_ENGINE_CONFIG_FILE + "'.");
        }
        catch (IOException e)
        {
            LOGGER.error(
                "Rule Engine Config File occur some IOException, with the exception message:'" + e.getMessage() + "'.");
        }
        finally
        {
           
             IOUtils.closeQuietly(in);
        }
        return result;
    }
    
}
