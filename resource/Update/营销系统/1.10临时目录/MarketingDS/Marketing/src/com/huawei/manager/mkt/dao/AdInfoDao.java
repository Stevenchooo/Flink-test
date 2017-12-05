/*
 * 文 件 名:  AdInfoDao.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-4
 */
package com.huawei.manager.mkt.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.mkt.constant.DaoDealState;
import com.huawei.manager.mkt.constant.DicType;
import com.huawei.manager.mkt.info.AdDicMapInfo;
import com.huawei.manager.mkt.info.AdTemplateInfo;
import com.huawei.manager.mkt.info.MktMeta;
import com.huawei.util.DBUtil;
import com.huawei.util.EncryptUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-4]
 * @see  [相关类/方法]
 */
public final class AdInfoDao
{
    
    private static final String EMAIL_COLUMN_NAME = "email";
    
    private static final String DIC_VALUE_COLUMN_NAME = "dic_value";
    
    private static final String DIC_KEY_COLUMN_NAME = "dic_key";
    
    private static final String NAME_COLUMN_NAME = "name";
    
    private static final String DEPT_TYPE_COLUMN_NAME = "dept_type";
    
    private static final String PID_COLUMN_NAME = "pid";
    
    private static final String ID_COLUMN_NAME = "id";
    
    /**
     *  日志
     */
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * <默认构造函数>
     */
    private AdInfoDao()
    {
    }
    
    /**
     * <获取广告位id>
     * @param dbConn        数据库连接
     * @param sql         执行sql
     * @param parameters  输入参数
     * @return            广告位ID
     * @see [类、类#方法、类#成员]
     */
    public static Integer getAidInfo(DBConnection dbConn, String sql, Object[] parameters)
    {
        //入口日志
        LOG.debug("enter getAidInfo, parameters is {}", new Object[] {parameters});
        
        List<Map<String, Object>> resList = DBUtil.query(dbConn, sql, false, parameters);
        
        if (null == resList || resList.isEmpty())
        {
            return null;
        }
        
        try
        {
            Integer aid = Integer.valueOf(resList.get(0).get("aid").toString());
            
            LOG.debug("exit getAidInfo, aid is {}", new Object[] {aid});
            
            return aid;
        }
        catch (NumberFormatException e)
        {
            LOG.error("getAidInfo error,Exception is {NumberFormatException}");
        }
        
        return null;
        
    }
    
    /**
     * <获取系统广告信息字典集合>
     * @param conn       数据库连接
     * @param account    用户账号
     * @return           字典集合
     * @see [类、类#方法、类#成员]
     */
    public static AdDicMapInfo getAdDicMapInfo(DBConnection conn, String account)
    {
        AdDicMapInfo info = new AdDicMapInfo();
        
        //获取活动名称字典
        Map<String, List<MktMeta>> mktMap = getMktMap(conn, account);
        info.setMktMap(mktMap);
        
        //获取网站名称字典
        Map<String, List<MktMeta>> webMap = getMap(conn, new Object[] {null, account, DicType.WEB});
        info.setWebMap(webMap);
        
        //获取素材类型字典
        Map<String, String> materiaMap = getMapString(conn, new Object[] {DicType.MATERIAL});
        info.setMateriaMap(materiaMap);
        
        //获取端口所属字典
        Map<String, String> portMap = getMapString(conn, new Object[] {DicType.PORT});
        info.setPortMap(portMap);
        
        //获取着陆平台字典
        Map<String, String> platformMap = getMapString(conn, new Object[] {DicType.LAND_PLATFORM});
        info.setPlatformMap(platformMap);
        
        //获取引流类型字典
        Map<String, String> flowMap = getMapString(conn, new Object[] {DicType.FLOW});
        info.setFlowMap(flowMap);
        
        //获取资源类型字典
        Map<String, String> resourceMap = getMapString(conn, new Object[] {DicType.RESOURCE});
        info.setResourceMap(resourceMap);
        
        //获取是否监控字典
        Map<String, String> booleanMap = getMapString(conn, new Object[] {DicType.BOOLEAN});
        info.setBooleanMap(booleanMap);
        
        //获取监控平台字典
        Map<String, String> monitorPlatformMap = getMapString(conn, new Object[] {DicType.MONITOR_PLATFORM});
        info.setMonitorPlatformMap(monitorPlatformMap);
        
        //获取媒体类型与网站名称对应关系
        Map<Integer, Integer> mediaAndWebMap = getMapInteger(conn, new Object[] {DicType.WEB});
        info.setMediaAndWebMap(mediaAndWebMap);
        
        return info;
        
    }
    
