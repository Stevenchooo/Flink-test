package com.huawei.ide.services.rcm;

import com.huawei.ide.interceptors.res.rcm.RcmForItemListReq;
import com.huawei.ide.interceptors.res.rcm.RcmForItemListRsp;
import com.huawei.ide.interceptors.res.rcm.RcmForThemeUserReq;
import com.huawei.ide.interceptors.res.rcm.RcmForThemeUserRsp;

/**
 * 
 * 规则引擎服务类
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see  [相关类/方法]
 */
public interface IRuleEngineService
{
    /**
     * 编译业务规则
     * <功能详细描述>
     * @param appId
     *        appId
     * @see [类、类#方法、类#成员]
     */
    public void buildBusinessRule(String appId);
    
    /**
     * 执行业务规则
     * <功能详细描述>
     * @param req
     *        req
     * @return  RcmForThemeUserRsp
     * @see [类、类#方法、类#成员]
     */
    public RcmForThemeUserRsp executeBusinessRule(RcmForThemeUserReq req);
    
    /**
     * 执行业务规则
     * <功能详细描述>
     * @param req
     *        req
     * @return   RcmForItemListRsp
     * @see [类、类#方法、类#成员]
     */
    public RcmForItemListRsp executeBusinessRule(RcmForItemListReq req);
}
