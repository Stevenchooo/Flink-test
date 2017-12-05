package com.huawei.manager.base.run.process;

import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.config.process.AuthPageProcessConfig;
import com.huawei.manager.base.protocol.RetCodeExt;
import com.huawei.manager.utils.Constant;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.DBUtil.DBQueryResult;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.SelfRDBProcessor;
import com.huawei.waf.facade.run.IMethodAide;
import com.huawei.waf.protocol.RetCode;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see  [相关类/方法]
 */
public class AuthPageProcessor extends SelfRDBProcessor
{
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 处理前
     * @param context   上下文
     * @param dbConn      数据库链接
     * @return           是否成功
     */
    @Override
    public int process(MethodContext context, DBConnection dbConn)
    {
        MethodConfig mc = context.getMethodConfig();
        AuthPageProcessConfig apc = (AuthPageProcessConfig)mc.getProcessConfig();
        int pid = JsonUtil.getAsInt(context.getParameters(), apc.getScopePara(), 0);
        
        String name =
            "sp_adminRight?" + context.getAccount() + Constant.CHARCOMMA + pid + Constant.CHARCOMMA + mc.getDataType();
        CacheResultData right = (CacheResultData)AuthProcessor.getRight(name);
        
        context.addResults(context.getParameters());
        context.setResult(IMethodAide.USERKEY, context.getUserKey());
        context.setResult(IMethodAide.USERACCOUNT, context.getAccount());
        if (right == null)
        {
            DBQueryResult res = DBUtil.query(dbConn.getConnection(),
                "{call sp_adminRight(?,?,?,?)}",
                true,
                dbConn.getResultSetRef(),
                new Object[] {context.getAccount(), pid, mc.getDataType()});
                
            if (res.retCode != RetCode.OK)
            {
                LOG.warn("{} has no right to operate data {}.{}", context.getAccount(), mc.getDataType(), pid);
                AuthProcessor.saveRight(name, new CacheResultData(res.retCode, context.getResults()));
                return res.retCode;
            }
            
            Map<String, Object> info = null;
            if ((info = res.getLine(0, 0)) == null)
            {
                AuthProcessor.saveRight(name,
                    new CacheResultData(RetCodeExt.USER_NOTAUTHENTICATED, context.getResults()));
                return RetCodeExt.USER_NOTAUTHENTICATED;
            }
            
            context.addResults(info);
            AuthProcessor.saveRight(name, new CacheResultData(RetCode.OK, context.getResults()));
            if (LOG.isDebugEnabled())
            {
                LOG.debug("CheckRight account:{}, pid:{}, meta:{}, right:{}",
                    context.getAccount(),
                    pid,
                    mc.getDataType(),
                    info);
            }
        }
        else
        {
            if (right.retCode != RetCode.OK)
            {
                return right.retCode;
            }
            context.addResults(right.results);
        }
        
        return super.process(context, dbConn);
    }
    
    /**
     * 处理完成
     * @param context  上下文
     * @return         接口返回值
     */
    @Override
    public int afterAll(MethodContext context)
    {
        return RetCode.OK; //不用写日志
    }
}
