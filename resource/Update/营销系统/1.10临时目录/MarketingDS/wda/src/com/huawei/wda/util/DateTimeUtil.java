package com.huawei.wda.util;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 日期处理工具.
 */

public abstract class DateTimeUtil
{

    /**
     * 开始日期.
     * 
     * @param period
     *            period
     * @return String
     */
    public static String getStartDate(int period)
    {
        LocalDate now = new LocalDate();
        return now.plusDays(period).toString();
    }

    /**
     * 结束日期.
     * 
     * @param period
     *            period
     * @return String
     */
    public static String getEndDate(int period)
    {
        LocalDate now = new LocalDate();
        if (period == -1)
        {
            return now.plusDays(period).toString();
        }
        else
        {
            return now.toString();
        }
    }

    /**
     * 环比开始日期.
     * 
     * @param startDate
     *            startDate
     * @param period
     *            period
     * @return String
     */
    public static String getHbStartDate(String startDate, int period)
    {
        if (period == 0)
        {
            return new LocalDate(startDate).plusDays(-1).toString();
        }
        else
        {
            return new LocalDate(startDate).plusDays(period).toString();
        }
    }

    /**
     * 环比结束日期.
     * 
     * @param endDate
     *            endDate
     * @param period
     *            period
     * @return String
     */
    public static String getHbEndDate(String endDate, int period)
    {
        if (period == 0)
        {
            return new LocalDate(endDate).plusDays(-1).toString();
        }
        else
        {
            return new LocalDate(endDate).plusDays(period).toString();
        }
    }

    /**
     * 日期转换.
     * 
     * @param curDate
     *            curDate
     * @param dim
     *            dim
     * @param period
     *            period
     * @return String
     */
    public static String convert2HbDate(String curDate, int dim, int period)
    {

        if (dim == 1)
        {
            DateTimeFormatter dtf = DateTimeFormat
                    .forPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime d = null;
            if (period == 0)
            {
                d = dtf.parseLocalDateTime(curDate).minusDays(-1);
            }
            else
            {
                d = dtf.parseLocalDateTime(curDate).minusDays(period);
            }

            return d.toString("yyyy-MM-dd HH:mm:ss");
        }
        else if (dim == 2)
        {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
            LocalDate d = null;
            if (period == 0)
            {
                d = dtf.parseLocalDate(curDate).minusDays(-1);
            }
            else
            {
                d = dtf.parseLocalDate(curDate).minusDays(period);
            }

            return d.toString("yyyy-MM-dd");
        }

        return null;
    }

}
