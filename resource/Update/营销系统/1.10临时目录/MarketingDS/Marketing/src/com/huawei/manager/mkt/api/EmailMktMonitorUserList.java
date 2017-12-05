/*
 * 文 件 名:  EmailMktMonitorUserList.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2015,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  p84035806
 * 创建时间:  2015-8-24
 */
package com.huawei.manager.mkt.api;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.constant.AdState;
import com.huawei.manager.mkt.constant.PageType;
import com.huawei.manager.mkt.email.EmailService;
import com.huawei.manager.mkt.info.AdEmailNumInfo;
import com.huawei.manager.mkt.util.MktUtils;
import com.huawei.manager.mkt.util.StringUtils;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-8-19]
 * @see  [相关类/方法]
 */
public class EmailMktMonitorUserList extends AuthRDBProcessor
{
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 处理中
     * @param context   系统上下文
     * @param conn      数据库连接
     * @return          是否成功
     */
    @SuppressWarnings("unchecked")
    @Override
    public int afterProcess(MethodContext context, DBConnection conn)
    {
        LOG.debug("enter EmailMktAdUserList");
        
        //获取活动信息
        Map<String, Object> results = context.getResults();
        
        List<Map<String, String>> list = (List<Map<String, String>>)results.get("result");
        
        //获取营销活动名称
        String mktName = list.get(0).get("mktinfoName");
        
        //获取用户名
        String account = context.getAccount();
        
        //获取主送人列表
        String users = (String)context.getParameter("users");
        
        //获取营销活动ID
        Integer mktinfoId = Integer.valueOf((String)context.getParameter("mktinfoId"));
        
        List<String> userList = StringUtils.getUserList(users);
        
        //获取主送邮件列表
        List<String> mainSendEmailList = MktUtils.getMainSendEmailList(conn, userList);
        
        //获取抄送邮件列表
        List<String> copySendEmailList = MktUtils.getCopySendEmailList(conn, account, mktinfoId, PageType.LAND_PAGE,mainSendEmailList);
        
        AdEmailNumInfo numInfo = MktUtils.getAdEmailNum(conn, account, mktinfoId, AdState.FOR_MONITOR);
        StringBuffer contentBuffer = getAdEmailContentBuffer(numInfo);
        
        String mainsendemails = StringUtils.getEmails(mainSendEmailList);
        String copysendemails = StringUtils.getEmails(copySendEmailList);
        
        String title = "你好，营销活动\"" + mktName + "\"的着陆链接信息已经录入，请登录营销广告系统并生成监控代码，谢谢，通知人" + account;
        //发送邮件
        EmailService.sendMktEmail(mainsendemails, copysendemails, title, contentBuffer.toString(), null);
        
        //修改权限
        MktUtils.modifyUserPermission(conn, account, mktinfoId, userList);
        
        return RetCode.OK;
    }
    
    /**
     * <获取邮件发送内容>
     * @param numInfo   邮件发送中广告位数目
     * @return          邮件发送内容
     * @see [类、类#方法、类#成员]
     */
    private StringBuffer getAdEmailContentBuffer(AdEmailNumInfo numInfo)
    {
        StringBuffer contentBuffer = new StringBuffer();
        contentBuffer.append("有" + numInfo.getTotalNum() + "个广告位尚未生成监控代码 ,");
        
        if (numInfo.getVmallNum() > 0)
        {
            contentBuffer.append("落地到VMALL广告位" + numInfo.getVmallNum() + "个,");
        }
        
        if (numInfo.getHonorNum() > 0)
        {
            contentBuffer.append("落地到荣耀官网广告位" + numInfo.getHonorNum() + "个,");
        }
        
        if (numInfo.getHuaweiNum() > 0)
        {
            contentBuffer.append("落地到华为官网广告位" + numInfo.getHuaweiNum() + "个,");
        }
        
        Integer otherNum = numInfo.getTotalNum() - numInfo.getVmallNum() - numInfo.getHonorNum() - numInfo.getHuaweiNum();        
        if (otherNum > 0)
        {
            contentBuffer.append("落地到第三方广告位" + otherNum + "个,");
        }
        contentBuffer.append("请尽快生成。营销广告分析系统网址: <a href=\"" + StringUtils.getConfigInfo("mktSystemUrl") + "\">"
            + StringUtils.getConfigInfo("mktSystemUrl") + "</a>");
        
        return contentBuffer;
    }
    
}
