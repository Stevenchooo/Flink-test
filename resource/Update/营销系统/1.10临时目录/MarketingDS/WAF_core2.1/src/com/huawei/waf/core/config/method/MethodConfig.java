package com.huawei.waf.core.config.method;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.parameter.CompoundParameterInfo;
import com.huawei.waf.core.config.method.process.*;
import com.huawei.waf.core.config.method.request.*;
import com.huawei.waf.core.config.method.response.*;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.core.run.process.*;
import com.huawei.waf.core.run.request.*;
import com.huawei.waf.core.run.response.*;
import com.huawei.waf.facade.AbstractInitializer;
import com.huawei.waf.facade.config.*;
import com.huawei.waf.facade.run.*;
import com.huawei.waf.protocol.Const;

/**
 * @author l00152046
 * 一个method对应一个接口，包括请求、处理、响应三个部分
 */
public class MethodConfig {
	private static final Logger LOG = LogUtil.getInstance();
    private static final String DEFAULT_JSONP = "callback";

	private static final String CONFIG_NAME = "name";
    private static final String CONFIG_TYPE = "type";
    
    private static final String CONFIG_DATATYPE = "dataType";
    private static final String CONFIG_OPERATION = "operation";
    private static final String CONFIG_WRITELOG = "writeLog";
    
    private static final String CONFIG_TYPES = "types"; //局部对象定义
	private static final String CONFIG_REQUEST = "request";
	private static final String CONFIG_PROCESS = "process";
	private static final String CONFIG_RESPONSE = "response";
	private static final String CONFIG_AUTHTYPE = "authType";
    private static final String CONFIG_TIMEOUT = "timeout";
    private static final String CONFIG_JSONP = "jsonp";
    private static final String CONFIG_INTERRUPTABLE = "interruptable";
    
    private static final String CONFIG_RESULTCODENAME = "resultCodeName";
    private static final String CONFIG_REASONNAME = "reasonName";
    private static final String CONFIG_AIDECLASS = "aide"; //方法的小助手，主要工作是鉴权
    private static final String CONFIG_MAX_INTERVAL = "maxInterval"; //预估的最大执行时间，用于任务在执行时是否需要启动降级处理
	
    private static final String AUTHTYPE_NONE  = "none";
	private static final String AUTHTYPE_USER  = "user";
	private static final String AUTHTYPE_IP    = "ip";
    private static final String AUTHTYPE_ONCE  = "once";
	
	public static final int AUTH_NONE = 0x00;
	public static final int AUTH_USER = 0x01;
	public static final int AUTH_IP   = 0x02;
    public static final int AUTH_ONCE = 0x03;
    
	private static int DEFAULT_AUTH_TYPE = AUTH_NONE;
	
	//局部对象定义，在每个method的定义中
    private final Map<String, CompoundParameterInfo> types = new HashMap<String, CompoundParameterInfo>();
	
	private int authType = DEFAULT_AUTH_TYPE; //whether the method need authenticate or not
	private String name = null;
	private String type = null;
	private long timeout = 10 * 1000; //异步servlet超时时间
    private int maxInterval = Integer.MAX_VALUE;
	
    private String dataType = null; //method执行操作的数据类型
	private String oprType = null; //method执行的操作类型，默认就是接口名称
	
	private boolean writeLog = true;
    private boolean interruptable = false; //当会话超时后，是否可以被终端
	
	private String resultCodeName = Const.RESULT;
    private String reasonName = Const.REASON;
	
    private String jsonp = DEFAULT_JSONP;
    private String ver = null;
	
	private AbstractRequestConfig requestConfig = null;
	private AbstractProcessConfig processConfig = null;
	private AbstractResponseConfig responseConfig = null;
	
	private AbstractRequester requester;
    private AbstractProcessor processor;
    private AbstractResponser responser;
    private IMethodAide aide;
	
