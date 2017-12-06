/*
 * 文 件 名:  TccConfig.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-21
 */
package com.huawei.platform.tcc.constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.common.CommonUtils;
import com.huawei.platform.tcc.constants.type.CycleType;
import com.huawei.platform.tcc.utils.TccUtil;

/**
 * TCC的配置文件类
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-21]
 * @see  [相关类/方法]
 */
public class TccConfig
{
    /**
     * 默认的登陆地址
     */
    public static final String DEFAULT_LOGIN_URL = "";
    
    /**
     * 默认的最新日志的任务周期（运行成功）保留数
     */
    public static final Integer DEFAULT_RESERVE_NLC_COUNT = 7;
    
    /**
     * 默认的批文件备份目录
     */
    public static final String DEFAULT_BACKUP_DIR = "/data1/odsbak/backupdir/%s";
    
    /**
     * 默认的邮件发送者
     */
    public static final String DEFAULT_EAMIL_FROM = "";
    
    /**
     * Tcc Portal外部访问地址
     */
    public static final String DEFAULT_PORTAL_URL = "http://localhost";
    
    /**
     * 默认重启命令
     */
    public static final String DEFAULT_REBOOT_CMD = "/bin/sh -c rebootTomcate.sh";
    
    /**
     * 默认重启脚本名
     */
    public static final String DEFAULT_REBOOT_SHELL_NAME = "rebootTomcate.sh";
    
    /**
     * 一天的小时数
     */
    public static final int HOURS_OF_DAY = 24;
    
    /**
     * TCC数据库连接配置文件路径
     */
    public static final String TCC_DB_PROPERTIES_PATH = "../classes/tcc.jdbc.properties";
    
    /**
     * TCC配置文件路径
     */
    public static final String TCC_CONFIG_PATH = "../conf/common/resource/systemconfig/tcc.sysconfig.properties";
    
    /**
     * TCC log4j配置文件路径
     */
    public static final String TCC_LOG4J_PROPERTIES_PATH = "../classes/log4j.properties";
    
    /**
     * 默认任务ID编号长度
     */
    public static final int TASKID_SERAILNO_LENGTH = 4;
    
    /**
     * 默认短执行时间命令(ls命令、hadoop job -kill命令)最大的并发调用数
     */
    public static final int DEFAULT_SHORT_TIME_CALL_NUM = 10;
    
    /**
     * 默认长执行时间命令(sh类型)最大的并发调用数
     */
    public static final int DEFAULT_LONG_TIME_CALL_NUM = 10;
    
    /**
     * 获取文件列表的默认命令模板
     */
    public static final String DEFAULT_LS_CMD = "ls -d --sort=time --time=ctime -r %s";
    
    /**
     * 结束job任务的默认命令模板
     */
    public static final String DEFAULT_KILL_JOB_CMD = "hadoop job -kill %s";
    
    /**
     * 执行的默认命令模板
     */
    public static final String DEFAULT_EXEC_CMD = "%s %s %s %s";
    
    /**
     * 调整的秒数
     */
    public static final int TUNE_SECONDS = 10;
    
    /**
     * 暂时无批处理任务需要执行时的休眠毫秒数,NBT(no batch task)
     */
    public static final long NBT_SLEEP_MILLISECONDS = 1000;
    
    /**
     * 检查新的输入文件的时间间隔
     */
    public static final long NEW_INPUT_FILES_MILLISECONDS = 10 * 1000;
    
    /**
     * 每秒的毫秒数
     */
    public static final long MILLS_PER_SECOND = 1000;
    
    /**
     * 每分钟的毫秒数
     */
    public static final long MILLS_PER_MINUTES = 60 * MILLS_PER_SECOND;
    
    /**
     * 每小时的毫秒数
     */
    public static final long MILLS_PER_HOUR = 60 * MILLS_PER_MINUTES;
    
    /**
     * 每天的毫秒数
     */
    public static final long MILLS_PER_DAY = 24 * MILLS_PER_HOUR;
    
    /**
     * 每天的秒数
     */
    public static final long SECOND_PER_DAY = 24 * 3600;
    
    /**
     * 等待时间偏移的偏移秒数
     */
    public static final int WAITTING_DELAY_SECONDS = (int)(NEW_INPUT_FILES_MILLISECONDS / MILLS_PER_SECOND);
    
