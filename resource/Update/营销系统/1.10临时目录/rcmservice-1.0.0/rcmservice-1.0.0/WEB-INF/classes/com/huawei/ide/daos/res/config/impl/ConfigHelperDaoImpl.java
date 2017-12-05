package com.huawei.ide.daos.res.config.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.huawei.ide.daos.res.config.ConfigHelperDao;

/**
 * 系统配置对象数据库操作类
 * 
 * @author zWX301264
 * 
 */
@Repository(value = "com.huawei.ide.daos.res.config.impl.ConfigHelperDaoImpl")
public class ConfigHelperDaoImpl extends JdbcDaoSupport implements ConfigHelperDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHelperDaoImpl.class);
    
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
     * 根据配置名称获取配置值，当存在相同配置名称时取级别高的配置值，level越小级别越高
     * 
     * @param configName
     *            configName
     * @return String String
     */
    @Override
    public String queryValueByName(String configName)
    {
        String sql = "SELECT VAL,DEFAULT_VAL FROM T_CONFIG WHERE NAME = ? ORDER BY LEVEL ASC";
        try
        {
            List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(sql, new Object[] {configName});
            if (list.isEmpty())
            {
                return null;
            }
            else
            {
                if ("".equals(list.get(0).get("val")))
                {
                    return (String)list.get(0).get("default_val");
                }
                else
                {
                    return (String)list.get(0).get("val");
                }
            }
            
        }
        catch (DataAccessException e)
        {
            LOGGER.error("", e);
            return null;
        }
    }
    
}
