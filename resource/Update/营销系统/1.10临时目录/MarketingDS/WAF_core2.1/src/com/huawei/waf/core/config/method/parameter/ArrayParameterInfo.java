package com.huawei.waf.core.config.method.parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.Methods;
import com.huawei.waf.core.run.MethodContext;

/**
 * 数组类型，数组中元素可以是基本类型，也可以是CompoundParameterInfo，
 * 如果是基本类型，需要用json对象定义一个完整的基本类型，形如：
 * object={"name":xxx,"type":"INT"...}
 * 如果是CompoundParameterInfo，则必须在method的types中定义，
 * 或者在typedef.cfg中定义全局的CompoundParameterInfo
 * 
 * @author l00152046
 *
 */
public class ArrayParameterInfo extends ParameterInfo {
	private static final Logger LOG = LogUtil.getInstance();
	private static final String PROPERTY_OBJECT = "object";
    private static final String PROPERTY_SEPARATOR = "separator";
    
    private String claim = "";
    
    private int min = 0;
    private int max = 100000;
	
	/**
	 * 分隔符，当array需要合并时，设置分隔符
	 */
	private char separator = ',';
	private ParameterInfo object = null;
	
	@SuppressWarnings("unchecked")
    @Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
        if(!para.containsKey(PROPERTY_OBJECT)) {
            LOG.error("No {} config item in array parameter {}", PROPERTY_OBJECT, this.name);
            return false;
        }
        
        if(this.options != null) {
            LOG.error("Can't set {} in array parameter {}", PROPERTY_OPTIONS, this.name);
            return false;
        }
        
		this.max = JsonUtil.getAsInt(para, PROPERTY_MAX, 100000);
		this.min = JsonUtil.getAsInt(para, PROPERTY_MIN, 0);
    	
	    Object type = para.get(PROPERTY_OBJECT);
	    if(type instanceof String) {
    		String typeName = JsonUtil.getAsStr(para, PROPERTY_OBJECT);
		    if(mc == null) { //读取全局对象定义时，并不知道method
		        this.object = Methods.getGlobalType(version, typeName);
		    } else {
		        this.object = mc.getType(typeName);
		    }
		    
            if(this.object == null) { //如果是个字符串，则必须为对象
                LOG.error("Object {} not defined in {}", typeName, this.name);
                return false;
            }
            
            if(this.object.isPrimitive()) { //如果是个字符串，则必须为对象
                LOG.error("Object {} is primitive, can't use it", typeName);
                return false;
            }
	    } else if(type instanceof Map<?,?>) {
	        this.object = ParameterInfo.parse(version, (Map<String, Object>)type, mc, 0);
	    }
    	
        if(this.object == null) {
            LOG.error("Fail to parase {} in {}", PROPERTY_OBJECT, this.name);
            return false;
        }
        
        this.separator = JsonUtil.getAsStr(para, PROPERTY_SEPARATOR, ",").charAt(0);
    	this.claim = "Array wanted, size between " + min + " and " + max;
    	
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
    protected boolean checkExt(MethodContext context, Object ele) {
		if(!(ele instanceof List<?>)) {
			return false;
		}
		List<Object> list = (List<Object>)ele;
		int size = list.size();
		if(size > this.max || size < this.min) {
			return false;
		}
		
		for(Object o : list) {
			if(!object.check(context, o)) {
				return false;
			}
		}
		
		return true;
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
	public void setToStatement(PreparedStatement statement, int idx, MethodContext context) throws SQLException {
	    Object val = context.getParameter(this.name);

	    StringBuilder s = new StringBuilder();
	    boolean first = true;
	    
	    @SuppressWarnings("unchecked")
        List<Object> oo = (List<Object>)val; 
	    for(Object o : oo) {
	        if(first) {
	            first = false;
	        } else {
	            s.append(this.separator);
	        }
	        
	        if(this.maps == null) {
                s.append(o.toString());
	        } else {
	            s.append(this.maps.get(o.toString()));
	        }
	    }
	    statement.setString(idx, s.toString());
	}

	@Override
	public Object getValue(ResultSet result, int idx) throws SQLException {
        String val = result.getString(idx);
        if(result.wasNull()) {
            return null;
        }
        
	    return splitString(val.toString(), this.separator);
	}
	
    @Override
    public Object getValue(MethodContext context) {
        Object o = context.getParameter(this.name);
        if(o != null && (o instanceof List)) {
            return o;
        }
        return null;
    }
    
    public ParameterInfo getObject() {
        return object;
    }

    /**
     * 用于合并array元素的分隔符
     * @return
     */
    public char getSeparator() {
    	return separator;
    }
    
    /**
     * 根据separator分解字符串，使用string本身的split比较耗内存
     * @param str
     * @param separator
     * @return
     */
    public static List<String> splitString(String str, char separator) {
    	int pos = 0;
    	int forePos = 0;
    	List<String> list = new ArrayList<String>();
    	
    	String s;
    	while((pos = str.indexOf(separator, pos)) >= 0) {
    		s = str.substring(forePos, pos);
    		list.add(s);
    		pos++;
    		forePos = pos;
    	}
    	list.add(str.substring(forePos));
    	
    	return list;
    }
}
