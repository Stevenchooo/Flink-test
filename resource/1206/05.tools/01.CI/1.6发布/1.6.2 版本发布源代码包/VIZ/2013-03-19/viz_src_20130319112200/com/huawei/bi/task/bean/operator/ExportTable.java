package com.huawei.bi.task.bean.operator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.sql.Statement;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.hive.da.DbConnectionDA;
import com.huawei.bi.hive.domain.DbConnection;
import com.huawei.bi.task.bean.logger.IExeLogger;
import com.huawei.bi.task.bean.taskrunner.CommandUtil;
import com.huawei.bi.task.domain.Task;
import com.huawei.bi.util.Constant;
import com.huawei.bi.util.DBUtil;
import com.huawei.bi.util.Util;
import com.huawei.termcloud.uniaccount.crypt.CryptUtil;

public class ExportTable {
	private static Logger log = LoggerFactory.getLogger(ExportTable.class);
	
	/** 导出数据到文件
	 * @param task
	 * @param connId
	 * @param code
	 * @param exeLogger
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean exportTableData(Task task,int connId,String code,IExeLogger exeLogger,String tableName)
	{
	    //hive导出数据
	    if(connId == Constant.HIVE_CONN)
	    {
	        try
            {
	            String cloumns = Util.getCloumns(task, new String[]{tableName});
	            exeLogger.writeResult(cloumns);
            }
            catch (Exception e)
            {
                throw new ExportFileException("Use hive command get columns exception",e);
            }
	        
	        CommandUtil.run(task, code, task.getTaskId(), exeLogger);
	    }
	    else if(connId == Constant.MYSQL_CONN)
	    {
	        DbConnection dbConn = null;
	        Connection conn = null;
            Statement stat = null;
            ResultSet rs = null;
            try {
                dbConn = new DbConnectionDA().getDbConnection(connId);
                Class.forName(dbConn.getConnDriver());

                conn = DriverManager.getConnection(
                        dbConn.getConnUrl(), dbConn.getConnUser(),
                        CryptUtil.decryptForAESStr(dbConn.getConnPassword(), Constant.KET_WORD));
                stat = conn.createStatement();
                rs = stat.executeQuery(code);
                log.debug("Sql Execute Code : " + code.toString());
                ResultSetMetaData rsmd = rs.getMetaData();
                
                int colSize  = rsmd.getColumnCount();

                StringBuffer sb = new StringBuffer();
                
                for (int i = 1; i <= colSize; i++) {
                    sb.append(rsmd.getColumnName(i));
                    if(i < colSize)
                    {
                        sb.append("\t");
                    }
                }
                exeLogger.writeResult(sb.toString());
                StringBuffer resultSb = null;
                while (rs.next()) {
                    resultSb = new StringBuffer();
                    for (int i = 1; i <= colSize; i++) {
                        resultSb.append(rs.getString(i));
                        if (i != colSize) {
                            resultSb.append("\t");
                        }
                    }
                    exeLogger.writeResult(resultSb.toString());
                }
                exeLogger.writeDebug("Query date success.");
            } catch (Exception e) {
                log.error("Mysql Connection Txt Write File Error : ", e);
                try
                {
                    exeLogger.writeDebug(e.getMessage());
                }
                catch (IOException ioe)
                {
                    log.warn("Write debug logger exception.",ioe);
                }
                throw new ExportFileException("Use JDBC export file exception",e);
            } finally {
                DBUtil.close(rs, stat, conn);
            }
	    }
	    else
	    {
	        //not supprot
	        log.warn("Current not support connection type=" + connId);
	    }
	    return false;
	}
}
