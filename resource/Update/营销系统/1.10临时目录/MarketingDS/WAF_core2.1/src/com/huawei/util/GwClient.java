package com.huawei.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

import org.slf4j.Logger;

import nsp.NSPClient;
import nsp.OAuth2Client;
import nsp.support.common.AccessToken;
import nsp.support.common.NSPException;

import com.huawei.waf.core.WAFException;
import com.huawei.waf.core.config.sys.SecurityConfig;

/**
 * GAF客户端工具类
 * 
 * @author l00152046
 * @version [VMALL OMS V100R001C01, 2013-9-11]
 * @since [VMALL OMS]
 */
public class GwClient {
	private static final String SERVER_GRANT_TYPE = "client_credentials";

	private static final Logger LOG = LogUtil.getInstance();

	private static final int TIMEOUT_EXCEPTION_CODE = 6;

	private static final int TOKEN_ILLEGAL = 102;

	private static GwClient gafClient = null;

	private static long sesssionTimeout = (86400 * 1000 * 3 / 4) + System.currentTimeMillis();

	private NSPClient client = null;

	private String loginUrl = null;

	private String apiUrl = null;

	private String clientId = null;

	private String secret = null;
	
	private String keyStoreFile = null;
	
	/**
	 * 获得单例
	 * 
	 * @return GAFClient
	 * @throws Exception
	 */
	public static final GwClient getInstance() throws WAFException {
		return GwClient.gafClient;
	}

	/**
	 * 初始化
	 * @param cfg 配置信息
	 * @return boolean
	 */
	public static final synchronized boolean init(String cfgPath, Map<String, Object> cfg) {
		if (GwClient.gafClient != null) {
			LOG.info("There is already an GAFClient exists");
			return true;
		}
		
		GwClient gwClient = new GwClient();
		
		gwClient.loginUrl = JsonUtil.getAsStr(cfg, "loginUrl");
		gwClient.apiUrl = JsonUtil.getAsStr(cfg, "apiUrl");
		gwClient.clientId = JsonUtil.getAsStr(cfg, "clientId");
		
		//解密网关密码-------------------
		String secret = JsonUtil.getAsStr(cfg, "secret", null);
		if(Utils.isStrEmpty(secret)) {
		    LOG.error("No valid secret config for gw");
		    return false;
		}
		
        gwClient.secret = EncryptUtil.configDecode(secret);
        if(gwClient.secret == null) {
            LOG.error("Fail to decode gw-secret, by salt:{}, iterationCount:{}", SecurityConfig.getSalt(), SecurityConfig.getIterationCount());
            return false;
        }
        //---------------------------
		
		gwClient.keyStoreFile = JsonUtil.getAsStr(cfg, "keyStore");
		if(Utils.isStrEmpty(gwClient.keyStoreFile)) {
			gwClient.keyStoreFile = cfgPath + "gwkeystore.jks";
		} else {
			gwClient.keyStoreFile = cfgPath + gwClient.keyStoreFile;
		}
		
		if(gwClient.initInternal()) {
			GwClient.gafClient = gwClient;
			return true;
		}
		
		return false;
	}
	
	private boolean initInternal() {
		LOG.info("Init GAFClient, loginUrl={},apiUrl={},keyStoreFile={}", loginUrl, apiUrl, keyStoreFile);

		try {
			LOG.info("Initialize GAFClient ...");
			this.client = getClient(loginUrl, apiUrl, clientId, secret, keyStoreFile);
			if (this.client != null) {
				LOG.info("Initialize GAFClient Success!");
			}
			return true;
		} catch (NSPException e) {
			LOG.error("Fail to init", e);
		}

		return false;
	}

	/**
	 * 传map参数，并返回map参数
	 * example:call("vmall.oms.queryOrder", new HashMap<String,
	 * Object>(){{put("userId", "yufangyi2");}});
	 * example:call("vmall.oms.createOrder", new HashMap<String, Object>(){{
	 * put("createOrderReq", new HashMap<String, Object>(){{...}}); }} );
	 * 
	 * @param name
	 *            接口名称
	 * @param parameter
	 *            参数
	 * @return Map<String, Object> 返回信息
	 */
	public Map<String, Object> call(String name, Map<String, Object> parameter) {
		try {
			NSPClient nspClient = getClient();
			if (nspClient == null) {
				if (initInternal()) {
					nspClient = getClient();
				}
				
				if (nspClient == null) {
					return null;
				}
			}

			return nspClient.call(name, parameter, Map.class);
		} catch (NSPException e) {
			if (e.getCode() == TIMEOUT_EXCEPTION_CODE || e.getCode() == TOKEN_ILLEGAL) {
				try {
					this.client = getClient();
					return call(name, parameter);
				} catch (NSPException ex) {
					LOG.error("Fail to get gw client", ex);
				}
			}
			LOG.error("Fail to call ", name, e);
		}

		return null;
	}

	private static final NSPClient getClient(String loginUrl, String apiUrl,
			String clientId, String secret, String keyStoreFile) throws NSPException 
	{
		LOG.debug("Refresh token from ", loginUrl);
		NSPClient client = null;

		// 获取token
		OAuth2Client authClient = new OAuth2Client();
		authClient.setTokenUrl(loginUrl);
		try {
			authClient.initKeyStoreStream(new FileInputStream(new File(keyStoreFile)), "123456");
		} catch (KeyStoreException e) {
			LOG.error("Fail to init KeyStore , KeyStoreException", e);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Fail to init KeyStore ,NoSuchAlgorithmException", e);
		} catch (CertificateException e) {
			LOG.error("Fail to init KeyStore ,CertificateException", e);
		} catch (IOException e) {
			LOG.error("Fail to init KeyStore , IOException", e);
		}
		AccessToken token = authClient.getAccessToken(SERVER_GRANT_TYPE, clientId, secret);

		String tokenStr = token.getAccess_token();
		if (tokenStr == null || tokenStr.isEmpty()) {
			LOG.error("Fail to get token from ", loginUrl);
			return null;
		}
		// session过期时间设置成网关给点时间的四分之三
		GwClient.sesssionTimeout = (3 * ((long) token.getExpires_in() * 1000) / 4) + System.currentTimeMillis();

		LOG.info("token.getExpires_in() =={}", token.getExpires_in());
		client = new NSPClient(tokenStr);
		client.initHttpConnections(200, 500);
		LOG.debug("Create connection to NSP-server ", apiUrl);
		client.setApiUrl(apiUrl);

		return client;
	}

	private final synchronized NSPClient getClient() throws NSPException {
		if (System.currentTimeMillis() >= GwClient.sesssionTimeout) {
			LOG.info("GAFClient.sesssionTimeout == {}, interval={}",
					GwClient.sesssionTimeout, GwClient.sesssionTimeout - System.currentTimeMillis());
			this.client = getClient(this.loginUrl, this.apiUrl, this.clientId, this.secret, this.keyStoreFile);
		}

		return client;
	}
}
