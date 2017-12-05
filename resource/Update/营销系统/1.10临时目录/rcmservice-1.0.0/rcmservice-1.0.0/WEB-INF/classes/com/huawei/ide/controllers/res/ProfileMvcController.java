/*
 * 文 件 名:  ProfileMvcController.java
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
import java.util.UUID;

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
import com.huawei.ide.beans.res.profile.Profile;
import com.huawei.ide.services.res.profile.ProfileService;

/**
 * RuleEngineSystem Mvc Controller for Profile Module
 * @author  z00219375
 * @version  [版本号, 2016年2月4日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Controller
@RequestMapping("/res")
public class ProfileMvcController
{
    private ProfileService profileService;
    
    @Autowired
    public void setProfileService(ProfileService profileService)
    {
        this.profileService = profileService;
    }
    
    /**
     * 进入用户管理首页
     * <功能详细描述>
     * @return  ModelAndView
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping("/profile_manager")
    public ModelAndView profileManager()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("rule_engine_system/profile_manager/profile_manager");
        return mv;
    }
    
    /**
     * 新建profile:/res/angularjs/profile/newProfile
     * <功能详细描述>
     * @param name
     *        name
     * @param passwd
     *        passwd
     * @param category
     *        category
     * @return   ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/profile/newProfile", method = RequestMethod.POST)
    public ResponseEntity<Object> newProfile(@RequestParam("name")
    String name, @RequestParam("passwd")
    String passwd, @RequestParam("category")
    String category)
    {
        Profile profile = new Profile();
        UUID uuid = UUID.randomUUID();
        profile.setUuid(uuid.toString());
        profile.setName(name);
        profile.setPasswd(passwd);
        profile.setCategory(Integer.parseInt(category));
        try
        {
            profileService.createProfile(profile);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 更新profile:/res/angularjs/profile/updateProfile
     * <功能详细描述>
     * @param profileStr
     *        profileStr
     * @return    ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/profile/updateProfile", method = RequestMethod.POST)
    public ResponseEntity<Object> updateProfile(@RequestParam("profile")
    String profileStr)
    {
        try
        {
            Profile profile = JSON.parseObject(profileStr, Profile.class);
            profileService.updateProfile(profile.getId(), profile);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除profile:/res/angularjs/profile/deleteProfile
     * <功能详细描述>
     * @param id
     *        id
     * @return  ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/profile/deleteProfile", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteProfile(@RequestParam("id")
    String id)
    {
        try
        {
            int profileId = Integer.parseInt(id);
            profileService.deleteProfile(profileId);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除profiles:/res/angularjs/profile/deleteProfiles
     * <功能详细描述>
     * @param ids
     *        ids
     * @return  ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/profile/deleteProfiles", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteProfiles(@RequestParam("ids")
    Object[] ids)
    {
        for (Object obj : ids)
        {
            int i = Integer.parseInt(obj.toString());
            try
            {
                profileService.deleteProfile(i);
            }
            catch (DataAccessException e)
            {
                return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 查询profiles总数:/res/angularjs/profile/queryProfileTotalNum
     * <功能详细描述>
     * @return  ResponseEntity<Integer>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/profile/queryProfileTotalNum", method = RequestMethod.POST)
    public ResponseEntity<Integer> queryProfileTotalNum()
    {
        Integer integer = profileService.queryProfileTotalNum();
        return new ResponseEntity<Integer>(integer, HttpStatus.OK);
    }
    
    /**
     * 查询所有的profiles:/res/angularjs/profile/queryAllProfiles
     * <功能详细描述>
     * @return   ResponseEntity<List<Profile>>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/profile/queryAllProfiles", method = RequestMethod.POST)
    public ResponseEntity<List<Profile>> queryAllProfiles()
    {
        List<Profile> profiles = profileService.queryProfiles();
        return new ResponseEntity<List<Profile>>(profiles, HttpStatus.OK);
    }
    
    /**
     * 查询指定页的profiles:/res/angularjs/profile/queryProfilesByPage
     * <功能详细描述>
     * @param index
     *        index
     * @param pageSize
     *         pageSize
     * @return   ResponseEntity<List<Profile>>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/profile/queryProfilesByPage", method = RequestMethod.POST)
    public ResponseEntity<List<Profile>> queryProfilesByPage(@RequestParam("index")
    Integer index, @RequestParam("pageSize")
    Integer pageSize)
    {
        List<Profile> profiles = profileService.queryProfilesByPage(index, pageSize);
        return new ResponseEntity<List<Profile>>(profiles, HttpStatus.OK);
    }
}
