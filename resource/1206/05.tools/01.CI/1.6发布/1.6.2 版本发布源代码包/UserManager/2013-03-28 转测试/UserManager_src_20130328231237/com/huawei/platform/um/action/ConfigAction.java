/*
 * 文 件 名:  TccConfigAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-4-18
 */
package com.huawei.platform.um.action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.um.constants.UMConfig;
import com.huawei.platform.um.constants.type.OperType;
import com.huawei.platform.um.entity.OperateAuditInfoEntity;
import com.huawei.platform.um.privilegeControl.OperatorMgnt;
import com.huawei.platform.um.utils.JDBCExtAppender;
import com.huawei.platform.um.utils.SysConfigService;
import com.huawei.platform.um.utils.UMUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 配置
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-4-18]
 */
public class ConfigAction extends BaseAction
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = 4864915535992793562L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigAction.class);
    
    private transient SysConfigService sysConfigService;
    
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
        
        String url = "";
        String username = "";
        String password = "";
        String rsLogThreshold = "INFO";
        String rslog2dbThreshold = "INFO";
        String tccLogThreshold = "INFO";
        String tcclog2dbThreshold = "INFO";
        String consoleThreshold = "INFO";
        String portalUrl = "";
        try
        {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
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
            portalUrl = request.getParameter("um.portalUrl");
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("failed to save TccConfig.", e);
            returnValue = "false";
        }
        
        if (StringUtils.isNotBlank(password))
        {
            password = UMUtil.encrypt(password);
        }
        
        try
        {
            if ("true".endsWith(returnValue))
            {
                
                //更新内存数据配置
                UMConfig.setPortalUrl(portalUrl);
                //更新配置文件
                //格式化日期
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                df.setLenient(false);
                sysConfigService.setProperty("um.portalUrl", UMConfig.getPortalUrl());
                
                //记录操作员修改的配置项
                logChanged(sysConfigService.getPropertys(), UMUtil.loadConfigItem(UMConfig.TCC_CONFIG_PATH));
                sysConfigService.save();
                //保存mysql配置
                Map<String, String> dbKeyValues = new HashMap<String, String>();
                dbKeyValues.put("jdbc.url", url);
                dbKeyValues.put("jdbc.username", username);
                if (StringUtils.isNotBlank(password))
                {
                    dbKeyValues.put("jdbc.password", password);
                }
                
                //记录操作员修改的配置项
                logChanged(dbKeyValues, UMUtil.loadConfigItem(UMConfig.TCC_DB_PROPERTIES_PATH));
                UMUtil.saveConfigItem(UMConfig.TCC_DB_PROPERTIES_PATH, dbKeyValues);
                //保存log4j配置
                Map<String, String> log4jKeyValues = new HashMap<String, String>();
                log4jKeyValues.put("log4j.appender.rsLog.Threshold", rsLogThreshold);
                log4jKeyValues.put("log4j.appender.rslog2db.Threshold", rslog2dbThreshold);
                log4jKeyValues.put("log4j.appender.rslog2db.URL", url);
                log4jKeyValues.put("log4j.appender.rslog2db.user", username);
                if (StringUtils.isNotBlank(password))
                {
                    log4jKeyValues.put("log4j.appender.rslog2db.password", password);
                }
                
                log4jKeyValues.put("log4j.appender.tccLog.Threshold", tccLogThreshold);
                log4jKeyValues.put("log4j.appender.tcclog2db.Threshold", tcclog2dbThreshold);
                log4jKeyValues.put("log4j.appender.tcclog2db.URL", url);
                log4jKeyValues.put("log4j.appender.tcclog2db.user", username);
                if (StringUtils.isNotBlank(password))
                {
                    log4jKeyValues.put("log4j.appender.tcclog2db.password", password);
                }
                
                log4jKeyValues.put("log4j.appender.Console.Threshold", consoleThreshold);
                //记录操作员修改的配置项
                logChanged(log4jKeyValues, UMUtil.loadConfigItem(UMConfig.TCC_LOG4J_PROPERTIES_PATH));
                UMUtil.saveConfigItem(UMConfig.TCC_LOG4J_PROPERTIES_PATH, log4jKeyValues);
                
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
        String key;
        for (Entry<String, String> keyValue : dst.entrySet())
        {
            key = keyValue.getKey();
            dstValue = keyValue.getValue();
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
        String key;
        for (Entry<Object, Object> keyValue : dst.entrySet())
        {
            key = keyValue.getKey().toString();
            dstValue = keyValue.getValue().toString();
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
            LOGGER.info("start to execute command[{}]...", UMConfig.getRebootCmd());
            
            Process process = runTime.exec(UMConfig.getRebootCmd());
            
            //如果执行的命令中包含"rebootTomcate.sh"，那么就不等待命令处理结束
            if (!UMConfig.getRebootCmd().contains("rebootTomcate.sh"))
            {
                try
                {
                    Thread thdError = new Thread(new ConsoleOutput(process.getErrorStream(), "ErrorStream: "));
                    Thread thdInput = new Thread(new ConsoleOutput(process.getInputStream(), "InputStream: "));
                    thdError.start();
                    thdInput.start();
                    process.waitFor();
                    LOGGER.info("command[{}] execute finished! exitValue is {}",
                        UMConfig.getRebootCmd(),
                        process.exitValue());
                }
                catch (Exception e)
                {
                    LOGGER.error("failed to execute command[{}]!", UMConfig.getRebootCmd(), e);
                }
                finally
                {
                    process.destroy();
                }
            }
            
            LOGGER.info("command[{}] execute finished!", UMConfig.getRebootCmd());
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
        request.setAttribute("portalUrl", UMConfig.getPortalUrl());
        //mysqldb配置
        try
        {
            Properties pros = UMUtil.loadConfigItem(UMConfig.TCC_DB_PROPERTIES_PATH);
            request.setAttribute("url", pros.get("jdbc.url"));
            request.setAttribute("username", pros.get("jdbc.username"));
            request.setAttribute("password", "");
        }
        catch (Exception e)
        {
            LOGGER.error("load tccDb config failed!", e);
        }
        
        //log4j配置
        try
        {
            Properties pros = UMUtil.loadConfigItem(UMConfig.TCC_LOG4J_PROPERTIES_PATH);
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
    
    public SysConfigService getSysConfigService()
    {
        return sysConfigService;
    }
    
    public void setSysConfigService(SysConfigService sysConfigService)
    {
        this.sysConfigService = sysConfigService;
    }
    
    /**
     * 将命令控制台输出日志中
     * @author  z00190465
     * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-4-28]
     */
    public static class ConsoleOutput implements Runnable
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
            this.inputStream = inputStream;
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
