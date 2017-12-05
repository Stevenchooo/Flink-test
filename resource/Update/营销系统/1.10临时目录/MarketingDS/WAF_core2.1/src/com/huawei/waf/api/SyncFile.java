package com.huawei.waf.api;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.FileUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.process.SmallFileProcessConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.DefaultJavaProcessor;
import com.huawei.waf.protocol.RetCode;

/**
 * @author l00152046
 * 接受其他同步到本系统的文件，文件内容base64编码，
 * 直接通过json方式发送，文件内容作为一个字段，base64解密后，存入本地，
 * 不适合大文件同步，提交
 */
public class SyncFile extends DefaultJavaProcessor {
    private static final Logger LOG = LogUtil.getInstance();
    private static final String PARAM_FILE = "file";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_SIZE = "size";
    
    @Override
    public int process(MethodContext context) {
        Map<String, Object> reqParams = context.getParameters();
        
        String name = JsonUtil.getAsStr(reqParams, PARAM_NAME);
        String type = FileUtil.getFileExtension(name);
        /**
         * api的type值必须为SMALLUPLOAD
         */
        SmallFileProcessConfig sfpc = (SmallFileProcessConfig)context.getMethodConfig().getProcessConfig();

        if(!sfpc.isRightType(type)) {
        	LOG.error("Wrong file type {}", type);
        	return context.setResult(RetCode.WRONG_PARAMETER, "Type '" + type + "' is not a valid file type");
        }
        
        int size = JsonUtil.getAsInt(reqParams, PARAM_SIZE, 0);
        String content = JsonUtil.getAsStr(reqParams, PARAM_FILE);
        
        if(size <= 0 || content.length() < size) {
        	LOG.error("Size is not valid size:{}, content size:{}", size, content.length());
        	return RetCode.WRONG_PARAMETER;
        }
        
        byte[] b = Utils.base642bin(content);
        if(b == null || b.length <= 0) {
        	LOG.error("Content is not valid");
        	return RetCode.WRONG_PARAMETER;
        }
        
        if(b.length != size) {
        	LOG.error("Size is not valid size:{}, content size:{}", size, b.length);
        	return RetCode.WRONG_PARAMETER;
        }
        
        if(!FileUtil.writeFile(new File(sfpc.getSaveDir(), name), b)) {
        	return RetCode.INTERNAL_ERROR;
        }
        
        return RetCode.OK;
    }
}
