package com.huawei.waf.core.run.process;

import java.io.InputStream;

import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

public class UploadRDBProcessor extends RDBProcessor implements IUploadProcessor {
    @Override
    public int process(MethodContext context) {
        int retCode = UploadProcessor.process(context, this);
        if(retCode != RetCode.OK) {
            context.setResult(retCode, "Fail to process file");
            return retCode;
        }
        return super.process(context);
    }
    
    /**
     * Overwrite it to handle the upload file 
     * @param paraName
     * @param fileName original file name
     * @param size size of the uploaded file
     * @param stream
     * @return if success to handle the file return RetCode.OK
     */
	@Override
    public int processFile(MethodContext context, String paraName, String fileName, long size, InputStream in) {
        return RetCode.OK;
    }
}
