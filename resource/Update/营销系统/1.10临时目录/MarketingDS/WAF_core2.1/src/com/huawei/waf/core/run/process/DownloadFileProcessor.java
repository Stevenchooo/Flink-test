package com.huawei.waf.core.run.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import com.huawei.util.FileUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.RSAUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.process.SmallFileProcessConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * @author l00152046
 * 接受其他同步到本系统的文件，文件内容base64编码，
 * 直接通过json方式发送，文件内容作为一个字段，base64解密后，存入本地，
 * 接受请求时，要求使用私钥对请求内容签名，
 * 响应时，传递了文件内容的md5，及服务端私钥的签名，调用方需要验证md5，并且使用公钥验证签名
 * 
 * 用到两对公私钥，假设为
 * Private1<->Public1
 * Private2<->Public2
 * 请求时调用方用Private1签名，服务方用Public1验证签名，
 * 服务端响应时，用Private2签名，本地用Public2验证签名，
 * 这两对公私钥最好是不同的，否则一旦泄露则会很容易伪造
 */
public class DownloadFileProcessor extends DefaultJavaProcessor {
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final String REQUEST_NAME = "name";
    private static final String REQUEST_SIGN = "sign";
    private static final String REQUEST_TIME = "time"; //上次刷新的时间
    
    private static final String RESPONSE_SIZE = "size";
    private static final String RESPONSE_CONTENT = "content";
    private static final String RESPONSE_MD5 = "md5";
    private static final String RESPONSE_SIGN = "sign";
    private static final String RESPONSE_TIME = "time"; //上次刷新的时间
    
    protected static final List<String> respSignParams = new ArrayList<String>();
    protected static final List<String> reqSignParams = new ArrayList<String>();
    
    static {
    	respSignParams.add(RESPONSE_SIZE);
    	respSignParams.add(RESPONSE_MD5);
    	
    	reqSignParams.add(REQUEST_NAME);
    	reqSignParams.add(REQUEST_TIME);
    }
    
    @Override
    public int process(MethodContext context) {
        SmallFileProcessConfig sfpc = (SmallFileProcessConfig)context.getMethodConfig().getProcessConfig();
        
    	if(!checkSign(context, sfpc)) {
    		LOG.error("Fail to check sign");
    		return RetCode.WRONG_SIGNATURE;
    	}
        Map<String, Object> reqParams = context.getParameters();
        
        String name = JsonUtil.getAsStr(reqParams, REQUEST_NAME);
        
        long time = JsonUtil.getAsLong(reqParams, REQUEST_TIME);
        long updateTime = getUpdateTime(sfpc, name);
        if(time <= updateTime) {
        	LOG.info("File {} not updated, time:{}, updateTime:{}", name, time, updateTime);
        	return RetCode.NEED_NOT_DO;
        }
        
        String type = FileUtil.getFileExtension(name);
        if(!sfpc.isRightType(type)) {
        	LOG.error("Wrong file type {}", type);
        	return context.setResult(RetCode.WRONG_PARAMETER, "Type '" + type + "' is not a valid file type");
        }
        
        String fileName = FileUtil.addPath(sfpc.getSaveDir(), name);
        byte[] content;
        
        if(sfpc.isZipped()) {
        	content = FileUtil.zipFile(fileName);
        } else {
        	content = FileUtil.readFile(new File(fileName));
        }
        
        if(content == null || content.length <= 0) {
        	LOG.error("Invalid file {}", fileName);
        	return context.setResult(RetCode.INTERNAL_ERROR, "Invalid file");
        }
        
        String contentBase64 = Base64.encodeBase64String(content);
        if(contentBase64 == null || contentBase64.length() <= 0) {
        	LOG.error("Content is not valid");
        	return context.setResult(RetCode.INTERNAL_ERROR, "Fail to base64 content");
        }
        
        context.setResult(RESPONSE_SIZE, content.length);
        context.setResult(RESPONSE_CONTENT, contentBase64);
        context.setResult(RESPONSE_TIME, updateTime);
        context.setResult(RESPONSE_MD5, Utils.bin2base64(Utils.md5(content)));
        try {
            context.setResult(RESPONSE_SIGN, calculateSign(context, sfpc));
        } catch(Exception e) {
            LOG.error("Fail to generate sign", e);
            return context.setResult(RetCode.INTERNAL_ERROR, "Fail to generate sign");
        }
        
        return RetCode.OK;
    }
    
    /**
     * 计算签名，如果签名方式不同，可以重载此函数
     * @param context
     * @param content 解码后的文件内容
     * @return
     */
    protected String calculateSign(MethodContext context, SmallFileProcessConfig sfpc) throws Exception {
    	return RSAUtil.sign(context.getResults(), respSignParams, sfpc.getPrivateKey());
    }
    
    
    /**
     * 检查校验字段
     * @param context
     * @param sfpc
     * @return
     */
    protected boolean checkSign(MethodContext context, SmallFileProcessConfig sfpc) {
    	Map<String, Object> reqParameters = context.getParameters();
    	String sign = JsonUtil.getAsStr(reqParameters, REQUEST_SIGN);
    	return RSAUtil.verify(reqParameters, reqSignParams, sign, sfpc.getPublicKey());
    }
    
    /**
     * 获取最新的更新时间，默认实现使用文件最新更新时间
     * @param sfpc
     * @param name
     * @return
     */
    protected long getUpdateTime(SmallFileProcessConfig sfpc, String name) {
        String fileName = FileUtil.addPath(sfpc.getSaveDir(), name);
        File f = new File(fileName);
		return f.lastModified();
    }
}
