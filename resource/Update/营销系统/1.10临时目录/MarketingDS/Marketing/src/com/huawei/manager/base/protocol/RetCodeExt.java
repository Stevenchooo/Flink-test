package com.huawei.manager.base.protocol;

import com.huawei.waf.protocol.RetCode;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see  [相关类/方法]
 */
public final class RetCodeExt extends RetCode
{
    /**
     * 用户不存在
     */
    public static final int USER_NOT_EXISTS = 4001;
    
    /**
     * 确认密码错误
     */
    public static final int USER_WRONG_CONFRIMPASSWORD = 4002;
    
    /**
     * 用户密码错误
     */
    public static final int USER_INVALID_PASSWORD = 4003;
    
    /**
     * 用户锁住
     */
    public static final int USER_LOCKED = 4004;
    
    /**
     * 不能移除自己
     */
    public static final int CAN_NOT_REMOVE_YOURSELF = 4100;
    
    /**
     * 错误模型类型
     */
    public static final int WRONG_MODEL_TYPE = 7002;
    
    /**
     * 模型不能为空
     */
    public static final int MODEL_NOT_EMPTY = 7003;
    
    /**
     * 用户没有权限
     */
    public static final int USER_NOTAUTHENTICATED = 7004;
    
    /**
     * 邮箱为空
     */
    public static final int EMAIL_EMPTY = 10001;
    
    /**
     * 邮件发送失败
     */
    public static final int EMAIL_SEND_FAIL = 10002;
    
    /**
     * 邮件passport位空
     */
    public static final int EMAIL_PASSPORT_ZERO = 10003;
    
    /**
     * 用户没有锁住
     */
    public static final int USER_NOT_LOCKED = 20001;
    
    /**
     * <默认构造函数>
     */
    private RetCodeExt()
    {
    }
    
    /**
     * <初始化>
     * @see [类、类#方法、类#成员]
     */
    public static void init()
    {
        addDescription(USER_NOT_EXISTS, "User not exists");
        addDescription(WRONG_MODEL_TYPE, "Wrong model type");
        addDescription(MODEL_NOT_EMPTY, "Model not empty");
        
        addDescription(USER_WRONG_CONFRIMPASSWORD, "Wrong confirm password");
        addDescription(USER_INVALID_PASSWORD, "Invalid password");
        
        addDescription(USER_NOTAUTHENTICATED, "User not authenticated");
        
        addDescription(EMAIL_EMPTY, "Email address is empty");
        addDescription(EMAIL_SEND_FAIL, "Failed to send email");
        addDescription(EMAIL_PASSPORT_ZERO, "Passport is illegal");
        
        addDescription(USER_NOT_LOCKED, "User is not locked");
    }
}
