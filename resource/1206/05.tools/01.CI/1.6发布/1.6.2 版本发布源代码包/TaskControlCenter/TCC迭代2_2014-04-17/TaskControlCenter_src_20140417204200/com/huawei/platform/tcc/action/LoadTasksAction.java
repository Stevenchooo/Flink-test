/*
 * 文 件 名:  LoadTasksAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  加载任务
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.alarm.AlarmThresholdManage;
import com.huawei.platform.tcc.constants.ResultCode;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.UsernameAndPasswordParam;
import com.huawei.platform.tcc.entity.OperatorInfoEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.message.req.LoadTasksReq;
import com.huawei.platform.tcc.message.rsp.LoadTasksRsp;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.huawei.platform.tcc.state.TccState;
import com.huawei.platform.tcc.task.TaskManage;
import com.huawei.platform.tcc.utils.TccUtil;
import com.huawei.platform.tcc.utils.crypt.CryptUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 加载任务
 * 
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class LoadTasksAction extends BaseAction
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = 9173350120751016476L;
    
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadTasksAction.class);
    
    /**
     * DAO
     */
    private TccDao tccDao;
    
    private transient TaskManage taskManage;
    
    private transient AlarmThresholdManage alarmThreManage;
    
    /**
     * 认证通过后,停止正运行的任务集合,并临时停止调度任务
     * @return  响应消息体
     * @throws UnsupportedEncodingException 异常
     */
    public String prepareLoadTasks()
        throws UnsupportedEncodingException
    {
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.setCharacterEncoding("UTF-8");
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        Long id = null;
        String body = "{}";
        LoadTasksReq req = null;
        LoadTasksRsp rsp = new LoadTasksRsp();
        try
        {
            if (request.getParameterNames().hasMoreElements())
            {
                body = (String)request.getParameterNames().nextElement();
            }
            
            //提取请求对象
            req = TccUtil.parseObject(body, LoadTasksReq.class);
            LOGGER.info("prepareLoadTasks startting... req is {}!", req);
            
            //认证用户名和密码
            authenticate(req.getUserName(), req.getPassword(), true);
            
            //临时停止调度任务
            id = TccState.getInstance().tempStopSchedule();
            //停止正运行的任务
            getTccService().stopCycleTasks(req.getTaskIds());
            
            //构造返回消息
            rsp.setResultCode(ResultCode.SUCCESS);
            rsp.setId(id);
        }
        catch (Exception e)
        {
            //发生错误则直接启用调度
            TccState.getInstance().continueSchedule(id);
            
            LOGGER.error("Exit prepareLoadTasks exception,req is {}", new Object[] {req}, e);
            TccUtil.setResultByException(rsp, e);
        }
        
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rsp).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 认证通过后,加载任务指定任务集合
     * @return  响应消息体
     * @throws UnsupportedEncodingException 异常
     */
    public String loadTasks()
        throws UnsupportedEncodingException
    {
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.setCharacterEncoding("UTF-8");
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        String body = "{}";
        LoadTasksReq req = null;
        LoadTasksRsp rsp = new LoadTasksRsp();
        try
        {
            if (request.getParameterNames().hasMoreElements())
            {
                body = (String)request.getParameterNames().nextElement();
            }
            
            //提取请求对象
            req = TccUtil.parseObject(body, LoadTasksReq.class);
            LOGGER.info("load Tasks startting... req is {}!", req);
            
            //认证用户名和密码
            authenticate(req.getUserName(), req.getPassword(), req.isLoadAll());
            
            //如果是全部加载
            if (req.isLoadAll())
            {
                getTccService().init();
            }
            else
            {
                //加载指定任务到任务缓存中
                List<TaskEntity> canLoadTasks = getCanLoadTasks(req.getUserName(), req.getTaskIds());
                List<Long> canLoadTaskIds = new ArrayList<Long>();
                for (TaskEntity taskE : canLoadTasks)
                {
                    canLoadTaskIds.add(taskE.getTaskId());
                }
                
                //先加载告警阈值
                alarmThreManage.loadAlarmThres(canLoadTaskIds);
                //后加载任务
                taskManage.loadTasks(canLoadTasks);
                
                
                
            }
            
            //继续开始调度
            if (null != req.getId())
            {
                TccState.getInstance().continueSchedule(req.getId());
            }
            
            //构造返回消息
            rsp.setResultCode(ResultCode.SUCCESS);
        }
        catch (Exception e)
        {
            LOGGER.error("Exit loadTasks exception,req is {}", new Object[] {req}, e);
            TccUtil.setResultByException(rsp, e);
        }
        
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rsp).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 是否认证通过
     * @param tccName tcc名字
     * @param userName 用户名
     * @param password 密码
     * @throws Exception 异常
     */
    private void authenticate(String userName, String password, boolean isLoadAll)
        throws Exception
    {
        //参数不合法，认证失败
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password))
        {
            LOGGER.error("both userName[{}] and password can't be blank!", userName);
            throw new CException(ResultCode.PARAMETER_INVALID);
        }
        
        UsernameAndPasswordParam param = new UsernameAndPasswordParam();
        param.setUserName(userName);
        param.setPassword(CryptUtil.getMD5HexString(password));
        OperatorInfoEntity operatorInfo = tccDao.getOperatorInfo(param);
        if (null == operatorInfo)
        {
            LOGGER.error("userName[{}] and password are not correct!", userName);
            throw new CException(ResultCode.AUTH_FAILED, userName);
        }
        
        //必需是系统管理员账号
        Operator operator = getOperatorMgnt().getOperator(userName);
        
        //仅对加载全部任务进行系统管理员认证，其它的由getCanLoadTasks处理
        if (null == operator || (isLoadAll && !operator.isSystemAdmin()))
        {
            LOGGER.error("user[{}] must be system administrator!", userName);
            throw new CException(ResultCode.NO_ENOUGH_PREVILEGE, userName);
        }
    }
    
    /**
     * 获取用户可以加载的任务集合
     * @param userName 用户名
     * @param taskIds 任务Id集合
     * @return 用户可以加载的任务集合
     */
    private List<TaskEntity> getCanLoadTasks(String userName, List<Long> taskIds)
    {
        List<TaskEntity> canLoadTasks = new ArrayList<TaskEntity>();
        if (StringUtils.isBlank(userName) || null == taskIds || taskIds.isEmpty())
        {
            return canLoadTasks;
        }
        
        try
        {
            //必需是系统管理员账号
            Operator operator = getOperatorMgnt().getOperator(userName);
            if (null == operator)
            {
                return canLoadTasks;
            }
            
            List<TaskEntity> tasks = tccDao.getTaskList(taskIds);
            for (TaskEntity taskE : tasks)
            {
                if (operator.canAdd(taskE.getOsUser()))
                {
                    canLoadTasks.add(taskE);
                }
                else
                {
                    LOGGER.error("can't load task[{}] because of no enough privilege!", taskE);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("loadTasks failed", e);
        }
        
        return canLoadTasks;
    }
    
    public TaskManage getTaskManage()
    {
        return taskManage;
    }
    
    public void setTaskManage(TaskManage taskManage)
    {
        this.taskManage = taskManage;
    }
    
    public AlarmThresholdManage getAlarmThreManage()
    {
        return alarmThreManage;
    }
    
    public void setAlarmThreManage(AlarmThresholdManage alarmThreManage)
    {
        this.alarmThreManage = alarmThreManage;
    }
    
    public TccDao getTccDao()
    {
        return tccDao;
    }
    
    public void setTccDao(TccDao tccDao)
    {
        this.tccDao = tccDao;
    }
}
