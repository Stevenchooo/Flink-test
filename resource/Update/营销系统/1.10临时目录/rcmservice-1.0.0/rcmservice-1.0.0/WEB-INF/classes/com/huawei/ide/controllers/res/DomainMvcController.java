/*
 * 文 件 名:  DomainMvcController.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2016年2月4日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.controllers.res;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.huawei.ide.beans.res.domain.Domain;
import com.huawei.ide.beans.res.domain.DomainPackage;
import com.huawei.ide.beans.res.domain.DomainPackageRelation;
import com.huawei.ide.beans.res.domain.DomainProperty;
import com.huawei.ide.services.res.domain.DomainService;
import com.huawei.ide.services.res.scenario.ScenarioService;

/**
 * RuleEngineSystem Mvc Controller for Domain Module
 * 
 * @author z00219375
 * @version [版本号, 2016年2月4日]
 * @see [相关类/方法]
 * @since [Consumer Cloud Big Data Platform Dept]
 */
@Controller
@RequestMapping("/res")
public class DomainMvcController
{
    private DomainService domainService;
    
    private ScenarioService scenarioService;
    
    @Autowired
    public void setDomainService(DomainService domainService)
    {
        this.domainService = domainService;
    }
    
    @Autowired
    public void setScenarioService(ScenarioService scenarioService)
    {
        this.scenarioService = scenarioService;
    }
    
