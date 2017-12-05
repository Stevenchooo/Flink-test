/*
 * 文 件 名:  AdInfoTemplateCheck.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-4
 */
package com.huawei.manager.mkt.util.checkUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.huawei.manager.mkt.constant.BooleanType;
import com.huawei.manager.mkt.dao.AdInfoDao;
import com.huawei.manager.mkt.info.AdTemplateInfo;
import com.huawei.manager.mkt.info.FieldCheckInfo;
import com.huawei.manager.mkt.info.MktMeta;
import com.huawei.manager.mkt.util.StringUtils;
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
public class AdInfoTemplateCheck
{
    private static final String DEFAULT_HONOR_PLATFORM = "VMALL";
    
    private static final String DEFAULT_HUAWEI_PLATFORM = "华为官网";
    
    private static final String DEFAULT_MONITOR_PLATFORM = "云平台";
    
    private static final String DEFAULT_CLICK = "是";
    
    private static final String DEFAULT_EXPOSURE = DEFAULT_CLICK;
    
    private static final String DEFAULT_RESOURCE = "BD资源";
    
    private static final String DEFAULT_FLOW_TYPE = "预约引流";
    
    private static final String DEFAULT_DELIVERY_DAYS = "30";
    
    private static final String DEFAULT_PORT = "PC";
    
    private static final String DEFAULT_MATERIAL_TYPE = "图片";
    
    private static final String DEFAULT_AD_POSITION = "全部";
    
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * <校验输入的广告位ID是否正确>
     * @param dbConn         数据库连接
     * @param info         广告位信息
     * @param buffer       校验信息
     * @return             是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkAidField(DBConnection dbConn, AdTemplateInfo info, StringBuffer buffer)
    {
        if (null == info || null == buffer)
        {
            return false;
        }
        
        String aidStr = info.getAidStr();
        if (null == aidStr)
        {
            return true;
        }
        
        boolean flag = StringUtils.checkIntegerValue(buffer, aidStr, "广告位ID");
        
        if (!flag)
        {
            return false;
        }
        
        Integer aid = Integer.valueOf(aidStr);
        info.setAid(aid);
        
        //判断广告位ID是否正确
        boolean exist = checkAidExist(dbConn, aid);
        if (!exist)
        {
            buffer.append("广告位ID不正确;");
            return false;
        }
        
        //获取广告位ID属性
        Integer deptType = AdInfoDao.getMktAdinfoDeptType(dbConn, aid);
        
        info.setDeptType(deptType);
        
        return true;
        
    }
    
