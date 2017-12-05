package com.huawei.waf.core.config;

import com.huawei.util.DBUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.config.method.parameter.sys.UUIDParameterInfo;
import com.huawei.waf.core.config.method.process.BalanceRDBProcessConfig;
import com.huawei.waf.core.config.method.process.HttpProcessConfig;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.facade.config.AbstractProcessConfig;
import com.huawei.waf.facade.config.AbstractRequestConfig;
import com.huawei.waf.facade.config.AbstractResponseConfig;
import com.huawei.waf.facade.run.AbstractProcessor;
import com.huawei.waf.facade.run.AbstractRequester;
import com.huawei.waf.facade.run.AbstractResponser;
import com.huawei.waf.protocol.Const;
import com.huawei.waf.protocol.RetCode;

public class Configuration {
	public static final boolean setDbBalancer(String balancerCls) {
		return DBUtil.setBalancer(balancerCls);
	}
	
	public static final void setDefaultAuthType(int authType) {
		MethodConfig.setDefaultAuthType(authType);
	}

    /**
     * @param type 接口类型的名称，对应每个接口配置中的type
     * @param requestCfgCls 请求配置解析类
     * @param procCfgCls 处理配置解析类
     * @param responseCfgCls 响应配置解析类
     * @param requester 请求消息处理类
     * @param processor 业务处理类
     * @param responser 响应消息处理类
     * @return
     */
    public static final boolean addMethodType(
            String type,
            Class<? extends AbstractRequestConfig> requestCfgCls,
            Class<? extends AbstractProcessConfig> procCfgCls,
            Class<? extends AbstractResponseConfig> responseCfgCls,
            AbstractRequester requester,
            AbstractProcessor processor,
            AbstractResponser responser) {
        return MethodConfig.addMethodType(type, requestCfgCls, procCfgCls, responseCfgCls,
                WAFConfig.getAide(), requester, processor, responser);
    }
    
    public static final void setResultCodeName(String name) {
    	Const.setResultCodeName(name);
    }
    
    public static final void setReasonName(String name) {
    	Const.setReasonName(name);
    }
    
    /**
     * 添加自定义参数类型解析器，必须在AbstractInitializer子类的beforeInit中调用，
     * 因为随后将是配置解析，需要用到参数类型
     * @param name
     * @param parser
     */
    public static void addParameterParser(String name, Class<? extends ParameterInfo> parser) {
        ParameterInfo.addParameterParser(name, parser);
    }    
    
    /**
     * 在需要负载均衡的接口中，指定默认的balanceKey的值
     * @param balanceKey
     */
    public static final void setDefaultBalanceKey(String balanceKey) {
    	BalanceRDBProcessConfig.setDefaultBalanceKey(balanceKey);
    }
    
    /**
     * 给WAF错误码映射成定制系统的错误码及错误信息
     * @param code WAF的错误码
     * @param mapCode 定制系统希望转成的错误码
     * @param descr 错误码描述
     */
    public static final void setMapCode(int code, int mapCode, String descr) {
        RetCode.setMapCode(code, mapCode, descr);
    }
    
    /**
     * 设置UUID参数默认是否使用base64格式，
     * 系统中默认为true，使用base64方式
     * @param base64
     */
    public static final void setUUIDParameterInfoBase64(boolean base64) {
        UUIDParameterInfo.setDefaultBase64(base64);
    }
    
    /**
     * 设置http类处理的默认请求超时时间，传输的超时时间为此时间的2倍
     * @param timeout
     */
    public static final void setDefaultHttpProcessTimeout(int timeout) {
        HttpProcessConfig.setDefaultHttpTimeout(timeout);
    }
}
