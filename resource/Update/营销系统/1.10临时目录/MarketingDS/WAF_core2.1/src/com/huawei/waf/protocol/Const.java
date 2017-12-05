package com.huawei.waf.protocol;

import java.nio.charset.Charset;

public final class Const {
    public static final String DEFAULT_ENCODING = "UTF-8";
    
    public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_ENCODING);
    
    public static final String DEFAULT_LANGUAGE = "zh_CN";
    
    public static String RESULT = "resultCode";
    public static String REASON = "info";
    
    public static final String SUCCESS = "Success";
    
    public static final String XML_CONTENT_TYPE = "text/xml;charset=utf-8";
    public static final String HTML_CONTENT_TYPE = "text/html;charset=utf-8";
    public static final String TEXTPLAIN_CONTENT_TYPE = "text/plain;charset=utf-8";
    public static final String JSON_CONTENT_TYPE = "application/json;charset=utf-8";
    public static final String REST_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    public static final String BIN_CONTENT_TYPE = "application/octet-stream";    
    
    /**
     * 请求参数名称，表示是否加密，
     * bit 1：表示请求是否加密
     * bit 2：表示响应是否要求加密
     */
    public static final String ENCRYPT = "_enc";
    
    public static final void setResultCodeName(String resultCodeName) {
    	RESULT = resultCodeName;
    }
    
    public static final void setReasonName(String reasonName) {
    	REASON = reasonName;
    }
}
