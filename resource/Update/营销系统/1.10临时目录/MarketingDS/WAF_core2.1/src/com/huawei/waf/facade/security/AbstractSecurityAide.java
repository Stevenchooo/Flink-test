package com.huawei.waf.facade.security;

import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.facade.config.AbstractRequestConfig;
import com.huawei.waf.facade.config.AbstractResponseConfig;

public abstract class AbstractSecurityAide {
	public boolean isCanPrintSql() {
		return true;
	}
	
	public boolean isCanLog(MethodConfig mc, AbstractRequestConfig cfg, ParameterInfo pi){
		return pi.isCanLog();
	}
	
	public boolean isCanLog(MethodConfig mc){
		return mc.isCanLog();
	}
	
	public boolean isCanLog(MethodConfig mc, AbstractResponseConfig cfg, ParameterInfo pi){
		return pi.isCanLog();
	}
}
