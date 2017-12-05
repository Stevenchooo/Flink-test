package com.huawei.manager.base.run.process;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.huawei.util.Utils;
import com.huawei.waf.core.run.process.RDBProcessor;
import com.huawei.waf.facade.AbstractInitializer;

/**
 * 处理类
 * 
 * @author w00296102
 */
public abstract class AuthProcessor extends RDBProcessor
{
    private static final String RIGHT_CACHE_NAME = "rightCache";

    /**
     * 获取缓存
     * 
     * @return 返回
     */
    protected static Cache getCache()
    {
        return AbstractInitializer.getCacheManager().getCache(RIGHT_CACHE_NAME);
    }

    /**
     * 获取权限
     * 
     * @param name
     *            权限名
     * @return 返回码
     */
    protected static Object getRight(String name)
    {
        Cache cache = getCache();

        String k = Utils.md5_base64(name);
        Element cv = cache.get(k);
        if (cv != null)
        {
            return cv.getObjectValue();
        }

        return null;
    }

    /**
     * 保存权限
     * 
     * @param name
     *            权限名
     * @param val
     *            权限值
     */
    protected static void saveRight(String name, Object val)
    {
        Cache cache = getCache();

        String k = Utils.md5_base64(name);
        cache.put(new Element(k, val));
    }

    /**
     * 删除权限名
     * 
     * @param name
     *            权限名
     */
    protected static void removeRight(String name)
    {
        Cache cache = getCache();

        String k = Utils.md5_base64(name);
        cache.remove(k);
    }
}
