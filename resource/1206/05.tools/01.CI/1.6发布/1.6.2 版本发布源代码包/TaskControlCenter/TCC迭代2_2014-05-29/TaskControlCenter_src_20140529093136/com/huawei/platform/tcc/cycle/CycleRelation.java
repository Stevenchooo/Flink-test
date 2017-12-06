/*
 * 文 件 名:  CycleRelation.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  任务周期关系【依赖】
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-21
 */
package com.huawei.platform.tcc.cycle;

import com.huawei.platform.tcc.constants.type.RunningState;

/**
 * 周期关系【依赖】
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-1-21]
 */
public class CycleRelation
{
    //源周期
    private Cycle srcCycle;
    
    //目标周期
    private Cycle dstCycle;
    
    //是否忽略错误
    private boolean ignoreError;
    
    /**
     * 构造函数
     * @param srcCycle 源周期
     * @param dstCycle 目标周期
     * @param ignoreError 是否忽略错误
     */
    public CycleRelation(Cycle srcCycle, Cycle dstCycle, boolean ignoreError)
    {
        this.srcCycle = srcCycle;
        this.dstCycle = dstCycle;
        this.ignoreError = ignoreError;
    }
    
    
    /**
     * 依赖关系是否OK
     * @return 依赖关系是否OK
     */
    public boolean isOK()
    {
        int state = dstCycle.getTaskRS().getState();
        boolean ok = RunningState.SUCCESS == state;
        if (!ignoreError)
        {
            //同一任务不同周期直接的依赖，虚拟成功也算成功
            if (srcCycle.getTaskRS().getTaskId().equals(dstCycle.getTaskRS().getTaskId()))
            {
                ok = ok || RunningState.VSUCCESS == state;
            }
        }
        else
        {
            ok = ok || RunningState.ERROR == state || RunningState.NOBATCH == state || RunningState.TIMEOUT == state;
            //同一任务不同周期直接的依赖，虚拟成功也算成功
            if (srcCycle.getTaskRS().getTaskId().equals(dstCycle.getTaskRS().getTaskId()))
            {
                ok = ok || RunningState.VSUCCESS == state;
            }
        }
        
        return ok;
    }
    
    /**
     * 是否已经忽略错误
     * @return 是否已经忽略错误
     */
    public boolean isRealIgnoreError()
    {
        int state = dstCycle.getTaskRS().getState();
        if (!ignoreError)
        {
            return false;
        }
        
        return RunningState.ERROR == state || RunningState.NOBATCH == state || RunningState.TIMEOUT == state;
    }
    
    public Cycle getSrcCycle()
    {
        return srcCycle;
    }
    
    public void setSrcCycle(Cycle srcCycle)
    {
        this.srcCycle = srcCycle;
    }
    
    public Cycle getDstCycle()
    {
        return dstCycle;
    }
    
    public void setDstCycle(Cycle dstCycle)
    {
        this.dstCycle = dstCycle;
    }
    
    public boolean isIgnoreError()
    {
        return ignoreError;
    }
    
    public void setIgnoreError(boolean ignoreError)
    {
        this.ignoreError = ignoreError;
    }
}
