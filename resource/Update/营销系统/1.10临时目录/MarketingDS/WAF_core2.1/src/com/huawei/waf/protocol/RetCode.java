package com.huawei.waf.protocol;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.Utils;

public class RetCode {
	private static final int RESERVED_NUM = 1000;
	
	//0正常，其他都是业务正常的错误
	public static final int OK = 0;
	
    //1000以内，内部处理问题
    public static final int INTERNAL_ERROR = 1;
    public static final int WRONG_RESULTCOLUMN = 2;
    public static final int WRONG_RESULTSET_CONFIG = 3;
    public static final int DB_ERROR = 4;
    public static final int INVALID_SESSION = 5;
    public static final int INVALID_USERKEY = 6;
    public static final int TOO_BUSY = 7;
    public static final int SYSTEM_TIMEOUT = 8;
    public static final int NOT_SUPPORTED = 9;
    public static final int INVALID_CLIENTIP = 10;
    public static final int NO_RIGHT = 11;
    
    public static final int RESERVED_CODE = 200;
	
	//2000+ 已知的业务相关错误
    public static final int EXISTS = 2000;
    public static final int NOT_EXISTS = 2001;
    public static final int NEED_NOT_DO = 2002; //不需要做任何处理的情况
    
	//3000+ API问题
	public static final int API_ERROR = 3000;
	public static final int WRONG_JSON_FORMAT = 3003;
	public static final int INVALID_VERSION = 3004;
	
	//4000+ 权限问题
    public static final int TOOSHORT_PASSWORD = 4006;
    public static final int TOOLONG_PASSWORD = 4007;
    public static final int TOOFEW_CHARTYPE_PASSWORD = 4008;
    public static final int TOOSIMPLE_PASSWORD = 4009;
    public static final int PASSWORD_LIKE_ACCOUNT = 4010;
    public static final int WRONG_VERIFYCODE = 4011;	
    public static final int VERIFYCODE_EXPIRED = 4012;    
    public static final int WRONG_SIGNATURE = 4013;    
	
	//6000+ 参数问题，6001表示第一个参数，以此类推
	public static final int WRONG_PARAMETER = 6000;
	
    public static final int INVALID_CODE = Integer.MIN_VALUE;
    
    private static final HashMap<Integer, CodePair> maps = new HashMap<Integer, CodePair>();
	private static final HashMap<Integer, String> descriptions = new HashMap<Integer, String>();
	
	static {
        descriptions.put(OK, "Success");
		descriptions.put(EXISTS, "Already exists");
		descriptions.put(NOT_EXISTS, "Not exists");
		
		descriptions.put(API_ERROR, "Api error");
		descriptions.put(INVALID_SESSION, "Invalid session");
		descriptions.put(DB_ERROR, "Database failed");
		descriptions.put(WRONG_JSON_FORMAT, "Wrong formated json data");
		descriptions.put(INVALID_VERSION, "Invalid version used");
        descriptions.put(NO_RIGHT, "No right");
		
        descriptions.put(RESERVED_CODE, "Use 0 to return OK, 200 is reserved");
		
        descriptions.put(WRONG_VERIFYCODE, "Wrong verify code");
        descriptions.put(VERIFYCODE_EXPIRED, "Verify code expired");
        
        descriptions.put(TOOSHORT_PASSWORD, "Password too short");
        descriptions.put(TOOLONG_PASSWORD, "Password too long");
        descriptions.put(TOOFEW_CHARTYPE_PASSWORD, "Too few different characters in password");
        descriptions.put(TOOSIMPLE_PASSWORD, "Password too simple");
        descriptions.put(PASSWORD_LIKE_ACCOUNT, "Password is very like account");
        
		descriptions.put(TOO_BUSY, "System too busy");
        descriptions.put(NOT_SUPPORTED, "Not supported");
        descriptions.put(SYSTEM_TIMEOUT, "System timeout");
        descriptions.put(INTERNAL_ERROR, "Internal error");
		descriptions.put(WRONG_RESULTCOLUMN, "Invalid result set column");
		descriptions.put(WRONG_RESULTSET_CONFIG, "Wrong result set config");
		descriptions.put(WRONG_SIGNATURE, "Wrong signature");
		
		descriptions.put(WRONG_PARAMETER, "Wrong parameter");
	}
	
	public static final String getDescription(int retCode) {
		String desc = descriptions.get(retCode);
		if(desc == null) {
		    if(retCode < RESERVED_NUM) {
	            desc = descriptions.get(INTERNAL_ERROR);
		    } else {
    			desc = descriptions.get((retCode / RESERVED_NUM) * RESERVED_NUM);
		    }
		}
		return desc == null ? "" : desc;
	}
	
	public static final void addDescription(int code, String desc) {
	    descriptions.put(code, desc);
	}
	
    /**
     * 给WAF错误码映射成定制系统的错误码及错误信息
     * @param code WAF系统的错误码值
     * @param mapCode 定制系统的错误码值
     * @param descr 错误码描述
     */
    public static final void setMapCode(int code, int mapCode, String descr) {
        if(Utils.isStrEmpty(descr)) {
            descr = descriptions.get(code);
        }
        
        maps.put(code, new CodePair(mapCode, descr));
    }
	
    public static final CodePair getMapCode(int code) {
        return maps.get(code);
    }
    
	public static void print(Logger LOG){
        LOG.info("\n===RetCode definition");
	    for(Map.Entry<Integer, String> one : descriptions.entrySet()) {
	        LOG.info("{} = {}", one.getKey(), one.getValue());
	    }
	}
	
	public static class CodePair {
	    public final int retCode;
	    public final String description;
	    
	    public CodePair(int retCode, String description) {
	        this.retCode = retCode;
	        this.description = description;
	    }
	}
}
