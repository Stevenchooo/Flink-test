package com.huawei.ide.services.res.user.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.huawei.ide.beans.res.user.User;
import com.huawei.ide.daos.res.user.UserDao;
import com.huawei.ide.services.res.user.UserService;

/**
 * 
 * 用户数据库操作服务实现类
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月15日]
 * @see  [相关类/方法]
 */
@Repository(value = "com.huawei.ide.services.res.user.impl.UserServiceImpl")
public class UserServiceImpl implements UserService
{
    
    @Resource(name = "com.huawei.ide.daos.res.user.impl.UserDaoImpl")
    private UserDao userDao;
    
    /**
     * 新建用户
     * @param user
     *        user
     */
    @Override
    public void createUser(User user)
    {
        userDao.createUser(user);
    }
    
    /**
     * 查询用户
     * @return  List<User>
     */
    @Override
    public List<User> queryUserList()
    {
        return userDao.queryUserList();
    }
    
    /**
     * 查询用户总数
     * @return  int
     */
    @Override
    public int queryUserTotalNum()
    {
        return userDao.queryUserTotalNum();
    }
    
    /**
     * 分页查询用户
     * @param index
     *        index
     * @param pageSize
     *        pageSize
     * @return   pageSize
     */
    @Override
    public List<User> querUsersByPage(int index, int pageSize)
    {
        return userDao.queryUsersByPage(index, pageSize);
    }
    
    /**
     * 更新用户
     * @param user
     *        user
     */
    @Override
    public void updateUser(User user)
    {
        userDao.updateUser(user);
    }
    
    /**
     * 删除用户
     * @param id
     *        id
     */
    @Override
    public void deleteUser(int id)
    {
        userDao.deleteUser(id);
    }
    
}
