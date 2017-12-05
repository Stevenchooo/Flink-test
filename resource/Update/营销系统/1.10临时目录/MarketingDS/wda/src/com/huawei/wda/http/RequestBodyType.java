package com.huawei.wda.http;

import java.io.IOException;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.huawei.wda.util.CommonUtils;

/**
 * 请求体类型
 * 
 * @author yWX289656
 * 
 */
public enum RequestBodyType
{
    /**
     * 字符串类型的请求类型
     */
    SRTING
    {
        /**
         * 构造请求体
         * 
         * @param req
         *            请求内容
         * @return HttpEntity 请求体
         * @throws IOException
         *             IOException
         */
        @Override
        protected HttpEntity buildRequestEntiry(HttpInvokerReq req)
            throws IOException
        {
            return new StringEntity(req.getContent(), ContentType.create(
                    req.getMimeType(), Consts.UTF_8));
        }
    },

    /**
     * name=value请求类型
     */
    NAME_VALUE_PAIR
    {
        /**
         * 构造请求体
         * 
         * @param req
         *            请求内容
         * @return HttpEntity 请求体
         * @throws IOException
         *             IOException
         */
        @Override
        protected HttpEntity buildRequestEntiry(HttpInvokerReq req)
            throws IOException
        {
            List<NameValuePair> nameValues = req.getNameValues();
            if (null == nameValues || nameValues.isEmpty())
            {
                return null;
            }

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValues,
                    CommonUtils.DEFUALT_ENCODING);

            return entity;

        }
    };

    /**
     * 构造请求体
     * 
     * @param req
     *            请求内容
     * @return HttpEntity 请求体
     * @throws IOException
     *             IOException
     */
    protected HttpEntity buildRequestEntiry(HttpInvokerReq req)
        throws IOException
    {
        return null;
    }

    /**
     * 构建请求
     * 
     * @param httpRequest
     *            httpRequest
     * @param req
     *            req
     * @return HttpRequestBase httpRequest
     * @throws IOException
     *             IOException
     */
    public HttpRequestBase combine(HttpRequestBase httpRequest,
            HttpInvokerReq req) throws IOException
    {
        HttpEntity httpEntity = this.buildRequestEntiry(req);
        if (httpRequest instanceof HttpPost)
        {
            ((HttpPost) httpRequest).setEntity(httpEntity);
        }

        return httpRequest;
    }
}