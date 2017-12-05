package com.huawei.manager.mkt.api;

import javax.servlet.http.HttpSession;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

public class GetHonorSession extends AuthRDBProcessor
{
    /**
     * 查看荣耀中心是否发来请求
     * @param context 上下文
     * @param dbConn 数据库连接
     * @return 结果集
     */
    public int process(MethodContext context, DBConnection dbConn){
        HttpSession session = context.getRequest().getSession();
        Object generalSituat = session.getAttribute("generalSituat");
        if(generalSituat != null){
            context.setResult("flag", true);
        }
        return RetCode.OK;
    }
}