    /**
     * 记录所有接口配置相关的限制信息，比如RDB的请求、处理、响应的配置及运行时的处理类，
     * 在InitListener中可以在AbstractInitializer.beforeInit中可以加载自己的接口配置信息
     */
    private static final Map<String, ProcessInfo> PROCESSORS = new HashMap<String, ProcessInfo>();
    
    /**
     * 记录所有数据类型，及每个类型的操作类型，
     * 用于在对用户鉴权时，来判断用户可以调用哪些类型
     */
    private static final Map<String, List<String>> DATATYPES = new HashMap<String, List<String>>();

    public static final void init() {
        /**
         * 以json方式传递参数，只能用post，json串在消息体中，
         * 操作数据库的结果以json方式返回
         */
        addMethodType("RDB", JsonRequestConfig.class, RDBProcessConfig.class, RDBResponseConfig.class,
            new JsonRequester(), new RDBProcessor(), new JsonResponser());
        
        /**
         * 只支持简单的sql处理，不能支持存储过程，比如在sqlite时，就必须使用这种类型，
         * 因为sqlite的jdbc中不能支持CallableStatement，
         * 以json方式传递参数，只能用post，json串在消息体中，
         * 操作数据库的结果以json方式返回
         */
        addMethodType("SIMPLE-RDB", JsonRequestConfig.class, SimpleRDBProcessConfig.class, RDBResponseConfig.class,
            new JsonRequester(), new SimpleRDBProcessor(), new JsonResponser());
        
        /**
         * 完全自己实现的数据库操作，框架只负责获取数据库连接
         * 可以将数据库操作结果换存在ehcahe中，当cache失效时再次查询数据库，配置中增加一个ehcache="ehcache.xml中配置的cache名称"
         * 适合数据变换不频繁、数据量不大、请求参数组合不多的场景，可以极大的提升性能，降低数据库负载
         * 请求与响应与SELF-RDB相同，所有基于SelfRDBProcessor的处理类都可以缓存
         */
        addMethodType("SELF-RDB", JsonRequestConfig.class, SelfRDBProcessConfig.class, EmptyResponseConfig.class,
            new JsonRequester(), new SelfRDBProcessor(), new EmptyResponser());
        
        /**
         * 以rest方式传递参数，操作数据的结果以json方式返回
         */
        addMethodType("RESTRDB", RestRequestConfig.class, RDBProcessConfig.class, RDBResponseConfig.class,
            new RestRequester(), new RDBProcessor(), new JsonResponser());
        
        /**
         * 以json方式传递参数，操作数据的结果以json方式返回
         * 执行批量数据库操作，一个请求中有且只有一个为array类型的参数
         */
        addMethodType("BATCHRDB", BatchRequestConfig.class, RDBProcessConfig.class, RDBResponseConfig.class,
            new BatchRequester(), new BatchRDBProcessor(), new JsonResponser());
        
        /**
         * 以json方式传递参数，操作数据的结果以json方式返回
         * 在分库方案中执行汇总分页查询，在每个库中先执行count，
         * 然后根据页号及页长，判断需要在哪些库中查询
         * 不支持排序
         */
        addMethodType("PAGE-COLLECT-RDB", JsonRequestConfig.class, PageCollectRDBProcessConfig.class, RDBResponseConfig.class,
            new JsonRequester(), new PageCollectRDBProcessor(), new JsonResponser());
        
        /**
         * 以json方式传递参数，操作数据的结果以json方式返回
         * 在分库方案中执行汇总分页查询，根据当前位置，在一个库中查询，
         * 返回的行数不一定是页长，当一个库查询后，移到下一个库，直到最后一个库
         */
        addMethodType("OBO-COLLECT-RDB", JsonRequestConfig.class, OboCollectRDBProcessConfig.class, RDBResponseConfig.class,
            new JsonRequester(), new OboCollectRDBProcessor(), new JsonResponser());

        /**
         * 以json方式传递参数，操作数据的结果以json方式返回
         * 在分库方案中执行汇总、分页、排序查询
         */
        addMethodType("SORT-COLLECT-RDB", JsonRequestConfig.class, SortCollectRDBProcessConfig.class, RDBResponseConfig.class,
            new JsonRequester(), new SortCollectRDBProcessor(), new JsonResponser());

        /**
         * 以json方式传递参数，操作数据的结果以json方式返回
         * 每个分库都执行一遍，然后将所有的结果合并，如果是list，则会合并，非list，使用第一个完成的结果集中的内容
         */
        addMethodType("LOOP-COLLECT-RDB", JsonRequestConfig.class, RDBProcessConfig.class, RDBResponseConfig.class,
            new JsonRequester(), new LoopCollectRDBProcessor(), new JsonResponser());
        
        /**
         * 以json方式传递参数，操作数据的结果以json方式返回
         * 根据均衡信息（默认为账号）进行分库，将请求发到不同的数据库
         * 一次只会在一个库中执行
         */
        addMethodType("BALANCE-RDB", JsonRequestConfig.class, BalanceRDBProcessConfig.class, RDBResponseConfig.class,
            new JsonRequester(), new BalanceRDBProcessor(), new JsonResponser());

        /**
         * 以rest方式传递参数，操作数据的结果以map方式传递给freemarker模板
         */
        addMethodType("RDBPAGE", RestRequestConfig.class, RDBProcessConfig.class, HtmlResponseConfig.class,
            new RestRequester(), new RDBProcessor(), new HtmlResponser());
          
        /**
         * 以json方式传递参数，只能用post，json串在消息体中，
         * 处理器是一个空的实现，默认只是将请求参数放到响应中
         * 操作的结果以json方式返回
         */
        addMethodType("JAVA", RestRequestConfig.class, JavaProcessConfig.class, JsonResponseConfig.class,
            new JsonRequester(), new NullProcessor(), new JsonResponser());
        
        /**
         * 以rest方式传递参数，
         * 处理器是一个空的实现，默认只是将请求参数放到响应中，
         * 不会直接处理响应信息，需要processor处理响应信息
         */
        addMethodType("BIN", RestRequestConfig.class, JavaProcessConfig.class, EmptyResponseConfig.class,
            new RestRequester(), new DefaultJavaProcessor(), new EmptyResponser());
        
        /**
         * 以base64字符串方式传递文件，适合传递小文件（<5M），
         * 操作的结果以json方式返回，但是contentType为text/plain，
         * 否则ie浏览器中，返回消息会提示下载文件
         */
        addMethodType("SMALLUPLOAD", JsonRequestConfig.class, SmallFileProcessConfig.class, JsonResponseConfig.class,
            new JsonRequester(), new SmallUploadProcessor(), new TextJsonResponser());
        
        /**
         * 以base64字符串方式传递下载文件，适合下载小文件（<5M），
         * 操作的结果以json方式返回，文件内容base64后传输
         */
        addMethodType("SMALLDOWNLOAD", JsonRequestConfig.class, SmallFileProcessConfig.class, JsonResponseConfig.class,
            new JsonRequester(), new DownloadFileProcessor(), new JsonResponser());
        /**
         * 以multi-part方式传递参数，只能用post，
         * 处理器在processFile中将文件流传递给应用，
         * 操作的结果以json方式返回，但是contentType为text/plain，
         * 否则ie浏览器中，返回消息会提示下载文件
         */
        addMethodType("UPLOAD", RestRequestConfig.class, UploadProcessConfig.class, JsonResponseConfig.class,
            new UploadRequester(), new UploadProcessor(), new TextJsonResponser());
        
        /**
         * 先处理文件，然后处理数据库，
         * 文件内容可以写入数据库，也可以写入文件，
         * 文件字段的内容在processFile中实现，默认不做任何处理
         */
        addMethodType("UPLOADRDB", RestRequestConfig.class, UploadRDBProcessConfig.class, RDBResponseConfig.class,
                new UploadRequester(), new UploadRDBProcessor(), new TextJsonResponser());
        
        /**
         * 以rest方式传递参数，返回页面内容
         * 处理器是一个空的实现，默认只是将请求参数放到响应中
         */
        addMethodType("PAGE", RestRequestConfig.class, JavaProcessConfig.class, HtmlResponseConfig.class,
            new RestRequester(), new DefaultJavaProcessor(), new HtmlResponser());

        /**
         * 以rest方式传递参数，返回页面内容，
         * 内容根据接口名称缓存，每隔300秒重新读取，
         * 处理器是一个空的实现，不做任何处理
         */
        addMethodType("HTML", RestRequestConfig.class, JavaProcessConfig.class, StaticHtmlResponseConfig.class,
            new RestRequester(), new NullProcessor(), new StaticHtmlResponser());

        /**
         * 返回固定内容的接口，通常用于返回配置信息
         */
        addMethodType("FIXED", RestRequestConfig.class, JavaProcessConfig.class, FixedResponseConfig.class,
            new RestRequester(), new DefaultJavaProcessor(), new FixedResponser());
        
        /**
         * 透传发向网关的请求，将响应直接返回给调用方，但是retCode会变为resultCode
         */
        addMethodType("GW", JsonRequestConfig.class, GwCallProcessConfig.class, JsonResponseConfig.class,
            new JsonRequester(), new GwCallProcessor(), new JsonResponser());

        /**
         * 透传发向网关的请求，将响应直接返回给调用方，但是retCode会变为resultCode
         */
        addMethodType("SDS-GW", JsonRequestConfig.class, SDSGwCallProcessConfig.class, JsonResponseConfig.class,
            new JsonRequester(), new SDSGwCallProcessor(), new JsonResponser());

        /**
         * 透传发向第三方服务器的HTTP请求，将响应直接返回给调用方，但是retCode会变为resultCode
         * 可以支持GET、POST两种方法
         */
        addMethodType("HTTP", JsonRequestConfig.class, HttpProcessConfig.class, JsonResponseConfig.class,
            new JsonRequester(), new HttpProcessor(), new JsonResponser());
        
        /**
         * 透传发向第三方服务器的HTTP请求，将响应直接返回给调用方，不解析其中的任何内容
         * 可以支持GET、POST两种方法
         */
        addMethodType("TRANSMISSION-HTTP", RestRequestConfig.class, HttpProcessConfig.class, TransmissionResponseConfig.class,
            new RestRequester(), new HttpProcessor(), new TransmissionResponser());
    }
    
