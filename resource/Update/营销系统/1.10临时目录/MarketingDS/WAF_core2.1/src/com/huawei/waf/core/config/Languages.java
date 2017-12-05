package com.huawei.waf.core.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;

public class Languages {
	private static final String CONFIG_FILE_EXT  = ".cfg";
	
	private static final Logger LOG = LogUtil.getInstance();
	
	private static final Map<String, Map<String, String>> languages = new HashMap<String, Map<String, String>>();
	
	public static final Map<String, String> get(String lang) {
		return languages.get(lang);
	}
	
	public static final boolean init(File path) {
		LOG.info("Multi-Language init from {}", path.getAbsoluteFile());
		
		if(!path.exists()) {
			LOG.info("{} not exists when init in {}", path, Languages.class.getName());
			return true;
		}
		
		String name;
		int len;
		Map<String, Object> o;
		
		for(File ff : path.listFiles()) {
			if(ff.isDirectory()) {
				continue;
			}
			name = ff.getName();
			len = name.length() - CONFIG_FILE_EXT.length();
			if(name.indexOf(CONFIG_FILE_EXT) != len) {
				LOG.info("{} is not a language config file", name);
				continue;
			}
			name = name.substring(0, len);
			LOG.info("Parse language, {}", name);
			o = JsonUtil.jsonFileToMap(ff);
			if(o == null) {
				LOG.error("Fail to parse language {}", ff.getAbsolutePath());
				return false;
			}
			
			Map<String, String> lang = new HashMap<String, String>();
			for(Map.Entry<String, Object> one : o.entrySet()) {
				lang.put(one.getKey(), one.getValue().toString());
			}
			languages.put(name, lang);
		}
		return true;
	}
}
