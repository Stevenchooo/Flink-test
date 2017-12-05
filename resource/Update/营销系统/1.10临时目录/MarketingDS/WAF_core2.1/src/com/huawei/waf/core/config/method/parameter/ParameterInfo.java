package com.huawei.waf.core.config.method.parameter;

import java.sql.*;
import java.util.*;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.sys.*;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

abstract public class ParameterInfo {
	private static final Logger LOG = LogUtil.getInstance();
	
	public static final String TYPE_ARRAY = "ARRAY";
    public static final String TYPE_OBJECT = "OBJECT";
	
	protected static final String PROPERTY_MAX  = "max";        //最大值
	protected static final String PROPERTY_MIN  = "min";        //最小值
	protected static final String PROPERTY_NAME = "name";       //字段名称
	protected static final String PROPERTY_TYPE = "type";       //类型
	protected static final String PROPERTY_OPTIONS = "options"; //取值范围
    protected static final String PROPERTY_MAPS = "maps";       //取值映射
	protected static final String PROPERTY_MUST = "must";       //是否必须
    protected static final String PROPERTY_RESPONSE = "response"; //是否放入响应中
	protected static final String PROPERTY_LOG = "log";         //是否计入日志
	protected static final String PROPERTY_DEFAULT = "default"; //默认值
	protected static final String PROPERTY_SQLINDEX = "index";  //在SQL中的顺序
	protected static final String PROPERTY_RETCODE = "retCode"; //检查失败时的返回码
	 
	protected static final String PROPERTY_DATASEG = "dataSeg"; //在数据库或其他数据源中的名称，默认为name
	
	protected Set<String> options = null;
	
	protected String name = "";
    protected String type = "";
	protected String dataSeg = "";
	
	protected boolean must = false; //是否为必须输入的参数
    protected boolean response = false;
    
    //设为0的情况，则忽略这个参数；大于0表示修改index；默认小于-1，按顺序处理
	protected int index = -1; 
	protected boolean log = true;
	protected int retCodeIfWrong = RetCode.OK;
	protected Map<String, Object> maps = null;
	
    private static final Map<String, Class<? extends ParameterInfo>> paraTypes = new HashMap<String, Class<? extends ParameterInfo>>();
    
	static {
	    addParameterParser("STRING", StringParameterInfo.class); 
	    addParameterParser("STR", StringParameterInfo.class);
	    addParameterParser("BOOL", BoolParameterInfo.class);
        addParameterParser("BOOLEAN", BoolParameterInfo.class);
	    addParameterParser("INT", IntParameterInfo.class);
        addParameterParser("INTEGER", IntParameterInfo.class);
	    addParameterParser("FLOAT", FloatParameterInfo.class);
	    addParameterParser("DOUBLE", DoubleParameterInfo.class);
	    addParameterParser("LONG", LongParameterInfo.class);
	    addParameterParser("DATE", DatetimeParameterInfo.class);
        addParameterParser("DATETIME", DatetimeParameterInfo.class);
	    addParameterParser(TYPE_ARRAY, ArrayParameterInfo.class);
	    addParameterParser("STRINGLIST", StringListParameterInfo.class);
	    addParameterParser("LIST", ArrayParameterInfo.class);
	    addParameterParser("OBJECT", ObjectParameterInfo.class);
	    addParameterParser("OBJ", ObjectParameterInfo.class);
	    addParameterParser("FILE", FileParameterInfo.class);
	    
        addParameterParser("MD5", MD5ParameterInfo.class);
        addParameterParser("NOW", NowParameterInfo.class);
        addParameterParser("UUID", UUIDParameterInfo.class);
        addParameterParser("ACCOUNT", AccountParameterInfo.class);
        addParameterParser("CLIENTIP", ClientIpParameterInfo.class);
        addParameterParser("PWD_EXPIRE_TIME", PwdExpireTimeParameterInfo.class);
        addParameterParser("PWD_VALID_DAYNUM", PwdValidDayNumParameterInfo.class);
        addParameterParser("PWD_DUPLICATE_NUM", PwdDuplicateNumParameterInfo.class);
        addParameterParser("PASSWORD", PasswordParameterInfo.class);
	}
	
