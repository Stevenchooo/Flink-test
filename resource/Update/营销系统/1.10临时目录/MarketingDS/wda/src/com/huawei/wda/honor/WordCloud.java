package com.huawei.wda.honor;

import java.io.IOException;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.manager.base.config.ConfigConstant;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.DefaultJavaProcessor;
import com.huawei.waf.protocol.RetCode;
import com.huawei.wda.http.HttpInvokerResp;
import com.huawei.wda.util.CommonUtils;
import com.huawei.wda.util.StringUtils;

/**
 * 社会舆情 <一句话功能简述> <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年6月2日]
 * @see [相关类/方法]
 */
public class WordCloud extends DefaultJavaProcessor
{
    /**
     * 日志接口类
     */
    private static final Logger LOGGER = LogUtil.getInstance();

    /**
     * 覆写的方法
     * 
     * @param context
     *            context
     * @return int
     */
    @Override
    public int process(MethodContext context)
    {

        String wordCloudData = obtainWordCloudData();
        JSONObject cloudDataObject = JSON.parseObject(wordCloudData);
        context.setResult("socialConditionData", cloudDataObject);

        return RetCode.OK;
    }

    /**
     * 获得数据<一句话功能简述> <功能详细描述>
     * 
     * @return String
     * @see [类、类#方法、类#成员]
     */
    public String obtainWordCloudData()
    {
        LOGGER.info("obtainSocialConditionData start...");

        // List<Integer> list = new ArrayList<Integer>();
        // 构造参数对象
        JSONObject params = new JSONObject();
        String currentDate = CommonUtils.obtainCurrentDate();
        params.put("currentDate", currentDate);
        // params.put("brandList", list);
        String url = StringUtils.getConfigInfo(ConfigConstant.WORD_CLOUD_IP);
        LOGGER.debug("obtainSocialConditionData, url = " + url);

        // 获取接口的返回值
        HttpInvokerResp resp = null;
        try
        {
            resp = CommonUtils.startHttpReq(url, params);
            LOGGER.info("obtainSocialConditionData end!");
            return resp.getRespBody();
        }
        catch (IOException e)
        {
            LOGGER.warn("obtainSocialConditionData IOException");
        }
        LOGGER.info("obtainSocialConditionData failed!");
        return "";
    }

}