	@SuppressWarnings("unchecked")
    public static final MethodConfig parse(String version, Map<String, Object> json) {
        String name = JsonUtil.getAsStr(json, CONFIG_NAME, "").trim().toLowerCase();
		if(Utils.isStrEmpty(name)) {
			LOG.error("Invalid method config, because no config item {}", CONFIG_NAME);
			return null;
		}
		
        String type = JsonUtil.getAsStr(json, CONFIG_TYPE, "").trim().toUpperCase();
        if(Utils.isStrEmpty(type)) {
            LOG.error("Invalid method config, because no config item {}, name={}", CONFIG_TYPE, name);
            return null;
        }
        
		ProcessInfo pi = PROCESSORS.get(type);
		if(pi == null) {
            LOG.error("Invalid method {}, no processor matched", name);
		    return null;
		}
		
		MethodConfig mc = new MethodConfig();
		
        if(json.containsKey(CONFIG_TYPES)) {
            List<Object> tds = JsonUtil.getAsList(json, CONFIG_TYPES);
            int num = tds.size();
            for(int i = 0; i < num; i++) {
                CompoundParameterInfo td = CompoundParameterInfo.parse(version, JsonUtil.getAsObject(tds, i), mc);
                mc.types.put(td.getName(), td);
            }
        }
        
        mc.name = name;
        mc.type = type;
        mc.ver = version;
        /**
         * 操作类型默认是接口的名称，
         * 用来设置权角色限表t_roleRight，
         * 鉴权时，根据用户的角色来判断是否具有当前接口的操作权限
         */
        mc.dataType = JsonUtil.getAsStr(json, CONFIG_DATATYPE, null);
        mc.oprType = JsonUtil.getAsStr(json, CONFIG_OPERATION, name);
        mc.writeLog = JsonUtil.getAsBool(json, CONFIG_WRITELOG, mc.writeLog);
        mc.interruptable = JsonUtil.getAsBool(json, CONFIG_INTERRUPTABLE, mc.interruptable);
        
        mc.resultCodeName = JsonUtil.getAsStr(json, CONFIG_RESULTCODENAME, mc.resultCodeName);
        mc.reasonName = JsonUtil.getAsStr(json, CONFIG_REASONNAME, mc.reasonName);
        
		/**
		 * 在request/process/response的配置中都可以设置handler，
		 * 来改变默认的处理器
		 */
		mc.setRequester(pi.requester);
		mc.setProcessor(pi.processor);
		mc.setResponser(pi.responser);
		
		mc.timeout = JsonUtil.getAsLong(json, CONFIG_TIMEOUT, WAFConfig.getMethodTimeout());
        mc.maxInterval = JsonUtil.getAsInt(json, CONFIG_MAX_INTERVAL, mc.maxInterval);
		
        String authType = JsonUtil.getAsStr(json, CONFIG_AUTHTYPE, "").toLowerCase();
        if(!Utils.isStrEmpty(authType)) {
            String s = authType.trim().toLowerCase();
            if(s.equals(AUTHTYPE_USER)) {
                mc.authType = AUTH_USER;
            } else if(s.equals(AUTHTYPE_IP)) {
                mc.authType = AUTH_IP;
            } else if(s.equals(AUTHTYPE_ONCE)){
                mc.authType = AUTH_ONCE;
            } else if(s.equals(AUTHTYPE_NONE)){
                mc.authType = AUTH_NONE;
            } else {
                LOG.warn("Wrong {} {}, valid:{},{},{},{} or {}|{}",
                         CONFIG_AUTHTYPE, s, 
                         AUTHTYPE_USER, AUTHTYPE_IP, AUTHTYPE_NONE, 
                         AUTHTYPE_USER, AUTHTYPE_IP);
            }
        }
        
        try {
            mc.requestConfig = pi.requestCfgCls.newInstance();
            if(!mc.requestConfig.parse(version, JsonUtil.getAsObject(json, CONFIG_REQUEST), mc)) {
                LOG.error("Fail to parse method {}, invalid {}", name, CONFIG_REQUEST);
                return null;
            }
            
            //先处理resononse信息，因为process中可能会用到response信息
            //而response中不会用到process中信息
            mc.responseConfig = pi.responseCfgCls.newInstance();
            if(!mc.responseConfig.parseConfig(version, JsonUtil.getAsObject(json, CONFIG_RESPONSE), mc)) {
                LOG.error("Fail to parse method {}, invalid {}", name, CONFIG_RESPONSE);
                return null;
            }
            
    		mc.processConfig = pi.procCfgCls.newInstance();
    		if(!mc.processConfig.parseConfig(version, JsonUtil.getAsObject(json, CONFIG_PROCESS), mc)) {
                LOG.error("Fail to parse method {}, invalid {}", name, CONFIG_PROCESS);
    			return null;
    		}
        } catch (Exception e) {
            LOG.error("Fail to parse method {}", name, e);
            return null;
        } 
		
		if(!mc.parseExt(json)) {
			return null;
		}
        mc.jsonp = JsonUtil.getAsStr(json, CONFIG_JSONP, DEFAULT_JSONP);
        
        String clsName = JsonUtil.getAsStr(json, CONFIG_AIDECLASS, null);
        IMethodAide aide = pi.aide;
        if(!Utils.isStrEmpty(clsName)) {
            try {
                aide = ((Class<? extends IMethodAide>)Class.forName(clsName)).newInstance();
            } catch (Exception e) {
                LOG.error("Fail to load class {}", clsName, e);
                return null;
            }
        }
        mc.aide = aide;
        mc.writeLog = AbstractInitializer.getSecurityAide().isCanLog(mc); //最终由程序来决定是否打印日志
		 
		return mc;
	}
	
