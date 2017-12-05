package com.huawei.waf.core.config.method.parameter.sys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.huawei.util.JsonUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.parameter.DatetimeParameterInfo;
import com.huawei.waf.core.config.sys.SecurityConfig;
import com.huawei.waf.core.run.MethodContext;

/**
 * 上下文参数，不做任何校验，一般是账号
 * IContextParameter 
 * @author l00152046
 * 
 */
public class PwdExpireTimeParameterInfo extends NowParameterInfo {
    @Override
    public Object getValue(MethodContext context) {
        Map<String, Object> params = context.getParameters();
        String exp = JsonUtil.getAsStr(params, this.name, null);
        if(Utils.isStrEmpty(exp)) {
            exp = DatetimeParameterInfo.toString(DatetimeParameterInfo.addDay(SecurityConfig.getMaxValidDayNum()), this.format);
            params.put(getName(), exp);
        }
        return exp;
    }
    
    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
        return DatetimeParameterInfo.toString(DatetimeParameterInfo.addDay(SecurityConfig.getMaxValidDayNum()), this.format);
    }
    
    @Override
    public String getClaim() {
    	return "PwdExpireTime";
    }
}
