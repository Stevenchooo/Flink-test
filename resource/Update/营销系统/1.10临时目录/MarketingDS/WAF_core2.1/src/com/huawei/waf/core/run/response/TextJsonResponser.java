package com.huawei.waf.core.run.response;

import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.Const;

public class TextJsonResponser extends JsonResponser {
    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void destroy() {
    }

    @Override
    public String getContentType(MethodContext  context) {
        return Const.TEXTPLAIN_CONTENT_TYPE;
    }
}