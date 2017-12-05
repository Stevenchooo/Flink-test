package com.huawei.manager.base.page;

import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.DefaultJavaProcessor;
import com.huawei.waf.protocol.RetCode;
import com.huawei.waf.facade.run.IMethodAide;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see  [相关类/方法]
 */
public class PageIndex extends DefaultJavaProcessor
{
    /**
     * 获取检查类型
     * @return      检查类型
     */
    public int getCheckType()
    { //不做任何校验
        return IMethodAide.CHECK_TYPE_NONE;
    }
    
    /**
     * 系统处理
     * @param context     系统上下文
     * @return            是否成功
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
