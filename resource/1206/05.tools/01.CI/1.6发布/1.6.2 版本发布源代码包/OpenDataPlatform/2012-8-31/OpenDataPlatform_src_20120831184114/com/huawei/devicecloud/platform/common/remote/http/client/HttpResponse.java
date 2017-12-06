/*
 * 文 件 名:  HttpResponse.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  Http 响应对象 Response
 * 修 改 人:  z00190465
 * 修改时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.common.remote.http.client;

/**
 * Http 响应对象 Response
 * 
 * @author z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class HttpResponse
{
    /**
     * http正常响应状态
     */
    public static final int OK = 200;
    
    //默认端口
    private int defaultPort;
    
    //资源路径
    private String file;
    
    //主机
    private String host;
    
    //路径
    private String path;
    
    //端口
    private int port;
    
    //协议
    private String protocol;
    
    //url中的查询
    private String query;
    
    private String ref;
    
    private String userInfo;
    
    //内容编码
    private String contentEncoding;
    
    //内容
    private String content;
    
    //类型
    private String contentType;
    
    //http响应码
    private int code;
    
    //消息
    private String message;
    
    //方法
    private String method;
    
    //连接超时时间
    private int connectTimeout;
    
    //读取超时时间
    private int readTimeout;
    
    public void setDefaultPort(int defaultPort)
    {
        this.defaultPort = defaultPort;
    }
    
    public void setFile(String file)
    {
        this.file = file;
    }
    
    public void setHost(String host)
    {
        this.host = host;
    }
    
    public void setPath(String path)
    {
        this.path = path;
    }
    
    public void setPort(int port)
    {
        this.port = port;
    }
    
    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }
    
    public void setQuery(String query)
    {
        this.query = query;
    }
    
    public void setRef(String ref)
    {
        this.ref = ref;
    }
    
    public void setUserInfo(String userInfo)
    {
        this.userInfo = userInfo;
    }
    
    public void setContentEncoding(String contentEncoding)
    {
        this.contentEncoding = contentEncoding;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }
    
    public void setCode(int code)
    {
        this.code = code;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public void setMethod(String method)
    {
        this.method = method;
    }
    
    public void setConnectTimeout(int connectTimeout)
    {
        this.connectTimeout = connectTimeout;
    }
    
    public void setReadTimeout(int readTimeout)
    {
        this.readTimeout = readTimeout;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public String getContentType()
    {
        return contentType;
    }
    
    public int getCode()
    {
        return code;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public String getContentEncoding()
    {
        return contentEncoding;
    }
    
    public String getMethod()
    {
        return method;
    }
    
    public int getConnectTimeout()
    {
        return connectTimeout;
    }
    
    public int getReadTimeout()
    {
        return readTimeout;
    }
    
    public int getDefaultPort()
    {
        return defaultPort;
    }
    
    public String getFile()
    {
        return file;
    }
    
    public String getHost()
    {
        return host;
    }
    
    public String getPath()
    {
        return path;
    }
    
    public int getPort()
    {
        return port;
    }
    
    public String getProtocol()
    {
        return protocol;
    }
    
    public String getQuery()
    {
        return query;
    }
    
    public String getRef()
    {
        return ref;
    }
    
    public String getUserInfo()
    {
        return userInfo;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("HttpResponse [defaultPort=");
        builder.append(defaultPort);
        builder.append(", file=");
        builder.append(file);
        builder.append(", host=");
        builder.append(host);
        builder.append(", path=");
        builder.append(path);
        builder.append(", port=");
        builder.append(port);
        builder.append(", protocol=");
        builder.append(protocol);
        builder.append(", query=");
        builder.append(query);
        builder.append(", ref=");
        builder.append(ref);
        builder.append(", userInfo=");
        builder.append(userInfo);
        builder.append(", contentEncoding=");
        builder.append(contentEncoding);
        builder.append(", content=");
        builder.append(content);
        builder.append(", contentType=");
        builder.append(contentType);
        builder.append(", code=");
        builder.append(code);
        builder.append(", message=");
        builder.append(message);
        builder.append(", method=");
        builder.append(method);
        builder.append(", connectTimeout=");
        builder.append(connectTimeout);
        builder.append(", readTimeout=");
        builder.append(readTimeout);
        builder.append("]");
        return builder.toString();
    }
}