	protected boolean parseExt(Map<String, Object> json) {
		return true;
	}
	
	public AbstractRequestConfig getRequestConfig() {
		return requestConfig;
	}
	
	public AbstractProcessConfig getProcessConfig() {
		return processConfig;
	}
	
	public AbstractResponseConfig getResponseConfig() {
		return responseConfig;
	}
	
	/**
	 * 认证类型，user|ip|none
	 * @return
	 */
	public int getAuthType() {
		return authType;
	}
	
	public String getName() {
		return name;
	}
	
    /**
     * 类型，通过addMethodType添加，默认的有RDB/PAGE/JAVA/BATCHRDB等
     * @return
     */
    public String getType() {
        return type;
    }
    
    /**
     * 操作类型，比如增、删、改、查
     * @return
     */
    public String getOperationType() {
        return oprType;
    }
    
    /**
     * 操作的数据类型
     * @return
     */
    public String getDataType() {
        return dataType;
    }
    
    /**
     * 操作是否需要写日志，默认情况下不处理此配置
     * @return
     */
    public boolean isCanLog() {
        return writeLog;
    }
    
    public CompoundParameterInfo getType(String name) {
        CompoundParameterInfo td = types.get(name); //首先查看局部是否有对象定义
        if(td != null) {
            return td;
        }
        //如果没有，再找全局对象定义
        return Methods.getGlobalType(this.ver, name);
    } 
    
