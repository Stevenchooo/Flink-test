/*
 * Copyright 2008 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package com.huawei.platform.tcc.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.inspektr.common.ioc.annotation.GreaterThan;
import org.springframework.beans.factory.DisposableBean;

/**
 * Memcache (or Repcache) backed ticket registry.
 * 
 * WebSSO Function
 * Memcached服务客户端，用于支持存储到Memcached的相关鉴权信息
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.1 $ $Date: 2005/08/19 18:27:17 $
 * @since 3.3
 *
 */
public final class MemCacheClientService implements DisposableBean
{
    
    private static final Log LOGGER = LogFactory.getLog(MemCacheClientService.class);
    
    private MemcachedClient client;
    
    @GreaterThan(0)
    private int authInfoTimeout;
    
    private boolean synchronizeUpdatesToRegistry = false;
    
    /**
     * Host names should be given in a list of the format: &lt;hostname&gt;:&lt;port&gt;
     * 
     * @param hostnames  memcache IP
     * @param serviceTicketTimeOut 超时时间
     */
    public MemCacheClientService(final String[] hostnames, final int serviceTicketTimeOut)
    {
        this.authInfoTimeout = serviceTicketTimeOut;
        final List<InetSocketAddress> addresses = new ArrayList<InetSocketAddress>();
        
        for (final String hostname : hostnames)
        {
            String[] hostPort = hostname.split(":");
            addresses.add(new InetSocketAddress(hostPort[0], Integer.parseInt(hostPort[1])));
        }
        
        try
        {
            this.client = new MemcachedClient(addresses);
        }
        catch (final IOException e)
        {
            throw new IllegalStateException(e);
        }
    }
    
    private void handleSynchronousRequest(final Future f)
    {
        try
        {
            if (this.synchronizeUpdatesToRegistry)
            {
                f.get();
            }
        }
        catch (final Exception e)
        {
            // ignore these.
            LOGGER.warn("handleSynchronousRequest failed!", e);
        }
    }
    
    /**
     * <想memcache中插入键值对>
     * @param key  key值
     * @param value  value值
     * @see [类、类#方法、类#成员]
     */
    public void addAuthenticationInfo(String key, String value)
    {
        handleSynchronousRequest(this.client.add(key, this.authInfoTimeout, value));
        
        LOGGER.debug("Added key [" + key + "] and value [" + value + "]");
    }
    
    /**
     * <获取memcachekey值>
     * @param key  key值
     * @return  memcache中的value值
     * @see [类、类#方法、类#成员]
     */
    public String getAuthentication(String key)
    {
        if (StringUtils.isNotBlank(key))
        {
            String result = String.valueOf(this.client.get(key));
            
            LOGGER.debug("Retrived key [" + key + "] returned value [" + result + "]");
            return result;
        }
        
        return null;
    }
    
    /**
     * <删除memcache中的key值>
     * @param key  key值
     * @return 是否删除成功
     * @see [类、类#方法、类#成员]
     */
    public boolean deleteAuthenticationInfo(String key)
    {
        LOGGER.debug("To delete key [" + key + "]");
        
        Future<Boolean> f = this.client.delete(key);
        try
        {
            return f.get().booleanValue();
        }
        catch (final Exception e)
        {
            return false;
        }
    }
    
    /**
     * 断开memcache的链接
     * @throws Exception  断开memcache的链接异常
     */
    public void destroy()
        throws Exception
    {
        this.client.shutdown();
    }
    
    public void setSynchronizeUpdatesToRegistry(final boolean b)
    {
        this.synchronizeUpdatesToRegistry = b;
    }
}
