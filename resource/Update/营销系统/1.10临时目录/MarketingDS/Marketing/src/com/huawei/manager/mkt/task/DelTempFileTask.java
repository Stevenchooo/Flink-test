/*
 * 文 件 名:  BackUpTableTask.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-10
 */
package com.huawei.manager.mkt.task;

import java.io.File;
import java.util.Collection;
import java.util.TimerTask;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import com.huawei.manager.mkt.util.StringUtils;
import com.huawei.manager.utils.Constant;
import com.huawei.util.LogUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-10]
 * @see  [相关类/方法]
 */
public class DelTempFileTask extends TimerTask
{
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 定时删除文件
     */
    @Override
    public void run()
    {
        LOG.debug("enter DelTempFileTask");
        
        //模板临时文件
        String pathName = DelTempFileTask.class.getResource(Constant.SLANT).getPath() + Constant.SUBEXCELPATH;
        
        //批量下载素材zip文件
        String materialSavePath = StringUtils.getConfigInfo("materialBatchSavePath");
        
        delPathFiles(pathName);
        delPathFiles(materialSavePath);
    }
    
    
    /**
     * <删除文件夹下面的文件>
     * @param pathName    文件夹
     * @see [类、类#方法、类#成员]
     */
    private void delPathFiles(String pathName)
    {
        File directory = new File(pathName);
        Collection<File> fileList = FileUtils.listFiles(directory, null, true);
        if (null == fileList || fileList.isEmpty())
        {
            return;
        }
        
        for (File file : fileList)
        {
            LOG.debug("delete file, file name is {}", file.getName());
            FileUtils.deleteQuietly(file);
        }
    }
    
}
