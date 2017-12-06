/*
 * 文 件 名:  OdpFileUtils.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-4-13
 */
package com.huawei.devicecloud.platform.bi.common.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;


/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Open Data Platform Service, 2013-4-13]
 * @see  [相关类/方法]
 */
public final class OdpFileUtils
{
    //缓存大小  
    private static final int BUFFER = 1024;
    
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(OdpFileUtils.class);
    
    
    /**
     * <解压缩文件>
     * @param zipFileName   zip文件名
     * @param fileDir       文件解压地址
     * @throws CException   解压缩文件失败
     * @see [类、类#方法、类#成员]
     */
    public static void upZipFile(String zipFileName, String fileDir)
        throws CException
    {
        LOGGER.info("enter function upZipFile! ZIP_FILENAME is {} and ZIP_DIR is {}", new Object[] {zipFileName,
            fileDir});
        try
        {
            ZipFile zfile = new ZipFile(zipFileName);
            Enumeration<? extends ZipEntry> zList = zfile.entries();
            ZipEntry ze = null;
            byte[] buf = new byte[BUFFER];
            while (zList.hasMoreElements())
            {
                ze = (ZipEntry)zList.nextElement();
                if (ze.isDirectory())
                {
                    File f = new File(fileDir + File.separator + ze.getName());
                    f.mkdir();
                    continue;
                }
                
                OutputStream os =
                    new BufferedOutputStream(new FileOutputStream(getRealFileName(fileDir, ze.getName())));
                InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
                int readLen = 0;
                while ((readLen = is.read(buf, 0, BUFFER)) != -1)
                {
                    os.write(buf, 0, readLen);
                }
                is.close();
                os.close();
            }
            zfile.close();
        }
        catch (Exception e)
        {
            LOGGER.error("exit function upZipFile Error, Exception is {}", new Object[] {e});
            throw new CException(ResultCode.UNZIP_ERROR, e);
            
        }
        
    }
    
    
    /**  
     * 给定根目录，返回一个相对路径所对应的实际文件名.  
     * @param baseDir 指定根目录  
     * @param absFileName 相对路径名，来自于ZipEntry中的name  
     * @return java.io.File 实际的文件  
     */
    public static File getRealFileName(String baseDir, String absFileName)
    {
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        if (dirs.length > 0)
        {
            for (int i = 0; i < dirs.length - 1; i++)
            {
                ret = new File(ret, dirs[i]);
            }
            if (!ret.exists())
            {
                ret.mkdirs();
            }
            ret = new File(ret, dirs[dirs.length - 1]);
            return ret;
        }
        return ret;
    }
    
    
    /**  
     * 取得指定目录下的所有文件列表，包括子目录. 也可以直接压缩文件 
     * @param baseDir File 指定的目录  
     * @return 包含java.io.File的List  
     */
    public static List<String> getSubFiles(File baseDir)
    {
        ArrayList<String> ret = new ArrayList<String>();
        File[] tmp = baseDir.listFiles();
        
        if (null == tmp)
        {
            return null;
        }
        for (int i = 0; i < tmp.length; i++)
        {
            if (tmp[i].isFile())
            {
                ret.add(tmp[i].getAbsolutePath());
            }
            if (tmp[i].isDirectory())
            {
                ret.addAll(getSubFiles(tmp[i]));
            }
        }
        return ret;
    }
    
    /**
     * <获取文件ID>
     * @return   文件ID
     * @see [类、类#方法、类#成员]
     */
    public static String getFileId()
    {
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String fileId = df.format(date);
        fileId = fileId.replaceAll("-", "");
        fileId = fileId.replaceAll(" ", "");
        fileId = fileId.replaceAll(":", "");
        
        //在生成六位随机吗
        int radom = (int)((Math.random() * 9 + 1) * 100000);
        fileId = fileId + radom;
        return fileId;
    }
}
