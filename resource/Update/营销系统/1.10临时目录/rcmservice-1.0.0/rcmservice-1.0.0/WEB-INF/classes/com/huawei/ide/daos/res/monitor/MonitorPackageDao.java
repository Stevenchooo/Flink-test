package com.huawei.ide.daos.res.monitor;

import java.util.List;

import com.huawei.ide.beans.res.monitor.EffectPackage;

/**
 * url package对象数据操作类
 * 
 * @author zWX301264
 * 
 */
public interface MonitorPackageDao
{
    /**
     * 查询所有的package
     * @return List<EffectPackage> package列表
     */
    List<EffectPackage> queryAllPackages();
    
    /**
     * 按照id查询package
     * @param pkgId package的id
     * @return EffectPackage package
     */
    EffectPackage queryPkgById(String pkgId);
    
    /**
     * 更新package
     * @param pkgId pkgId
     * @param pkgName pkgName
     */
    void updatePkg(String pkgId, String pkgName);
    
    /**
     * 删除package
     * 
     * @param pkgId pkgId
     */
    void deletePkg(String pkgId);
    
    /**
     * 创建package
     * 
     * @param pkgName pkgName
     * @param category category
     */
    void createPkg(String pkgName, String category);
    
}
