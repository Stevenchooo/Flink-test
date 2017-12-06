/*
 * 文 件 名:  TccUtil.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-29
 */
package com.huawei.platform.tcc.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.huawei.cct.websecurity.encoder.EncodingException;
import com.huawei.platform.common.CException;
import com.huawei.platform.common.CommonUtils;
import com.huawei.platform.common.CryptUtilforcfg;
import com.huawei.platform.common.remote.http.client.HttpRequester;
import com.huawei.platform.common.remote.http.client.HttpResponse;
import com.huawei.platform.tcc.constants.ResultCode;
import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.constants.type.CycleType;
import com.huawei.platform.tcc.constants.type.NameStoredInSession;
import com.huawei.platform.tcc.constants.type.RunningState;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.CycleDependRelation;
import com.huawei.platform.tcc.domain.DependRelation;
import com.huawei.platform.tcc.domain.IModifyResult;
import com.huawei.platform.tcc.entity.Log2DBEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskRunningStateEntity;
import com.huawei.platform.tcc.message.req.LoadTasksReq;
import com.huawei.platform.tcc.message.rsp.LoadTasksRsp;
import com.opensymphony.xwork2.ActionContext;

/**
 * Tcc工具类
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2011-12-29]
 */
public class TccUtil
{
    /**
     * 客户端ip
     */
    public static final String CLIENT_IP = "clientIp";
    
    /**
     * 任务Id
     */
    public static final String TASK_ID = "taskId";
    
    /**
     * 周期Id
     */
    public static final String CYCLE_ID = "cycleId";
    
    /**
     * 用户名
     */
    public static final String USER_NAME = "userName";
    
    /**
     * 角色权限
     */
    public static final String ROLE_PRIVILEGE = "rolePrivilege";
    
    /**
     * 获取客户端ip的头属性名
     */
    public static final String IP_HEADER_NAME = "ipHeaderName";
    
    /**
     * 每分钟毫秒数
     */
    public static final long MILLSECONDE_PER_MINUTE = 60 * 1000;
    
    /**
     * 每小时毫秒数
     */
    public static final long MILLSECONDE_PER_HOUR = 3600 * 1000;
    
    /**
     * 每天毫秒数
     */
    public static final long MILLSECONDE_PER_DAY = 24 * MILLSECONDE_PER_HOUR;
    
    /**
     * 最大字符串参数大小100-200M
     */
    public static final int MAX_STRING_LENGTH = 100 * 1024 * 1024;
    
    private static final int MAX_DEPEND_CYCLES = 1000;
    
