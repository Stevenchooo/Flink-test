package com.huawei.wda.honor;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.config.ConfigConstant;
import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.LogUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.WAFException;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;
import com.huawei.wda.util.DownloadFileUtil;
import com.huawei.wda.util.StringUtils;

/**
 * 用于管理下载报告 <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月30日]
 * @see [相关类/方法]
 */
public class ReportDownload extends AuthRDBProcessor
{
    /**
     * 日志类
     */
    private static final Logger LOG = LogUtil.getInstance();

    /**
     * 执行sql之后
     * 
     * @param context
     *            context
     * @param conn
     *            conn
     * @return int
     * @throws SQLException
     *             SQLException
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        List reportList = (ArrayList) context.getResults().get("data");
        String reportFilePath = (String) ((Map) reportList.get(0))
                .get("report_filepath");
        String reportFileName = (String) ((Map) reportList.get(0))
                .get("report_filename");
        String firstPath = StringUtils
                .getConfigInfo(ConfigConstant.REPORT_FILE_DIR);
        String totalPath = firstPath + File.separator + reportFilePath;
        try
        {
            DownloadFileUtil.downLoadFile(totalPath, reportFileName, context);
        }
        catch (WAFException e)
        {
            LOG.warn("ReportDownload WAFException");
        }
        return RetCode.OK;
    }
}
