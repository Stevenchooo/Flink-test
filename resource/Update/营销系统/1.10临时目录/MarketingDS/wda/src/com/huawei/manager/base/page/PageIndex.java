package com.huawei.manager.base.page;

import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.DefaultJavaProcessor;
import com.huawei.waf.protocol.RetCode;
import com.huawei.waf.facade.run.IMethodAide;

/**
 * PageIndex
 * 
 * @author w00296102
 */
public class PageIndex extends DefaultJavaProcessor
{
    /**
     * getCheckType
     * 
     * @return int
     */
    public int getCheckType()
    { // 不做任何校验
        return IMethodAide.CHECK_TYPE_NONE;
    }

    /**
     * 运行方法
     * 
     * @param context
     *            方法的配置信息
     * 
     * @return 返回码
     */
    @Override
    public int process(MethodContext context)
    {
        int retCode = super.process(context);
        if (retCode != RetCode.OK)
        {
            return retCode;
        }

        context.setResult(IMethodAide.USERKEY, context.getUserKey());
        context.setResult(IMethodAide.USERACCOUNT, context.getAccount());

        return RetCode.OK;
    }
}
