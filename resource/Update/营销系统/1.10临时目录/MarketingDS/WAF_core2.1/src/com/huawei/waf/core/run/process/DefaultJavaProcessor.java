package com.huawei.waf.core.run.process;

import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

public class DefaultJavaProcessor extends NullProcessor {
    /* 将所有输入参数放到输出参数中
     * @see com.huawei.core.facade.IProcessor#process(com.huawei.core.bean.MethodContext)
     */
    @Override
    public int process(MethodContext context) {
        context.addResults(context.getParameters());
        return RetCode.OK;
    }
}
