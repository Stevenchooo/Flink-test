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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.huawei.platform.common.CException;
import com.huawei.platform.common.CommonUtils;
import com.huawei.platform.common.CryptUtilforcfg;
import com.huawei.platform.common.exmapping.TccSysConfigService;
import com.huawei.platform.tcc.constants.ResultCodeConstants;
import com.huawei.platform.tcc.constants.TccConfig;
import com.huawei.platform.tcc.constants.type.CycleType;
import com.huawei.platform.tcc.constants.type.NameStoredInSession;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.DependRelation;
import com.huawei.platform.tcc.domain.LinuxUser;
import com.huawei.platform.tcc.entity.Log2DBEntity;
import com.huawei.platform.tcc.interfaces.Tcc2Shell;
import com.huawei.platform.tcc.interfaces.impl.Tcc2ShellImpl;
import com.opensymphony.xwork2.ActionContext;

/**
 * Tcc工具类
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-29]
 * @see  [相关类/方法]
 */
public class TccUtil
{
    /**
     * linux服务器ip
     */
    public static final String LINUX_IP = "ip";
    
    /**
     * linux服务器用户
     */
    public static final String LINUX_USER = "user";
    
    /**
     * linux服务器密码
     */
    public static final String LINUX_PASSWORD = "password";
    
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
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TccUtil.class);
    
    private static Tcc2Shell tcc2Shell = new Tcc2ShellImpl(getTccDao());
    
    private static String keyword = "PkmJygVfrDxsDeeD";
    
    /**
     * 配置文件所在的目录
     */
    private static final String XMLPATH = TccUtil.class.getResource("/").getFile();
    
    /**
     * systemconf文件存放的路径
     */
    private static final String SYS_CONFIGPATH = XMLPATH + "../conf/remoteshell/systemconf.xml";
    
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
        
        DateFormat df = new SimpleDateFormat("yyyyMMdd-HH");
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
        
        DateFormat df = new SimpleDateFormat("yyyyMMdd-HH");
        df.setLenient(false);
        
        try
        {
            return df.parse(cycleID);
        }
        catch (ParseException e)
        {
            LOGGER.error("parse cycleid to date failed, cycleid is [{}]!", cycleID, e);
            throw new CException(ResultCodeConstants.PARSE_CYCLEID_DATE_ERROR, cycleID);
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
     * jdom解析xml
     * @return map
     */
    public static Map<String, Map<String, String>> readRemoteShellSysInfo()
    {
        String path = SYS_CONFIGPATH;
        
        //存放多台服务器的信息
        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
        
        try
        {
            //解析xml
            SAXBuilder builder = new SAXBuilder();
            
            Document document = builder.build(path);
            
            //获取根节点
            Element root = document.getRootElement();
            
            //获取根节点下的第一级子节点
            List list = root.getChildren();
            
            Map<String, String> lMap = null;
            Element item = null;
            
            for (int i = 0; i < list.size(); i++)
            {
                //存放子节点信息map
                lMap = new HashMap<String, String>();
                
                item = (Element)list.get(i);
                
                //获取ip  user  password
                String ip = item.getChildText(TccUtil.LINUX_IP);
                String user = item.getChildText(TccUtil.LINUX_USER);
                String password = item.getChildText(TccUtil.LINUX_PASSWORD);
                try
                {
                    password = CryptUtilforcfg.decryptForAESStr(password.trim(), keyword);
                }
                catch (Exception e)
                {
                    LOGGER.error("decrypt password error. linuxUser[ip={},user={}].", new Object[] {ip, user, e});
                }
                lMap.put(TccUtil.LINUX_IP, ip);
                lMap.put(TccUtil.LINUX_USER, user);
                lMap.put(TccUtil.LINUX_PASSWORD, password);
                
                //以ip做key标识每台服务器
                map.put(ip, lMap);
            }
            
        }
        catch (JDOMException e)
        {
            LOGGER.error("readRemoteShellSysInfo excute failed!", e);
        }
        catch (IOException e)
        {
            LOGGER.error("readRemoteShellSysInfo excute failed!", e);
        }
        
        return map;
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
     * 获取远程登录的用户信息
     * @return 远程登录的用户信息
     * @throws Exception 异常
     */
    public static List<LinuxUser> readLinuxUsers()
        throws Exception
    {
        List<LinuxUser> users = new ArrayList<LinuxUser>();
        //将xml文档转换为Document的对象 
        org.dom4j.Document document = readDocument(SYS_CONFIGPATH);
        
        //获取文档的过滤对象节点
        @SuppressWarnings("rawtypes")
        List list = document.selectNodes("/server/machine");
        //filterObject节点
        org.dom4j.Element node;
        LinuxUser user;
        for (@SuppressWarnings("rawtypes")
        Iterator it = list.iterator(); it.hasNext();)
        {
            user = new LinuxUser();
            node = (org.dom4j.Element)it.next();
            user.setIp(node.elementText(TccUtil.LINUX_IP));
            user.setUserName(node.elementText(TccUtil.LINUX_USER));
            user.setPassword(node.elementText(TccUtil.LINUX_PASSWORD));
            users.add(user);
        }
        return users;
    }
    
    /**
     * 删除linux用户信息
     * @param user linux用户信息
     * @throws Exception 异常
     */
    public static void deleteLinuxUser(LinuxUser user)
        throws Exception
    {
        if (null == user || StringUtils.isBlank(user.getIp()))
        {
            return;
        }
        
        List<LinuxUser> users = new ArrayList<LinuxUser>();
        //将xml文档转换为Document的对象 
        org.dom4j.Document document = readDocument(SYS_CONFIGPATH);
        
        //获取文档的过滤对象节点
        @SuppressWarnings("rawtypes")
        List list = document.selectNodes("/server/machine");
        //filterObject节点
        org.dom4j.Element node;
        for (@SuppressWarnings("rawtypes")
        Iterator it = list.iterator(); it.hasNext();)
        {
            node = (org.dom4j.Element)it.next();
            if (user.getIp().endsWith(node.elementText(TccUtil.LINUX_IP)))
            {
                node.detach();
                break;
            }
        }
        
        XMLWriter xmlWriter = null;
        try
        {
            
            xmlWriter = new XMLWriter(new FileOutputStream(SYS_CONFIGPATH));
            xmlWriter.write(document);
        }
        catch (Exception e)
        {
            LOGGER.error("write LinuxUsers failed! users is {}", users, e);
            throw e;
        }
        finally
        {
            if (null != xmlWriter)
            {
                xmlWriter.flush();
                xmlWriter.close();
            }
        }
    }
    
    /**
     * 修改或新增远程登录的用户信息
     * @param user 远程登录的用户信息
     * @throws Exception 异常
     */
    public static void modifyLinuxUser(LinuxUser user)
        throws Exception
    {
        if (null == user || StringUtils.isBlank(user.getIp()))
        {
            return;
        }
        
        List<LinuxUser> users = new ArrayList<LinuxUser>();
        //将xml文档转换为Document的对象 
        org.dom4j.Document document = readDocument(SYS_CONFIGPATH);
        
        //获取文档的过滤对象节点
        @SuppressWarnings("rawtypes")
        List list = document.selectNodes("/server/machine");
        //filterObject节点
        org.dom4j.Element node;
        boolean findElement = false;
        for (@SuppressWarnings("rawtypes")
        Iterator it = list.iterator(); it.hasNext();)
        {
            node = (org.dom4j.Element)it.next();
            if (user.getIp().endsWith(node.elementText(TccUtil.LINUX_IP)))
            {
                node.element(TccUtil.LINUX_USER).setText(user.getUserName());
                node.element(TccUtil.LINUX_PASSWORD).setText(user.getPassword());
                findElement = true;
                break;
            }
        }
        
        //新增
        if (!findElement)
        {
            org.dom4j.Element machineE = document.getRootElement().addElement("machine");
            org.dom4j.Element ipE = machineE.addElement(TccUtil.LINUX_IP);
            org.dom4j.Element userNameE = machineE.addElement(TccUtil.LINUX_USER);
            org.dom4j.Element passwordE = machineE.addElement(TccUtil.LINUX_PASSWORD);
            
            ipE.setText(user.getIp());
            userNameE.setText(user.getUserName());
            passwordE.setText(user.getPassword());
        }
        
        XMLWriter xmlWriter = null;
        try
        {
            
            xmlWriter = new XMLWriter(new FileOutputStream(SYS_CONFIGPATH));
            xmlWriter.write(document);
        }
        catch (Exception e)
        {
            LOGGER.error("write LinuxUsers failed! users is {}", users, e);
            throw e;
        }
        finally
        {
            if (null != xmlWriter)
            {
                xmlWriter.flush();
                xmlWriter.close();
            }
        }
    }
    
    /**
     * 将远程登录的用户信息写入xml文件
     * @param users 远程登录的用户信息集合
     * @throws Exception 异常
     */
    public static void writeLinuxUsers(List<LinuxUser> users)
        throws Exception
    {
        XMLWriter xmlWriter = null;
        try
        {
            xmlWriter = new XMLWriter(new FileOutputStream(SYS_CONFIGPATH));
            org.dom4j.Document document = DocumentHelper.createDocument();//创建xml文档对象  
            org.dom4j.Element root = DocumentHelper.createElement("server");
            document.setRootElement(root);
            org.dom4j.Element machineE;
            org.dom4j.Element ipE;
            org.dom4j.Element userNameE;
            org.dom4j.Element passwordE;
            if (null != users && !users.isEmpty())
            {
                for (LinuxUser user : users)
                {
                    machineE = DocumentHelper.createElement("machine");
                    ipE = DocumentHelper.createElement(TccUtil.LINUX_IP);
                    userNameE = DocumentHelper.createElement(TccUtil.LINUX_USER);
                    passwordE = DocumentHelper.createElement(TccUtil.LINUX_PASSWORD);
                    
                    ipE.setText(user.getIp());
                    userNameE.setText(user.getUserName());
                    passwordE.setText(user.getPassword());
                    
                    ipE.setParent(machineE);
                    userNameE.setParent(machineE);
                    passwordE.setParent(machineE);
                    
                    machineE.setParent(root);
                }
            }
            xmlWriter.write(document);
        }
        catch (Exception e)
        {
            LOGGER.error("write LinuxUsers failed! users is {}", users, e);
            throw e;
        }
        finally
        {
            if (null != xmlWriter)
            {
                xmlWriter.flush();
                xmlWriter.close();
            }
        }
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
        for (String idDependErr : idDependErrs)
        {
            if (!StringUtils.isEmpty(idDependErr))
            {
                String[] idDependErrArr = idDependErr.split(",");
                
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
                            new Object[] {idDependErr, taskId, e});
                        throw new CException(ResultCodeConstants.TASK_DEPEND_REALATION_ERROR, idDependErr, taskId);
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
                        new Object[] {idDependErr, taskId});
                    throw new CException(ResultCodeConstants.TASK_DEPEND_REALATION_ERROR, idDependErr, taskId);
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
        
        for (String idDependErr : idDependErrs)
        {
            if (!StringUtils.isEmpty(idDependErr))
            {
                String[] idDependErrArr = idDependErr.split(",");
                
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
                            new Object[] {idDependErr, taskId, e});
                        throw new CException(ResultCodeConstants.TASK_DEPEND_REALATION_ERROR, idDependErr, taskId);
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
                        new Object[] {idDependErr, taskId});
                    throw new CException(ResultCodeConstants.TASK_DEPEND_REALATION_ERROR, idDependErr, taskId);
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
                        LOGGER.error("don't send any email, because valid mailsTo is empty! from is {}, "
                            + "mail subject is {},mail content is {}", new Object[] {mailfrom, subject, content});
                    }
                    
                }
                catch (Exception e)
                {
                    LOGGER.error("send mail to {} from {} failed, " + "mail subject is {},mail content is {}",
                        new Object[] {realMails.toString(), mailfrom, subject, content, e});
                }
            }
            else
            {
                LOGGER.info("don't send any email, because mailsTo is empty. from is {}, "
                    + "mail subject is {},mail content is {}", new Object[] {from, subject, content});
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
                            replaceTaskCycleId = log2Db.getTaskId() + "_" + log2Db.getCycleId();
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
                LOGGER.error("format fail!",e);
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
     * 获取tcc2shell对象
     * 
     * @return tcc2shell对象
     */
    public static Tcc2Shell getTcc2Shell()
    {
        return tcc2Shell;
    }
}
