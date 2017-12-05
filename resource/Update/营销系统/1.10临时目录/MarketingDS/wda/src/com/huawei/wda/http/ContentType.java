package com.huawei.wda.http;

/**
 * mime类型
 * 
 * @author yWX289656
 *
 */
public enum ContentType
{
    /**
     * JSON
     */
    JSON("application/json"),

    /**
     * text
     */
    TEXT("text/xml"),;

    private String mimeType;

    private ContentType(String mimeType)
    {
        this.mimeType = mimeType;
    }

    /**
     * 获取请求头中的文件类型表示
     * 
     * @return String 请求头中的文件类型表示
     */
    public String getContentTypeHeader()
    {
        return mimeType + ";charset=UTF-8";
    }

    /**
     * 获取Mime类型
     * 
     * @return String Mime类型
     */
    public String getMimeType()
    {
        return mimeType;
    }
}
