package com.huawei.waf.core.config.method.parameter;

import java.sql.*;
import java.util.Map;

import com.huawei.util.JsonUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;

public class BoolParameterInfo extends ParameterInfo {
	protected Boolean defaultVal = null;
    
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
    	if(para.containsKey(PROPERTY_DEFAULT)) {
    		this.defaultVal = JsonUtil.getAsBool(para, PROPERTY_DEFAULT);
    	}
    	
    	return true;
    }
	
	@Override
    protected boolean checkExt(MethodContext context, Object ele) {
        if(ele instanceof Boolean) {
            return true;
        } else {
            try {
                Boolean.parseBoolean(ele.toString());
            } catch(Exception e) {
                return false;
            }
        }

    	return true;
    }

	@Override
	public void setToStatement(PreparedStatement statement, int idx, MethodContext context) throws SQLException {
	    Object val = this.getValue(context);
    	if(val != null) {
    		statement.setBoolean(idx, Utils.parseBool(val, false));
		} else {
    		statement.setNull(idx, java.sql.Types.BOOLEAN);
		}
	}

    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
    	boolean val = result.getBoolean(idx);
    	
    	if(!result.wasNull()) {
    		return val;
    	} else if(this.defaultVal != null) {
    		return this.defaultVal;
    	} else {
    		return false;
    	}
    }

    @Override
    public Object getValue(MethodContext context) {
        Object o = context.getParameter(this.name);
        if(o != null) {
            return Utils.parseBool(o, false);
        } else if(this.defaultVal != null) {
            return this.defaultVal;
        } else {
            return null;
        }
    }
    
    @Override
    public String getClaim() {
    	return "Boolean,true|false|1|0";
    }
}
