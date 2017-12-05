package com.huawei.waf.core.run.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ArrayParameterInfo;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.config.method.request.BatchRequestConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.config.AbstractRequestConfig;
import com.huawei.waf.protocol.RetCode;

@SuppressWarnings("unchecked")
public class BatchRequester extends JsonRequester {
    public static final String BATCH_ARRAY_NAME = "list";
    
    private static final Logger LOG = LogUtil.getInstance();
    
    /* 
     * 参数的结构从
     * {...,"arrPara":[{...},{...},...]...}
     * 调整为
     * {"list":[{...},{...},...]}
     * 
     * @see com.huawei.waf.core.run.request.JsonRequester#buildParas(com.huawei.waf.core.run.MethodContext, java.util.Map)
     */
    @Override
    public int buildParas(MethodContext context, Map<String, Object> reqParams) {
        int retCode = super.buildParas(context, reqParams); 
        if(retCode != RetCode.OK) {
            return retCode;
        }
        
        List<Map<String, Object>> lines = new ArrayList<Map<String, Object>>();
        Map<String, Object> line;
        MethodConfig mc = context.getMethodConfig();
        BatchRequestConfig reqCfg = (BatchRequestConfig)mc.getRequestConfig();
        ArrayParameterInfo arrParaCfg = reqCfg.getArrayInfo();
        String arrName = arrParaCfg.getName();

        String name;
        Map<String,Object> rParams = new HashMap<String, Object>();
        //配置中的参数才放入rParams中，过滤掉公共参数，比如通行证
        for(ParameterInfo pi : reqCfg.getNorParameters()) {
            name = pi.getName();
            rParams.put(name, reqParams.get(name));
        }
        
        List<Object> arrParams = JsonUtil.getAsList(reqParams, arrName);
        if(arrParams == null || arrParams.size() <= 0) {
            LOG.debug("Parameter {} is null or size is 0", arrName);
            return context.setResult(RetCode.WRONG_PARAMETER, "Invalid parameter " + arrName); 
        }
        ParameterInfo objPara = arrParaCfg.getObject();
        boolean objIsPrimitive = objPara.isPrimitive();
        String objName = objPara.getName();
        
        int no = 0;
        for(Object one : arrParams) {
            if(one instanceof Map<?,?>) {
                line = (Map<String, Object>)one;
                line.putAll(rParams);
            } else if(objIsPrimitive){
                line = new HashMap<String, Object>(rParams);
                line.put(objName, one);
            } else {
                LOG.error("Wrong parameter at line {}", no);
                return context.setResult(RetCode.WRONG_PARAMETER, "Wrong parameter at line " + no);
            }
            lines.add(line);
            no++;
        }
        
        //不能清除所有参数，通行证等在第一层
        //reqParams.clear();
        reqParams.remove(arrName); //删除array参数，已直接扩展到每行中
        reqParams.put(BATCH_ARRAY_NAME, lines);
        
        return RetCode.OK;
    }
    
    @Override
    public int checkRequest(MethodContext context) {
        MethodConfig mc = context.getMethodConfig();
        AbstractRequestConfig rc = mc.getRequestConfig();
        String name;
        List<Object> lines = JsonUtil.getAsList(context.getParameters(), BATCH_ARRAY_NAME);
        int retCode = RetCode.OK;
        
        int sn = 1;
        Map<String, Object> line;
        /**
         * 变成扁平结构后，逐行检查，公共部分会被重复检查，
         * 逐行是有必要的，因为处理函数可能会做参数的关联性检查，
         * 如果不做重复检查，数组与公共参数的关联性将无法检查
         */
        ParameterInfo[] params = rc.getParameters();
        for(Object o : lines) { 
            line = (Map<String, Object>)o;
            for(ParameterInfo parameterInfo : params) {
                name = parameterInfo.getName();
                if(!parameterInfo.check(context, line.get(name))) {
                    retCode = parameterInfo.getWrongCode(sn);
                    //约定返回码-RetCode.WRONG_PARAMETER为第N个参数错误，也可以通过配置制定返回码
                    context.setResult(retCode, "Parameter(" + name + ") error," + parameterInfo.getClaim());
                    break;
                }
                
                sn++;
            }
        }
        
        return retCode;
    }
}
