package com.huawei.ide.daos.res.monitor.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.huawei.ide.beans.res.monitor.Effect;
import com.huawei.ide.daos.res.monitor.MonitorDao;
import com.mysql.jdbc.Statement;

/**
 * 监控数据操作类
 * @author zWX301264
 *
 */
@Repository(value = "com.huawei.ide.daos.res.monitor.impl.MonitorDaoImpl")
public class MonitorDaoImpl extends JdbcDaoSupport implements MonitorDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorDaoImpl.class);
    
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
    private class MonitorRowMapper implements RowMapper<Effect>
    {
        
        @Override
        public Effect mapRow(ResultSet rs, int rowNum)
            throws SQLException
        {
            Effect effect = new Effect();
            effect.setId(rs.getInt("id"));
            effect.setName(rs.getString("name"));
            effect.setUrl(rs.getString("url"));
            return effect;
        }
        
    }
    
    /**
     * 查询所有url
     * @return List<Effect> List<Effect>
     */
    @Override
    public List<Effect> queryAllEffect()
    {
        String sql = "SELECT * FROM T_URL ORDER BY ID";
        List<Effect> effects = this.getJdbcTemplate().query(sql, new MonitorRowMapper());
        return (effects != null && !effects.isEmpty()) ? effects : null;
    }
    
    /**
     * 创建url对象
     * @param effect url对象
     * @return 对象id
     */
    @Override
    public int createEffect(final Effect effect)
    {
        final String sql = "INSERT INTO T_URL(NAME,URL) VALUES(?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator creator = new PreparedStatementCreator()
        {
            private PreparedStatement ps = null;
            
           /* @Override
            protected void finalize()
                throws Throwable
            {
                try
                {
                    if (null != ps)
                    {
                        ps.close();
                    }
                }
                finally
                {
                    super.finalize();
                }
            }*/
            
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException
            {
                if (null == connection || connection.isClosed())
                {
                    return null;
                }
               /* try
                {*/
                    ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, effect.getName());
                  ps.setString(2, effect.getUrl());
                  /* }
                catch (SQLException e)
                {
                    if (null != ps)
                    {
                        try
                        {
                            ps.close();
                        }
                        catch (SQLException e1)
                        {
                            LOGGER.error(e1.getMessage(), e1);
                        }
                    }
                    throw e;
                }*/
                return ps;
            }
        };
        this.getJdbcTemplate().update(creator, keyHolder);
        return keyHolder.getKey().intValue();
    }
    
    /**
     * 更新url对象
     * @param effect url对象
     */
    @Override
    public void updateEffect(Effect effect)
    {
        String sql = "UPDATE T_URL SET NAME=?,URL=? WHERE ID=?";
        this.getJdbcTemplate().update(sql, new Object[] {effect.getName(), effect.getUrl(), effect.getId()});
    }
    
    /**
     * 删除url对象
     * @param effectId  url的id
     */
    @Override
    public void deleteEffect(int effectId)
    {
        String sql = "DELETE FROM T_URL WHERE ID =?";
        this.getJdbcTemplate().update(sql, new Object[] {effectId});
    }
    
}
