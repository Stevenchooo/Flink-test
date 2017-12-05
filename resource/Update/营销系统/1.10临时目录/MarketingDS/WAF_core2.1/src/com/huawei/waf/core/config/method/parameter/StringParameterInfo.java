package com.huawei.waf.core.config.method.parameter;

import java.nio.charset.Charset;
import java.sql.*;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.huawei.util.EncryptUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.Const;

public class StringParameterInfo extends ParameterInfo {
    private static final Logger LOG = LogUtil.getInstance();
    
	private static final String PROPERTY_REGULAR   = "regular";
	private static final String PROPERTY_NOSQL     = "noSql";
	private static final String PROPERTY_NOHTML    = "noHtml";
	private static final String PROPERTY_NOLINE    = "noLine";
	private static final String PROPERTY_NOQUOT    = "noQuot";
    private static final String PROPERTY_CODEMODE  = "codeMode";
	private static final String PROPERTY_STRICT    = "strict";
    private static final String PROPERTY_LEN       = "len";
    private static final String PROPERTY_TRIM      = "trim";
    private static final String PROPERTY_TAIL      = "tail";
    private static final String PROPERTY_TRANSCODE = "transcode";

	protected int minLen = 0;
	protected int maxLen = 255;
	protected String defaultVal = null;
	protected Pattern regular = null;
	protected String tail = null;

	protected boolean noSql = false;
	protected boolean noHtml = false;
	protected boolean noLine = false;
	protected boolean noQuot = false;
	protected boolean strict = false;
	protected boolean trim = false;  //是否去除首尾空格
	
	private static enum CodeMode{NONE, ENCODE, DECODE};
	
	private CodeMode codeMode = CodeMode.NONE;
	
	private TransCode transCode = null;
	
	private String claim = "";
	
    @Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
    	if(para.containsKey(PROPERTY_REGULAR)) {
    		this.regular = Pattern.compile(JsonUtil.getAsStr(para, PROPERTY_REGULAR));
    	}
    	
        this.claim = "String,length";
    	int len = JsonUtil.getAsInt(para, PROPERTY_LEN, -1);
    	if(len > 0) {
            this.maxLen = len;
            this.minLen = len;
            this.claim += "=" + len;
    	} else {
    		this.maxLen = JsonUtil.getAsInt(para, PROPERTY_MAX, this.maxLen);
    		this.minLen = JsonUtil.getAsInt(para, PROPERTY_MIN, this.minLen);
            this.claim += "(" + this.minLen + "," + this.maxLen + ")";
    	}
    	
    	if(para.containsKey(PROPERTY_DEFAULT)) {
    		this.defaultVal = JsonUtil.getAsStr(para, PROPERTY_DEFAULT);
    	}
    	
    	if(para.containsKey(PROPERTY_STRICT)) {
    		this.strict = JsonUtil.getAsBool(para, PROPERTY_STRICT);
    		if(this.strict) {
    			this.noSql = true;
    			this.noLine = true;
    			this.noQuot = true;
    			this.noHtml = true;
    		}
    	}
    	
    	String codeMode = JsonUtil.getAsStr(para, PROPERTY_CODEMODE, "NONE").toUpperCase();
    	try {
    	    this.codeMode = CodeMode.valueOf(codeMode);
    	} catch(Exception e) {
    	    LOG.error("Invalid codeMode setted", e);
			return false;
    	}
    	
    	/**
    	 * 如果设置了加解密，则日志默认为不打印
    	 */
    	if(this.codeMode != CodeMode.NONE && !para.containsKey(PROPERTY_LOG)) {
    	    this.log = false;
    	}
    	
		this.noSql = JsonUtil.getAsBool(para, PROPERTY_NOSQL, this.noSql);
		this.noLine = JsonUtil.getAsBool(para, PROPERTY_NOLINE, this.noLine);
		this.noHtml = JsonUtil.getAsBool(para, PROPERTY_NOHTML, this.noHtml);
		this.noQuot = JsonUtil.getAsBool(para, PROPERTY_NOQUOT, this.noQuot);
		
		this.trim = JsonUtil.getAsBool(para, PROPERTY_TRIM, this.trim);
    	
        if(this.must) {
            this.claim += ",must";
        }
        if(this.regular != null) {
            this.claim += ',' + this.regular.toString();
        }
        if(this.noSql) {
            this.claim += ",no %?";
        }
        if(this.noHtml) {
            this.claim += ",no <>";
        }
        if(this.noQuot) {
            this.claim += ",no \'\\\"";
        }
        if(this.noLine) {
            this.claim += ",no line-feed and return";
        }
        
        Map<String, Object> cfgObj = JsonUtil.getAsObject(para, PROPERTY_TRANSCODE);
        if(cfgObj == null || cfgObj.size() <= 0) {
            return true;
        }
        