    /**
     * 返回页面
     * @return ModelAndView
     */
    @RequestMapping("/domain_manager")
    public ModelAndView domainManager()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("rule_engine_system/domain_manager/domain_manager");
        return mv;
    }
    
    /**
     * 处理请求:/res/ajaxJquery/domain/queryDomains
     * @param body body
     * @return ResponseEntity<List<Domain>>
     */
    @RequestMapping(value = "/ajaxJquery/domain/queryDomains")
    public ResponseEntity<List<Domain>> ajaxJqueryQueryDomains(@RequestBody String body)
    {
        List<Domain> domains = new ArrayList<Domain>();
        try
        {
            domains = domainService.queryAllDomains();
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<List<Domain>>(domains, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Domain>>(domains, HttpStatus.OK);
    }
    
    /**
     * 新建domain:/res/angularjs/domain/newDomain
     * @param name name
     * @param published published
     * @return ResponseEntity<Domain>
     */
    @RequestMapping(value = "/angularjs/domain/newDomain", method = RequestMethod.POST)
    public ResponseEntity<Domain> newDomain(@RequestParam("name") String name,
        @RequestParam("published") String published)
    {
        Domain domain = new Domain();
        domain.setName(name);
        domain.setPublished(published);
        try
        {
            domainService.createDomain(domain);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Domain>(domain, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Domain>(domain, HttpStatus.OK);
    }
    
    /**
     * 更新domain:/res/angularjs/domain/updateDomain
     * @param domainStr domainStr
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/angularjs/domain/updateDomain", method = RequestMethod.POST)
    public ResponseEntity<Object> updateDomain(@RequestParam("domain") String domainStr)
    {
        try
        {
            Domain domain = JSON.parseObject(domainStr, Domain.class);
            int domainId = domain.getId();
            List<DomainPackageRelation> domainPackageRelations =
                domainService.queryDomainPackageRelationsByDomainId(domainId);
            if (null != domainPackageRelations)
            {
                for (DomainPackageRelation domainPackageRelation : domainPackageRelations)
                {
                    domainPackageRelation.setDomainName(domain.getName());
                    domainPackageRelation.setDomainPublished(domain.getPublished());
                    domainService.updateDomainPackageRelation(domainPackageRelation.getId(), domainPackageRelation);
                }
            }
            domainService.updateDomain(domainId, domain);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JSONException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除domain:/res/angularjs/domain/deleteDomain
     * @param id id
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/angularjs/domain/deleteDomain", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteDomain(@RequestParam("id") String id)
    {
        try
        {
            int domainId = Integer.parseInt(id);
            domainService.deleteDomainPropertyByDomainId(domainId);
            domainService.deleteDomainPackageRelationByDomainId(domainId);
            domainService.deleteDomain(domainId);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除domains:/res/angularjs/domain/deleteDomains
     * @param ids ids
     * @return  ResponseEntity<List<Integer>>
     */
    @RequestMapping(value = "/angularjs/domain/deleteDomains", method = RequestMethod.POST)
    public ResponseEntity<List<Integer>> deleteDomains(@RequestParam("ids") Object[] ids)
    {
        List<Integer> listRtn = new ArrayList<Integer>();
        for (Object obj : ids)
        {
            try
            {
                int domainId = Integer.parseInt(obj.toString());
                // TODO 删除关联外键的表 T_SCENARIO_DOMAIN_PROPERTY
                domainService.deleteDomainPropertyByDomainId(domainId);
                domainService.deleteDomainPackageRelationByDomainId(domainId);
                domainService.deleteDomain(domainId);
                
            }
            catch (DataAccessException e)
            {
                return new ResponseEntity<List<Integer>>(new ArrayList<Integer>(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            catch (NumberFormatException e)
            {
                return new ResponseEntity<List<Integer>>(new ArrayList<Integer>(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<List<Integer>>(listRtn, HttpStatus.OK);
    }
    
    /**
     * 是否被业务场景使用:/res/angularjs/domain/domainUsed
     * @param id id
     * @return ResponseEntity<Boolean>
     */
    @RequestMapping(value = "/angularjs/domain/domainUsed", method = RequestMethod.POST)
    public ResponseEntity<Boolean> domainUsed(@RequestParam("id") String id)
    {
        int domainId;
        boolean rtn = false;
        try
        {
            domainId = Integer.parseInt(id);
            
            rtn = scenarioService.queryScenarioDomainProperty(domainId);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Boolean>(rtn, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            return new ResponseEntity<Boolean>(rtn, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Boolean>(rtn, HttpStatus.OK);
    }
    
    /**
     * 查询domains总数:/res/angularjs/domain/queryDomainTotalNum
     * @return ResponseEntity<Integer>
     */
    @RequestMapping(value = "/angularjs/domain/queryDomainTotalNum", method = RequestMethod.POST)
    public ResponseEntity<Integer> queryDomainTotalNum()
    {
        Integer integer = null;
        try
        {
            integer = domainService.queryDomainTotalNum();
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Integer>(integer, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(integer, HttpStatus.OK);
        
    }
    
    /**
     * 查询所有的domains:/res/angularjs/domain/queryAllDomains
     * @return ResponseEntity<List<Domain>>
     */
    @RequestMapping(value = "/angularjs/domain/queryAllDomains", method = RequestMethod.POST)
    public ResponseEntity<List<Domain>> queryDomains()
    {
        List<Domain> domains = new ArrayList<Domain>();
        try
        {
            domains = domainService.queryAllDomains();
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<List<Domain>>(domains, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Domain>>(domains, HttpStatus.OK);
    }
    
    /**
     * 查询指定页的domains:/res/angularjs/domain/queryDomainsByPage
     * @param index index
     * @param pageSize pageSize
     * @return ResponseEntity<List<Domain>>
     */
    @RequestMapping(value = "/angularjs/domain/queryDomainsByPage", method = RequestMethod.POST)
    public ResponseEntity<List<Domain>> queryDomainsByPage(@RequestParam("index") Integer index,
        @RequestParam("pageSize") Integer pageSize)
    {
        List<Domain> domains = new ArrayList<Domain>();
        try
        {
            domains = domainService.queryDomainsByPage(index, pageSize);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<List<Domain>>(domains, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Domain>>(domains, HttpStatus.OK);
    }
    
    /**
     * 保存domainPropertys:/res/angularjs/domain/newDomainPropertys
     * @param domainId domainId
     * @param domainPropertysStr domainPropertysStr
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/angularjs/domain/newDomainPropertys", method = RequestMethod.POST)
    public ResponseEntity<Object> newDomainPropertys(@RequestParam("domainId") String domainId,
        @RequestParam("domainPropertys") String domainPropertysStr)
    {
        try
        {
            int domainIdTemp = Integer.parseInt(domainId);
            domainService.deleteDomainPropertyByDomainId(domainIdTemp);
            List<DomainProperty> domainProperties = JSON.parseArray(domainPropertysStr, DomainProperty.class);
            if (null != domainProperties)
            {
                for (DomainProperty domainProperty : domainProperties)
                {
                    domainProperty.setDomainId(domainIdTemp);
                    domainService.createDomainProperty(domainProperty);
                }
            }
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 查询所有的domainPropertys:/res/angularjs/domain/queryDomainPropertysByDomainId
     * @param domainId domainId
     * @return ResponseEntity<List<DomainProperty>>
     */
    @RequestMapping(value = "/angularjs/domain/queryDomainPropertysByDomainId", method = RequestMethod.POST)
    public ResponseEntity<List<DomainProperty>> queryDomainPropertysByDomainId(
        @RequestParam("domainId") String domainId)
    {
        List<DomainProperty> domainPropertys = new ArrayList<DomainProperty>();
        try
        {
            domainPropertys = domainService.queryDomainPropertysByDomainId(Integer.parseInt(domainId));
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<List<DomainProperty>>(domainPropertys, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<DomainProperty>>(domainPropertys, HttpStatus.OK);
    }
    
    /**
     * 新建Domain Package:/res/angularjs/domain/newDomainPackage
     * @param packageStr packageStr
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/angularjs/domain/newDomainPackage", method = RequestMethod.POST)
    public ResponseEntity<Object> newDomainPackage(@RequestParam("package") String packageStr)
    {
        try
        {
            DomainPackage domainPackage = JSON.parseObject(packageStr, DomainPackage.class);
            domainService.createDomainPackage(domainPackage);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JSONException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     *  更新Domain Package:/res/angularjs/domain/updateDomainPackage
     * @param packageStr packageStr
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/angularjs/domain/updateDomainPackage", method = RequestMethod.POST)
    public ResponseEntity<Object> updateDomainPackage(@RequestParam("package") String packageStr)
    {
        try
        {
            DomainPackage domainPackage = JSON.parseObject(packageStr, DomainPackage.class);
            domainService.updateDomainPackage(domainPackage.getId(), domainPackage);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JSONException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     *  删除Domain Package:/res/angularjs/domain/deleteDomainPackage
     * @param packageIdStr packageIdStr
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/angularjs/domain/deleteDomainPackage", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteDomainPackage(@RequestParam("packageId") String packageIdStr)
    {
        try
        {
            int packageId = Integer.parseInt(packageIdStr);
            domainService.deleteDomainPackageRelationByPackageId(packageId);
            domainService.deleteDomainPackage(packageId);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 新建Domain Package Relation:/res/angularjs/domain/newDomainPackageRelation
     * @param domainpackageStr domainpackageStr
     * @param domainStr domainStr
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/angularjs/domain/newDomainPackageRelation", method = RequestMethod.POST)
    public ResponseEntity<Object> newDomainPackageRelation(@RequestParam("domainPackage") String domainpackageStr,
        @RequestParam("domain") String domainStr)
    {
        try
        {
            DomainPackage domainPackage = JSON.parseObject(domainpackageStr, DomainPackage.class);
            // Domain domain = JSON.parseObject(domainStr, Domain.class);
            Domain domain = domainService.queryDomain(Integer.parseInt(domainStr));
            DomainPackageRelation domainPackageRelation = new DomainPackageRelation();
            domainPackageRelation.setPackageId(domainPackage.getId());
            domainPackageRelation.setPackageName(domainPackage.getName());
            if (null != domain)
            {
                domainPackageRelation.setDomainId(domain.getId());
                domainPackageRelation.setDomainName(domain.getName());
                domainPackageRelation.setDomainPublished(domain.getPublished());
            }
            domainService.createDomainPackageRelation(domainPackageRelation);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JSONException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 更新Domain Package
     * @param domainPackageRelationStr  domainPackageRelationStr
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/angularjs/domain/updateDomainPackageRelation", method = RequestMethod.POST)
    public ResponseEntity<Object> updateDomainPackageRelation(
        @RequestParam("domainPackageRelation") String domainPackageRelationStr)
    {
        try
        {
            DomainPackageRelation domainPackageRelation =
                JSON.parseObject(domainPackageRelationStr, DomainPackageRelation.class);
            
            Domain domain = domainService.queryDomain(domainPackageRelation.getDomainId());
            if (null != domain)
            {
                domainPackageRelation.setDomainName(domain.getName());
                domainPackageRelation.setDomainPublished(domain.getPublished());
            }
            domainService.updateDomainPackageRelation(domainPackageRelation.getId(), domainPackageRelation);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除Domain Package
     * @param domainPackageRelationIdStr domainPackageRelationIdStr
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/angularjs/domain/deleteDomainPackageRelation", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteDomainPackageRelation(
        @RequestParam("domainPackageRelationId") String domainPackageRelationIdStr)
    {
        try
        {
            Integer domainPackageRelationId = Integer.parseInt(domainPackageRelationIdStr);
            domainService.deleteDomainPackageRelation(domainPackageRelationId);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 结果类
     * @author zWX301264
     *
     */
    class DomainPackageRelationResult
    {
        private String domainPackageId;
        
        private String domainPackageName;
        
        private List<DomainPackageRelation> domainPackageRelations;
        
        public String getDomainPackageId()
        {
            return domainPackageId;
        }
        
        public void setDomainPackageId(String domainPackageId)
        {
            this.domainPackageId = domainPackageId;
        }
        
        public String getDomainPackageName()
        {
            return domainPackageName;
        }
        
        public void setDomainPackageName(String domainPackageName)
        {
            this.domainPackageName = domainPackageName;
        }
        
        public List<DomainPackageRelation> getDomainPackageRelations()
        {
            return domainPackageRelations;
        }
        
        public void setDomainPackageRelations(List<DomainPackageRelation> domainPackageRelations)
        {
            this.domainPackageRelations = domainPackageRelations;
        }
    }
    
    /**
     * 查询明细的domainPackages:/res/angularjs/domain/queryRefreshDomainPackages
     * @return ResponseEntity<List<DomainPackageRelationResult>>
     */
    @RequestMapping(value = "/angularjs/domain/queryRefreshDomainPackages", method = RequestMethod.POST)
    public ResponseEntity<List<DomainPackageRelationResult>> queryRefreshDomainPackages()
    {
        List<DomainPackageRelationResult> results = new ArrayList<DomainPackageRelationResult>();
        try
        {
            List<DomainPackage> domainPackages = domainService.queryAllDomainPackages();
            if (null != domainPackages)
            {
                for (DomainPackage domainPackage : domainPackages)
                {
                    List<DomainPackageRelation> domainPackageRelations =
                        domainService.queryDomainPackageRelationsByPackageId(domainPackage.getId());
                    DomainPackageRelationResult result = new DomainPackageRelationResult();
                    result.setDomainPackageId(String.valueOf(domainPackage.getId()));
                    result.setDomainPackageName(domainPackage.getName());
                    result.setDomainPackageRelations(domainPackageRelations);
                    results.add(result);
                }
            }
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<List<DomainPackageRelationResult>>(results, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<DomainPackageRelationResult>>(results, HttpStatus.OK);
    }
    
}
