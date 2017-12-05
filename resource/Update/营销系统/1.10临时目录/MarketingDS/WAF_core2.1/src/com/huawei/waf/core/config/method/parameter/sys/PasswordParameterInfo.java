package com.huawei.waf.core.config.method.parameter.sys;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.SecureUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.StringParameterInfo;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * @author l00152046
 * 密码参数，需要从其他字段中获取一个值来共同sha，只能作为输入参数
 */
public class PasswordParameterInfo extends StringParameterInfo {
    private static Logger LOG = LogUtil.getInstance();
    
    private static final String PARAMETER_PARTNER = "partner";
    private static final String PARAMETER_EQUALSTO = "equalsTo";
    private static final String PARAMETER_VERIFY = "verify";
    
    private String partner = null;
    private String equalsTo = null; //用于确认密码
    private boolean verify = false; //校验密码是否符合要求
    
    @Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
        if(!super.parseExt(version, para, mc)) {
            return false;
        }
        
        this.verify = JsonUtil.getAsBool(para, PARAMETER_VERIFY, this.verify);
        this.partner = JsonUtil.getAsStr(para, PARAMETER_PARTNER, null);
        if(this.verify && Utils.isStrEmpty(this.partner)) {
            LOG.error("There must be {} when need verify in parameter {}", PARAMETER_PARTNER, this.name);
            return false;
        }
        
        if(this.partner != null && this.partner.equals("")) {
            LOG.error("Invalid {} config item in parameter {}", PARAMETER_PARTNER, this.name);
            return false;
        }
        
        this.equalsTo = JsonUtil.getAsStr(para, PARAMETER_EQUALSTO, null);
        if(this.equalsTo != null && this.equalsTo.equals("")) {
            LOG.error("Invalid {} config item in parameter {}", PARAMETER_EQUALSTO, this.name);
            return false;
        }
        
    	return true;
    }
	
	@Override
	public void setToStatement(PreparedStatement statement, int idx, MethodContext context) throws SQLException {
	    Object o = this.getValue(context);
	    if(o == null) {
	        statement.setNull(idx, java.sql.Types.VARCHAR);
	    } else {
	        statement.setString(idx, o.toString());
	    }
	}

    @Override
    public Object getValue(MethodContext context) {
        Object o = super.getValue(context);
        if(o == null) {
            return null;
        }
        
        String pwd = o.toString();
        if(pwd.equals("")) {
            return null;
        }
        
        if(this.partner != null) {
            Object partner = context.getParameter(this.partner);
            if(partner == null) {
                return null;
            }
            
            pwd = SecureUtil.encodePassword(partner.toString(), pwd);
        } else {
            pwd = SecureUtil.encodePassword("", pwd);
        }
        
        return pwd;
    }
    
    @Override
    protected boolean checkExt(MethodContext context, Object ele) {
    	ele = context.getParameter(this.name);
        if(!super.checkExt(context, ele)) {
            return false;
        }
        
        Map<String, Object> params = context.getParameters();
        String pwd = Utils.parseString(ele, "");
        if(this.verify) { //校验密码是否符合要求
            String acc = JsonUtil.getAsStr(params, this.partner, "");
            if(Utils.isStrEmpty(acc)) {
                context.setResult(RetCode.WRONG_PARAMETER, "No " + this.partner + " parameter");
                return false;
            }
          
            int retCode = SecureUtil.isValidPassword(acc, pwd);
            if(retCode != RetCode.OK) {
                context.setResult(retCode);
                return false;
            }
        }
        
        if(Utils.isStrEmpty(this.equalsTo)) {
            return true;
        }
        String equalsToVal = JsonUtil.getAsStr(params, this.equalsTo);
        
        return pwd.equals(equalsToVal);
    }
    
    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
        return "";
    }
    
    @Override
    public String getClaim() {
    	return "Password parameter";
    }
}
