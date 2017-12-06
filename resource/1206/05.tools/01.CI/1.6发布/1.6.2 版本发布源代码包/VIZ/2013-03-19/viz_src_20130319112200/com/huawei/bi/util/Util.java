package com.huawei.bi.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.task.bean.logger.IExeLogger;
import com.huawei.bi.task.bean.logger.TailedLogger;
import com.huawei.bi.task.bean.taskrunner.CommandUtil;
import com.huawei.bi.task.domain.Task;

public class Util
{
    private static Logger log = LoggerFactory.getLogger(Util.class);
    
    private static final int BUFFER_SIZE = 1024;
    
    private static long MIL_PER_SECOND = 1000;
    
    private static long SEC_PER_MIN = 60;
    
    private static long MIN_PER_HOUR = 60;
    
    public static String printMap(Map<String, String> map)
    {
        StringBuilder sb = new StringBuilder();
        for (String s : map.keySet())
        {
            String v = map.get(s);
            sb.append(s + ": " + v);
            sb.append("<br/>");
        }
        
        return sb.toString();
    }
    
    public static String printList(List<String> list)
    {
        StringBuilder sb = new StringBuilder();
        for (String s : list)
        {
            sb.append(s);
            sb.append("<br/>");
        }
        
        return sb.toString();
    }
    
