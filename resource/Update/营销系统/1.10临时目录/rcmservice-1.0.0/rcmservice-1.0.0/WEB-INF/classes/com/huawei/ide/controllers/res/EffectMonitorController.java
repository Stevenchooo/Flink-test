package com.huawei.ide.controllers.res;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

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
import com.huawei.ide.beans.res.monitor.Effect;
import com.huawei.ide.beans.res.monitor.EffectPackage;
import com.huawei.ide.beans.res.monitor.EffectPackageRelation;
import com.huawei.ide.services.res.monitor.MonitorService;

/**
 * 效果监控控制器类
 * @author zWX301264
 *
 */
@Controller
@RequestMapping("/res")
public class EffectMonitorController
{
    
    private MonitorService monitorService;
    
    @Autowired
    public void setMonitorService(MonitorService monitorService)
    {
        this.monitorService = monitorService;
    }
    
    /**
     * 返回页面
     * @return ModelAndView
     */
    @RequestMapping("/effect_monitor")
    public ModelAndView effectMonitor()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("rule_engine_system/effect_monitor/effect_monitor");
        return mv;
    }
    
    /**
     * 返回类
     * @author zWX301264
     *
     */
    private class EffectPackageRelationRtn
    {
        private String packageId;
        
        private String packageName;
        
        private String category;
        
        private List<EffectPackageRelation> effectPackageRelations;
        
        @SuppressWarnings("unused")
        public String getPackageId()
        {
            return packageId;
        }
        
        public void setPackageId(String packageId)
        {
            this.packageId = packageId;
        }
        
        @SuppressWarnings("unused")
        public String getPackageName()
        {
            return packageName;
        }
        
        public void setPackageName(String packageName)
        {
            this.packageName = packageName;
        }
        
        @SuppressWarnings("unused")
        public List<EffectPackageRelation> getEffectPackageRelations()
        {
            return effectPackageRelations;
        }
        
        public void setEffectPackageRelations(List<EffectPackageRelation> effectPackageRelations)
        {
            this.effectPackageRelations = effectPackageRelations;
        }
        
        @SuppressWarnings("unused")
        public String getCategory()
        {
            return category;
        }
        
        public void setCategory(String category)
        {
            this.category = category;
        }
        
    }
    
    /**
     * 查询所有package relation:/res/monitor/queryPackageByType
     * @return ResponseEntity<List<EffectPackageRelationRtn>>
     */
    @RequestMapping(value = "/monitor/queryPackageByType", method = RequestMethod.POST)
    public ResponseEntity<List<EffectPackageRelationRtn>> queryPackageByType()
    {
        List<EffectPackageRelationRtn> result = new ArrayList<EffectPackageRelationRtn>();
        try
        {
            List<EffectPackage> packages = monitorService.queryAllPackages();
            if (null != packages)
            {
                for (EffectPackage ep : packages)
                {
                    List<EffectPackageRelation> effectPackageRelations =
                        monitorService.queryEffectPackageRelationByPkgId(ep.getId());
                    EffectPackageRelationRtn rtn = new EffectPackageRelationRtn();
                    rtn.setPackageId(String.valueOf(ep.getId()));
                    rtn.setPackageName(ep.getName());
                    rtn.setCategory(ep.getCategory());
                    rtn.setEffectPackageRelations(effectPackageRelations);
                    result.add(rtn);
                }
            }
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<List<EffectPackageRelationRtn>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<EffectPackageRelationRtn>>(result, HttpStatus.OK);
    }
    
    /**
     * 新建package:/res/monitor/newPackage
     * @param pkg pkg
     * @param category category
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/monitor/newPackage", method = RequestMethod.POST)
    public ResponseEntity<Object> newPackage(@RequestParam("package") String pkg,
        @RequestParam("category") String category)
    {
        try
        {
            EffectPackage effectPackage = JSON.parseObject(pkg, EffectPackage.class);
            monitorService.createPkg(effectPackage.getName(), category);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 更新package:/res/monitor/updatePackage
     * @param pkg pkg
     * @param pkgId pkgId
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/monitor/updatePackage", method = RequestMethod.POST)
    public ResponseEntity<Object> updatePackage(@RequestParam("package") String pkg,
        @RequestParam("pkg_id") String pkgId)
    {
        try
        {
            String pkg1 = JSON.parseObject(pkg, String.class);
            monitorService.updatePkg(pkgId, pkg1);
            monitorService.updatePkgRelation(pkgId, pkg1);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除package:/res/monitor/deletePackage
     * @param pkgId pkgId
     * @return ResponseEntity<Object
     */
    @RequestMapping(value = "/monitor/deletePackage", method = RequestMethod.POST)
    public ResponseEntity<Object> deletePackage(@RequestParam("packageId") String pkgId)
    {
        try
        {
            List<EffectPackageRelation> effectPackageRelations =
                monitorService.queryEffectPackageRelationByPkgId(Integer.parseInt(pkgId));
            monitorService.deletePkgRelation(pkgId);
            monitorService.deletePkg(pkgId);
            if (null != effectPackageRelations)
            {
                for (EffectPackageRelation effectPackageRelation : effectPackageRelations)
                {
                    monitorService.deleteEffect(effectPackageRelation.getUrlId());
                }
            }
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    private Boolean query(String url)
    {
        URL urlTemp;
        HttpsURLConnection connts = null;
        HttpURLConnection connt = null;
        try
        {
            urlTemp = new URL(url);
            if (url.indexOf("https") != -1)
            {
                connts = (HttpsURLConnection)urlTemp.openConnection();
                connts.connect();
                connts.getLocalCertificates();
                //Map<String, List<String>> map = connts.getHeaderFields();
                String strMessage = connts.getResponseMessage();
                if (null != strMessage && strMessage.compareTo("Not Found") == 0)
                {
                    return false;
                }
                
            }
            else
            {
                connt = (HttpURLConnection)urlTemp.openConnection();
                connt.setRequestMethod("HEAD");
                String strMessage = connt.getResponseMessage();
                //Map<String, List<String>> map = connt.getHeaderFields();
                if (null != strMessage && strMessage.compareTo("Not Found") == 0)
                {
                    return false;
                }
                
            }
            
        }
        catch (IOException e)
        {
            return false;
        }
        finally
        {
            if (null != connts)
            {
                connts.disconnect();
            }
            if (null != connt)
            {
                connt.disconnect();
            }
        }
        return true;
        
    }
    
    /**
     * 查询所有url对象:/res/monitor/queryAllEffect
     * @return ResponseEntity<List<Map<String, Object>>>
     */
    @RequestMapping(value = "/monitor/queryAllEffect", method = RequestMethod.POST)
    public ResponseEntity<List<Map<String, Object>>> queryAllEffect()
    {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<Effect> rtn = null;
        try
        {
            rtn = monitorService.queryAllEffect();
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<List<Map<String, Object>>>(mapList, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (null != rtn)
        {
            for (Effect ef : rtn)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("url", ef);
                map.put("avilable", query(ef.getUrl()));
                mapList.add(map);
            }
        }
        return new ResponseEntity<List<Map<String, Object>>>(mapList, HttpStatus.OK);
    }
    
    /**
     * 新建url对象:/res/monitor/newEffect
     * @param effectStr effectStr
     * @param pkgId pkgId
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/monitor/newEffect", method = RequestMethod.POST)
    public ResponseEntity<Object> newEffect(@RequestParam("new_effect") String effectStr,
        @RequestParam("pkg_id") String pkgId)
    {
        try
        {
            Effect effect = JSON.parseObject(effectStr, Effect.class);
            EffectPackage ep = monitorService.queryPkgById(pkgId);
            int id = monitorService.createEffect(effect);
            EffectPackageRelation epr = new EffectPackageRelation();
            epr.setPackageId(Integer.parseInt(pkgId));
            epr.setPackageName(ep.getName());
            epr.setType(ep.getCategory());
            epr.setUrlId(id);
            epr.setUrl(effect.getUrl());
            epr.setUrlName(effect.getName());
            monitorService.createEffectRelation(epr);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 更新url对象:/res/monitor/updateEffect
     * @param effectStr effectStr
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/monitor/updateEffect", method = RequestMethod.POST)
    public ResponseEntity<Object> updateEffect(@RequestParam("update_effect") String effectStr)
    {
        try
        {
            Effect effect = JSON.parseObject(effectStr, Effect.class);
            monitorService.updateEffectPackageRelationByUrlId(effect);
            monitorService.updateEffect(effect);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除url对象:/res/monitor/deleteEffect
     * @param effectId effectId
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/monitor/deleteEffect", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteEffect(@RequestParam("effectId") String effectId)
    {
        try
        {
            int effectId1 = Integer.parseInt(effectId);
            monitorService.deleteEffectPackageRelationByUrlId(effectId1);
            monitorService.deleteEffect(effectId1);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
}
