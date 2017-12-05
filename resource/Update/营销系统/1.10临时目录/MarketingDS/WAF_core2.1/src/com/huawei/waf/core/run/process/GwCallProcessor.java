package com.huawei.waf.core.run.process;

import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.GwClient;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.GwCallProcessConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.run.AbstractProcessor;
import com.huawei.waf.protocol.RetCode;

public class GwCallProcessor extends AbstractProcessor {
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final int EXTERNAL_ERROR = 100000;

	@Override
	public int process(MethodContext context) {
		MethodConfig methodConfig = context.getMethodConfig();
		GwCallProcessConfig gpc = (GwCallProcessConfig)methodConfig.getProcessConfig();

		try {
			GwClient client = GwClient.getInstance();
			Map<String, Object> resp = client.call(gpc.getApiName(), context.getParameters());
			
			return handleResult(context, gpc, resp);
		} catch (Exception e) {
			LOG.error("Fail to get response when call gaf.{}", gpc.getApiName(), e);
			return RetCode.INTERNAL_ERROR;
		}
	}

	@Override
	public int afterAll(MethodContext context) {
		return RetCode.OK;
	}

	@Override
	public boolean init() {
//		try {
//			return GAFClient.getInstance() != null;
//		} catch (Exception e) {
//			LOG.error("Fail to get gaf client");
//		}
//		
//		return false;
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
	protected int handleResult(MethodContext context, GwCallProcessConfig gpc, Map<String, Object> resp) {
		if(resp == null) {
			LOG.error("Fail to get response when call gaf.{}", gpc.getApiName());
			return context.setResult(RetCode.INTERNAL_ERROR, "Fail to get response");
		}
		
		int retCode = JsonUtil.getAsInt(resp, gpc.getRetCodeName(), RetCode.OK);
		if(retCode != RetCode.OK) {
			retCode += EXTERNAL_ERROR;
			return context.setResult(retCode);
		}
		
		context.addResults(resp); //原样返回
		
		return RetCode.OK;
	}
}
