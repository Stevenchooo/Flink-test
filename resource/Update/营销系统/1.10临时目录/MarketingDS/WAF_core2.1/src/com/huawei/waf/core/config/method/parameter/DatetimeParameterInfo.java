package com.huawei.waf.core.config.method.parameter;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;

public class DatetimeParameterInfo extends ParameterInfo {
	private static Logger LOG = LogUtil.getInstance();
	
	private static final String DEFAULT_INNER_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ssZ";
	private static final String PROPERTY_INNER_FORMAT = "innerFormat";
	private static final String PROPERTY_FORMAT = "format";
    private static final String FORMAT_TIMESTAMP = "timestamp";
	
	protected String format = null;
	protected String dbFormat = null;
	protected long min;
	protected long max;
	protected String defaultVal = null;
	private String claim = "";
	private boolean isTimeStamp = false;
	
	public DatetimeParameterInfo() {
		this.min = parseFromString("2000-01-01 00:00:00", DEFAULT_INNER_FORMAT).getTime();
		this.max = parseFromString("2200-01-01 00:00:00", DEFAULT_INNER_FORMAT).getTime();
	}
	
    @Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
        this.format = DEFAULT_FORMAT;
    	if(para.containsKey(PROPERTY_FORMAT)) {
    		try {
    		    String fmt = JsonUtil.getAsStr(para, PROPERTY_FORMAT);
    		    if(fmt.toLowerCase().equals(FORMAT_TIMESTAMP)) {
    		        isTimeStamp = true;
    		    } else {
    		        this.format = fmt;
    		    }
			} catch(IllegalArgumentException iae) {
				LOG.error("Wrong format in parameter:{}", this.name);
				return false;
			}
    	}

    	if(para.containsKey(PROPERTY_INNER_FORMAT)) {
    		try {
    			this.dbFormat = JsonUtil.getAsStr(para, PROPERTY_INNER_FORMAT);
    		} catch(IllegalArgumentException iae) {
    			LOG.error("Wrong dbFormat in parameter:{}", this.name);
    			return false;
    		}
    	} else {
    		this.dbFormat = DEFAULT_INNER_FORMAT;
    	}

    	Date dt;
    	String str;
    	if(para.containsKey(PROPERTY_DEFAULT)) {
    		str = JsonUtil.getAsStr(para, PROPERTY_DEFAULT);
    		dt = parseFromString(str, this.format);
    		if(dt == null) {
    			LOG.error("Wrong default date format:{}", str);
    			return false;
    		}
    	    this.defaultVal = str;
    	}
    	
    	if(para.containsKey(PROPERTY_MIN)) {
    		str = JsonUtil.getAsStr(para, PROPERTY_MIN);
    		dt = parseFromString(str, this.format);
    		if(dt == null) {
    			LOG.error("Wrong min date format:{}", str);
    			return false;
    		}
    		this.min = dt.getTime();
    	}

    	if(para.containsKey(PROPERTY_MAX)) {
    		str = JsonUtil.getAsStr(para, PROPERTY_MAX);
    		dt = parseFromString(str, this.format);
    		if(dt == null) {
    			LOG.error("Wrong max date format:{}", str);
    			return false;
    		}
    		this.max = dt.getTime();
    	}
    	
        Date begin = new Date(this.min);
        Date end = new Date(this.max);
        
        this.claim = "Format " + format + ", between '" +  toString(begin, format)
                     + " and '" + toString(end, format)
                     + "\',default is " + (this.defaultVal != null ? this.defaultVal : "null");
    	return true;
    }
    
    @Override
    protected boolean checkExt(MethodContext context, Object ele) {
        java.util.Date v;
        if(ele instanceof java.util.Date) {
            v = (java.util.Date)ele;
        } else if(isTimeStamp) {
            v = new java.util.Date(Utils.parseLong(ele, 0L));
        } else {
            v = parseFromString(ele.toString(), this.format);
        }
    	
    	if(v == null) {
    		return false;
    	}
    	long vv = v.getTime();
    	
    	return (vv >= this.min && vv <= this.max);
    }
    
    @Override
    public void setToStatement(PreparedStatement statement, int idx, MethodContext context) throws SQLException {
        Object val = this.getValue(context);
    	if(val != null) {
    		Date dt = parseFromString(Utils.parseString(val, ""), this.format);
    		statement.setString(idx, toString(dt, this.dbFormat));
		} else {
    		statement.setNull(idx, java.sql.Types.DATE);
		}
    }
    
    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
        Timestamp dt = null;
        
        //当返回为'0000-00-00 00:00:00'时，无法取出
        try {
    	    dt = result.getTimestamp(idx);
        } catch(SQLException e) {
            //LOG.error("Fail to get get from result set", e);
        }
    	
    	if(dt != null) {
    	    if(isTimeStamp) {
    	        return dt.getTime();
    	    }
    	    
    		return toString(dt, this.format);
    	} else if(this.defaultVal != null) {
    		return this.defaultVal;
    	} else {
    		return "0000-00-00 00:00:00";
    	}
    }
    
    public static final Date parseFromString(String val, String format) {
    	try {
    		SimpleDateFormat sdf = new SimpleDateFormat(format);
    		java.util.Date dt = sdf.parse(val);
    		return new Date(dt.getTime());
    	} catch(Exception e) {
    		LOG.error("Wrong date format", e);
    	}
		return null;
    }

    @Override
    public Object getValue(MethodContext context) {
        Object v = context.getParameter(this.name);
        
        if(v != null) {
            if(isTimeStamp) {
                return Utils.parseLong(v, 0L);
            }
            return parseFromString(Utils.parseString(v, "0000-00-00 00:00:00"), this.format);
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
    
    public static final String toString(java.util.Date date, String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(date);
    }

    public static final Date fromString(String val, SimpleDateFormat fmt) {
        try {
            java.util.Date dt = fmt.parse(val);
            return new Date(dt.getTime());
        } catch(Exception e) {
            LOG.error("Wrong date format", e);
        }
        return null;
    }
    
    public static final java.util.Date addDay(int day) {
        java.util.Date dt = new Date(new java.util.Date().getTime() + (long)day * 86400 * 1000);
        return new java.util.Date(dt.getTime());
    }
}
