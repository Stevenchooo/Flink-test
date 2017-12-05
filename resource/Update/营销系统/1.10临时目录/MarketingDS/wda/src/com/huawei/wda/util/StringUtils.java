/*
 * 文 件 名:  StringUtils.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-2
 */
package com.huawei.wda.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.huawei.manager.base.protocol.RetCodeExt;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.sys.SysConfig;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-2]
 * @see [相关类/方法]
 */
public abstract class StringUtils
{
    // 日志
    private static final Logger LOG = LogUtil.getInstance();

    /**
     * <获取字符串长度>
     * 
     * @param s
     *            字符
     * @return 字符串长度
     * @see [类、类#方法、类#成员]
     */
    public static int getLength(String s)
    {
        if (s == null)
        {
            return 0;
        }

        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++)
        {
            len++;
            if (!isLetter(c[i]))
            {
                len++;
            }
        }
        return len;
    }

    /**
     * 是否是空串
     * 
     * @param s
     *            字符串
     * @return 是否是空串
     * @see [类、类#方法、类#成员]
     */
    public static boolean isEmptyStr(String s)
    {
        return s == null || s.isEmpty();
    }

    /**
     * <判断是否是单字符>
     * 
     * @param c
     *            字符
     * @return 否是单字符
     * @see [类、类#方法、类#成员]
     */
    public static boolean isLetter(char c)
    {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * <判断是否是合法URL>
     * 
     * @param url
     *            链接
     * @return 是否正确
     * @see [类、类#方法、类#成员]
     */
    public static boolean isUrl(String url)
    {
        if (null == url)
        {
            return false;
        }

        String regex = "(http|ftp|https)://[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    /**
     * <判断是否是合法数字>
     * 
     * @param value
     *            字符串
     * @return 是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean isValidInt(String value)
    {
        try
        {
            Integer i = Integer.parseInt(value);
            if (i < 0)
            {
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            LOG.error("isValidInt error!");
            return false;
        }
        return true;
    }

    /**
     * <判读是否为正确的数字>
     * 
     * @param value
     *            字符串
     * @return 字符串是否是数字
     * @see [类、类#方法、类#成员]
     */
    public static boolean isValidNumber(String value)
    {
        if (null == value)
        {
            return false;
        }
        String regex = "^[0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /**
     * <判读是否是合法浮点数>
     * 
     * @param value
     *            字符串
     * @return 是否合法
     * @see [类、类#方法、类#成员]
     */
    public static boolean isValidFloat(String value)
    {
        try
        {
            Float f = Float.parseFloat(value);
            if (f <= 0)
            {
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            LOG.error("isValidFloat error!");
            return false;
        }
        return true;
    }

    /**
     * <将excel文件中如果是数字的值去掉后面的小数点>
     * 
     * @param d
     *            数字
     * @return 格式化后的数值
     * @see [类、类#方法、类#成员]
     */
    public static String formateNumber(double d)
    {

        // number格式为 1.11;1.0;1
        DecimalFormat df = new DecimalFormat("0.00");
        String value = df.format(d);
        // 判断后面有没有小数点，小数点后为0返回整数型

        if (value.endsWith(".00"))
        {
            value = value.replace(".00", "");
        }

        return value;
    }

    /**
     * <检查是否是integer数字>
     * 
     * @param buffer
     *            检验信息
     * @param value
     *            数据值
     * @param field
     *            字段
     * @return 校验是否成功
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkIntegerValue(StringBuffer buffer, String value,
            String field)
    {
        if (null == buffer || null == field)
        {
            return false;
        }

        boolean numFlag = StringUtils.isValidNumber(value);
        if (!numFlag)
        {
            buffer.append(field + "格式错误,请输入数字;");
            return false;
        }

        // 是否数字
        boolean flag = StringUtils.isValidInt(value);
        if (!flag)
        {
            buffer.append(field + "格式错误,请输入数字,最小值大于等于0最大值为2147483647;");
            return false;
        }

        return true;
    }

    /**
     * <获取系统配置值>
     * 
     * @param key
     *            配置项
     * @return 配置项对应的值
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public static String getConfigInfo(String key)
    {
        Map<String, Object> casClient = (Map<String, Object>) SysConfig
                .getFreemarkerConfigs().get("variables");
        if (casClient == null)
        {
            return null;
        }

        return casClient.get(key).toString();
    }

    /**
     * <查看校验码是否正确>
     * 
     * @param verifyCode
     *            校验码
     * @param verifyCodeCookie
     *            校验码cookie
     * @return 检查信息
     * @see [类、类#方法、类#成员]
     */
    public static int checkVerifyCode(String verifyCode, String verifyCodeCookie)
    {
        if (Utils.isStrEmpty(verifyCodeCookie))
        {
            LOG.info("verifyCode expired");
            return RetCode.VERIFYCODE_EXPIRED;
        }

        if (Utils.isStrEmpty(verifyCode))
        {
            LOG.info("verifyCode is null");
            return RetCode.WRONG_VERIFYCODE;
        }

        if (!verifyCodeCookie.equalsIgnoreCase(verifyCode))
        {
            LOG.info("verifyCode {} != verifyCode in Cookie {}, authCode={}",
                    verifyCode, verifyCodeCookie, WAFConfig.getAuthCode());
            return RetCodeExt.WRONG_VERIFYCODE;
        }

        return RetCode.OK;
    }

    /**
     * <获取默认的投放日期>
     * 
     * @return 默认的投放日期
     * @see [类、类#方法、类#成员]
     */
    public static String getDefaultDeliveryTimes()
    {
        Date now = new Date();

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(now.getTime());
        c.add(Calendar.DATE, 30);

        Date newDate = new Date(c.getTimeInMillis());

        DateFormat inputDateFormat = new SimpleDateFormat("yyyyMMdd");

        return inputDateFormat.format(now) + "-"
                + inputDateFormat.format(newDate);
    }

    /**
     * <日期格式化>
     * 
     * @param date
     *            输入时间值
     * @return 指定格式的输出值
     * @see [类、类#方法、类#成员]
     */
    public static String dateFormat(String date)
    {

        DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try
        {
            Date tempDate = inputDateFormat.parse(date);
            return inputDateFormat.format(tempDate);
        }
        catch (ParseException e)
        {
            LOG.info("dateFormat error!");
        }
        return null;
    }
}