	/**
	 * 用于设置默认的认证类型，必须在加载配置前设置
	 * @param authType
	 */
	public static final void setDefaultAuthType(int authType) {
	    DEFAULT_AUTH_TYPE = authType;
	}

    public AbstractRequester getRequester() {
        return requester;
    }

    public void setRequester(AbstractRequester requester) {
        this.requester = requester;
    }

    public AbstractProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(AbstractProcessor processor) {
        this.processor = processor;
    }

    public AbstractResponser getResponser() {
        return responser;
    }

    public void setResponser(AbstractResponser responser) {
        this.responser = responser;
    }

    public long getTimeout() {
        return timeout;
    }
    
    public String getJsonP() {
        return jsonp;
    }
    /**
     * 内部使用，不对外开放
     * 用于记录不同接口请求、处理、响应的不同配置
     */
    private static class ProcessInfo {
        Class<? extends AbstractRequestConfig> requestCfgCls;
        Class<? extends AbstractProcessConfig> procCfgCls;
        Class<? extends AbstractResponseConfig> responseCfgCls;
        
        IMethodAide aide;
        AbstractProcessor processor;
        AbstractRequester requester;
        AbstractResponser responser;
        
        public ProcessInfo(Class<? extends AbstractRequestConfig> requestCfgCls,
                           Class<? extends AbstractProcessConfig> procCfgCls,
                           Class<? extends AbstractResponseConfig> responseCfgCls,
                           IMethodAide aide,
                           AbstractRequester requester,
                           AbstractProcessor processor,
                           AbstractResponser responser) {
            this.requestCfgCls = requestCfgCls;
            this.procCfgCls = procCfgCls;
            this.responseCfgCls = responseCfgCls;
            this.aide = aide;
            this.processor = processor;
            this.requester = requester;
            this.responser = responser;
        }
    }
    