        this.transCode = new TransCode();
        this.tail = JsonUtil.getAsStr(para, PROPERTY_TAIL, this.tail);//加在字符串尾部
        
        return this.transCode.parse(cfgObj);
    }

    @Override
    protected boolean checkExt(MethodContext context, Object ele) {
    	String val;
        if(ele instanceof String) {
            val = (String)ele;
        } else {
            val = ele.toString();
        }
        
    	int len = val.length();
        if(len < this.minLen || len > this.maxLen) {
    		return false;
    	}
    	
    	if(this.noSql) {
    		if(val.indexOf('?') >= 0 || val.indexOf('%') >= 0) {
    			return false;
    		}
    	}
    	
    	if(this.noHtml) {
    		if(val.indexOf('<') >= 0 || val.indexOf('>') >= 0) {
    			return false;
    		}
    	}
    	
    	if(this.noLine) {
    		if(val.indexOf('\n') >= 0 || val.indexOf('\r') >= 0) {
    			return false;
    		}
    	}
    	
    	if(this.noQuot) {
    		if(val.indexOf('\"') >= 0 || val.indexOf('\'') >= 0) {
    			return false;
    		}
    	}
    	
    	if(this.regular != null && !this.regular.matcher(val).matches()) {
    		return false;
    	}

    	return true;
    }
    
    @Override
    public void setToStatement(PreparedStatement statement, int idx, MethodContext context) throws SQLException {
        Object val = this.getValue(context);
    	if(val != null) {
    		statement.setString(idx, code(val.toString(), this.codeMode));
		} else {
    		statement.setNull(idx, java.sql.Types.VARCHAR);
		}
    }
    
    private static String code(String val, CodeMode mode) {
        if(!Utils.isStrEmpty(val)) {
            if(mode == CodeMode.ENCODE) {
                return EncryptUtil.encode(val);
            }
            
            if(mode == CodeMode.DECODE) {
                return EncryptUtil.decode(val);
            }
        }
        
        return val;
    }
    
//    public static void main(String[] args) {
//        String s = "test1111";
//        String e = code(s, CodeMode.ENCODE);
//        String d = code(e, CodeMode.DECODE);
//        System.out.println("src:" + s + ",enc:" + e + ",dec:" + d);
//        s = "";
//        e = code(s, CodeMode.ENCODE);
//        d = code(e, CodeMode.DECODE);
//        System.out.println("src:" + s + ",enc:" + e + ",dec:" + d);
//        s = "jkljlkjlkjkljlk---jlkllllllllll";
//        e = code(s, CodeMode.ENCODE);
//        d = code(e, CodeMode.DECODE);
//        System.out.println("src:" + s + ",enc:" + e + ",dec:" + d);
//        
//        System.exit(0);
//    }
//    
    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
    	String val = result.getString(idx);
    	
    	if(!result.wasNull()) {
            if(this.maps != null) {
                val = this.maps.get(val).toString();
            } else if(this.trim) {
                val = val.trim();
            }
            
            val = code(val, this.codeMode);
            if(this.tail != null) {
                val += this.tail;
            }
            
            return val;
    	} else if(this.defaultVal != null) {
    		return this.defaultVal;
    	} else {
    		return "";
    	}
    }
    
    public static String serialize(String str) {
    	return str.replace("\r", "").replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
    
    @Override
    public String getClaim() {
    	return claim;
    }

    @Override
    public Object getValue(MethodContext context) {
        String val = Utils.parseString(context.getParameters().get(this.name), null);
        
        if(val != null) {
            if(this.maps != null) {
                val = this.maps.get(val).toString();
            } else if(this.trim) { //如果设置了映射，最好自己做trim
                val = val.trim();
            }
            
            if(this.tail != null) {
                val += this.tail;
            }
            
            if(this.transCode != null) {
                try {
                    return new String(val.getBytes(this.transCode.from), this.transCode.to);
                } catch (Exception e) {
                    LOG.error("Fail to transcode from {} to {}, use orignal data", this.transCode.from, this.transCode.to);
                }
            }
            
            return val;
        } else if(this.defaultVal != null) {
            return this.defaultVal;
        } else {
            return null;
        }
    }
    
    protected class TransCode {
        Charset from;
        Charset to = Const.DEFAULT_CHARSET;
        
        public boolean parse(Map<String, Object> cfg) {
            String fromStr = JsonUtil.getAsStr(cfg, "from", "ISO-8859-1");
            String toStr = JsonUtil.getAsStr(cfg, "to", "UTF-8");
            
            try {
                from = Charset.forName(fromStr);
                to = Charset.forName(toStr);
                return true;
            } catch(Exception e) {
                LOG.error("Fail to parse TransCode", e);
            }
            return false;
        }
    }
}
