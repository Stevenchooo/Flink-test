package com.huawei.ide.services.rcm;

import com.huawei.ide.interceptors.res.rcm.RcmForItemListReq;
import com.huawei.ide.interceptors.res.rcm.RcmForItemListRsp;
import com.huawei.ide.interceptors.res.rcm.RcmForThemeUserReq;
import com.huawei.ide.interceptors.res.rcm.RcmForThemeUserRsp;

/**
 * 
 * 推荐服务接口类
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see  [相关类/方法]
 */
public interface IRecommendService
{
    /**
     * 根据业务构造推荐服务
     * <功能详细描述>
     * @param appId
     *         appId
     * @see [类、类#方法、类#成员]
     */
    public void buildRecommendRule(String appId);
    
    /**
     * 请求推荐服务返回推荐结果
     * <功能详细描述>
     * @param req
     *        req
     * @return  RcmForThemeUserRsp
     * @see [类、类#方法、类#成员]
     */
    public RcmForThemeUserRsp executeRecommendService(RcmForThemeUserReq req);
    
    /**
     * 请求推荐服务返回推荐结果
     * <功能详细描述>
     * @param req
     *        req
     * @return  RcmForItemListRsp
     * @see [类、类#方法、类#成员]
     */
    public RcmForItemListRsp executeRecommendService(RcmForItemListReq req);
}
