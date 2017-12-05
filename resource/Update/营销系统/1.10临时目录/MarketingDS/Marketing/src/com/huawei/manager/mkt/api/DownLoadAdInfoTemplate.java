/*
 * 文 件 名:  DownLoadAdInfoTemplate.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-5-19
 */
package com.huawei.manager.mkt.api;

import java.sql.SQLException;

import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.dao.AdInfoDao;
import com.huawei.manager.mkt.info.AdDicMapInfo;
import com.huawei.manager.mkt.util.ExcelUtils;
import com.huawei.manager.mkt.util.FileUtils;
import com.huawei.manager.utils.Constant;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-5-19]
 * @see  [相关类/方法]
 */
public class DownLoadAdInfoTemplate extends AuthRDBProcessor
{
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    
    /**
     * 处理后
     * @param context   系统上下文
     * @param conn      数据库链接
     * @return          是否成功
     * @throws SQLException   数据库异常
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        LOG.debug("enter DownLoadAdInfoTemplate");
        
        //获取账号
        String account = context.getAccount();
        //获取数据库信息
        AdDicMapInfo dicMap = AdInfoDao.getAdDicMapInfo(conn,account);      
        //动态分配的文件路径
        String filePath =
            AdExoprt.class.getResource(Constant.SLANT).getPath() + Constant.SUBEXCELPATH + System.currentTimeMillis() + ".xls";
        //生成临时文件
        ExcelUtils.writeAdInfoTemplateFile(filePath,dicMap);
        
        FileUtils.writeHttpFileResponse(context, filePath);
        //写入ad信息
        return RetCode.OK;
    }
    
  
    
}
