/*
 * 文 件 名:  RunningState.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-21
 */
package com.huawei.platform.tcc.constants.type;

/**
 * 运行状态
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-21]
 * @see  [相关类/方法]
 */
public class RunningState
{
    /**
     * 全部
     */
    public static final int ALL = -2;
    
    /**
     * 未知状态,可能是停止中或者是状态正要更新
     */
    public static final int UNKNOWN = -1;
    
    /**
     * 初始化(等待)
     */
    public static final int INIT = 0;
    
    /**
     * 开始
     */
    public static final int START = 1;
    
    /**
     * 成功
     */
    public static final int SUCCESS = 2;
    
    /**
     * 出错
     */
    public static final int ERROR = 3;
    
    /**
     * 超时
     */
    public static final int TIMEOUT = 4;
    
    /**
     * 虚拟成功，相同周期任务的周期依赖认为是成功
     */
    public static final int VSUCCESS = 5;
    
    /**
     * 未初始化
     */
    public static final int NOTINIT = 6;
    
    /**
     * 等待队列中等待调度运行
     */
    public static final int WAITTINGRUN = 7;
    
    /**
     * 运行中
     */
    public static final int RUNNING = 8;
    
    /**
     * 请求删除
     */
    public static final int REQDELETE = 9;
    
    /**
     * 停止
     */
    public static final int STOP = 10;
    
    /**
     * 批次任务没有批文件到达
     */
    public static final int NOBATCH = 11;
}
