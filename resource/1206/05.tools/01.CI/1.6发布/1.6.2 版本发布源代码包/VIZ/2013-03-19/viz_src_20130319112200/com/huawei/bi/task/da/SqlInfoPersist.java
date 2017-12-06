package com.huawei.bi.task.da;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.task.domain.SqlInfo;
import com.huawei.bi.util.DBUtil;

public class SqlInfoPersist implements ISqlInfoPersist
{
    private static final Logger LOG = LoggerFactory.getLogger(SqlInfoPersist.class);
    
    /**
     * 新增sql信息
     * @param user 用户
     * @param sqlStmt sql语句
     * @return 自增长Id
     * @throws Exception 异常
     */
    @Override
    public Integer addSqlInfo(String user, String sqlStmt)
        throws Exception
    {
        String sqlInsert = "INSERT INTO sqlInfo (user, sqlStmt) VALUES (?,?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Integer sqlId = null;
        try
        {
            conn = DBUtil.getDataSource().getConnection();
            stmt = conn.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
            //填充参数
            stmt.setString(1, user);
            stmt.setString(1 + 1, sqlStmt);
            stmt.execute();
            rs = stmt.getGeneratedKeys();
            //获取自增长Id
            sqlId = rs.next() ? rs.getInt(1) : null;
        }
        catch (Exception e)
        {
            LOG.error("addSqlInfo error! user is {}, sqlStmt is {}", new Object[] {user, sqlStmt, e});
            throw e;
        }
        finally
        {
            //关闭连接
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        
        return sqlId;
    }
    
    /**
     * 更新申请原因
     * @param sqlId id
     * @param user 用户
     * @param exportReason 大数据导出原因
     * @throws Exception 异常
     */
    @Override
    public void updateExportReason(Integer sqlId, String user, String exportReason)
        throws Exception
    {
        String updateSql = "update sqlInfo set exportReason=? where sqlId=? and user=?";
        QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
        qr.update(updateSql, exportReason,sqlId, user);
    }
    
    /**
     * 通过user和stmt获取sqlId
     * @param user 用户
     * @param stmt sql语句
     * @return sqlId
     * @throws Exception 异常
     */
    @Override
    public Integer getSqlId(String user, String stmt)
        throws Exception
    {
        String sqlSelect = "select sqlId from sqlInfo where user=? and sqlStmt=?";
        QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
        return (Integer)qr.query(sqlSelect, new ScalarHandler(), user, stmt);
    }
    
    /**
     * 通过sqlId获取SqlInfo
     * @param sqlId id
     * @return sqlInfo
     * @throws Exception 异常
     */
    @Override
    public SqlInfo getSqlInfo(Integer sqlId)
        throws Exception
    {
        String sqlSelect = "select * from sqlInfo where sqlId=?";
        QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
        return qr.query(sqlSelect, new BeanHandler<SqlInfo>(SqlInfo.class), sqlId);
    }
    
    /**
     * 删除sql信息
     * @param sqlId id
     * @throws Exception 异常
     */
    @Override
    public void deleteSqlInfo(Integer sqlId)
        throws Exception
    {
        String sqlSelect = "delete from sqlInfo where sqlId=?";
        QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
        qr.update(sqlSelect, sqlId);
    }
}
