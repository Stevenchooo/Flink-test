package com.huawei.waf.core.config.method.parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.huawei.util.JsonUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;


public class LongParameterInfo extends ParameterInfo {
	protected long min = Long.MIN_VALUE;
	protected long max = Long.MAX_VALUE;
	protected Long defaultVal = null;
	private String claim = "";
    
    @Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
		this.max = JsonUtil.getAsLong(para, PROPERTY_MAX, this.max);
		this.min = JsonUtil.getAsLong(para, PROPERTY_MIN, this.min);
    	
    	if(para.containsKey(PROPERTY_DEFAULT)) {
    		this.defaultVal = JsonUtil.getAsLong(para, PROPERTY_DEFAULT);
    	}
    	this.claim = "Long,between " +  min + " and " + max;
    	
    	return true;
    }
	
    @Override
    protected boolean checkExt(MethodContext context, Object ele) {
    	long v = 0;
        if(ele instanceof Number) {
            v = ((Number)ele).longValue();
        } else {
            try {
                v = Long.parseLong(ele.toString());
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
    		statement.setLong(idx, Utils.parseLong(val, 0L));
		} else {
    		statement.setNull(idx, java.sql.Types.BIGINT);
		}
	}

    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
    	long val = result.getLong(idx);
    	
    	if(!result.wasNull()) {
    		return val;
    	} else if(this.defaultVal != null) {
    		return this.defaultVal;
    	} else {
    		return 0L;
    	}
    }
    
    @Override
    public Object getValue(MethodContext context) {
        Object o = context.getParameter(this.name);
        if(o != null) {
            return Utils.parseLong(o, 0L);
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
