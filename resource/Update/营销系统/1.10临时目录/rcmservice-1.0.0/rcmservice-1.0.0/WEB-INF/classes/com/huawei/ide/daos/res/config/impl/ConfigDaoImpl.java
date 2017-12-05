/*
 * 文 件 名:  ConfigDaoImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.res.config.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.huawei.ide.beans.res.config.Config;
import com.huawei.ide.daos.res.config.ConfigDao;

/**
 * 系统配置对象数据库操作实现类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Repository(value = "com.huawei.ide.daos.res.config.impl.ConfigDaoImpl")
public class ConfigDaoImpl extends JdbcDaoSupport implements ConfigDao
{
    @Autowired
    private DataSource dataSource;
    
    /**
     * 初始化
     */
    @PostConstruct
    public void initialize()
    {
        setDataSource(dataSource);
    }
    
    /**
     * 结果类
     * @author zWX301264
     *
     */
    private class ConfigRowMapper implements RowMapper<Config>
    {
        @Override
        public Config mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            Config config = new Config();
            config.setId(resultSet.getInt("id"));
            config.setName(resultSet.getString("name"));
            config.setVal(resultSet.getString("val"));
            config.setDefaultVal(resultSet.getString("default_val"));
            config.setCategory(resultSet.getString("category"));
            config.setLevel(resultSet.getInt("level"));
            config.setComment(resultSet.getString("comment"));
            return config;
        }
    }
    
    /**
     * 创建系统配置对象
     * @param config config
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void createConfig(Config config)
    {
        String sql = "INSERT INTO T_CONFIG(name, val, default_val, category, level, comment) VALUES(?, ?, ?, ?, ?, ?)";
        this.getJdbcTemplate().update(sql,
            new Object[] {config.getName(), config.getVal(), config.getDefaultVal(), config.getCategory(),
                config.getLevel(), config.getComment()});
    }
    
    /**
     * 更新系统配置对象
     * @param id id
     * @param config config
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void updateConfig(int id, Config config)
    {
        String sql = "UPDATE T_CONFIG SET name=?,val=?,default_val=?,category=?,level=?,comment=? WHERE id=?";
        this.getJdbcTemplate().update(sql,
            new Object[] {config.getName(), config.getVal(), config.getDefaultVal(), config.getCategory(),
                config.getLevel(), config.getComment(), id});
    }
    
    /**
     * 删除系统配置对象
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteConfig(int id)
    {
        String sql = "DELETE FROM T_CONFIG WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
    /**
     * 查询指定系统配置对象
     * @param id id
     * @return Config Config
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Config queryConfig(int id)
    {
        String sql = "SELECT * FROM T_CONFIG WHERE id=?";
        Config config = this.getJdbcTemplate().queryForObject(sql, new Object[] {id}, new ConfigRowMapper());
        return config != null ? config : null;
    }
    
    /**
     * 查询所有系统配置对象
     * @return List<Config> List<Config>
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Config> queryConfigs()
    {
        String sql = "SELECT * FROM T_CONFIG ORDER BY ID";
        List<Config> configs = this.getJdbcTemplate().query(sql, new ConfigRowMapper());
        return (configs != null && !configs.isEmpty()) ? configs : null;
    }
    
    /**
     * 查询config对象总数
     * @return int int
     * @see [类、类#方法、类#成员]
     */
    @Override
    public int queryConfigTotalNum()
    {
        String sql = "SELECT COUNT(*) FROM T_CONFIG";
        return this.getJdbcTemplate().queryForObject(sql, Integer.class);
    }
    
    /**
     * 分页查询指定config对象
     * @param index
     * 分页查询指定索引，从0开始
     * @param pageSize
     * 分页查询指定的页大小
     * @return List<Config> List<Config>
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Config> queryConfigsByPage(int index, int pageSize)
    {
        String sql = "SELECT * FROM T_CONFIG ORDER BY ID LIMIT ?,?";
        List<Config> configs = this.getJdbcTemplate().query(sql, new Object[] {index, pageSize}, new ConfigRowMapper());
        return (configs != null && !configs.isEmpty()) ? configs : null;
    }
    
}
