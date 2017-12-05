package com.huawei.wda.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;

import com.huawei.util.LogUtil;

/**
 * 日期工具类.
 */
public abstract class DateUtil
{
    /**
     * 7天的毫秒数
     */
    public static final long TIME_SECONDS = 7 * 24 * 60 * 60 * 1000;

    private static final Logger LOG = LogUtil.getInstance();

    /**
     * 获得指定日期的前一天.
     * 
     * @param specifiedDay
     *            specifiedDay
     * @param period
     *            period
     * @return String
     */
    public static String getSpecifiedDayBefore(String specifiedDay, int period)
    {
        if (specifiedDay != null)
        {
            // 可以用new Date().toLocalString()传递参数
            Calendar c = Calendar.getInstance();
            Date date = new Date();
            try
            {
                date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
            }
            catch (ParseException e)
            {
                LOG.debug("ParseException");
            }
            c.setTime(date);
            int day = c.get(Calendar.DATE);
            c.set(Calendar.DATE, day - period);

            String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
                    .getTime());
            return dayBefore;
        }
        return null;
    }

    /**
     * 获得指定日期的后一天.
     * 
     * @param specifiedDay
     *            specifiedDay
     * @return String
     */
    public static String getSpecifiedDayAfter(String specifiedDay)
    {
        if (specifiedDay != null)
        {
            Calendar c = Calendar.getInstance();
            Date date = new Date();
            try
            {
                date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
            }
            catch (ParseException e)
            {
                LOG.debug("ParseException");
            }
            c.setTime(date);
            int day = c.get(Calendar.DATE);
            c.set(Calendar.DATE, day + 1);

            String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c
                    .getTime());
            return dayAfter;
        }
        return null;
    }

    /**
     * 将"yyyy-MM-dd"格式的日期转换成"yyyy/MM/dd" <功能详细描述>.
     * 
     * @param currentDate
     *            currentDate
     * @return String
     * @see [类、类#方法、类#成员]
     */
    public static String changeDateFormat(String currentDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String finalStringDate = null;
        try
        {
            Date date = sdf.parse(currentDate);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
            finalStringDate = sdf1.format(date);
        }
        catch (ParseException e)
        {
            LOG.debug("DateUtil : ParseException");
        }
        return finalStringDate;
    }

    /**
     * 获取一个日期段的每个自然周的开始日期和结束日期 ，如：2016-04-19-2016-04-26
     * 分割后：2016-04-19#2016-04-24和2016-04-25#2016-04-26
     * 
     * @param fromDate
     *            起始日期
     * @param toDate
     *            结束日期
     * @return List<String>
     * @see [类、类#方法、类#成员]
     */
    public static List<String> obtainWeekDate(String fromDate, String toDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
        List<String> list = new ArrayList<String>();
        try
        {
            Date date = sdf.parse(fromDate);
            Date dateLast = sdf.parse(toDate);
            String startDate = sdf1.format(date);
            String endDate = sdf1.format(dateLast);
            if (fromDate.equals(toDate))
            {
                list.add(startDate + "-" + endDate);
            }
            else if (date.getTime() < dateLast.getTime())
            {
                String sundayFirst = null;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                // 设置周一为一周的第一天
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (1 == dayOfWeek) // 如果是礼拜天
                {
                    sundayFirst = sdf1.format(calendar.getTime());
                    list.add(startDate + "-" + sundayFirst);
                }
                else
                {
                    calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek()
                            - dayOfWeek);
                    calendar.add(Calendar.DATE, 6);
                    sundayFirst = sdf1.format(calendar.getTime());
                    list.add(startDate + "-" + sundayFirst);
                }
                Date dateOne = sdf1.parse(sundayFirst);
                String monday = null;
                String sundaySecond = null;
                long count = (dateLast.getTime() - dateOne.getTime())
                        / TIME_SECONDS;
                if (count >= 1)
                {

                    for (int i = 0; i < count; i++)
                    {
                        calendar.add(Calendar.DATE, 1);
                        monday = sdf1.format(calendar.getTime());
                        calendar.add(Calendar.DATE, 6);
                        sundaySecond = sdf1.format(calendar.getTime());
                        list.add(monday + "-" + sundaySecond);

                    }
                    if (dateLast.getTime() > calendar.getTime().getTime())
                    {
                        calendar.add(Calendar.DATE, 1);
                        monday = sdf1.format(calendar.getTime());
                        sundaySecond = endDate;
                        list.add(monday + "-" + sundaySecond);
                    }
                }
                else
                {
                    // 不足一周
                    calendar.add(Calendar.DATE, 1);
                    monday = sdf1.format(calendar.getTime());
                    sundaySecond = endDate;
                    list.add(monday + "-" + sundaySecond);
                }

            }
        }
        catch (ParseException e)
        {
            LOG.debug("ParseException");
        }
        return list;
    }

    /**
     * 将String类型的日期转成Date类型的日期(yyyy/MM/dd转换成Date)
     * 
     * @param strDate
     *            String类型的日期
     * @return Date类型日期
     * @see [类、类#方法、类#成员]
     */
    public static Date strToDate(String strDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try
        {
            date = sdf.parse(strDate);
        }
        catch (ParseException e)
        {
            LOG.debug("ParseException");
        }
        return date;
    }

    /**
     * 将String类型的日期转成String类型的日期（yyyy-MM-dd--->yyyy/MM） <功能详细描述>
     * 
     * @param strDate
     *            String类型的日期
     * @return string
     * @see [类、类#方法、类#成员]
     */
    public static String strChangeToMonth(String strDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        String finalDate = null;
        try
        {
            date = sdf.parse(strDate);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM");
            finalDate = sdf1.format(date);
        }
        catch (ParseException e)
        {
            LOG.debug("ParseException");
        }
        return finalDate;
    }

    /**
     * 获取一个日期范围内所有的年月list或者年月日list <功能详细描述>
     * 
     * @param fromDate
     *            开始日期
     * @param toDate
     *            结束日期
     * @param isAccordingMonth
     *            是否是按月
     * @return List<String>
     * @see [类、类#方法、类#成员]
     */
    public static List<String> obtainMonthListOrDayList(String fromDate,
            String toDate, boolean isAccordingMonth)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> list = new ArrayList<String>();
        Date date1 = null;
        Date date2 = null;
        try
        {
            date1 = sdf.parse(fromDate);
            date2 = sdf.parse(toDate);

            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(date1);
            c2.setTime(date2);
            if (isAccordingMonth)
            {
                c2.set(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), 1);

                String strFromDate = strChangeToMonth(fromDate);
                list.add(strFromDate);
                while (c1.compareTo(c2) < 0)
                {
                    c1.add(Calendar.MONTH, 1);
                    Date changeDate = c1.getTime();
                    String str = sdf.format(changeDate);
                    str = strChangeToMonth(str);
                    list.add(str);
                }
            }
            else
            {
                String strFromDate = changeDateFormat(fromDate);
                list.add(strFromDate);
                while (c1.compareTo(c2) < 0)
                {
                    c1.add(Calendar.DATE, 1);
                    Date changeDate = c1.getTime();
                    String str = sdf.format(changeDate);
                    str = changeDateFormat(str);
                    list.add(str);
                }
            }

        }
        catch (ParseException e)
        {
            LOG.debug("ParseException");
        }
        return list;
    }

    /**
     * 获取几个月前的日期，日期格式为（yyyy-MM-dd）<一句话功能简述> <功能详细描述>
     * 
     * @param number
     *            几个月的参数（例如：-3，三个月前）
     * @return String
     * @see [类、类#方法、类#成员]
     */
    public static String obtainxMonthsAgoDate(int number)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        c1.add(Calendar.MONTH, number);
        Date handleDate = c1.getTime();
        String strDate = sdf.format(handleDate);
        return strDate;
    }

    /**
     * 获取当前日期的String日期（yyyy-MM-dd）<一句话功能简述> <功能详细描述>
     * 
     * @return String
     * @see [类、类#方法、类#成员]
     */
    public static String obtainCurrentStrDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String strDate = sdf.format(date);
        return strDate;
    }
}