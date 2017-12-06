package com.huawei.bi.task.da;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.task.domain.ExecuteKey;
import com.huawei.bi.task.domain.Execution;
import com.huawei.bi.util.Config;
import com.huawei.bi.util.DBUtil;
import com.huawei.bi.util.Util;

public class DbExePersist implements IExecutionPersist
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DbExePersist.class);
    
    private static final int RESERVED_DAYS = Integer.parseInt(Config.get("execute.history.outdate.days"));
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void deleteOutdateExecution()
    {
        List<ExecuteKey> exeKeys = null;
        try
        {
            Date now = new Date();
            //获取过期的执行id列表
            String sqlQuery = "select exeId from execution where startAt < DATE_ADD(?,INTERVAL ? DAY);";
            QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
            exeKeys = (List<ExecuteKey>)qr.query(sqlQuery, new BeanListHandler(ExecuteKey.class), now, -RESERVED_DAYS);
            //删除历史文件(调试文件、结果文件、结果压缩文件)
            if (null != exeKeys && !exeKeys.isEmpty())
            {
                //删除执行历史
                String sqlDelete = "delete from execution where startAt < DATE_ADD(?,INTERVAL ? DAY);";
                qr.update(sqlDelete, now, -RESERVED_DAYS);
                
                String rpath = Config.get("task.exe.result.dir");
                String dpath = Config.get("task.exe.debug.dir");
                String rzipFile = null;
                String rtxtFile = null;
                String ttxtFile = null;
                for (ExecuteKey exeKey : exeKeys)
                {
                    ttxtFile = dpath + File.separator + exeKey.getExeId();
                    rtxtFile = rpath + File.separator + exeKey.getExeId();
                    rzipFile = rtxtFile + ".zip";
                    Util.deleteFile(ttxtFile);
                    Util.deleteFile(rtxtFile);
                    Util.deleteFile(rzipFile);
                }
            }
            LOGGER.debug("deleteOutdateExecution sucess.  exeIds is {}.", exeKeys);
        }
        catch (Exception e)
        {
            LOGGER.error("deleteOutdateExecution failed! exeIds is {}", exeKeys, e);
        }
    }
    
    @Override
    public void addExecution(Execution execution)
        throws Exception
    {
        
        String sqlInsert =
            "INSERT INTO execution (exeId, taskId, executionType, options, startAt, endAt, status,sqlId,user,code) VALUES (?,?,?,?,?,?,?,?,?,?)";
        
        QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
        qr.update(sqlInsert,
            execution.getExeId(),
            execution.getTaskId(),
            execution.getExecutionType(),
            execution.getOptions(),
            execution.getStartAt(),
            execution.getEndAt(),
            execution.getStatus(),
            execution.getSqlId(),
            execution.getUser(),
            execution.getCode());
    }
    
    @Override
    public void updateExecution(String exeId, String status, long records, Integer exportState)
        throws Exception
    {
        String sqlUpdate = "UPDATE execution SET endAt = ?, STATUS = ?,recordNumber=?,exportState=? WHERE exeId = ?";
        
        QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
        qr.update(sqlUpdate, new Date(), status, records, exportState, exeId);
    }
    
    @Override
    public Execution getExecution(String exeId)
        throws Exception
    {
        String sqlQuery = "select * from execution where exeId=?";
        QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
        return qr.query(sqlQuery, new BeanHandler<Execution>(Execution.class), exeId);
    }
    
    @Override
    public void deleteExecution(String exeId)
        throws Exception
    {
        String sqlDelete = "delete from execution WHERE exeId = ?";
        
        QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
        qr.update(sqlDelete, exeId);
    }
    
    @Override
    public void updateExportState(String exeId, String user, int applyExport)
        throws Exception
    {
        String sqlUpdate = "UPDATE execution SET exportState=? WHERE exeId = ? and user= ?";
        
        QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
        qr.update(sqlUpdate, applyExport, exeId, user);
    }
}
