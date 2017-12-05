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

import com.huawei.ide.beans.res.user.User;
import com.huawei.ide.daos.res.user.UserDao;

/**
 * 
 * 用户对数据库操作类
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月15日]
 * @see  [相关类/方法]
 */
@Repository(value = "com.huawei.ide.daos.res.user.impl.UserDaoImpl")
public class UserDaoImpl extends JdbcDaoSupport implements UserDao
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
     * UserRowMapper
     * <功能详细描述>
     * 
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
     * @see  [相关类/方法]
     */
    private class UserRowMapper implements RowMapper<User>
    {
        
        @Override
        public User mapRow(ResultSet rs, int rowNum)
            throws SQLException
        {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setRoleId(rs.getInt("role_id"));
            return user;
        }
        
    }
    
    /**
     * 新建用户
     * @param user
     *        user
     */
    @Override
    public void createUser(User user)
    {
        String sql = "INSERT INTO T_USER(NAME) VALUES(?)";
        this.getJdbcTemplate().update(sql, new Object[] {user.getName()});
    }
    
    /**
     * 查询用户
     * @return List<User>
     */
    @Override
    public List<User> queryUserList()
    {
        String sql = "SELECT * FROM T_USER ORDER BY ID";
        List<User> userList = this.getJdbcTemplate().query(sql, new UserRowMapper());
        return (null != userList && !userList.isEmpty()) ? userList : null;
    }
    
    /**
     * 查询用户总数
     * @return  int
     */
    @Override
    public int queryUserTotalNum()
    {
        String sql = "SELECT COUNT(*) FROM T_USER";
        return this.getJdbcTemplate().queryForObject(sql, Integer.class);
    }
    
    /**
     * 分页查询用户
     * @param index
     *        index
     * @param pageSize
     *        pageSize
     * @return  List<User>
     */
    @Override
    public List<User> queryUsersByPage(int index, int pageSize)
    {
        String sql = "SELECT * FROM T_USER ORDER BY ID LIMIT ?,?";
        List<User> userList = this.getJdbcTemplate().query(sql, new Object[] {index, pageSize}, new UserRowMapper());
        return (null != userList && !userList.isEmpty()) ? userList : null;
    }
    
    /**
     * 更新用户
     * @param user
     *        user
     */
    @Override
    public void updateUser(User user)
    {
        String sql = "UPDATE T_USER SET name = ?,role_id = ? WHERE id = ?";
        this.getJdbcTemplate().update(sql, new Object[] {user.getName(), user.getRoleId(), user.getId()});
    }
    
    /**
     * 删除用户
     * @param id
     *         id
     */
    @Override
    public void deleteUser(int id)
    {
        String sql = "DELETE FROM T_USER WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
}
