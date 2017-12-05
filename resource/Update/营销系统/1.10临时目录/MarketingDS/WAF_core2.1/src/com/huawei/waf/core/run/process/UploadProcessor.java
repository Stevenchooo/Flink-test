package com.huawei.waf.core.run.process;

import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;

import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.parameter.FileParameterInfo;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.request.UploadRequester;
import com.huawei.waf.facade.run.AbstractProcessor;
import com.huawei.waf.protocol.RetCode;

public class UploadProcessor extends AbstractProcessor implements IUploadProcessor {
    private static final Logger LOG = LogUtil.getInstance();

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void destroy() {
    }

    @Override
    public int process(MethodContext context) {
        return process(context, this);
    }
    
    public static int process(MethodContext context, IUploadProcessor processor) {
        Object val;
        UploadRequester.FileInfo fi;
        String itemName;
        ParameterInfo[] parameters = context.getMethodConfig().getRequestConfig().getParameters();
        
        for(ParameterInfo pi : parameters) {
            if(!(pi instanceof FileParameterInfo)) {
                continue;
            }
            
            itemName = pi.getName();
            val = context.getParameter(itemName);
            if(val == null || !(val instanceof UploadRequester.FileInfo)) {
                if(pi.isMust()) {
                    LOG.info("Wrong file format of {}", itemName);
                    return context.setResult(RetCode.INTERNAL_ERROR, "Wrong file format of " + itemName);
                }
                continue;
            }
            
            fi = (UploadRequester.FileInfo)val;
            InputStream in = null;
            try {
                in = fi.item.getInputStream();
                int retCode = processor.processFile(context, itemName, fi.fileName, fi.item.getSize(), in);
                if(retCode != RetCode.OK) {
                    return retCode;
                }
            } catch (IOException e) {
                LOG.error("Fail to processFile {}.{}", itemName, fi.fileName, e);
                return context.setResult(RetCode.INTERNAL_ERROR, "Fail to handle " + fi.fileName);
            } finally {
                if(in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        LOG.error("Fail to close {}.{}", itemName, fi.fileName, e);
                    }
                }
                
                if(fi.item != null) { //删除临时文件
                    fi.item.delete();
                }
            }
        }
        
        return RetCode.OK;
    }
    
    /**
     * 请重载此函数，默认处理将文件存在saveDir（在接口的process中配置，默认为"./"），文件名为上传时的文件名
     * 这种处理，在绝大部分情况不符合要求，容易导致文件覆盖 
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
    
    @Override
    public int afterAll(MethodContext context) {
        return RetCode.OK;
    }
}
