package com.huawei.wda.http;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
/**
 * 请求方法类型
 * @author yWX289656
 *
 */
public enum HttpMethodType
{
    /**
     * POST方法
     */
    POST
    {
        /**
         * 获取http请求
         * @param uri 请求地址
         * @return HttpRequestBase http请求
         */
        @Override
        public HttpRequestBase getHttpRequest(String uri)
        {
            return new HttpPost(uri);
        }
    },
    
    /**
     * GET方法
     */
    GET
    {
        /**
         * 获取http请求
         * @param uri 请求地址
         * @return HttpRequestBase http请求
         */
        @Override
        public HttpRequestBase getHttpRequest(String uri)
        {
            return new HttpGet(uri);
        }
    };

    /**
     * 获取http请求
     * @param uri 请求地址
     * @return HttpRequestBase http请求
     */
    public HttpRequestBase getHttpRequest(String uri)
    {
        return null;
    }
}