    /**
     * 最大运行周期任务数的默认值
     */
    public static final int DEFAULT_MAX_RUNNING_CYCLETASK_NUM = 10;
    
    /**
     * job的详细信息展示页面地址的默认值
     */
    public static final String DEFAULT_JOB_DETAIL_URL = "http://localhost:50030/jobdetails.jsp?jobid=%s&refresh=0";
    
    /**
     * 如果小时任务类型，如果是历史周期（非当天），每天仅执行选择的小时任务重做
     */
    public static final int CHOOSED_HOUR = 23;
    
    /**
     * 最大日期
     */
    public static final Date MAX_DATE = new Date(365 * 1000 * MILLS_PER_DAY);
    
    /**
     * 默认的超时分钟数
     */
    public static final int DEFAULT_TIMEOUT_MINUTES = 30;
    
    /**
     * 结束job任务的命令模板
     */
    private static String killJobCmdTemplate = loadKillJobCmcTemplate();
    
    /**
     * 执行的命令模板
     */
    private static String executeCmdTemplate = loadExecuteCmdTemplate();
    
    /**
     * 基准日期
     */
    private static Date benchDate = loadBenchDate();
    
    /**
     * 是否是完整路径文件名
     */
    private static boolean isFullName = isFullFileName();
    
    /**
     * 最大并发运行的周期任务数
     */
    private static int maxRunningCycleTaskNum = loadMaxRunningNum();
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TccConfig.class);
    
    /**
     * 获取文件列表的命令模板
     */
    private static String lSCmdTemplate = loadLsCmdTemplate();
    
    /**
     * 短执行时间命令(ls命令、hadoop job -kill命令)最大的并发调用数
     */
    private static int shortTimeCallNum = loadShortTimeCallNum();
    
    /**
     * 长执行时间命令(sh类型)最大的并发调用数
     */
    private static int longTimeCallNum = loadLongTimeCallNum();
    
    /**
     * job的详细信息展示页面地址
     */
    private static String jobDetailUrl = loadJobDetailUrl();
    
    /**
     * 是否发送邮件
     */
    private static boolean isSendEmail = loadIsSendEmail();
    
    /**
     * 是否记录调度时长
     */
    private static boolean isLogScheduleDuration = loadIsLogScheduleDuration();
    
    /**
     * 是否启用备份文件恢复功能
     */
    private static boolean isAutoMoveFile = loadIsAutoMoveFile();
    
    /**
     * 是否使用su执行命令
     */
    private static boolean isUseSuExecCmd = loadIsUseSuExecCmd();
    
    /**
     * 发送者邮箱
     */
    private static String emailFrom = loadEmailFrom();
    
    /**
     * Tcc portal外部访问地址
     */
    private static String portalUrl = loadPortalUrl();
    
    /**
     * 重启脚本目录
     */
    private static String rebootCmd = loadRebootCmd();
    
    /**
     * 批文件备份目录
     */
    private static String backupDir = loadBackUpDir();
    
    /**
     * 登陆地址
     */
    private static String loginUrl = loadLoginUrl();
    
    /**
     * 保留最近reserverNLCCount个执行的任务周期的远程壳输出日志
     */
    private static Integer reserverNLCCount = loadReserverNLCCount();
    
    public static String getLSCmdTemplate()
    {
        return lSCmdTemplate;
    }
    
    public static void setLSCmdTemplate(String lSCmdTemplate)
    {
        TccConfig.lSCmdTemplate = lSCmdTemplate;
    }
    
    public static String getKillJobCmdTemplate()
    {
        return killJobCmdTemplate;
    }
    
    public static void setKillJobCmdTemplate(String killJobCmdTemplate)
    {
        TccConfig.killJobCmdTemplate = killJobCmdTemplate;
    }
    
    public static String getExecuteCmdTemplate()
    {
        return executeCmdTemplate;
    }
    
    public static void setExecuteCmdTemplate(String executeCmdTemplate)
    {
        TccConfig.executeCmdTemplate = executeCmdTemplate;
    }
    
    public static Date getBenchDate()
    {
        return benchDate;
    }
    
    public static void setBenchDate(Date benchDate)
    {
        TccConfig.benchDate = benchDate;
    }
    
    public static int getShortTimeCallNum()
    {
        return shortTimeCallNum;
    }
    
    public static void setShortTimeCallNum(int shortTimeCallNum)
    {
        TccConfig.shortTimeCallNum = shortTimeCallNum;
    }
    
    public static int getLongTimeCallNum()
    {
        return longTimeCallNum;
    }
    
    public static void setLongTimeCallNum(int longTimeCallNum)
    {
        TccConfig.longTimeCallNum = longTimeCallNum;
    }
    
    public static int getMaxRunningCycleTaskNum()
    {
        return maxRunningCycleTaskNum;
    }
    
    public static void setMaxRunningCycleTaskNum(int maxRunningCycleTaskNum)
    {
        TccConfig.maxRunningCycleTaskNum = maxRunningCycleTaskNum;
    }
    
    /**
     * 最小的周期Id
     * 注意：（依赖于BENCH_DATE先初始化）
     * @return 最小的周期Id
     */
    public static String getMinCycleId()
    {
        Date benchDateS = TccUtil.roll2CycleStart(TccConfig.benchDate, CycleType.HOUR, 0);
        if (!benchDateS.equals(TccConfig.benchDate))
        {
            benchDateS = TccUtil.roll2CycleStart(TccConfig.benchDate, CycleType.HOUR, 1);
        }
        return TccUtil.covDate2CycleID(benchDateS);
    }
    
    public static boolean getIsFullName()
    {
        return TccConfig.isFullName;
    }
    
    public static void setIsFullName(boolean isFullName)
    {
        TccConfig.isFullName = isFullName;
    }
    
    public static boolean getIsSendEmail()
    {
        return TccConfig.isSendEmail;
    }
    
    public static void setIsSendEmail(boolean isSendEmail)
    {
        TccConfig.isSendEmail = isSendEmail;
    }
    
    public static boolean getIsLogScheduleDuration()
    {
        return TccConfig.isLogScheduleDuration;
    }
    
    public static void setIsLogScheduleDuration(boolean isLogScheduleDuration)
    {
        TccConfig.isLogScheduleDuration = isLogScheduleDuration;
    }
    
    private static int loadShortTimeCallNum()
    {
        try
        {
            return Integer.parseInt(CommonUtils.getSysConfig("tcc.shortTimeCallNum"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.shortTimeCallNum(eg '10') is not correct!", e);
            LOGGER.info("beacuse no tcc.shortTimeCallNum item do be found in config file"
                + ", tcc.shortTimeCallNum is set to {}", DEFAULT_SHORT_TIME_CALL_NUM);
            return DEFAULT_SHORT_TIME_CALL_NUM;
        }
    }
    
    private static String loadEmailsTo()
    {
        try
        {
            return CommonUtils.getSysConfig("tcc.emailsTo");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.emailsTo(eg 'zzz@huawei.com') is not correct!", e);
            LOGGER.info("beacuse no tcc.shortTimeCallNum item do be found in config file"
                + ", tcc.emailsTo is set to {}", "");
            return "";
        }
    }
    
    private static String loadEmailFrom()
    {
        try
        {
            return CommonUtils.getSysConfig("tcc.emailFrom");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.emailFrom(eg 'zzz@huawei.com') is not correct!", e);
            LOGGER.info("beacuse no tcc.emailFrom item do be found in config file" + ", tcc.emailFrom is set to {}",
                DEFAULT_EAMIL_FROM);
            return DEFAULT_EAMIL_FROM;
        }
    }
    
    private static String loadPortalUrl()
    {
        try
        {
            return CommonUtils.getSysConfig("tcc.portalUrl");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.portalUrl(eg 'http://localhost') is not correct!", e);
            LOGGER.info("beacuse no tcc.portalUrl item do be found in config file" + ", tcc.portalUrl is set to {}",
                DEFAULT_PORTAL_URL);
            return DEFAULT_PORTAL_URL;
        }
    }
    
    private static int loadLongTimeCallNum()
    {
        try
        {
            return Integer.parseInt(CommonUtils.getSysConfig("tcc.longTimeCallNum"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.longTimeCallNum(eg '10') is not correct!", e);
            LOGGER.info("beacuse no tcc.longTimeCallNum item do be found in config file"
                + ", tcc.longTimeCallNum is set to {}", DEFAULT_LONG_TIME_CALL_NUM);
            return DEFAULT_LONG_TIME_CALL_NUM;
        }
    }
    
    private static String loadJobDetailUrl()
    {
        try
        {
            return CommonUtils.getSysConfig("tcc.jobDetailUrl");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.jobDetailUrl(eg 'http://localhost:50030"
                + "/jobdetails.jsp?jobid=%s&refresh=0') is not correct!", e);
            LOGGER.info("beacuse no tcc.jobDetailUrl item do be found in config file"
                + ", tcc.jobDetailUrl is set to {}", DEFAULT_JOB_DETAIL_URL);
            return DEFAULT_JOB_DETAIL_URL;
        }
    }
    
    private static String loadRebootCmd()
    {
        try
        {
            return CommonUtils.getSysConfig("tcc.rebootCmd");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.rebootCmd(eg '/bin/sh -c rebootTomcate.sh" + "') is not correct!", e);
            LOGGER.info("beacuse no tcc.rebootCmd item do be found in config file" + ", tcc.rebootCmd is set to {}",
                DEFAULT_REBOOT_CMD);
            return DEFAULT_REBOOT_CMD;
        }
    }
    
    private static String loadLsCmdTemplate()
    {
        try
        {
            return CommonUtils.getSysConfig("tcc.lsCmdTemplate");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.lsCmdTemplate(eg 'ls -d --sort=time --time=ctime -r %s') is not correct!", e);
            LOGGER.info("beacuse no tcc.lsCmdTemplate item do be found in config file"
                + ", tcc.lsCmdTemplate is set to {}", DEFAULT_LS_CMD);
            return DEFAULT_LS_CMD;
        }
    }
    
    private static String loadExecuteCmdTemplate()
    {
        try
        {
            return CommonUtils.getSysConfig("tcc.execCmdTemplate");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.execCmdTemplate(eg '%s %s %s %s') is not correct!", e);
            LOGGER.info("beacuse no tcc.lsCmdTemplate item do be found in config file"
                + ", tcc.execCmdTemplate is set to {}", DEFAULT_EXEC_CMD);
            return DEFAULT_EXEC_CMD;
        }
    }
    
    private static String loadKillJobCmcTemplate()
    {
        try
        {
            return CommonUtils.getSysConfig("tcc.killJobCmcTemplate");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.killJobCmcTemplate(eg 'hadoop job -kill %s') is not correct!", e);
            LOGGER.info("beacuse no tcc.killJobCmcTemplate item do be found in config file"
                + ", tcc.killJobCmcTemplate is set to {}", DEFAULT_KILL_JOB_CMD);
            return DEFAULT_KILL_JOB_CMD;
        }
    }
    
    private static String loadBackUpDir()
    {
        try
        {
            return CommonUtils.getSysConfig("tcc.backupDir");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.backupDir(eg '/data1/odsbak/backupdir/%s') is not correct!", e);
            LOGGER.info("beacuse no tcc.backupDir item do be found in config file" + ", tcc.backupDir is set to {}",
                DEFAULT_BACKUP_DIR);
            return DEFAULT_BACKUP_DIR;
        }
    }
    
    private static String loadLoginUrl()
    {
        try
        {
            return CommonUtils.getSysConfig("tcc.loginUrl");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.loginUrl(eg '') is not correct!", e);
            LOGGER.info("beacuse no tcc.loginUrl item do be found in config file" + ", tcc.loginUrl is set to {}",
                DEFAULT_LOGIN_URL);
            return DEFAULT_LOGIN_URL;
        }
    }
    
    private static Integer loadReserverNLCCount()
    {
        try
        {
            return Integer.parseInt(CommonUtils.getSysConfig("tcc.reserveNLCCount"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.reserveNLCCount(eg '7') is not correct!", e);
            LOGGER.info("beacuse no tcc.reserveNLCCount item do be found in config file"
                + ", tcc.reserveNLCCount is set to {}", DEFAULT_RESERVE_NLC_COUNT);
            return DEFAULT_RESERVE_NLC_COUNT;
        }
    }
    
    private static boolean isFullFileName()
    {
        try
        {
            return "1".equals(CommonUtils.getSysConfig("tcc.isFullFileName"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.isFullFileName(eg '0') is not correct!", e);
            LOGGER.info("beacuse no tcc.isFullFileName item do be found in config file"
                + ", tcc.isFullFileName is set to {}", 0);
            return false;
        }
    }
    
    private static boolean loadIsSendEmail()
    {
        try
        {
            return "1".equals(CommonUtils.getSysConfig("tcc.isSendEmail"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.isSendEmail(eg '0') is not correct!", e);
            LOGGER.info("beacuse no tcc.isSendEmail item do be found in config file" + ", tcc.isSendEmail is set to {}",
                0);
            return false;
        }
    }
    
    private static boolean loadIsLogScheduleDuration()
    {
        try
        {
            return "1".equals(CommonUtils.getSysConfig("tcc.isLogScheduleDuration"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.isLogScheduleDuration(eg '0') is not correct!", e);
            LOGGER.info("beacuse no tcc.isLogScheduleDuration item do be found in config file"
                + ", tcc.isLogScheduleDuration is set to {}", 0);
            return false;
        }
    }
    
    private static boolean loadIsAutoMoveFile()
    {
        try
        {
            return "1".equals(CommonUtils.getSysConfig("tcc.isAutoMoveFile"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.isAutoMoveFile(eg '0') is not correct!", e);
            LOGGER.info("beacuse no tcc.isAutoMoveFile item do be found in config file"
                + ", tcc.isAutoMoveFile is set to {}", 0);
            return false;
        }
    }
    
    private static boolean loadIsUseSuExecCmd()
    {
        try
        {
            return "1".equals(CommonUtils.getSysConfig("tcc.isUseSuExecCmd"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.isUseSuExecCmd(eg '0') is not correct!", e);
            LOGGER.info("beacuse no tcc.isUseSuExecCmd item do be found in config file"
                + ", tcc.isUseSuExecCmd is set to {}", 0);
            return false;
        }
    }
    
    private static Date loadBenchDate()
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        try
        {
            return df.parse(CommonUtils.getSysConfig("tcc.benchDate"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.benchDate(eg '2011-10-11') is not correct!", e);
            Date current = CommonUtils.getCrruentTime();
            LOGGER.info("beacuse no tcc.benchDate item do be found in config file"
                + ", tcc.benchDate is set to currentTime[{}]", current);
            return current;
        }
    }
    
    private static int loadMaxRunningNum()
    {
        try
        {
            return Integer.parseInt(CommonUtils.getSysConfig("tcc.maxRunngingNum"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.maxRunngingNum(eg '10') is not correct!", e);
            LOGGER.info("beacuse no tcc.maxRunngingNum item do be found in config file"
                + ", tcc.maxRunngingNum is set to {}", DEFAULT_MAX_RUNNING_CYCLETASK_NUM);
            return DEFAULT_MAX_RUNNING_CYCLETASK_NUM;
        }
    }
    
    public static String getJobDetailUrl()
    {
        return jobDetailUrl;
    }
    
    public static void setJobDetailUrl(String jobDetailUrl)
    {
        TccConfig.jobDetailUrl = jobDetailUrl;
    }
    
    public static String getRebootCmd()
    {
        return rebootCmd;
    }
    
    public static void setRebootCmd(String rebootCmd)
    {
        TccConfig.rebootCmd = rebootCmd;
    }
    
    public static String getEmailFrom()
    {
        return emailFrom;
    }
    
    public static void setEmailFrom(String emailFrom)
    {
        TccConfig.emailFrom = emailFrom;
    }
    
    public static String getPortalUrl()
    {
        return portalUrl;
    }
    
    public static void setPortalUrl(String portalUrl)
    {
        TccConfig.portalUrl = portalUrl;
    }
    
    public static String getBackupDir()
    {
        return backupDir;
    }
    
    public static void setBackupDir(String backupDir)
    {
        TccConfig.backupDir = backupDir;
    }
    
    public static Integer getReserverNLCCount()
    {
        return reserverNLCCount;
    }
    
    public static void setReserverNLCCount(Integer reserverNLCCount)
    {
        TccConfig.reserverNLCCount = reserverNLCCount;
    }
    
    public static boolean getIsAutoMoveFile()
    {
        return isAutoMoveFile;
    }
    
    public static void setIsAutoMoveFile(boolean isAutoMoveFile)
    {
        TccConfig.isAutoMoveFile = isAutoMoveFile;
    }
    
    public static String getLoginUrl()
    {
        return loginUrl;
    }
    
    public static void setLoginUrl(String loginUrl)
    {
        TccConfig.loginUrl = loginUrl;
    }
    
    public static boolean getIsUseSuExecCmd()
    {
        return isUseSuExecCmd;
    }
    
    public static void setUseSuExecCmd(boolean useSuExecCmd)
    {
        TccConfig.isUseSuExecCmd = useSuExecCmd;
    }
}
