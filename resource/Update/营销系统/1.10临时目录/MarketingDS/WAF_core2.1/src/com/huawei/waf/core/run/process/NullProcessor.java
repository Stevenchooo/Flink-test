package com.huawei.waf.core.run.process;

import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.run.AbstractProcessor;
import com.huawei.waf.protocol.RetCode;

public class NullProcessor extends AbstractProcessor {

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void destroy() {
    }

    /* 将所有输入参数放到输出参数中
     * @see com.huawei.core.facade.IProcessor#process(com.huawei.core.bean.MethodContext)
     */
    @Override
    public int process(MethodContext context) {
        return RetCode.OK;
    }

    @Override
    public int afterAll(MethodContext context) {
        return RetCode.OK;
    }
}
