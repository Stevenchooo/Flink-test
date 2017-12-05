/*
 * 文 件 名:  LandInfoDao.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-5
 */
package com.huawei.manager.mkt.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.mkt.entity.AdInfoEntity;
import com.huawei.manager.mkt.info.AdTemplateInfo;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-5]
 * @see  [相关类/方法]
 */
public final class LandInfoDao
{
    
    /**
     * 日志
     */
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * <默认构造函数>
     */
    private LandInfoDao()
    {
    }
    
    /**
     * <判断广告位着陆链接信息是否存在>
     * @param dbConn     数据库链接
     * @param aid      广告位ID
     * @return         广告位着陆链接信息是否存在
     * @see [类、类#方法、类#成员]
     */
    public static boolean isLandInfoExist(DBConnection dbConn, Integer aid)
    {
        String sql = "select aid from t_mkt_land_info where aid = ?";
        Object[] parameters = new Object[] {aid};
        List<Map<String, Object>> resList = DBUtil.query(dbConn, sql, false, parameters);
        
        if (null != resList && !resList.isEmpty())
        {
            LOG.debug("land info exist, aid is {}", aid);
            return true;
        }
        
        LOG.debug("land info don't exist, aid is {}", aid);
        return false;
    }
    
    /**
     * <更新vmall着陆链接信息>
     * @param account     账号
     * @param dbConn        数据库链接
     * @param info        着陆链接信息
     * @return            更新是否成功
     * @see [类、类#方法、类#成员]
     */
    public static boolean updateVmallLandInfo(String account, DBConnection dbConn, AdTemplateInfo info)
    {
        //数据插入
        int res = DBUtil.execute(dbConn,
            "call sp_mktLandInfoModify(?,?,?,?,?,?,?,?,?,?)",
            true,
            new Object[] {account, info.getAid(), info.getSid(), info.getCpsName(), info.getSource(),
                info.getLandChannelName(), info.getLandChannel(), info.getCid(), info.getLandUrl()});
                
        if (res == 0)
        {
            LOG.debug("updateVmallLandInfo success, LandTemplateInfo is {}", info);
            return true;
        }
        
        LOG.debug("updateVmallLandInfo fail, LandTemplateInfo is {}", info);
        return false;
    }
    
    /**
     * <获取广告位信息>
     * @param dbConn     数据库链接
     * @param aid      广告位ID
     * @return         广告位信息
     * @see [类、类#方法、类#成员]
     */
    public static AdInfoEntity getAdInfo(DBConnection dbConn, Integer aid)
    {
        if (null == aid)
        {
            return null;
        }
        
        String sql = "select * from t_mkt_ad_info where aid = ?";
        Object[] parameters = new Object[] {aid};
        List<Map<String, Object>> resList = DBUtil.query(dbConn, sql, false, parameters);
        
        if (null != resList && !resList.isEmpty())
        {
            try
            {
                Integer state = Integer.valueOf((String)resList.get(0).get("state"));
                String platform = (String)resList.get(0).get("platform");
                AdInfoEntity entity = new AdInfoEntity();
                entity.setState(state);
                entity.setPlatform(platform);
                return entity;
            }
            catch (NumberFormatException e)
            {
                LOG.error("getAdInfo error! exception is NumberFormatException");
            }
            
        }
        
        return null;
    }
    
    /**
     * <更新荣耀官网着陆链接>
     * @param account    用户账号
     * @param dbConn       数据库链接
     * @param info       着陆链接信息
     * @return           更新是否成功
     * @see [类、类#方法、类#成员]
     */
    public static boolean updateHonorLandInfo(String account, DBConnection dbConn, AdTemplateInfo info)
    {
        //数据插入
        int res = DBUtil.execute(dbConn,
            "call sp_mktLandInfoModifyHonor(?,?,?,?)",
            true,
            new Object[] {account, info.getAid(), info.getLandUrl()});
        if (res == 0)
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * <用户有没有权限更新着陆链接信息>
     * @param dbConn      数据库链接
     * @param aid       广告位ID
     * @param account   用户账号
     * @param userDeptType 用户类型
     * @param adminFlag    管理员
     * @return          有没有权限
     * @see [类、类#方法、类#成员]
     */
    public static boolean canUpdateLandInfo(DBConnection dbConn, Integer aid, String account, Integer userDeptType,
        boolean adminFlag)
    {
        //查看用户有没有权限修改着陆链接
        if (!adminFlag)
        {
            Integer aidDeptType = AdInfoDao.getAdInfoDeptType(dbConn, aid);
            
            if (!userDeptType.equals(aidDeptType))
            {
                return false;
            }
        }
        
        //是否是广告业务用户
        boolean userflag = AdInfoDao.isAdRoleUser(dbConn, account);
        if (!userflag)
        {
            return true;
        }
        
        //判断没有活动权限，1代表全部
        String sql =
            "select * from t_mkt_user_ad_info where id in (select distinct id from t_mkt_ad_info where aid = ?) and account = ? and flag = 1";
        Object[] parameters = new Object[] {aid, account};
        List<Map<String, Object>> list = DBUtil.query(dbConn, sql, false, parameters);
        
        if (null != list && !list.isEmpty())
        {
            return true;
        }
        
        //判断有没有广告位权限
        sql = "select * from t_mkt_ad_info where aid = ? and operator = ?";
        list = DBUtil.query(dbConn, sql, false, new Object[] {aid, account});
        
        if (null != list && !list.isEmpty())
        {
            return true;
        }
        
        return false;
        
    }
}
