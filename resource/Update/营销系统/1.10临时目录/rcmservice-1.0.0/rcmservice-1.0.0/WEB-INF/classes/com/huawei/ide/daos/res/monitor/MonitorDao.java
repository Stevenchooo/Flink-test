package com.huawei.ide.daos.res.monitor;

import java.util.List;

import com.huawei.ide.beans.res.monitor.Effect;

/**
 * 效果监控对象数据库操作类
 * 
 * @author zWX301264
 * 
 */
public interface MonitorDao
{
    /**
     * 查询所有url
     * @return List<Effect> List<Effect>
     */
    public List<Effect> queryAllEffect();
    
    /**
     * 创建url对象
     * @param effect url对象
     * @return 对象id
     */
    public int createEffect(Effect effect);
    
    /**
     * 更新url对象
     * @param effect url对象
     */
    public void updateEffect(Effect effect);
    
    /**
     * 删除url对象
     * @param effectId  url的id
     */
    public void deleteEffect(int effectId);
    
}
