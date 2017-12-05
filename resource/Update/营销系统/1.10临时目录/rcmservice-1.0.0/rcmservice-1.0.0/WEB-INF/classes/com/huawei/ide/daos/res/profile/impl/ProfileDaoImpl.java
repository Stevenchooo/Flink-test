/*
 * 文 件 名:  ProfileDaoImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.res.profile.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.huawei.ide.beans.res.profile.Profile;
import com.huawei.ide.daos.res.profile.ProfileDao;

/**
 * 系统用户对象数据库操作实现类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Repository(value = "com.huawei.ide.daos.res.profile.impl.ProfileDaoImpl")
public class ProfileDaoImpl extends JdbcDaoSupport implements ProfileDao
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
     * ProfileRowMapper
     * <功能详细描述>
     * 
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
     * @see  [相关类/方法]
     */
    private class ProfileRowMapper implements RowMapper<Profile>
    {
        @Override
        public Profile mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            Profile profile = new Profile();
            profile.setId(resultSet.getInt("id"));
            profile.setUuid(resultSet.getString("uuid"));
            profile.setName(resultSet.getString("name"));
            profile.setPasswd(resultSet.getString("passwd"));
            profile.setCategory(resultSet.getInt("category"));
            return profile;
        }
    }
    
    /**
     * 新建用户
     * @param profile
     *        profile
     */
    @Override
    public void createProfile(Profile profile)
    {
        String sql = "INSERT INTO T_PROFILE(uuid, name, passwd, category) VALUES(?, ?, ?, ?)";
        this.getJdbcTemplate().update(sql,
            new Object[] {profile.getUuid(), profile.getName(), profile.getPasswd(), profile.getCategory()});
    }
    
    /**
     * 更新用户
     * @param id
     *        id
     * @param profile
     *        profile
     */
    @Override
    public void updateProfile(int id, Profile profile)
    {
        String sql = "UPDATE T_PROFILE SET uuid=?,name=?,passwd=?,category=? WHERE id=?";
        this.getJdbcTemplate().update(sql,
            new Object[] {profile.getUuid(), profile.getName(), profile.getPasswd(), profile.getCategory(), id});
    }
    
    /**
     * 删除用户
     * @param id
     *        id
     */
    @Override
    public void deleteProfile(int id)
    {
        String sql = "DELETE FROM T_PROFILE WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
    /**
     * 查询用户
     * @param id
     *        id
     * @return Profile
     */
    @Override
    public Profile queryProfile(int id)
    {
        String sql = "SELECT * FROM T_PROFILE WHERE id=?";
        Profile profile = this.getJdbcTemplate().queryForObject(sql, new Object[] {id}, new ProfileRowMapper());
        return profile != null ? profile : null;
    }
    
    /**
     * 查询所有用户
     * @return  List<Profile>
     */
    @Override
    public List<Profile> queryProfiles()
    {
        String sql = "SELECT * FROM T_PROFILE ORDER BY ID";
        List<Profile> profiles = this.getJdbcTemplate().query(sql, new ProfileRowMapper());
        return (profiles != null && !profiles.isEmpty()) ? profiles : null;
    }
    
    /**
     * 查询用户总数
     * @return   int
     */
    @Override
    public int queryProfileTotalNum()
    {
        String sql = "SELECT COUNT(*) FROM T_PROFILE";
        return this.getJdbcTemplate().queryForObject(sql, Integer.class);
    }
    
    /**
     * 分页查询用户
     * @param index
     *        index
     * @param pageSize
     *         pageSize
     * @return   List<Profile>
     */
    @Override
    public List<Profile> queryProfilesByPage(int index, int pageSize)
    {
        String sql = "SELECT * FROM T_PROFILE ORDER BY ID LIMIT ?,?";
        List<Profile> profiles =
            this.getJdbcTemplate().query(sql, new Object[] {index, pageSize}, new ProfileRowMapper());
        return (profiles != null && !profiles.isEmpty()) ? profiles : null;
    }
    
}
