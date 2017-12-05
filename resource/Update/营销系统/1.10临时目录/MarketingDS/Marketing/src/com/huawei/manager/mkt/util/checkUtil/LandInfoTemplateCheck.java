/*
 * 文 件 名:  LandInfoTemplateCheck.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-5
 */
package com.huawei.manager.mkt.util.checkUtil;

import com.huawei.manager.mkt.constant.AdState;
import com.huawei.manager.mkt.constant.LandPlatform;
import com.huawei.manager.mkt.constant.UploadDealState;
import com.huawei.manager.mkt.dao.AdInfoDao;
import com.huawei.manager.mkt.dao.LandInfoDao;
import com.huawei.manager.mkt.entity.AdInfoEntity;
import com.huawei.manager.mkt.info.AdDicMapInfo;
import com.huawei.manager.mkt.info.AdTemplateInfo;
import com.huawei.manager.mkt.info.FieldCheckInfo;
import com.huawei.manager.mkt.util.StringUtils;
import com.huawei.util.DBUtil.DBConnection;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-5]
 * @see  [相关类/方法]
 */
public class LandInfoTemplateCheck
{
    /**
     * <检查广告位ID字段>
     * @param dbConn        数据库链接
     * @param info        着陆链接信息
     * @param buffer      校验信息
     * @param dicMap      字典集合
     * @param userDeptType 用户属性
     * @return            广告位ID是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkAidField(DBConnection dbConn, AdTemplateInfo info, StringBuffer buffer,
        AdDicMapInfo dicMap, Integer userDeptType)
    {
        //入口参数验证
        if (null == info || null == buffer)
        {
            return false;
        }
        
        //广告位ID字符串
        String aidStr = info.getAidStr();
        
        //根据广告位信息信息进行查询aid
        if (null == aidStr || 0 == aidStr.trim().length())
        {
            ///活动名称、网站名称、频道、广告位、端口所属
            AdInfoTemplateCheck.checkMktNameField(info, dicMap.getMktMap(), buffer, userDeptType);
            
            AdInfoTemplateCheck.checkWebNameField(info, dicMap.getWebMap(), buffer, userDeptType);
            
            AdInfoTemplateCheck.checkChannelField(info, buffer);
            
            AdInfoTemplateCheck.checkAdPositionField(info, buffer);
            
            AdInfoTemplateCheck.checkPortField(info, dicMap.getPortMap(), buffer);
            
            Integer aid = AdInfoDao.getAidByInfo(dbConn, info);
            
            if (null == aid)
            {
                buffer.append("根据活动信息无法找到对应的广告位信息;");
                return false;
            }
            
            info.setAid(aid);
            info.setAidStr(aid.toString());
            return true;
        }
        
        boolean flag = StringUtils.checkIntegerValue(buffer, aidStr, "广告位ID");
        
        if (!flag)
        {
            return false;
        }
        
        Integer aid = Integer.valueOf(aidStr);
        info.setAid(aid);
        
        return true;
    }
    
    /**
     * <检查着陆链接是否合法>
     * @param info        着陆链接信息
     * @param buffer      校验信息
     * @return            是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkLandUrlField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数验证
        if (null == info || null == buffer)
        {
            return false;
        }
        
        String landUrl = info.getLandUrl();
        
        //长度判断
        FieldCheckInfo landUrlCheckInfo = StringUtils.checkField(landUrl, true, 200);
        if (null != landUrlCheckInfo)
        {
            buffer.append("着陆链接错误, " + landUrlCheckInfo.getDesc() + ";");
            return false;
        }
        
        //是否满足正则表达式要求
        boolean flag = StringUtils.isUrl(landUrl);
        if (!flag)
        {
            buffer.append("着陆链接格式不符合URL要求;");
            return false;
        }
        return true;
    }
    
    /**
     * <检查vmall着陆链接>
     * @param info        着陆链接信息
     * @param buffer      校验信息
     * @return            是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkVmallLandUrlField(AdTemplateInfo info, StringBuffer buffer)
    {
        //常规链接格式判断
        boolean flag = checkLandUrlField(info, buffer);
        
        if (!flag)
        {
            return false;
        }
        
        //sid格式判断
        String cidInfo = "cid=" + info.getCid();
        int index = info.getLandUrl().indexOf(cidInfo);
        if (-1 == index)
        {
            buffer.append("着陆链接中cid信息错误;");
            return false;
        }
        
        return true;
        
    }
    
    /**
     * <检查SID是否正确>
     * @param info       着陆链接信息
     * @param buffer     校验信息
     * @return           是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkSidField(AdTemplateInfo info, StringBuffer buffer)
    {
        if (null == info || null == buffer)
        {
            return false;
        }
        
        String sid = info.getSid();
        
        if(null == sid||"".equals(sid))
        {
        	return true;
        }
        
        
        //是否数字
        boolean flag = StringUtils.isValidNumber(sid);
        if (!flag)
        {
            buffer.append("SID格式错误,请输入数字");
            return false;
        }
        
        //判读长度
        FieldCheckInfo sidFieldCheckInfo = StringUtils.checkField(sid, false, 255);
        if (null != sidFieldCheckInfo)
        {
            buffer.append("SID检查错误, " + sidFieldCheckInfo.getDesc() + ";");
            return false;
        }
        
        return true;
    }
    
    /**
     * <检查cps名称>
     * @param info       着陆链接信息
     * @param buffer     校验信息
     * @return           是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkCpsNameField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == buffer)
        {
            return false;
        }
        
        //字段检查
        FieldCheckInfo cpsNameCheckInfo = StringUtils.checkField(info.getCpsName(), false, 50);
        if (null != cpsNameCheckInfo)
        {
            buffer.append("CPS提供商名称错误, " + cpsNameCheckInfo.getDesc() + ";");
            return false;
        }
        return true;
    }
    
    /**
     * <检查source是否合法>
     * @param info        着陆链接信息
     * @param buffer      校验信息
     * @return            是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkSourceField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数校验
        if (null == info || null == buffer)
        {
            return false;
        }
        
        //字段检查
        FieldCheckInfo sourceCheckInfo = StringUtils.checkField(info.getSource(), false, 50);
        if (null != sourceCheckInfo)
        {
            buffer.append("Source错误, " + sourceCheckInfo.getDesc() + ";");
            return false;
        }
        
        return true;
    }
    
    /**
     * <检查channelName是否合法>
     * @param info       着陆链接信息
     * @param buffer     校验信息
     * @return           是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkChannelNameField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数验证
        if (null == info || null == buffer)
        {
            return false;
        }
        
        //字段检查
        FieldCheckInfo channelNameCheckInfo = StringUtils.checkField(info.getLandChannelName(), false, 50);
        if (null != channelNameCheckInfo)
        {
            buffer.append("渠道名称错误, " + channelNameCheckInfo.getDesc() + ";");
        }
        
        return true;
    }
    
    /**
     * <检查channel是否合法>
     * @param info       着陆链接信息
     * @param buffer     校验信息
     * @return           是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkChannelField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数验证
        if (null == info || null == buffer)
        {
            return false;
        }
        
        //字段检查
        FieldCheckInfo channelCheckInfo = StringUtils.checkField(info.getLandChannel(), false, 50);
        if (null != channelCheckInfo)
        {
            buffer.append("channel错误, " + channelCheckInfo.getDesc() + ";");
        }
        
        return true;
    }
    
    /**
     * <检查cid字段是否合法>
     * @param info        着陆链接信息
     * @param buffer      校验信息
     * @return            是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkCidField(AdTemplateInfo info, StringBuffer buffer)
    {
        //参数检查
        if (null == info || null == buffer)
        {
            return false;
        }
        
        String cid = info.getCid();
        
        //是否为空
        if (null == cid)
        {
            buffer.append("CID为空格式错误;");
            return false;
        }
        
        //是否数字
        boolean flag = StringUtils.isValidNumber(cid);
        if (!flag)
        {
            buffer.append("CID格式错误,请输入数字;");
            return false;
        }
        
        //判读长度
        FieldCheckInfo sidFieldCheckInfo = StringUtils.checkField(cid, true, 255);
        if (null != sidFieldCheckInfo)
        {
            buffer.append("CID检查错误, " + sidFieldCheckInfo.getDesc() + ";");
            return false;
        }
        
        return true;
    }
    
    /**
     * <检查aid是否合法>
     * @param dbConn          数据链接
     * @param info          着陆链接
     * @param buffer        校验信息
     * @param account       账号信息
     * @param vmallFlag     是否是vmall
     * @param userDeptType  账号类型
     * @param adminFlag      是否是管理员
     * @return              是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkAidValid(DBConnection dbConn, AdTemplateInfo info, StringBuffer buffer, String account,
        boolean vmallFlag, Integer userDeptType, boolean adminFlag)
    {
        
        //检查广告位ID是否存在
        AdInfoEntity entity = LandInfoDao.getAdInfo(dbConn, info.getAid());
        
        if (null == entity)
        {
            buffer.append("此广告位ID不存在;");
            return false;
        }
        
        //检查广告位ID是否正确
        Integer state = entity.getState();
        if (!AdState.FOR_LAND.equals(state))
        {
            buffer.append("此广告位状态不是待录入着陆链接不可以录入信息;");
            return false;
        }
        
        //检查广告位ID有没有权限
        boolean roleFlag = LandInfoDao.canUpdateLandInfo(dbConn, info.getAid(), account, userDeptType, adminFlag);
        if (!roleFlag)
        {
            buffer.append("用户没有权限操作此广告位着陆连接;");
            return false;
        }
        
        return true;
        
    }
    
    /**
     * <检查着陆平台是否合法>
     * @param entity        广告位信息
     * @param buffer        校验信息
     * @param vmallFlag      是否是vmall
     * @return              是否合法
     * @see [类、类#方法、类#成员] 逻辑上都是废代码
     */
    public static UploadDealState checkPlatform(AdInfoEntity entity, StringBuffer buffer, int vmallFlag)
    {
        //若处理前已经发现错误
        if (0 != buffer.toString().trim().length() || null == entity)
        {
            return UploadDealState.DEAL;
        }
        
        String platform = entity.getPlatform();
        if (vmallFlag == 0 && !LandPlatform.VMALL.equals(platform))
        {
            buffer.append("此广告位的着陆链接不是VMALL不可以录入信息;");
            if (LandPlatform.HONOR.equals(platform) || LandPlatform.HUAWEI.equals(platform))
            {
                return UploadDealState.IGNOREHONOR;
            }
            else
            {
                return UploadDealState.IGNOREOTHER;
            }
        }
        else if (vmallFlag == 1 && !(LandPlatform.HONOR.equals(platform) || LandPlatform.HUAWEI.equals(platform)))
        {
            
            buffer.append("此广告位的着陆链接不是荣耀不可以录入信息;");
            if (LandPlatform.VMALL.equals(platform))
            {
                return UploadDealState.IGNOREVMALL;
            }
            else
            {
                return UploadDealState.IGNOREOTHER;
            }
        }
        else if (vmallFlag == 2)
        {
            if (LandPlatform.TIANMAO.equals(platform) || LandPlatform.JD.equals(platform)
                || LandPlatform.OTHER.equals(platform))
            {
                return UploadDealState.DEAL;
            }
            else
            {
                
                buffer.append("此广告位的着陆链接不是第三方不可以录入信息;");
                if (LandPlatform.VMALL.equals(platform))
                {
                    return UploadDealState.IGNOREVMALL;
                }
                else
                {
                    return UploadDealState.IGNOREHONOR;
                }
            }
        }
        
        if (vmallFlag != 0 && !(LandPlatform.HONOR.equals(platform) || LandPlatform.HUAWEI.equals(platform)
            || LandPlatform.TIANMAO.equals(platform) || LandPlatform.JD.equals(platform)
            || LandPlatform.OTHER.equals(platform)))
        {
            buffer.append("此广告位的着陆链接为非法或未定义录入信息;");
            return UploadDealState.IGNOREOTHER;
            
        }
        return UploadDealState.DEAL;
    }
    
}
