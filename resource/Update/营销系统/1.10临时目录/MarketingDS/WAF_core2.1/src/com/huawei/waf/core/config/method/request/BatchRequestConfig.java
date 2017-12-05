package com.huawei.waf.core.config.method.request;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ArrayParameterInfo;
import com.huawei.waf.core.config.method.parameter.CompoundParameterInfo;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;

public class BatchRequestConfig extends JsonRequestConfig {
    private static final Logger LOG = LogUtil.getInstance();
    
    private ParameterInfo[] parameters = null;
    private ParameterInfo[] norParameters = null;
    private ArrayParameterInfo arrParaInfo = null;
    
    @Override
    protected boolean parseExt(String version, Map<String, Object> json, MethodConfig mc) {
        if(!super.parseExt(version, json, mc)) {
            return false;
        }
        
        ParameterInfo[] parameters = super.getParameters();
        if(parameters.length <= 0) {
            LOG.error("A batch rdb request must have paramters");
            return false;
        }
        
        int num = 0;
        int index = 1;
        ParameterInfo arrObject;
        ArrayList<ParameterInfo> expandedParams = new ArrayList<ParameterInfo>();
        ArrayList<ParameterInfo> norParams = new ArrayList<ParameterInfo>();
        for(ParameterInfo pi : parameters) {
            if(pi instanceof ArrayParameterInfo) {
                num++;
                arrParaInfo = (ArrayParameterInfo)pi;
                arrObject = arrParaInfo.getObject();
                if(arrObject.isPrimitive()) {
                    if(arrObject.getIndex() > 0) {
                        arrObject.setIndex(index++);
                    }
                    expandedParams.add(arrObject);
                } else if(arrObject instanceof CompoundParameterInfo) {
                    CompoundParameterInfo cpi = (CompoundParameterInfo)arrObject;
                    ParameterInfo[] paras = cpi.getSegments();
                    for(ParameterInfo p : paras) {
                        if(p.getIndex() > 0) {
                            p.setIndex(index++);
                        }
                        expandedParams.add(p);
                    }
                } else {
                    LOG.error("Wrong array config {}", pi.getName());
                    return false;
                }
            } else {
                if(pi.getIndex() > 0) {
                    pi.setIndex(index); //改变数据库脚本中的顺序
                }
                expandedParams.add(pi);
                norParams.add(pi);
            }
        }
        
        if(num != 1) {
            LOG.error("A batch rdb request should have only one {} paramters", ParameterInfo.TYPE_ARRAY);
            return false;
        }
        //为了提高效率，将list变为[]
        this.parameters = expandedParams.toArray(new ParameterInfo[0]);
        this.norParameters = norParams.toArray(new ParameterInfo[0]);
        
        return true;
    }
    
    @Override
    public ParameterInfo[] getParameters() {
        return parameters;
    }
    
    /**
     * 不带array参数的参数列表，便于requester中获取参数
     * @return
     */
    public ParameterInfo[] getNorParameters() {
        return norParameters;
    }
    
    /**
     * 获取原始的参数配置，因为批量请求的参数被线性化了
     * 直接调用getParameters获取的配置与原始配置不一致
     * @return
     */
    public ParameterInfo[] getOriginalParameters() {
        return super.getParameters();
    }
    
    public ParameterInfo getOriginalParameter(String name) {
        return super.getParameter(name);
    }
    
    @Override
    public ParameterInfo getParameter(String name) {
        for(ParameterInfo pi : parameters) {
            if(name.equals(pi.getName())) {
                return pi;
            }
        }
        return null;
    }
    
    public ArrayParameterInfo getArrayInfo() {
        return arrParaInfo;
    }
}