    /**
     * <检查广告位ID是否存在>
     * @param dbConn    数据库连接
     * @param aid     广告位ID
     * @return        是否合法
     * @see [类、类#方法、类#成员]
     */
    private static boolean checkAidExist(DBConnection dbConn, Integer aid)
    {
        String getAidInfoSql = "select * from t_mkt_ad_info where aid = ?";
        
        Integer searchAid = AdInfoDao.getAidInfo(dbConn, getAidInfoSql, new Object[] {aid});
        
        if (null == searchAid || !searchAid.equals(aid))
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * <检查活动名称是否正确>
     * @param info    广告位信息
     * @param map     活动名称集合
     * @param buffer  校验信息
     * @param userType 用户类型
     * @return        是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkMktNameField(AdTemplateInfo info, Map<String, List<MktMeta>> map, StringBuffer buffer,
        Integer userType)
    {
        if (null == info || null == map || null == buffer)
        {
            return false;
        }
        
        String mktName = info.getMktName();
        List<MktMeta> list = map.get(mktName);
        if (null == list || list.isEmpty())
        {
            buffer.append("活动名称检查错误,此名称不存在;");
            return false;
        }
        
        //若存在一个活动
        if (1 == list.size())
        {
            info.setMktId(list.get(0).getId());
            return true;
        }
        
        Integer deptType = info.getDeptType();
        //若存在多个
        for (MktMeta meta : list)
        {
            if (null != meta)
            {
                Integer type = meta.getType();
                
                //先根据广告位ID进行查询
                if (null != deptType)
                {
                    if (deptType.equals(type))
                    {
                        info.setMktId(meta.getId());
                        break;
                    }
                    
                }
                
                //后根据用户类型进行查询
                else if (null != type && type.equals(userType))
                {
                    info.setMktId(meta.getId());
                    break;
                }
            }
        }
        
        return true;
        
    }
    
    /**
     * <检查web名称是否正确>
     * @param info        广告位信息
     * @param map         网站名称集合
     * @param buffer      校验信息
     * @param userType    账号类型
     * @return            是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkWebNameField(AdTemplateInfo info, Map<String, List<MktMeta>> map, StringBuffer buffer,
        Integer userType)
    {
        //参数校验
        if (null == info || null == map || null == buffer)
        {
            return false;
        }
        
        String webName = info.getWebName();
        List<MktMeta> list = map.get(webName);
        
        //找到直接返回
        if (null != list && !list.isEmpty())
        {
            Integer webNameId = getWebNameId(list, userType, info.getDeptType());
            info.setWebNameId(webNameId);
            
            return true;
        }
        
        //输入值为空，直接返回错误
        if (null == webName)
        {
            buffer.append("网站名称检查错误,名称不存在;");
            return false;
        }
        
        //遍历列表，不区分大小写处理
        for (Entry<String, List<MktMeta>> entry : map.entrySet())
        {
            String key = entry.getKey();
            
            if (key.equalsIgnoreCase(webName))
            {
                info.setWebName(key);
                list = map.get(key);
                Integer webNameId = getWebNameId(list, userType, info.getDeptType());
                info.setWebNameId(webNameId);
                return true;
            }
            
        }
        
        buffer.append("网站名称检查错误,名称不存在;");
        return false;
        
    }
    
    /**
     * <获取网站名称ID>
     * @param list              字典列表
     * @param userType          用户属性
     * @param adInfoDeptType    活动属性
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static Integer getWebNameId(List<MktMeta> list, Integer userType, Integer adInfoDeptType)
    {
        if (null == list || list.isEmpty())
        {
            return null;
        }
        
        if (1 == list.size())
        {
            return list.get(0).getId();
        }
        
        for (MktMeta meta : list)
        {
            if (null == meta)
            {
                continue;
            }
            
            Integer type = meta.getType();
            
            if (null != adInfoDeptType)
            {
                if (adInfoDeptType.equals(type))
                {
                    return meta.getId();
                }
            }
            else if (null != type && type.equals(userType))
            {
                return meta.getId();
            }
            
        }
        return null;
    }
    
    /**
     * <检查媒体类型与网站名称对应关系是否正确>
     * @param info                   广告位信息
     * @param map       媒体类型与广告位对应关系集合
     * @param buffer                 校验信息
     * @return                       对应关系是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkMediaAndWebField(AdTemplateInfo info, Map<Integer, Integer> map, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == map || null == buffer)
        {
            return false;
        }
        
        if (null == info.getMediaTypeId() || null == info.getWebNameId())
        {
            return true;
        }
        
        //获取对应ID
        Integer pid = map.get(info.getWebNameId());
        
        if (null == pid || !pid.equals(info.getMediaTypeId()))
        {
            buffer.append("网站名称与媒体类型对应关系错误;");
            return false;
        }
        return true;
    }
    
    /**
     * <检查channel是否合法>
     * @param info        广告位信息
     * @param buffer      校验信息
     * @return            是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkChannelField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == buffer)
        {
            return false;
        }
        
        //字段检查
        FieldCheckInfo channelCheckInfo = StringUtils.checkField(info.getAdChannel(), true, 100);
        
        if (null != channelCheckInfo)
        {
            buffer.append("频道检查错误, " + channelCheckInfo.getDesc() + ";");
            return false;
        }
        return true;
    }
    
    /**
     * <检查广告位是否合法>
     * @param info        广告位信息
     * @param buffer      校验信息
     * @return            是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkAdPositionField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == buffer)
        {
            return false;
        }
        
        String adPosition = info.getAdPosition();
        
        if (null == adPosition || 0 == adPosition.trim().length())
        {
            info.setAdPosition(DEFAULT_AD_POSITION);
        }
        
        FieldCheckInfo adPositonCheckInfo = StringUtils.checkField(info.getAdPosition(), true, 100);
        
        if (null != adPositonCheckInfo)
        {
            buffer.append("广告位检查错误, " + adPositonCheckInfo.getDesc() + ";");
            return false;
        }
        return true;
    }
    
    /**
     * <检查素材类型是否正确>
     * @param info            广告位信息
     * @param map      素材信息集合
     * @param buffer          校验信息
     * @return                是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkMaterialTypeField(AdTemplateInfo info, Map<String, String> map, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == map || null == buffer)
        {
            return false;
        }
        
        String materialType = info.getMaterialType();
        if (null == materialType)
        {
            info.setMaterialType(DEFAULT_MATERIAL_TYPE);
        }
        
        //素材可以为空
        String materialTypeId = map.get(info.getMaterialType());
        if (null != materialTypeId)
        {
            info.setMaterialTypeId(materialTypeId);
            
            return true;
        }
        
        materialType = info.getMaterialType();
        for (Entry<String, String> entry : map.entrySet())
        {
            String key = entry.getKey();
            if (key.equalsIgnoreCase(materialType))
            {
                info.setMaterialType(key);
                info.setMaterialTypeId(map.get(key));
                return true;
            }
        }
        
        buffer.append("广告素材类型检查错误,名称不存在;");
        return false;
        
    }
    
    /**
     * <检查端口所属> 
     * @param info           广告位信息
     * @param map        端口所属集合
     * @param buffer         校验信息
     * @return               是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkPortField(AdTemplateInfo info, Map<String, String> map, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == map || null == buffer)
        {
            return false;
        }
        String port = info.getPort();
        if (null == port || 0 == port.trim().length())
        {
            info.setPort(DEFAULT_PORT);
        }
        
        //port必须选择
        String portId = map.get(info.getPort());
        if (null != portId)
        {
            info.setPortId(portId);
            
            return true;
        }
        
        port = info.getPort();
        for (Entry<String, String> entry : map.entrySet())
        {
            String key = entry.getKey();
            if (key.equalsIgnoreCase(port))
            {
                info.setPort(key);
                info.setPortId(map.get(key));
                return true;
            }
        }
        
        buffer.append("端口所属检查错误,名称不存在;");
        return false;
        
    }
    
    /**
     * <检查着陆平台是否正确>
     * @param dbConn               数据库链接
     * @param info                 广告位信息
     * @param map                  着陆平台字典集合
     * @param buffer               校验信息
     * @param userDeptType         用户属性
     * @param adminFlag             是否为管理员
     * @return                     是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkPlatformField(DBConnection dbConn, AdTemplateInfo info, Map<String, String> map,
        StringBuffer buffer, Integer userDeptType, boolean adminFlag)
    {
        //参数校验
        if (null == info || null == map || null == buffer)
        {
            return false;
        }
        
        if (null == info.getPlatform())
        {
            //如果是超级管理员，则根据活动ID选择默认平台
            if (adminFlag)
            {
                setAdminDefaultPlatform(dbConn, info);
            }
            
            //如果不是超级管理员，根据用户类型选择默认平台
            else if (0 == userDeptType)
            {
                info.setPlatform(DEFAULT_HONOR_PLATFORM);
            }
            else
            {
                info.setPlatform(DEFAULT_HUAWEI_PLATFORM);
            }
        }
        
        //着陆平台不可为空
        String platformId = map.get(info.getPlatform());
        if (null != platformId)
        {
            info.setPlatformId(platformId);
            
            return true;
        }
        
        String platform = info.getPlatform();
        for (Entry<String, String> entry : map.entrySet())
        {
            String key = entry.getKey();
            if (key.equalsIgnoreCase(platform))
            {
                info.setPlatform(key);
                info.setPlatformId(map.get(key));
                return true;
            }
        }
        
        buffer.append("着陆平台检查错误,此名称不存在;");
        return false;
        
    }
    
    /**
     * <设置管理员默认着陆平台>
     * @param dbConn        数据库链接
     * @param info          广告位信息
     * @see [类、类#方法、类#成员]
     */
    private static void setAdminDefaultPlatform(DBConnection dbConn, AdTemplateInfo info)
    {
        if (null != info && null != info.getMktId())
        {
            Integer mktDeptType = AdInfoDao.getMktDeptType(dbConn, info.getMktId());
            
            //如果不是超级管理员，根据用户类型选择默认平台
            if (null != mktDeptType && 0 == mktDeptType)
            {
                info.setPlatform(DEFAULT_HONOR_PLATFORM);
            }
            else
            {
                info.setPlatform(DEFAULT_HUAWEI_PLATFORM);
            }
        }
    }
    
    /**
     * <检查着陆平台描述是否正确>
     * @param info                 广告位信息
     * @param buffer               校验信息
     * @return                     是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkPlatformDescField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == buffer)
        {
            return false;
        }
        
        //平台描述可以为空
        FieldCheckInfo platformDescCheckInfo = StringUtils.checkField(info.getPlatformDesc(), false, 255);
        if (null != platformDescCheckInfo)
        {
            buffer.append("着陆页面描述错误, " + platformDescCheckInfo.getDesc() + ";");
            return false;
        }
        
        return true;
    }
    
    /**
     * <检查投放天数是否正确>
     * @param info           广告位信息
     * @param buffer         校验信息
     * @return               是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkDeliveryDaysField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == buffer)
        {
            return false;
        }
        
        String deliveryDaysStr = info.getDeliveryDaysStr();
        
        if (null == deliveryDaysStr || 0 == deliveryDaysStr.trim().length())
        {
            info.setDeliveryDaysStr(DEFAULT_DELIVERY_DAYS);
        }
        
        boolean flag = StringUtils.checkIntegerValue(buffer, info.getDeliveryDaysStr(), "投放天数");
        
        if (!flag)
        {
            return false;
        }
        
        Integer deliveryDays = Integer.valueOf(info.getDeliveryDaysStr());
        if (deliveryDays <= 0)
        {
            buffer.append("投放天数格式错误,投放天数必须大于0;");
        }
        info.setDeliveryDays(deliveryDays);
        
        return true;
    }
    
    /**
     * <判断投入日期是否合法>
     * @param info       广告位信息
     * @param buffer     校验信息
     * @return           是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkDeliveryTimesField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == buffer)
        {
            return false;
        }
        
        //若投入日期为空，设置为默认值
        if (null == info.getDeliveryTimes())
        {
            String deliveryTimes = StringUtils.getDefaultDeliveryTimes();
            info.setDeliveryTimes(deliveryTimes);
        }
        
        //日期校验
        FieldCheckInfo deliveryTimesCheckInfo = checkDeliveryTimes(info.getDeliveryTimes());
        
        if (null != deliveryTimesCheckInfo)
        {
            buffer.append("投放日期错误, " + deliveryTimesCheckInfo.getDesc() + ";");
            return false;
        }
        
        //获取开始截止日期
        String[] days = info.getDeliveryTimes().split("-");
        info.setDeliveryBeginDay(days[0]);
        info.setDeliveryEndDay(days[1]);
        return true;
        
    }
    
    /**
     * <检查投放日期>
     * @param deliveryTimes   投放日期
     * @return                检查信息
     * @see [类、类#方法、类#成员]
     */
    public static FieldCheckInfo checkDeliveryTimes(String deliveryTimes)
    {
        FieldCheckInfo info = null;
        
        //判空处理
        info = checkDeliveryTimeNotNULL(deliveryTimes);
        if (null != info)
        {
            return info;
        }
        
        //正则表达式
        info = checkDeliveryTimesFormate(deliveryTimes);
        if (null != info)
        {
            return info;
        }
        
        info = checkDeliveryTimesValid(deliveryTimes);
        
        return info;
    }
    
    /**
     * <检查投入日期时间逻辑是否正确>
     * @param deliveryTimes    投入日期
     * @return                 检查信息
     * @see [类、类#方法、类#成员]
     */
    private static FieldCheckInfo checkDeliveryTimesValid(String deliveryTimes)
    {
        FieldCheckInfo info = new FieldCheckInfo();
        
        //上段代码已经判断了YYYYMMDD-YYYYMMDD格式，这边不做长度判断
        String[] days = deliveryTimes.split("-");
        
        Date fromDay = getDate(days[0]);
        
        Date endDay = getDate(days[1]);
        
        //开始日期格式判断
        if (null == fromDay)
        {
            info.setFlag(false);
            info.setDesc(days[0] + "格式错误");
            return info;
        }
        
        //结束日期判断
        if (null == endDay)
        {
            info.setFlag(false);
            info.setDesc(days[1] + "格式错误");
            return info;
        }
        
        //开始日期与结束日期大小判读
        if (fromDay.after(endDay))
        {
            info.setFlag(false);
            info.setDesc(days[0] + "大于" + days[1]);
            return info;
        }
        
        return null;
    }
    
    /**
     * <判断投放日期是否满足日期格式要求>
     * @param deliveryTimes      投放日期
     * @return                   检查信息
     * @see [类、类#方法、类#成员]
     */
    private static FieldCheckInfo checkDeliveryTimesFormate(String deliveryTimes)
    {
        Pattern pattern = Pattern.compile("^[0-9]{8}-[0-9]{8}$");
        Matcher matcher = pattern.matcher(deliveryTimes);
        
        if (!matcher.matches())
        {
            FieldCheckInfo info = new FieldCheckInfo();
            info.setFlag(false);
            info.setDesc("不满足YYYYMMDD-YYYYMMDD的日期格式");
            return info;
        }
        
        return null;
    }
    
    /**
     * <判断时间是否为空>
     * @param deliveryTimes      投放时间
     * @return                   检查信息
     * @see [类、类#方法、类#成员]
     */
    private static FieldCheckInfo checkDeliveryTimeNotNULL(String deliveryTimes)
    {
        if (null == deliveryTimes || 0 == deliveryTimes.trim().length())
        {
            FieldCheckInfo info = new FieldCheckInfo();
            info.setFlag(false);
            info.setDesc("不能为空");
            
            return info;
        }
        
        return null;
    }
    
    /**
     * <获取日期值>
     * @param time           时间字符串
     * @return               日期
     * @see [类、类#方法、类#成员]
     */
    private static Date getDate(String time)
    {
        try
        {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMMdd");
            inputDateFormat.setLenient(false);
            Date date = inputDateFormat.parse(time);
            return date;
        }
        catch (ParseException e)
        {
            LOG.error("getDate error! time is {} and ParseException is {}");
        }
        return null;
    }
    
    /**
     * <检查引流类型是否正确>
     * @param info             广告位信息
     * @param map          引流类型集合
     * @param buffer           校验信息
     * @return                 是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkFlowTypeField(AdTemplateInfo info, Map<String, String> map, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == map || null == buffer)
        {
            return false;
        }
        
        String flowType = info.getFlowType();
        
        //引流类型可以为空
        if (null == flowType)
        {
            info.setFlowType(DEFAULT_FLOW_TYPE);
        }
        
        String flowTypeId = map.get(info.getFlowType());
        if (null == flowTypeId)
        {
            buffer.append("引流类型检查错误,名称不存在;");
            return false;
        }
        info.setFlowTypeId(flowTypeId);
        
        return true;
    }
    
    /**
     * <检查预计曝光量是否正确>
     * @param info         广告位信息
     * @param buffer       校验信息
     * @return             是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkExpAmountField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == buffer)
        {
            return false;
        }
        
        //预计曝光量
        String expAmountStr = info.getExpAmountStr();
        if (null == expAmountStr)
        {
            return true;
        }
        
        boolean flag = StringUtils.checkIntegerValue(buffer, expAmountStr, "预计曝光量");
        
        if (!flag)
        {
            return false;
        }
        
        try
        {
            Integer expAmount = Integer.valueOf(expAmountStr);
            info.setExpAmount(expAmount);
        }
        catch (NumberFormatException e)
        {
            LOG.error("expAmountStr formate error! expAmountStr is {} and NumberFormatException is {}");
        }
        
        return true;
    }
    
    /**
     * <检查点击量是否正确>
     * @param info     广告位信息
     * @param buffer   校验信息
     * @return         是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkClickAmountField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == buffer)
        {
            return false;
        }
        
        String clickAmountStr = info.getClickAmountStr();
        
        //点击量可以为空
        if (null == clickAmountStr)
        {
            return true;
        }
        
        boolean flag = StringUtils.checkIntegerValue(buffer, clickAmountStr, "预计点击量");
        
        if (!flag)
        {
            return false;
        }
        
        Integer clickAmount = Integer.valueOf(clickAmountStr);
        info.setClickAmount(clickAmount);
        
        return true;
    }
    
    /**
     * <检查刊例价是否正确>
     * @param info          广告位信息
     * @param buffer        校验信息
     * @return              是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkPublishPriceField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == buffer)
        {
            return false;
        }
        
        String publishPriceStr = info.getPublishPriceStr();
        
        if (null == publishPriceStr)
        {
            return true;
        }
        
        boolean flag = StringUtils.checkIntegerValue(buffer, publishPriceStr, "刊例价");
        
        if (!flag)
        {
            return false;
        }
        
        Integer publishPrice = Integer.valueOf(publishPriceStr);
        info.setPublishPrice(publishPrice);
        
        return true;
    }
    
    /**
     * <检查净价是否正确>
     * @param info         广告位信息
     * @param buffer       校验信息
     * @return             是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkNetPriceField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == buffer)
        {
            return false;
        }
        
        String netPriceStr = info.getNetPriceStr();
        
        if (null == netPriceStr)
        {
            return true;
        }
        
        boolean flag = StringUtils.checkIntegerValue(buffer, netPriceStr, "净价");
        
        if (!flag)
        {
            return false;
        }
        
        Integer netPrice = Integer.valueOf(netPriceStr);
        info.setNetPrice(netPrice);
        return true;
    }
    
    /**
     * <检查资源来源是否正确>
     * @param info         广告位信息
     * @param map  资源来源集合
     * @param buffer       校验信息
     * @return             是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkResourceField(AdTemplateInfo info, Map<String, String> map, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == map || null == buffer)
        {
            return false;
        }
        
        String resource = info.getResource();
        if (null == resource)
        {
            info.setResource(DEFAULT_RESOURCE);
        }
        String resourceId = map.get(info.getResource());
        
        if (null != resourceId)
        {
            info.setResourceId(resourceId);
            
            return true;
        }
        
        resource = info.getResource();
        for (Entry<String, String> entry : map.entrySet())
        {
            String key = entry.getKey();
            if (key.equalsIgnoreCase(resource))
            {
                info.setResource(key);
                info.setResourceId(map.get(key));
                return true;
            }
            
        }
        buffer.append("资源来源检查错误,名称不存在;");
        return false;
        
    }
    
    /**
     * <检查是否监控曝光是否合法>
     * @param info          广告位信息
     * @param map    是否集合
     * @param buffer        校验信息
     * @return              是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkIsExposureField(AdTemplateInfo info, Map<String, String> map, StringBuffer buffer)
    {
        
        //参数校验
        if (null == info || null == map || null == buffer)
        {
            return false;
        }
        
        String isExposure = info.getIsExposure();
        //用户不输入监控信息默认监控
        if (null == isExposure)
        {
            info.setIsExposure(DEFAULT_EXPOSURE);
            info.setIsExposureId(BooleanType.YES);
            return true;
        }
        
        String isExposureId = map.get(isExposure);
        if (null == isExposureId)
        {
            buffer.append("是否监控曝光检查错误,名称不存在;");
            return false;
        }
        info.setIsExposureId(isExposureId);
        
        return true;
    }
    
    /**
     * <检查是否监控点击是否合法>
     * @param info          广告位信息
     * @param map    是否集合
     * @param buffer        校验信息
     * @return              是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkIsClickField(AdTemplateInfo info, Map<String, String> map, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == map || null == buffer)
        {
            return false;
        }
        
        //用户不选择默认监控
        String isClick = info.getIsClick();
        if (null == isClick)
        {
            info.setIsClick(DEFAULT_CLICK);
            info.setIsClickId(BooleanType.YES);
            return true;
        }
        
        String isClickId = map.get(isClick);
        if (null == isClickId)
        {
            buffer.append("是否监控点击检查错误,名称不存在;");
            return false;
        }
        info.setIsClickId(isClickId);
        return true;
    }
    
    /**
     * <检查是监控平台是否合法>
     * @param info                  广告位信息
     * @param map    监控平台集合
     * @param buffer                校验信息
     * @return                      是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkMonitorPlatformField(AdTemplateInfo info, Map<String, String> map, StringBuffer buffer)
    {
        
        //参数校验
        if (null == info || null == map || null == buffer)
        {
            return false;
        }
        
        //
        String monitorPlatform = info.getMonitorPlatform();
        if (null == monitorPlatform || 0 == monitorPlatform.trim().length())
        {
            info.setMonitorPlatform(DEFAULT_MONITOR_PLATFORM);
        }
        
        //监控平台必须选择
        String monitorPlatformId = map.get(info.getMonitorPlatform());
        if (null == monitorPlatformId)
        {
            buffer.append("监控平台检查错误,此名称不存在;");
            return false;
        }
        info.setMonitorPlatformId(monitorPlatformId);
        return true;
    }
    
    /**
     * <检查是素材要求是否合法>
     * @param info                  广告位信息
     * @param buffer                校验信息
     * @return                      是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkMaterialDescField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == buffer)
        {
            return false;
        }
        
        //平台描述可以为空
        FieldCheckInfo materialDescCheckInfo = StringUtils.checkField(info.getMaterialDesc(), false, 2000);
        if (null != materialDescCheckInfo)
        {
            buffer.append("素材要求错误, " + materialDescCheckInfo.getDesc() + ";");
            return false;
        }
        
        return true;
        
    }
    
    /**
     * <检查用户名称、广告位ID、活动ID、网站名称对应关系是否正确>
     * @param dbConn            数据库链接
     * @param info              广告位信息
     * @param userDeptType      用户账号类型
     * @param buffer            出错返回信息
     * @param adminFlag         是否是管理员
     * @see [类、类#方法、类#成员]
     */
    public static void checkAdInfoUserAuthority(DBConnection dbConn, AdTemplateInfo info, Integer userDeptType,
        StringBuffer buffer, boolean adminFlag)
    {
        //若活动名称、网站名称未选，直接返回
        if (null == info || null == info.getMktId() || null == info.getWebNameId())
        {
            return;
        }
        //获取活动ID属性
        Integer mktIdDeptType = AdInfoDao.getMktDeptType(dbConn, info.getMktId());
        
        //如果找不到对应属性，返回
        if (null == mktIdDeptType)
        {
            return;
        }
        
        //获取网站属性
        Integer webNameDeptType = AdInfoDao.getWebNameDeptType(dbConn, info.getWebNameId());
        
        //如果不是超级管理员，判断活动属性与操作员属性是否一致
        if (!adminFlag)
        {
            if (!mktIdDeptType.equals(userDeptType))
            {
                buffer.append("操作员无法操作该营销活动;");
                return;
            }
        }
        
        if (!mktIdDeptType.equals(webNameDeptType))
        {
            buffer.append("营销活动名称与网站名称对应关系错误,请重新选择对应的网站名称;");
            return;
        }
        
        //根据广告位ID判断
        if (null != info.getAid())
        {
            Integer aidDeptType = AdInfoDao.getAdInfoDeptType(dbConn, info.getAid());
            
            if (!mktIdDeptType.equals(aidDeptType))
            {
                buffer.append("营销活动名称与广告位ID对应的广告位部门属性不一致错误;");
                return;
            }
        }
        
    }
    
}
