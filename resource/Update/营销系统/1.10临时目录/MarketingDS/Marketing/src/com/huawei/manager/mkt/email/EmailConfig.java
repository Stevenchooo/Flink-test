package com.huawei.manager.mkt.email;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.manager.mkt.constant.EmailInfo;
import com.huawei.manager.mkt.util.StringUtils;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2013-5-27]
 * @see  [相关类/方法]
 */
public class EmailConfig
{
    /**
     * 日志类
     */
    private static final Logger LOG = LoggerFactory.getLogger(EmailConfig.class);
    
    /**
     * 加载配置项
     */
    private Properties properties;
    
    /**
     * <默认构造函数>
     */
    public EmailConfig()
    {
        properties = new Properties();
        properties.setProperty(EmailInfo.SMTPPROT, StringUtils.getConfigInfo(EmailInfo.SMTPPROT));
        properties.setProperty(EmailInfo.SMTPHOST, StringUtils.getConfigInfo(EmailInfo.SMTPHOST));
        properties.setProperty(EmailInfo.USERNAME, StringUtils.getConfigInfo(EmailInfo.USERNAME));
        properties.setProperty(EmailInfo.ENCODEPWD, StringUtils.getConfigInfo(EmailInfo.ENCODEPWD));
        properties.setProperty(EmailInfo.FROMADDRESS, StringUtils.getConfigInfo(EmailInfo.FROMADDRESS));
        LOG.debug("EmailConfig success");
    }
    
    /** 获取邮件配置文件
     * @return 邮件配置文件
     * @see [类、类#方法、类#成员]
     */
    public Properties getProperties()
    {
        return properties;
    }
    
}
