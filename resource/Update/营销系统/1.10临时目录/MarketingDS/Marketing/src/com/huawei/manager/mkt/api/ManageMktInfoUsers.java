/*
 * 文 件 名:  ManageMktInfoUsers.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-3-27
 */
package com.huawei.manager.mkt.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.info.MktUserInfo;
import com.huawei.manager.utils.Constant;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.process.RDBProcessConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-3-27]
 * @see  [相关类/方法]
 */
public class ManageMktInfoUsers extends AuthRDBProcessor
{
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 处理中
     * @param context       系统上下文
     * @param conn          数据库链接
     * @return              处理是否成功
     */
    @SuppressWarnings("unused")
    @Override
    public int process(MethodContext context, DBConnection conn)
    {
        Map<String, Object> reqParameters = context.getParameters();
        
        //校验验证码
        Integer mktId = JsonUtil.getAsInt(reqParameters, "mktinfoId");
        if (null == mktId)
        {
            return RetCode.OK;
        }
        String users = JsonUtil.getAsStr(reqParameters, "users");
        List<MktUserInfo> userList = getMktUserList(mktId, users);
        
        
        
        //获取活动ID删除对应信息
        int retCode = DBUtil.execute(conn, "delete from t_mkt_user_ad_info where id = ? ", false, new Object[] {mktId});
        if (retCode != RetCode.OK)
        {
            return retCode;
        }
        
        //插入对应的用户信息
        RDBProcessConfig pc = (RDBProcessConfig)context.getMethodConfig().getProcessConfig();
        String sql = pc.getSQL();
        
        for (MktUserInfo user : userList)
        {
            retCode =
                DBUtil.execute(conn, sql, false, new Object[] {user.getMktinfoId(), user.getAccount(), user.getFlag()});
            if (retCode != RetCode.OK)
            {
                context.setResult(retCode, "Fail to save " + user.toString());
                return retCode;
            }
        }
        return RetCode.OK;
    }
    
    /**
     * <获取营销活动对应的广告角色用户>
     * @param mktId      活动ID
     * @param users      用户
     * @return           营销活动用户信息列表
     * @see [类、类#方法、类#成员]
     */
    private static List<MktUserInfo> getMktUserList(Integer mktId, String users)
    {
        List<MktUserInfo> userList = new ArrayList<MktUserInfo>();
        
        if (null == users || null == mktId)
        {
            return userList;
        }
        String[] user = users.split(Constant.SEMICOLON);
        for (String s : user)
        {
            //获取单个营销活动用户信息
            MktUserInfo userInfo = getMktUserInfo(s, mktId);
            
            if (null != userInfo)
            {
                userList.add(userInfo);
            }
        }
        return userList;
    }
    
    /**
     * <获取单个营销活动用户信息>
     * @param info     调用后台拼接的字符串
     * @param mktId    活动ID
     * @return         单个营销活动用户信息
     * @see [类、类#方法、类#成员]
     */
    private static MktUserInfo getMktUserInfo(String info, Integer mktId)
    {
        //参数校验
        if (null == info)
        {
            return null;
        }
        
        //分割字符串
        String[] array = info.split(",");
        if (2 != array.length)
        {
            return null;
        }
        
        //获取用户信息
        MktUserInfo userInfo = new MktUserInfo();
        userInfo.setMktinfoId(mktId);
        userInfo.setAccount(array[0]);
        Integer flag = getFlag(array[1]);
        if (null == flag)
        {
            return null;
        }
        userInfo.setFlag(flag);
        return userInfo;
        
    }
    
    /**
     * <获取用户标志>
     * @param flag   字符格式用户标注
     * @return       用户标志int型
     * @see [类、类#方法、类#成员]
     */
    private static Integer getFlag(String flag)
    {
        try
        {
            Integer value = Integer.valueOf(flag);
            
            if (1 == value || 0 == value)
            {
                return value;
            }
        }
        catch (NumberFormatException e)
        {
            LOG.error("getFlag error! exception is NumberFormatException");
        }
        
        return null;
    }
}
