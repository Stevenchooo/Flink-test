package com.huawei.wda.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 集合工具类.
 */
public class CollectionUtil
{
    /**
     * 转换数据
     * 
     * @param srcData
     *            srcData
     * @return List
     */
    @SuppressWarnings(
    {"rawtypes", "unchecked"})
    public static List formatData(List<Map<String, String>> srcData)
    {

        List destData = new ArrayList();

        if (srcData == null || srcData.size() == 0)
        {
            return destData;
        }

        // 遍历list
        Iterator<Map<String, String>> iterator = srcData.iterator();
        while (iterator.hasNext())
        {
            destData.add((iterator.next()).values().toArray());
        }

        return destData;
    }

    /**
     * 格式化x轴.
     * 
     * @param srcData
     *            srcData
     * @throws ParseException
     *             ParseException
     * @return List
     *             ParseException
     */
    @SuppressWarnings(
    {"rawtypes", "unchecked"})
    public static List formatAxis(List<Map<String, String>> srcData)
        throws ParseException
    {

        List axisData = new ArrayList();

        if (srcData == null || srcData.size() == 0)
        {
            return axisData;
        }

        Iterator<Map<String, String>> iterator = srcData.iterator();

        while (iterator.hasNext())
        {
            List temp = new ArrayList();
            Integer a = (Integer) iterator.next().values().toArray()[0];
            if (a <= 23)
            {
                temp.add(a);
                temp.add(a + "点");
                axisData.add(temp);
            }
            else if (a > 23)
            {
                temp.add(a);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                Date date = sdf.parse(a.toString());
                sdf = new SimpleDateFormat("yyyy/MM/dd");
                temp.add(sdf.format(date));
                axisData.add(temp);
            }
        }

        return axisData;
    }

}
