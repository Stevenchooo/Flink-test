package com.huawei.waf.core.config.method.parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.Methods;
import com.huawei.waf.core.run.MethodContext;

/**
 * 对象类型，object配置项必须为一个CompoundParameterInfo，
 * 否则ObjectParameterInfo将没有存在的意义
 * CompoundParameterInfo必须在method的types中定义，
 * 或者在typedef.cfg中定义全局的CompoundParameterInfo
 * @author l00152046
 *
 */
public class ObjectParameterInfo extends ParameterInfo {
	private static final Logger LOG = LogUtil.getInstance();
	
	private static final String PROPERTY_OBJECT = "object";
	
	private ParameterInfo object = null;
	private String claim = "";
	
	@Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
		if(!para.containsKey(PROPERTY_OBJECT)) {
			LOG.error("No {} config item in object parameter {}", PROPERTY_OBJECT, this.name);
			return false;
		}
		
        if(this.options != null) {
            LOG.error("Can't set {} in object parameter {}", PROPERTY_OPTIONS, this.name);
            return false;
        }
		
        String type = JsonUtil.getAsStr(para, PROPERTY_OBJECT, null);
		if(mc == null) { //读取全局对象定义时，并不知道method
            this.object = Methods.getGlobalType(version, type);
        } else {
            this.object = mc.getType(type);
        }
		
        //对象必须是定义过的，否则毫无存在的意义
		if(this.object == null) {
			LOG.error("Object {} not defined in {}", type, this.name);
			return false;
		}
		
		this.claim = "Object wanted, " + type;
		
		return true;
	}

	@Override
    protected boolean checkExt(MethodContext context, Object ele) {
		return this.object.check(context, ele);
	}

	/**
	 * 因为对象是复杂类型，不可以直接设置到数据库statement中，只能转为字符串处理
	 * @see com.huawei.waf.core.config.method.parameter.ParameterInfo#setToStatement(java.sql.PreparedStatement, int, java.lang.Object)
	 */
	@Override
	public void setToStatement(PreparedStatement statement, int idx, MethodContext context) throws SQLException {
	    Object val = context.getParameter(this.name);
        if(val != null) {
            statement.setString(idx, val.toString());
        } else {
            statement.setNull(idx, java.sql.Types.VARCHAR);
        }
	}

    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
        return null;
    }
    
    @Override
    public String getClaim() {
    	return claim;
    }
    
    @Override
    public boolean isPrimitive() {
    	return false;
    }
    
    @Override
    public Object getValue(MethodContext context) {
        return context.getParameter(this.name);
    }
}
