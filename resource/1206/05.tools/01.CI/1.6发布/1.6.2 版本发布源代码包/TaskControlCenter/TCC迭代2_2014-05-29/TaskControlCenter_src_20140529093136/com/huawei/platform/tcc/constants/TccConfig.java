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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.constants.type.CycleType;
import com.huawei.platform.tcc.utils.TccUtil;

/**
 * TCC的配置文件类
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-21]
 */
public class TccConfig
{
    /**
     * ssh连接阻塞递增次数阈值
     */
    public static final Integer SCB_ASCEND_TIMES_THRESHOLD = 1000;
    
    /**
     * 周期Id格式化字符串
     */
    public static final String CYCLE_ID_FORMAT = "yyyyMMdd-HHmm";
    
    /**
     * 默认的最新日志的任务周期（运行成功）保留数
     */
    public static final Integer DEFAULT_RESERVE_NLC_COUNT = 7;
    
    /**
     * 默认的最大SSH连接数
     */
    public static final Integer DEFAULT_MAX_SSH_CONNECTION_NUM = 10;
    
    /**
     * 默认的最大SSH连接数（实时）
     */
    public static final Integer DEFAULT_MAX_RT_SSH_CONNECTION_NUM = 10;
    
    /**
     * 默认的连接重试次数
     */
    public static final Integer DEFAULT_CONNECT_RETRY_TIMES = 8;
    
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
    public static final long NEW_INPUT_FILES_MILLISECONDS = 2 * 1000;
    
    /**
     * 每秒的毫秒数
     */
    public static final long MILLS_PER_SECOND = 1000;
    
    /**
     * 每分钟的毫秒数
     */
    public static final long MILLS_PER_MINUTE = 60 * MILLS_PER_SECOND;
    
    /**
     * 每小时的毫秒数
     */
    public static final long MILLS_PER_HOUR = 60 * MILLS_PER_MINUTE;
    
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
    
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(TccConfig.class);
    
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
    
    /**
     * 获取文件列表的命令模板
     */
    private static String lSCmdTemplate = loadLsCmdTemplate();
    
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
     * 是否ID中心
     */
    private static boolean taskIdCenter = loadTaskIdCenter();
    
    /**
     * 保留最近reserverNLCCount个执行的任务周期的远程壳输出日志
     */
    private static Integer reserverNLCCount = loadReserverNLCCount();
    
    /**
     * 每台主机的最大连接数
     */
    private static Integer maxSSHConnectionNum = loadMaxSSHConnectionNum();
    
    /**
     * 每台主机的最大连接数（实时任务）
     */
    private static Integer maxRTSSHConnectionNum = loadMaxRTSSHConnectionNum();
    
    /**
     * 连接重试次数
     */
    private static Integer conRetryTimes = loadConRetryTimes();
    
    /**
     * 观察者地址
     */
    private static List<String> observerUrls = loadObserverUrls();
    
    /**
     * ssh连接阻塞阈值
     */
    private static int scbAscendTimesThreshold = loadSCBAscendTimesThreshold();
    
    /**
     * 输入的正则表达式
     */
    private static String inputRegex = loadInputRegex();
    
    /**
     * ssh日志级别
     */
    private static String sshLogLevel = loadSshLogLevel();
    
    /**
     * ssh日志文件
     */
    private static String sshLogFile = loadSshLogFile();
    
    public static String getSshLogLevel()
    {
        return sshLogLevel;
    }
    
    public static void setSshLogLevel(String sshLogLevel)
    {
        TccConfig.sshLogLevel = sshLogLevel;
    }
    
    public static String getSshLogFile()
    {
        return sshLogFile;
    }
    
    public static void setSshLogFile(String sshLogFile)
    {
        TccConfig.sshLogFile = sshLogFile;
    }
    
    public static int getScbAscendTimesThreshold()
    {
        return scbAscendTimesThreshold;
    }
    
    public static void setScbAscendTimesThreshold(int scbAscendTimesThreshold)
    {
        TccConfig.scbAscendTimesThreshold = scbAscendTimesThreshold;
    }
    
    public static List<String> getObserverUrls()
    {
        return observerUrls;
    }
    
    public static void setObserverUrls(List<String> observerUrls)
    {
        TccConfig.observerUrls = observerUrls;
    }
    
    public static boolean isTaskIdCenter()
    {
        return taskIdCenter;
    }
    
    public static void setTaskIdCenter(boolean taskIdCenter)
    {
        TccConfig.taskIdCenter = taskIdCenter;
    }
    
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
    
    public static void setMaxSSHConnectionNum(int maxSSHConnectionNum)
    {
        TccConfig.maxSSHConnectionNum = maxSSHConnectionNum;
    }
    
