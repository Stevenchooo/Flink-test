package com.huawei.ide.services.res.user.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.huawei.ide.beans.res.user.Role;
import com.huawei.ide.daos.res.user.RoleDao;
import com.huawei.ide.services.res.user.RoleService;

/**
 * 
 * 角色对象数据库操作服务实现类
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月15日]
 * @see  [相关类/方法]
 */
@Repository(value = "com.huawei.ide.services.res.user.impl.RoleServiceImpl")
public class RoleServiceImpl implements RoleService
{
    
    @Resource(name = "com.huawei.ide.daos.res.user.impl.RoleDaoImpl")
    private RoleDao roleDao;
    
    /**
     * 查询所有角色
     * @return  List<Role>
     */
    @Override
    public List<Role> queryAllRoles()
    {
        return roleDao.queryAllRoles();
    }
    
}