    public static final boolean addMethodType(
            String type,
            Class<? extends AbstractRequestConfig> requestCfgCls,
            Class<? extends AbstractProcessConfig> procCfgCls,
            Class<? extends AbstractResponseConfig> responseCfgCls,
            AbstractRequester requester,
            AbstractProcessor processor,
            AbstractResponser responser) {
        return addMethodType(type, requestCfgCls, procCfgCls, responseCfgCls,
                WAFConfig.getAide(), requester, processor, responser);

    }
    
    public static final boolean addMethodType(
            String type,
            Class<? extends AbstractRequestConfig> requestCfgCls,
            Class<? extends AbstractProcessConfig> procCfgCls,
            Class<? extends AbstractResponseConfig> responseCfgCls,
            IMethodAide aide,
            AbstractRequester requester,
            AbstractProcessor processor,
            AbstractResponser responser) {
        LOG.info("Method Type: {}, \nconfig, request:{},process:{},response:{}\nprocess, requester:{}, processor:{}, responser:{}",
                type, requestCfgCls.getName(), procCfgCls.getName(), responseCfgCls.getName(),
                requester.getClass().getName(), processor.getClass().getName(), responser.getClass().getName());
        type = type.trim().toUpperCase();
        if(PROCESSORS.containsKey(type)) {
            LOG.error("Processor {} already exists", type);
            return false;
        }
        
        if((requestCfgCls.getModifiers() & Modifier.ABSTRACT) != 0) {
            LOG.error("Abstract requestCfgCls {}", requestCfgCls.getName());
            return false;
        }
        
        if((procCfgCls.getModifiers() & Modifier.ABSTRACT) != 0) {
            LOG.error("Abstract procCfgCls {}", procCfgCls.getName());
            return false;
        }
        
        if((responseCfgCls.getModifiers() & Modifier.ABSTRACT) != 0) {
            LOG.error("Abstract responseCfgCls {}", responseCfgCls.getName());
            return false;
        }
        
        if((requester.getClass().getModifiers() & Modifier.ABSTRACT) != 0) {
            LOG.error("Abstract requester {}", requester.getClass().getName());
            return false;
        }
        
        if((processor.getClass().getModifiers() & Modifier.ABSTRACT) != 0) {
            LOG.error("Abstract processor {}", processor.getClass().getName());
            return false;
        }
        
        if((responser.getClass().getModifiers() & Modifier.ABSTRACT) != 0) {
            LOG.error("Abstract responser {}", responser.getClass().getName());
            return false;
        }
        
        if(!processor.init()) {
            LOG.error("Fail to call {}.init()", processor.getClass().getName());
            return false;
        }
        PROCESSORS.put(type, new ProcessInfo(requestCfgCls, procCfgCls, responseCfgCls, aide, requester, processor, responser));
        
        return true;
    }
    
