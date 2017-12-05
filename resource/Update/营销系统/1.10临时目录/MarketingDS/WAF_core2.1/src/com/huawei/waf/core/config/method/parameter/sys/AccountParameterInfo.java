package com.huawei.waf.core.config.method.parameter.sys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;

/**
 * @author l00152046
 * 
 */
public class AccountParameterInfo extends ContextParameterInfo {
    private String claim;
    
    @Override
    public boolean check(MethodContext context, Object ele) {
        String account = context.getAccount();
        return !Utils.isStrEmpty(account);
    }
    
    @Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
        this.claim = getName() + ",Account"; 
    	return true;
    }
	
    @Override
    public Object getValue(MethodContext context) {
        String account = context.getAccount();
        context.setParameter(getName(), account);
        return account;
    }
    
    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
        return "";
    }
    
    @Override
    public String getClaim() {
    	return claim;
    }
}
