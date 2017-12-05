/*
 * 文 件 名:  MaterialBatchDownload.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-7-26
 */
package com.huawei.manager.mkt.api;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.info.MaterialInfo;
import com.huawei.manager.mkt.util.FileUtils;
import com.huawei.manager.mkt.util.StringUtils;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-7-26]
 * @see  [相关类/方法]
 */
public class MaterialBatchDownload extends AuthRDBProcessor
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
        LOG.debug("enter MaterialBatchDownload");
        
        //获取列表
        List<MaterialInfo> materialList = getMaterialInfoList(context);
        
        //获取配置目录
        String materialSavePath = StringUtils.getConfigInfo("materialBatchSavePath");
        
        //创建目录
        FileUtils.createPath(materialSavePath);
        
        //压缩zip文件路径
        String filePath = materialSavePath + File.separator + System.currentTimeMillis() + ".zip";
        
        //压缩文件
        FileUtils.zipFiles(materialList, filePath);
        
        //系统返回
        FileUtils.writeHttpFileResponse(context, filePath);
        
        //写入ad信息
        return RetCode.OK;
    }
    
    private List<MaterialInfo> getMaterialInfoList(MethodContext context)
    {
        //数据查询结果
        Map<String, Object> results = context.getResults();
        
        //存储过程返回的列表
        @SuppressWarnings("unchecked")
        List<Map<String, String>> list = (List<Map<String, String>>)results.get("result");
        
        List<MaterialInfo> materialList = new ArrayList<MaterialInfo>();
        
        if (null == list || list.isEmpty())
        {
            return materialList;
        }
        
        //动态分配的文件路径
        for (Map<String, String> map : list)
        {
            Integer adInfoId = Integer.valueOf(map.get("adInfoId"));
            String mktinfoName = map.get("mktinfoName");
            String adInfoMediaType = map.get("adInfoMediaType");
            String adInfoWebName = map.get("adInfoWebName");
            String adInfoChannel = map.get("adInfoChannel");
            String adInfoPosition = map.get("adInfoPosition");
            String materialName = map.get("materialName");
            String materialPath = map.get("materialPath");
            
            MaterialInfo info = new MaterialInfo();
            info.setAdInfoId(adInfoId);
            info.setMktinfoName(mktinfoName);
            info.setAdInfoMediaType(adInfoMediaType);
            info.setAdInfoWebName(adInfoWebName);
            info.setAdInfoChannel(adInfoChannel);
            info.setAdInfoPosition(adInfoPosition);
            info.setMaterialName(materialName);
            info.setMaterialPath(materialPath);
            
            materialList.add(info);
            
        }
        return materialList;
    }
}
