package com.huawei.wda.api.home;

import static com.huawei.waf.protocol.RetCode.OK;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.wda.util.DateUtil;

/**
 * 请求列表处理
 */
public class ShowWebsiteList extends AuthRDBProcessor
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
     * @throws SQLException
     *             SQLException
     * @return 返回码
     */
    @Override
    protected int beforeProcess(MethodContext context, DBConnection conn,
            CallableStatement statement) throws SQLException
    {

        String account = context.getAccount();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String curDate = dateFormat.format(new Date());

        String lastDate = DateUtil.getSpecifiedDayBefore(curDate, 1);

        statement.setString(2, account);
        statement.setString(3, lastDate);

        return OK;
    }

    /**
     * 调用此函数时，sql脚本已经执行，context的result中已有数据库返回的结果集
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @throws SQLException
     *             SQLException
     * @return 返回码
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        return OK;
    }
}