package com.huawei.wda.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.wda.http.ContentType;
import com.huawei.wda.http.HttpInvokerReq;
import com.huawei.wda.http.HttpInvokerResp;
import com.huawei.wda.http.HttpInvokerUtils;
import com.huawei.wda.http.RequestBodyType;

/**
 * 公共类 <一句话功能简述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年6月2日]
 * @see [相关类/方法]
 */
public abstract class CommonUtils
{
    /**
     * 默认编码格式utf-8
     */
    public static final String DEFUALT_ENCODING = "utf-8";

    /**
     * 时间格式：yyyyMMdd
     */
    public static final String TIME_FORMAT = "yyyyMMdd";

    /**
     * 开始发送请求 <功能详细描述>
     * 
     * @param uri
     *            uri
     * @param params
     *            params
     * @return HttpInvokerResp
     * @throws IOException
     *             IO异常
     * @see [类、类#方法、类#成员]
     */

    public static final HttpInvokerResp startHttpReq(String uri,
            JSONObject params) throws IOException
    {
        // 构造请求头
        HttpInvokerReq req = new HttpInvokerReq(uri, ContentType.JSON,
                RequestBodyType.SRTING, JSON.toJSONString(params));
        req.setTrustAny(true);
        return HttpInvokerUtils.sendMsg(req);
    }

    /**
     * 获取当前的日期：yyyyMMdd格式 <功能详细描述>
     * 
     * @return String
     * @see [类、类#方法、类#成员]
     */
    public static String obtainCurrentDate()
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        String finalDate = sdf.format(date);
        return finalDate;
    }

    /**
     * 判断集合是否为空
     * 
     * @param list
     *            集合
     * @return 结果
     * 
     */
    public static boolean isNotEmptyList(List<?> list)
    {
        if (null != list && !list.isEmpty())
        {
            return true;
        }
        return false;
    }

    /**
     * 取任意小数后多少位 <功能详细描述>
     * 
     * @param number
     *            小数
     * @param digit
     *            位数
     * @return double
     * @see [类、类#方法、类#成员]
     */
    public static double obtainAnyDecimals(double number, int digit)
    {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        double finalNumber = bd.doubleValue();
        return finalNumber;
    }

    /**
     * 判断字符串是否为null、空或者只包换空格
     * 
     * @param str
     *            String
     * @return boolean （true 字符串为null、空或者只包换空格；反之则false)
     */
    public static boolean isStringNullOrEmpty(String str)
    {
        return null == str || str.isEmpty() || str.matches("^\\s+$");
    }
}
