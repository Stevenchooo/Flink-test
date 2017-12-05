package com.huawei.waf.core.config.method.parameter;

import java.util.List;
import com.huawei.waf.core.run.MethodContext;

public class StringListParameterInfo extends ArrayParameterInfo {

    @Override
    public Object getValue(MethodContext context) {
        Object o = context.getParameter(this.name);
        if(o == null) {
        	return null;
        }
        
        if((o instanceof List)) {
            return o;
        }
        
        List<String> val = splitString(o.toString(), this.getSeparator());
        context.setParameter(this.name, val);
        
        return val;
    }
}
