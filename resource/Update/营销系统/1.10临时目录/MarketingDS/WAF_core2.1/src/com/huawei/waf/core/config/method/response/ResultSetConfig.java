package com.huawei.waf.core.config.method.response;

import java.util.*;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.facade.AbstractInitializer;
import com.huawei.waf.facade.security.AbstractSecurityAide;

public class ResultSetConfig {
	private static final Logger LOG = LogUtil.getInstance();
	
	private static final String CONFIG_SEGMENTS = "segments";
	private static final String CONFIG_NAME = "name";
	private static final String CONFIG_MULTI = "multi";
    
	/**
     * 如果设置merge，则将单行的结果集合并到第一次对象中
     * 只能用在multi为false的情况下起作用
     */
    private static final String CONFIG_MERGE = "merge";
    private static final String CONFIG_OPTIONAL = "optional";
    
    private static final String CONFIG_LOG = "log";
    
    /**
     * 如果设置为array，结果集中将没有列名称，比如：
     * {"a":1, "b":2} ==> [1, 2]
     * 只能用在multi为true的情况下
     */
    private static final String CONFIG_ARRAY = "array";
	
	private boolean multi = false;
    private boolean merge = false;
    private boolean array = false;
    private boolean optional = false;
    private boolean log = true;
    
	private String name = "";
	private Map<String, ParameterInfo> segments = new HashMap<String, ParameterInfo>();
	private ParameterInfo[] segmentList = null;
	
	@SuppressWarnings("unchecked")
	public static ResultSetConfig parseConfig(String version, Map<String, Object> json, MethodConfig mc) {
		ResultSetConfig rc = new ResultSetConfig();
		
		rc.log = JsonUtil.getAsBool(json, CONFIG_LOG, rc.log);
		rc.multi = JsonUtil.getAsBool(json, CONFIG_MULTI, rc.multi);
		if(rc.multi) {
	        rc.array = JsonUtil.getAsBool(json, CONFIG_ARRAY, rc.array);
		} else {
            rc.merge = JsonUtil.getAsBool(json, CONFIG_MERGE, rc.merge);
		}
		
		rc.name = JsonUtil.getAsStr(json, CONFIG_NAME);
		if(Utils.isStrEmpty(rc.name)) {
    		if(rc.multi){
    			rc.name = "list";
    		} else if(!rc.merge) {
    			rc.name = "result";
    		}
    	}
		rc.optional = JsonUtil.getAsBool(json, CONFIG_OPTIONAL, rc.optional);
		
		if(json.containsKey(CONFIG_SEGMENTS)) {
			List<Object> arr = JsonUtil.getAsList(json, CONFIG_SEGMENTS);
			ParameterInfo para;
			AbstractSecurityAide securityAide = AbstractInitializer.getSecurityAide();
			
			int index = 1;
			
			for(Object o : arr) {
				if(!(o instanceof Map<?, ?>)) {
					LOG.error("Invalid {} config", CONFIG_SEGMENTS);
					return null;
				}
				
				if((para = ParameterInfo.parse(version, (Map<String, Object>)o, mc, index)) == null) {
					LOG.error("Fail to parse parameter information");
					return null;
				}
				index++;
				rc.segments.put(para.getDataSeg().toUpperCase(), para);
				para.setIsCanLog(securityAide.isCanLog(mc, mc.getResponseConfig(), para));
			}
		}
        rc.segmentList = rc.segments.values().toArray(new ParameterInfo[rc.segments.size()]);
        
		return rc;
	}
	
	public boolean isMulti() {
		return multi;
	}
	
    public boolean isArray() {
        return array;
    }
    
    public boolean isMerge() {
        return merge;
    }
    
	public String getName() {
		return name;
	}
	
	public Map<String, ParameterInfo> getSegments() {
		return segments;
	}
	
    public ParameterInfo[] getSegmentList() {
        return segmentList;
    }
	
    /**
     * 当需要从参数列表中获取字段时，调用次函数
     * @param name
     * @return
     */
    public ParameterInfo getParameter(String name) {
        ParameterInfo pi;
        for(Map.Entry<String, ParameterInfo> one : segments.entrySet()) {
            pi = one.getValue();
            if(pi.getName().equals(name)) {
                return pi;
            }
        }
        return null;
    }
    
    /**
     * 结果集是否是可选的
     * @return
     */
    public boolean isOptional() {
        return optional;
    }
    
    public boolean isCanLog() {
        return log;
    }
}
