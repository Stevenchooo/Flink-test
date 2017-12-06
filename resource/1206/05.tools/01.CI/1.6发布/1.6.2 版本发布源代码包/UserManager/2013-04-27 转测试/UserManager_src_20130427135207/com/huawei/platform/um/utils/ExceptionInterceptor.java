/*
 * 文 件 名:  ExceptionInterceptor.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-29
 */
package com.huawei.platform.um.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 异常拦截器
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2011-12-29]
 * @see  [相关类/方法]
 */
public class ExceptionInterceptor extends AbstractInterceptor
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = 124764750516700213L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionInterceptor.class);
    
    //需要在日志中记录用户名和ip信息的action名集合
    private static final Map<String, Object> LOG_USER_ACTIONMAP = new HashMap<String, Object>();
    
    static
    {
        LOG_USER_ACTIONMAP.put("saveTccTask", null);
        LOG_USER_ACTIONMAP.put("changeTaskState", null);
        LOG_USER_ACTIONMAP.put("deleteTask", null);
        LOG_USER_ACTIONMAP.put("batchRedoTasks", null);
        
        LOG_USER_ACTIONMAP.put("redoAll", null);
        LOG_USER_ACTIONMAP.put("redoTaskCycle", null);
        LOG_USER_ACTIONMAP.put("integratedRedo", null);
        
        LOG_USER_ACTIONMAP.put("saveTccStep", null);
        LOG_USER_ACTIONMAP.put("deleteTaskStep", null);
        LOG_USER_ACTIONMAP.put("exchangeTaskStep", null);
        LOG_USER_ACTIONMAP.put("changeStepState", null);
        
        LOG_USER_ACTIONMAP.put("saveTccConfig", null);
        LOG_USER_ACTIONMAP.put("rebootTcc", null);
        
        LOG_USER_ACTIONMAP.put("deleteRole", null);
        LOG_USER_ACTIONMAP.put("saveRole", null);
        LOG_USER_ACTIONMAP.put("saveUserRoleId", null);
        
        LOG_USER_ACTIONMAP.put("deleteOSUser", null);
        LOG_USER_ACTIONMAP.put("saveOSUser", null);
        
        LOG_USER_ACTIONMAP.put("saveOSGroup", null);
        LOG_USER_ACTIONMAP.put("deleteOSGroup", null);

        LOG_USER_ACTIONMAP.put("deleteUser", null);
        LOG_USER_ACTIONMAP.put("saveUser", null);
        
        LOG_USER_ACTIONMAP.put("saveService", null);
        LOG_USER_ACTIONMAP.put("deleteService", null);
        
        LOG_USER_ACTIONMAP.put("deleteNode", null);
        LOG_USER_ACTIONMAP.put("saveNode", null);
        
        LOG_USER_ACTIONMAP.put("deleteOldOperationRecord", null);
        
        LOG_USER_ACTIONMAP.put("saveAlarmHandle", null);
        LOG_USER_ACTIONMAP.put("saveAlarmConfig", null);
        
        LOG_USER_ACTIONMAP.put("logout", null);
    }
    
    @Override
    public String intercept(ActionInvocation invocation)
        throws Exception
    {
        String result = null;
        String actionName = invocation.getInvocationContext().getName();
        
        try
        {
            if (null != actionName && LOG_USER_ACTIONMAP.containsKey(actionName))
            {
                UMUtil.startLogIPUser2Log();
                result = invocation.invoke();
            }
            else
            {
                result = invocation.invoke();
            }
        }
        catch (Throwable e)
        {
            LOGGER.error("execute action[name={}] error!, actionError is {}", new Object[] {actionName,
                getActionErrors(invocation.getAction()), e});
            resetActionErrors(invocation.getAction());
        }
        finally
        {
            UMUtil.stopLogIPUser2Log();
        }
        
        if ("error".equals(result))
        {
            LOGGER.error("execute action[name={}] error!, error is {}",
                actionName,
                getActionErrors(invocation.getAction()));
            resetActionErrors(invocation.getAction());
            result = null;
        }
        
        return result;
    }
    
    //重做action的所有错误已经消息
    private void resetActionErrors(Object actionSupport)
    {
        if (actionSupport instanceof ActionSupport)
        {
            ActionSupport as = (ActionSupport)actionSupport;
            as.clearErrorsAndMessages();
        }
    }
    
    //返回action的所有错误以及消息
    private String getActionErrors(Object actionSupport)
    {
        StringBuilder errors = new StringBuilder();
        if (actionSupport instanceof ActionSupport)
        {
            ActionSupport as = (ActionSupport)actionSupport;
            errors.append("ActionErrors:[");
            errors.append(as.getActionErrors());
            errors.append("],FieldErrors:[");
            errors.append(as.getFieldErrors());
            errors.append("],ActionMessages:[");
            errors.append(as.getActionMessages());
            errors.append("]");
        }
        return errors.toString();
    }
}
