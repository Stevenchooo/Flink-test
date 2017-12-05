package com.huawei.wda.honor;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.config.ConfigConstant;
import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.WAFException;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;
import com.huawei.wda.util.DownloadFileUtil;
import com.huawei.wda.util.StringUtils;

/**
 * 下载图片 <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年6月8日]
 * @see [相关类/方法]
 */
public class ReportImgDownload extends AuthRDBProcessor
{
    /**
     * 日志类
     */
    private static final Logger LOG = LogUtil.getInstance();

    /**
     * 执行脚本之后
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
        String imgRealPath = "";
        List reportList = (ArrayList) context.getResults().get("data");
        String reportImgPath = (String) ((Map) reportList.get(0))
                .get("report_back_img");
        if (!reportImgPath.startsWith("images"))
        {
            imgRealPath = StringUtils
                    .getConfigInfo(ConfigConstant.REPORT_IMG_DIR)
                    + File.separator + reportImgPath;
        }
        else
        {
            imgRealPath = context.getRequest().getSession().getServletContext()
                    .getRealPath("/")
                    + reportImgPath;
        }

        try
        {
            DownloadFileUtil.downLoadImages(imgRealPath, context);
        }
        catch (WAFException e)
        {
            LOG.warn("ReportImgDownload WAFException");
        }

        return RetCode.OK;
    }
}
