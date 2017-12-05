/*
 * 文 件 名:  DownLoadLandInfoTemplate.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-1
 */
package com.huawei.manager.mkt.api;

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
 * @version [Internet Business Service Platform SP V100R100, 2015-6-1]
 * @see  [相关类/方法]
 */
public class DownLoadLandInfoTemplate extends AuthRDBProcessor
{
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 处理中
     * @param context   系统上下文
     * @param conn      数据库连接
     * @return          是否成功
     */
    @Override
    public int process(MethodContext context, DBConnection conn)
    {
        LOG.debug("enter DownLoadLandInfoTemplate");
        
        //获取账号
        String account = context.getAccount();
        //数据库连接
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
