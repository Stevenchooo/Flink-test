package com.huawei.waf.core.run.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.parameter.FileParameterInfo;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.run.AbstractProcessor;
import com.huawei.waf.protocol.RetCode;

/**
 * @author l00152046
 * 小文件上传，不需要fileupload插件，而是直接传输一个base64格式的文件内容，
 * 适合小文件上传
 * 
 */
public class SmallUploadProcessor extends AbstractProcessor implements IUploadProcessor {
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
        String itemName;
        ParameterInfo[] parameters = context.getMethodConfig().getRequestConfig().getParameters();
        
        for(ParameterInfo pi : parameters) {
            if(!(pi instanceof FileParameterInfo)) {
                continue;
            }
            
            itemName = pi.getName();
            val = context.getParameter(itemName);
            if(val == null || !(val instanceof String)) {
                if(pi.isMust()) {
                    LOG.info("Wrong file format of {}", itemName);
                    return context.setResult(RetCode.INTERNAL_ERROR, "Wrong file format of " + itemName);
                }
                continue;
            }
            
            FileParameterInfo fpi = (FileParameterInfo)pi;
            InputStream in = null;
            try {
            	byte[] content = Base64.decodeBase64((String)val); //标准base64格式
                in = new ByteArrayInputStream(content);
                int retCode = processor.processFile(context, itemName, fpi.getFileName(), content.length, in);
                if(retCode != RetCode.OK) {
                    return retCode;
                }
            } catch (Exception e) {
                LOG.error("Fail to processFile {}.{}", itemName, fpi.getFileName(), e);
                return context.setResult(RetCode.INTERNAL_ERROR, "Fail to handle " + fpi.getFileName());
            } finally {
                if(in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        LOG.error("Fail to close {}.{}", itemName, fpi.getFileName(), e);
                    }
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
