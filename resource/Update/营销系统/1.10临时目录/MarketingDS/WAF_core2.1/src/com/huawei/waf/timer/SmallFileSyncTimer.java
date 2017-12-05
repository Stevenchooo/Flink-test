package com.huawei.waf.timer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;

import com.huawei.util.FileUtil;
import com.huawei.util.HttpUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.RSAUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.sys.TimerConfig;
import com.huawei.waf.facade.WAFTimerTask;
import com.huawei.waf.protocol.RetCode;

/**
 * 小文件同步定时器，建议文件不等于10M
 * @author l00152046
 * 用到两对公私钥，假设为
 * Private1<->Public1
 * Private2<->Public2
 * 请求时使用Private1签名，服务方用Public1验证签名，
 * 服务端响应时，用Private2签名，本地用Public2验证签名，
 * 这两对公私钥最好是不同的，否则一旦泄露则会很容易伪造
 */
public class SmallFileSyncTimer extends WAFTimerTask {
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final String CFG_PUBLIC_KEY = "publicKey";
    private static final String CFG_PRIVATE_KEY = "privateKey";
	private static final String CFG_URL = "url";
	private static final String CFG_REMOTEFILE = "remote";
	private static final String CFG_LOCALFILE = "local";
    
	private static final String PARAM_TIMEOUT = "timeout";

    private static final String REQUEST_NAME = "name";
    private static final String REQUEST_SIGN = "sign";
    private static final String REQUEST_TIME = "time"; //上次刷新的时间
    
    private static final String RESPONSE_SIZE = "size";
    private static final String RESPONSE_CONTENT = "content";
    private static final String RESPONSE_MD5 = "md5";
    private static final String RESPONSE_SIGN = "sign";
    private static final String RESPONSE_RETCODE = "resultCode";
	
    protected static final List<String> respSignParams = new ArrayList<String>();
    protected static final List<String> reqSignParams = new ArrayList<String>();
    
    protected byte[] privateKey = null;
    protected byte[] publicKey = null;
    
    private String remoteFile;
    private String localFile;
    
    static {
    	respSignParams.add(RESPONSE_SIZE);
    	respSignParams.add(RESPONSE_MD5);
    	
    	reqSignParams.add(REQUEST_NAME);
    	reqSignParams.add(REQUEST_TIME);
    }
    
	private String url;
	private int timeout = 30 * 1000; //请求最大超时时间
	private long updateTime = 0;
	
	@Override
	public boolean init(TimerConfig tc) {
		Map<String, Object> config = tc.getConfig();
		this.url = JsonUtil.getAsStr(config, CFG_URL);
		if(Utils.isStrEmpty(this.url)) {
			LOG.error("There are no {} config in timer {}", CFG_URL, tc.getName());
			return false;
		}
		this.timeout = JsonUtil.getAsInt(config, PARAM_TIMEOUT, this.timeout);

        this.remoteFile = JsonUtil.getAsStr(config, CFG_REMOTEFILE, null);
        if(Utils.isStrEmpty(this.remoteFile)) {
			LOG.error("There are no {} config in timer {}", CFG_REMOTEFILE, tc.getName());
        	return false;
        }
        
        this.localFile = JsonUtil.getAsStr(config, CFG_LOCALFILE, null);
        if(Utils.isStrEmpty(this.localFile)) {
			LOG.error("There are no {} config in timer {}", CFG_LOCALFILE, tc.getName());
        	return false;
        }
        
        
        String key = JsonUtil.getAsStr(config, CFG_PRIVATE_KEY, null);
    	this.publicKey = Base64.decodeBase64(key);
    	if(this.publicKey == null) {
    		LOG.error("Invalid {}, {}", CFG_PUBLIC_KEY, key);
    		return false;
    	}
        
        key = JsonUtil.getAsStr(config, CFG_PRIVATE_KEY, null);
        if(Utils.isStrEmpty(key)) {
			LOG.error("There are no {} config in timer {}", CFG_PUBLIC_KEY, tc.getName());
        	return false;
        }
        
    	this.privateKey = Base64.decodeBase64(key);
    	if(this.privateKey == null) {
    		LOG.error("Invalid {}, {}", CFG_PRIVATE_KEY, key);
    		return false;
    	}
		
		return true;
	}

	@Override
	public void run() {
		String url = HttpUtil.addUrlParameter(this.url, REQUEST_TIME, updateTime);
		url = HttpUtil.addUrlParameter(url, REQUEST_NAME, this.remoteFile);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(REQUEST_TIME, updateTime);
		params.put(REQUEST_NAME, this.remoteFile);
		
		String sign = RSAUtil.sign(params, reqSignParams, this.privateKey);
		url = HttpUtil.addUrlParameter(url, REQUEST_SIGN, sign);
		
		HttpResponse resp = HttpUtil.get(url, this.timeout);
		Map<String, Object> map = JsonUtil.jsonToMap(HttpUtil.getBinary(resp));
		
		int retCode = JsonUtil.getAsInt(map, RESPONSE_RETCODE, RetCode.API_ERROR);
		if(retCode != RetCode.OK) {
			LOG.error("Wrong {} {}", RESPONSE_RETCODE, retCode);
			return;
		}
		
		sign = JsonUtil.getAsStr(map, RESPONSE_SIGN);
		if(!RSAUtil.verify(map, respSignParams, sign, this.publicKey)) {
			LOG.error("Wrong {} {}", RESPONSE_SIGN, sign);
			return;
		}
		
		String sContent = JsonUtil.getAsStr(map, RESPONSE_CONTENT);
		byte[] content = Base64.decodeBase64(sContent);
		if(content == null || content.length <= 0) {
			LOG.error("Null {}", RESPONSE_CONTENT);
			return;
		}
		
		String md5 = JsonUtil.getAsStr(map, RESPONSE_MD5, "");
		if(!md5.equals(Utils.bin2hex(Utils.md5(content)))) {
			LOG.error("Wrong {} {}", RESPONSE_MD5, md5);
			return;
		}
		
		FileUtil.writeFile(new File(this.localFile), content);
	}
}
