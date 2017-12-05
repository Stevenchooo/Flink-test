package com.huawei.waf.core.config.method.process;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;

public class RDBProcessConfig extends SelfRDBProcessConfig {
    private static final Logger LOG = LogUtil.getInstance();
    
	private static final String PARAMETER_SQL = "sql";
    private static final String PARAMETER_NEEDRESULT = "needResult";
	
	private String sql = "";
	private boolean storedProcedure = false;
	private boolean needResult = false;
	
	@Override
    protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc) {
	    if(!super.parseExt(ver, json, mc)) {
	        return false;
	    }
	    
	    sql = JsonUtil.getAsStr(json, PARAMETER_SQL, sql);
	    if(Utils.isStrEmpty(sql)) {
            LOG.error("{} should be prompted in {}", PARAMETER_SQL, mc.getName());
	        return false;
	    }
	    
        if(!sql.matches("^\\s*\\{\\s*call\\s+.*\\}\\s*$")) { //判断是否为存储过程
            return true;
        }
        
        this.storedProcedure = true;
        
        /**
         * 设置了是否需要返回码，则直接返回，
         * 因为oracle通过registerOutParameter判断不出来
         */
        if(json.containsKey(PARAMETER_NEEDRESULT)) {
            this.needResult = JsonUtil.getAsBool(json, PARAMETER_NEEDRESULT, this.needResult);
            return true;
        }
        
        LOG.info("{} is stored-procedure, do something to judge whether the first parameter is out-put or not", sql);
        DBUtil.DBConnection dbConn = DBUtil.getConnection(getDataSource(), false); //判断dataSource是否有效
        if (dbConn == null) {
            LOG.error("Invalid data source {} in method {}", getDataSource(), mc.getName());
            return false;
        }

        CallableStatement statement = null;
        //存储过程，判断是否需要返回码
        try {
            Connection conn = dbConn.getConnection();
            //通过检查存储过程中的第一个参数是否为out类型，来判断是否需要返回码
            if ((statement = conn.prepareCall(sql)) == null) {
                LOG.error("Fail to create statement for {}", sql);
                return false;
            }
            statement.registerOutParameter(1, Types.INTEGER);
            needResult = true;
            LOG.info("{}, the first parameter is out-put return code", sql);
        } catch(SQLException e) {
            LOG.info("There are no return-code in sql {}", sql);
            needResult = false;
        } catch(Exception e) {
            LOG.error("Fail to test sql {}", sql, e);
            return false;
        } finally {
            //必须关闭，否则资源未释放，且tomcat不能正常关闭
            DBUtil.closeStatement(statement);
            DBUtil.freeConnection(dbConn, false);
        }
        
		return true;
	}
	
	public String getSQL() {
		return sql;
	}
	
	/**
	 * 从sql的配置中可以判断是否存储过程
	 * @return
	 */
	public boolean isStoredProcedure() {
	    return storedProcedure;
	}

    /* 
     * 是否需要从存储过程中返回执行结果retCode，从sql定义中自动获得
     */
    public boolean isNeedResult() {
        return needResult;
    }
}
