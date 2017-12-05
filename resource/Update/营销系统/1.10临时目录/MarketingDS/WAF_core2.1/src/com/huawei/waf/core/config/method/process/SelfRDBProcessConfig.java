package com.huawei.waf.core.config.method.process;

import java.util.Map;

import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.config.AbstractProcessConfig;

/**
 * 只需要数据源，不需要框架处理数据库操作，
 * 需要处理类中重载RDBProcessor.process(MethodContext context, DBUtil.DBConnection dbConn)
 * 且不能调用super.process，阻止框架处理数据库
 * @author l00152046
 *
 */
public class SelfRDBProcessConfig extends AbstractProcessConfig {
	private static final String PARAMETER_TRANSACTION = "transaction";
    private static final String PARAMETER_DATASOURCE = "dataSource";
	
	private boolean transaction = false; //是否使用事务
	private String dataSource = DBUtil.getDefaultSource(); //数据库资源
    
	
	@Override
    protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc) {
        transaction = JsonUtil.getAsBool(json, PARAMETER_TRANSACTION, transaction);
        dataSource = JsonUtil.getAsStr(json, PARAMETER_DATASOURCE, dataSource);

    	return true;
	}
	
	public boolean useTransaction() {
		return transaction;
	}
	
    public String getDataSource(MethodContext context) {
        return dataSource;
    }
    
    public String getDataSource() {
        return dataSource;
    }
}
