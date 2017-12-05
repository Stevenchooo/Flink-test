package com.huawei.waf.core.config.method.parameter.sys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.StringParameterInfo;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.Const;

/**
 * @author l00152046
 * MD5，如果配置了src，则md5(src参数)，否则md5(本参数)
 */
public class MD5ParameterInfo extends StringParameterInfo {
    private static Logger LOG = LogUtil.getInstance();
    
    private static final String PARAMETER_SRC = "src";
    private String src = null;
    
    @Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
        if(!super.parseExt(version, para, mc)) {
            return false;
        }
        
        this.src = JsonUtil.getAsStr(para, PARAMETER_SRC, null);
        if(this.src != null && this.src.equals("")) {
            LOG.error("Invalid {} config item in parameter {}", PARAMETER_SRC, this.name);
            return false;
        }
        
    	return true;
    }
	
    @Override
    public Object getValue(MethodContext context) {
        String n;
        if(this.src != null) { //如果未设置src，则取当前的参数
            n = this.src;
        } else {
            n = this.name;
        }
        
        String str = JsonUtil.getAsStr(context.getParameters(), n, null);
        if(Utils.isStrEmpty(str)) {
            throw new RuntimeException("There are no " + this.src + " parameter");
        }
        
        return Utils.bin2base64(Utils.md5(str.getBytes(Const.DEFAULT_CHARSET)));
    }
    
    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
        return "";
    }
    
    @Override
    public String getClaim() {
    	return "MD5 parameter";
    }
}
