package com.huawei.waf.core.config.method.process;

import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;

/**
 * <配置文件中关于分库数据库配置项的对象类>
 * 
 * @author  l00152046
 */
public class BalanceRDBProcessConfig extends RDBProcessConfig {
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final String PARAMETER_BALANCEKEY = "balanceKey";
    
    private static String DEFAULT_BALANCE_KEY = "userId";
    
    private String balanceKey = null; //均衡的字段，默认为userId
    
    /**
     * <解析配置文件中关于数据库扩展配置项>
     * {@inheritDoc}
     */
    @Override
    protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc) {
        if(!super.parseExt(ver, json, mc)) {
        	return false;
        }
    	
        balanceKey = JsonUtil.getAsStr(json, PARAMETER_BALANCEKEY, DEFAULT_BALANCE_KEY);
        if(Utils.isStrEmpty(balanceKey)) {
        	LOG.debug("There are no {} config item in {}, use {} as default", PARAMETER_BALANCEKEY, mc.getName(), DEFAULT_BALANCE_KEY);
        }
        
        return true;
    }

    /**
     * 负载均衡的字段名称，默认为"userId"，从context中获得
     * @return
     */
    public String getBalanceKey() {
        return balanceKey;
    }
    
    @Override
    public String getDataSource(MethodContext context) {
    	if(balanceKey == null) {
    		return DBUtil.getBalancer().getNode(context.getAccount());
    	}
    	String k = Utils.parseString(context.getParameter(balanceKey), "");
    	if(Utils.isStrEmpty(k)) {
    		return null;
    	}
    	return DBUtil.getBalancer().getNode(k);
    }
    
    public static final void setDefaultBalanceKey(String balanceKey) {
    	DEFAULT_BALANCE_KEY = balanceKey;
    }
}
