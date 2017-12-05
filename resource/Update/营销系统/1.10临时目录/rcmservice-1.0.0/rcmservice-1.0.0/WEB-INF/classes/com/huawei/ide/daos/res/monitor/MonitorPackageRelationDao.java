package com.huawei.ide.daos.res.monitor;

import java.util.List;

import com.huawei.ide.beans.res.monitor.Effect;
import com.huawei.ide.beans.res.monitor.EffectPackageRelation;

/**
 * url package relation 数据操作类
 * 
 * @author zWX301264
 * 
 */
public interface MonitorPackageRelationDao
{
    /**
     * 查询所有package relation
     * 
     * @param id id
     * @return List<EffectPackageRelation>
     */
    List<EffectPackageRelation> queryEffectPackageRelationByPkgId(int id);
    
    /**
     * 创建package relation
     * @param epr epr
     */
    void createEffectRelation(EffectPackageRelation epr);
    
    /**
     * 更新package relation
     * @param pkgId pkgId
     * @param name name
     */
    void updatePkgRelation(String pkgId, String name);
    
    /**
     * 删除package relation
     * 
     * @param pkgId pkgId
     */
    void deletePkgRelation(String pkgId);
    
    /**
     * 根据urlId删除package relation
     * 
     * @param effectId effectId
     */
    void deleteEffectPackageRelationByUrlId(int effectId);
    
    /**
     * 根据urlId更新package relation
     * 
     * @param effect effect
     */
    void updateEffectPackageRelationByUrlId(Effect effect);
    
}
