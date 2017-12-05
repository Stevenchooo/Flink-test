package com.huawei.waf.core.config.method.parameter.sys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.huawei.util.JsonUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.DatetimeParameterInfo;
import com.huawei.waf.core.run.MethodContext;

/**
 * 上下文参数，不做任何校验，一般是账号
 * IContextParameter 
 * @author l00152046
 * 
 */
public class NowParameterInfo extends ContextParameterInfo {
    private static final String DEFAULT_INNER_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ssZ";
    
    private static final String PROPERTY_FORMAT = "format";
    private static final String PROPERTY_INNER_FORMAT = "innerFormat";
    
    protected String format = null;
    protected String dbFormat = null;
    
    @Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
        this.format =JsonUtil.getAsStr(para, PROPERTY_FORMAT, DEFAULT_FORMAT);
        this.dbFormat = JsonUtil.getAsStr(para, PROPERTY_INNER_FORMAT, DEFAULT_INNER_FORMAT);

    	return true;
    }
	
    @Override
    public Object getValue(MethodContext context) {
        Map<String, Object> params = context.getParameters();
        String now = JsonUtil.getAsStr(params, this.name, null);
        if(Utils.isStrEmpty(now)) {
            now = DatetimeParameterInfo.toString(new java.util.Date(), this.format);
            params.put(getName(), now);
        }
        return now;
    }
    
    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
        return DatetimeParameterInfo.toString(new java.util.Date(), this.format);
    }
    
    @Override
    public String getClaim() {
    	return "Now";
    }
}
