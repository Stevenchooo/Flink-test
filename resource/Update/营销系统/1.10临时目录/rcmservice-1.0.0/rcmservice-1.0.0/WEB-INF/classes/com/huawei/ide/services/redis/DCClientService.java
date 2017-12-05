package com.huawei.ide.services.redis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.huawei.dc.sdk.DCClient;
import com.huawei.ide.interceptors.res.rcm.CommonUtils;
import com.huawei.ide.interceptors.res.rcm.RcmServiceConstants;

import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * DCClientService
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see  [相关类/方法]
 */
@Service(value = "com.huawei.ide.services.redis.DCClientService")
public class DCClientService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DCClientService.class);
    
    private DCClient dcClient;
    
    /**
     * 初始化
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    @PostConstruct
    public void init()
    {
        JedisPoolConfig jConfig = new JedisPoolConfig();
        jConfig.setMaxTotal(-1);
        dcClient = new DCClient();
        String service = CommonUtils.getSysConfigValueByKey(RcmServiceConstants.CONF_REDIS_SERVER);
        String etcdUrl = CommonUtils.getSysConfigValueByKey(RcmServiceConstants.CONF_REDIS_ETCDURL);
        if (!dcClient.init(service, etcdUrl, jConfig))
        {
            LOGGER.error("DCClientService init() failed, with the service name '" + service + "' and the etcdUrl '"
                + etcdUrl + "'.");
            /*
            throw new CException(RCMResultCodeConstants.REDIS_CONNECT_ERROR,
                "DCClientService init() failed, with the service name '" + service + "' and the etcdUrl '" + etcdUrl
                    + "'.");
             */
        }
    }
    
    /**
     * getRedisValue
     * <功能详细描述>
     * @param key
     *        key
     * @return  String
     * @see [类、类#方法、类#成员]
     */
    public String getRedisValue(String key)
    {
        if (null != dcClient)
        {
            return dcClient.get(key);
        }
        return null;
    }
    
    /**
     * destory
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    @PreDestroy
    public void destory()
    {
        if (null != dcClient)
        {
            dcClient.close();
        }
    }
    
}
