package com.huawei.bi.task.da;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.hive.da.DbConnectionDA;
import com.huawei.bi.hive.domain.DbConnection;
import com.huawei.bi.util.Constant;
import com.huawei.termcloud.uniaccount.crypt.CryptUtil;

public class HiveMetaDA
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HiveMetaDA.class);
    
    public HiveMetaDA()
    {
    }
    
    private Connection getConn(int connId)
        throws Exception
    {
        DbConnection dbConn = new DbConnectionDA().getDbConnection(connId);
        Class.forName(dbConn.getConnDriver());
        
        Connection conn =
            DriverManager.getConnection(dbConn.getConnUrl(),
                dbConn.getConnUser(),
                CryptUtil.decryptForAESStr(dbConn.getConnPassword(), Constant.KET_WORD));
        return conn;
    }
    
    public String getTableDesc(int connId, String dbName, String tableName)
    {
        StringBuilder sbDesc = new StringBuilder();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConn(connId);
            stmt = conn.createStatement();
            Integer tblId = null;
            Integer sdId = null;
            rs =
                stmt.executeQuery("select tbls.TBL_ID,tbls.SD_ID,FROM_UNIXTIME(tbls.CREATE_TIME) as CREATE_TIME,tbls.OWNER,tbls.TBL_TYPE from tbls,dbs where tbls.db_id=dbs.db_id and dbs.NAME='"
                    + dbName + "' and tbls.TBL_NAME='" + tableName + "';");
            if (rs.next())
            {
                sbDesc.append(dbName + "." + tableName);
                sbDesc.append("<br/><table class='simple'><tr><td>");
                sbDesc.append("create time:</td><td>");
                sbDesc.append(rs.getString("CREATE_TIME"));
                sbDesc.append("</td></tr><tr><td>");
                sbDesc.append("owner:");
                sbDesc.append("</td><td>");
                sbDesc.append(rs.getString("OWNER"));
                sbDesc.append("</td></tr><tr><td>");
                sbDesc.append("table type:");
                sbDesc.append("</td><td>");
                sbDesc.append(rs.getString("TBL_TYPE"));
                sbDesc.append("</td></tr>");
                sbDesc.append("</table>");
                
                tblId = rs.getInt("TBL_ID");
                sdId = rs.getInt("SD_ID");
            }
            
            if (null != sdId)
            {
                rs =
                    stmt.executeQuery("select column_name,type_name,comment from columns where columns.sd_id=" + sdId
                        + " order by integer_idx");
                sbDesc.append("<br/>fields:<br/>");
                sbDesc.append("<table class='simple'>");
                while (rs.next())
                {
                    sbDesc.append("<tr><td>");
                    sbDesc.append(rs.getString("column_name"));
                    sbDesc.append("</td><td>");
                    sbDesc.append(rs.getString("type_name"));
                    sbDesc.append("</td>");
                    if (null != rs.getObject("comment"))
                    {
                        sbDesc.append("<td>");
                        sbDesc.append(rs.getString("comment"));
                        sbDesc.append("</td>");
                    }
                    sbDesc.append("</tr>");
                }
                sbDesc.append("</table>");
            }
            
            if (null != tblId)
            {
                rs =
                    stmt.executeQuery("select PKEY_NAME,PKEY_TYPE,PKEY_COMMENT from partition_keys where TBL_ID="
                        + tblId + " order by INTEGER_IDX;");
                sbDesc.append("<br/>partitions:<br/>");
                sbDesc.append("<table class='simple'>");
                while (rs.next())
                {
                    sbDesc.append("<tr><td>");
                    sbDesc.append(rs.getString("PKEY_NAME"));
                    sbDesc.append("</td><td>");
                    sbDesc.append(rs.getString("PKEY_TYPE"));
                    sbDesc.append("</td>");
                    if (null != rs.getObject("PKEY_COMMENT"))
                    {
                        sbDesc.append("<td>");
                        sbDesc.append(rs.getString("PKEY_COMMENT"));
                        sbDesc.append("</td>");
                    }
                    sbDesc.append("</tr>");
                }
                sbDesc.append("</table>");
            }
            
            if (null != tblId)
            {
                rs = stmt.executeQuery("select PART_NAME from partitions where TBL_ID=" + tblId);
                sbDesc.append("<br/>partition values:<br/>");
                sbDesc.append("<table class='simple'>");
                while (rs.next())
                {
                    sbDesc.append("<tr><td>");
                    sbDesc.append(rs.getString("PART_NAME"));
                    sbDesc.append("</td></tr>");
                }
                sbDesc.append("</table>");
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("error occured read db infomation:" + e);
        }
        finally
        {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        
        return sbDesc.toString();
    }
    
    public String getDbDesc(int connId, String dbName)
    {
        StringBuilder sbDesc = new StringBuilder();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConn(connId);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT `DESC`,`DB_LOCATION_URI` FROM DBS where `NAME`='" + dbName + "';");
            if (rs.next())
            {
                sbDesc.append(rs.getString("DESC"));
                sbDesc.append("<br/>");
                sbDesc.append(rs.getString("DB_LOCATION_URI"));
                sbDesc.append("<br/>parameters:<br/>");
            }
            
            rs =
                stmt.executeQuery("SELECT a.PARAM_KEY,a.PARAM_VALUE FROM database_params a,DBS b where a.DB_ID=b.DB_ID and b.NAME='"
                    + dbName + "'");
            while (rs.next())
            {
                sbDesc.append("&nbsp&nbsp" + rs.getString("PARAM_KEY"));
                sbDesc.append(':');
                sbDesc.append(rs.getString("PARAM_VALUE"));
                sbDesc.append("<br/>");
            }
            
        }
        catch (Exception e)
        {
            LOGGER.error("error occured read db infomation:" + e);
        }
        finally
        {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        
        return sbDesc.toString();
    }
    
    public List<String> getDBs(int connId)
        throws Exception
    {
        List<String> dbList = new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConn(connId);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT NAME FROM DBS ORDER BY NAME;");
            while (rs.next())
            {
                String name = rs.getString("NAME");
                dbList.add(name);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("error occured read db infomation:" + e);
        }
        finally
        {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return dbList;
    }
    
    public List<String> getTables(int connId, String dbName)
        throws Exception
    {
        
        List<String> tabList = new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConn(connId);
            stmt = conn.createStatement();
            rs =
                stmt.executeQuery("SELECT T.TBL_NAME FROM TBLS T,DBS D WHERE D.DB_ID = T.DB_ID AND D.NAME='" + dbName
                    + "' ORDER BY T.TBL_NAME;");
            while (rs.next())
            {
                String name = rs.getString("TBL_NAME");
                tabList.add(name);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("error occured read " + dbName + "'s tables infomation:" + e);
        }
        finally
        {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return tabList;
        
    }
    
    public List<String> getPartitions(int connId, String dbName, String tabName)
        throws Exception
    {
        List<String> partList = new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConn(connId);
            stmt = conn.createStatement();
            rs =
                stmt.executeQuery("SELECT DISTINCT P.PART_NAME FROM TBLS T,PARTITIONS P,"
                    + " SDS S, PARTITION_KEY_VALS PK, DBS D WHERE T.TBL_ID = P.TBL_ID AND P.SD_ID = "
                    + "S.SD_ID  AND P.PART_ID = PK.PART_ID  AND T.TBL_NAME ='" + tabName + "' AND D.NAME='" + dbName
                    + "'  ORDER BY P.PART_NAME;");
            while (rs.next())
            {
                String name = rs.getString("PART_NAME");
                partList.add(name);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("error occured read " + tabName + "'s partition infomation:" + e);
        }
        finally
        {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return partList;
    }
}
