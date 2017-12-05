package com.huawei.ide.services.res.monitor.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.ide.beans.res.monitor.Effect;
import com.huawei.ide.beans.res.monitor.EffectPackage;
import com.huawei.ide.beans.res.monitor.EffectPackageRelation;
import com.huawei.ide.daos.res.monitor.MonitorDao;
import com.huawei.ide.daos.res.monitor.MonitorPackageDao;
import com.huawei.ide.daos.res.monitor.MonitorPackageRelationDao;
import com.huawei.ide.services.res.monitor.MonitorService;

/**
 * 效果监控url操作数据库服务类
 * 
 * @author zWX301264
 * 
 */
@Service(value = "com.huawei.ide.services.res.monitor.impl.MonitorServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
public class MonitorServiceImpl implements MonitorService
{
    @Resource(name = "com.huawei.ide.daos.res.monitor.impl.MonitorDaoImpl")
    private MonitorDao monitorDao;
    
    @Resource(name = "com.huawei.ide.daos.res.monitor.impl.MonitorPackageDaoImpl")
    private MonitorPackageDao monitorPackageDao;
    
    @Resource(name = "com.huawei.ide.daos.res.monitor.impl.MonitorPackageRelationDaoImpl")
    private MonitorPackageRelationDao monitorPackageRelationDao;
    /**
     * 查询所有url
     * 
     * @return List<Effect> url对象列表
     */
    @Override
    public List<Effect> queryAllEffect()
    {
        return monitorDao.queryAllEffect();
    }
    /**
     * 创建url对象
     * @param effect
     *            url对象
     * @return int 数据库表该条记录id
     */
    @Override
    public int createEffect(Effect effect)
    {
        return monitorDao.createEffect(effect);
    }
    /**
     * 更新url
     * 
     * @param effect
     *            url对象
     */
    @Override
    public void updateEffect(Effect effect)
    {
        monitorDao.updateEffect(effect);
    }
    /**
     * 删除url
     * 
     * @param effectId
     *            url id
     */
    @Override
    public void deleteEffect(int effectId)
    {
        monitorDao.deleteEffect(effectId);
    }
    /**
     * 查询所有package
     * 
     * @return List<EffectPackage> package列表
     */
    @Override
    public List<EffectPackage> queryAllPackages()
    {
        return monitorPackageDao.queryAllPackages();
    }
    /**
     * 查询package下所有url relation对象
     * 
     * @param id
     *            package id
     * @return List<EffectPackageRelation> List<EffectPackageRelation>
     */
    @Override
    public List<EffectPackageRelation> queryEffectPackageRelationByPkgId(int id)
    {
        return monitorPackageRelationDao.queryEffectPackageRelationByPkgId(id);
    }
    /**
     * 根据id查询package
     * 
     * @param pkgId
     *            package id
     * @return EffectPackage EffectPackage
     */
    @Override
    public EffectPackage queryPkgById(String pkgId)
    {
        return monitorPackageDao.queryPkgById(pkgId);
    }
    /**
     * 创建url relation对象
     * 
     * @param epr
     *            EffectPackageRelation
     */
    @Override
    public void createEffectRelation(EffectPackageRelation epr)
    {
        monitorPackageRelationDao.createEffectRelation(epr);
    }
    /**
     * 创建package
     * 
     * @param pkgName
     *            pkgName
     * @param category
     *            category
     */
    @Override
    public void createPkg(String pkgName, String category)
    {
        monitorPackageDao.createPkg(pkgName, category);
    }
    /**
     * 更新package
     * 
     * @param pkgId
     *            package id
     * @param pkgName
     *            package name
     */
    @Override
    public void updatePkg(String pkgId, String pkgName)
    {
        monitorPackageDao.updatePkg(pkgId, pkgName);
    }
    /**
     * 更新 package relation
     * 
     * @param pkgId
     *            package id
     * @param name
     *            package name
     */
    @Override
    public void updatePkgRelation(String pkgId, String name)
    {
        monitorPackageRelationDao.updatePkgRelation(pkgId, name);
    }
    /**
     * 删除package relation
     * 
     * @param pkgId
     *            package id
     */
    @Override
    public void deletePkgRelation(String pkgId)
    {
        monitorPackageRelationDao.deletePkgRelation(pkgId);
    }
    /**
     * 删除package
     * 
     * @param pkgId
     *            package id
     */
    @Override
    public void deletePkg(String pkgId)
    {
        monitorPackageDao.deletePkg(pkgId);
    }
    /**
     * 根据url id删除package relation
     * 
     * @param effectId
     *            url id
     */
    @Override
    public void deleteEffectPackageRelationByUrlId(int effectId)
    {
        monitorPackageRelationDao.deleteEffectPackageRelationByUrlId(effectId);
        
    }
    /**
     * 根据url id更新package relation
     * 
     * @param effect
     *            url 对象
     */
    @Override
    public void updateEffectPackageRelationByUrlId(Effect effect)
    {
        monitorPackageRelationDao.updateEffectPackageRelationByUrlId(effect);
    }
    
}