    public static final void addDataType(String dataType, String operation) {
        String[] ss = dataType.split("\\s*\\|\\s*");
        for(String s : ss) {
            s = s.trim();
            List<String> operations = DATATYPES.get(s);
            if(operations == null) {
                operations = new ArrayList<String>();
                DATATYPES.put(s, operations);
            }
            
            if(!operations.contains(operation)) {
                operations.add(operation);
            }
        }
    }
    
    public static final Map<String, List<String>> getDataTypes() {
        return DATATYPES;
    }
    
    /**
     * 每个接口可以使用不同的resultCode与reason名称
     * @return
     */
    public String getResultCodeName() {
        return resultCodeName;
    }
    
    public String getReasonName() {
        return reasonName;
    }
    
    /**
     * 预估的最大执行时间，单位毫秒，
     * 当请求进入线程池时，时间已经超过预估的最大时间时，将设置busy标志
     * @return
     */
    public int getMaxInterval() {
        return maxInterval;
    }
    
    /**
     * 默认的小助手为通行证方式认证，可以通过修改method的aide，实现不同的认证方式
     * 每个method的aide可以不同
     * @return
     */
    public IMethodAide getAide() {
        return aide;
    }
    
    /**
     * 接口的版本号
     * @return
     */
    public String getVersion() {
        return ver;
    }
    
    /**
     * 当会话超时后，线程是否可以被打断
     * @return
     */
    public boolean isInterruptable() {
        return interruptable;
    }
}
