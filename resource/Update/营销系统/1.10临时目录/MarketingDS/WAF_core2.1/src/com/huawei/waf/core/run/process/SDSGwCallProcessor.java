package com.huawei.waf.core.run.process;

import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.SDSGwCallProcessConfig;
import com.huawei.waf.core.run.MethodContext;

public class SDSGwCallProcessor extends GwCallProcessor {
    private static final String CONFIG_TABLE_NAME = "tableName";
    
	@Override
	public int process(MethodContext context) {
		MethodConfig methodConfig = context.getMethodConfig();
		SDSGwCallProcessConfig gpc = (SDSGwCallProcessConfig)methodConfig.getProcessConfig();

		context.getParameters().put(CONFIG_TABLE_NAME, gpc.getTableName());
		return super.process(context);
	}
}
