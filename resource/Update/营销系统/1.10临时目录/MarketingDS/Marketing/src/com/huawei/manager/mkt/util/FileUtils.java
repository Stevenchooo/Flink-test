/*
 * 文 件 名:  FileUtils.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-5
 */
package com.huawei.manager.mkt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;

import com.huawei.manager.mkt.info.MaterialInfo;
import com.huawei.manager.utils.Constant;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-5]
 * @see  [相关类/方法]
 */
public final class FileUtils
{
    
    //最大值
    private static final Integer MAX_SIZE = 1024;
    
    private static final String FILE_NOT_EXIST = "../../temp/file_not_exist.txt";
    
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * <默认构造函数>
     */
    private FileUtils()
    {
    }
    
    /**
     * <写文件操作>
     * @param resFile    文件名称
     * @param workbook   Excel对象
     * @see [类、类#方法、类#成员]
     */
    public static void writeFile(String resFile, HSSFWorkbook workbook)
    {
        // 生成文件  
        File file = new File(resFile);
        
        FileOutputStream fos = null;
        try
        {
            file.setWritable(true);
            fos = new FileOutputStream(file);
            workbook.write(fos);
        }
        catch (FileNotFoundException e)
        {
            LOG.error("wirte file error! exception is FileNotFoundException");
        }
        catch (IOException e)
        {
            LOG.error("wirte file error! exception is IOException");
        }
        finally
        {
            //关闭文件流
            IOUtils.closeQuietly(fos);
        }
    }
    
    /**
     * <将文件输入流写入文件>
     * @param inStream     文件输入流    
     * @param desFile      目标文件
     * @see [类、类#方法、类#成员]
     */
    public static void writeFile(InputStream inStream, String desFile)
    {
        
        File file = new File(desFile);
        
        FileOutputStream outStream = null;
        try
        {
            file.setWritable(true);
            outStream = new FileOutputStream(file);
            IOUtils.copy(inStream, outStream);
        }
        catch (IOException e)
        {
            LOG.error("write file error! exception is IOException, file path is {}", desFile);
        }
        finally
        {
            IOUtils.closeQuietly(outStream);
        }
        
    }
    
    /**
     * <将文件写入http消息>
     * @param context     系统上线问
     * @param filePath    文件目录
     * @see [类、类#方法、类#成员]
     */
    public static void writeHttpFileResponse(MethodContext context, String filePath)
    {
        ServletOutputStream out = null;
        
        FileInputStream in = null;
        
        try
        {
            File targetFile = new File(filePath);
            
            if (!targetFile.exists())
            {
                String defaultFilePath = FileUtils.class.getResource(Constant.SLANT).getPath() + FILE_NOT_EXIST;
                targetFile = new File(defaultFilePath);
                
            }
            
            //封装成http返回消息
            HttpServletResponse response = context.getResponse();
            response.setContentType("application/x-download;charset=utf-8");
            response.setHeader("pragma", "no-cache");
            String encodedfileName = new String(targetFile.getName().getBytes("utf-8"), "ISO8859-1");
            response.addHeader("Content-Disposition", "attachment;filename=\"" + encodedfileName + "\"");
            in = new FileInputStream(targetFile);
            out = response.getOutputStream();
            
            byte[] bytes = new byte[MAX_SIZE];
            int size = in.read(bytes);
            while (size >= 0)
            {
                out.write(bytes, 0, size);
                size = in.read(bytes);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.error("ad template downLoad error! Exception is UnsupportedEncodingException, file path is {}",
                filePath);
        }
        catch (FileNotFoundException e)
        {
            LOG.error("ad template downLoad error! Exception is FileNotFoundException, file path is {}", filePath);
        }
        catch (IOException e)
        {
            LOG.error("ad template downLoad error! Exception is IOException, file path is {}", filePath);
        }
        finally
        {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
    }
    
    /**
     * <创建新的文件目录>
     * @param path         文件目录
     * @return             创建文件目录是否OK
     * @see [类、类#方法、类#成员]
     */
    public static boolean createPath(String path)
    {
        try
        {
            org.apache.commons.io.FileUtils.forceMkdir(new File(path));
            return true;
        }
        catch (IOException e)
        {
            LOG.error("create Path error! exception is IOException,file path is {}", path);
        }
        
        return false;
        
    }
    
    /**
     * <zip压缩素材信息>
     * @param list       素材信息列表
     * @param filePath   压缩文件目录
     * @see [类、类#方法、类#成员]
     */
    public static void zipFiles(List<MaterialInfo> list, String filePath)
    {
        //系统参数检查
        if (null == filePath || null == list || list.isEmpty())
        {
            return;
        }
        
        //文件压缩
        ZipArchiveOutputStream zaos = null;
        try
        {
            File out = new File(filePath);
            zaos = new ZipArchiveOutputStream(out);
            
            //遍历列表
            for (MaterialInfo info : list)
            {
                //压缩单个广告位素材
                zipMaterialFile(zaos, info);
            }
            
            zaos.finish();
            zaos.close();
        }
        catch (IOException e)
        {
            LOG.error("zipFiles error! exception is IOException");
        }
        finally
        {
            IOUtils.closeQuietly(zaos);
        }
    }
    
    /**
     * <zip压缩文件>
     * @param zaos         zip压缩文件流
     * @param info         素材信息
     * @see [类、类#方法、类#成员]
     */
    private static void zipMaterialFile(ZipArchiveOutputStream zaos, MaterialInfo info)
    {
        if (null == info || null == info.getMaterialPath())
        {
            return;
        }
        
        File file = new File(info.getMaterialPath());
        
        if (!file.exists())
        {
            LOG.error("zip material file not exist! file name is {}", info.getMaterialPath());
            return;
        }
        
        try
        {
            String zipPath = info.getAdInfoId() + Constant.UNDERLINE + info.getAdInfoMediaType() + Constant.UNDERLINE
                + info.getAdInfoWebName() + Constant.UNDERLINE + info.getAdInfoChannel() + Constant.UNDERLINE
                + info.getAdInfoPosition() + File.separator + info.getMaterialName();
            //新建zip文件
            ZipArchiveEntry zae = new ZipArchiveEntry(file, zipPath);
            zaos.putArchiveEntry(zae);
            
            //拷贝文件
            copyFileInputStream(zaos, file);
            zaos.closeArchiveEntry();
        }
        catch (IOException e)
        {
            LOG.error("zipMaterialFile error! exception is IOException, material info is {}", info);
        }
        
    }
    
    /**
     * <拷贝文件>
     * @param zaos     压缩的zip文件
     * @param file     待zip压缩加入的文件
     * @see [类、类#方法、类#成员]
     */
    private static void copyFileInputStream(ZipArchiveOutputStream zaos, File file)
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file.getAbsolutePath());
            IOUtils.copy(fis, zaos);
        }
        catch (IOException e)
        {
            LOG.error("IOUtils copy file error! Exception is IOException and file name is {}", file.getName());
        }
        finally
        {
            IOUtils.closeQuietly(fis);
        }
    }
    
}
