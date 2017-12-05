package com.huawei.ide.services.res.user;

import java.util.List;

import com.huawei.ide.beans.res.user.Role;

/**
 * 
 * 角色对象数据库操作服务接口
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月15日]
 * @see  [相关类/方法]
 */
public interface RoleService
{
    /**
     * 查询所有角色
     * <功能详细描述>
     * @return  List<Role>
     * @see [类、类#方法、类#成员]
     */
    public List<Role> queryAllRoles();
}
