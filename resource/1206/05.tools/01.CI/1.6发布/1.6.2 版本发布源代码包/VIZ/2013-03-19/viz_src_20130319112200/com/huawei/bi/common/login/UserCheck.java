/*
 * 文 件 名:  PasswdCheck.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  temp
 * 创建时间:  2012-6-12
 */
package com.huawei.bi.common.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.util.Config;

import ch.ethz.ssh2.Connection;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  temp
 * @version [华为终端云统一账号模块, 2012-6-12]
 * @see  [相关类/方法]
 */
public class UserCheck
{
    private static final String IP = Config.get("app.login.server.ip");
    
    /** Logger for this class. */
    private static Logger log = LoggerFactory.getLogger(UserCheck.class);
    private static Map<String,String> userMap = new HashMap<String, String>();
    public static boolean check(String user, String pwd)
    {
        try
        {
            boolean loginSuccess = login(IP, user, pwd);
            if(loginSuccess)
            {
                userMap.put(user, pwd);
            }
            return loginSuccess;
        }
        catch (UserIllegalException e)
        {
            log.error("User verification failed.", e);
        }
        return false;
    }
    
    /** 
     * 远程登录到Linux主机 
     * @param ip 登陆服务器ip
     * @return boolean
     * @throws IOException io
     */
    public static boolean login(String ip, String userName, String password)
            throws UserIllegalException
    {
        Connection conn = null;
        try
        {
            conn = new Connection(IP);
            //连接 
            conn.connect();
            //认证
            return conn.authenticateWithPassword(userName, password);
        }
        catch (Exception e)
        {
            throw new UserIllegalException(e);
        }
        finally
        {
            conn.close();
        }
    }
    
    /** 删除登录退出的用户
     * @param user 用户
     * @see [类、类#方法、类#成员]
     */
    public static void removeUser(String user)
    {
        userMap.remove(user);
        log.debug("Remove user success.");
    }

    /** 获取用户对应的密码
     * @param owner 用户
     * @return 用户的登录密码
     * @see [类、类#方法、类#成员]
     */
    public static String getUserPwd(String owner)
    {
        return userMap.get(owner);
    }
}