    /**
     * web应用程序上下文
     */
    private static WebApplicationContext webAppContext = ContextLoader.getCurrentWebApplicationContext();
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TccUtil.class);
    
    private static String keyword = "PkmJygVfrDxsDeeD";
    
    /**
     * 对text进行换行符替换编码
     * @param text 文本
     * @return 换行符替换编码
     */
    public static String noNewLineEncode(Object text)
    {
        Map<Character, String> replaceChars = new HashMap<Character, String>();
        replaceChars.put('\n', "<br/>");
        replaceChars.put('\r', "<br/>");
        return new Encoder(replaceChars).truncatEncode(text.toString());
    }
    
    /**
     * input是否是有效输入
     * @param input 输入
     * @return 是否是有效输入
     */
    public static boolean isValidInput(String input)
    {
        //null是允许的
        if (null == input || input.isEmpty())
        {
            return true;
        }
        
        //对输入信息校验
        Pattern pat = Pattern.compile(TccConfig.getInputRegex(), Pattern.MULTILINE);
        //对输入做归一化，防止其它形式的字符出现
        Matcher macher = pat.matcher(Normalizer.normalize(input, Form.NFKC));
        return macher.find();
    }
    
    /**
     * 远程调用prepareLoadTasks/loadTasks方法
     * @param url web服务地址
     * @param req 请求体
     * @return 响应
     * @throws CException 异常
     */
    public static LoadTasksRsp callLoadTasks(String url, LoadTasksReq req)
        throws CException
    {
        //转化为JSON字符串
        final String body = JSON.toJSONString(req);
        
        final HttpRequester httpreq = new HttpRequester();
        
        LOGGER.info("call loadTasks start,url is {}, req is {}", url, req);
        HttpResponse httprsp = null;
        try
        {
            //通过post方法调用回调url地址
            httprsp = httpreq.post(url, null, body);
            
            LOGGER.info("call loadTasks end, response is {}", TccUtil.truncatEncode(httprsp));
            if (null != httprsp && HttpRequester.HTTP_OK == httprsp.getCode())
            {
                //获取HTTP响应消息
                final String rspBody = httprsp.getContent();
                final LoadTasksRsp rsp = TccUtil.parseObject(rspBody, LoadTasksRsp.class);
                //返回码为0表示正常响应
                if (null != rsp)
                {
                    return rsp;
                }
                else
                {
                    LOGGER.error("call {} failed! httprsp is {}", url, httprsp);
                    throw new CException(ResultCode.SYSTEM_ERROR);
                }
            }
            else
            {
                //HTTP响应异常
                LOGGER.error("not correct responce for called loadTasks! req is {},response is {}", new Object[] {
                    TccUtil.truncatEncode(req), TccUtil.truncatEncode(httprsp)});
                throw new CException(ResultCode.SYSTEM_ERROR);
            }
        }
        catch (IOException e)
        {
            LOGGER.error("call httpreq.post failed! req is {},response is {}", new Object[] {
                TccUtil.truncatEncode(req), TccUtil.truncatEncode(httprsp), e});
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 关闭流
     * @param stream 流
     */
    public static void close(Closeable stream)
    {
        //流关闭方法
        if (null != stream)
        {
            try
            {
                stream.close();
            }
            catch (IOException e)
            {
                LOGGER.error("close stream failed!", e);
            }
        }
    }
    
    /**
     * 根据异常设置返回码
     * @param result 可以修改返回码的接口
     * @param e 异常
     */
    public static void setResultByException(IModifyResult result, Exception e)
    {
        //如果参数为null，直接返回
        if (null == result || null == e)
        {
            LOGGER.error("setResultByException error! result is {}, e is {}", result, e);
            return;
        }
        
        //从异常中提取错误码
        if (e instanceof CException)
        {
            result.setResultCode(((CException)e).getErrorCode());
        }
        else if (e instanceof JSONException)
        {
            //JSON异常返回消息格式不合法错误码
            result.setResultCode(ResultCode.REQUEST_FORMAT_ERROR);
        }
        else
        {
            //其它返回系统错误码
            result.setResultCode(ResultCode.SYSTEM_ERROR);
        }
    }
    
    /**
     * 将Obj对象转换成“字段名：字段值”对象的Map对象
     * @param obj 待转换对象
     * @return Map对象
     */
    public static Map<String, Object> covObj2Map(final Object obj)
    {
        //字段名，字段值Map
        final Map<String, Object> keyValueMap = new HashMap<String, Object>();
        if (null != obj)
        {
            //获取声明的字段数组
            final Field[] fields = obj.getClass().getDeclaredFields();
            //使用权限块处理
            AccessController.doPrivileged(new PrivilegedAction<Map<String, Object>>()
            {
                @Override
                public Map<String, Object> run()
                {
                    Object value = null;
                    for (int j = 0; j < fields.length; j++)
                    {
                        fields[j].setAccessible(true);
                        try
                        {
                            //获取字段值
                            value = fields[j].get(obj);
                            keyValueMap.put(fields[j].getName(), value);
                        }
                        catch (IllegalArgumentException e)
                        {
                            //参数不合法
                            LOGGER.error("getValue failed!", e);
                        }
                        catch (IllegalAccessException e)
                        {
                            //访问不合法
                            LOGGER.error("getValue failed!", e);
                        }
                    }
                    return keyValueMap;
                }
            });
        }
        else
        {
            //参数不能为空
            LOGGER.error("obj can't be null!");
        }
        
        return keyValueMap;
    }
    
    /**
     * 解析请求json消息体为相应的对象
     * @param body 请求json消息体
     * @param reqClass 请求json消息体对应的类
     * @param <T> 泛型类型
     * @return 请求对象 
     */
    public static <T extends Object> T parseObject(String body, Class<T> reqClass)
    {
        //在json对象外部包装上req字段名，方便读取和转换对象
        JSONObject json = JSONObject.parseObject(String.format("{\"req\":%s}", body));
        T req = json.getObject("req", reqClass);
        return req;
    }
    
    /**
     * 时间字符串 
     * @param time 时间
     * @return 时间字符串 
     */
    public static String timeString(Date time)
    {
        if (null == time)
        {
            return "";
        }
        
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        df.setLenient(false);
        return df.format(time);
    }
    
    /**
     * 分离出节点Id列表
     * @param deployedNodeIds 以逗号分割的部署节点字符串
     * @return 节点Id列表
     */
    public static List<Integer> parseNodeIds(String deployedNodeIds)
    {
        List<Integer> nodeIds = new ArrayList<Integer>();
        if (StringUtils.isBlank(deployedNodeIds))
        {
            return nodeIds;
        }
        
        String[] nodes = deployedNodeIds.split(",");
        for (String nodeId : nodes)
        {
            //非空
            if (!StringUtils.isBlank(nodeId))
            {
                nodeIds.add(Integer.parseInt(nodeId));
            }
        }
        
        return nodeIds;
    }
    
    /**
     * 分离出任务Id列表
     * @param taskIdStrs 以逗号分割的任务Id字符串
     * @return 任务Id列表
     */
    public static List<Long> parseTaskIds(String taskIdStrs)
    {
        List<Long> taskIds = new ArrayList<Long>();
        if (StringUtils.isBlank(taskIdStrs))
        {
            return taskIds;
        }
        
        String[] taskIdArr = taskIdStrs.split(",");
        for (String taskIdStr : taskIdArr)
        {
            //非空
            if (!StringUtils.isBlank(taskIdStr))
            {
                taskIds.add(Long.parseLong(taskIdStr));
            }
        }
        
        return taskIds;
    }
    
    /**
     * 以逗号分割的部署节点字符串
     * @param nodeIds 节点Id集合
     * @return 以逗号分割的部署节点字符串
     */
    public static String toDeployedNodeIdStr(List<String> nodeIds)
    {
        StringBuilder deployedNodeIds = new StringBuilder();
        
        for (String nodeId : nodeIds)
        {
            deployedNodeIds.append(nodeId);
            deployedNodeIds.append(',');
        }
        
        if (deployedNodeIds.length() > 0)
        {
            //删除最后一个','号
            deployedNodeIds.deleteCharAt(deployedNodeIds.length() - 1);
        }
        
        return deployedNodeIds.toString();
    }
    
    /**
     * 是否不包含任何元素
     * @param coll 集合
     * @param <T> 类型
     * @return 是否不包含任何元素
     * 
     */
    public static <T extends Object> boolean noAnyElement(Collection<T> coll)
    {
        return null == coll || coll.isEmpty();
    }
    
    /**
     * 获取当天日期
     * @return 日期
     */
    public static Date getCurrentDate()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        return cal.getTime();
    }
    
    /**
     * 获取time所属的日期
     * @param time 时间
     * @return 日期
     */
    public static Date getDate(Date time)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        return cal.getTime();
    }
    
    /**
     * 当前时间偏移seconds秒数
     * @param seconds 秒
     * @return 日期
     */
    public static Date addSeconds(int seconds)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.SECOND, seconds);
        
        return cal.getTime();
    }
    
    /**
     * 获取系统配置
     * @param key 键
     * @return 系统配置项
     */
    public static String getSysConfig(String key)
    {
        TccSysConfigService sysConfigService = (TccSysConfigService)getBeanByID("tccSysConfigService");
        return sysConfigService != null ? sysConfigService.getSysConfigKey(key) : null;
    }
    
    /**
     * 获取bean对象
     * @param beanID bean标识
     * @return bean对象
     */
    public static Object getBeanByID(String beanID)
    {
        if (StringUtils.isBlank(beanID))
        {
            return null;
        }
        else
        {
            if (null == webAppContext)
            {
                webAppContext = ContextLoader.getCurrentWebApplicationContext();
            }
            return webAppContext.getBean(beanID);
        }
    }
    
    /**
     * 获取不在startTime之前的最小周期Id
     * @param startTime 时间
     * @return 最小周期Id
     */
    public static String getMinCycleId(Date startTime)
    {
        String minCycleId;
        if (null != startTime && startTime.after(TccConfig.getBenchDate()))
        {
            Calendar cTime = Calendar.getInstance();
            cTime.setTime(startTime);
            if (0 != cTime.get(Calendar.SECOND))
            {
                cTime.add(Calendar.MINUTE, 1);
            }
            minCycleId = TccUtil.covDate2CycleID(startTime);
        }
        else
        {
            minCycleId = TccConfig.getMinCycleId();
        }
        
        return minCycleId;
    }
    
    /**
     * 判断两个对象是否相等
     * @param a 对象a
     * @param b 对象b
     * @return 两个对象是否相等
     */
    public static boolean equals(Object a, Object b)
    {
        if (null == a && null == b)
        {
            return true;
        }
        else if (null != a && null != b)
        {
            if (a.getClass().equals(b.getClass()))
            {
                return a.equals(b);
            }
        }
        
        return false;
    }
    
    /**
     * 计算直接依赖当前任务周期的所有任务周期集合,仅支持同周期依赖
     * @param task 任务
     * @param tasks 直接依赖于task的任务集合
     * @param cycleId 周期Id
     * @return 依赖当前任务周期的所有任务周期集合
     * @exception Exception 异常
     */
    public static List<CycleDependRelation> getDeppingCycleRs(TaskEntity task, List<TaskEntity> tasks, String cycleId)
        throws Exception
    {
        if (null == task || null == task.getTaskId() || null == cycleId)
        {
            return new ArrayList<CycleDependRelation>(0);
        }
        
        //最大开始时间
        Date curMaxStartTime = task.getStartTime();
        if (null == curMaxStartTime || curMaxStartTime.before(TccConfig.getBenchDate()))
        {
            curMaxStartTime = TccConfig.getBenchDate();
        }
        
        //当前周期的起始时间
        Date currentDate = TccUtil.covCycleID2Date(cycleId);
        
        //处在起始时间之前的任务周期不会被任何周期依赖
        if (currentDate.before(curMaxStartTime))
        {
            return new ArrayList<CycleDependRelation>(0);
        }
        
        List<CycleDependRelation> cycleDeppedRLst = new ArrayList<CycleDependRelation>();
        
        Long taskId = task.getTaskId();
        int cycleType = CycleType.toCycleType(task.getCycleType());
        boolean seqDepend = task.getCycleDependFlag();
        int cycleLength = task.getCycleLength();
        
        //任务周期不能超越当前时间
        Date maxTime = new Date();
        
        //如果是顺序依赖，则后一个任务周期依赖于当前任务周期。
        if (seqDepend)
        {
            //后一个周期
            Date nextDate = TccUtil.roll2CycleStart(currentDate, cycleType, cycleLength);
            String nextDateCycleId = TccUtil.covDate2CycleID(nextDate);
            
            //基准时间或者开始时间之前的周期可不用考虑依赖关系
            if (!nextDate.before(currentDate) && nextDate.before(maxTime))
            {
                //周期依赖关系
                CycleDependRelation cycleDepRT = new CycleDependRelation();
                cycleDepRT.setDependTaskId(taskId);
                cycleDepRT.setDependCycleId(nextDateCycleId);
                cycleDepRT.setIgnoreError(false);
                if (!cycleDeppedRLst.contains(cycleDepRT))
                {
                    cycleDeppedRLst.add(cycleDepRT);
                }
            }
        }
        
        //周期依赖关系
        CycleDependRelation cycleDepR;
        //周期大小比较
        int compareValue;
        //依赖关系
        DependRelation dependRelation;
        Long deppingTaskId;
        int deppingCycleType;
        int deppingCycleLength;
        for (TaskEntity depingTask : tasks)
        {
            dependRelation = TccUtil.filterDepRs(taskId, depingTask.getDependTaskIdList());
            
            if (null != dependRelation)
            {
                deppingTaskId = depingTask.getTaskId();
                deppingCycleType = CycleType.toCycleType(depingTask.getCycleType());
                deppingCycleLength = depingTask.getCycleLength();
                
                //周期大小比较
                compareValue = deppingCycleType * depingTask.getCycleLength() - cycleType * cycleLength;
                
                //如果大周期类型依赖于小周期类型并且是全周期依赖
                if (compareValue > 0)
                {
                    //小周期的当前周期的起始时间
                    Date smallcurrDate = TccUtil.covCycleID2Date(cycleId);
                    //计算smallcurrDate所在的大周期的周期时间
                    Date depingPreDate =
                        TccUtil.roll2CycleStartOnBenchDate(smallcurrDate,
                            deppingCycleType,
                            depingTask.getCycleLength(),
                            0);
                    Date depingNextDate =
                        TccUtil.roll2CycleStart(depingPreDate, deppingCycleType, depingTask.getCycleLength());
                    
                    Date nextSmallDate = TccUtil.roll2CycleStart(smallcurrDate, cycleType, cycleLength);
                    
                    //如果是全周期依赖或者是大周期内的最后一个小周期
                    if (dependRelation.isFullCycleDepend() || !nextSmallDate.before(depingNextDate))
                    {
                        //允许最早开始的时间
                        Date earliestStartTime = depingTask.getStartTime();
                        if (null == earliestStartTime || earliestStartTime.before(TccConfig.getBenchDate()))
                        {
                            earliestStartTime = TccConfig.getBenchDate();
                        }
                        
                        //基准时间之前的周期可不用考虑依赖关系
                        if (!depingPreDate.before(earliestStartTime) && depingPreDate.before(maxTime))
                        {
                            cycleDepR = new CycleDependRelation();
                            cycleDepR.setDependTaskId(deppingTaskId);
                            cycleDepR.setDependCycleId(TccUtil.covDate2CycleID(depingPreDate));
                            cycleDepR.setIgnoreError(dependRelation.isIgnoreError());
                            if (!cycleDeppedRLst.contains(cycleDepR))
                            {
                                cycleDeppedRLst.add(cycleDepR);
                            }
                        }
                    }
                }
                else
                {
                    //小周期依赖与大周期(已知)
                    //大周期的当前周期的起始时间
                    Date largeCurrentDate = TccUtil.covCycleID2Date(cycleId);
                    //大周期的后一个周期的起始时间
                    Date largenextDate = TccUtil.roll2CycleStart(largeCurrentDate, cycleType, cycleLength);
                    
                    //做一些微量的时间偏移，以便排除掉最后一个小周期
                    largenextDate = TccUtil.tuneTiny(largenextDate, false);
                    
                    //获取上一个大周期内的全部小周期ID
                    List<String> dependCycleIds =
                        TccUtil.generateCycleIDs(largeCurrentDate, largenextDate, deppingCycleType, deppingCycleLength);
                    
                    //允许最早开始的时间
                    Date earliestStartTime = depingTask.getStartTime();
                    if (null == earliestStartTime || earliestStartTime.before(TccConfig.getBenchDate()))
                    {
                        earliestStartTime = TccConfig.getBenchDate();
                    }
                    
                    Date deppingCycleTime;
                    
                    for (String depCycleId : dependCycleIds)
                    {
                        //基准时间或者开始时间之前的周期可不用考虑依赖关系
                        deppingCycleTime = TccUtil.covCycleID2Date(depCycleId);
                        if (!deppingCycleTime.before(earliestStartTime) && deppingCycleTime.before(maxTime))
                        {
                            cycleDepR = new CycleDependRelation();
                            cycleDepR.setDependTaskId(deppingTaskId);
                            cycleDepR.setDependCycleId(depCycleId);
                            cycleDepR.setIgnoreError(dependRelation.isIgnoreError());
                            if (!cycleDeppedRLst.contains(cycleDepR))
                            {
                                cycleDeppedRLst.add(cycleDepR);
                            }
                        }
                    }
                }
            }
        }
        
        return cycleDeppedRLst;
    }
    
    /**
     * 向sbDependStates中追加依赖任务状态
     * @param sbDependStates 任务依赖状态
     * @param taskId  任务Id
     * @param cycleId 周期Id
     * @param cycleDepRLst 周期依赖集合
     */
    public static void genDependStates(StringBuilder sbDependStates, Long taskId, String cycleId,
        List<CycleDependRelation> cycleDepRLst)
    {
        if (null == cycleDepRLst || cycleDepRLst.isEmpty())
        {
            return;
        }
        
        if (cycleDepRLst.size() >= MAX_DEPEND_CYCLES)
        {
            LOGGER.error("depends[{}] of cycle[taskId={},cycleId={}] has more than {}.not schedule it!", new Object[] {
                cycleDepRLst.size(), taskId, cycleId, MAX_DEPEND_CYCLES});
            return;
        }
        
        for (CycleDependRelation cycleDepR : cycleDepRLst)
        {
            
            sbDependStates.append(String.format("(%d,'%s',%d,'%s',%d),",
                taskId,
                cycleId,
                cycleDepR.getDependTaskId(),
                cycleDepR.getDependCycleId(),
                RunningState.SUCCESS));
            
            //如果依赖于本任务,虚拟成功标识也认为是成功
            if (taskId.equals(cycleDepR.getDependTaskId()))
            {
                sbDependStates.append(String.format("(%d,'%s',%d,'%s',%d),",
                    taskId,
                    cycleId,
                    cycleDepR.getDependTaskId(),
                    cycleDepR.getDependCycleId(),
                    RunningState.VSUCCESS));
            }
            
            //如果忽略依赖任务执行出错,超时,文件未到达的状态
            if (cycleDepR.isIgnoreError())
            {
                sbDependStates.append(String.format("(%d,'%s',%d,'%s',%d),",
                    taskId,
                    cycleId,
                    cycleDepR.getDependTaskId(),
                    cycleDepR.getDependCycleId(),
                    RunningState.ERROR));
                
                sbDependStates.append(String.format("(%d,'%s',%d,'%s',%d),",
                    taskId,
                    cycleId,
                    cycleDepR.getDependTaskId(),
                    cycleDepR.getDependCycleId(),
                    RunningState.TIMEOUT));
                
                sbDependStates.append(String.format("(%d,'%s',%d,'%s',%d),",
                    taskId,
                    cycleId,
                    cycleDepR.getDependTaskId(),
                    cycleDepR.getDependCycleId(),
                    RunningState.NOBATCH));
            }
        }
        
    }
    
    /**
     * 依赖关系是否全部ok
     * @param cycleDepRLst 依赖周期列表
     * @param taskId 周期Id
     * @param tccDao tccDao
     * @return 是否依赖ok
     * @throws CException 异常
     */
    public static boolean isDependsOk(List<CycleDependRelation> cycleDepRLst, Long taskId, TccDao tccDao)
        throws CException
    {
        if (null == cycleDepRLst)
        {
            return true;
        }
        List<TaskRunningStateEntity> taskIdCycleIDStateLst = new ArrayList<TaskRunningStateEntity>();
        TaskRunningStateEntity taskRS;
        for (CycleDependRelation cycleDepR : cycleDepRLst)
        {
            taskRS = new TaskRunningStateEntity();
            taskRS.setTaskId(cycleDepR.getDependTaskId());
            taskRS.setCycleId(cycleDepR.getDependCycleId());
            taskRS.setState(RunningState.SUCCESS);
            taskIdCycleIDStateLst.add(taskRS);
            
            //如果依赖于本任务,虚拟成功标识也认为是成功
            if (taskId.equals(cycleDepR.getDependTaskId()))
            {
                taskRS = new TaskRunningStateEntity();
                taskRS.setTaskId(cycleDepR.getDependTaskId());
                taskRS.setCycleId(cycleDepR.getDependCycleId());
                taskRS.setState(RunningState.VSUCCESS);
                taskIdCycleIDStateLst.add(taskRS);
            }
            
            //如果忽略依赖任务执行出错,超时,文件未到达的状态
            if (cycleDepR.isIgnoreError())
            {
                taskRS = new TaskRunningStateEntity();
                taskRS.setTaskId(cycleDepR.getDependTaskId());
                taskRS.setCycleId(cycleDepR.getDependCycleId());
                taskRS.setState(RunningState.ERROR);
                taskIdCycleIDStateLst.add(taskRS);
                
                taskRS = new TaskRunningStateEntity();
                taskRS.setTaskId(cycleDepR.getDependTaskId());
                taskRS.setCycleId(cycleDepR.getDependCycleId());
                taskRS.setState(RunningState.TIMEOUT);
                taskIdCycleIDStateLst.add(taskRS);
                
                taskRS = new TaskRunningStateEntity();
                taskRS.setTaskId(cycleDepR.getDependTaskId());
                taskRS.setCycleId(cycleDepR.getDependCycleId());
                taskRS.setState(RunningState.NOBATCH);
                taskIdCycleIDStateLst.add(taskRS);
            }
        }
        //当前的周期任务所依赖任务的周期必然已经创建了至少一个
        
        //存在的周期任务数
        
        //int existNum = tccDao.getTaskRunningStateExceptStateCount(taskIdCycleIDStateLst);
        int existNum = cycleDepRLst.size();
        
        //存在且状态完成的周期任务数
        int existFinishedNum = tccDao.getTaskRunningStateCount(taskIdCycleIDStateLst);
        if (existNum == existFinishedNum)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 获取邮件内容
     * @param taskId 任务Id
     * @param taskName 任务名
     * @param cycleId 周期Id
     * @param alarmType 告警类型
     * @return 邮件内容
     */
    public static String getEmailContent(Long taskId, String taskName, String cycleId, Integer alarmType)
    {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("taskId", (null != taskId) ? Long.toString(taskId) : "");
        data.put("taskName", taskName);
        data.put("cycleId", cycleId);
        data.put("alarmType", alarmType);
        data.put("portalUrl", TccConfig.getPortalUrl());
        
        //加载freemarker模板服务类
        ResourceFormatterServiceImpl service =
            (ResourceFormatterServiceImpl)CommonUtils.getBeanByID("emailResourceFormatService");
        return service.getFormattedResource(alarmType + ".content.ftl", data);
    }
    
    /**
     * 获取邮件主题
     * @param taskId 任务Id
     * @param taskName 任务名
     * @param cycleId 周期Id
     * @param alarmType 告警类型
     * @return 邮件主题
     */
    public static String getEmailSubject(Long taskId, String taskName, String cycleId, Integer alarmType)
    {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("taskId", (null != taskId) ? Long.toString(taskId) : "");
        data.put("taskName", taskName);
        data.put("cycleId", cycleId);
        data.put("alarmType", alarmType);
        data.put("portalUrl", TccConfig.getPortalUrl());
        //加载freemarker模板服务类
        ResourceFormatterServiceImpl service =
            (ResourceFormatterServiceImpl)CommonUtils.getBeanByID("emailResourceFormatService");
        return service.getFormattedResource(alarmType + ".subject.ftl", data);
    }
    
    /**
     * 判断字符串是否为整数字符串
     * @param str 字符串
     * @return 是否为整数字符串
     */
    public static boolean isInteger(String str)
    {
        if (null == str)
        {
            return false;
        }
        
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    
    /**
     * 记录客户端ip以及用户名信息到日志中，并返回ip以及用户名
     * @return ip以及用户名
     */
    public static Map<String, Object> startLogIPUser2Log()
    {
        Map<String, Object> ipUserMap = new HashMap<String, Object>();
        ActionContext actx = ActionContext.getContext();
        if (null == actx)
        {
            return ipUserMap;
        }
        
        Map<String, Object> session = actx.getSession();
        if (null != session)
        {
            Object ip = session.get(NameStoredInSession.CLIENT_IP);
            Object userName = session.get(NameStoredInSession.USER_NAME);
            if (null != ip)
            {
                MDC.put(NameStoredInSession.CLIENT_IP, ip);
                ipUserMap.put(NameStoredInSession.CLIENT_IP, ip);
            }
            
            if (null != userName)
            {
                MDC.put(NameStoredInSession.USER_NAME, userName);
                ipUserMap.put(NameStoredInSession.USER_NAME, userName);
            }
            
        }
        else
        {
            LOGGER.warn("get session error! action is {}", actx.getName());
        }
        
        return ipUserMap;
    }
    
    /**
     * 停止记录客户端ip以及用户名信息到日志中
     */
    public static void stopLogIPUser2Log()
    {
        MDC.remove(NameStoredInSession.CLIENT_IP);
        MDC.remove(NameStoredInSession.USER_NAME);
    }
    
    /**
     * 记录任务周期ID
     * @param taskId 任务Id
     * @param cycleId 周期Id
     */
    public static void startTaskCycleLog(Long taskId, String cycleId)
    {
        MDC.put(TASK_ID, taskId);
        MDC.put(CYCLE_ID, cycleId);
    }
    
    /**
     * 停止记录任务周期日志
     */
    public static void stopTaskCycleLog()
    {
        MDC.remove(TASK_ID);
        MDC.remove(CYCLE_ID);
    }
    
    /**
     * 获取访问客户端的真实IP地址以及获取到ip的头属性名
     * @param request 请求体
     * @return 访问客户端的真实IP地址
     */
    public static Map<String, String> getRealIp(HttpServletRequest request)
    {
        Map<String, String> mapIp = new HashMap<String, String>();
        mapIp.put(IP_HEADER_NAME, "X-Forwarded-For");
        String ip = request.getHeader("X-Forwarded-For");
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            mapIp.put(IP_HEADER_NAME, "Proxy-Client-IP");
            ip = request.getHeader("Proxy-Client-IP");
        }
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            mapIp.put(IP_HEADER_NAME, "WL-Proxy-Client-IP");
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            mapIp.put(IP_HEADER_NAME, "getRemoteAddr()");
            ip = request.getRemoteAddr();
        }
        mapIp.put(CLIENT_IP, ip);
        return mapIp;
    }
    
    /**
     * 将date提前或延迟数秒
     * @param date 需要提前或者延迟的日期
     * @param bDelay 是延迟还是提前
     * @return 日期
     */
    public static Date tuneTiny(Date date, boolean bDelay)
    {
        Calendar cDate = Calendar.getInstance();
        //校准start为本周期起始点
        cDate.setTime(date);
        if (bDelay)
        {
            cDate.add(Calendar.SECOND, TccConfig.TUNE_SECONDS);
        }
        else
        {
            cDate.add(Calendar.SECOND, -TccConfig.TUNE_SECONDS);
        }
        return cDate.getTime();
    }
    
    /**
     * 产生从start到end时间段包含的完整周期IDs.(start,end]
     * start不能小于基准时间，否则取基准时间为start
     * @param start 起始日期
     * @param end 结束日期
     * @param cycleType 周期类型
     * @param cycleLength 周期长度
     * @return 周期标识集合
     */
    public static List<String> generateCycleIDs(Date start, Date end, int cycleType, int cycleLength)
    {
        Date startDefault = start;
        List<String> cycldIDLst = new ArrayList<String>();
        Calendar cStart = Calendar.getInstance();
        Calendar cEnd = Calendar.getInstance();
        
        //start不能小于基准时间
        if (startDefault.before(TccConfig.getBenchDate()))
        {
            startDefault = TccConfig.getBenchDate();
        }
        
        //如果开始时间晚于结束时间，直接退出
        if (startDefault.after(end))
        {
            return cycldIDLst;
        }
        
        //校准start为本周期起始点
        Date cycleStart = TccUtil.roll2CycleStartOnBenchDate(startDefault, cycleType, cycleLength, 0);
        
        cStart.setTime(cycleStart);
        cEnd.setTime(end);
        
        //24小时制
        SimpleDateFormat df = new SimpleDateFormat(TccConfig.CYCLE_ID_FORMAT);
        
        //如果start是周期起点，就先添加该周期ID
        if (cycleStart.equals(startDefault))
        {
            cycldIDLst.add(df.format(startDefault));
        }
        
        while (true)
        {
            cStart.add(CycleType.toCalendarType(cycleType), cycleLength);
            if (cStart.after(cEnd))
            {
                break;
            }
            //YYYYMMDD-hh 
            cycldIDLst.add(df.format(cStart.getTime()));
        }
        return cycldIDLst;
    }
    
    /** 
     * 获取上一 依赖周期Id
     * @param curTime 当前时间
     * @param cycleType 周期类型
     * @param cycleLen 周期长度
     * @param depCycleType 依赖的周期类型
     * @param depCycleLen 依赖的周期长度
     * @return 上一 依赖周期Id
     */
    public static String getPrevCycleId(Date curTime, int cycleType, int cycleLen, int depCycleType, int depCycleLen)
    {
        Date priviousDate;
        int compareValue;
        //周期大小比较
        compareValue = cycleType * cycleLen - depCycleType * depCycleLen;
        
        if (compareValue > 0)
        {
            //如果大周期类型依赖与小周期类型
            
            //计算出每一个依赖任务的上一个周期ID
            //大周期依赖的小周期的上一个周期，是指上一个大周期内的启动的最后一个小周期
            priviousDate = TccUtil.roll2CycleStartOnBenchDateQuick(curTime, depCycleType, depCycleLen, 0);
            Date priviousDate2 = TccUtil.roll2CycleStartOnBenchDate(curTime, depCycleType, depCycleLen, 0);
            if (!priviousDate.equals(priviousDate2))
            {
                LOGGER.error("----------------------------------------{},{},{},{},{}", new Object[] {curTime,
                    cycleType, cycleLen, depCycleType, depCycleLen});
            }
            if (curTime.equals(priviousDate))
            {
                priviousDate = TccUtil.roll2CycleStart(curTime, depCycleType, -depCycleLen);
            }
        }
        else
        {
            //如果小周期类型依赖于大周期类型，相同周期依赖
            //计算出每一个依赖任务的上一个周期ID
            //小周期依赖的大周期的上一个周期，是指小周期所在大周期的上一个大周期
            priviousDate = TccUtil.roll2CycleStartOnBenchDateQuick(curTime, depCycleType, depCycleLen, -1);
            Date priviousDate2 = TccUtil.roll2CycleStartOnBenchDate(curTime, depCycleType, depCycleLen, -1);
            if (!priviousDate.equals(priviousDate2))
            {
                LOGGER.error("----------------------------------------{},{},{},{},{}", new Object[] {curTime,
                    cycleType, cycleLen, depCycleType, depCycleLen});
            }
        }
        
        return TccUtil.covDate2CycleID(priviousDate);
    }
    
    /**
     * 将日期转换为周期类型对应的字符串（截取方式）
     * 
     * @param date 日期
     * @param cycleType 周期类型
     * @return 将日期转换为周期类型对应的字符串（截取方式）
     */
    public static String covDate2CycleTypeString(Date date, Integer cycleType)
    {
        if (null == date)
        {
            return null;
        }
        String formatString;
        switch (cycleType)
        {
            case CycleType.YEAR:
                formatString = "yyyy";
                break;
            case CycleType.MONTH:
                formatString = "yyyyMM";
                break;
            case CycleType.DAY:
                formatString = "yyyyMMdd";
                break;
            case CycleType.MINUTE:
                formatString = "yyyyMMddHHmm";
                break;
            default:
                formatString = "yyyyMMddHH";
                break;
        }
        
        DateFormat df = new SimpleDateFormat(formatString);
        df.setLenient(false);
        return df.format(date);
    }
    
    /**
     * 获取时间上满足运行条件的最大的周期Id
     * @param curDate 当前时间
     * @param cycleOffSet 周期偏移
     * @return 时间可以运行的最大周期Id
     * @throws CException 异常
     */
    public static String maxCycleIdTimeisOK(Date curDate, String cycleOffSet)
        throws CException
    {
        Date currentDate;
        if (null == curDate)
        {
            currentDate = CommonUtils.getCrruentTime();
        }
        else
        {
            currentDate = curDate;
        }
        
        //cycleOffSet 形如：xMxD xhxm
        String[] cycleOffSetArr = new String[1];
        cycleOffSetArr[0] = cycleOffSet;
        //获取偏移月数
        int mouths = TccUtil.extractNum(cycleOffSetArr, "M");
        //获取偏移天数
        int days = TccUtil.extractNum(cycleOffSetArr, "D");
        //获取偏移小时数
        int hours = TccUtil.extractNum(cycleOffSetArr, "h");
        //获取偏移分钟数
        
        int minutes = TccUtil.extractNum(cycleOffSetArr, "m");
        
        Calendar cDate = Calendar.getInstance();
        
        //将周期加上周期偏移
        cDate.setTime(currentDate);
        cDate.add(Calendar.MONTH, -mouths);
        cDate.add(Calendar.DAY_OF_MONTH, -days);
        cDate.add(Calendar.HOUR_OF_DAY, -hours);
        cDate.add(Calendar.MINUTE, -minutes);
        
        return TccUtil.covDate2CycleID(cDate.getTime());
    }
    
    /**
     * 获取周期偏移指定间隔之后的时间
     * @param cycleId 周期Id
     * @param cycleOffSet 周期偏移
     * @return 周期偏移指定间隔之后的时间
     * @throws CException 异常
     */
    public static Date canRunningTime(String cycleId, String cycleOffSet)
        throws CException
    {
        Date cycleDate = covCycleID2Date(cycleId);
        
        //cycleOffSet 形如：xMxD xhxm
        String[] cycleOffSetArr = new String[1];
        cycleOffSetArr[0] = cycleOffSet;
        //获取偏移月数
        int mouths = TccUtil.extractNum(cycleOffSetArr, "M");
        //获取偏移天数
        int days = TccUtil.extractNum(cycleOffSetArr, "D");
        //获取偏移小时数
        int hours = TccUtil.extractNum(cycleOffSetArr, "h");
        //获取偏移分钟数
        
        int minutes = TccUtil.extractNum(cycleOffSetArr, "m");
        
        Calendar cDate = Calendar.getInstance();
        
        //将周期加上周期偏移
        cDate.setTime(cycleDate);
        cDate.add(Calendar.MONTH, mouths);
        cDate.add(Calendar.DAY_OF_MONTH, days);
        cDate.add(Calendar.HOUR_OF_DAY, hours);
        cDate.add(Calendar.MINUTE, minutes);
        
        return cDate.getTime();
    }
    
    /**
     * 为time加上时间偏移
     * @param time 时间
     * @param offSet 偏移,形如(xMxD xhxm)
     * @return 时间可以运行的最大周期Id
     * @throws CException 异常
     */
    public static Date addTimeOffSet(Date time, String offSet)
        throws CException
    {
        if (null == time)
        {
            return null;
        }
        //cycleOffSet 形如：xMxD xhxm
        
        String[] cycleOffSetArr = new String[1];
        cycleOffSetArr[0] = offSet;
        //获取偏移月数
        int mouths = TccUtil.extractNum(cycleOffSetArr, "M");
        //获取偏移天数
        int days = TccUtil.extractNum(cycleOffSetArr, "D");
        //获取偏移小时数
        int hours = TccUtil.extractNum(cycleOffSetArr, "h");
        //获取偏移分钟数
        
        int minutes = TccUtil.extractNum(cycleOffSetArr, "m");
        
        Calendar cDate = Calendar.getInstance();
        
        //将周期加上周期偏移
        cDate.setTime(time);
        cDate.add(Calendar.MONTH, mouths);
        cDate.add(Calendar.DAY_OF_MONTH, days);
        cDate.add(Calendar.HOUR_OF_DAY, hours);
        cDate.add(Calendar.MINUTE, minutes);
        
        return cDate.getTime();
    }
    
    /**
     * 将keyValues中的键值对配置项保存到path指定的文件中
     * 
     * @param path 以bin作为相对目录的文件路径
     * @param keyValues 键值对配置集合
     * @throws Exception 异常
     */
    public static void saveConfigItem(String path, Map<String, String> keyValues)
        throws Exception
    {
        Properties cfgDefinition = new Properties();
        String configPath = TccSysConfigService.class.getResource("/").getFile() + path;
        BufferedReader confPath = null;
        try
        {
            confPath = new BufferedReader(new FileReader(new File(configPath)));
            cfgDefinition.load(confPath);
        }
        catch (Exception e)
        {
            LOGGER.error("read ConfigItem failed. configPath is {}", configPath, e);
            throw e;
        }
        finally
        {
            if (null != confPath)
            {
                try
                {
                    confPath.close();
                    
                }
                catch (IOException e)
                {
                    LOGGER.error("close BufferedReader failed.", e);
                }
            }
        }
        
        BufferedWriter confWriter = new BufferedWriter(new FileWriter(new File(configPath)));
        try
        {
            if (null != keyValues && !keyValues.isEmpty())
            {
                for (Entry<String, String> keyValue : keyValues.entrySet())
                {
                    cfgDefinition.setProperty(keyValue.getKey(), keyValue.getValue());
                }
            }
            cfgDefinition.store(confWriter, null);
        }
        catch (IOException e)
        {
            LOGGER.error("save Properties failed. configPath is {}", configPath, e);
            throw e;
        }
        finally
        {
            if (null != confWriter)
            {
                confWriter.close();
            }
        }
    }
    
    /**
     * 读取配置文件中的属性集合
     * 
     * @param path 以bin作为相对目录的文件路径
     * @return 配置文件中的属性集合
     * @throws Exception 异常
     */
    public static Properties loadConfigItem(String path)
        throws Exception
    {
        Properties cfgDefinition = new Properties();
        String configPath = TccSysConfigService.class.getResource("/").getFile() + path;
        BufferedReader confPath = null;
        try
        {
            confPath = new BufferedReader(new FileReader(new File(configPath)));
            cfgDefinition.load(confPath);
        }
        catch (Exception e)
        {
            LOGGER.error("read ConfigItem failed. configPath is {}", configPath, e);
            throw e;
        }
        finally
        {
            if (null != confPath)
            {
                try
                {
                    confPath.close();
                    
                }
                catch (IOException e)
                {
                    LOGGER.error("close BufferedReader failed.", e);
                }
            }
        }
        
        return cfgDefinition;
    }
    
    /**
     * 将日期调整n个周期类型对应的（年、月、天、时）
     * @param date 日期
     * @param cycleType 周期类型
     * @param n n个（年、月、天、时）
     * @return 将日期调整n个周期类型对应的（年、月、天、时）
     */
    public static Date tuneDate(Date date, Integer cycleType, Integer n)
    {
        if (null == n || null == date)
        {
            return date;
        }
        
        Calendar cDate = Calendar.getInstance();
        cDate.setTime(date);
        cDate.add(CycleType.toCalendarType(cycleType), n);
        return cDate.getTime();
    }
    
    /**
     * 将curTime向左偏移数个周期，直到找到第一个大于leftEdgeTime的时间
     * @param curTime 当前时间
     * @param leftEdgeTime 左边界时间,必需不能超过curTime
     * @param cycleType 周期类型
     * @param cycleLength 周期长度
     * @return 将curTime向左偏移数个周期，直到找到第一个大于leftEdgeTime的时间
     */
    public static Date roll2FirstAfterLeftEdge(Date curTime, Date leftEdgeTime, int cycleType, int cycleLength)
    {
        //时间相同，直接返回
        if (curTime.equals(leftEdgeTime))
        {
            return curTime;
        }
        
        if (curTime.before(leftEdgeTime))
        {
            return null;
        }
        
        Calendar cTime = Calendar.getInstance();
        cTime.setTime(curTime);
        
        while (!cTime.getTime().before(leftEdgeTime))
        {
            cTime.add(CycleType.toCalendarType(cycleType), -cycleLength);
        }
        //cTime此时一定小于左边界
        //在加一个周期使其处于右边界
        cTime.add(CycleType.toCalendarType(cycleType), cycleLength);
        
        return cTime.getTime();
    }
    
    /**
     * 将date调整到周期起始点(通常要求time为周期起点),然后让date继续偏移cycleOffSet个周期
     * @param time 时间
     * @param cycleType 周期类型
     * @param cycleOffSet 偏移的周期数，可为负值
     * @return cycleOffSet偏移周期量的周期起始点时间
     * @see [类、类#方法、类#成员]
     */
    public static Date roll2CycleStart(Date time, int cycleType, int cycleOffSet)
    {
        Calendar cTime = Calendar.getInstance();
        cTime.setTime(time);
        
        Calendar cal = Calendar.getInstance();
        //保证cTime与cal在秒以下的时间是相同的
        cal.setTime(time);
        switch (cycleType)
        {
            case CycleType.YEAR:
            {
                //1月1日 0点0分0秒
                int year = cTime.get(CycleType.toCalendarType(cycleType));
                cal.set(year, 0, 1, 0, 0, 0);
                cal.add(Calendar.YEAR, cycleOffSet);
                break;
            }
            case CycleType.MONTH:
            {
                //1日 0点0分0秒
                int year = cTime.get(Calendar.YEAR);
                int month = cTime.get(Calendar.MONTH);
                cal.set(year, month, 1, 0, 0, 0);
                cal.add(Calendar.MONTH, cycleOffSet);
                break;
            }
            case CycleType.DAY:
            {
                //0点0分0秒
                int year = cTime.get(Calendar.YEAR);
                int month = cTime.get(Calendar.MONTH);
                int day = cTime.get(Calendar.DAY_OF_MONTH);
                cal.set(year, month, day, 0, 0, 0);
                cal.add(Calendar.DAY_OF_MONTH, cycleOffSet);
                break;
            }
            case CycleType.MINUTE:
            {
                //0秒
                int year = cTime.get(Calendar.YEAR);
                int month = cTime.get(Calendar.MONTH);
                int day = cTime.get(Calendar.DAY_OF_MONTH);
                int hour = cTime.get(Calendar.HOUR_OF_DAY);
                int minute = cTime.get(Calendar.MINUTE);
                cal.set(year, month, day, hour, minute, 0);
                cal.add(Calendar.MINUTE, cycleOffSet);
                break;
            }
            default:
            {
                //0分0秒
                int year = cTime.get(Calendar.YEAR);
                int month = cTime.get(Calendar.MONTH);
                int day = cTime.get(Calendar.DAY_OF_MONTH);
                int hour = cTime.get(Calendar.HOUR_OF_DAY);
                cal.set(year, month, day, hour, 0, 0);
                cal.add(Calendar.HOUR_OF_DAY, cycleOffSet);
                break;
            }
            
        }
        return cal.getTime();
    }
    
    /**
     * 将date调整到基于基准时间的周期起始点,然后让date继续偏移OffSet个周期
     * @param time 时间
     * @param cycleType 周期类型
     * @param cycleLength 周期的长度
     * @param offset 偏移offset个周期
     * @return 基于基准时间的周期起始点
     */
    public static Date roll2CycleStartOnBenchDateQuick(Date time, int cycleType, int cycleLength, int offset)
    {
        Calendar cTime = Calendar.getInstance();
        cTime.setTime(time);
        //周期的基准时间
        Date cycleBenchTime = roll2CycleStart(TccConfig.getBenchDate(), cycleType, 0);
        Calendar cBenchTime = Calendar.getInstance();
        cBenchTime.setTime(cycleBenchTime);
        
        int calendarType = CycleType.toCalendarType(cycleType);
        
        if (cBenchTime.before(cTime))
        {
            //天、小时、分钟类型不要循环遍历
            if (CycleType.MINUTE == cycleType)
            {
                long dur = MILLSECONDE_PER_MINUTE * cycleLength;
                long dis = time.getTime() - cycleBenchTime.getTime();
                int cycles = (int)(dis / dur) + offset;
                cBenchTime.add(calendarType, cycles * cycleLength);
            }
            else if (CycleType.DAY == cycleType)
            {
                long dur = MILLSECONDE_PER_DAY * cycleLength;
                long dis = time.getTime() - cycleBenchTime.getTime();
                int cycles = (int)(dis / dur) + offset;
                cBenchTime.add(calendarType, cycles * cycleLength);
            }
            else if (CycleType.HOUR == cycleType)
            {
                long dur = MILLSECONDE_PER_HOUR * cycleLength;
                long dis = time.getTime() - cycleBenchTime.getTime();
                int cycles = (int)(dis / dur) + offset;
                cBenchTime.add(calendarType, cycles * cycleLength);
            }
            else
            {
                while (cTime.after(cBenchTime))
                {
                    cBenchTime.add(calendarType, cycleLength);
                }
                
                if (!cBenchTime.equals(cTime))
                {
                    //如果cBenchTime在cTime之后，需要往前移动一个周期
                    cBenchTime.add(calendarType, -cycleLength);
                }
                
                //偏移offset个周期
                cBenchTime.add(calendarType, offset * cycleLength);
            }
        }
        else if (cTime.before(cBenchTime))
        {
            //天、小时、分钟类型不要循环遍历
            if (CycleType.MINUTE == cycleType)
            {
                long dur = MILLSECONDE_PER_MINUTE * cycleLength;
                long dis = cycleBenchTime.getTime() - time.getTime();
                int cycles = offset - (int)(dis / dur);
                cBenchTime.add(calendarType, cycles * cycleLength);
            }
            else if (CycleType.DAY == cycleType)
            {
                long dur = MILLSECONDE_PER_DAY * cycleLength;
                long dis = cycleBenchTime.getTime() - time.getTime();
                int cycles = offset - (int)(dis / dur);
                cBenchTime.add(calendarType, cycles * cycleLength);
            }
            else if (CycleType.HOUR == cycleType)
            {
                long dur = MILLSECONDE_PER_HOUR * cycleLength;
                long dis = cycleBenchTime.getTime() - time.getTime();
                int cycles = offset - (int)(dis / dur);
                cBenchTime.add(calendarType, cycles * cycleLength);
            }
            else
            {
                //获取第一个小于等于cTime周期起始时间
                while (cBenchTime.after(cTime))
                {
                    cBenchTime.add(calendarType, -cycleLength);
                }
                
                //偏移offset个周期
                cBenchTime.add(calendarType, offset * cycleLength);
            }
        }
        else
        {
            //偏移offset个周期
            cBenchTime.add(calendarType, offset * cycleLength);
        }
        
        return cBenchTime.getTime();
    }
    
    /**
     * 将date调整到基于基准时间的周期起始点,然后让date继续偏移OffSet个周期
     * @param time 时间
     * @param cycleType 周期类型
     * @param cycleLength 周期的长度
     * @param offset 偏移offset个周期
     * @return 基于基准时间的周期起始点
     * @see [类、类#方法、类#成员]
     */
    public static Date roll2CycleStartOnBenchDate(Date time, int cycleType, int cycleLength, int offset)
    {
        Calendar cTime = Calendar.getInstance();
        cTime.setTime(time);
        //周期的基准时间
        Date cycleBenchTime = roll2CycleStart(TccConfig.getBenchDate(), cycleType, 0);
        Calendar cBenchTime = Calendar.getInstance();
        cBenchTime.setTime(cycleBenchTime);
        
        int calendarType = CycleType.toCalendarType(cycleType);
        
        if (cBenchTime.before(cTime))
        {
            while (cTime.after(cBenchTime))
            {
                cBenchTime.add(calendarType, cycleLength);
            }
            
            if (!cBenchTime.equals(cTime))
            {
                //如果cBenchTime在cTime之后，需要往前移动一个周期
                cBenchTime.add(CycleType.toCalendarType(cycleType), -cycleLength);
            }
        }
        else if (cTime.before(cBenchTime))
        {
            //获取第一个小于等于cTime周期起始时间
            while (cBenchTime.after(cTime))
            {
                cBenchTime.add(calendarType, -cycleLength);
            }
        }
        
        //偏移offset个周期
        cBenchTime.add(calendarType, offset * cycleLength);
        return cBenchTime.getTime();
    }
    
    /**
     * 将日期转换为周期Id（截取方式）
     * 
     * @param date 日期
     * @return 周期Id
     */
    public static String covDate2CycleID(Date date)
    {
        if (null == date)
        {
            return null;
        }
        
        DateFormat df = new SimpleDateFormat(TccConfig.CYCLE_ID_FORMAT);
        df.setLenient(false);
        return df.format(date);
    }
    
    /**
     * 将周期Id转换为日期
     * 
     * @param cycleID 周期Id
     * @return 日期
     * @throws CException 统一封装的异常
     */
    public static Date covCycleID2Date(String cycleID)
        throws CException
    {
        if (null == cycleID)
        {
            return null;
        }
        
        DateFormat df = new SimpleDateFormat(TccConfig.CYCLE_ID_FORMAT);
        df.setLenient(false);
        
        try
        {
            return df.parse(cycleID);
        }
        catch (ParseException e)
        {
            LOGGER.error("parse cycleid to date failed, cycleid is [{}]!", TccUtil.truncatEncode(cycleID), e);
            throw new CException(ResultCode.PARSE_CYCLEID_DATE_ERROR, cycleID);
        }
    }
    
    /**
     * 密码是否加密
     * @param password 密码是否加密
     * @return 是否加密
     */
    public static boolean canDecrypt(String password)
    {
        boolean crypt = true;
        if (StringUtils.isBlank(password))
        {
            crypt = false;
        }
        else
        {
            try
            {
                CryptUtilforcfg.decryptForAESStr(password.trim(), keyword);
            }
            catch (Exception e)
            {
                crypt = false;
            }
        }
        
        return crypt;
    }
    
    /**
     * 加密
     * @param password 对密码进行加密
     * @return 加密
     */
    public static String encrypt(String password)
    {
        if (StringUtils.isNotBlank(password))
        {
            return CryptUtilforcfg.encryptToAESStr(password.trim(), keyword);
        }
        
        return null;
    }
    
    /**
     * 获取邮件发送器
     * @return 邮件发送器
     */
    public static JavaMailSenderImpl getJavaMailSender()
    {
        Object obj = CommonUtils.getBeanByID("javaMailSender");
        
        if (null != obj)
        {
            return (JavaMailSenderImpl)obj;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 获取tccDao对象
     * 
     * @return tccDao对象
     */
    public static TccDao getTccDao()
    {
        Object obj = CommonUtils.getBeanByID("tccDao");
        
        if (null != obj)
        {
            return (TccDao)obj;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 判断周期Id是否合法
     * @param cycleId 周期Id
     * @return 周期Id是否合法
     */
    public static boolean isCorrect(String cycleId)
    {
        //检查cycleId是否合法
        if (!StringUtils.isEmpty(cycleId))
        {
            try
            {
                TccUtil.covCycleID2Date(cycleId);
            }
            catch (Exception e)
            {
                return false;
            }
            
        }
        else
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * 从cycleOffSet中提取出数量值,splitChar必需是cycleOffSet中出现的首个非数值字符
     * 
     * @param cycleOffSet 周期偏移数组，只有第一个元素起作用，主要用来传出子串
     * @param splitChar 只能是“M”、“D”、“h”、“m”
     * @return 提取的数值，通过cycleOffSet返回截取后剩余的子串
     */
    public static int extractNum(String[] cycleOffSet, String splitChar)
    {
        int num = 0;
        if (!StringUtils.isEmpty(cycleOffSet[0]))
        {
            cycleOffSet[0] = cycleOffSet[0].trim();
            int mIndex = cycleOffSet[0].indexOf(splitChar);
            //存在月份
            if (-1 != mIndex)
            {
                try
                {
                    num = Integer.parseInt(cycleOffSet[0].substring(0, mIndex).trim());
                }
                catch (NumberFormatException nfe)
                {
                    LOGGER.debug("extract num from [{}] by splitChar [{}] failed!", new Object[] {cycleOffSet,
                        splitChar}, nfe);
                }
                cycleOffSet[0] = cycleOffSet[0].substring(mIndex + 1);
            }
        }
        return num;
    }
    
    /** 
     * 解析任务依赖依赖关系
     *
     * @param taskId 任务Id
     * @param dependTasks 依赖任务ID列表
     * @param dependRelationLst 返回的依赖关系集合
     * @param taskIds 返回的任务Id集合
     * @throws CException 异常
     */
    public static void parseDependIdList(long taskId, String dependTasks,
        HashMap<Long, DependRelation> dependRelationLst, List<Long> taskIds)
        throws CException
    {
        //存在依赖任务
        String[] idDependErrs = dependTasks.split(";");
        Long dependTaskId;
        boolean isfullCycleDepend;
        boolean isIgnoreErr;
        
        DependRelation dependRelation;
        for (String idDepErr : idDependErrs)
        {
            if (!StringUtils.isBlank(idDepErr))
            {
                String[] idDependErrArr = idDepErr.split(",");
                
                if (idDependErrArr.length > DependRelation.IGNORE_INDEX)
                {
                    try
                    {
                        dependTaskId = Long.parseLong(idDependErrArr[DependRelation.ID_INDEX]);
                    }
                    catch (NumberFormatException e)
                    {
                        LOGGER.error("dependRelation({}) of task({}) error,"
                            + " please correct it[eg:task_id,depend_full_cycle(0 or 1),ignore_err(0 or 1)...]!",
                            new Object[] {idDepErr, taskId, e});
                        throw new CException(ResultCode.TASK_DEPEND_REALATION_ERROR, new Object[] {idDepErr, taskId});
                    }
                    
                    isfullCycleDepend = "1".endsWith(idDependErrArr[DependRelation.FULLDEPEND_INDEX]);
                    isIgnoreErr = "1".endsWith(idDependErrArr[DependRelation.IGNORE_INDEX]);
                    
                    dependRelation = new DependRelation(dependTaskId, isfullCycleDepend, isIgnoreErr);
                    dependRelationLst.put(dependTaskId, dependRelation);
                    taskIds.add(dependTaskId);
                }
                else
                {
                    LOGGER.error("dependRelation({}) of task({}) error,"
                        + " please correct it[eg:task_id,depend_full_cycle(0 or 1),ignore_err(0 or 1)...]!",
                        new Object[] {idDepErr, taskId});
                    throw new CException(ResultCode.TASK_DEPEND_REALATION_ERROR, idDepErr, taskId);
                }
            }
        }
    }
    
    /** 
     * 从dependTasks中筛选出taskId对应的依赖关系
     *
     * @param taskId 任务Id
     * @param dependTasks 依赖任务ID列表
     * @return 依赖关系
     * @throws CException 异常
     */
    public static DependRelation filterDepRs(Long taskId, String dependTasks)
        throws CException
    {
        if (null == dependTasks || null == taskId)
        {
            return null;
        }
        
        //存在依赖任务
        String[] idDependErrs = dependTasks.split(";");
        Long dependTaskId;
        boolean isfullCycleDepend;
        boolean isIgnoreErr;
        
        for (String idDepErr : idDependErrs)
        {
            if (!StringUtils.isEmpty(idDepErr))
            {
                String[] idDependErrArr = idDepErr.split(",");
                
                if (idDependErrArr.length > DependRelation.IGNORE_INDEX)
                {
                    try
                    {
                        dependTaskId = Long.parseLong(idDependErrArr[DependRelation.ID_INDEX]);
                    }
                    catch (NumberFormatException e)
                    {
                        LOGGER.error("dependRelation({}) of task({}) error,"
                            + " please correct it[eg:task_id,depend_full_cycle(0 or 1),ignore_err(0 or 1)...]!",
                            new Object[] {idDepErr, taskId, e});
                        throw new CException(ResultCode.TASK_DEPEND_REALATION_ERROR, new Object[] {idDepErr, taskId});
                    }
                    
                    isfullCycleDepend = "1".endsWith(idDependErrArr[DependRelation.FULLDEPEND_INDEX]);
                    isIgnoreErr = "1".endsWith(idDependErrArr[DependRelation.IGNORE_INDEX]);
                    
                    //找到直接返回
                    if (dependTaskId.equals(taskId))
                    {
                        return new DependRelation(dependTaskId, isfullCycleDepend, isIgnoreErr);
                    }
                }
                else
                {
                    LOGGER.error("dependRelation({}) of task({}) error,"
                        + " please correct it[eg:task_id,depend_full_cycle(0 or 1),ignore_err(0 or 1)...]!",
                        new Object[] {idDepErr, taskId});
                    throw new CException(ResultCode.TASK_DEPEND_REALATION_ERROR, idDepErr, taskId);
                }
            }
        }
        return null;
    }
    
    /**
     * 给邮箱列表发送邮件
     * @param emailsTo 目的地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param from 邮件发件人（显示名）
     * @throws Exception 邮件发送异常
     * 
     */
    public static void sendEmail(String from, final String emailsTo, final String subject, final String content)
        throws Exception
    {
        LOGGER.debug("enter sendEmail...param is [from={},mailsTo={},subject={},content={}]", new Object[] {from,
            emailsTo, subject, content});
        if (TccConfig.getIsSendEmail())
        {
            if (!StringUtils.isBlank(emailsTo))
            {
                final JavaMailSenderImpl mailSender = getJavaMailSender();
                final MimeMessage mailmessage = mailSender.createMimeMessage();
                final MimeMessageHelper messageHelper = new MimeMessageHelper(mailmessage);
                String mailfrom = from;
                if (StringUtils.isBlank(mailfrom))
                {
                    mailfrom = mailSender.getUsername();
                }
                
                messageHelper.setFrom(mailSender.getUsername(), TccConfig.getEmailFrom());
                messageHelper.setSubject(subject);
                messageHelper.setText(content, true);
                
                //向邮件列表发送邮件
                String[] mails = emailsTo.split(";");
                StringBuilder realMails = new StringBuilder();
                try
                {
                    Pattern pattern =
                        Pattern.compile("^\\+?[a-z0-9](([-+.]|[_]+)?[a-z0-9]+)*@([a-z0-9]+(\\.|\\-))+[a-z]{2,6}$");
                    Matcher matcher;
                    for (String mail : mails)
                    {
                        if (StringUtils.isBlank(mail))
                        {
                            continue;
                        }
                        
                        //是邮件格式
                        matcher = pattern.matcher(mail.trim());
                        if (matcher.matches())
                        {
                            realMails.append(mail.trim());
                            realMails.append(';');
                            messageHelper.addTo(mail.trim());
                        }
                    }
                    
                    if (!StringUtils.isEmpty(realMails.toString()))
                    {
                        mailSender.send(mailmessage);
                        LOGGER.debug("send mail to {} from {} successfully, " + "mail subject is {},mail content is {}",
                            new Object[] {realMails.toString(), mailfrom, subject, content});
                    }
                    else
                    {
                        LOGGER.error("don't send any email, because valid mailsTo is empty! from is {}, mail subject "
                            + "is {},mail content is {}",
                            new Object[] {TccUtil.truncatEncode(mailfrom), TccUtil.truncatEncode(subject),
                                TccUtil.truncatEncode(content)});
                    }
                    
                }
                catch (Exception e)
                {
                    LOGGER.error("send mail to {} from {} failed, " + "mail subject is {},mail content is {}",
                        new Object[] {TccUtil.truncatEncode(realMails.toString()), TccUtil.truncatEncode(mailfrom),
                            TccUtil.truncatEncode(subject), TccUtil.truncatEncode(content), e});
                }
            }
            else
            {
                LOGGER.info("don't send any email, because mailsTo is empty. from is {}, "
                    + "mail subject is {},mail content is {}",
                    new Object[] {TccUtil.truncatEncode(from), TccUtil.truncatEncode(subject),
                        TccUtil.truncatEncode(content)});
            }
        }
        LOGGER.debug("exit sendEmail...param is [from={},mailsTo={},subject={},content={}]", new Object[] {from,
            emailsTo, subject, content});
    }
    
    /**
     * 格式化输出日志记录列表
     * @param logList 日志记录列表
     * @return 日志记录列表格式化字符串
     */
    public static String format(List<Log2DBEntity> logList)
    {
        StringBuilder sbLogs = new StringBuilder();
        if (null != logList && !logList.isEmpty())
        {
            sbLogs.append("<table style='margin-left: auto; margin-right: auto;width:800px;font-weight:bold;'><tr>");
            sbLogs.append("<td>时间</td><td>级别</td>");
            sbLogs.append("<td>操作员</td><td>访问者IP</td>");
            sbLogs.append("<td>方法名</td><td>线程名</td>");
            sbLogs.append("<td>详细日志信息</td>");
            sbLogs.append("</tr></table>");
            String replaceTaskCycleId;
            try
            {
                for (Log2DBEntity log2Db : logList)
                {
                    sbLogs.append(log2Db.getCreateTime());
                    sbLogs.append("&nbsp;");
                    formatLevel(log2Db.getLogLevel(), sbLogs);
                    
                    sbLogs.append("&nbsp;");
                    if (!StringUtils.isBlank(log2Db.getUserName()))
                    {
                        sbLogs.append(log2Db.getUserName());
                    }
                    else
                    {
                        sbLogs.append("-");
                    }
                    
                    sbLogs.append("&nbsp;");
                    if (!StringUtils.isBlank(log2Db.getClientIp()))
                    {
                        
                        sbLogs.append(log2Db.getClientIp());
                    }
                    else
                    {
                        sbLogs.append("-");
                    }
                    
                    sbLogs.append("&nbsp;");
                    sbLogs.append("<font color='green'>");
                    if (!StringUtils.isBlank(log2Db.getMethodName()))
                    {
                        
                        sbLogs.append(log2Db.getMethodName().replace("<", "&lt;").replace(">", "&gt;"));
                        sbLogs.append("()");
                    }
                    else
                    {
                        sbLogs.append("-");
                    }
                    sbLogs.append("</font>");
                    
                    sbLogs.append("&nbsp;");
                    if (!StringUtils.isBlank(log2Db.getThreadName()))
                    {
                        if (!StringUtils.isBlank(log2Db.getTaskId()) && !StringUtils.isBlank(log2Db.getCycleId()))
                        {
                            replaceTaskCycleId = log2Db.getTaskId() + "_" + log2Db.getCycleId();
                            sbLogs.append(log2Db.getThreadName().replace(replaceTaskCycleId,
                                String.format("<a target='_blank' href='TaskRunningDetail.jsp?taskId=%s&cycleId=%s'>"
                                    + "<font color='#00aa00'>%s</font></a>",
                                    log2Db.getTaskId(),
                                    log2Db.getCycleId(),
                                    replaceTaskCycleId)));
                        }
                        else if (!StringUtils.isBlank(log2Db.getTaskId()))
                        {
                            sbLogs.append(log2Db.getThreadName().replace(log2Db.getTaskId(),
                                String.format("<a target='_blank' href='TaskList.jsp?taskId=%s'>"
                                    + "<font color='#00aa00'>%s</font></a>", log2Db.getTaskId(), log2Db.getTaskId())));
                        }
                        else
                        {
                            sbLogs.append(log2Db.getThreadName());
                        }
                    }
                    else
                    {
                        sbLogs.append("-");
                    }
                    sbLogs.append("&nbsp;");
                    sbLogs.append("<font color='green'>");
                    if (!StringUtils.isBlank(log2Db.getMessage()))
                    {
                        sbLogs.append(log2Db.getMessage()
                            .replace("&", "&amp;")
                            .replace("<", "&lt;")
                            .replace(">", "&gt;")
                            .replace("\'", "&apos;")
                            .replace("\"", "&quot;")
                            .replace("\0", "\\0")
                            .replace("\n\r", "</font><br/><font color='green'>")
                            .replace("\n", "</font><br/><font color='green'>")
                            .replace("\t", "&emsp;"));
                        sbLogs.append("</font>");
                    }
                    else
                    {
                        sbLogs.append("-</font><br/>");
                    }
                    
                }
            }
            catch (Exception e)
            {
                LOGGER.error("format fail!", e);
            }
        }
        return sbLogs.toString();
    }
    
    private static void formatLevel(String level, StringBuilder sbLogs)
    {
        String color;
        if ("FATAL".endsWith(level))
        {
            color = "#FF0000";
        }
        else if ("ERROR".endsWith(level))
        {
            color = "DD0000";
        }
        else if ("WARN".endsWith(level))
        {
            color = "aaaa00";
        }
        //        else if ("INFO".endsWith(level))
        //        {
        //            color = "00aa00";
        //        }
        else
        {
            color = "00aa00";
        }
        
        sbLogs.append("<font color='");
        sbLogs.append(color);
        sbLogs.append("'>");
        sbLogs.append(level);
        sbLogs.append("</font>");
    }
    
    /**
     * 将单引号括起来的json字符串转换成双引号(通常fastjson转换有问题，需要经过处理)
     * @param jsonSimpleQuote 单引号括起来的json字符串
     * @return  将单引号括起来的json字符串转换成双引号
     */
    public static String replace2Quotes(String jsonSimpleQuote)
    {
        
        if (!StringUtils.isEmpty(jsonSimpleQuote))
        {
            StringBuilder sb = new StringBuilder(jsonSimpleQuote);
            char preChar = sb.charAt(0);
            //num为转义字符同时出现的个数
            int num = 0;
            int modeN = 1;
            //2为模数
            modeN++;
            
            if ('\'' == preChar)
            {
                sb.setCharAt(0, '"');
            }
            
            for (int i = 1; i < sb.length(); i++)
            {
                //将不以转义字符\开头的'替换成""
                if ('\\' == preChar)
                {
                    num = (num + 1) % modeN;
                }
                else
                {
                    num = 0;
                }
                
                if ('\'' == sb.charAt(i))
                {
                    if (num == 0)
                    {
                        sb.setCharAt(i, '"');
                    }
                    //为保证兼容ie与firefox，需要将 "d\'b" 替换成  "d'b" ,ie可以正确识别"\'"，但是firefox不行
                    else
                    {
                        //删除掉'\''中的\'替换成'
                        sb.deleteCharAt(i - 1);
                        i--;
                    }
                }
                
                preChar = sb.charAt(i);
            }
            return sb.toString();
        }
        else
        {
            return jsonSimpleQuote;
        }
    }
    
    /**
     * 向url通知信息，传递obj参数
     * @param url 请求地址
     * @param obj 对象
     */
    public static void notify(String url, Object obj)
    {
        HttpResponse httprsp = null;
        try
        {
            //转化为JSON字符串
            final String body = JSON.toJSONString(obj);
            
            //远程调用接口
            if (StringUtils.isBlank(url))
            {
                LOGGER.error("url is blank! req is {}", obj);
                return;
            }
            
            final HttpRequester httpreq = new HttpRequester();
            
            LOGGER.info("call {} startting, request is {}", url, body);
            
            //通过post方法调用回调url地址
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("Content-Type", "application/json; charset=UTF-8");
            properties.put("Accept", "application/json; charset=UTF-8");
            httprsp = httpreq.post(url, properties, body);
            
            LOGGER.info("call notify end, response is {}",
                TccUtil.truncatEncode(null == httprsp ? "" : httprsp.toString()));
            if (null == httprsp || HttpRequester.HTTP_OK != httprsp.getCode())
            {
                //HTTP响应异常
                LOGGER.error("not correct responce for called {}! request is {},response is {}", new Object[] {url,
                    obj, TccUtil.truncatEncode(null == httprsp ? "" : httprsp.toString())});
            }
        }
        catch (IOException e)
        {
            LOGGER.error("call httpreq.post failed! request is {},response is {}",
                new Object[] {obj, TccUtil.truncatEncode(null == httprsp ? "" : httprsp.toString()), e});
        }
        catch (Exception ex)
        {
            LOGGER.error("call httpreq.post failed! request is {},response is {}",
                new Object[] {obj, TccUtil.truncatEncode(null == httprsp ? "" : httprsp.toString()), ex});
        }
    }
    
    /**
     * 将cycleId转化为有效值
     * @param cycleId 周期Id
     * @return 有效的周期Id
     */
    public static String covValidCycleId(String cycleId)
    {
        String validData = "";
        if (StringUtils.isNotBlank(cycleId) && isCorrect(cycleId))
        {
            validData = new Encoder().truncatEncode(cycleId);
        }
        
        return validData;
    }
    
    /**
     * 将taskId转化为有效值
     * @param taskId 任务Id
     * @return 有效的任务Id
     */
    public static String covValidTaskId(String taskId)
    {
        String validData = "0";
        if (StringUtils.isNotBlank(taskId) && CheckParamUtils.checkTaskId(taskId))
        {
            validData = new Encoder().truncatEncode(taskId);
        }
        
        return validData;
    }
    
    /**
     * 将taskId转化为有效值
     * @param stepId 任务Id
     * @return 有效的任务Id
     */
    public static String covValidStepId(String stepId)
    {
        String validData = "";
        if (StringUtils.isNotBlank(stepId) && CheckParamUtils.checkStepId(stepId))
        {
            validData = new Encoder().truncatEncode(stepId);
        }
        
        return validData;
    }
    
    /**
     * 编码参数
     * @param text 文本
     * @return 编码参数
     */
    public static String truncatEncode(Object text)
    {
        return new Encoder().truncatEncode(text.toString());
    }
    
    /**
     * 编码参数
     * @param text 文本
     * @return 编码参数
     */
    public static String encodeForJS(String text)
    {
        try
        {
            return com.huawei.cct.websecurity.encoder.impl.Encoder.getInstance().encodeForJavascript(text.toString());
            
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("encodeForJS failed", e);
        }
        catch (EncodingException e)
        {
            LOGGER.error("encodeForJS failed", e);
        }
        
        return "";
    }
}