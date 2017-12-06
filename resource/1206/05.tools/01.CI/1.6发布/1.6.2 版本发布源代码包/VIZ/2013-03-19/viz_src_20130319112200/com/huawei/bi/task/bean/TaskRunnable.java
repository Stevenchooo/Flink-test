package com.huawei.bi.task.bean;

import java.io.File;
import java.util.Date;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.task.bean.logger.ExeLoggerBuilder;
import com.huawei.bi.task.bean.logger.IExeLogger;
import com.huawei.bi.task.bean.taskrunner.ITaskRunner;
import com.huawei.bi.task.bean.taskrunner.TaskRunnerFactory;
import com.huawei.bi.task.constant.ExportState;
import com.huawei.bi.task.da.ExePersistBuilder;
import com.huawei.bi.task.da.IExecutionPersist;
import com.huawei.bi.task.da.SqlInfoPersist;
import com.huawei.bi.task.domain.Execution;
import com.huawei.bi.task.domain.SqlInfo;
import com.huawei.bi.task.domain.Task;
import com.huawei.bi.util.Config;
import com.huawei.bi.util.Util;

public class TaskRunnable implements Runnable
{
    private static Logger log = LoggerFactory.getLogger(TaskRunnable.class);
    
    Task task;
    
    String exeType;
    
    String exeId;
    
    public TaskRunnable(Task task, String exeType, String exeId)
    {
        super();
        this.task = task;
        this.exeType = exeType;
        this.exeId = exeId;
    }
    
    @Override
    public void run()
    {
        log.debug("task=" + task + ",exeTye=" + exeType + ",exeId=" + exeId);
        try
        {
            // build the logger
            IExeLogger exeLogger = ExeLoggerBuilder.buildExeLogger(exeType, exeId);
            
            //store the sql_info
            //用户precode替换code
            String code = task.getOptionMap().remove("code");
            String preCode = task.getOptionMap().get("preCode");
            Integer sqlId = addGetSqlId(task.getOwner(), preCode);
            
            // store the execution
            Execution execution = new Execution();
            execution.setTaskId(task.getTaskId());
            execution.setOptions(new JSONObject(task.getOptionMap()).toString());
            execution.setCode(code);
            execution.setExeId(exeId);
            execution.setExecutionType(exeType);
            execution.setStatus("running");
            execution.setStartAt(new Date());
            
            execution.setUser(task.getOwner());
            execution.setSqlId(sqlId);
            
            IExecutionPersist exePersist = ExePersistBuilder.buildExePersist(exeType);
            
            exePersist.addExecution(execution);
            // run
            ITaskRunner runner = TaskRunnerFactory.buildTaskRunner(task.getTaskType());
            runner.run(task, task.getOptionMap(), exeId, exeLogger);
            // flush log
            exeLogger.flush();
            // update the execution?
            Integer exportState = getExportState(sqlId, exeLogger.getResultRows());
            
            String srcfile = "";
            try
            {
                //压缩文件
                String path = Config.get("task.exe.result.dir");
                String file = path + File.separator + exeId;
                System.out.println(path);
                System.out.println(file);
                //重命名
    			File fileOld = new File(file);
    			File fileNew = new File(fileOld.getPath()+ ".txt");
    			boolean a =fileOld.renameTo(fileNew);
    			srcfile = fileNew.getPath();
                String dstfile = path + File.separator + exeId + ".zip";
                //文件超过4G可能无法压缩
                Util.compress(srcfile, dstfile);
            }
            catch (Exception e)
            {
                log.error("compress file {} failed", srcfile, e);
            }
            
            exePersist.updateExecution(exeId, "complete", exeLogger.getResultRows(), exportState);
            log.debug("Complete exeute sql.");
        }
        catch (Exception e)
        {
            log.error("Exeute sql error.", e);
        }
    }
    
    //获取导出状态
    private Integer getExportState(Integer sqlId, long rows)
        throws Exception
    {
        //默认是禁止导出
        Integer exportState = ExportState.FORBID_EXPORT;
        if (rows > Long.parseLong(Config.get("max.result.download.num")))
        {
            if (haveExportPrivilege(sqlId))
            {
                exportState = ExportState.APPLY_EXPORT;
            }
        }
        else
        {
            //5000条一下自由导出
            exportState = ExportState.UNLIMIT_EXPORT;
        }
        
        return exportState;
    }
    
    //新增sql信息，获取sqlId
    private Integer addGetSqlId(String user, String stmt)
        throws Exception
    {
        SqlInfoPersist sqlPersist = new SqlInfoPersist();
        Integer sqlId = sqlPersist.getSqlId(user, stmt);
        //通过user和stmt来判断sql信息是否存在
        if (null == sqlId)
        {
            sqlId = sqlPersist.addSqlInfo(user, stmt);
        }
        
        return sqlId;
    }
    
    //是否具有导出权限
    private boolean haveExportPrivilege(Integer sqlId)
        throws Exception
    {
        SqlInfoPersist sqlPersist = new SqlInfoPersist();
        SqlInfo sqlInfo = sqlPersist.getSqlInfo(sqlId);
        if (null != sqlInfo)
        {
            return sqlInfo.isExportPrivilege();
        }
        
        return false;
    }
}
