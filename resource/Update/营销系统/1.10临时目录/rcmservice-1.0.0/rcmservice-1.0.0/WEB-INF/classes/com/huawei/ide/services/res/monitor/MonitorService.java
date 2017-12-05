package com.huawei.ide.services.res.monitor;

import java.util.List;

import com.huawei.ide.beans.res.monitor.Effect;
import com.huawei.ide.beans.res.monitor.EffectPackage;
import com.huawei.ide.beans.res.monitor.EffectPackageRelation;

/**
 * 效果监控url操作数据库服务类
 * 
 * @author zWX301264
 * 
 */
public interface MonitorService
{
    /**
     * 查询所有url
     * 
     * @return List<Effect> url对象列表
     */
    public List<Effect> queryAllEffect();
    
    /**
     * 创建url对象
     * @param effect
     *            url对象
     * @return int 数据库表该条记录id
     */
    public int createEffect(Effect effect);
    
    /**
     * 更新url
     * 
     * @param effect
     *            url对象
     */
    public void updateEffect(Effect effect);
    
    /**
     * 删除url
     * 
     * @param effectId
     *            url id
     */
    public void deleteEffect(int effectId);
    
    /**
     * 查询所有package
     * 
     * @return List<EffectPackage> package列表
     */
    public List<EffectPackage> queryAllPackages();
    
    /**
     * 查询package下所有url relation对象
     * 
     * @param id
     *            package id
     * @return List<EffectPackageRelation> List<EffectPackageRelation>
     */
    public List<EffectPackageRelation> queryEffectPackageRelationByPkgId(int id);
    
    /**
     * 根据id查询package
     * 
     * @param pkgId
     *            package id
     * @return EffectPackage EffectPackage
     */
    public EffectPackage queryPkgById(String pkgId);
    
    /**
     * 创建url relation对象
     * 
     * @param epr
     *            EffectPackageRelation
     */
    public void createEffectRelation(EffectPackageRelation epr);
  
    /**
     * 更新package
     * 
     * @param pkgId
     *            package id
     * @param pkgName
     *            package name
     */
    public void updatePkg(String pkgId, String pkgName);
    
    /**
     * 更新 package relation
     * 
     * @param pkgId
     *            package id
     * @param name
     *            package name
     */
    public void updatePkgRelation(String pkgId, String name);
    
    /**
     * 删除package relation
     * 
     * @param pkgId
     *            package id
     */
    public void deletePkgRelation(String pkgId);
    
    /**
     * 删除package
     * 
     * @param pkgId
     *            package id
     */
    public void deletePkg(String pkgId);
    
    /**
     * 根据url id删除package relation
     * 
     * @param effectId
     *            url id
     */
    public void deleteEffectPackageRelationByUrlId(int effectId);
    
    /**
     * 根据url id更新package relation
     * 
     * @param effect
     *            url 对象
     */
    public void updateEffectPackageRelationByUrlId(Effect effect);
    
    /**
     * 创建package
     * 
     * @param pkgName
     *            pkgName
     * @param category
     *            category
     */
    void createPkg(String pkgName, String category);
    
}
