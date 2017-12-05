package com.huawei.manager.base.config;

import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.FileUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see  [相关类/方法]
 */
public final class ManagerConfig
{
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final String MANAGERCONFIG_QUICKLINKPICURL = "quickLinkPicUrl";
    
    private static final String MANAGERCONFIG_QUICKLINKPICPATH = "quickLinkPicPath";
    
    private static final String MANAGERCONFIG_REFRESHINTERVAL = "refreshInterval";
    
    private static final String MANAGERCONFIG_RANDOMINTERVAL = "randomInterval";
    
    private static final String MANAGERCONFIG_SMTPHOST = "smtpHost";
    
    private static final String MANAGERCONFIG_SMTPPORT = "smtpPort";
    
    private static final String MANAGERCONFIG_SENDER = "sender";
    
    private static final String MANAGERCONFIG_SENDERPWD = "senderPwd";
    
    private static final String MANAGERCONFIG_SOURCEADDRESS = "sourceAddress";
    
    private static final String MANAGERCONFIG_SUBJECT = "subject";
    
    private static final String MANAGERCONFIG_RESETURL = "resetUrl";
    
    private static String quickLinkPicUrl = "";
    
    private static int refreshInterval = 24 * 3600;
    
    private static int randomInterval = 0;
    
    private static String smtpHost = "";
    
    private static String smtpPort = "";
    
    private static String sender = "";
    
    private static String senderPwd = "";
    
    private static String sourceAddress = "";
    
    private static String subject = "";
    
    private static String resetUrl = "";
    
    private static String quickLinkPicPath = FileUtil.addPath(System.getProperty("java.io.tmpdir"), "pic");
    
    /**
     * 系统配置
     */
    private static final String SYSTEM_CONFIG_MANAGER = "manager";
    
    /**
     * <默认构造函数>
     */
    private ManagerConfig()
    {
    }
    
    /**
     * <获取系统配置>
     * @param cfg   配置
     * @return      是否成功
     * @see [类、类#方法、类#成员]
     */
    public static boolean parse(Map<String, Object> cfg)
    {
        if (cfg == null)
        {
            LOG.info("There are no manager config, use default configs");
            return true;
        }
        
        quickLinkPicPath = JsonUtil.getAsStr(cfg, MANAGERCONFIG_QUICKLINKPICPATH, quickLinkPicPath);
        quickLinkPicUrl = JsonUtil.getAsStr(cfg, MANAGERCONFIG_QUICKLINKPICURL, quickLinkPicUrl);
        refreshInterval = JsonUtil.getAsInt(cfg, MANAGERCONFIG_REFRESHINTERVAL, refreshInterval);
        randomInterval = JsonUtil.getAsInt(cfg, MANAGERCONFIG_RANDOMINTERVAL, randomInterval);
        
        smtpHost = JsonUtil.getAsStr(cfg, MANAGERCONFIG_SMTPHOST, smtpHost);
        smtpPort = JsonUtil.getAsStr(cfg, MANAGERCONFIG_SMTPPORT, smtpPort);
        sender = JsonUtil.getAsStr(cfg, MANAGERCONFIG_SENDER, sender);
        senderPwd = JsonUtil.getAsStr(cfg, MANAGERCONFIG_SENDERPWD, senderPwd);
        sourceAddress = JsonUtil.getAsStr(cfg, MANAGERCONFIG_SOURCEADDRESS, sourceAddress);
        subject = JsonUtil.getAsStr(cfg, MANAGERCONFIG_SUBJECT, subject);
        resetUrl = JsonUtil.getAsStr(cfg, MANAGERCONFIG_RESETURL, resetUrl);
        
        return true;
    }
    
    public static String getResetURL()
    {
        return resetUrl;
    }
    
    public static String getSubject()
    {
        return subject;
    }
    
    public static String getSourceAddress()
    {
        return sourceAddress;
    }
    
    public static String getSmtpHost()
    {
        return smtpHost;
    }
    
    public static String getSmtpPort()
    {
        return smtpPort;
    }
    
    public static String getSender()
    {
        return sender;
    }
    
    public static String getSenderPwd()
    {
        return senderPwd;
    }
    
    public static String getQuickLinkPicPath()
    {
        return quickLinkPicPath;
    }
    
    /**
     * 放在cookie名称的前面，避免cookie命名空间冲突
     * @return  quickLinkPicUrl
     */
    public static String getQuickLinkPicUrl()
    {
        return quickLinkPicUrl;
    }
    
    public static int getRefreshInterval()
    {
        return refreshInterval;
    }
    
    public static int getRandomInterval()
    {
        return randomInterval;
    }
    
    public static String getSysconfigManager()
    {
        return SYSTEM_CONFIG_MANAGER;
    }
}
