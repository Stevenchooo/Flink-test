/*
 * 文 件 名:  BusinessMvcController.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2016年2月4日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.controllers.res;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huawei.ide.beans.res.business.Business;
import com.huawei.ide.beans.res.business.BusinessPackage;
import com.huawei.ide.beans.res.business.BusinessPackageRelation;
import com.huawei.ide.beans.res.business.BusinessRule;
import com.huawei.ide.services.res.business.BusinessService;

/**
 * RuleEngineSystem Mvc Controller for Business Module
 * @author  z00219375
 * @version  [版本号, 2016年2月4日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Controller
@RequestMapping("/res")
public class BusinessMvcController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessMvcController.class);
    
    private static final String TRUE = "true";
    
    private static final Integer PACKAGE_NAME_MAX_LENGTH = 20;
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private RepositoryService repositoryService;
    
    /**
     * 进入业务规则管理页
     * <功能详细描述>
     * @return   ModelAndView
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping("/business_manager")
    public ModelAndView businessManager()
    {
        LOGGER.info("businessManager start..");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("rule_engine_system/business_manager/business_manager");
        LOGGER.info("businessManager end..");
        return mv;
    }
    
    /**
     * 新建Business Package:/res/angularjs/business/newBusinessPackage
     * <功能详细描述>
     * @param packageStr
     *        packageStr
     * @return    ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/business/newBusinessPackage", method = RequestMethod.POST)
    public ResponseEntity<Object> newBusinessPackage(@RequestParam("package") String packageStr)
    {
        LOGGER.info("newBusinessPackage start..");
        try
        {
            BusinessPackage businessPackage = JSON.parseObject(packageStr, BusinessPackage.class);
            String packageName = businessPackage.getName();
            if (packageName.length() > PACKAGE_NAME_MAX_LENGTH)
            {
                return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            businessService.createBusinessPackage(businessPackage);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("newBusinessPackage failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JSONException e)
        {
            LOGGER.error("newBusinessPackage failed: JSONException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("newBusinessPackage end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 更新Business Package:/res/angularjs/business/updateBusinessPackage
     * <功能详细描述>
     * @param packageStr
     *         packageStr
     * @return   ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/business/updateBusinessPackage", method = RequestMethod.POST)
    public ResponseEntity<Object> updateBusinessPackage(@RequestParam("package") String packageStr)
    {
        LOGGER.info("updateBusinessPackage start..");
        try
        {
            BusinessPackage businessPackage = JSON.parseObject(packageStr, BusinessPackage.class);
            int packageId = businessPackage.getId();
            String packageName = businessPackage.getName();
            if (packageName.length() > PACKAGE_NAME_MAX_LENGTH)
            {
                return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            businessService.updateBusinessPackage(packageId, businessPackage);
            List<BusinessPackageRelation> businessPackageRelations =
                businessService.queryBusinessPackageRelationsByPackageId(packageId);
            if (null != businessPackageRelations)
            {
                for (BusinessPackageRelation businessPackageRelation : businessPackageRelations)
                {
                    
                    businessPackageRelation.setPackageName(packageName);
                    businessService.updateBusinessPackageRelation(businessPackageRelation.getId(),
                        businessPackageRelation);
                }
            }
        }
        catch (DataAccessException e)
        {
            LOGGER.error("updateBusinessPackage failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JSONException e)
        {
            LOGGER.error("updateBusinessPackage failed: JSONException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("updateBusinessPackage end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除Business Package:/res/angularjs/business/deleteBusinessPackage
     * <功能详细描述>
     * @param packageIdStr
     *         packageIdStr
     * @return   ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/business/deleteBusinessPackage", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteBusinessPackage(@RequestParam("packageId") String packageIdStr)
    {
        LOGGER.info("deleteBusinessPackage start..");
        try
        {
            int packageId = Integer.parseInt(packageIdStr);
            List<BusinessPackageRelation> businessPackageRelations =
                businessService.queryBusinessPackageRelationsByPackageId(packageId);
            if (null != businessPackageRelations)
            {
                for (BusinessPackageRelation businessPackageRelation : businessPackageRelations)
                {
                    
                    int businessId = businessPackageRelation.getBusinessId();
                    List<BusinessRule> businessRules = businessService.queryBusinessRulesByBusinessId(businessId);
                    if (null != businessRules)
                    
                    {
                        for (BusinessRule businessRule : businessRules)
                        {
                            
                            repositoryService.deleteModel(businessRule.getBpmModelId());
                            
                        }
                    }
                    businessService.deleteBusinessRuleByBusinessId(businessId);
                    businessService.deleteBusiness(businessId);
                }
            }
            businessService.deleteBusinessPackageRelationByPackageId(packageId);
            businessService.deleteBusinessPackage(packageId);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("deleteBusinessPackage failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("deleteBusinessPackage failed: NumberFormatException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("deleteBusinessPackage end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 新建Business:/res/angularjs/business/newBusiness
     * <功能详细描述>
     * @param name
     *         name
     * @param published
     *         published
     * @return    ResponseEntity<Business>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/business/newBusiness", method = RequestMethod.POST)
    public ResponseEntity<Business> newBusiness(@RequestParam("name") String name,
        @RequestParam("published") String published)
    {
        LOGGER.info("newBusiness start..");
        Business business = new Business();
        if (TRUE.equals(published))
        {
            return new ResponseEntity<Business>(business, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        business.setName(name);
        business.setPublished(published);
        try
        {
            businessService.createBusiness(business);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("newBusiness failed: DataAccessException!");
            return new ResponseEntity<Business>(business, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("newBusiness end..");
        return new ResponseEntity<Business>(business, HttpStatus.OK);
    }
    
    /**
     * 更新Business:/res/angularjs/business/updateBusiness
     * <功能详细描述>
     * @param businessStr
     *        businessStr
     * @return   ResponseEntity<Object>  
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/business/updateBusiness", method = RequestMethod.POST)
    public ResponseEntity<Object> updateBusiness(@RequestParam("business") String businessStr)
    {
        LOGGER.info("updateBusiness start..");
        try
        {
            Business business = JSON.parseObject(businessStr, Business.class);
            if (TRUE.equals(business.getPublished()))
            {
                return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
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
            businessService.updateBusiness(businessId, business);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("updateBusiness failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JSONException e)
        {
            LOGGER.error("updateBusiness failed: JSONException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("updateBusiness end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除Business:/res/angularjs/business/deleteBusiness
     * <功能详细描述>
     * @param id
     *         id
     * @return   ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/business/deleteBusiness", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteBusiness(@RequestParam("id") String id)
    {
        LOGGER.info("deleteBusiness start..");
        try
        {
            //TODO 删除关联外键的表 T_SCENARIO_DOMAIN_PROPERTY
            int businessId = Integer.parseInt(id);
            Business business = businessService.queryBusiness(businessId);
            if (null != business)
            {
                if (TRUE.equals(business.getPublished()))
                {
                    return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            businessService.deleteBusinessRuleByBusinessId(businessId);
            businessService.deleteBusinessPackageRelationByBusinessId(businessId);
            businessService.deleteBusiness(businessId);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("deleteBusiness failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("deleteBusiness failed: NumberFormatException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("deleteBusiness end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    //@SuppressWarnings("deprecation")
    private String createActivitiModel(String name)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        Model modelData = repositoryService.newModel();
        ObjectNode modelObjectNode = objectMapper.createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        //description = StringUtils.defaultString(description);
        String description = "";
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelData.setMetaInfo(modelObjectNode.toString());
        modelData.setName(name);
        String key = "";
        modelData.setKey(StringUtils.defaultString(key));
        repositoryService.saveModel(modelData);
        try
        {
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        return modelData.getId();
    }
    
    /**
     * 新建Business Package Relation:/res/angularjs/business/newBusinessPackageRelation
     * <功能详细描述>
     * @param businessPackageStr
     *         businessPackageStr
     * @param businessStr
     *          businessStr
     * @return   ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/business/newBusinessPackageRelation", method = RequestMethod.POST)
    public ResponseEntity<Object> newBusinessPackageRelation(@RequestParam("businessPackage") String businessPackageStr,
        @RequestParam("business") String businessStr)
    {
        try
        {
            LOGGER.info("newBusinessPackageRelation start..");
            Business business = JSON.parseObject(businessStr, Business.class);
            // 新建business都是未发布状态
            business.setPublished("false");
            int businessId = businessService.createBusinessReturnId(business);
            BusinessPackage businessPackage = JSON.parseObject(businessPackageStr, BusinessPackage.class);
            BusinessPackageRelation businessPackageRelation = new BusinessPackageRelation();
            businessPackageRelation.setPackageId(businessPackage.getId());
            businessPackageRelation.setPackageName(businessPackage.getName());
            businessPackageRelation.setBusinessId(businessId);
            businessPackageRelation.setBusinessName(business.getName());
            businessPackageRelation.setBusinessPublished(business.getPublished());
            businessService.createBusinessPackageRelation(businessPackageRelation);
            String modelId = createActivitiModel(business.getName());
            BusinessRule businessRule = new BusinessRule();
            businessRule.setBusinessId(businessId);
            businessRule.setBpmModelId(modelId);
            businessRule.setBpmContent(new byte[] {});
            businessService.createBusinessRule(businessRule);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("newBusinessPackageRelation failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JSONException e)
        {
            LOGGER.error("newBusinessPackageRelation failed: JSONException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("newBusinessPackageRelation end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 更新Business Package Relation:/res/angularjs/business/updateBusinessPackageRelation
     * <功能详细描述>
     * @param businessPackageRelationStr
     *         businessPackageRelationStr
     * @return    ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/business/updateBusinessPackageRelation", method = RequestMethod.POST)
    public ResponseEntity<Object> updateBusinessPackageRelation(
        @RequestParam("businessPackageRelation") String businessPackageRelationStr)
    {
        try
        {
            LOGGER.info("updateBusinessPackageRelation start..");
            BusinessPackageRelation businessPackageRelation =
                JSON.parseObject(businessPackageRelationStr, BusinessPackageRelation.class);
            Business business = new Business();
            business.setId(businessPackageRelation.getBusinessId());
            business.setName(businessPackageRelation.getBusinessName());
            business.setPublished(businessPackageRelation.getBusinessPublished());
            businessService.updateBusiness(business.getId(), business);
            businessService.updateBusinessPackageRelation(businessPackageRelation.getId(), businessPackageRelation);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("updateBusinessPackageRelation failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JSONException e)
        {
            LOGGER.error("updateBusinessPackageRelation failed: JSONException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("updateBusinessPackageRelation end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除Business Package Relation:/res/angularjs/business/deleteBusinessPackageRelation
     * <功能详细描述>
     * @param businessPackageRelationIdStr
     *        businessPackageRelationIdStr
     * @return   ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/business/deleteBusinessPackageRelation", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteBusinessPackageRelation(
        @RequestParam("businessPackageRelationId") String businessPackageRelationIdStr)
    {
        try
        {
            LOGGER.info("deleteBusinessPackageRelation start..");
            Integer businessPackageRelationId = Integer.parseInt(businessPackageRelationIdStr);
            BusinessPackageRelation businessPackageRelation =
                businessService.queryBusinessPackageRelationById(businessPackageRelationId);
            if (null == businessPackageRelation)
            {
                return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            int businessId = businessPackageRelation.getBusinessId();
            List<BusinessRule> businessRules = businessService.queryBusinessRulesByBusinessId(businessId);
            if (null != businessRules)
            {
                for (BusinessRule businessRule : businessRules)
                {
                    repositoryService.deleteModel(businessRule.getBpmModelId());
                }
            }
            businessService.deleteBusinessRuleByBusinessId(businessId);
            businessService.deleteBusinessPackageRelation(businessPackageRelationId);
            businessService.deleteBusiness(businessId);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("deleteBusinessPackageRelation failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("deleteBusinessPackageRelation failed: NumberFormatException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("deleteBusinessPackageRelation end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 
     * BusinessPackageRelationResult
     * <功能详细描述>
     * 
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
     * @see  [相关类/方法]
     */
    class BusinessPackageRelationResult
    {
        private String businessPackageId;
        
        private String businessPackageName;
        
        private List<BusinessPackageRelation> businessPackageRelations;
        
        public String getBusinessPackageId()
        {
            return businessPackageId;
        }
        
        public void setBusinessPackageId(String businessPackageId)
        {
            this.businessPackageId = businessPackageId;
        }
        
        public String getBusinessPackageName()
        {
            return businessPackageName;
        }
        
        public void setBusinessPackageName(String businessPackageName)
        {
            this.businessPackageName = businessPackageName;
        }
        
        public List<BusinessPackageRelation> getBusinessPackageRelations()
        {
            return businessPackageRelations;
        }
        
        public void setBusinessPackageRelations(List<BusinessPackageRelation> businessPackageRelations)
        {
            this.businessPackageRelations = businessPackageRelations;
        }
    }
    
    /**
     * 查询明细的BusinessPackages:/res/angularjs/business/queryRefreshBusinessPackages
     * <功能详细描述>
     * @return  ResponseEntity<List<BusinessPackageRelationResult>>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/business/queryRefreshBusinessPackages", method = RequestMethod.POST)
    public ResponseEntity<List<BusinessPackageRelationResult>> queryRefreshBusinessPackages()
    {
        LOGGER.info("queryRefreshBusinessPackages start..");
        List<BusinessPackageRelationResult> results = new ArrayList<BusinessPackageRelationResult>();
        try
        {
            List<BusinessPackage> businessPackages = businessService.queryAllBusinessPackages();
            if (null != businessPackages)
            {
                for (BusinessPackage businessPackage : businessPackages)
                {
                    List<BusinessPackageRelation> businessPackageRelations =
                        businessService.queryBusinessPackageRelationsByPackageId(businessPackage.getId());
                    BusinessPackageRelationResult result = new BusinessPackageRelationResult();
                    result.setBusinessPackageId(String.valueOf(businessPackage.getId()));
                    result.setBusinessPackageName(businessPackage.getName());
                    result.setBusinessPackageRelations(businessPackageRelations);
                    results.add(result);
                }
            }
        }
        catch (DataAccessException e)
        {
            LOGGER.error("queryRefreshBusinessPackages failed: DataAccessException!");
            return new ResponseEntity<List<BusinessPackageRelationResult>>(results, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("queryRefreshBusinessPackages end..");
        return new ResponseEntity<List<BusinessPackageRelationResult>>(results, HttpStatus.OK);
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param businessIdStr
     *          businessIdStr
     * @return   ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/business/cancelPublish", method = RequestMethod.POST)
    public ResponseEntity<Object> cancelPublish(@RequestParam("businessId") String businessIdStr)
    {
        try
        {
            LOGGER.info("cancelPublish start..");
            Business business = businessService.queryBusiness(Integer.parseInt(businessIdStr));
            if (null == business)
            {
                return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (TRUE.equals(business.getPublished()))
            {
                business.setPublished("false");
            }
            else
            {
                return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
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
            businessService.updateBusiness(businessId, business);
        }
        
        catch (DataAccessException e)
        {
            LOGGER.error("cancelPublish failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("cancelPublish failed: NumberFormatException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("cancelPublish end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
        
    }
    
}
