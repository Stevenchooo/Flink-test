package com.huawei.manager.base.protocol;

import com.huawei.waf.protocol.RetCode;

/**
 * 错误状态码
 * 
 * @author w00296102
 */
public abstract class RetCodeExt extends RetCode
{

    /**
     * 用户不存在
     */
    public static final int USER_NOT_EXISTS = 4001;

    /**
     * 第二次确认密码错误
     */
    public static final int USER_WRONG_CONFRIMPASSWORD = 4002;

    /**
     * 用户无效密码
     */
    public static final int USER_INVALID_PASSWORD = 4003;

    /**
     * 用户被锁
     */
    public static final int USER_LOCKED = 4004;

    /**
     * 用户不能删除自己帐号
     */
    public static final int CAN_NOT_REMOVE_YOURSELF = 4100;

    /**
     * 错误模型类型
     */
    public static final int WRONG_MODEL_TYPE = 7002;

    /**
     * 模型不为空
     */
    public static final int MODEL_NOT_EMPTY = 7003;

    /**
     * 用户为验证
     */
    public static final int USER_NOTAUTHENTICATED = 7004;

    /**
     * 邮件为空
     */
    public static final int EMAIL_EMPTY = 10001;

    /**
     * 邮件发送失败
     */
    public static final int EMAIL_SEND_FAIL = 10002;

    /**
     * 邮件
     */
    public static final int EMAIL_PASSPORT_ZERO = 10003;

    /**
     * 用户未锁
     */
    public static final int USER_NOT_LOCKED = 20001;

    /**
     * 初始化
     */
    public static final void init()
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
