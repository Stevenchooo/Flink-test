package com.huawei.wda.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import com.huawei.util.LogUtil;
import com.huawei.waf.core.WAFException;
import com.huawei.waf.core.run.MethodContext;

/**
 * 下载文件工具类 <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年6月14日]
 * @see [相关类/方法]
 */
public abstract class DownloadFileUtil
{
    private static final Logger LOG = LogUtil.getInstance();

    /**
     * 默认编码格式
     */
    private static final String DEFUALT_ENCODING = "utf-8";

    /**
     * txt文件后缀
     */
    private static final String FILE_NULL = "文件不存在.txt";

    /**
     * 编码格式iso-8859-1
     */
    private static final String ISO_ENCODING = "iso-8859-1";

    /**
     * 文件名正则
     */
    private static final String FILENAME_REG = "[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])"
            + "*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$";

    /**
     * 下载文件方法<功能详细描述>
     * 
     * @param reportRealPath
     *            文件物理路径
     * @param fileName
     *            文件名称
     * @param context
     *            上下文
     * @return boolean
     * @throws WAFException
     *             WAFException
     * @see [类、类#方法、类#成员]
     */
    public static boolean downLoadFile(String reportRealPath, String fileName,
            MethodContext context) throws WAFException
    {
        HttpServletResponse response = context.getResponse();
        HttpServletRequest request = context.getRequest();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ServletOutputStream out = null;
        try
        {
            File downFile = checkFile(reportRealPath);

            String agent = request.getHeader("User-Agent");
            // IE6-10的头
            boolean isMsie = agent != null && agent.indexOf("MSIE") != -1;
            // IE6-11的头
            boolean isMsieEleven = agent != null
                    && agent.indexOf("rv:11") != -1;
            if (!isValidFileName(fileName))
            {
                fileName = downFile.getName();
                if (!isValidFileName(fileName))
                {
                    LOG.error("downLoadFile failed: fileName isNotValid");
                    throw new WAFException("");
                }
            }
            out = response.getOutputStream();
            if (downFile.exists())
            {
                bis = new BufferedInputStream(new FileInputStream(downFile));
                bos = new BufferedOutputStream(out);

                if (isMsie || isMsieEleven)
                {
                    fileName = URLEncoder.encode(fileName, DEFUALT_ENCODING);
                    // 文件名空格变成+号问题
                    fileName = fileName.replace("+", "%20");
                }
                else
                {
                    fileName = new String(fileName.getBytes(DEFUALT_ENCODING),
                            ISO_ENCODING);
                    // 火狐文件名空格被截断问题
                    fileName = "\"" + fileName + "\"";
                }
                // 清空response
                response.reset();
                response.addHeader("Content-Length", "" + downFile.length());
                // 设置下载文件类型
                response.setContentType("application/octet-stream");
                // 设置下载文件头
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + fileName);

                byte[] buffer = new byte[1024];
                int len = bis.read(buffer);
                while (len != -1)
                {
                    bos.write(buffer, 0, len);
                    len = bis.read(buffer);
                }
                bos.flush();
            }
            else
            {
                if (isMsie || isMsieEleven)
                {
                    fileName = URLEncoder.encode(FILE_NULL, DEFUALT_ENCODING);
                }
                else
                {
                    fileName = new String(FILE_NULL.getBytes(DEFUALT_ENCODING),
                            ISO_ENCODING);
                }

                // 清空response
                response.reset();

                // 设置下载文件类型
                response.setContentType("text/plain;charset=utf-8");
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + fileName);
                String fileIsNotExist = "该报告文件不存在或报告已经被删除";
                out.write(fileIsNotExist.getBytes(DEFUALT_ENCODING));
            }

        }
        catch (FileNotFoundException e)
        {
            LOG.warn("downReportFile FileNotFoundException");
            return false;
        }
        catch (IOException e)
        {
            LOG.warn("downReportFile IOException");
            return false;
        }
        finally
        {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(out);
        }
        return true;
    }

    /**
     * 
     * 校验文件名是否规范
     * 
     * @param fileName
     *            fileName
     * @return 是 否
     * 
     */
    public static boolean isValidFileName(String fileName)
    {
        if (CommonUtils.isStringNullOrEmpty(fileName)
                || fileName.length() > 255)
        {
            return false;
        }
        return fileName.matches(FILENAME_REG);
    }

    /**
     * 
     * 判断文件名是否合法
     * 
     * @param fileName
     *            文件名
     * @return file文件
     * @throws UserInsightException
     *             文件不合法
     */
    public static File checkFile(String fileName) throws WAFException
    {
        String path = FilenameUtils.normalize(fileName);
        if (null == path || !path.equals(fileName))
        {
            LOG.warn("checkFile failed: file fath is dangerous");
            throw new WAFException("");
        }
        File file = new File(path);
        return file;
    }

    /**
     * 下载图片 <功能详细描述>
     * 
     * @param imagePath
     *            图片物理路径
     * @param context
     *            上下文
     * @return String
     * @throws WAFException
     *             WAFException
     * @see [类、类#方法、类#成员]
     */

    public static String downLoadImages(String imagePath, MethodContext context)
        throws WAFException
    {
        HttpServletResponse response = context.getResponse();
        ServletOutputStream out = null;
        OutputStream toClient = null;
        InputStream fileStream = null;
        int suffixStart = imagePath.lastIndexOf(".");
        int suffixend = imagePath.length();
        String fileSuffix = imagePath.substring(suffixStart, suffixend);
        try
        {
            File imgFile = checkFile(imagePath);
            if (imgFile.exists())
            {
                fileStream = new BufferedInputStream(new FileInputStream(
                        imgFile));
                byte[] buffer = new byte[1024];
                // 设置response的Header
                response.reset();
                response.addHeader("Content-Length", "" + imgFile.length());
                out = response.getOutputStream();
                toClient = new BufferedOutputStream(out);
                if (".GIF".equals(fileSuffix.toUpperCase(Locale.ENGLISH)))
                {
                    response.setContentType("image/gif");
                }
                else
                {
                    response.setContentType("image/jpeg");
                }

                int len = fileStream.read(buffer);
                while (len != -1)
                {
                    toClient.write(buffer, 0, len);
                    len = fileStream.read(buffer);
                }
                toClient.flush();
            }
        }
        catch (FileNotFoundException e)
        {
            LOG.warn("downLoadImages FileNotFoundException");

        }
        catch (IOException e)
        {
            LOG.warn("downLoadImages IOException");
        }
        finally

        {
            IOUtils.closeQuietly(fileStream);
            IOUtils.closeQuietly(toClient);
        }

        return null;

    }
}
