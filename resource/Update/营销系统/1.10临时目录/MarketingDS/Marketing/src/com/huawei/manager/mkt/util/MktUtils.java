/*
 * 文 件 名:  ExportUtils.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-10-13
 */
package com.huawei.manager.mkt.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huawei.manager.mkt.dao.AdInfoDao;
import com.huawei.manager.mkt.info.AdDicMapInfo;
import com.huawei.manager.mkt.info.AdEmailNumInfo;
import com.huawei.manager.mkt.info.AdExportInfo;
import com.huawei.util.DBUtil;
import com.huawei.util.EncryptUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-10-13]
 * @see  [相关类/方法]
 */
public final class MktUtils
{
    
    private MktUtils()
    {
    }
    
    /**
     * <获取已经完成导出信息列表>
     * @param context  系统上下文
     * @return         导出广告位信息列表
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public static List<AdExportInfo> getAdExportInfo(MethodContext context)
    {
        //数据查询结果
        Map<String, Object> results = context.getResults();
        
        List<AdExportInfo> exportList = new ArrayList<AdExportInfo>();
        
        //存储过程返回的列表
        List<Map<String, String>> list = (List<Map<String, String>>)results.get("result");
        
        //列表为空，直接返回
        if (null == list || list.isEmpty())
        {
            return null;
        }
        
        //遍历查询结果集，添加入列表
        for (Map<String, String> objMap : list)
        {
            addAdExportList(exportList, objMap);
        }
        
        //列表返回
        return exportList;
    }
    
    /**
     * <将查询的单个记录插入返回列表>
     * @param exportList   返回数据列表
     * @param objMap       单个查询信息
     * @see [类、类#方法、类#成员]
     */
    private static void addAdExportList(List<AdExportInfo> exportList, Map<String, String> objMap)
    {
        //参数校验
        if (null == exportList || null == objMap || objMap.isEmpty())
        {
            return;
        }
        
        //获取对应的广告位导出信息
        AdExportInfo info = getAdExportInfo(objMap);
        
        //插入对应的列表
        if (null != info)
        {
            exportList.add(info);
        }
    }
    
    /**
     * <将数据库查询的map转成AdExportInfo>
     * @param objMap   数据查询map结果体
     * @return         查询导出信息
     * @see [类、类#方法、类#成员]
     */
    private static AdExportInfo getAdExportInfo(Map<String, String> objMap)
    {
        if (null != objMap && !objMap.isEmpty())
        {
            AdExportInfo info = new AdExportInfo();
            
            info.setAdInfoId(String.valueOf(objMap.get("adInfoId")));
            info.setMktName(objMap.get("mktinfoName"));
            info.setAdInfoWebName(objMap.get("adInfoWebName"));
            info.setAdInfoChannel(objMap.get("adInfoChannel"));
            info.setAdInfoPosition(objMap.get("adInfoPosition"));
            info.setMaterialType(objMap.get("materialType"));
            info.setMaterialDesc(objMap.get("materialDesc"));
            info.setMaterialState(objMap.get("materialState"));
            info.setAdInfoPort(objMap.get("adInfoPort"));
            info.setAdInfoPlatform(objMap.get("adInfoPlatform"));
            info.setAdInfoPlatformDesc(objMap.get("adInfoPlatformDesc"));
            info.setAdInfoDeliveryDays(objMap.get("adInfoDeliveryDays"));
            info.setAdInfoDeliveryTimes(objMap.get("adInfoDeliveryTimes"));
            info.setAdInfoFlowType(objMap.get("adInfoFlowType"));
            info.setExpAmount(objMap.get("expAmount"));
            info.setClickAmount(objMap.get("clickAmount"));
            info.setPublishPrice(objMap.get("publishPrice"));
            info.setNetPrice(objMap.get("netPrice"));
            info.setAdInfoResource(objMap.get("adInfoResource"));
            info.setIsExposure(objMap.get("isExposure"));
            info.setIsClick(objMap.get("isClick"));
            info.setMonitorPlatform(objMap.get("monitorPlatform"));
            info.setSid(objMap.get("sid"));
            info.setCpsName(objMap.get("cpsName"));
            info.setSource(objMap.get("source"));
            info.setChannelName(objMap.get("channelName"));
            info.setChannel(objMap.get("channel"));
            info.setCid(objMap.get("cid"));
            info.setLandUrl(objMap.get("landUrl"));
            info.setBiCode(objMap.get("biCode"));
            info.setMonitorExposureUrl(objMap.get("monitorExposureUrl"));
            info.setMonitorClickUrl(objMap.get("monitorClickUrl"));
            info.setAdInfoState(objMap.get("adInfoState"));
            info.setOperator(objMap.get("operator"));
            info.setUpdateTime(StringUtils.dateFormat(objMap.get("updateTime")));
            
            return info;
        }
        
        return null;
        
    }
    
