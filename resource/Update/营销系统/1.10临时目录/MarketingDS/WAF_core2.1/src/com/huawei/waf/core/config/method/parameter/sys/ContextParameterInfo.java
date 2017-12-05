package com.huawei.waf.core.config.method.parameter.sys;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;

import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.run.MethodContext;

/**
 * 上下文参数，不做任何校验，一般是账号
 * IContextParameter 
 * @author l00152046
 * 
 */
abstract public class ContextParameterInfo extends ParameterInfo {
	private static final Logger LOG = LogUtil.getInstance();
	
    @Override
    public boolean check(MethodContext context, Object ele) {
        return true; //不必检查，从session中获得
    }
    
	@Override
    protected boolean checkExt(MethodContext context, Object ele) {
    	return true;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }
    
    @Override
    public void setToStatement(PreparedStatement statement, int idx, MethodContext context) throws SQLException {
    	Object v = this.getValue(context);
    	if(v == null) {
    		LOG.error("Fail to get {} from context", getName());
    		statement.setString(idx, null);
    	} else {
    		statement.setString(idx, v.toString());
    	}
    }
}
