/*
 * 文 件 名:  Tcc2Shell.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-1-9
 */
package com.huawei.platform.tcc.interfaces.impl;

import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.entity.StepRunningStateEntity;
import com.huawei.platform.tcc.interfaces.Tcc2Shell;

/**
 * tcc给壳程序提供更新数据的接口
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-1-9]
 * @see  [相关类/方法]
 */
public class Tcc2ShellImpl implements Tcc2Shell
{
    private TccDao tccDao;
    
    /**
     * 构造函数
     * @param tccDao tccDao对象
     */
    public Tcc2ShellImpl(TccDao tccDao)
    {
        this.tccDao = tccDao;
    }
    
    /**
     * 更新步骤运行状态信息
     * @param stepRS 步骤运行状态
     * @return 受影响的记录数 
     * @throws CException 统一封装的异常
     */
    @Override
    public int updateStepRunningState(StepRunningStateEntity stepRS)
        throws CException
    {
        return tccDao.updateStepRunningState(stepRS);
    }
}