    /**
     * <获取导出EXCEL文件>
     * @param context       系统上下文
     * @param conn          数据库链接
     * @param filePath      文件目录
     * @see [类、类#方法、类#成员]
     */
    public static void getAdExportFile(MethodContext context, DBConnection conn, String filePath)
    {
        //导出信息列表
        List<AdExportInfo> exportList = MktUtils.getAdExportInfo(context);
        
        //获取账号
        String account = context.getAccount();
        //数据库连接
        AdDicMapInfo dicMap = AdInfoDao.getAdDicMapInfo(conn, account);
        
        //数据查询结果
        Map<String, Object> results = context.getResults();
        //存储过程返回的列表
        
        int exportType = Integer.valueOf(results.get("adExportType").toString());
        
        //生成临时文件
        ExcelUtils.writeAdExportFile(filePath, exportList, dicMap, exportType);
    }
    
    /**
     * <获取广告位id列表>
     * @param context     接口请求上下文
     * @return            广告位ID列表
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> getAdinfoIdList(MethodContext context)
    {
        List<Integer> aidList = new ArrayList<Integer>();
        
        //数据查询结果
        Map<String, Object> results = context.getResults();
        
        //存储过程返回的列表
        List<Map<String, Integer>> list = (List<Map<String, Integer>>)results.get("result");
        
        //列表为空，直接返回
        if (null == list || list.isEmpty())
        {
            return null;
        }
        
        //遍历查询结果集，添加入列表
        for (Map<String, Integer> objMap : list)
        {
            Integer aid = objMap.get("adInfoId");
            
            if (null != aid)
            {
                aidList.add(aid);
            }
            
        }
        
        return aidList;
    }
    
    /**
     * <根据用户列表，获取用户列表邮箱列表>
     * @param conn          数据库链接
     * @param userList      用户列表
     * @return              邮箱列表
     * @see [类、类#方法、类#成员]
     */
    public static List<String> getMainSendEmailList(DBConnection conn, List<String> userList)
    {
        List<String> mainSendEmailList = new ArrayList<String>();
        
        if (null == userList || userList.isEmpty())
        {
            return mainSendEmailList;
        }
        
        //获取抄送人列表
        for (String user : userList)
        {
            String email = AdInfoDao.getUserEmail(conn, user);
            
            if (null != email)
            {
                mainSendEmailList.add(email);
            }
        }
        return mainSendEmailList;
    }
    
    /**
     * <获取管理员邮件列表>
     * @param conn      数据库链接
     * @return          邮件列表
     * @see [类、类#方法、类#成员]
     */
    public static List<String> getAdminEmailList(DBConnection conn)
    {
        List<String> adminEmailList = new ArrayList<String>();
        
        //获取管理员邮箱
        List<Map<String, Object>> resutList = DBUtil.query(conn,
            "select t1.email from t_ms_user t1 join (select distinct name as account from t_ms_model where meta = 'admin' and val = 'root')t2 on t1.account = t2.account",
            false,
            new Object[] {});
        if (null == resutList || resutList.isEmpty())
        {
            return adminEmailList;
        }
        
        for (Map<String, Object> map : resutList)
        {
            String email = EncryptUtil.decode(map.get("email").toString());
            if (null != email)
            {
                adminEmailList.add(email);
            }
        }
        
        return adminEmailList;
    }
    
    /**
     * <获取营销活动创建，抄送用户列表>
     * @param conn          数据库链接
     * @param account       用户账号
     * @param mktinfoId     营销活动ID
     * @param pageType      页面属性                  
     * @return              抄送用户邮箱列表
     * @see [类、类#方法、类#成员]
     */
    public static List<String> getMktCopySendEmailList(DBConnection conn, String account, Integer mktinfoId,
        Integer pageType)
    {
        List<String> copySendEmailList = new ArrayList<String>();
        //获取抄送人列表
        List<Map<String, Object>> ccResultList =
            DBUtil.query(conn, "call sp_getUserEmailList (?,?,?,?)", true, new Object[] {account, mktinfoId, pageType});
        if (null == ccResultList || ccResultList.isEmpty())
        {
            return copySendEmailList;
        }
        
        for (Map<String, Object> map : ccResultList)
        {
            String email = EncryptUtil.decode(map.get("email").toString());
            if (null != email)
            {
                copySendEmailList.add(email);
            }
        }
        
        return copySendEmailList;
    }
    
