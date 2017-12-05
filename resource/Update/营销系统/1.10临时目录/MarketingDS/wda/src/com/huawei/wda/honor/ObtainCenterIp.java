package com.huawei.wda.honor;

import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.DefaultJavaProcessor;
import com.huawei.waf.protocol.RetCode;
//import com.huawei.wda.util.AES128;
import com.huawei.wda.util.StringUtils;

/**
 * 
 * 获取首页其他网站的地址 <功能详细描述>
 * 
 * @author cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月31日]
 * @see [相关类/方法]
 */
public class ObtainCenterIp extends DefaultJavaProcessor
{

    private static final String MARKET_IP = "marketIp";

    private static final String QUESTIONAIRE_IP = "questionnaireIp";

    private static final String WDA_IP = "wdaIp";

    private static final String SOCIAL_PUBLIC_OPINION_IP = "socialPublicOpinionIp";

    private static final String USERINSIGHT_IP = "userInsightIp";

    // private static final String SOCIAL_USERNAME = "socialUserName";

    // private static final String SOCIAL_PWD = "socialPwd";

    // private static final String SOCIAL_KEY = "socialKey";
    /**
     * process
     * 
     * @param context
     *            上下文
     * @return int
     */
    @Override
    public int process(MethodContext context)
    {
        // TODO Auto-generated method stub
        // byte[] encodeUserName =
        // AES128.encrypt(StringUtils.getConfigInfo(SOCIAL_USERNAME),
        // StringUtils.getConfigInfo(SOCIAL_KEY));
        // byte[] encodePassword =
        // AES128.encrypt(StringUtils.getConfigInfo(SOCIAL_PWD),
        // StringUtils.getConfigInfo(SOCIAL_KEY));
        context.setResult(MARKET_IP, StringUtils.getConfigInfo(MARKET_IP));
        context.setResult(QUESTIONAIRE_IP,
                StringUtils.getConfigInfo(QUESTIONAIRE_IP));
        context.setResult(WDA_IP, StringUtils.getConfigInfo(WDA_IP));
        context.setResult(SOCIAL_PUBLIC_OPINION_IP,
                StringUtils.getConfigInfo(SOCIAL_PUBLIC_OPINION_IP));
        context.setResult(USERINSIGHT_IP,
                StringUtils.getConfigInfo(USERINSIGHT_IP));
        // context.setResult(SOCIAL_USERNAME,encodeUserName.toString());
        // context.setResult(SOCIAL_PWD,encodePassword.toString());
        return RetCode.OK;
    }

}
