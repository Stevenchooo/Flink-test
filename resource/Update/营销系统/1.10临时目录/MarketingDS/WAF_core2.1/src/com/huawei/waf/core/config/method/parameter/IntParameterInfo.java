package com.huawei.waf.core.config.method.parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.huawei.util.JsonUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;

public class IntParameterInfo extends ParameterInfo {
	protected int min = Integer.MIN_VALUE;
	protected int max = Integer.MAX_VALUE;
	protected Integer defaultVal = null;
	private String claim = "";
    
    @Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
		this.max = JsonUtil.getAsInt(para, PROPERTY_MAX, this.max);
		this.min = JsonUtil.getAsInt(para, PROPERTY_MIN, this.min);
    	
    	if(para.containsKey(PROPERTY_DEFAULT)) {
    		this.defaultVal = JsonUtil.getAsInt(para, PROPERTY_DEFAULT);
    	}
    	this.claim = "Integer,between " +  min + " and " + max;
    	
    	return true;
    }
	
	@Override
    protected boolean checkExt(MethodContext context, Object ele) {
		int v = 0;

        if(ele instanceof Number) {
            v = ((Number)ele).intValue();
        } else {
            try {
                v = Integer.parseInt(ele.toString());
            } catch(Exception e) {
                return false;
            }
        }
		
    	if(v < this.min || v > this.max) {
    		return false;
    	}
    	
    	return true;
    }

	@Override
	public void setToStatement(PreparedStatement statement, int idx, MethodContext context) throws SQLException {
        Object val = this.getValue(context);
    	if(val != null) {
    		statement.setInt(idx, Utils.parseInt(val, 0));
		} else {
    		statement.setNull(idx, java.sql.Types.INTEGER);
		}
	}

    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
    	int val = result.getInt(idx);
    	
    	if(!result.wasNull()) {
    	    if(this.maps != null) {
    	        return this.maps.get(Integer.toString(val));
    	    }
    		return val;
    	} else if(this.defaultVal != null) {
    		return this.defaultVal;
    	} else {
    		return 0;
    	}
    }

    @Override
    public Object getValue(MethodContext context) {
        Object v = context.getParameter(this.name);
        if(v != null) {
            if(this.maps != null && this.maps.containsKey(v)) {
                return this.maps.get(v);
            }
            return Utils.parseInt(v, 0);
        } else if(this.defaultVal != null) {
            return this.defaultVal;
        } else {
            return null;
        }
    }
    
    @Override
    public String getClaim() {
    	return claim;
    }
}
