/*
 * 文 件 名:  FileServer.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-4-11
 */
package com.huawei.devicecloud.platform.bi.servlet;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.huawei.devicecloud.platform.bi.common.CException;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Open Data Platform Service, 2013-4-11]
 * @see  [相关类/方法]
 */
public class FileServer extends HttpServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 7413957448947957680L;
    
    /**
     * 文件路径
     */
    private String filePath;
    
    
    /**
     * servlet初始化
     * @param config    配置内容
     * @throws ServletException   ServletException异常
     */
    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        filePath = config.getInitParameter("filePath");
        filePath = getServletContext().getRealPath(filePath);
        
    }
    
    /**
     * 文件上传servlet
     * @param req  消息请求
     * @param rsp  消息返回
     * @throws ServletException  ServletException异常
     * @throws IOException  IOException异常
     */
    @SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest req, HttpServletResponse rsp)
        throws ServletException, IOException
    {
        try
        {
            //获取鉴权头
            Enumeration<?> e = req.getHeaderNames();
            String authenInfo = null;
            while (e.hasMoreElements()) 
            {
                String name = (String)e.nextElement();
                String value = req.getHeader(name);
                if (name.equalsIgnoreCase("Authorization"))
                {
                    authenInfo = value;
                    break;
                }
            }
            
            //鉴权校验
            
            
            
            /**获取上传参数*/
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(-1);
            ServletFileUpload load = new ServletFileUpload(factory);
            load.setFileSizeMax(-1);
            load.setSizeMax(-1);
            load.setHeaderEncoding("UTF-8");
            List<FileItem> fileItemList = load.parseRequest(req);
            Map<String, String> formFiledParams = getParams(fileItemList);
            for (int i = 0; i < fileItemList.size(); i++)
            {
                FileItem fileItem = fileItemList.get(i);
                if (!fileItem.isFormField())
                {
                    
                    if (fileItem.getSize() > 0)
                    {
                        try
                        {
                            String fileName = fileItem.getName();
                            int index = fileName.lastIndexOf("\\");
                            fileName = fileName.substring(index + 1, fileName.length());
                            File file = new File(filePath);
                            if (!file.exists())
                            {
                                file.mkdir();
                            }
                            fileItem.write(new File(filePath + File.separator + fileName));
                        }
                        catch (FileUploadException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                }
            }
            
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param fileItemList
     * @return
     * @throws UnsupportedEncodingException
     * @throws CException
     * @see [类、类#方法、类#成员]
     */
    private Map<String, String> getParams(List<FileItem> fileItemList)
        throws UnsupportedEncodingException
    {
        Map<String, String> paramMap = new HashMap<String, String>();
        for (FileItem fileItem : fileItemList)
        {
            if (fileItem.isFormField())
            {
                if (fileItem.getFieldName().equals("app_id"))
                {
                    paramMap.put("app_id", fileItem.getString("UTF-8"));
                }
                else if (fileItem.getFieldName().equals("timestamp"))
                {
                    paramMap.put("timestamp", fileItem.getString("UTF-8"));
                }
                else if (fileItem.getFieldName().equals("file_type"))
                {
                    paramMap.put("file_type", fileItem.getString("UTF-8"));
                }
            }
            
        }
        return paramMap;
    }
    
}
