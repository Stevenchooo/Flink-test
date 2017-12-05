package com.huawei.manager.base.config;

import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;

/**
 * 
 * <一句话功能简述> <功能详细描述>
 * 
 * @author w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see [相关类/方法]
 */
public abstract class ManagerConfig
{
    /**
     * 配置文件的manager字段
     * 
     */
    public static final String SYSCONFIG_MANAGER = "manager";

    private static final Logger LOG = LogUtil.getInstance();

    private static final String MANAGERCONFIG_SMTPHOST = "smtpHost";

    private static final String MANAGERCONFIG_SMTPPORT = "smtpPort";

    private static final String MANAGERCONFIG_SENDER = "sender";

    private static final String MANAGERCONFIG_SENDERPWD = "senderPwd";

    private static final String MANAGERCONFIG_SOURCEADDRESS = "sourceAddress";

    private static final String MANAGERCONFIG_SUBJECT = "subject";

    private static final String MANAGERCONFIG_RESETURL = "resetUrl";

    private static String smtpHost = "";

    private static String smtpPort = "";

    private static String sender = "";

    private static String senderPwd = "";

    private static String sourceAddress = "";

    private static String subject = "";

    private static String resetUrl = "";

    /**
     * <获取系统配置>
     * 
     * @param cfg
     *            配置
     * @return 是否成功
     * @see [类、类#方法、类#成员]
     */
    public static final boolean parse(Map<String, Object> cfg)
    {
        if (cfg == null)
        {
            LOG.info("There are no manager config, use default configs");
            return true;
        }

        smtpHost = JsonUtil.getAsStr(cfg, MANAGERCONFIG_SMTPHOST, smtpHost);
        smtpPort = JsonUtil.getAsStr(cfg, MANAGERCONFIG_SMTPPORT, smtpPort);
        sender = JsonUtil.getAsStr(cfg, MANAGERCONFIG_SENDER, sender);
        senderPwd = JsonUtil.getAsStr(cfg, MANAGERCONFIG_SENDERPWD, senderPwd);
        sourceAddress = JsonUtil.getAsStr(cfg, MANAGERCONFIG_SOURCEADDRESS,
                sourceAddress);
        subject = JsonUtil.getAsStr(cfg, MANAGERCONFIG_SUBJECT, subject);
        resetUrl = JsonUtil.getAsStr(cfg, MANAGERCONFIG_RESETURL, resetUrl);

        return true;
    }

    public static final String getResetURL()
    {
        return resetUrl;
    }

    public static final String getSubject()
    {
        return subject;
    }

    public static final String getSourceAddress()
    {
        return sourceAddress;
    }

    public static final String getSmtpHost()
    {
        return smtpHost;
    }

    public static final String getSmtpPort()
    {
        return smtpPort;
    }

    public static final String getSender()
    {
        return sender;
    }

    public static final String getSenderPwd()
    {
        return senderPwd;
    }
}
