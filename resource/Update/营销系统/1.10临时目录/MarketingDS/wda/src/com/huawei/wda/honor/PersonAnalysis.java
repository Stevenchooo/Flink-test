package com.huawei.wda.honor;

import java.io.IOException;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.DefaultJavaProcessor;
import com.huawei.waf.protocol.RetCode;
import com.huawei.wda.http.HttpInvokerResp;
import com.huawei.wda.util.CommonUtils;
import com.huawei.wda.util.StringUtils;

/**
 * 人群分析 <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年6月2日]
 * @see [相关类/方法]
 */
public class PersonAnalysis extends DefaultJavaProcessor
{
    /**
     * 日志接口类
     */
    private static final Logger LOGGER = LogUtil.getInstance();

    private static final String HONOR_ONE = "荣耀";

    private static final String HONOR_WAN = "荣耀畅玩";

    /**
     * 覆写的方法
     * 
     * @param context
     *            上下文
     * @return int
     */
    @Override
    public int process(MethodContext context)
    {
        String questionData = obtainQuestionData(HONOR_ONE);
        String questionData1 = obtainQuestionData(HONOR_WAN);
        JSONObject data = JSON.parseObject(questionData);
        JSONObject data1 = JSON.parseObject(questionData1);
        context.setResult("personAnalysisData", data);
        context.setResult("personAnalysisData1", data1);
        return RetCode.OK;
    }

    /**
     * 获取问卷的数据<一句话功能简述> <功能详细描述>
     * 
     * @param brand
     *            brand
     * @return String
     * @see [类、类#方法、类#成员]
     */
    public String obtainQuestionData(String brand)
    {

        LOGGER.info("obtainQuestionData start...");

        // 构造参数对象
        JSONObject params = new JSONObject();

        params.put("searchKeyWord", brand);

        String url = StringUtils.getConfigInfo("personAnalysis");
        LOGGER.debug("obtainQuestionData, url = " + url);

        // 获取接口的返回值
        HttpInvokerResp resp = null;
        try
        {
            resp = CommonUtils.startHttpReq(url, params);
            LOGGER.info("obtainQuestionData end!");
            return resp.getRespBody();
        }
        catch (IOException e)
        {
            LOGGER.warn("obtainQuestionData IOException");
        }
        LOGGER.info("obtainQuestionData failed!");
        return "";
    }

}
