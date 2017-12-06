/*
 * 文 件 名:  UserManagement.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  用户管理
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.management;

import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.constants.OdpConfig;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;
import com.huawei.devicecloud.platform.bi.odp.dao.OdpDao;
import com.huawei.devicecloud.platform.bi.odp.domain.ConfigInfo;
import com.huawei.devicecloud.platform.bi.odp.entity.UserEntity;
import com.huawei.devicecloud.platform.bi.odp.privelege.InterfaceCallPrevilege;
import com.huawei.devicecloud.platform.bi.odp.utils.crypt.CryptUtil;

/**
 * 用户管理
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class UserManagement
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserManagement.class);
    
    private ConfigMgnt configMgnt;
    
    /**
     * Dao层调用对象
     */
    private OdpDao odpDao;
    
    /**
     * 认证消息头是否合法
     * @param appID 应用标识
     * @param timestamp 时间戳(绝对秒表示)
     * @param rawAuthenInfo 待比较sha256加密串
     * @throws CException 不合法异常
     */
    public void authentication(final String rawAuthenInfo, final String appID, final String timestamp)
        throws CException
    {
        boolean authed = false;
        if (!OdpConfig.createOdpConfig().isEnableAuthen())
        {
            LOGGER.info("The Authorization Switch is off.");
            return;
        }
        
        //获取访问码
        final String accessCode = getAccessCode(appID);
        
        //查询token
        String value = null;
        try
        {
            //使用sha-256加密算法机密
            value = CryptUtil.encryptToSHA(String.format("%s%s%s", accessCode, appID, timestamp), "SHA-256");
        }
        catch (NoSuchAlgorithmException e)
        {
            LOGGER.error("encryptToSHA failed!", e);
            throw new CException(ResultCode.AUTH_FAILED, e);
        }
        
        //转换成大写
        if (null != value)
        {
            //转换成大写
            value = value.toUpperCase(Locale.ENGLISH);
            if (value.equals(rawAuthenInfo))
            {
                //消息头鉴权成功
                authed = true;
            }
            else
            {
                //记录鉴权失败信息
                LOGGER.error("authentication failed! appID is {}, timestamp is {}, authInfo is {}", new Object[] {
                    appID, timestamp, rawAuthenInfo});
            }
        }
        
        if (!authed)
        {
            //消息头鉴权失败，记录日期，抛出异常
            LOGGER.info("Do not pass the Authentication!");
            throw new CException(ResultCode.AUTH_FAILED);
        }
    }
    
    /**
     * 获取appId的访问码
     * @param appId 应用标识
     * @return 访问码
     * @throws CException 异常
     */
    public String getAccessCode(final String appId)
        throws CException
    {
        //获取用户信息
        final UserEntity userInfo = odpDao.getUserInfo(appId);
        if (null == userInfo)
        {
            //未授权的应用
            LOGGER.error("access_code of appId[{}] don't exist!", appId);
            throw new CException(ResultCode.AUTH_FAILED);
        }
        else
        {
            //返回访问授权码
            return userInfo.getAccessCode();
        }
    }
    
    /**
     * 检查应用是否有权限访问该接口
     * @param appID 应用Id
     * @param interfaceId 接口号
     * @throws CException 异常
     */
    public void checkPriveleges(final String appID, final int interfaceId)
        throws CException
    {
        //通过appID获取访问码
        final String accessCode = getAccessCode(appID);
        
        //判断token是否有调用接口的权限
        final InterfaceCallPrevilege icp = new InterfaceCallPrevilege();
        if (!icp.canCall(accessCode, interfaceId))
        {
            LOGGER.error("permission denied. appID[{}] has no enough previlege for calling interface[{}]",
                appID,
                interfaceId);
            throw new CException(ResultCode.NO_ENOUGH_PREVILEGE);
        }
    }
    
    /**
     * 获取配置信息
     * @return 配置
     * @throws CException 异常
     */
    public ConfigInfo getConfigInfo()
        throws CException
    {
        //未初始化异常
        if (null == configMgnt)
        {
            throw new CException(ResultCode.SYSTEM_ERROR);
        }
        
        //读取配置
        configMgnt.readConfig();
        
        //返回配置信息
        return configMgnt.getConfigInfo();
    }
    
    public ConfigMgnt getConfigMgnt()
    {
        return configMgnt;
    }
    
    public void setConfigMgnt(final ConfigMgnt configMgnt)
    {
        this.configMgnt = configMgnt;
    }
    
    public OdpDao getOdpDao()
    {
        return odpDao;
    }
    
    public void setOdpDao(final OdpDao odpDao)
    {
        this.odpDao = odpDao;
    }
}
