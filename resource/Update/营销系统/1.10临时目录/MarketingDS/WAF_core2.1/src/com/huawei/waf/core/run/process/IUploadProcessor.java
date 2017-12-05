package com.huawei.waf.core.run.process;

import java.io.InputStream;

import com.huawei.waf.core.run.MethodContext;

public interface IUploadProcessor {
    /**
     * Overwrite it to handle the upload file 
     * @param paraName
     * @param fileName original file name
     * @param size size of the uploaded file
     * @param stream
     * @return if success to handle the file return RetCode.OK
     */
    public int processFile(MethodContext context, String paraName, String fileName, long size, InputStream in);
}
