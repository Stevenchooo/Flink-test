package com.huawei.manager.base.api;

import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.DefaultJavaProcessor;
import com.huawei.waf.protocol.RetCode;

/**
 * 
 * <一句话功能简述> <功能详细描述>
 * 
 * @author w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see [相关类/方法]
 */
public class UserLogout extends DefaultJavaProcessor
{
    /**
     * 系统处理后
     * 
     * @param context
     *            系统上下文
     * @return 是否成功
     */
    @Override
    public int process(MethodContext context)
    {
        context.getMethodConfig().getAide().removeAuthInfo(context);
        return RetCode.OK;
    }
}
