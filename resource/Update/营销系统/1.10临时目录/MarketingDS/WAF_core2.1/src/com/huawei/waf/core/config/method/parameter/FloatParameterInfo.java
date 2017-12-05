package com.huawei.waf.core.config.method.parameter;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;

public class FloatParameterInfo extends ParameterInfo {
    private static final Logger LOG = LogUtil.getInstance();
    protected static final String PROPERTY_SCALE  = "scale";        //精度
    
    protected int scale = 0; //精度 
	protected float min = -Float.MAX_VALUE;
	protected float max = Float.MAX_VALUE;
	protected Float defaultVal = null;
	private String claim = "";

	@Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
		this.max = JsonUtil.getAsFloat(para, PROPERTY_MAX, this.max);
		this.min = JsonUtil.getAsFloat(para, PROPERTY_MIN, this.min);
    	
		this.scale = JsonUtil.getAsInt(para, PROPERTY_SCALE, this.scale);
		if(this.scale > 7) {
		    LOG.error("Invalid {} {}, must be smaller than 7", PROPERTY_SCALE, this.scale);
		    return false;
		}
		
    	if(para.containsKey(PROPERTY_DEFAULT)) {
    		this.defaultVal = JsonUtil.getAsFloat(para, PROPERTY_DEFAULT);
    	}
    	this.claim = "Float,between " +  min + " and " + max;
    	
    	return true;
	}

	@Override
    protected boolean checkExt(MethodContext context, Object ele) {
		float v = 0;
        if(ele instanceof Number) {
            v = ((Number)ele).floatValue();
        } else {
            try {
                v = Float.parseFloat(ele.toString());
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
            float v = Utils.parseFloat(val, 0f);
            
            if(scale > 0) {
                BigDecimal b = new BigDecimal(v);
                v = b.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue(); //表明四舍五入，保留scale位小数 
            }
    		statement.setFloat(idx, v);
		} else {
    		statement.setNull(idx, java.sql.Types.FLOAT);
		}
	}

    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
    	float val = result.getFloat(idx);
    	
    	if(!result.wasNull()) {
    	    if(scale > 0) {
    	        BigDecimal b = new BigDecimal(val);
    	        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue(); //表明四舍五入，保留scale位小数 
    	    }
    		return val;
    	} else if(this.defaultVal != null) {
    		return this.defaultVal;
    	} else {
    		return 0f;
    	}
    }
    
    @Override
    public Object getValue(MethodContext context) {
        Object v = context.getParameter(this.name);
        if(v != null) {
            return Utils.parseFloat(v, 0f);
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
