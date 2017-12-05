package com.huawei.waf.facade.config;

import java.util.*;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.facade.AbstractInitializer;
import com.huawei.waf.facade.run.AbstractHandler;
import com.huawei.waf.facade.run.AbstractRequester;
import com.huawei.waf.facade.security.AbstractSecurityAide;

public abstract class AbstractRequestConfig {
	private static final Logger LOG = LogUtil.getInstance();
	
	private static final String CONFIG_PARAMETERS = "parameters";
    private static final String CONFIG_SIGN = "sign"; //接口的参数是否需要校验签名
    private static final String CONFIG_HTTPS = "https"; //只容许https方式访问
    
	private ParameterInfo[] parameters = new ParameterInfo[0];
    private Map<String, ParameterInfo> mapParameters = new HashMap<String, ParameterInfo>();
    private ParameterInfo[] responseParameters = null; //需要放入响应的请求参数
    private boolean sign = false;
    private boolean httpsOnly = false;
	
    abstract protected boolean parseExt(String version, Map<String, Object> json, MethodConfig mc);
    
	@SuppressWarnings("unchecked")
	public boolean parse(String version, Map<String, Object> json, MethodConfig mc) {
	    if(json == null) {
	        return true; //使用默认的配置
	    }
	    
	    AbstractRequester processor = (AbstractRequester)AbstractHandler.getHandler(json, mc.getRequester());
	    if(processor == null) {
	        LOG.error("Fail to read requester-processor of {}", mc.getName());
	        return false;
	    }
        mc.setRequester(processor);
	    
		if(json.containsKey(CONFIG_PARAMETERS)) {
			List<Object> arr = JsonUtil.getAsList(json, CONFIG_PARAMETERS);
			ParameterInfo para;
			
			int no = 0, index = 1;
			List<ParameterInfo> responseParamrs = new ArrayList<ParameterInfo>();
			this.parameters = new ParameterInfo[arr.size()];
			AbstractSecurityAide securityAide = AbstractInitializer.getSecurityAide();

			for(Object o : arr) {
				if(!(o instanceof Map<?, ?>)) {
					LOG.error("Invalid parameter config");
					return false;
				}
				
				if((para = ParameterInfo.parse(version, (Map<String, Object>)o, mc, index)) == null) {
					LOG.error("Fail to parse parameter information");
					return false;
				}
				
				if(para.getIndex() > 0) { //如果参数是被忽略的，数据库index不必递增
				    index++;
				}
				
				this.parameters[no++] = para;
				if(para.response()) {
				    responseParamrs.add(para);
				}
				
				this.mapParameters.put(para.getName(), para);
				para.setIsCanLog(securityAide.isCanLog(mc, this, para));
			}
			
	        if(responseParamrs.size() > 0) {
	            this.responseParameters = responseParamrs.toArray(new ParameterInfo[0]);
	        }
		}
        this.sign = JsonUtil.getAsBool(json, CONFIG_SIGN, this.sign);
        this.httpsOnly = JsonUtil.getAsBool(json, CONFIG_HTTPS, this.httpsOnly);
		
		return parseExt(version, json, mc);
	}
	
	/**
	 * 获得所有的参数列表，与配置的顺序一致
	 * @return
	 */
	public ParameterInfo[] getParameters() {
		return parameters;
	}
	
    /**
     * 根据参数名称获得参数相关的配置信息
     * @param name
     * @return
     */
    public ParameterInfo getParameter(String name) {
        return mapParameters.get(name);
    }
    
    /**
     * 放入响应信息的字段列表
     * @return
     */
    public ParameterInfo[] getResponseParameters() {
        return responseParameters;
    }
    
    /**
     * 接口是否需要签名，如果需要签名，context中可以获取到原始参数列表
     * @return
     */
    public boolean isSign() {
        return sign;
    }
    
    /**
     * 是否只容许https方式访问
     * @return
     */
    public boolean isHttpsOnly() {
        return httpsOnly;
    }
    
    //override it when need it
    public void destroy() {
    }
}