    public static int getMaxRunningCycleTaskNum()
    {
        return maxRunningCycleTaskNum;
    }
    
    public static void setMaxRunningCycleTaskNum(int maxRunningCycleTaskNum)
    {
        TccConfig.maxRunningCycleTaskNum = maxRunningCycleTaskNum;
    }
    
    public static Integer getMaxRTSSHConnectionNum()
    {
        return maxRTSSHConnectionNum;
    }
    
    public static void setMaxRTSSHConnectionNum(Integer maxRTSSHConnectionNum)
    {
        TccConfig.maxRTSSHConnectionNum = maxRTSSHConnectionNum;
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
    
    private static String loadSshLogLevel()
    {
        try
        {
            return TccUtil.getSysConfig("tcc.ssh.loglevel");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.ssh.loglevel(eg '3') is not correct!", e);
            LOGGER.info("beacuse no tcc.inputRegex item do be found in config file" + ", tcc.inputRegex is set to {}",
                "3");
            return "3";
        }
    }
    
    private static String loadSshLogFile()
    {
        try
        {
            return TccUtil.getSysConfig("tcc.ssh.logfile");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.ssh.logfile(eg 'ssh2.log') is not correct!", e);
            LOGGER.info("beacuse no tcc.inputRegex item do be found in config file" + ", tcc.inputRegex is set to {}",
                "ssh2.log");
            return "ssh2.log";
        }
    }
    
    private static String loadInputRegex()
    {
        try
        {
            return TccUtil.getSysConfig("tcc.inputRegex");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.inputRegex(eg '[.\n\r]*') is not correct!", e);
            LOGGER.info("beacuse no tcc.inputRegex item do be found in config file" + ", tcc.inputRegex is set to {}",
                "[.\n\r]*");
            return "[.\n\r]*";
        }
    }
    
    private static int loadSCBAscendTimesThreshold()
    {
        try
        {
            return Integer.parseInt(TccUtil.getSysConfig("tcc.scbAscendTimesThreshold"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.scbAscendTimesThreshold(eg '1000') is not correct!", e);
            LOGGER.info("beacuse no tcc.scbAscendTimesThreshold item do be found in config file"
                + ", tcc.scbAscendTimesThreshold is set to {}", "");
            return SCB_ASCEND_TIMES_THRESHOLD;
        }
    }
    
    private static List<String> loadObserverUrls()
    {
        try
        {
            String urls = TccUtil.getSysConfig("tcc.observerUrls");
            if (null != urls)
            {
                return Arrays.asList(urls.split(";"));
            }
            else
            {
                return new ArrayList<String>(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.observerUrls(eg '1') is not correct!", e);
            LOGGER.info("beacuse no tcc.observerUrls item do be found in config file"
                + ", tcc.observerUrls is set to {}", "");
            return new ArrayList<String>(0);
        }
    }
    
    private static boolean loadTaskIdCenter()
    {
        try
        {
            return "1".equals(TccUtil.getSysConfig("tcc.taskIdCenter"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.taskIdCenter(eg '1') is not correct!", e);
            LOGGER.info("beacuse no tcc.taskIdCenter item do be found in config file"
                + ", tcc.taskIdCenter is set to {}", false);
            return false;
        }
    }
    
    private static String loadEmailFrom()
    {
        try
        {
            return TccUtil.getSysConfig("tcc.emailFrom");
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
            return TccUtil.getSysConfig("tcc.portalUrl");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.portalUrl(eg 'http://localhost') is not correct!", e);
            LOGGER.info("beacuse no tcc.portalUrl item do be found in config file" + ", tcc.portalUrl is set to {}",
                DEFAULT_PORTAL_URL);
            return DEFAULT_PORTAL_URL;
        }
    }
    
    private static String loadJobDetailUrl()
    {
        try
        {
            return TccUtil.getSysConfig("tcc.jobDetailUrl");
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
            return TccUtil.getSysConfig("tcc.rebootCmd");
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
            return TccUtil.getSysConfig("tcc.lsCmdTemplate");
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
            return TccUtil.getSysConfig("tcc.execCmdTemplate");
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
            return TccUtil.getSysConfig("tcc.killJobCmcTemplate");
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
            return TccUtil.getSysConfig("tcc.backupDir");
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.backupDir(eg '/data1/odsbak/backupdir/%s') is not correct!", e);
            LOGGER.info("beacuse no tcc.backupDir item do be found in config file" + ", tcc.backupDir is set to {}",
                DEFAULT_BACKUP_DIR);
            return DEFAULT_BACKUP_DIR;
        }
    }
    
    private static Integer loadReserverNLCCount()
    {
        try
        {
            return Integer.parseInt(TccUtil.getSysConfig("tcc.reserveNLCCount"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.reserveNLCCount(eg '7') is not correct!", e);
            LOGGER.info("beacuse no tcc.reserveNLCCount item do be found in config file"
                + ", tcc.reserveNLCCount is set to {}", DEFAULT_RESERVE_NLC_COUNT);
            return DEFAULT_RESERVE_NLC_COUNT;
        }
    }
    
    private static Integer loadMaxSSHConnectionNum()
    {
        try
        {
            return Integer.parseInt(TccUtil.getSysConfig("tcc.maxSSHConnectionNum"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.maxSSHConnectionNum(eg '10') is not correct!", e);
            LOGGER.info("beacuse no tcc.maxSSHConnectionNum item do be found in config file"
                + ", tcc.maxSSHConnectionNum is set to {}", DEFAULT_MAX_SSH_CONNECTION_NUM);
            return DEFAULT_MAX_SSH_CONNECTION_NUM;
        }
    }
    
    private static Integer loadMaxRTSSHConnectionNum()
    {
        try
        {
            return Integer.parseInt(TccUtil.getSysConfig("tcc.maxRTSSHConnectionNum"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.maxRTSSHConnectionNum(eg '10') is not correct!", e);
            LOGGER.info("beacuse no tcc.maxRTSSHConnectionNum item do be found in config file"
                + ", tcc.maxRTSSHConnectionNum is set to {}", DEFAULT_MAX_RT_SSH_CONNECTION_NUM);
            return DEFAULT_MAX_RT_SSH_CONNECTION_NUM;
        }
    }
    
    private static Integer loadConRetryTimes()
    {
        try
        {
            return Integer.parseInt(TccUtil.getSysConfig("tcc.conRetryTimes"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.conRetryTimes(eg '8') is not correct!", e);
            LOGGER.info("beacuse no tcc.conRetryTimes item do be found in config file"
                + ", tcc.conRetryTimes is set to {}", DEFAULT_CONNECT_RETRY_TIMES);
            return DEFAULT_CONNECT_RETRY_TIMES;
        }
    }
    
    private static boolean isFullFileName()
    {
        try
        {
            return "1".equals(TccUtil.getSysConfig("tcc.isFullFileName"));
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
            return "1".equals(TccUtil.getSysConfig("tcc.isSendEmail"));
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
            return "1".equals(TccUtil.getSysConfig("tcc.isLogScheduleDuration"));
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
            return "1".equals(TccUtil.getSysConfig("tcc.isAutoMoveFile"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.isAutoMoveFile(eg '0') is not correct!", e);
            LOGGER.info("beacuse no tcc.isAutoMoveFile item do be found in config file"
                + ", tcc.isAutoMoveFile is set to {}", 0);
            return false;
        }
    }
    
    private static Date loadBenchDate()
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        try
        {
            return df.parse(TccUtil.getSysConfig("tcc.benchDate"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.benchDate(eg '2011-10-11') is not correct!", e);
            Date current = new Date();
            LOGGER.info("beacuse no tcc.benchDate item do be found in config file"
                + ", tcc.benchDate is set to currentTime[{}]", current);
            return current;
        }
    }
    
    private static int loadMaxRunningNum()
    {
        try
        {
            return Integer.parseInt(TccUtil.getSysConfig("tcc.maxRunngingNum"));
        }
        catch (Exception e)
        {
            LOGGER.error("tcc.maxRunngingNum(eg '10') is not correct!", e);
            LOGGER.info("beacuse no tcc.maxRunngingNum item do be found in config file"
                + ", tcc.maxRunngingNum is set to {}", DEFAULT_MAX_RUNNING_CYCLETASK_NUM);
            return DEFAULT_MAX_RUNNING_CYCLETASK_NUM;
        }
    }
    
    public static String getInputRegex()
    {
        return inputRegex;
    }
    
    public static void setInputRegex(String inputRegex)
    {
        TccConfig.inputRegex = inputRegex;
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
    
    public static Integer getMaxSSHConnectionNum()
    {
        return maxSSHConnectionNum;
    }
    
    public static void setMaxSSHConnectionNum(Integer maxSSHConnectionNum)
    {
        TccConfig.maxSSHConnectionNum = maxSSHConnectionNum;
    }
    
    public static Integer getConRetryTimes()
    {
        return conRetryTimes;
    }
    
    public static void setConRetryTimes(Integer conRetryTimes)
    {
        TccConfig.conRetryTimes = conRetryTimes;
    }
}