    /**
     * <获取字典信息>
     * @param conn          数据库执行对象
     * @param parameters    执行参数
     * @return       字典集合
     * @see [类、类#方法、类#成员]
     */
    private static Map<Integer, Integer> getMapInteger(DBConnection conn, Object[] parameters)
    {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        
        String sql = "select distinct id,pid from t_mkt_dic_info where type = ? ";
        
        List<Map<String, Object>> resultList = DBUtil.query(conn, sql, false, parameters);
        
        if (null == resultList || resultList.isEmpty())
        {
            return map;
        }
        
        for (Map<String, Object> tmpMap : resultList)
        {
            try
            {
                //参数判断
                if (null == tmpMap)
                {
                    continue;
                }
                
                Integer id = Integer.valueOf(tmpMap.get(ID_COLUMN_NAME).toString());
                Integer pid = Integer.valueOf(tmpMap.get(PID_COLUMN_NAME).toString());
                map.put(id, pid);
            }
            catch (NumberFormatException e)
            {
                LOG.error("getMapInteger error! exception is {NumberFormatException}");
            }
            
        }
        return map;
    }
    
    /**
     * <获取字典信息>
     * @param conn   数据库执行对象
     * @param account 用户账号
     * @return       字典集合
     * @see [类、类#方法、类#成员]
     */
    private static Map<String, List<MktMeta>> getMktMap(DBConnection conn, String account)
    {
        HashMap<String, List<MktMeta>> map = new HashMap<String, List<MktMeta>>();
        
        String sql = "call sp_mktNameListQuery(?,?)";
        
        List<Map<String, Object>> resultList = DBUtil.query(conn, sql, false, new Object[] {null, account});
        
        if (null == resultList || resultList.isEmpty())
        {
            return map;
        }
        
        for (Map<String, Object> tmpMap : resultList)
        {
            try
            {
                //参数判断
                if (null == tmpMap)
                {
                    continue;
                }
                
                //获取参数
                Integer id = Integer.valueOf(tmpMap.get(ID_COLUMN_NAME).toString());
                Integer type = Integer.valueOf(tmpMap.get(DEPT_TYPE_COLUMN_NAME).toString());
                String name = tmpMap.get(NAME_COLUMN_NAME).toString();
                
                //封装成对象
                MktMeta meta = new MktMeta();
                meta.setId(id);
                meta.setType(type);
                
                //放入集合中
                List<MktMeta> list = map.get(name);
                if (null == list)
                {
                    list = new ArrayList<MktMeta>();
                }
                
                list.add(meta);
                map.put(name, list);
            }
            catch (NumberFormatException e)
            {
                LOG.error("getMktMap error! exception is {NumberFormatException}");
            }
            
        }
        
        return map;
    }
    
    /**
     * <获取字典信息>
     * @param conn          数据库执行对象
     * @param parameters    执行sql
     * @return       字典集合
     * @see [类、类#方法、类#成员]
     */
    private static Map<String, List<MktMeta>> getMap(DBConnection conn, Object[] parameters)
    {
        HashMap<String, List<MktMeta>> map = new HashMap<String, List<MktMeta>>();
        String sql = "call sp_mktWebNameListQuery(?,?,?)";
        List<Map<String, Object>> resultList = DBUtil.query(conn, sql, false, parameters);
        if (null == resultList || resultList.isEmpty())
        {
            return map;
        }
        
        for (Map<String, Object> tmpMap : resultList)
        {
            try
            {
                Integer id = Integer.valueOf((String)tmpMap.get(ID_COLUMN_NAME));
                Integer type = Integer.valueOf((String)tmpMap.get(DEPT_TYPE_COLUMN_NAME));
                String name = (String)tmpMap.get(NAME_COLUMN_NAME);
                
                MktMeta meta = new MktMeta();
                meta.setId(id);
                meta.setType(type);
                
                List<MktMeta> list = map.get(name);
                if (null == list)
                {
                    list = new ArrayList<MktMeta>();
                }
                list.add(meta);
                map.put(name, list);
            }
            catch (NumberFormatException e)
            {
                LOG.error("get map error! exception is NumberFormatException");
            }
            
        }
        
        return map;
    }
    
