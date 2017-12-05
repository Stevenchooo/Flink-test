/*
 * 文 件 名:  ConfigMvcController.java
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.huawei.ide.beans.res.config.Config;
import com.huawei.ide.services.res.config.ConfigService;

/**
 * RuleEngineSystem Mvc Controller for Config Module
 * @author  z00219375
 * @version  [版本号, 2016年2月4日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Controller
@RequestMapping("/res")
public class ConfigMvcController
{
    private ConfigService configService;
    
    @Autowired
    public void setConfigService(ConfigService configService)
    {
        this.configService = configService;
    }
    
    /**
     * 返回页面
     * @return ModelAndView
     */
    @RequestMapping("/system_manager")
    public ModelAndView systemManager()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("rule_engine_system/system_manager/system_manager");
        return mv;
    }
    
    /**
     * 新建config:/res/angularjs/system/newConfig
     * @param name name
     * @param val val
     * @param defaultVal defaultVal
     * @param category category
     * @param levelStr levelStr
     * @param comment comment
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/angularjs/system/newConfig", method = RequestMethod.POST)
    public ResponseEntity<Object> newConfig(@RequestParam("name") String name, @RequestParam("val") String val,
        @RequestParam("default_val") String defaultVal, @RequestParam("category") String category,
        @RequestParam("level") String levelStr, @RequestParam("comment") String comment)
    {
        try
        {
            Config config = new Config();
            config.setName(name);
            config.setVal(val);
            config.setDefaultVal(defaultVal);
            config.setCategory(category);
            config.setLevel(Integer.parseInt(levelStr));
            config.setComment(comment);
            configService.createConfig(config);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 更新config:/res/angularjs/system/updateConfig
     * @param configStr configStr
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/angularjs/system/updateConfig", method = RequestMethod.POST)
    public ResponseEntity<Object> updateConfig(@RequestParam("config") String configStr)
    {
        try
        {
            Config config = JSON.parseObject(configStr, Config.class);
            configService.updateConfig(config.getId(), config);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除config:/res/angularjs/system/deleteConfig
     * @param id id
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/angularjs/system/deleteConfig", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteConfig(@RequestParam("id") String id)
    {
        try
        {
            int configId = Integer.parseInt(id);
            configService.deleteConfig(configId);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除configs:/res/angularjs/system/deleteConfigs
     * @param ids ids
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/angularjs/system/deleteConfigs", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteConfigs(@RequestParam("ids") Object[] ids)
    {
        try
        {
            for (Object obj : ids)
            {
                int i = Integer.parseInt(obj.toString());
                configService.deleteConfig(i);
            }
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 查询configs总数:/res/angularjs/system/queryConfigTotalNum
     * @return ResponseEntity<Integer>
     */
    @RequestMapping(value = "/angularjs/system/queryConfigTotalNum", method = RequestMethod.POST)
    public ResponseEntity<Integer> queryConfigTotalNum()
    {
        Integer integer = null;
        try
        {
            integer = configService.queryConfigTotalNum();
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Integer>(integer, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(integer, HttpStatus.OK);
    }
    
    /**
     * 查询所有的configs:/res/angularjs/system/queryAllConfigs
     * @return ResponseEntity<List<Config>>
     */
    @RequestMapping(value = "/angularjs/system/queryAllConfigs", method = RequestMethod.POST)
    public ResponseEntity<List<Config>> queryConfigs()
    {
        List<Config> configs = new ArrayList<Config>();
        try
        {
            configs = configService.queryConfigs();
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<List<Config>>(configs, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Config>>(configs, HttpStatus.OK);
    }
    
    /**
     * 查询指定页的configs:/res/angularjs/system/queryConfigsByPage
     * @param index index
     * @param pageSize pageSize
     * @return  ResponseEntity<List<Config>>
     */
    @RequestMapping(value = "/angularjs/system/queryConfigsByPage", method = RequestMethod.POST)
    public ResponseEntity<List<Config>> queryConfigsByPage(@RequestParam("index") Integer index,
        @RequestParam("pageSize") Integer pageSize)
    {
        List<Config> configs = new ArrayList<Config>();
        try
        {
            configs = configService.queryConfigsByPage(index, pageSize);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<List<Config>>(configs, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Config>>(configs, HttpStatus.OK);
    }
    
}
