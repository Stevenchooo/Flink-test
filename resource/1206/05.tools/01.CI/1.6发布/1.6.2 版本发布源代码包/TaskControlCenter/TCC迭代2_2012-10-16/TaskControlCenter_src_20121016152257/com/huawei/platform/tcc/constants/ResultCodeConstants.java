/*
 * 文 件 名:  ResultCodeConstants.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <Profile错误码常量定义类>
 * 创 建 人:  l00166278
 * 创建时间:  2011-8-12
 */
package com.huawei.platform.tcc.constants;

/**
 * <TCC错误码常量定义类>
 * 
 * @author  l00166278
 * @version [Internet Business Service Platform SP V100R100, 2011-8-12]
 * @see  [相关类/方法]
 */
public class ResultCodeConstants
{
    //===================内部错误 8700014XX begin================
    /**系统错误*/
    public static final int SYSTEM_ERROR = 870001401;
    
    //===================内部错误 8700014XX end===================
    
    //==================接入鉴权错误 8700011XX begin==================
    /**
    * 接口调用过程鉴权失败，会话失效且鉴权码错误
    */
    public static final int SERVICE_AUTH_FAILED = 870001101;
    
    /**业务请求已超过请求速率上限*/
    //挪至公共的错误码文件中
    //public static final int REQOVERLIMIT = 870001104;
    /**系统忙*/
    //挪至公共的错误码文件中
    //public static final int SYSTEMBUSY =  870001105;
    
    //==================接入鉴权错误 8700011XX end==================
    
    //===========参数校验错误 8700012XX begin =====================   
    /**参数不合法*/
    public static final int PARAMETER_INVALID = 870001201;
    
    /**消息格式不合法*/
    public static final int REQUEST_FORMAT_ERROR = 870001202;
    
    //===========参数校验错误 8700012XX end =====================
    
    //===========路由和连接错误 8700013XX begin===================
    /**调用个人UP接口失败*/
    public static final int TRANSFER_INDIVUP_ERROR = 870001301;
    
    /**缓存数据操作失败*/
    public static final int MEMCACHED_OP_FAILED = 870001304;
    
    //===========路由和连接错误 8700013XX end=====================
    
    //==========业务逻辑错误 870002XXX begin ====================== 
    /**更新营销活动状态失败 **/
    public static final int UPDATE_ACTIVITY_STATE_ERROR = 870002001;
    
    /**更新营销活动审核状态失败 **/
    public static final int UPDATE_AUDITING_STATE_ERROR = 870002002;
    
    /**更新营销活动审核信息失败 **/
    public static final int UPDATE_AUDITING_INFO_ERROR = 870002003;
    
    /**查询营销活动计划信息失败 **/
    public static final int QUERY_ACTIVITY_PLAN_ERROR = 870002004;
    
    /**查询营销活动计划最大ID失败 **/
    public static final int GET_MAX_ACTIVITYID_ERROR = 870002005;
    
    /**添加营销活动计划失败 **/
    public static final int ADD_ACTIVITY_PLAN_ERROR = 870002006;
    
    /**查询营销权限失败 **/
    public static final int QUERY_MKT_PRIVILEGE_ERROR = 870002007;
    
    /**用户名单列表查询失败*/
    public static final int USER_INFO_NOT_EXIST = 870002008;
    
    //==========业务逻辑错误 870002XXX end ======================== 
    /**添加push信息失败*/
    public static final int ADD_PUSHINFO_ERROR = 870002009;
    
    /**
     * 转换周期Id为日期错误
     */
    public static final int PARSE_CYCLEID_DATE_ERROR = 870002010;
    
    /**
     * 转换日期为周期Id错误
     */
    public static final int PARSE_DATE_CYCLEID_ERROR = 870002011;
    
    /**
     * 执行远程命令错误
     */
    public static final int EXEC_REMOTE_COMMAND_ERROR = 870002012;
    
    /**
     * 指定任务的周期不存在
     */
    public static final int TASK_CYCLE_NOT_EXIST_ERROR = 870002013;
    
    /**
     * ip地址错误
     */
    public static final int IP_ADDR_ERROR = 870002014;
    
    /**
     * 依赖关系错误
     */
    public static final int TASK_DEPEND_REALATION_ERROR = 870002015;
    
    /**
     * 生成任务ID编号错误
     */
    public static final int GENERATE_TASKID_ERROR = 870002016;
    
    
}
