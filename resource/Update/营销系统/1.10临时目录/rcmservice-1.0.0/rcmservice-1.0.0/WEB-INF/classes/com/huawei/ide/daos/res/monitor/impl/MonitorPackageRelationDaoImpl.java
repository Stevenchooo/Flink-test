package com.huawei.ide.daos.res.monitor.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.huawei.ide.beans.res.monitor.Effect;
import com.huawei.ide.beans.res.monitor.EffectPackageRelation;
import com.huawei.ide.daos.res.monitor.MonitorPackageRelationDao;

/**
 * 监控packagerelation数据库操作类
 * @author zWX301264
 *
 */
@Repository(value = "com.huawei.ide.daos.res.monitor.impl.MonitorPackageRelationDaoImpl")
public class MonitorPackageRelationDaoImpl extends JdbcDaoSupport implements MonitorPackageRelationDao
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
     * 返回结果类
     * @author zWX301264
     *
     */
    private class EffectPackageRelationRowMapper implements RowMapper<EffectPackageRelation>
    {
        
        @Override
        public EffectPackageRelation mapRow(ResultSet rs, int rowNum)
            throws SQLException
        {
            EffectPackageRelation effectPackageRelation = new EffectPackageRelation();
            effectPackageRelation.setId(rs.getInt("id"));
            effectPackageRelation.setPackageId(rs.getInt("package_id"));
            effectPackageRelation.setPackageName(rs.getString("package_name"));
            effectPackageRelation.setUrl(rs.getString("url"));
            effectPackageRelation.setUrlId(rs.getInt("url_id"));
            effectPackageRelation.setUrlName(rs.getString("url_name"));
            effectPackageRelation.setType(rs.getString("category"));
            return effectPackageRelation;
        }
        
    }
    
    /**
     * 查询所有package relation
     * 
     * @param id id
     * @return List<EffectPackageRelation>
     */
    @Override
    public List<EffectPackageRelation> queryEffectPackageRelationByPkgId(int id)
    {
        String sql = "SELECT * FROM T_URL_PACKAGE_RELATION WHERE package_id=?";
        List<EffectPackageRelation> effectPackageRelations =
            this.getJdbcTemplate().query(sql, new Object[] {id}, new EffectPackageRelationRowMapper());
        return (effectPackageRelations != null && !effectPackageRelations.isEmpty()) ? effectPackageRelations : null;
    }
    
    /**
     * 创建package relation
     * @param epr epr
     */
    @Override
    public void createEffectRelation(EffectPackageRelation epr)
    {
        String sql =
            "INSERT INTO T_URL_PACKAGE_RELATION(package_id,url_id,package_name,url_name,url,category) values(?,?,?,?,?,?)";
        
        this.getJdbcTemplate().update(sql,
            new Object[] {epr.getPackageId(), epr.getUrlId(), epr.getPackageId(), epr.getUrlName(), epr.getUrl(),
                epr.getType()});
    }
    
    /**
     * 更新package relation
     * @param pkgId pkgId
     * @param name name
     */
    @Override
    public void updatePkgRelation(String pkgId, String name)
    {
        String sql = "UPDATE T_URL_PACKAGE_RELATION SET package_name=? WHERE package_id=?";
        this.getJdbcTemplate().update(sql, new Object[] {name, pkgId});
    }
    
    /**
     * 删除package relation
     * 
     * @param pkgId pkgId
     */
    @Override
    public void deletePkgRelation(String pkgId)
    {
        String sql = "DELETE FROM T_URL_PACKAGE_RELATION WHERE package_id=?";
        this.getJdbcTemplate().update(sql, new Object[] {pkgId});
    }
    
    /**
     * 根据urlId更新package relation
     * 
     * @param effect effect
     */
    @Override
    public void updateEffectPackageRelationByUrlId(Effect effect)
    {
        String sql = "UPDATE T_URL_PACKAGE_RELATION SET url_name=?,url=? WHERE url_id=?";
        this.getJdbcTemplate().update(sql, new Object[] {effect.getName(), effect.getUrl(), effect.getId()});
    }
    
    /**
     * 根据urlId删除package relation
     * 
     * @param effectId effectId
     */
    @Override
    public void deleteEffectPackageRelationByUrlId(int effectId)
    {
        String sql = "DELETE FROM T_URL_PACKAGE_RELATION WHERE url_id=?";
        this.getJdbcTemplate().update(sql, new Object[] {effectId});
    }
    
}
