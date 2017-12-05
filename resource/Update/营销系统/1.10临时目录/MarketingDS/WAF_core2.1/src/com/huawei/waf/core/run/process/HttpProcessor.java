package com.huawei.waf.core.run.process;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;

import com.huawei.util.HttpUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.HttpProcessConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.run.AbstractProcessor;
import com.huawei.waf.protocol.RetCode;

public class HttpProcessor extends AbstractProcessor {
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final int EXTERNAL_ERROR = 100000;

	@Override
	public int process(MethodContext context) {
		MethodConfig methodConfig = context.getMethodConfig();
		HttpProcessConfig hpc = (HttpProcessConfig)methodConfig.getProcessConfig();
	        
		try {
			HttpResponse response;
			String url = hpc.getUrl();
			
			Map<String, Object> params = context.getParameters();
			switch(hpc.getMethod()) {
			case GET:
				response = HttpUtil.get(url, params, hpc.getHttpClient(), null, hpc.getRequestConfig());
				break;
			default:
				response = HttpUtil.post(url, params, hpc.getHttpClient(), null, hpc.getRequestConfig(), null);
				break;
			}
			
			if(response == null) {
			    hpc.buildClient();
			    return RetCode.INTERNAL_ERROR;
			}
			
			String content = HttpUtil.getString(response);
			
			if(Utils.isStrEmpty(content)) {
				return context.setResult(RetCode.INTERNAL_ERROR, "No response content");
			}
			
			switch(hpc.getResponse()) {
			case JSON:
				Map<String, Object> resp = JsonUtil.jsonToMap(content);
				return handleResult(context, hpc, resp);
			default:
				context.setResult("content", content);
				return RetCode.OK;
			}
		} catch (Exception e) {
			LOG.error("Fail to get response when call gaf.{}", hpc.getUrl(), e);
			return RetCode.INTERNAL_ERROR;
		}
	}

	@Override
	public int afterAll(MethodContext context) {
		return RetCode.OK;
	}

	@Override
	public boolean init() {
		return true;
	}

	@Override
	public void destroy() {
	}
	
	/**
	 * 处理网关的返回信息，可以重载，改变处理方式
	 * @param context
	 * @param gpc
	 * @param resp
	 * @return
	 */
	protected int handleResult(MethodContext context, HttpProcessConfig hpc, Map<String, Object> resp) {
		if(resp == null) {
			LOG.error("Fail to get response when call gaf.{}", hpc.getUrl());
			return context.setResult(RetCode.INTERNAL_ERROR, "Fail to get response");
		}
		
		int retCode = JsonUtil.getAsInt(resp, hpc.getRetCodeName(), RetCode.OK);
		
		context.addResults(resp); //原样返回
		
		if(retCode != RetCode.OK) {
			retCode += EXTERNAL_ERROR;
			return context.setResult(retCode);
		}
		
		return retCode;
	}
}
