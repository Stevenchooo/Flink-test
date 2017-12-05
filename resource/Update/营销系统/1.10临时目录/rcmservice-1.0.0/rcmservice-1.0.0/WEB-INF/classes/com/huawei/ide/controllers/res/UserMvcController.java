package com.huawei.ide.controllers.res;

import java.util.ArrayList;
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
import com.alibaba.fastjson.JSON;
import com.huawei.ide.beans.res.user.Role;
import com.huawei.ide.beans.res.user.User;
import com.huawei.ide.services.res.user.RoleService;
import com.huawei.ide.services.res.user.UserService;


/*import com.huawei.manager.common.client.meta.AccountInfo;
import com.huawei.manager.common.client.meta.QueryResponse;
import java.util.HashMap;
import java.util.Map;
import com.huawei.ide.portal.CommonUtils;*/

/**
 * 
 * UserMvcController
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see  [相关类/方法]
 */
@Controller
@RequestMapping("/res")
public class UserMvcController
{
    
    /**
     * 密钥第二段
     */
    public static final String KEY_2 = "DxsDeeD";
    
   /* *//**
     * 密钥第一段
     *//*
    private static final String KEY_1 = "database.pass.key.1";*/
    
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserMvcController.class);
    
    private UserService userService;
    
    private RoleService roleService;
    
    @Autowired
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
    
    @Autowired
    public void setRoleService(RoleService roleService)
    {
        this.roleService = roleService;
    }
    
    /**
     * 进入用户管理首页
     * <功能详细描述>
     * @return  ModelAndView
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping("/user_manager")
    public ModelAndView userManager()
    {
       /* try
        {
            List<AccountInfo> accountList = getUmUserList();
            updateUsers(accountList);
        }
        catch (DataAccessException e)
        {
            LOGGER.info("update user failed!");
        }*/
        ModelAndView mv = new ModelAndView();
        mv.setViewName("rule_engine_system/user_manager/user_manager");
        return mv;
        
    }
    
    /**
     * 查询profiles总数:/res/angularjs/user/queryUserTotalNum
     * <功能详细描述>
     * @return  ResponseEntity<Integer>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/user/queryUserTotalNum", method = RequestMethod.POST)
    public ResponseEntity<Integer> queryUserTotalNum()
    {
        Integer number = userService.queryUserTotalNum();
        return new ResponseEntity<Integer>(number, HttpStatus.OK);
    }
    
    /**
     * 查询所有user：/res/angularjs/user/queryAllUsers
     * <功能详细描述>
     * @return   ResponseEntity<List<User>>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/user/queryAllUsers", method = RequestMethod.POST)
    public ResponseEntity<List<User>> queryAllUsers()
    {
        List<User> userList = new ArrayList<User>();
        try
        {
            userList = userService.queryUserList();
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<List<User>>(userList, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }
    
    /**
     * 分页查询user： /res/angularjs/user/queryUsersByPage
     * <功能详细描述>
     * @param index
     *        index
     * @param pageSize
     *        pageSize
     * @return  ResponseEntity<List<User>>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/user/queryUsersByPage", method = RequestMethod.POST)
    public ResponseEntity<List<User>> queryUsersByPage(@RequestParam("index")
    Integer index, @RequestParam("pageSize")
    Integer pageSize)
    {
        List<User> userList = new ArrayList<User>();
        try
        {
            userList = userService.querUsersByPage(index, pageSize);
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<List<User>>(userList, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }
    
    /**
     * 获取所有角色： /res/angularjs/user/queryAllRoles
     * <功能详细描述>
     * @return  ResponseEntity<List<Role>>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/user/queryAllRoles", method = RequestMethod.POST)
    public ResponseEntity<List<Role>> queryAllRoles()
    {
        List<Role> roleList = new ArrayList<Role>();
        try
        {
            roleList = roleService.queryAllRoles();
        }
        catch (DataAccessException e)
        {
            return new ResponseEntity<List<Role>>(roleList, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Role>>(roleList, HttpStatus.OK);
    }
    
    /**
     * 更新用户： /res/angularjs/user/updateUser
     * <功能详细描述>
     * @param userJson
     *         userJson
     * @return   ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/user/updateUser", method = RequestMethod.POST)
    public ResponseEntity<Object> updateUser(@RequestParam("user")
    String userJson)
    {
        try
        {
            User user = JSON.parseObject(userJson, User.class);
            userService.updateUser(user);
        }
        catch (DataAccessException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * <获取cas服务器的用户列表>
     * 
     * @return 服务器用户列表
     * @see [类、类#方法、类#成员]
     *//*
    private List<AccountInfo> getUmUserList()
    {
        
        com.huawei.manager.common.client.User user =
            new com.huawei.manager.common.client.User(CommonUtils.getUmServerUrl(), CommonUtils.getUmAppId(),
                CommonUtils.decryptForAESCBCStr(CommonUtils.getUmKey(), CommonUtils.getSysConfig(KEY_1) + KEY_2));
        
        QueryResponse response = user.query(null, null, null);
        user.logout();
        if (null == response || 0 != response.getResultCode())
        {
            LOGGER.warn("getUmUserList failed, response = " + response);
            return null;
        }
        return response.getResult();
        
    }
    
    private void updateUsers(List<AccountInfo> accountInfoList)
    {
        List<User> localUserList = userService.queryUserList();
        Map<String, User> localUserMap = new HashMap<String, User>();
        if (null != localUserList)
        {
            for (User user : localUserList)
            {
                localUserMap.put(user.getName(), user);
            }
        }
        
        if (null != accountInfoList && !accountInfoList.isEmpty())
        {
            for (AccountInfo account : accountInfoList)
            {
                if (!localUserMap.containsKey(account.getAccount()))
                {
                    User user = createUser(account);
                    userService.createUser(user);
                }
            }
        }
        
        Map<String, AccountInfo> portalUserMap = new HashMap<String, AccountInfo>();
        if (null != accountInfoList)
        {
            for (AccountInfo accountInfo : accountInfoList)
            {
                portalUserMap.put(accountInfo.getAccount(), accountInfo);
            }
        }
        
        if (null != localUserList && !localUserList.isEmpty())
        {
            for (User localUser : localUserList)
            {
                if (!portalUserMap.containsKey(localUser.getName()))
                {
                    userService.deleteUser(localUser.getId());
                }
            }
        }
        
    }
    
    private User createUser(AccountInfo account)
    {
        User user = new User();
        user.setName(account.getAccount());
        return user;
    }*/
    
}
