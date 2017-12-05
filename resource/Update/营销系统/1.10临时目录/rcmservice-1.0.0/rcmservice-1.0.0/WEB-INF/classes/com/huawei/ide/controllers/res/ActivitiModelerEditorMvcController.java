/*
 * 文 件 名:  ActivitiModelerEditorMvcController.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2016年2月4日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.controllers.res;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.huawei.core.commons.CommonException;
import com.huawei.ide.beans.res.business.Business;
import com.huawei.ide.beans.res.business.BusinessPackageRelation;
import com.huawei.ide.beans.res.business.BusinessRule;
import com.huawei.ide.services.rcm.IRecommendService;
import com.huawei.ide.services.res.business.BusinessService;

/**
 * RuleEngineSystem Mvc Controller for Activiti Modeler Editor
 * @author  z00219375
 * @version  [版本号, 2016年2月4日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Controller
public class ActivitiModelerEditorMvcController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiModelerEditorMvcController.class);
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private IRecommendService recommendService;
    
    /**
     * 空白页
     * <功能详细描述>
     * @return  ModelAndView
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping("/")
    public ModelAndView blank()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("blank/blank");
        return mv;
    }
    
    /**
     * 规则引擎编辑页
     * <功能详细描述>
     * @return ModelAndView
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping("/activiti_modeler_editor")
    public ModelAndView domainManager()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("rule_engine_system/activiti/activiti_modeler_editor");
        return mv;
    }
    
    /**
     * callActivitiModel
     * <功能详细描述>
     * @param businessId
     *        businessId
     * @return   ResponseEntity<String>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/activiti/callActivitiModel", method = RequestMethod.POST)
    public ResponseEntity<String> callActivitiModel(@RequestParam("business_id") String businessId)
    {
        String modelId = null;
        try
        {
            List<BusinessRule> businessRules =
                businessService.queryBusinessRulesByBusinessId(Integer.parseInt(businessId));
            if (null == businessRules || businessRules.isEmpty())
            {
                return new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            BusinessRule businessRule = businessRules.get(0);
            modelId = businessRule.getBpmModelId();
        }
        catch (DataAccessException e)
        {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(modelId, HttpStatus.OK);
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param businessIdStr
     *        businessIdStr
     * @return ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/activiti/executeActivitiModel", method = RequestMethod.POST)
    public ResponseEntity<Object> executeActivitiModel(@RequestParam("business_id") String businessIdStr)
    {
        try
        {
            List<BusinessRule> businessRules =
                businessService.queryBusinessRulesByBusinessId(Integer.parseInt(businessIdStr));
            if (null == businessRules || businessRules.isEmpty())
            {
                throw new CommonException("No such business rule with the business_id='" + businessIdStr + "'.");
            }
            int businessIdTemp = businessRules.get(0).getBusinessId();
            Business business = businessService.queryBusiness(businessIdTemp);
            if (null == business)
            {
                throw new CommonException("No such business with the business_id='" + businessIdTemp + "'.");
            }
            recommendService.buildRecommendRule(business.getName());
            business.setPublished("true");
            int businessId = business.getId();
            List<BusinessPackageRelation> businessPackageRelations =
                businessService.queryBusinessPackageRelationsByBusinessId(businessId);
            if (null != businessPackageRelations)
            {
                for (BusinessPackageRelation businessPackageRelation : businessPackageRelations)
                {
                    businessPackageRelation.setBusinessName(business.getName());
                    
                    businessPackageRelation.setBusinessPublished(business.getPublished());
                    
                    businessService.updateBusinessPackageRelation(businessPackageRelation.getId(),
                        businessPackageRelation);
                }
            }
            businessService.updateBusiness(businessIdTemp, business);
        }
        catch (CommonException e)
        {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
}
