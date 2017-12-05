package com.huawei.wda.api.website;

import static com.huawei.waf.protocol.RetCode.OK;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;

/**
 * 查询数据SummaryAnalytics <一句话功能简述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月14日]
 * @see [相关类/方法]
 */
public class SummaryAnalytics extends AuthRDBProcessor
{

    /**
     * SummaryAnalytics在数据脚本执行之前调用，提供一个机会来处理其他事情
     * 比如给statement添加不在parameters中的其他参数
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

        return OK;
    }

    /**
     * SummaryAnalytics调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
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

        List detail = (ArrayList) context.getResults().get("detail");

        List preDetail = (ArrayList) context.getResults().get("pre_detail");

        int detailSize = detail.size();
        int preDetailSize = preDetail.size();

        if (detail.size() < 24)
        {
            for (int i = detailSize; i < 24; i++)
            {
                Map map = new HashMap();
                map.put("flag", 1);
                map.put("pt_h", i);
                map.put("pt_d", ((Map) (detail.get(0))).get("pt_d"));
                //pv
                map.put("pv", 0);
                //访客
                map.put("visits", 0);
                map.put("ip", 0);
                map.put("bounce_rate", 0.00);
                detail.add(map);
            }
        }

        if (preDetail.size() < 24)
        {
            for (int i = preDetailSize; i < 24; i++)
            {
                Map map = new HashMap();
                map.put("flag", 1);
                map.put("pt_h", i);
                map.put("pt_d", ((Map) (preDetail.get(0))).get("pt_d"));
                map.put("pv", 0);
                map.put("visits", 0);
                map.put("ip", 0);
                map.put("bounce_rate", 0.00);
                preDetail.add(map);
            }
        }

        List result = new ArrayList();
        for (int i = 0; i < 24; i++)
        {
            Map map = new HashMap();
            map.put("flag", ((Map) detail.get(i)).get("flag"));
            map.put("pt_h", ((Map) detail.get(i)).get("pt_h"));
            map.put("pt_d", ((Map) detail.get(i)).get("pt_d"));
            map.put("pv", ((Map) detail.get(i)).get("pv"));
            map.put("visits", ((Map) detail.get(i)).get("visits"));
            map.put("ip", ((Map) detail.get(i)).get("ip"));
            map.put("bounce_rate", ((Map) detail.get(i)).get("bounce_rate"));

            result.add(map);

            Map preMap = new HashMap();

            preMap.put("flag", ((Map) preDetail.get(i)).get("flag"));
            preMap.put("pt_h", ((Map) preDetail.get(i)).get("pt_h"));
            preMap.put("pt_d", ((Map) preDetail.get(i)).get("pt_d"));
            preMap.put("pv", ((Map) preDetail.get(i)).get("pv"));
            preMap.put("visits", ((Map) preDetail.get(i)).get("visits"));
            preMap.put("ip", ((Map) preDetail.get(i)).get("visits"));
            preMap.put("bounce_rate",
                    ((Map) preDetail.get(i)).get("bounce_rate"));

            result.add(preMap);

        }

        context.setResult("result", result);
        return OK;
    }
}
