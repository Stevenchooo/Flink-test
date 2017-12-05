package com.huawei.waf.core.config.method.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.facade.config.AbstractProcessConfig;

public class SmallFileProcessConfig extends AbstractProcessConfig {
	private static final Logger LOG = LogUtil.getInstance();
	
    protected static final String PARAMETER_SAVEDIR = "saveDir"; //文件存放路径
    protected static final String PARAMETER_MAX_FILESIZE = "maxFileSize"; //bytes
    protected static final String PARAMETER_FILETYPE = "type"; //bytes
    protected static final String PARAMETER_ZIP = "zip";
    protected static final String PARAMETER_PUBLIC_KEY = "publicKey";
    protected static final String PARAMETER_PRIVATE_KEY = "privateKey";
    
    protected List<String> fileTypes = null;
    protected long maxFileSize = -1; //无限制
    protected String saveDir;
    protected boolean zip = false;
    protected byte[] privateKey = null;
    protected byte[] publicKey = null;
    
    @Override
    protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc) {
        this.zip = JsonUtil.getAsBool(json, PARAMETER_ZIP, this.zip);
        this.maxFileSize = JsonUtil.getAsLong(json, PARAMETER_MAX_FILESIZE, -1L);
        this.saveDir = JsonUtil.getAsStr(json, PARAMETER_SAVEDIR, this.saveDir);
        
        File f = new File(this.saveDir);
        if(!f.exists()) {
        	if(!f.mkdirs()) {
        		LOG.error("Fail to make save dir {}", this.saveDir);
        		return false;
        	}
        }
        
        String str = JsonUtil.getAsStr(json, PARAMETER_FILETYPE, null);
        if(!Utils.isStrEmpty(str)) {
            String[] ss = str.split(",");
            
            if(ss.length > 0) {
	            this.fileTypes = new ArrayList<String>();
	            for(String s : ss) {
	                s = s.trim();
	                if(!Utils.isStrEmpty(s)) {
	                    this.fileTypes.add(s);
	                }
	            }
            }
        }
        
        String key = JsonUtil.getAsStr(json, PARAMETER_PUBLIC_KEY, null);
        if(!Utils.isStrEmpty(key)) {
        	this.publicKey = Base64.decodeBase64(key);
        	if(this.publicKey == null) {
        		LOG.error("Invalid {}, {}", PARAMETER_PUBLIC_KEY, key);
        	}
        }
        
        key = JsonUtil.getAsStr(json, PARAMETER_PRIVATE_KEY, null);
        if(!Utils.isStrEmpty(key)) {
        	this.privateKey = Base64.decodeBase64(key);
        	if(this.privateKey == null) {
        		LOG.error("Invalid {}, {}", PARAMETER_PRIVATE_KEY, key);
        	}
        }
        
        return true;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }
    
    public String getSaveDir() {
        return saveDir;
    }
    
    public boolean isRightType(String type) {
        if(fileTypes == null) { //不限制
            return true;
        }
        
        if(Utils.isStrEmpty(type)) {
            return false;
        }
        
        return fileTypes.contains(type);
    }
    
    /**
     * 文件内容是否需要压缩
     * @return
     */
    public boolean isZipped() {
    	return zip;
    }
    
    /**
     * 服务端用于计算签名的私钥，由客户端发布
     * @return
     */
    public byte[] getPrivateKey() {
    	return privateKey;
    }
    
    /**
     * 服务端验证客户端算签名的公钥，由服务端发布，响应私钥发给客户端
     * @return
     */
    public byte[] getPublicKey() {
    	return publicKey;
    }
}