    /**
     * @param version
     * @param para
     * @param mc
     * @param index 序列号
     * @return
     */
    public static ParameterInfo parse(String version, Map<String, Object> para, MethodConfig mc, int index) {
    	if(!para.containsKey(PROPERTY_NAME) || !para.containsKey(PROPERTY_TYPE)) {
    		return null;
    	}
    	ParameterInfo p = null;
    	String type = JsonUtil.getAsStr(para, PROPERTY_TYPE).toUpperCase();
    	String name = JsonUtil.getAsStr(para, PROPERTY_NAME);
    	Class<? extends ParameterInfo> cls = paraTypes.get(type);
    	if(cls == null) {
    	    LOG.error("Wrong parameter type '{}' in parameter {}", type, name);
    	    return null;
    	}
    	
    	try {
            p = cls.newInstance();
            p.type = type;
            p.name = name;
            p.dataSeg = JsonUtil.getAsStr(para, PROPERTY_DATASEG, p.name);
            p.must = JsonUtil.getAsBool(para, PROPERTY_MUST, p.must);
            p.response = JsonUtil.getAsBool(para, PROPERTY_RESPONSE, p.response);
            p.log = JsonUtil.getAsBool(para, PROPERTY_LOG, p.log);
            p.retCodeIfWrong = JsonUtil.getAsInt(para, PROPERTY_RETCODE, p.retCodeIfWrong);
            p.index = JsonUtil.getAsInt(para, PROPERTY_SQLINDEX, index);
            
            if(para.containsKey(PROPERTY_OPTIONS)) {
                p.options = new HashSet<String>();
                List<Object> arr = JsonUtil.getAsList(para, PROPERTY_OPTIONS);

                for(Object o : arr) {
                    p.options.add(o.toString());
                }
            }
            
            if(para.containsKey(PROPERTY_MAPS)) {
                p.maps = JsonUtil.getAsObject(para, PROPERTY_MAPS);
            }
            
            if(!p.parseExt(version, para, mc)) {
                LOG.error("Fail to parse extension of parameter {}", p.name);
                return null; 
            }
            return p;
        } catch (Exception e) {
            LOG.error("Fail to parse parameter {}, type={}", name, type, e);
            return null;
        }
    }
    
    /**
     * 参数校验
     * @param ele 参数值
     * @return
     */
    public boolean check(MethodContext context, Object ele) {
    	if(ele == null) {
    		return !this.must;
    	}

		if(this.options != null) {
		    if(!this.options.contains(ele.toString())) {
		        return false;
		    }
		}
    	
		return checkExt(context, ele);
    }
    
    /**
     * 参数名称
     * @return
     */
    public String getName() {
    	return name;
    }
    
    /**
     * 参数类型的名称，比如STRING/INT等
     * @return
     */
    public String getType() {
        return type;
    }
    
    /**
     * 是否将此参数放到响应中
     * @return
     */
    public boolean response() {
        return response;
    }
    
    /**
     * 通常是指在数据库脚本中的顺序
     * -1表示不做为key的一部分，也不是数据脚本的一部分，0表示不是数据库脚本的一部分
     * @return
     */
    public int getIndex() {
    	return index;
    }
    
    /**
     * 设置在数据库脚本中的顺序
     * @return
     */
    public void setIndex(int index) {
        this.index = index;
    }
    
    /**
     * 当参数校验失败后，返回的错误码
     * @param sn
     * @return
     */
    public int getWrongCode(int sn) {
    	if(retCodeIfWrong == RetCode.OK) {
    		return RetCode.WRONG_PARAMETER + sn;
    	}
    	return retCodeIfWrong;
    }
    
    /**
     * 是否可以写入日志
     * @return
     */
    public boolean isCanLog() {
    	return log;
    }
    
    public void setIsCanLog(boolean canLog) {
    	this.log = canLog;
    }
    
    /**
     * 获得参数对应数据库中的字段名
     * @return
     */
    public String getDataSeg() {
    	return dataSeg;
    }
    
    /**
     * 是否为基本类型，处理Object、ARRAY外都是基本类型
     * @return
     */
    public boolean isPrimitive() {
    	return true;
    }
    
    /**
     * 参数是否必须的
     * @return
     */
    public boolean isMust() {
    	return must;
    }
    
    /**
     * 在parseConfig中做基本属性解析，每个参数不同的部分放在parseExt中解析
     * @param version method所在的版本
     * @param para
     * @param mc
     * @return
     */
    protected abstract boolean parseExt(String version, Map<String, Object> para, MethodConfig mc);
    
    /**
     * 基本参数校验在check中，此函数做扩展的校验
     * @param ele 参数值，一定不为空，
     * 因为在check中已判断过，当ele为空时，不会调用checkExt
     * @return
     */
    protected abstract boolean checkExt(MethodContext context, Object ele);
    
    /**
     * 向数据库的statement中设置参数
     * @param statement
     * @param idx 参数的编号，第n个参数，n从1开始，就是statement中第n个"?"
     * @param context method会话
     * @throws SQLException
     */
    public abstract void setToStatement(PreparedStatement statement, int idx, MethodContext context) throws SQLException;
    
    /**
     * 从数据库的结果集中获取信息
     * @param result 结果集
     * @param idx 在结果集中的序号
     * @return
     * @throws SQLException
     */
    public abstract Object getValue(ResultSet result, int idx) throws SQLException;
    
    /**
     * 从参数中获取信息
     * @param parameters
     * @return
     */
    public abstract Object getValue(MethodContext context);
    
    /**
     * 返回参数的描述信息
     * @return
     */
    public abstract String getClaim();
    
    /**
     * 添加自定义参数类型解析器，必须在AbstractInitializer子类的beforeInit中调用，
     * 因为随后将是配置解析，需要用到参数类型
     * @param name
     * @param parser
     */
    public static void addParameterParser(String name, Class<? extends ParameterInfo> parser) {
        paraTypes.put(name.trim().toUpperCase(), parser);
    }
}
