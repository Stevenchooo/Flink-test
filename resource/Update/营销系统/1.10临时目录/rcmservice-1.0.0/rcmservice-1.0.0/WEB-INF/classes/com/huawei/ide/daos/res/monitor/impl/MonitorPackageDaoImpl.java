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

import com.huawei.ide.beans.res.monitor.EffectPackage;
import com.huawei.ide.daos.res.monitor.MonitorPackageDao;
/**
 * 监控package数据操作类
 * @author zWX301264
 *
 */
@Repository(value = "com.huawei.ide.daos.res.monitor.impl.MonitorPackageDaoImpl")
public class MonitorPackageDaoImpl extends JdbcDaoSupport implements MonitorPackageDao
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
     * 查询结果类
     * @author zWX301264
     *
     */
    private class MonitorPackageRowMapper implements RowMapper<EffectPackage>
    {
        
        @Override
        public EffectPackage mapRow(ResultSet rs, int rowNum)
            throws SQLException
        {
            EffectPackage effectPackage = new EffectPackage();
            effectPackage.setId(rs.getInt("id"));
            effectPackage.setName(rs.getString("name"));
            effectPackage.setCategory(rs.getString("category"));
            return effectPackage;
        }
    }
    
    /**
     * 查询所有的package
     * @return List<EffectPackage> package列表
     */
    @Override
    public List<EffectPackage> queryAllPackages()
    {
        String sql = "SELECT * FROM T_URL_PACKAGE ORDER BY NAME";
        List<EffectPackage> effectPackages = this.getJdbcTemplate().query(sql, new MonitorPackageRowMapper());
        return (effectPackages != null && !effectPackages.isEmpty()) ? effectPackages : null;
    }
    
    /**
     * 按照id查询package
     * @param pkgId package的id
     * @return EffectPackage package
     */
    @Override
    public EffectPackage queryPkgById(String pkgId)
    {
        String sql = "SELECT * FROM T_URL_PACKAGE WHERE id=?";
        EffectPackage effectPackage =
            this.getJdbcTemplate().queryForObject(sql, new Object[] {pkgId}, new MonitorPackageRowMapper());
        return (effectPackage != null) ? effectPackage : null;
    }
    
    /**
     * 创建package
     * 
     * @param pkgName pkgName
     * @param category category
     */
    @Override
    public void createPkg(String pkgName, String category)
    {
        String sql = "INSERT INTO T_URL_PACKAGE(name,category) values(?,?)";
        this.getJdbcTemplate().update(sql, new Object[] {pkgName, category});
    }
    
    /**
     * 更新package
     * @param pkgId pkgId
     * @param pkgName pkgName
     */
    @Override
    public void updatePkg(String pkgId, String pkgName)
    {
        String sql = "UPDATE T_URL_PACKAGE SET NAME=? WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {pkgName, pkgId});
    }
    
    /**
     * 删除package
     * 
     * @param pkgId pkgId
     */
    @Override
    public void deletePkg(String pkgId)
    {
        String sql = "DELETE FROM T_URL_PACKAGE WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {pkgId});
    }
}
