/*
 * 文 件 名:  AdBatchResDownLoad.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-5-26
 */
package com.huawei.manager.mkt.api;

import org.slf4j.Logger;

import com.huawei.manager.mkt.util.FileUtils;
import com.huawei.manager.utils.Constant;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.DefaultJavaProcessor;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-5-26]
 * @see  [相关类/方法]
 */
public class AdBatchResDownLoad extends DefaultJavaProcessor
{
    
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 处理过程
     * @param context   系统上下文
     * @return          返回值
     */
    @Override
    public int process(MethodContext context)
    {
        LOG.debug("AdBatchResDownLoad process");
        
        //导出信息列表
        String fileId = (String)context.getParameter("fileId");
        
        //动态分配的文件路径
        String filePath = AdExoprt.class.getResource(Constant.SLANT).getPath() + Constant.SUBEXCELPATH + fileId + "_res.xls";
        
        //写文件流
        FileUtils.writeHttpFileResponse(context, filePath);
        
        //返回
        return RetCode.OK;
    }
}
