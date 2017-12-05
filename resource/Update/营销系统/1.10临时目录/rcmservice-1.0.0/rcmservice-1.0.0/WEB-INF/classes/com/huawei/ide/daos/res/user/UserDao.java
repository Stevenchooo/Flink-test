package com.huawei.ide.daos.res.user;

import java.util.List;

import com.huawei.ide.beans.res.user.User;

/**
 * 
 * 用户对象数据库操作接口类
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月15日]
 * @see  [相关类/方法]
 */
public interface UserDao
{
    /**
     * 添加一个用户
     * <功能详细描述>
     * @param user
     *        user
     * @see [类、类#方法、类#成员]
     */
    public void createUser(User user);
    
    /**
     * 查询所有用户
     * <功能详细描述>
     * @return List<User>
     * @see [类、类#方法、类#成员]
     */
    public List<User> queryUserList();
    
    /**
     * 查询用户总数
     * <功能详细描述>
     * @return   int
     * @see [类、类#方法、类#成员]
     */
    public int queryUserTotalNum();
    
    /**
     * 分页查询用户
     * <功能详细描述>
     * @param index
     *        index
     * @param pageSize
     *        pageSize
     * @return List<User>
     * @see [类、类#方法、类#成员]
     */
    public List<User> queryUsersByPage(int index, int pageSize);
    
    /**
     * 更新用户
     * <功能详细描述>
     * @param user
     *         user
     * @see [类、类#方法、类#成员]
     */
    public void updateUser(User user);
    
    /**
     * 删除本地用户
     * <功能详细描述>
     * @param id
     *        id
     * @see [类、类#方法、类#成员]
     */
    public void deleteUser(int id);
}