    /**
     * 压缩产生的目标文件名
     * @param srcFileName 源文件名
     * @param dstFileName 目标文件名
     * @throws CException 异常
     */
    public static void compress(String srcFileName, String dstFileName)
        throws Exception
    {
        BufferedInputStream bis = null;
        ZipOutputStream zos = null;
        try
        {
            //输出文件
            File outfile = new File(dstFileName);
            //创建zip文件
            CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(outfile), new CRC32());
            zos = new ZipOutputStream(cos);
            
            //获取输入文件名
            File inputfile = new File(srcFileName);
            ZipEntry entry = new ZipEntry(inputfile.getName());
            //创建压缩包中的文件名
            zos.putNextEntry(entry);
            bis = new BufferedInputStream(new FileInputStream(inputfile));
            int count = 0;
            byte[] data = new byte[BUFFER_SIZE];
            //将文件流输出到压缩文件流中
            while (true)
            {
                count = bis.read(data, 0, BUFFER_SIZE);
                if (-1 == count)
                {
                    break;
                }
                
                zos.write(data, 0, count);
            }
        }
        catch (FileNotFoundException e)
        {
            //文件不存在异常
            log.error("compress failed! srcFileName is {},dstFileName is {}",
                new Object[] {srcFileName, dstFileName, e});
            throw e;
        }
        catch (IOException e)
        {
            //IO读写异常
            log.error("compress failed! srcFileName is {},dstFileName is {}",
                new Object[] {srcFileName, dstFileName, e});
            throw e;
        }
        finally
        {
            //关闭输入流
            close(bis);
            
            //关闭输出流
            close(zos);
        }
    }
    
    /**
     * 删除数据文件
     * @param fileName 文件
     */
    public static void deleteFile(String fileName)
    {
        //文件名为null直接返回
        if (null == fileName)
        {
            return;
        }
        
        try
        {
            //文件存在则删除，删除失败记录日志退出
            File file = new File(fileName);
            //文件存在
            if (file.exists())
            {
                if (!file.delete())
                {
                    //创建失败记录错误信息
                    log.error("delete file[{}] failed!", fileName);
                }
            }
        }
        catch (SecurityException e)
        {
            //记录错误信息
            log.error("delete file[{}] failed!", fileName, e);
        }
    }
    
    public static boolean exists(String fileName)
    {
        if (StringUtils.isBlank(fileName))
        {
            return false;
        }
        
        //文件存在则删除，删除失败记录日志退出
        File file = new File(fileName);
        //文件存在
        return file.exists();
    }
    
    /**
     * 关闭流
     * @param stream 流
     */
    public static void close(Closeable stream)
    {
        //流关闭方法
        if (null != stream)
        {
            try
            {
                stream.close();
            }
            catch (IOException e)
            {
                log.error("close stream failed!", e);
            }
        }
    }
    
    public static List<String> fileToList(String filePath)
        throws IOException
    {
        List<String> list = new ArrayList<String>();
        
        FileReader fr = null;
        BufferedReader br = null;
        try
        {
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null)
            {
                list.add(line);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            log.error("fileToList failed! filePath is  {}", filePath, e);
        }
        finally
        {
            if (null != br)
            {
                br.close();
            }
        }
        
        return list;
    }
    
    public static List<String> fileToList(String filePath, long limit)
        throws IOException
    {
        List<String> list = new ArrayList<String>();
        
        FileReader fr = null;
        BufferedReader br = null;
        try
        {
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);
            String line;
            //不允许超过限制
            while ((line = br.readLine()) != null && list.size() < limit)
            {
                list.add(line);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            log.error("fileToList failed! filePath is  {}, limit is {}.", new Object[] {filePath, limit, e});
        }
        finally
        {
            //关闭流
            if (null != br)
            {
                br.close();
            }
        }
        
        return list;
    }
    
    public static String httpRequestInfo(HttpServletRequest request, HttpServletResponse response)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Servlet Context request [getAttributeNames()=");
        builder.append(request.getAttributeNames());
        builder.append(", getAuthType()=");
        builder.append(request.getAuthType());
        builder.append(", getCharacterEncoding()=");
        builder.append(request.getCharacterEncoding());
        builder.append(", getContentLength()=");
        builder.append(request.getContentLength());
        builder.append(", getContentType()=");
        builder.append(request.getContentType());
        builder.append(", getContextPath()=");
        builder.append(request.getContextPath());
        builder.append(", getCookies()=");
        builder.append(Arrays.toString(request.getCookies()));
        builder.append(", getHeaderNames()=");
        builder.append(request.getHeaderNames());
        builder.append(", getLocalAddr()=");
        builder.append(request.getLocalAddr());
        //        builder.append(", getLocalName()=");
        //        builder.append(request.getLocalName());
        builder.append(", getLocalPort()=");
        builder.append(request.getLocalPort());
        builder.append(", getLocale()=");
        builder.append(request.getLocale());
        builder.append(", getLocales()=");
        builder.append(request.getLocales());
        builder.append(", getMethod()=");
        builder.append(request.getMethod());
        builder.append(", getParameterMap()=");
        builder.append(request.getParameterMap());
        builder.append(", getParameterNames()=");
        builder.append(request.getParameterNames());
        builder.append(", getPathInfo()=");
        builder.append(request.getPathInfo());
        builder.append(", getPathTranslated()=");
        builder.append(request.getPathTranslated());
        builder.append(", getProtocol()=");
        builder.append(request.getProtocol());
        builder.append(", getQueryString()=");
        builder.append(request.getQueryString());
        builder.append(", getRemoteAddr()=");
        builder.append(request.getRemoteAddr());
        builder.append(", getRemoteHost()=");
        builder.append(request.getRemoteHost());
        builder.append(", getRemotePort()=");
        builder.append(request.getRemotePort());
        builder.append(", getRemoteUser()=");
        builder.append(request.getRemoteUser());
        builder.append(", getRequestURI()=");
        builder.append(request.getRequestURI());
        builder.append(", getRequestURL()=");
        builder.append(request.getRequestURL());
        builder.append(", getRequestedSessionId()=");
        builder.append(request.getRequestedSessionId());
        builder.append(", getScheme()=");
        builder.append(request.getScheme());
        builder.append(", getServerName()=");
        builder.append(request.getServerName());
        builder.append(", getServerPort()=");
        builder.append(request.getServerPort());
        builder.append(", getServletPath()=");
        builder.append(request.getServletPath());
        builder.append(", getSession()=");
        builder.append(request.getSession(false));
        builder.append(", getUserPrincipal()=");
        builder.append(request.getUserPrincipal());
        builder.append(", isRequestedSessionIdFromCookie()=");
        builder.append(request.isRequestedSessionIdFromCookie());
        builder.append(", isRequestedSessionIdFromURL()=");
        builder.append(request.isRequestedSessionIdFromURL());
        builder.append(", isRequestedSessionIdValid()=");
        builder.append(request.isRequestedSessionIdValid());
        builder.append(", isSecure()=");
        builder.append(request.isSecure());
        builder.append("TransactionContext response [getCharacterEncoding()=");
        builder.append(response.getCharacterEncoding());
        builder.append(", getContentType()=");
        builder.append(response.getContentType());
        builder.append(", getLocale()=");
        builder.append(response.getLocale());
        builder.append("]");
        return builder.toString();
    }
    
    public static boolean isDisplaceOrDownload(String exeId)
    {
        String resultDir = Config.get("task.exe.result.dir") + "/" + exeId;
        File file = new File(resultDir);
        return file.length() > 1024 * 1024;
    }
    
    public static String getExeIDPath(String exeId)
    {
        return Config.get("task.exe.result.dir") + "/" + exeId;
    }
    
    /** 产生exeID
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String generateExeId()
    {
        return UUID.randomUUID().toString().replace("-", "_");
    }
    
    /** 将文件流写出到response的out流中
     * @param response HttpServletResponse
     * @param file 文件
     * @throws IOException 异常
     * @see [类、类#方法、类#成员]
     */
    public static void writeResult(HttpServletResponse response, String file)
        throws IOException
    {
        OutputStream outp = null;
        InputStream in = null;
        try
        {
            outp = response.getOutputStream();
            in = new FileInputStream(file);
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = in.read(b)) != -1)
            {
                outp.write(b, 0, i);
            }
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (Exception e)
                {
                    log.warn("Cloes input stream exception.", e);
                }
                in = null;
            }
            if (outp != null)
            {
                try
                {
                    outp.close();
                }
                catch (Exception e)
                {
                    log.warn("Cloes output stream exception.", e);
                }
                outp = null;
            }
            //删除文件
           /* 
            File delFile = new File(file);
            if (delFile.exists())
            {
                if (!delFile.delete())
                {
                    log.warn(file + " delete failed.");
                }
            }
            */
            
        }
    }
    
    /** 简单sql的分析
     * @param sql
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static List<String> getSqlColumns(String sql)
    {
        List<String> list = new ArrayList<String>();
        String reg = "\\s*select\\s+(.+)from\\s+";
        Pattern pc = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher mc = pc.matcher(sql);
        while (mc.find())
        {
            String c = mc.group(1);
            if (null != c)
            {
                String[] colArray = c.split(",");
                if (null != colArray && colArray.length > 0)
                {
                    for (int i = 0; i < colArray.length; i++)
                    {
                        list.add(colArray[i].trim());
                    }
                }
            }
        }
        return list;
    }
    
    /** 获取表对应的列
     * @param table 表名
     * @return
     * @throws IOException 
     * @see [类、类#方法、类#成员]
     */
    public static String getCloumns(Task task, String[] tables)
        throws IOException
    {
        //获取sql中的表名
        StringBuffer sb = new StringBuffer();
        if (tables != null && tables.length > 0)
        {
            StringBuffer execCode = new StringBuffer();
            for (String table : tables)
            {
                String curTable = table;
                int index = table.indexOf(".");
                if (index != -1)
                {
                    curTable = table.substring(index + 1);
                }
                execCode.append("desc ").append(curTable).append(";");
            }
            IExeLogger exeLogger = new TailedLogger(generateExeId());
            CommandUtil.run(task, execCode.toString(), task.getTaskId(), exeLogger);
            log.debug(Arrays.toString(exeLogger.readDebug().toArray()));
            List<String> columns = exeLogger.readResult();
            if (!columns.isEmpty())
            {
                for (String column : columns)
                {
                    String[] values = column.split("\t");
                    sb.append(values[0]).append("\t");
                }
            }
        }
        return sb.toString();
    }
    
    public static String getTableInfo(Task task, String table)
        throws IOException
    {
        //获取sql中的表名
        String execCode = "desc " + table;
        IExeLogger exeLogger = new TailedLogger(task.getTaskId());
        CommandUtil.run(task, execCode, task.getTaskId(), exeLogger);
        log.debug(Arrays.toString(exeLogger.readDebug().toArray()));
        List<String> columns = exeLogger.readResult();
        StringBuffer sb = new StringBuffer();
        if (!columns.isEmpty())
        {
            for (String column : columns)
            {
                String[] values = column.split("\t");
                sb.append(values[0]);
                sb.append("|");
                sb.append(values[1]);
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    /**
     * curr是否在start和end之间
     * @param start 开始时间
     * @param end 结束时间
     * @param curr 当前时间
     * @return curr是否在start和end之间
     */
    public static boolean between(Date start, Date end, Date curr)
    {
        if (null == curr)
        {
            return false;
        }
        
        if (null != start && null != end)
        {
            return curr.after(start) && curr.before(end);
        }
        else if (null != start && null == end)
        {
            return curr.after(start);
        }
        else if (null == start && null != end)
        {
            return curr.before(end);
        }
        else
        {
            return true;
        }
    }
    
    public static String formatDiff(Date start, Date end)
    {
        String showDiff = "";
        if (null != start && null != end)
        {
            long diff = end.getTime() - start.getTime();
            //转换成秒
            diff = diff / MIL_PER_SECOND;
            long hours = diff / (SEC_PER_MIN * MIN_PER_HOUR);
            diff = diff % (SEC_PER_MIN * MIN_PER_HOUR);
            long mins = diff / SEC_PER_MIN;
            long seconds = diff % SEC_PER_MIN;
            
            if (0 != hours)
            {
                showDiff = "<font color='red'>" + hours + "</font>小时";
            }
            if (0 != mins)
            {
                showDiff += "<font color='red'>" + mins + "</font>分钟";
            }
            if (0 != seconds || "" == showDiff)
            {
                showDiff += "<font color='red'>" + seconds + "</font>秒";
            }
        }
        return showDiff;
    }
}
