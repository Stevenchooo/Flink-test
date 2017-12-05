package com.huawei.ide.services.rcm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huawei.core.beans.ApplicationContextHelper;
import com.huawei.ide.beans.res.business.Business;
import com.huawei.ide.commons.RuleEngineRuntimeConfigUtil;
import com.huawei.ide.interceptors.res.rcm.RcmForItemListReq;
import com.huawei.ide.interceptors.res.rcm.RcmForItemListRsp;
import com.huawei.ide.interceptors.res.rcm.RcmForThemeUserReq;
import com.huawei.ide.interceptors.res.rcm.RcmForThemeUserRsp;
import com.huawei.ide.services.res.business.BusinessService;

/**
 * 推荐服务类
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
 * @see  [相关类/方法]
 */
@Service(value = "com.huawei.ide.services.rcm.RecommendService")
public class RecommendService implements IRecommendService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendService.class);
    
    @Autowired
    private BusinessService businessService;
    
    private volatile Map<String, IRuleEngineService> ruleEngineServiceMap;
    
    /**
     * 初始化
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    @PostConstruct
    public void init()
    {
        ruleEngineServiceMap = new HashMap<String, IRuleEngineService>();
        LOGGER.info("Business Rules start init publish...");
        businessRulePublish();
        LOGGER.info("Business Rules init publish end.");
    }
    
    private void businessRulePublish()
    {
        ruleEngineServiceMap.clear();
        List<Business> businesses = businessService.queryAllBusinesses();
        if (null == businesses || businesses.isEmpty())
        {
            LOGGER.info("Business rules of the database records is empty.");
            return;
        }
        List<String> configBusinessRules = RuleEngineRuntimeConfigUtil.getBusinessRulesToPublish();
        for (Business business : businesses)
        {
            String published = business.getPublished();
            if (Boolean.parseBoolean(published))
            {
                String businessName = business.getName();
                if (configBusinessRules.isEmpty())
                {
                    buildRecommendRule(businessName);
                }
                else if (configBusinessRules.contains(businessName))
                {
                    buildRecommendRule(businessName);
                }
            }
        }
    }
    
    /**
     * buildRecommendRule
     * @param businessName
     *       businessName
     */
    @Override
    public synchronized void buildRecommendRule(String businessName)
    {
        LOGGER.info("Business Rule '" + businessName + "' start build...");
        try
        {
            if (ruleEngineServiceMap.containsKey(businessName))
            {
                LOGGER.info("Business Rule '" + businessName + "' republish build start...");
                ruleEngineServiceMap.get(businessName).buildBusinessRule(businessName);
                LOGGER.info("Business Rule '" + businessName + "' republish build end.");
            }
            else
            {
                IRuleEngineService ruleEngineService =
                    ApplicationContextHelper.getBean("com.huawei.ide.services.rcm.RuleEngineService");
                if (null == ruleEngineService)
                {
                    LOGGER.error(
                        "The bean of 'com.huawei.ide.services.rcm.RuleEngineService' cann't obtain from beans, please check the spring framework if start correctly.");
                    return;
                }
                LOGGER.info("Business Rule '" + businessName + "' first publish build start...");
                ruleEngineService.buildBusinessRule(businessName);
                LOGGER.info("Business Rule '" + businessName + "' first publish build end.");
                ruleEngineServiceMap.put(businessName, ruleEngineService);
            }
        }
        catch (ClassCastException e)
        {
            LOGGER.error("Business Rule '" + businessName + "' build error with the message '" + e.getMessage() + "'.");
        }
        LOGGER.info("Business Rule '" + businessName + "' build end.");
    }
    
    /**
     * executeRecommendService
     * @param req
     *       req
     * @return  RcmForThemeUserRsp
     */
    @Override
    public RcmForThemeUserRsp executeRecommendService(RcmForThemeUserReq req)
    {
        IRuleEngineService ruleEngineService = ruleEngineServiceMap.get(req.getAppKey());
        if (null == ruleEngineService)
        {
            LOGGER.error("No Such Business Rule '" + req.getAppKey()
                + "' within the rule engine service, please check the business rule config.");
            return null;
        }
        return ruleEngineService.executeBusinessRule(req);
    }
    
    /**
     * executeRecommendService
     * @param req
     *         req
     * @return  RcmForItemListRsp
     */
    @Override
    public RcmForItemListRsp executeRecommendService(RcmForItemListReq req)
    {
        IRuleEngineService ruleEngineService = ruleEngineServiceMap.get(req.getAppKey());
        if (null == ruleEngineService)
        {
            LOGGER.error("No Such Business Rule '" + req.getAppKey()
                + "' within the rule engine service, please check the business rule config.");
            return null;
        }
        return ruleEngineService.executeBusinessRule(req);
    }
    
    /**
     * destory
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    @PreDestroy
    public void destory()
    {
        ruleEngineServiceMap.clear();
    }
    
}
