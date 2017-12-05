package com.huawei.waf.core.config.method.response;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;

public class RDBResponseConfig extends JsonResponseConfig {
    private static final Logger LOG = LogUtil.getInstance();
    private static final String CONFIG_RESULTS = "results";
    
    //不可以为空，因为在读取时，有的地方未判断空
    private ResultSetConfig[] resultSetConfigs = new ResultSetConfig[0];

    @Override
    protected boolean parseExt(String version, Map<String, Object> json, MethodConfig mc) {
        if(!super.parseExt(version, json, mc)) {
            return false;
        }
        
        if(!json.containsKey(CONFIG_RESULTS)) {
            return true;
        }
        
        List<Object> arr = JsonUtil.getAsList(json, CONFIG_RESULTS);
        ResultSetConfig rsc;
        int len = arr.size();
        
        this.resultSetConfigs = new ResultSetConfig[len];
        
        for(int i = 0; i < len; i++) {
            rsc = ResultSetConfig.parseConfig(version, JsonUtil.getAsObject(arr, i), mc);
            if(rsc == null) {
                LOG.error("Fail to parse result-set config");
                return false;
            }
            this.resultSetConfigs[i] = rsc;
        }
        
        return true;
    }
    
    public ResultSetConfig[] getResultSetConfigs() {
        return resultSetConfigs;
    }
}
