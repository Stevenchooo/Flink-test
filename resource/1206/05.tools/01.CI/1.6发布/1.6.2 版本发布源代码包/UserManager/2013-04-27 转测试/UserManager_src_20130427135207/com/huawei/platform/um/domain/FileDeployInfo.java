/*
 * 文 件 名:  FileDeployInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-26
 */
package com.huawei.platform.um.domain;

import java.util.Set;

/**
 * 文件部署新型
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-26]
 */
public class FileDeployInfo
{
    //默认端口
    private static int def_port = 22;
    
    private String host;
    
    private Integer port;
    
    private String userName;
    
    private String password;
    
    private String dir;
    
    private String fileName;
    
    private boolean rename;
    
    private Set<String> checkFileNames;
    
    private boolean decompress;
    
    private String visitedUrl;
    
    public boolean isRename()
    {
        return rename;
    }

    public void setRename(boolean rename)
    {
        this.rename = rename;
    }

    public Set<String> getCheckFileNames()
    {
        return checkFileNames;
    }
    
    public void setCheckFileNames(Set<String> checkFileNames)
    {
        this.checkFileNames = checkFileNames;
    }
    
    public String getVisitedUrl()
    {
        return visitedUrl;
    }
    
    public void setVisitedUrl(String visitedUrl)
    {
        this.visitedUrl = visitedUrl;
    }
    
    public boolean isDecompress()
    {
        return decompress;
    }
    
    public void setDecompress(boolean decompress)
    {
        this.decompress = decompress;
    }
    
    public Integer getPort()
    {
        if (null == port || port <= 0)
        {
            return def_port;
        }
        else
        {
            return port;
        }
    }
    
    public void setPort(Integer port)
    {
        this.port = port;
    }
    
    public String getHost()
    {
        return host;
    }
    
    public void setHost(String host)
    {
        this.host = host;
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getDir()
    {
        return dir;
    }
    
    public void setDir(String dir)
    {
        this.dir = dir;
    }
    
    public String getFileName()
    {
        return fileName;
    }
    
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
