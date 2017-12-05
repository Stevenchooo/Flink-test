package com.huawei.waf.core.config.method.process;

import java.util.Map;
import org.slf4j.Logger;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;

public class SimpleRDBProcessConfig extends SelfRDBProcessConfig {
    private static final Logger LOG = LogUtil.getInstance();
    
	private static final String PARAMETER_SQL = "sql";
	
	private String sql = "";
	
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
	    
        if(sql.matches("^\\s*\\{\\s*call\\s+.*\\}\\s*$")) { //判断是否为存储过程
            LOG.error("Simple RDB, can't use stored-procedure");
            return false; //不可以使用存储过程
        }
		return true;
	}
	
	public String getSQL() {
		return sql;
	}
}
