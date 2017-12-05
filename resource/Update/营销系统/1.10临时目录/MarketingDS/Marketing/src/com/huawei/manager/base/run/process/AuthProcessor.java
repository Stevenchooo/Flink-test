package com.huawei.manager.base.run.process;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.huawei.util.Utils;
import com.huawei.waf.core.run.process.RDBProcessor;
import com.huawei.waf.facade.AbstractInitializer;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see  [相关类/方法]
 */
public class AuthProcessor extends RDBProcessor
{
    private static final String RIGHT_CACHE_NAME = "rightCache";
    
    /**
     * <获取cache值>
     * @return     cache值
     * @see [类、类#方法、类#成员]
     */
    protected static Cache getCache()
    {
        return AbstractInitializer.getCacheManager().getCache(RIGHT_CACHE_NAME);
    }
    
    /**
     * <获取用户权限>
     * @param name    用户账号
     * @return        用户权限
     * @see [类、类#方法、类#成员]
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
     * <保存用户权限>
     * @param name      用户账号
     * @param val       权限
     * @see [类、类#方法、类#成员]
     */
    protected static void saveRight(String name, Object val)
    {
        Cache cache = getCache();
        
        String k = Utils.md5_base64(name);
        cache.put(new Element(k, val));
    }
    
    /**
     * <移除用户权限>
     * @param name      用户账号
     * @see [类、类#方法、类#成员]
     */
    protected void removeRight(String name)
    {
        Cache cache = getCache();
        
        String k = Utils.md5_base64(name);
        cache.remove(k);
    }
}
