package com.huawei.bi.task.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.huawei.bi.task.bean.logger.ExeLoggerBuilder;
import com.huawei.bi.task.bean.logger.IExeLogger;
import com.huawei.bi.task.bean.taskrunner.RemoteCommandExecuteUtil;
import com.huawei.bi.task.constant.ExportState;
import com.huawei.bi.task.da.ExePersistBuilder;
import com.huawei.bi.task.da.IExecutionPersist;
import com.huawei.bi.task.da.ISqlInfoPersist;
import com.huawei.bi.task.da.MemExePersist;
import com.huawei.bi.task.da.SqlInfoPersist;
import com.huawei.bi.task.da.TaskDA;
import com.huawei.bi.task.da.UserDA;
import com.huawei.bi.task.domain.Execution;
import com.huawei.bi.task.domain.SqlInfo;
import com.huawei.bi.task.domain.Task;
import com.huawei.bi.task.domain.User;
import com.huawei.bi.util.SqlPretreat;
import com.huawei.bi.util.Util;

public class TaskBean
{
    private static final Integer DEFAULT_SQLS = 5;
    
    MemExePersist mem = new MemExePersist();
    
    UserDA userDA = new UserDA();
    
    TaskDA taskDA = new TaskDA();
    
    /**
     * 是否允许用户运行新的任务
     * @param user 用户
     * @return 是否允许用户运行新的任务
     * @throws Exception 异常
     */
    public boolean allowRunNew(String user)
        throws Exception
    {
        if (StringUtils.isBlank(user))
        {
            return false;
        }
        
        return mem.getRunningExeNum(user) <= getMaxRunnningSqls(user);
    }
    
    /**
     * 停止正运行的任务
     * @param exeId 执行Id
     * @param user 用户
     * @throws Exception 异常
     */
    public void stop(String exeId, String user)
        throws Exception
    {
        Execution exec = getExecution("", exeId);
        if (null != exec && user.equals(exec.getUser()))
        {
            RemoteCommandExecuteUtil.stop(exeId);
        }
    }
    
    /**
     * 获取用户可以运行的最大语句数
     * @param user 用户
     * @return 用户可以运行的最大语句数
     * @throws Exception 异常
     */
    public Integer getMaxRunnningSqls(String user)
        throws Exception
    {
        User userInfo = userDA.getUser(user);
        if (null != userInfo && null != userInfo.getMaxRunningSqls() && userInfo.getMaxRunningSqls() >= 0)
        {
            return userInfo.getMaxRunningSqls();
        }
        else
        {
            return DEFAULT_SQLS;
        }
    }
    
    public void addTask(Task task)
        throws Exception
    {
        taskDA.addTask(task);
    }
    
    /**
     * user用户是否具有execId的导出权限
     * @param execId 执行Id
     * @param user 用户
     * @return user用户是否具有execId的导出权限
     * @throws Exception 异常
     */
    public boolean canExport(String execId, String user)
        throws Exception
    {
        boolean canExport = false;
        Execution exec = getExecution("", execId);
        if (null != exec && user.equals(exec.getUser()))
        {
            Integer exportState = exec.getExportState();
            if (null != exportState)
            {
                if (exportState == ExportState.UNLIMIT_EXPORT)
                {
                    canExport = true;
                }
                //else if (exportState == ExportState.AUDIT_AGREE)
                else
                {
                    //获取有效期
                    ISqlInfoPersist sqlPersist = new SqlInfoPersist();
                    SqlInfo sqlInfo = sqlPersist.getSqlInfo(exec.getSqlId());
                    canExport =
                        (null != sqlInfo) && (user.equals(sqlInfo.getUser())) && sqlInfo.isExportPrivilege()
                            && Util.between(sqlInfo.getValidStartDate(), sqlInfo.getValidEndDate(), new Date());
                }
            }
        }
        return canExport;
    }
    
    public Task createTask(String user, String connId, String defaultDB, String code)
        throws Exception
    {
        Task task = new Task();
        task.setTaskId(UUID.randomUUID().toString().replace('-', '_'));
        task.setTaskType("DBScriptTask");
        task.setTaskName("temp - " + task.getTaskType());
        
        Map<String, String> optionMap = new HashMap<String, String>();
        
        SqlPretreat sqlPre = new SqlPretreat();
        String preCode = sqlPre.pretreat(code);
        //空语句不执行
        if (StringUtils.isBlank(preCode))
        {
            return null;
        }
        //增加默认库
        preCode = sqlPre.insertUseDB(defaultDB, preCode);
        
        
        optionMap.put("preCode", preCode);
        optionMap.put("code", code);
        optionMap.put("connId", connId);
        task.setOptionMap(optionMap);
        task.setOptions(new JSONObject(optionMap).toString());
        // TODO :) its me
        task.setOwner(user);
        task.setLastModTime(new Date());
        task.setTempTask(true);
        this.addTask(task);
        
        return task;
    }
    
    public String executeTask(Task task, String exeType)
        throws Exception
    {
        
        String exeId = UUID.randomUUID().toString().replace("-", "_");
        
        // TODO no direct invoke
        Runnable runnable = new TaskRunnable(task, exeType, exeId);
        new Thread(runnable).start();
        
        return exeId;
    }
    
    public Execution getExecution(String exeType, String exeId)
        throws Exception
    {
        IExecutionPersist exePersist = ExePersistBuilder.buildExePersist(exeType);
        return exePersist.getExecution(exeId);
    }
    
    public void saveExportReason(String exeType, String user, String exeId, String exportReason)
        throws Exception
    {
        //获取sqlId
        IExecutionPersist exePersist = ExePersistBuilder.buildExePersist(exeType);
        Execution execution = exePersist.getExecution(exeId);
        //更新执行状态
        exePersist.updateExportState(exeId, user, ExportState.APPLY_EXPORT);
        
        //更新原因
        ISqlInfoPersist sqlPersist = new SqlInfoPersist();
        sqlPersist.updateExportReason(execution.getSqlId(), user, exportReason);
        
    }
    
    public void deleteExcution(String exeType, String exeId)
        throws Exception
    {
        // delete execution
        IExecutionPersist exePersist = ExePersistBuilder.buildExePersist(exeType);
        exePersist.deleteExecution(exeId);
        
        // TODO delete task if needed		
        
        // delete log file
        if (Execution.EXE_TYPE_BACKEND.equals(exeType))
        {
            IExeLogger exeLogger = ExeLoggerBuilder.buildExeLogger(exeType, exeId);
            exeLogger.delete();
        }
    }
    
    public static void main(String[] args)
        throws Exception
    {
        Task task = new Task();
        task.setTaskType("HiveScriptTask");
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "select * from u_data");
        task.setOptionMap(map);
        TaskBean bean = new TaskBean();
        String exeId = bean.executeTask(task, Execution.EXE_TYPE_FRONTEND);
        
        while (true)
        {
            IExeLogger exeLogger = ExeLoggerBuilder.buildExeLogger(Execution.EXE_TYPE_FRONTEND, exeId);
            List<String> list = exeLogger.readDebug();
            
            // TODO
            if ("complete".equals(bean.getExecution(Execution.EXE_TYPE_FRONTEND, exeId).getStatus()))
            {
                bean.deleteExcution(Execution.EXE_TYPE_FRONTEND, exeId);
                
                break;
            }
            
            Thread.currentThread().sleep(5000);
        }
        
    }
    
}
