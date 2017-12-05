package com.huawei.waf.core.config.method.parameter;

import java.util.Map;

import com.huawei.util.JsonUtil;
import com.huawei.waf.core.config.method.MethodConfig;

/**
 * 对象类型，object配置项必须为一个CompoundParameterInfo，
 * 否则ObjectParameterInfo将没有存在的意义
 * CompoundParameterInfo必须在method的types中定义，
 * 或者在typedef.cfg中定义全局的CompoundParameterInfo
 * @author l00152046
 *
 */
public class FileParameterInfo extends StringParameterInfo {
	private static final String PROPERTY_FILENAME = "fileName";
	
	private String fileName;
	
    @Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
    	if(!super.parseExt(version, para, mc)) {
    		return false;
    	}
		this.fileName = JsonUtil.getAsStr(para, PROPERTY_FILENAME, "");
		
		return true;
    }
    
    public String getFileName() {
    	return fileName;
    }
    
	@Override
    public String getClaim() {
    	return "File parameter, used only in file upload";
    }
}
