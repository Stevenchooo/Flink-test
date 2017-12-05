package com.huawei.wda.http;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;

import com.huawei.util.LogUtil;

/**
 * http调用工具类
 * 
 * @author yWX289656
 * 
 */
public final class HttpInvokerUtils
{
    /**
     * 日志接口类
     */
    private static final Logger LOGGER = LogUtil.getInstance();

    /**
     * 私有构造函数
     */
    private HttpInvokerUtils()
    {
        throw new AssertionError(
                "No com.huawei.wda.http.HttpInvokerUtils instances for you!");
    }

    /**
     * 发送http消息
     * 
     * @param req
     *            请求
     * @return HttpInvokerResp 响应
     * @throws IOException
     *             输入输出异常
     */
    public static HttpInvokerResp sendMsg(HttpInvokerReq req)
        throws IOException
    {
        if (null == req)
        {
            LOGGER.warn("HttpInvokerReq is null");
            throw new IOException("HttpInvokerReq is null");
        }
        CloseableHttpClient client = null;
        HttpRequestBase httpRequest = null;
        CloseableHttpResponse httpResponse = null;
        try
        {
            client = req.createClient();

            httpRequest = req.createHttpRequest();

            httpResponse = client.execute(httpRequest);

            HttpInvokerResp resp = new HttpInvokerResp(httpResponse);
            return resp;
        }
        finally
        {
            IOUtils.closeQuietly(httpResponse);
            IOUtils.closeQuietly(client);

        }

    }

    /**
     * 发送json字符串
     * 
     * @param uri
     *            请求地址
     * @param jsonStr
     *            json字符串
     * @param timeOut
     *            超时
     * @return HttpInvokerResp 响应，不会为空
     * @throws IOException
     *             IOException
     */
    public static HttpInvokerResp sendJSONMsg(String uri, String jsonStr,
            Integer timeOut) throws IOException
    {
        HttpInvokerReq req = new HttpInvokerReq();
        req.setUri(uri);
        req.setBodyType(RequestBodyType.SRTING);
        req.setContent(jsonStr);
        req.setContentType(ContentType.JSON);
        if (null != timeOut)
        {
            req.setTimeOut(timeOut);
        }
        req.setType(HttpMethodType.POST);

        return HttpInvokerUtils.sendMsg(req);

    }

}
