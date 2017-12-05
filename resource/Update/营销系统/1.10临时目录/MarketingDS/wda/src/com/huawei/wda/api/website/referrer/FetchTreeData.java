package com.huawei.wda.api.website.referrer;

import static com.huawei.waf.protocol.RetCode.OK;
import static com.huawei.waf.protocol.RetCode.WRONG_PARAMETER;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.run.MethodContext;

/**
 * 查询数据FetchTreeData <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class FetchTreeData extends AuthRDBProcessor
{

    /**
     * 在数据脚本执行之前调用，提供一个机会来处理其他事情 比如给statement添加不在parameters中的其他参数
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @param statement
     *            statement
     * @return int型参数
     * @throws SQLException
     *             SQLException
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {

        Map<String, Object> reqParameters = context.getParameters();

        String periodTime = JsonUtil.getAsStr(reqParameters, "periodTime");

        String huanBiDate = JsonUtil.getAsStr(reqParameters, "huanBiDate");
        String huanBiFromDate = null;
        String huanBiToDate = null;
        String fromDate = null;
        String toDate = null;
        if (periodTime.contains("#"))
        {
            String[] tempDate = periodTime.split("#");
            fromDate = tempDate[0];
            toDate = tempDate[1];
            if (StringUtils.isNotBlank(huanBiDate))
            {
                String[] tempDate1 = huanBiDate.split("#");
                huanBiFromDate = tempDate1[0];
                huanBiToDate = tempDate1[1];
            }
        }
        else
        {
            // log
            return WRONG_PARAMETER;
        }

        statement.setString(5, fromDate);
        statement.setString(6, toDate);
        statement.setString(7, huanBiFromDate);
        statement.setString(8, huanBiToDate);

        return OK;
    }

    /**
     * 调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @return int型参数
     * @throws SQLException
     *             SQLException
     */
    @Override
    @SuppressWarnings(
    {"rawtypes", "unchecked"})
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        SerializerFeature f = SerializerFeature.DisableCircularReferenceDetect;
        List a = (ArrayList) context.getResults().get("data1");

        List result = new ArrayList();
        List a2 = (ArrayList) context.getResults().get("data2");
        List a3 = (ArrayList) context.getResults().get("data3");
        if (a != null && a.size() >= 1)
        {

            Map map1 = (Map) a.get(0);
            List map1Value = (null == a2 || a2.isEmpty()) ? a3 : a2;
            map1.put("children", map1Value);
            result.add(map1);
        }

        if (a != null && a.size() >= 2)
        {
            Map map2 = (Map) a.get(1);
            List map2Value = (null == a2 || a2.isEmpty()) ? a2 : a3;
            map2.put("children", map2Value);
            result.add(map2);
        }

        if (a != null && a.size() >= 3)
        {
            Map map3 = (Map) a.get(2);
            result.add(map3);
        }
        String result1 = JSON.toJSONString(result, f);
        context.setResult("data", result1);
        return OK;
    }
}