package com.huawei.waf.facade.run;

import com.huawei.waf.core.run.MethodContext;

/**
 * 用于解决不同系统在认证方面的差异
 * @author l00152046
 *
 */
public interface IMethodAide {
    public static final String USERACCOUNT = "__account";
    public static final String USERKEY  = "__userKey"; 
    public static final String CLIENTIP = "__clientip";
    public static final String LANGUAGE = "__lang";
    
    public static final String AUTH = "passport";
    
    //登录时指定checkType
    public static final int CHECK_TYPE_NULL = -1;
    public static final int CHECK_TYPE_NONE = 0x00;
    public static final int CHECK_TYPE_IP   = 0x01;
    public static final int CHECK_TYPE_KEY  = 0x02;
    public static final int CHECK_TYPE_ONCE = 0x04;
    public static final int CHECK_TYPE_FOREVER = 0x08;
    
    public String getPassportName();
    
	/**
	 * 解析通行证
	 * @param context
	 * @return
	 */
    public int parseAuthInfo(MethodContext context);
    
    /**
     * 记录认证的信息，比如记录到通行证中，也可以记录到session中
     * 根据需要可以提供不同的实现
     * @param context
     * @param account
     * @param userKey
     * @param checkType 指定在检查通行证时需要检查的内容
     */
    public void saveAuthInfo(MethodContext context, String account, String userKey, int checkType);
    
    /**
     * 清除认证信息
     * @param context 会话
     */
    public void removeAuthInfo(MethodContext context);
    
    /**
     * 获取当前请求使用的语言有en/zh_CN等
     * @param context
     * @return
     */
    public String getLanguage(MethodContext context);
}
