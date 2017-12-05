package com.huawei.waf.core.config.method.parameter.sys;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.sys.SecurityConfig;
import com.huawei.waf.core.run.MethodContext;

/**
 * @author l00152046
 * 
 */
public class PwdDuplicateNumParameterInfo extends ContextParameterInfo {
    @Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
    	return true;
    }
	
	@Override
	public void setToStatement(PreparedStatement statement, int idx, MethodContext context) throws SQLException {
        statement.setInt(idx, SecurityConfig.getMaxDupPwdNum());
	}

    @Override
    public Object getValue(MethodContext context) {
        int num = SecurityConfig.getMaxDupPwdNum();
        context.setParameter(this.name, num);
        return num;
    }
    
    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
        return SecurityConfig.getMaxDupPwdNum();
    }
    
    @Override
    public String getClaim() {
    	return "MaxDupPwdNum";
    }
}
