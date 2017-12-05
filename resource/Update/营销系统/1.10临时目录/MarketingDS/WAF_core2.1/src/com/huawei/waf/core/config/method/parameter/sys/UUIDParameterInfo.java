package com.huawei.waf.core.config.method.parameter.sys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.huawei.util.JsonUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;

/**
 * @author l00152046
 * 
 */
public class UUIDParameterInfo extends ContextParameterInfo {
    private static final String PROPERTY_BASE64 = "base64";
    private static boolean DEFAULT_BASE64 = true;
    
    private String claim = "";
    private boolean base64 = DEFAULT_BASE64;
    
    @Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
        this.base64 = JsonUtil.getAsBool(para, PROPERTY_BASE64, base64);
        this.claim = "UUID," + (base64 ? "base64" : "hex");
    	return true;
    }
	
    @Override
    public Object getValue(MethodContext context) {
        Map<String, Object> paras = context.getParameters();
        
        String v = JsonUtil.getAsStr(paras, this.name, null);
        if(Utils.isStrEmpty(v)) {
            v = base64 ? Utils.genUUID_64() : Utils.bin2hex(Utils.genUUID());
            paras.put(this.name, v);
        }
        
        return v;
    }
    
    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
        return base64 ? Utils.genUUID_64() : Utils.bin2hex(Utils.genUUID());
    }
    
    @Override
    public String getClaim() {
    	return claim;
    }
    
    public static final void setDefaultBase64(boolean base64) {
        DEFAULT_BASE64 = base64;
    }
}
