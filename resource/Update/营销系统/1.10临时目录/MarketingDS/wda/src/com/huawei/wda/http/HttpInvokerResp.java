package com.huawei.wda.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

import com.huawei.wda.util.CommonUtils;

/**
 * http响应
 * 
 * @author yWX289656
 * 
 */
public class HttpInvokerResp
{
    /**
     * 状态码
     */
    private int statusCode = -1;

    /**
     * 响应体
     */
    private String respBody;

    /**
     * 构造函数
     * 
     * @param httpResponse
     *            响应
     * @throws IOException
     *             IOException
     */
    public HttpInvokerResp(HttpResponse httpResponse) throws IOException
    {
        if (null == httpResponse)
        {
            throw new IOException("httpResponse is null");
        }
        else
        {
            StatusLine statusLine = httpResponse.getStatusLine();
            if (null != statusLine)
            {
                statusCode = statusLine.getStatusCode();
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity)
                {
                    respBody = EntityUtils.toString(entity,
                            CommonUtils.DEFUALT_ENCODING);
                }

            }
            else
            {
                throw new IOException("statusLine is null");
            }
        }
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getRespBody()
    {
        return respBody;
    }

    public void setRespBody(String respBody)
    {
        this.respBody = respBody;
    }

    /**
     * 获取该实例的表示
     * 
     * @return String 该实例的表示
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("HttpInvokerResp [statusCode=");
        builder.append(statusCode);
        builder.append(", respBody=");
        builder.append(respBody);
        builder.append(']');
        return builder.toString();
    }

}