    /**
     * <修改用户权限>
     * @param conn              数据库链接
     * @param account           操作用户账号
     * @param mktinfoId         营销活动ID
     * @param userList          用户列表
     * @see [类、类#方法、类#成员]
     */
    public static void modifyUserPermission(DBConnection conn, String account, Integer mktinfoId, List<String> userList)
    {
        if (null == userList || userList.isEmpty())
        {
            return;
        }
        
        //修改权限
        for (String user : userList)
        {
            DBUtil.execute(conn, "call sp_mktUpdateUserRole(?,?,?,?)", true, new Object[] {account, mktinfoId, user});
        }
    }
    
    /**
     * <获取抄送邮件列表>
     * @param conn          数据库连接
     * @param account       操作员账号
     * @param mktinfoId     营销活动ID
     * @param pageType      页面类型
     * @param mainSendEmailList 主送邮件
     * @return              抄送用户列表
     * @see [类、类#方法、类#成员]
     */
    public static List<String> getCopySendEmailList(DBConnection conn, String account, Integer mktinfoId,
        Integer pageType, List<String> mainSendEmailList)
    {
        List<String> copySendEmailList = new ArrayList<String>();
        
        //系统管理员邮箱列表
        //delete by sxy 不抄送给系统管理员
        /*List<String> adminEmailList = MktUtils.getAdminEmailList(conn);
        
        copySendEmailList.addAll(adminEmailList);
        */
        
        //营销活动抄送用户邮箱列表
        List<String> mktCopyEmailList = MktUtils.getMktCopySendEmailList(conn, account, mktinfoId, pageType);
        for (String user : mktCopyEmailList)
        {
            if (!mainSendEmailList.contains(user))
            {
                copySendEmailList.add(user);
            }
            
        }
        
        //触发发送用户邮箱
        String accountEmail = AdInfoDao.getUserEmail(conn, account);
        
        if (!copySendEmailList.contains(accountEmail) && !mainSendEmailList.contains(accountEmail))
        {
            copySendEmailList.add(accountEmail);
        }
        
        return copySendEmailList;
    }
    
    /**
     * <获取邮件发送中广告位数目>
     * @param conn          数据库链接
     * @param account       用户账号
     * @param mktinfoId     活动ID
     * @param adState       广告位ID
     * @return              邮件发送中广告位数目
     * @see [类、类#方法、类#成员]
     */
    public static AdEmailNumInfo getAdEmailNum(DBConnection conn, String account, Integer mktinfoId, Integer adState)
    {
        AdEmailNumInfo info = new AdEmailNumInfo();
        List<Map<String, Object>> resutList =
            DBUtil.query(conn, "call sp_adInfoNumQuery(?,?,?,?)", true, new Object[] {account, mktinfoId, adState});
        if (null == resutList || resutList.isEmpty())
        {
            return info;
        }
        Integer totalNum = StringUtils.getIntValue(resutList.get(0).get("totalCount").toString());
        Integer vmallNum = StringUtils.getIntValue(resutList.get(0).get("vmallCount").toString());
        Integer honorNum = StringUtils.getIntValue(resutList.get(0).get("honorCount").toString());
        Integer huaweiNum = StringUtils.getIntValue(resutList.get(0).get("huaweiCount").toString());
        info.setTotalNum(totalNum);
        info.setVmallNum(vmallNum);
        info.setHonorNum(honorNum);
        info.setHuaweiNum(huaweiNum);
        return info;
    }
    
    /**
     * <获取营销活动名称>
     * @param conn              数据库链接
     * @param mktinfoId         营销活动
     * @return                  营销活动名称
     * @see [类、类#方法、类#成员]
     */
    public static String getMktName(DBConnection conn, Integer mktinfoId)
    {
        List<Map<String, Object>> resutList =
            DBUtil.query(conn, "select name from t_mkt_info where id = ?", false, new Object[] {mktinfoId});
        if (null == resutList || resutList.isEmpty())
        {
            return null;
        }
        String mktName = (String)resutList.get(0).get("name");
        
        return mktName;
    }
    
}
