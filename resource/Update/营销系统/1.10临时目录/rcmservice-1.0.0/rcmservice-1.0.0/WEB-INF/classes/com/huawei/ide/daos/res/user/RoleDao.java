package com.huawei.ide.daos.res.user;

import java.util.List;

import com.huawei.ide.beans.res.user.Role;

/**
 * 
 * 角色对象数据库操作类
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月14日]
 * @see  [相关类/方法]
 */
public interface RoleDao
{
    /**
     * 查询所有角色
     * <功能详细描述>
     * @return   List<Role>
     * @see [类、类#方法、类#成员]
     */
    public List<Role> queryAllRoles();
}
