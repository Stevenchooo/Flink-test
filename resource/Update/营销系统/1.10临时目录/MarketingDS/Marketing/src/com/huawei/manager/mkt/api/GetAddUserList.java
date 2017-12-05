/*
 * 文 件 名:  GetAddUserList.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-1-14
 */
package com.huawei.manager.mkt.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.common.client.User;
import com.huawei.manager.common.client.meta.AccountInfo;
import com.huawei.manager.common.client.meta.QueryResponse;
import com.huawei.manager.mkt.util.StringUtils;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.EncryptUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

import org.slf4j.Logger;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-1-14]
 * @see  [相关类/方法]
 */
public class GetAddUserList extends AuthRDBProcessor
{
    /**
     * 日志
     */
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 处理后
     * @param context   系统上线文
     * @param conn      数据库链接
     * @return          是否成功
     * @throws SQLException       sql异常
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        //数据查询结果
        List<String> totalUserList = getSystemTotalUserList(context);
        
        List<String> umUserList = getUmUserList();
        
        LOG.debug("totalUserList is {}", totalUserList);
        
        LOG.debug("umUserList is {}", umUserList);
        
        umUserList.removeAll(totalUserList);
        
        Collections.sort(umUserList);
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        
        for (String s : umUserList)
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("userName", s);
            result.add(map);
        }
        
        LOG.debug("add user list is {}", umUserList);
        
        context.setResult("result", result);
        
        return RetCode.OK;
    }
    
    /**
     * <获取现在系统所有用户列表>
     * @param context   查询结果
     * @return          用户列表
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    private List<String> getSystemTotalUserList(MethodContext context)
    {
        List<String> userList = new ArrayList<String>();
        
        //数据查询结果
        Map<String, Object> results = context.getResults();
        
        List<Map<String, String>> list = (List<Map<String, String>>)results.get("result");
        
        if (null == list || list.isEmpty())
        {
            return userList;
        }
        
        for (Map<String, String> map : list)
        {
            addUser(map, userList);
        }
        
        return userList;
    }
    
    /**
     * <添加用户>
     * @param map       查询结果map
     * @param userList  用户列表
     * @see [类、类#方法、类#成员]
     */
    private void addUser(Map<String, String> map, List<String> userList)
    {
        if (null != map && null != map.get("userName"))
        {
            userList.add(map.get("userName"));
        }
        
    }
    
    /**
     * <获取cas服务器的用户列表>
     * @return  服务器用户列表
     * @see [类、类#方法、类#成员]
     */
    private List<String> getUmUserList()
    {
        List<String> userNames = new ArrayList<String>();
        String umServerUrl = StringUtils.getConfigInfo("userMangerServerUrl");
        String umAppId = StringUtils.getConfigInfo("userMangerAppId");
        String umKey = StringUtils.getConfigInfo("userMangerKey");
        User user = new User(umServerUrl, umAppId, EncryptUtil.decode(umKey));
        QueryResponse response = user.query(null, null, null);
        user.logout();
        if (null == response || 0 != response.getResultCode())
        {
            return userNames;
        }
        
        List<AccountInfo> accountLst = response.getResult();
        if (null == accountLst)
        {
            return userNames;
        }
        
        for (AccountInfo each : accountLst)
        {
            userNames.add(each.getAccount());
        }
        return userNames;
    }
    
}
