/*
 * 文 件 名:  UMUtil.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-29
 */
package com.huawei.platform.um.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.MessageFormat;
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
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.huawei.platform.common.CException;
import com.huawei.platform.common.CryptUtilforcfg;
import com.huawei.platform.common.ErrorDescConfigService;
import com.huawei.platform.um.constants.ResultCode;
import com.huawei.platform.um.constants.type.CycleType;
import com.huawei.platform.um.constants.type.NameStoredInSession;
import com.huawei.platform.um.domain.IModifyResult;
import com.huawei.platform.um.domain.ReturnExceptionInfo;
import com.opensymphony.xwork2.ActionContext;

/**
 * Um工具类
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-03-27]
 */
public class UMUtil
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
     * 每小时毫秒数
     */
    public static final long MILLSECONDE_PER_HOUR = 3600 * 1000;
    
    /**
     * 每天毫秒数
     */
    public static final long MILLSECONDE_PER_DAY = 24 * MILLSECONDE_PER_HOUR;
    
    /**
     * web应用程序上下文
     */
    private static WebApplicationContext webAppContext = ContextLoader.getCurrentWebApplicationContext();
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UMUtil.class);
    
    private static String keyword = "PkmJygVfrDxsDeeD";
    
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
        SysConfigService sysConfigService = (SysConfigService)getBeanByID("sysConfigService");
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
        String configPath = SysConfigService.class.getResource("/").getFile() + path;
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
        String configPath = SysConfigService.class.getResource("/").getFile() + path;
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
     * 获取指定xml文档的Document对象,xml文件必须在classpath中可以找到 
     * 
     * @param xmlFilePath xml文件路径 
     * @throws Exception 文件不存在异常，xml文档解析异常
     * @return Document对象 
     */
    public static org.dom4j.Document readDocument(String xmlFilePath)
        throws Exception
    {
        SAXReader reader = new SAXReader();
        org.dom4j.Document document = null;
        InputStream in = null;
        try
        {
            in = new FileInputStream(xmlFilePath);
            document = reader.read(in);
        }
        catch (FileNotFoundException fe)
        {
            LOGGER.error("{} does not exist in classpath. error is {}", xmlFilePath, fe);
            throw fe;
        }
        catch (DocumentException e)
        {
            LOGGER.error("format of {} is not correct. error is {}", xmlFilePath, e);
            throw e;
        }
        finally
        {
            if (null != in)
            {
                in.close();
            }
        }
        return document;
    }
    
    /**
     * 获取邮件发送器
     * @return 邮件发送器
     */
    public static JavaMailSenderImpl getJavaMailSender()
    {
        Object obj = UMUtil.getBeanByID("javaMailSender");
        
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
     * 根据异常码获取异常描述信息
     * @param errorCode 异常码
     * @param fmtObjects 待格式化的异常描述信息
     * @return 异常描述信息
     */
    public static String getErrorDesc(int errorCode, Object... fmtObjects)
    {
        //资源文件获取配置的异常信息
        String cfgErrorDesc = getErrorDescFromResource(errorCode);
        
        //如果未配置指定异常码对应的异常描述信息
        if (StringUtils.isBlank(cfgErrorDesc))
        {
            cfgErrorDesc = "Unkown exception or error occured.";
        }
        
        //没有需要合入并格式化的参数信息则直接返回资源文件定义的异常信息描述
        if ((null == fmtObjects) || (fmtObjects.length < 1))
        {
            return cfgErrorDesc;
        }
        
        return MessageFormat.format(cfgErrorDesc, fmtObjects);
    }
    
    /** 
     * 从错误码资源文件获取指定错误码对应的错误描述信息
     * @param errorCode 异常错误码
     * @return 配置的错误描述信息
     */
    private static String getErrorDescFromResource(int errorCode)
    {
        ErrorDescConfigService exDefServ = (ErrorDescConfigService)getBeanByID("exDefConfigService");
        
        return (null == exDefServ) ? null : exDefServ.getExceptionDesc(String.valueOf(errorCode));
    }
    
    /** 
     * 从错误码映射资源文件获取指定错误码映射的错误码
     * @param errorCode 异常错误码
     * @return 映射的错误码
     */
    public static int getErrorCodeMapper(int errorCode)
    {
        ErrorDescConfigService exDefServ = (ErrorDescConfigService)getBeanByID("exDefConfigService");
        
        return (null == exDefServ) ? -1 : exDefServ.getMappingExceptionCode(errorCode);
    }
    
    /**
     * 构造异常
     */
    public static Map<String, Object> buildCommonException(Exception e)
    {
        Map<String, Object> model;
        if (e instanceof CException)
        {
            ReturnExceptionInfo returnException =
                new ReturnExceptionInfo(((CException)e).getErrorCode(), ((CException)e).getErrorDesc());
            model = buildReturnException("CException", returnException);
        }
        else if (e instanceof JSONException)
        {
            ReturnExceptionInfo returnException =
                new ReturnExceptionInfo(ResultCode.REQUEST_FORMAT_ERROR, "request body format error");
            model = buildReturnException("CException", returnException);
        }
        
        else
        {
            ReturnExceptionInfo returnException =
                new ReturnExceptionInfo(ResultCode.SYSTEM_ERROR, "system error");
            model = buildReturnException("CException", returnException);
        }
        return model;
    }
    
    /**
     * <构造返回异常>
     * @param objName 待封装对象的名字
     * @param rspobj 待封装对象
     * @return model
     */
    public static Map<String, Object> buildReturnException(String objName, Object rspobj)
    {
        ReturnExceptionInfo re = (ReturnExceptionInfo)rspobj;
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("resultCode", String.valueOf(re.getErrorCode()));
        model.put(objName, re);
        return model;
    }
    
    /**
     * 构造返回对象
     * @param objName 对象名
     * @param rspobj 待封装对象
     * @return model
     */
    public static Map<String, Object> buildReturnObj(String objName, Object rspobj)
    {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("resultCode", "0");
        model.put(objName, rspobj);
        return model;
    }
    
    /**
     * 构造返回对象
     * @param objName 对象名
     * @param rspobj 待封装对象
     * @return model
     */
    public static Map<String, Object> buildReturnObj()
    {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("resultCode", "0");
        return model;
    }
}
