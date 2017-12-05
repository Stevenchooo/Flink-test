package com.huawei.ide.daos.res.user.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.huawei.ide.beans.res.user.Role;
import com.huawei.ide.daos.res.user.RoleDao;

/**
 * 
 * 角色对象数据库操作类
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月15日]
 * @see  [相关类/方法]
 */
@Repository(value = "com.huawei.ide.daos.res.user.impl.RoleDaoImpl")
public class RoleDaoImpl extends JdbcDaoSupport implements RoleDao
{
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * 初始化
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    @PostConstruct
    public void initialize()
    {
        setDataSource(dataSource);
    }
    
    /**
     * 
     * RoleRowMapper
     * <功能详细描述>
     * 
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
     * @see  [相关类/方法]
     */
    private class RoleRowMapper implements RowMapper<Role>
    {
        
        @Override
        public Role mapRow(ResultSet rs, int rowNum)
            throws SQLException
        {
            Role role = new Role();
            role.setId(rs.getInt("id"));
            role.setName(rs.getString("name"));
            return role;
        }
        
    }
    
    /**
     * 查询所有角色
     * @return  List<Role>
     */
    @Override
    public List<Role> queryAllRoles()
    {
        String sql = "SELECT * FROM T_ROLE ORDER BY ID";
        List<Role> roleList = this.getJdbcTemplate().query(sql, new RoleRowMapper());
        return (null != roleList && !roleList.isEmpty()) ? roleList : null;
    }
    
}
