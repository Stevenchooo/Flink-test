package com.huawei.waf.core.config.method.parameter.sys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;

/**
 * @author l00152046
 * 
 */
public class ClientIpParameterInfo extends ContextParameterInfo {
    @Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
    	return true;
    }
	
    @Override
    public Object getValue(MethodContext context) {
        String v = context.getClientIp();
        context.setParameter(this.name, v);
        return v;
    }
    
    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
        return "";
    }
    
    @Override
    public String getClaim() {
    	return "ClientIp";
    }
}