    /**
     * <获取字典信息>
     * @param conn   数据库执行对象
     * @param sql    执行sql
     * @param parameters 
     * @return       字典集合
     * @see [类、类#方法、类#成员]
     */
    private static Map<String, String> getMapString(DBConnection conn, Object[] parameters)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        String sql = "select dic_key,dic_value from t_mkt_common_dic_info where type = ? order by dic_key";
        List<Map<String, Object>> resultList = DBUtil.query(conn, sql, false, parameters);
        if (null == resultList || resultList.isEmpty())
        {
            return map;
        }
        
        for (Map<String, Object> tmpMap : resultList)
        {
            String id = (String)tmpMap.get(DIC_KEY_COLUMN_NAME);
            String name = (String)tmpMap.get(DIC_VALUE_COLUMN_NAME);
            map.put(name, id);
        }
        
        return map;
    }
    
    /**
     * <判断广告位信息是否是新建>
     * @param dbConn       数据库连接
     * @param info       广告位信息
     * @return           是否新增
     * @see [类、类#方法、类#成员]
     */
    public static boolean getAddAdInfoFlag(DBConnection dbConn, AdTemplateInfo info)
    {
        //广告位ID存在，则不是新建
        if (null != info.getAid())
        {
            return false;
        }
        
        //获取广告位ID
        Integer searchAid = getAidByInfo(dbConn, info);
        
        //判断是否存在,不存在则要新增
        if (null == searchAid)
        {
            return true;
        }
        info.setAid(searchAid);
        return false;
    }
    
    /**
     * <获取广告位ID>
     * @param dbConn     数据库链接
     * @param info     广告位信息
     * @return         广告位ID
     * @see [类、类#方法、类#成员]
     */
    public static Integer getAidByInfo(DBConnection dbConn, AdTemplateInfo info)
    {
        //活动名称、网站名称、频道、广告位、端口所属
        String getAidInfoSql =
            "select * from t_mkt_ad_info where id = ? and web_name = ? and channel = ? and ad_position = ? and port = ? ";
        Integer searchAid =
            AdInfoDao.getAidInfo(dbConn,
                getAidInfoSql,
                new Object[] {info.getMktId(), info.getWebNameId(), info.getAdChannel(), info.getAdPosition(),
                    info.getPortId()});
        return searchAid;
    }
    
    /**
     * <判断用户有没有权限修改广告位信息>
     * @param addFlag    是否是新增
     * @param dbConn       数据库连接
     * @param info       广告位信息
     * @param account    用户账号
     * @return           有无权限
     * @see [类、类#方法、类#成员]
     */
    public static boolean getRoleFlag(boolean addFlag, DBConnection dbConn, AdTemplateInfo info, String account)
    {
        //是新增，判断有无权限
        if (addFlag)
        {
            return canAddAdInfo(dbConn, account, info.getMktId());
        }
        else
        {
            //不是新增，判断有无更新权限
            return canUpdateAdInfo(dbConn, account, info.getMktId(), info.getAid());
        }
        
    }
    
    /**
     * <判断有没有新增广告位信息权限>
     * @param dbConn       数据库连接
     * @param account    用户账号
     * @param mktId      活动ID
     * @return           有没有权限
     * @see [类、类#方法、类#成员]
     */
    public static boolean canAddAdInfo(DBConnection dbConn, String account, Integer mktId)
    {
        //是否是广告业务用户
        boolean userflag = isAdRoleUser(dbConn, account);
        if (!userflag)
        {
            return true;
        }
        
        //是否可以创建该活动广告位
        String sql = "select * from t_mkt_user_ad_info where id = ? and account = ?";
        
        List<Map<String, Object>> list = DBUtil.query(dbConn, sql, false, new Object[] {mktId, account});
        
        //有结果则有权限
        if (null != list && !list.isEmpty())
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * <更新广告位信息>
     * @param account   用户账号
     * @param dbConn      数据库连接
     * @param info      广告位信息
     * @return          更新是否成功标志
     * @see [类、类#方法、类#成员]
     */
    public static int updateAdInfo(String account, DBConnection dbConn, AdTemplateInfo info)
    {
        
        //更新数据
        String sql = "call sp_adInfoModify(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int res =
            DBUtil.execute(dbConn,
                sql,
                true,
                new Object[] {account, info.getAid(), info.getMktId(), info.getMediaTypeId(), info.getWebNameId(),
                    info.getAdChannel(), info.getAdPosition(), info.getMaterialTypeId(), info.getMaterialDesc(),
                    info.getPortId(), info.getPlatformId(), info.getPlatformDesc(), info.getDeliveryDays(),
                    info.getDeliveryTimes(), info.getFlowTypeId(), info.getExpAmount(), info.getClickAmount(),
                    info.getPublishPrice(), info.getNetPrice(), info.getResourceId(), info.getIsExposureId(),
                    info.getIsClickId(), info.getMonitorPlatformId()});
        return res;
    }
    
    /**
     * <查看有没有权限修改广告位信息>
     * @param dbConn        数据库连接
     * @param account     用户账号
     * @param mktId       活动ID
     * @param aid         广告位ID
     * @return            是否有权限修改
     * @see [类、类#方法、类#成员]
     */
    public static boolean canUpdateAdInfo(DBConnection dbConn, String account, Integer mktId, Integer aid)
    {
        //是否是广告业务用户
        boolean userflag = isAdRoleUser(dbConn, account);
        if (!userflag)
        {
            return true;
        }
        
        //是否可以创建该活动广告位,0代码只能查看自己，1代码可以查询全部
        String sql = "select * from t_mkt_user_ad_info where id = ? and account = ? and flag = 1";
        List<Map<String, Object>> list = DBUtil.query(dbConn, sql, false, new Object[] {mktId, account});
        
        if (null != list && !list.isEmpty())
        {
            return true;
        }
        
        //查看操作员是否满足条件
        sql = "select * from t_mkt_ad_info where aid = ? and operator = ?";
        list = DBUtil.query(dbConn, sql, false, new Object[] {aid, account});
        
        if (null != list && !list.isEmpty())
        {
            return true;
        }
        return false;
    }
    
    /**
     * <新增广告位信息>
     * @param account       用户账号
     * @param dbConn          数据库连接
     * @param info          广告位信息
     * @return              新增广告位状态
     * @see [类、类#方法、类#成员]
     */
    public static int addAdInfo(String account, DBConnection dbConn, AdTemplateInfo info)
    {
        //插入数据表
        String sql = "call sp_adInfoCreate(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        int res =
            DBUtil.execute(dbConn,
                sql,
                true,
                new Object[] {account, info.getMktId(), info.getMediaTypeId(), info.getWebNameId(),
                    info.getAdChannel(), info.getAdPosition(), info.getMaterialTypeId(), info.getPortId(),
                    info.getPlatformId(), info.getPlatformDesc(), info.getDeliveryDays(), info.getDeliveryTimes(),
                    info.getFlowTypeId(), info.getExpAmount(), info.getClickAmount(), info.getPublishPrice(),
                    info.getNetPrice(), info.getResourceId(), info.getIsExposureId(), info.getIsClickId(),
                    info.getMonitorPlatformId(), info.getMaterialDesc(), info.getDeliveryBeginDay(),
                    info.getDeliveryEndDay()});
        
        if (DaoDealState.OK == res)
        {
            Integer aid = getAidByInfo(dbConn, info);
            info.setAid(aid);
        }
        
        return res;
        
    }
    
    /**
     * <判断是否是广告角色用户>
     * @param dbConn         数据库连接
     * @param account      用户账号
     * @return             是否是广告角色账号
     * @see [类、类#方法、类#成员]
     */
    public static boolean isAdRoleUser(DBConnection dbConn, String account)
    {
        //判断是否是广告角色人员
        String sql = "select * from t_ms_model where name= ? and val REGEXP  '^ad'";
        List<Map<String, Object>> list = DBUtil.query(dbConn, sql, false, new Object[] {account});
        
        if (null != list && !list.isEmpty())
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * <查询用户的邮箱>
     * @param conn    数据库链接
     * @param user    用户名
     * @return        加密后邮箱值
     * @see [类、类#方法、类#成员]
     */
    public static String getUserEmail(DBConnection conn, String user)
    {
        List<Map<String, Object>> resutList =
            DBUtil.query(conn, "select email from t_ms_user where account = ?", false, new Object[] {user});
        
        if (null == resutList || resutList.isEmpty())
        {
            return null;
        }
        String email = EncryptUtil.decode(resutList.get(0).get(EMAIL_COLUMN_NAME).toString());
        
        if (null == email || 0 == email.trim().length())
        {
            return null;
        }
        return email;
    }
    
    /**
     * <获取广告位对应的属性>
     * @param dbConn    数据库链接
     * @param aid       广告位ID
     * @return          广告位属性
     * @see [类、类#方法、类#成员]
     */
    public static Integer getAdInfoDeptType(DBConnection dbConn, Integer aid)
    {
        String sql = "select dept_type from t_mkt_ad_info where aid = ?";
        List<Map<String, Object>> resutList = DBUtil.query(dbConn, sql, false, new Object[] {aid});
        if (null == resutList || resutList.isEmpty())
        {
            return null;
        }
        
        Integer deptType = Integer.valueOf(resutList.get(0).get(DEPT_TYPE_COLUMN_NAME).toString());
        return deptType;
        
    }
    
    /**
     * <判断用户数是否是超级管理员>
     * @param dbConn        数据库链接
     * @param account       用户账号
     * @return              是否是超级管理员
     * @see [类、类#方法、类#成员]
     */
    public static boolean isAdminAccount(DBConnection dbConn, String account)
    {
        String sql = "select name from t_ms_model where meta = 'admin' and val = 'root' and name = ?";
        List<Map<String, Object>> resutList = DBUtil.query(dbConn, sql, false, new Object[] {account});
        
        //找不到则不是管理员
        if (null == resutList || resutList.isEmpty())
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * <获取用户属性> 
     * @param dbConn        数据库链接
     * @param account       用户账号
     * @return              用户属性
     * @see [类、类#方法、类#成员]
     */
    public static Integer getUserDeptType(DBConnection dbConn, String account)
    {
        String sql = "select deptType as dept_type from t_ms_user where account = ?";
        List<Map<String, Object>> resutList = DBUtil.query(dbConn, sql, false, new Object[] {account});
        if (null == resutList || resutList.isEmpty())
        {
            return -1;
        }
        
        Integer deptType = Integer.valueOf(resutList.get(0).get(DEPT_TYPE_COLUMN_NAME).toString());
        return deptType;
    }
    
    /**
     * <获取营销活动部门属性>
     * @param dbConn    数据库链接
     * @param mktId     营销活动ID
     * @return          营销活动部门属性
     * @see [类、类#方法、类#成员]
     */
    public static Integer getMktDeptType(DBConnection dbConn, Integer mktId)
    {
        String sql = "select dept_type from t_mkt_info where id = ?";
        List<Map<String, Object>> resutList = DBUtil.query(dbConn, sql, false, new Object[] {mktId});
        if (null == resutList || resutList.isEmpty())
        {
            return null;
        }
        
        try
        {
            Integer deptType = Integer.valueOf(resutList.get(0).get(DEPT_TYPE_COLUMN_NAME).toString());
            return deptType;
        }
        catch (NumberFormatException e)
        {
            LOG.error("formate mktid {} deptType NumberFormatException", mktId);
        }
        
        return null;
        
    }
    
    /**
     * <获取网站名称属性>
     * @param dbConn            数据库链接
     * @param webNameId         网站名称ID
     * @return                  网站名称属性
     * @see [类、类#方法、类#成员]
     */
    public static Integer getWebNameDeptType(DBConnection dbConn, Integer webNameId)
    {
        String sql = "select dept_type from t_mkt_dic_info where id = ?";
        List<Map<String, Object>> resutList = DBUtil.query(dbConn, sql, false, new Object[] {webNameId});
        if (null == resutList || resutList.isEmpty())
        {
            return null;
        }
        
        Integer deptType = Integer.valueOf(resutList.get(0).get(DEPT_TYPE_COLUMN_NAME).toString());
        return deptType;
    }
    
    /**
     * <获取网站名称属性>
     * @param dbConn    数据库链接
     * @param aid       网站名称ID
     * @return          网站名称属性
     * @see [类、类#方法、类#成员]
     */
    public static Integer getMktAdinfoDeptType(DBConnection dbConn, Integer aid)
    {
        String sql = "select dept_type from t_mkt_ad_info where aid = ?";
        List<Map<String, Object>> resList = DBUtil.query(dbConn, sql, false, new Object[] {aid});
        
        if (null == resList || resList.isEmpty())
        {
            return -1;
        }
        
        try
        {
            Integer type = Integer.valueOf((String)resList.get(0).get(DEPT_TYPE_COLUMN_NAME));
            
            return type;
        }
        catch (NumberFormatException e)
        {
            LOG.error("getAidInfo error!");
        }
        return -1;
    }
}
