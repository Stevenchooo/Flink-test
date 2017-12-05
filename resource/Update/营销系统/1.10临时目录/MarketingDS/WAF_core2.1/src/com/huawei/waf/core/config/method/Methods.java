
package com.huawei.waf.core.config.method;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.parameter.CompoundParameterInfo;

public class Methods {
    private static final String CONFIG_FILE_EXT = ".cfg";
    private static final String CONFIG_METHODS  = "methods";
    private static final String CONFIG_DEFAULTSETTINGS  = "defaultSettings";
    private static final String CONFIG_TYPEDEFINE_FILE_NAME = "typedef.cfg";
	
	private static final Logger LOG = LogUtil.getInstance();
	
	private static final Map<String, MethodConfig> METHODS = new HashMap<String, MethodConfig>();
    private static final Map<String, CompoundParameterInfo> types = new HashMap<String, CompoundParameterInfo>();

	public static MethodConfig get(String name) {
		return METHODS.get(name.toLowerCase());
	}
	
	public static final boolean init(File path) {
		LOG.debug("Methods init from {}", path.toString());
		
		if(!path.exists()) {
			LOG.error("{} not exists where init in {}", path, Methods.class.getName());
			return false;
		}
		
		String version;
		File[] vers = path.listFiles();
		String name;
		File typeDef;
		
		for(File ver : vers) {
			if(!ver.isDirectory()) {
				continue;
			}
			name = ver.getName();
			if(!name.matches("^v\\d+$")) {
	            LOG.info("{} is not a config path, vxx", name);
			    continue;
			}
			version = name.substring(1);
			LOG.info("Parse version {}", version);
			
			typeDef = new File(ver.getAbsolutePath() + File.pathSeparator + CONFIG_TYPEDEFINE_FILE_NAME);
			if(typeDef.exists()) {
			    /**
			     * 首先读取当前版本中的全局对象定义
			     * 因为在method解析中会用到
			     */
                if(!loadGlobalType(version, typeDef)) {
                    LOG.info("Fail to load type define from {}", typeDef.getAbsoluteFile());
                    return false;
                }
			}

			if(!readOnePath(version, ver)) {
				LOG.error("Fail to parse version:{}", version);
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean readOnePath(String version, File path) {
		File[] cfgs = path.listFiles();
		int len;
		String name;
		
		for(File cfg : cfgs) {
			if(cfg.isDirectory()) {
			    if(!readOnePath(version, cfg)) {
			        return false;
			    }
				continue;
			}
			name = cfg.getName(); 
			len = name.length() - CONFIG_FILE_EXT.length();
			if(name.indexOf(CONFIG_FILE_EXT) != len || name.equals(CONFIG_TYPEDEFINE_FILE_NAME)) {
				LOG.info("{} is not a method config file", name);
				continue;
			}
			
			LOG.info("Read one config file:{}", name);
			if(!readOneConfig(version, cfg)) {
				return false;
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private static boolean readOneConfig(String version, File cfgFile) {
		LOG.debug("Read one method config file {}", cfgFile.getAbsolutePath());
		
		Map<String, Object> moduleCfg = JsonUtil.jsonFileToMap(cfgFile);
		
		if(moduleCfg == null) {
			LOG.error("Fail to read config file:{}", cfgFile.getAbsolutePath());
			return false;
		}
		
		if(!moduleCfg.containsKey(CONFIG_METHODS)) {
			LOG.error("There are no {} in config", CONFIG_METHODS);
			return false;
		}
		
		Map<String, Object> defaultSettings = JsonUtil.getAsObject(moduleCfg, CONFIG_DEFAULTSETTINGS);
		Map<String, Object> settings;
		List<Object> oo = JsonUtil.getAsList(moduleCfg, CONFIG_METHODS);
		MethodConfig mc;
		String method, name;
		String[] nameList;
		
		for(Object o : oo) {
			if(!(o instanceof Map<?,?>)) {
				LOG.error("Wrong method config in {}", cfgFile.getAbsolutePath());
				return false;
			}

			/**
			 * 支持默认配置，可以减少配置的量
			 */
			if(defaultSettings == null) {
			    settings = (Map<String, Object>)o;
			} else {
			    settings = new HashMap<String, Object>(defaultSettings);
			    settings.putAll((Map<String, Object>)o);
			}
			
			if((mc = MethodConfig.parse(version, settings)) == null) {
				return false;
			}
			name = mc.getName().trim();
			nameList = name.split("\\s*\\|\\s*");
			for(String n : nameList) {
    			method = (version + n).toLowerCase();
    			if(METHODS.containsKey(method)) {
                    LOG.error("Method {} already exists, in {}", method, cfgFile.getAbsoluteFile());
    			    return false;
    			}
    			METHODS.put(method, mc);
                LOG.info("Add one {} method {}", mc.getType(), method);
			}
	        LOG.debug("\n--requester: {},  config: {}\n--processor:{},  config: {}\n--responser: {},  config: {}\n",
	                  mc.getRequester().getClass().getName(),
	                  mc.getRequestConfig().getClass().getName(),
	                  mc.getProcessor().getClass().getName(),
                      mc.getProcessConfig().getClass().getName(),
	                  mc.getResponser().getClass().getName(),
                      mc.getResponseConfig().getClass().getName()
	                 );
		}
		return true;
	}
	
	public static final void destroy() {
        LOG.info("Methods destroy");

        MethodConfig mc;
		
		for(Map.Entry<String, MethodConfig> o : METHODS.entrySet()) {
		    mc = o.getValue();
		    LOG.info("Destroy {}", mc.getName());
            mc.getRequestConfig().destroy();
            mc.getProcessConfig().destroy();
            mc.getResponseConfig().destroy();
		    mc.getRequester().destroy();
		    mc.getProcessor().destroy();
		    mc.getResponser().destroy();
		}
	}

    @SuppressWarnings("unchecked")
    public static final boolean loadGlobalType(String version, File cfg) {
        LOG.info("Read typedefine from {}", cfg.getAbsoluteFile());
        
        List<Object> tds = JsonUtil.jsonFileToList(cfg);
        for(Object o : tds) {
            if(!(o instanceof Map<?,?>)) {
                LOG.info("Fail parse one typedefine of version:{}", version);
                return false;
            }
            CompoundParameterInfo td = CompoundParameterInfo.parse(version, (Map<String, Object>)o, null);
            
            types.put(version + "/" + td.getName(), td);
        }
        return true;
    }
    
    public static final CompoundParameterInfo getGlobalType(String version, String name) {
        return types.get(version + "/" + name);
    }
	
    /**
     * 增加接口配置信息
     * @param ver
     * @param mc
     */
    public static final void addMethod(String ver, MethodConfig mc) {
        METHODS.put((ver + mc.getName().trim()).toLowerCase(), mc);
    }
}
