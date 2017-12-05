/*
 * 文 件 名:  EmailMktAdUserList.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-8-19
 */
package com.huawei.manager.mkt.api;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.constant.PageType;
import com.huawei.manager.mkt.email.EmailService;
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
public class EmailMktUserList extends AuthRDBProcessor
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
        LOG.debug("enter EmailMktUserList");
        
        //获取活动信息
        Map<String, Object> results = context.getResults();
        
        List<Map<String, String>> list = (List<Map<String, String>>)results.get("result");
        
        //获取营销活动名称
        String mktName = list.get(0).get("mktinfoName");
        
        //获取用户名
        String account = context.getAccount();
        
        //获取主送人列表
        String users = (String)context.getParameter("users");
        
        //营销活动ID
        Integer mktinfoId = Integer.valueOf((String)context.getParameter("mktinfoId"));
        
        //页面勾选的用户列表
        List<String> userList = StringUtils.getUserList(users);
        
        //获取主送邮件列表
        List<String> mainSendEmailList = MktUtils.getMainSendEmailList(conn, userList);
        
        //获取抄送邮件列表
        List<String> copySendEmailList = MktUtils.getCopySendEmailList(conn, account, mktinfoId, PageType.MKT_PAGE,mainSendEmailList);
        
        mktSendEmail(mktName, account, mainSendEmailList, copySendEmailList);
        
        MktUtils.modifyUserPermission(conn, account, mktinfoId, userList);
        
        return RetCode.OK;
    }
    
    /**
     * <营销活动发送邮件>
     * @param mktName               营销活动名称
     * @param account               用户账号
     * @param mainSendEmailList     主送列表
     * @param copySendEmailList     抄送列表
     * @see [类、类#方法、类#成员]
     */
    private void mktSendEmail(String mktName, String account, List<String> mainSendEmailList,
        List<String> copySendEmailList)
    {
        String mainsendemails = StringUtils.getEmails(mainSendEmailList);
        String copysendemails = StringUtils.getEmails(copySendEmailList);
        String content =
            "营销广告分析系统网址: <a href=\"" + StringUtils.getConfigInfo("mktSystemUrl") + "\">"
                + StringUtils.getConfigInfo("mktSystemUrl") + "</a>";
        
        String title = "你好，营销活动\"" + mktName + "\"已经创建，请登录营销广告系统并录入广告位信息，谢谢，通知人" + account;
        EmailService.sendMktEmail(mainsendemails, copysendemails, title, content, null);
    }
    
}
