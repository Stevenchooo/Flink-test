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
 * 品牌声量 <功能详细描述>
 * 
 * @author yWX302483
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年6月15日]
 * @see [相关类/方法]
 */
public class BrandVoice extends DefaultJavaProcessor
{
    /**
     * 日志接口类
     */
    private static final Logger LOGGER = LogUtil.getInstance();

    /**
     * 复写方法
     * 
     * @param context
     *            上下文
     * @return int
     */
    @Override
    public int process(MethodContext context)
    {
        String brandVoiceData = obtainBrandVoiceData();
        JSONObject brandVoiceDataObject = JSON.parseObject(brandVoiceData);
        context.setResult("brandVoiceData", brandVoiceDataObject);
        return RetCode.OK;
    }

    /**
     * 获取品牌声量的数据 <功能详细描述>
     * 
     * @return String
     * @see [类、类#方法、类#成员]
     */
    public String obtainBrandVoiceData()
    {
        LOGGER.info("obtainBrandVoiceData start...");
        JSONObject params = new JSONObject();
        String currentDate = CommonUtils.obtainCurrentDate();
        params.put("currentDate", currentDate);
        // 获得品牌声量的外链ip
        String url = StringUtils.getConfigInfo(ConfigConstant.BRAND_VOICE_IP);
        // 获取接口的返回值
        HttpInvokerResp resp = null;
        try
        {
            resp = CommonUtils.startHttpReq(url, params);
            LOGGER.info("obtainBrandVoiceData end!");
            return resp.getRespBody();
        }
        catch (IOException e)
        {
            LOGGER.warn("obtainBrandVoiceData IOException");
        }
        LOGGER.info("obtainBrandVoiceData failed!");
        return "";
    }

}
