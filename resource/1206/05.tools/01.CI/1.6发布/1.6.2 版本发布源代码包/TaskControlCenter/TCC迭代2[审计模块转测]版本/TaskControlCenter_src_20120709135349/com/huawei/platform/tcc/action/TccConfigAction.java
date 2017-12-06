/*
 * 文 件 名:  TccConfigAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-4-18
 */
package com.huawei.platform.tcc.action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.common.exmapping.TccSysConfigService;
import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.constants.type.OperType;
import com.huawei.platform.tcc.domain.LinuxUser;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.utils.JDBCExtAppender;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * TCC配置
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-4-18]
 * @see  [相关类/方法]
 */
public class TccConfigAction extends BaseAction
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = 4864915535992793562L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TccConfigAction.class);
    
    private transient TccSysConfigService tccSysConfigService;
    
    /**
     * 任务Id列表数据json格式
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String saveTccConfig()
        throws Exception
    {
        //修改线程的名字
        Thread.currentThread().setName(String.format("saveTccConfig"));
        String returnValue = "true";
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        Date benchDate = null;
        String executeCmdTemplate = TccConfig.DEFAULT_EXEC_CMD;
        String killJobCmdTemplate = TccConfig.DEFAULT_KILL_JOB_CMD;
        String lSCmdTemplate = TccConfig.DEFAULT_LS_CMD;
        String jobDetailUrl = TccConfig.DEFAULT_JOB_DETAIL_URL;
        String loginUrl = TccConfig.DEFAULT_LOGIN_URL;
        String backupDir = TccConfig.DEFAULT_BACKUP_DIR;
        Integer longTimeCallNum = TccConfig.DEFAULT_LONG_TIME_CALL_NUM;
        Integer reserveNLCCount = TccConfig.DEFAULT_RESERVE_NLC_COUNT;
        Integer shortTimeCallNum = TccConfig.DEFAULT_SHORT_TIME_CALL_NUM;
        Integer maxRunningCycleTaskNum = TccConfig.DEFAULT_MAX_RUNNING_CYCLETASK_NUM;
        String emailsTo = "";
        Boolean isSendEmail = false;
        String portalUrl = TccConfig.DEFAULT_PORTAL_URL;
        String emailFrom = TccConfig.DEFAULT_EAMIL_FROM;
        
        String url = "";
        String username = "";
        String password = "";
        String rsLogThreshold = "INFO";
        String rslog2dbThreshold = "INFO";
        String tccLogThreshold = "INFO";
        String tcclog2dbThreshold = "INFO";
        String consoleThreshold = "INFO";
        Boolean isFullName = false;
        try
        {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            benchDate = df.parse(request.getParameter("benchDate"));
            maxRunningCycleTaskNum = Integer.parseInt(request.getParameter("maxRunningCycleTaskNum"));
            shortTimeCallNum = Integer.parseInt(request.getParameter("shortTimeCallNum"));
            longTimeCallNum = Integer.parseInt(request.getParameter("longTimeCallNum"));
            reserveNLCCount = Integer.parseInt(request.getParameter("reserveNLCCount"));
            isFullName = Boolean.parseBoolean(request.getParameter("isFullName"));
            killJobCmdTemplate = request.getParameter("killJobCmdTemplate");
            executeCmdTemplate = request.getParameter("executeCmdTemplate");
            lSCmdTemplate = request.getParameter("lSCmdTemplate");
            jobDetailUrl = request.getParameter("jobDetailUrl");
            loginUrl = request.getParameter("loginUrl");
            backupDir = request.getParameter("backupDir");
            emailsTo = request.getParameter("emailsTo");
            isSendEmail = Boolean.parseBoolean(request.getParameter("isSendEmail"));
            portalUrl = request.getParameter("portalUrl");
            emailFrom = request.getParameter("emailFrom");
            
            //mysqldb配置
            url = request.getParameter("url");
            username = request.getParameter("username");
            password = request.getParameter("password");
            //log4j配置
            rsLogThreshold = request.getParameter("rsLogThreshold");
            rslog2dbThreshold = request.getParameter("rslog2dbThreshold");
            tccLogThreshold = request.getParameter("tccLogThreshold");
            tcclog2dbThreshold = request.getParameter("tcclog2dbThreshold");
            consoleThreshold = request.getParameter("consoleThreshold");
        }
        catch (Exception e)
        {
            LOGGER.error("failed to save TccConfig.", e);
            returnValue = "false";
        }
        
        if (!"............".equals(password) && !TccUtil.canDecrypt(password))
        {
            returnValue = "false|密码需要加密!";
        }
        
        try
        {
            if ("true".endsWith(returnValue))
            {
                
                //更新内存数据配置
                TccConfig.setExecuteCmdTemplate(executeCmdTemplate);
                TccConfig.setKillJobCmdTemplate(killJobCmdTemplate);
                TccConfig.setLongTimeCallNum(longTimeCallNum);
                TccConfig.setReserverNLCCount(reserveNLCCount);
                TccConfig.setLSCmdTemplate(lSCmdTemplate);
                TccConfig.setMaxRunningCycleTaskNum(maxRunningCycleTaskNum);
                TccConfig.setShortTimeCallNum(shortTimeCallNum);
                TccConfig.setIsFullName(isFullName);
                TccConfig.setJobDetailUrl(jobDetailUrl);
                TccConfig.setLoginUrl(loginUrl);
                TccConfig.setBackupDir(backupDir);
                TccConfig.setEmailsTo(emailsTo);
                TccConfig.setIsSendEmail(isSendEmail);
                TccConfig.setPortalUrl(portalUrl);
                TccConfig.setEmailFrom(emailFrom);
                //更新配置文件
                //格式化日期
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                df.setLenient(false);
                
                String benchDateStr = df.format(TccConfig.getBenchDate());
                if (benchDate.before(TccConfig.getBenchDate()))
                {
                    LOGGER.warn("only allowed to modify the benchDate later, ignore it!");
                }
                else
                {
                    benchDateStr = df.format(benchDate);
                }
                tccSysConfigService.setProperty("tcc.benchDate", benchDateStr);
                tccSysConfigService.setProperty("tcc.maxRunngingNum",
                    Integer.toString(TccConfig.getMaxRunningCycleTaskNum()));
                tccSysConfigService.setProperty("tcc.shortTimeCallNum",
                    Integer.toString(TccConfig.getShortTimeCallNum()));
                String lCallNum = Integer.toString(TccConfig.getLongTimeCallNum());
                tccSysConfigService.setProperty("tcc.longTimeCallNum", lCallNum);
                tccSysConfigService.setProperty("tcc.reserveNLCCount",
                    Integer.toString(TccConfig.getReserverNLCCount()));
                tccSysConfigService.setProperty("tcc.killJobCmcTemplate", TccConfig.getKillJobCmdTemplate());
                tccSysConfigService.setProperty("tcc.execCmdTemplate", TccConfig.getExecuteCmdTemplate());
                tccSysConfigService.setProperty("tcc.lsCmdTemplate", TccConfig.getLSCmdTemplate());
                tccSysConfigService.setProperty("tcc.isFullFileName", TccConfig.getIsFullName() ? "1" : "0");
                tccSysConfigService.setProperty("tcc.jobDetailUrl", TccConfig.getJobDetailUrl());
                tccSysConfigService.setProperty("tcc.loginUrl", TccConfig.getLoginUrl());
                tccSysConfigService.setProperty("tcc.backupDir", TccConfig.getBackupDir());
                
                tccSysConfigService.setProperty("tcc.emailsTo", TccConfig.getEmailsTo());
                tccSysConfigService.setProperty("tcc.isSendEmail", TccConfig.getIsSendEmail() ? "1" : "0");
                tccSysConfigService.setProperty("tcc.portalUrl", TccConfig.getPortalUrl());
                tccSysConfigService.setProperty("tcc.emailFrom", TccConfig.getEmailFrom());
                
                //记录操作员修改的配置项
                logChanged(tccSysConfigService.getPropertys(), TccUtil.loadConfigItem(TccConfig.TCC_CONFIG_PATH));
                tccSysConfigService.save();
                //保存mysql配置
                Map<String, String> dbKeyValues = new HashMap<String, String>();
                dbKeyValues.put("jdbc.url", url);
                dbKeyValues.put("jdbc.username", username);
                if (!"............".equals(password))
                {
                    dbKeyValues.put("jdbc.password", password);
                }
                
                //记录操作员修改的配置项
                logChanged(dbKeyValues, TccUtil.loadConfigItem(TccConfig.TCC_DB_PROPERTIES_PATH));
                TccUtil.saveConfigItem(TccConfig.TCC_DB_PROPERTIES_PATH, dbKeyValues);
                //保存log4j配置
                Map<String, String> log4jKeyValues = new HashMap<String, String>();
                log4jKeyValues.put("log4j.appender.rsLog.Threshold", rsLogThreshold);
                log4jKeyValues.put("log4j.appender.rslog2db.Threshold", rslog2dbThreshold);
                log4jKeyValues.put("log4j.appender.rslog2db.URL", url);
                log4jKeyValues.put("log4j.appender.rslog2db.user", username);
                if (!"............".equals(password))
                {
                    log4jKeyValues.put("log4j.appender.rslog2db.password", password);
                }
                
                log4jKeyValues.put("log4j.appender.tccLog.Threshold", tccLogThreshold);
                log4jKeyValues.put("log4j.appender.tcclog2db.Threshold", tcclog2dbThreshold);
                log4jKeyValues.put("log4j.appender.tcclog2db.URL", url);
                log4jKeyValues.put("log4j.appender.tcclog2db.user", username);
                if (!"............".equals(password))
                {
                    log4jKeyValues.put("log4j.appender.tcclog2db.password", password);
                }
                
                log4jKeyValues.put("log4j.appender.Console.Threshold", consoleThreshold);
                //记录操作员修改的配置项
                logChanged(log4jKeyValues, TccUtil.loadConfigItem(TccConfig.TCC_LOG4J_PROPERTIES_PATH));
                TccUtil.saveConfigItem(TccConfig.TCC_LOG4J_PROPERTIES_PATH, log4jKeyValues);
                
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.TCCCONFIG_MODIFY);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("saveTccConfig failed!", e);
            returnValue = "false";
        }
        
        setInputStream(new ByteArrayInputStream(returnValue.getBytes("UTF-8")));
        return SUCCESS;
    }
    
    //记录修改日志
    private void logChanged(Map<String, String> dst, Properties src)
    {
        if (null == dst || null == src)
        {
            return;
        }
        
        String dstValue;
        String srcValue;
        for (String key : dst.keySet())
        {
            dstValue = dst.get(key);
            if (src.containsKey(key))
            {
                srcValue = src.get(key).toString();
                //键值不等
                if (!dstValue.equals(srcValue))
                {
                    //密码项不记录具体密码
                    if (key.toLowerCase().contains("password"))
                    {
                        LOGGER.info("change src[{}={}] to dst[{}={}]. ", new Object[] {key, "PASSWORD_A", key,
                            "PASSWORD_B"});
                    }
                    else
                    {
                        LOGGER.info("change src[{}={}] to dst[{}={}]. ",
                            new Object[] {key, src.get(key), key, dst.get(key)});
                    }
                }
            }
            else
            {
                //密码项不记录具体密码
                if (key.toLowerCase().contains("password"))
                {
                    LOGGER.info("add configItem[{}={}].", new Object[] {key, "PASSWORD_A"});
                }
                else
                {
                    LOGGER.info("add configItem[{}={}].", new Object[] {key, dst.get(key)});
                }
            }
        }
    }
    
    //记录修改日志
    private void logChanged(Properties dst, Properties src)
    {
        if (null == dst || null == src)
        {
            return;
        }
        
        String dstValue;
        String srcValue;
        for (Object key : dst.keySet())
        {
            dstValue = dst.get(key).toString();
            if (src.containsKey(key))
            {
                srcValue = src.get(key).toString();
                //键值不等
                if (!dstValue.equals(srcValue))
                {
                    //密码项不记录具体密码
                    if (key.toString().toLowerCase().contains("password"))
                    {
                        LOGGER.info("change src[{}={}] to dst[{}={}]. ", new Object[] {key, "PASSWORD_A", key,
                            "PASSWORD_B"});
                    }
                    else
                    {
                        LOGGER.info("change src[{}={}] to dst[{}={}]. ",
                            new Object[] {key, src.get(key), key, dst.get(key)});
                    }
                }
            }
            else
            {
                //密码项不记录具体密码
                if (key.toString().toLowerCase().contains("password"))
                {
                    LOGGER.info("add configItem[{}={}].", new Object[] {key, "PASSWORD_A"});
                }
                else
                {
                    LOGGER.info("add configItem[{}={}].", new Object[] {key, dst.get(key)});
                }
            }
        }
    }
    
    /**
     * 任务Id列表数据json格式
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public synchronized String rebootTcc()
        throws Exception
    {
        String returnValue = "true";
        try
        {
            //修改线程的名字
            Thread.currentThread().setName(String.format("rebootTcc"));
            
            LOGGER.info("reboot TCC...");
            java.lang.Runtime runTime = Runtime.getRuntime();
            LOGGER.info("start to execute command[{}]...", TccConfig.getRebootCmd());
            
            Process process = runTime.exec(TccConfig.getRebootCmd());
            
            //如果执行的命令中包含"rebootTomcate.sh"，那么就不等待命令处理结束
            if (!TccConfig.getRebootCmd().contains(TccConfig.DEFAULT_REBOOT_SHELL_NAME))
            {
                try
                {
                    Thread thdError = new Thread(new ConsoleOutput(process.getErrorStream(), "ErrorStream: "));
                    Thread thdInput = new Thread(new ConsoleOutput(process.getInputStream(), "InputStream: "));
                    thdError.start();
                    thdInput.start();
                    process.waitFor();
                    LOGGER.info("command[{}] execute finished! exitValue is {}",
                        TccConfig.getRebootCmd(),
                        process.exitValue());
                }
                catch (Exception e)
                {
                    LOGGER.error("failed to execute command[{}]!", TccConfig.getRebootCmd(), e);
                }
                finally
                {
                    process.destroy();
                }
            }
            
            LOGGER.info("command[{}] execute finished!", TccConfig.getRebootCmd());
            JDBCExtAppender.fresh2Db();
            //记录审计信息
            HttpServletRequest request =
                    (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
            operateAuditInfo.setOpType(OperType.REBOOT_TCC);
            operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
            operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
            getOperationRecord().writeOperLog(operateAuditInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("failed to reboot TCC!", e);
            returnValue = "false";
        }
        
        setInputStream(new ByteArrayInputStream(returnValue.getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 获取linux用户列表
     * @return linux用户列表
     * @throws Exception 异常
     */
    public String reqRemoteShellConfigJson()
        throws Exception
    {
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        
        //JSONObject纯对象
        JSONObject jsonObject = new JSONObject();
        List<LinuxUser> users;
        try
        {
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            
            users = TccUtil.readLinuxUsers();
            
            if (null != users)
            {
                jsonObject.put("total", users.size());
                //修改密码，防止泄密
                for (LinuxUser user : users)
                {
                    user.setPassword("............");
                }
                jsonObject.put("rows", users);
            }
            else
            {
                jsonObject.put("total", 0);
                jsonObject.put("rows", new ArrayList<LinuxUser>(0));
            }
            
            //格式化日期输出
            out.print(JSONObject.toJSONString(jsonObject, SerializerFeature.UseISO8601DateFormat));
            return null;
        }
        catch (Exception e)
        {
            LOGGER.error("reqremoteShellConfig failed!", e);
            throw e;
        }
    }
    
    /**
     * 删除linux用户信息
     * @return 成功标识
     */
    public synchronized String deleteIp()
    {
        String result = "true";
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String ip = request.getParameter("ip");
        try
        {
            //修改线程的名字
            Thread.currentThread().setName(String.format("deleteIp_%s", ip));
            
            LinuxUser user = new LinuxUser();
            user.setIp(ip);
            LOGGER.info("delete LinuxUser[Ip={}].", ip);
            TccUtil.deleteLinuxUser(user);
        }
        catch (Exception e)
        {
            result = "false";
            LOGGER.error("failed to delete LinuxUser[Ip={}].", ip, e);
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("delete user fail! ip is {} ", ip, e);
        }
        return SUCCESS;
    }
    
    /**
     * 修改linux用户信息
     * @return 成功标识
     */
    public synchronized String modifyLinuxUser()
    {
        String result = "true";
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String ip = request.getParameter("ip");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        
        try
        {
            //修改线程的名字
            Thread.currentThread().setName(String.format("modifyLinuxUser_%s_%s", ip, userName));
            
            //如果能解密
            if (TccUtil.canDecrypt(password))
            {
                LinuxUser user = new LinuxUser();
                user.setIp(ip);
                user.setUserName(userName);
                user.setPassword(password);
                
                LOGGER.info("modify LinuxUser[ip={},user={}].", new Object[] {ip, userName});
                TccUtil.modifyLinuxUser(user);
            }
            else
            {
                result = "false|密码必需加密!";
            }
        }
        catch (Exception e)
        {
            result = "false";
            LOGGER.info("failed to modify LinuxUser[ip={},user={}].", new Object[] {ip, userName}, e);
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("modify LinuxUser fail! ", e);
        }
        return SUCCESS;
    }
    
    /**
     * 任务Id列表数据json格式
     * 
     * @return 任务Id列表数据json格式
     */
    public String loadTccConfig()
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        //格式化日期
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        String benchDateStr = df.format(TccConfig.getBenchDate());
        request.setAttribute("benchDate", benchDateStr);
        request.setAttribute("maxRunningCycleTaskNum", TccConfig.getMaxRunningCycleTaskNum());
        request.setAttribute("shortTimeCallNum", TccConfig.getShortTimeCallNum());
        request.setAttribute("longTimeCallNum", TccConfig.getLongTimeCallNum());
        request.setAttribute("reserveNLCCount", TccConfig.getReserverNLCCount());
        request.setAttribute("killJobCmdTemplate", TccConfig.getKillJobCmdTemplate());
        request.setAttribute("executeCmdTemplate", TccConfig.getExecuteCmdTemplate());
        request.setAttribute("lSCmdTemplate", TccConfig.getLSCmdTemplate());
        request.setAttribute("isFullName", TccConfig.getIsFullName());
        request.setAttribute("jobDetailUrl", TccConfig.getJobDetailUrl());
        request.setAttribute("loginUrl", TccConfig.getLoginUrl());
        request.setAttribute("backupDir", TccConfig.getBackupDir());
        request.setAttribute("emailsTo", TccConfig.getEmailsTo());
        request.setAttribute("isSendEmail", TccConfig.getIsSendEmail());
        request.setAttribute("portalUrl", TccConfig.getPortalUrl());
        request.setAttribute("emailFrom", TccConfig.getEmailFrom());
        //mysqldb配置
        try
        {
            Properties pros = TccUtil.loadConfigItem(TccConfig.TCC_DB_PROPERTIES_PATH);
            request.setAttribute("url", pros.get("jdbc.url"));
            request.setAttribute("username", pros.get("jdbc.username"));
            request.setAttribute("password", "............");
        }
        catch (Exception e)
        {
            LOGGER.error("load tccDb config failed!", e);
        }
        
        //log4j配置
        try
        {
            Properties pros = TccUtil.loadConfigItem(TccConfig.TCC_LOG4J_PROPERTIES_PATH);
            request.setAttribute("rsLogThreshold", pros.get("log4j.appender.rsLog.Threshold"));
            request.setAttribute("rslog2dbThreshold", pros.get("log4j.appender.rslog2db.Threshold"));
            request.setAttribute("tccLogThreshold", pros.get("log4j.appender.tccLog.Threshold"));
            request.setAttribute("tcclog2dbThreshold", pros.get("log4j.appender.tcclog2db.Threshold"));
            request.setAttribute("consoleThreshold", pros.get("log4j.appender.Console.Threshold"));
        }
        catch (Exception e)
        {
            LOGGER.error("load log4j Config failed!", e);
        }
        
        return SUCCESS;
    }
    
    public TccSysConfigService getTccSysConfigService()
    {
        return tccSysConfigService;
    }
    
    public void setTccSysConfigService(TccSysConfigService tccSysConfigService)
    {
        this.tccSysConfigService = tccSysConfigService;
    }
    
    /**
     * 将命令控制台输出日志中
     * @author  z00190465
     * @version [Internet Business Service Platform SP V100R100, 2012-4-28]
     */
    public class ConsoleOutput implements Runnable
    {
        private InputStream inputStream;
        
        private String prefix;
        
        /**
         * 构造函数
         * @param inputStream 输入流
         * @param prefix 前缀
         */
        public ConsoleOutput(InputStream inputStream, String prefix)
        {
            setInputStream(inputStream);
            this.prefix = prefix;
        }
        
        @Override
        public void run()
        {
            if (null != inputStream)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                try
                {
                    while (true)
                    {
                        line = br.readLine();
                        if (line == null)
                        {
                            break;
                        }
                        
                        LOGGER.info(prefix + line + "\n");
                    }
                }
                catch (IOException e)
                {
                    LOGGER.error("read outStream or errStream error!", e);
                }
                finally
                {
                    //关闭流
                    try
                    {
                        if (null != br)
                        {
                            br.close();
                        }
                    }
                    catch (IOException ioE)
                    {
                        LOGGER.error("close io error", ioE);
                    }
                }
            }
        }
    }
}
