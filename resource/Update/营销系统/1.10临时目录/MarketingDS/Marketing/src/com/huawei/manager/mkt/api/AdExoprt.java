/*
 * 文 件 名:  AdExoprt.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2014-12-10
 */
package com.huawei.manager.mkt.api;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.dao.AdInfoDao;
import com.huawei.manager.mkt.info.AdDicMapInfo;
import com.huawei.manager.mkt.info.AdExportInfo;
import com.huawei.manager.mkt.util.ExcelUtils;
import com.huawei.manager.mkt.util.MktUtils;
import com.huawei.manager.utils.Constant;
import com.huawei.manager.mkt.util.FileUtils;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2014-12-10]
 * @see  [相关类/方法]
 */
public class AdExoprt extends AuthRDBProcessor
{
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 接口处理后如何处理
     * @param context   系统上下文
     * @param conn      数据库连接
     * @return          是否成功
     * @throws SQLException   数据库异常
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        LOG.debug("enter AdExoprt");
        
        //导出信息列表
        List<AdExportInfo> exportList = MktUtils.getAdExportInfo(context);
        
        //获取账号
        String account = context.getAccount();
        
        //数据库连接
        AdDicMapInfo dicMap = AdInfoDao.getAdDicMapInfo(conn, account);
        
        //动态分配的文件路径
        String filePath =
            AdExoprt.class.getResource(Constant.SLANT).getPath() + Constant.SUBEXCELPATH + System.currentTimeMillis()
                + ".xls";
        
        //数据查询结果
        Map<String, Object> results = context.getResults();
        //存储过程返回的列表
        
        int exportType = Integer.valueOf(results.get("adExportType").toString());
        
        //生成临时文件
        ExcelUtils.writeAdExportFile(filePath, exportList, dicMap, exportType);
        
        FileUtils.writeHttpFileResponse(context, filePath);
        
        //写入ad信息
        return RetCode.OK;
    }
    
}
