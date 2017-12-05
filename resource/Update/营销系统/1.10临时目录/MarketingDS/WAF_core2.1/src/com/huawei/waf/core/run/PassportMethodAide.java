package com.huawei.waf.core.run;

import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.AESUtil;
import com.huawei.util.CookieUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.facade.AbstractInitializer;
import com.huawei.waf.protocol.Const;

import static com.huawei.waf.protocol.RetCode.*;

import com.huawei.waf.facade.run.IMethodAide;

/**
 * @author l00152046
 * 
 * 项目助手，实现项目中可能不同的部分，比如用户认证等，
 * 默认使用通行证方式实现用户认证
 * 通行证中默认携带 CheckType(1) + Time(4) + Key(16) + Account(>1)
 * 可以根据项目需要，继承重载DefaultProjectAide对象，修改通行证的内容
 *
 */
public class PassportMethodAide implements IMethodAide {
    /**
     * 临时密钥，客户端用rsa加密传送到服务端，服务端用私钥解开
     * 放入通行证中，后期凡是需加密传输的使用aes加密算法传输
     */
    
    private static final Logger LOG = LogUtil.getInstance();

    private static final String INVALID_AUTH = "Invalid " + AUTH + ',';
    
    private static final int PASSPORT_FIXED_LEN = 1 + 4 + 16; //checkType + time + key
    
    //2010.1.1的时间戳，减去一个偏移，使得计时有效性延长30年
    private static final int BIAS_TIME = 1262342147; 
    
    @Override
    public String getPassportName() {
        return AUTH;
    }
    
    /*
     * 当接口或页面authType为none，不会刷新通行证的时间，
     * 这里需要注意，当用户登录时，如果大部分是none，可能会莫名其妙的登出，
     * 所以在设计接口时，应该做适当的取舍，关键操作应该是需要认证的
     * @see com.huawei.waf.facade.AbstractProjectAide#parseAuthInfo(com.huawei.waf.core.run.MethodContext)
     */
    @Override
    public int parseAuthInfo(MethodContext context) {
        return parseAuthInfo(context, WAFConfig.getAuthCode());
    }
    
	public int parseAuthInfo(MethodContext context, byte[] authCode) {
		int authType = context.getMethodConfig().getAuthType();
		int retCode = OK;
        
        process: {
   	        //当为none时，不检查通行证，也不会刷新通行证的时间
            if(authType == MethodConfig.AUTH_NONE) {
            	context.setClientIp(null);
                break process;
            }
            
            Map<String, Object> parameters = context.getParameters();
            
            try {
                //CheckType(1) + Time(4) + Key(16) + Account(>1) 
                byte[] passport = Utils.base642bin(JsonUtil.getAsStr(parameters, AUTH, null));
                if(passport == null) {
                    retCode = context.setResult(INVALID_SESSION, INVALID_AUTH + "it is null");
                    break process;
                }
                
                passport = AESUtil.aesDecrypt(passport, authCode, WAFConfig.getInitVec());
                if(passport == null || passport.length <= PASSPORT_FIXED_LEN) {
                    retCode = context.setResult(INVALID_SESSION, INVALID_AUTH + "fail to decode");
                    break process;
                }
                
                //以秒为单位，可以计时70年
                int curTime = (int)(System.currentTimeMillis() / 1000) - BIAS_TIME;
                
                ByteBuffer buff = ByteBuffer.wrap(passport);
                
                int checkType = context.getCheckType(); //回话中设置了checkType 
                if(checkType == CHECK_TYPE_NULL) {
                	checkType = ((int)buff.get()) & 0xff;
                    context.setCheckType(checkType);
                } else {
                	buff.get(); //跳过一个字节
                }
                
                /**
                 * 临时通行证，只能用于AUTH_ONCE的method
                 * 用于需要用户认证，但是又不能直接登录的情况
                 */
                if((checkType & CHECK_TYPE_ONCE) != 0 && authType != MethodConfig.AUTH_ONCE) {
                    retCode = context.setResult(INVALID_SESSION, INVALID_AUTH + "it is once");
                    break process;
                }
                
                int passportTime = buff.getInt();
                if((checkType & CHECK_TYPE_FOREVER) == 0) { //永久通行证无需判断
                    if(curTime >= passportTime) {
                        retCode = context.setResult(INVALID_SESSION, INVALID_AUTH + "expired");
                        break process;
                    }
                }
                
                byte[] key = new byte[16];
                buff.get(key);
                String userKey = Utils.bin2base64(key);
                context.setUserKey(userKey);
                if((checkType & CHECK_TYPE_KEY) != 0) {
                    String clientKey = JsonUtil.getAsStr(parameters, USERKEY, "");
                    if(!userKey.equals(clientKey)) {
                        retCode = context.setResult(INVALID_USERKEY, "invalid key");
                        break process;
                    }
                }
                
                
                byte[] buf = new byte[passport.length - PASSPORT_FIXED_LEN];
                buff.get(buf);
                String acc = new String(buf, Const.DEFAULT_CHARSET);
                String ip = null;

                if((checkType & CHECK_TYPE_IP) != 0) {
                    /**
                     * 当使用checkip时，账号就是用户的IP，需要先通过ip作为账号登录，
                     * 然后每次都会检查ip是否为登录时的ip
                     */
                    ip = acc;
                    String remoteAddr = context.getRemoteAddr();
                    if(!remoteAddr.equals(ip)) {
                        retCode = context.setResult(INVALID_CLIENTIP, "invalid client ip " + remoteAddr);
                        break process;
                    }
                }
                
        		context.setAccount(acc);
                context.setClientIp(ip);
                
                if(!WAFConfig.isRemoteAddrValid(context.getClientIp())) {
                    retCode = context.setResult(INVALID_CLIENTIP, "Invalid ip " + context.getClientIp());
                    break process;
                }
                
                if((checkType & CHECK_TYPE_FOREVER) == 0) { //永久通行证无需重写
                    //通行证过期时间
                    passportTime = curTime + WAFConfig.getSessionTimeout();
                    for(int i = 3; i >= 0; i--) {
                        passport[i + 1] = (byte)(passportTime & 0xff);
                        passportTime >>>= 8;
                    }
                    
                    String val = Utils.bin2base64(AESUtil.aesEncrypt(passport, authCode, WAFConfig.getInitVec()));
                    CookieUtil.setCookie(
                         context.getResponse(),
                         WAFConfig.getCookieHead() + getPassportName(),
                         val,
                         AbstractInitializer.getWebRoot(),
                         context.isHttps()
                    );
                }
            } catch(Exception e) {
                LOG.error("Fail to parse passport", e);
                retCode = context.setResult(INVALID_SESSION, INVALID_AUTH + "invalid passport");
                break process;
            }
            
            context.setResult(USERACCOUNT, context.getAccount());
    	} //end of process
		
        /**
         * 通行证发生任何错误，删除通行证
         */
		if(retCode != OK) {
		    CookieUtil.removeCookie(
	            context.getResponse(),
	            WAFConfig.getCookieHead() + getPassportName(),
	            AbstractInitializer.getWebRoot()
	        );
		}
		
		return retCode;
	}

	
	/* 
	 * checkType有CHECK_TYPE_IP|CHECK_TYPE_KEY|CHECK_TYPE_ONCE，位或方式
	 * @see com.huawei.waf.facade.AbstractProjectAide#saveAuthInfo(com.huawei.waf.core.run.MethodContext, byte[], int)
	 */
	@Override
	public void saveAuthInfo(MethodContext context, String account, String userKey, int checkType) {
        String val = genAuthInfo(context, WAFConfig.getAuthCode(), account, userKey, checkType);
        
        CookieUtil.setCookie(
            context.getResponse(),
            WAFConfig.getCookieHead() + AUTH,
            val,
            AbstractInitializer.getWebRoot(),
            context.isHttps()
        );
	}


    /* cookie中设置了httponly，这种cookie在客户端不能操作，
     * 所以logout操作时，必须在服务端removeCookie
     * @see com.huodian.waf.facade.run.IMethodAide#removeAuthInfo(com.huodian.waf.core.run.MethodContext)
     */
    @Override
    public void removeAuthInfo(MethodContext context) {
        CookieUtil.removeCookie(
            context.getResponse(),
            WAFConfig.getCookieHead() + getPassportName(),
            AbstractInitializer.getWebRoot()
        );
    }
	
    private String genAuthInfo(MethodContext context, byte[] authCode, String account, String userKey, int checkType) {
        byte[] acc = account.getBytes(Const.DEFAULT_CHARSET);
        ByteBuffer buff = ByteBuffer.allocate(PASSPORT_FIXED_LEN + acc.length);
        buff.put((byte)checkType);
        //秒为单位
        int passportTime = (int)(System.currentTimeMillis() / 1000 - BIAS_TIME + WAFConfig.getSessionTimeout());
        buff.putInt(passportTime);
        /**
         * 是否使用key，取决于客户端调用时是否携带了USERKEY字段，
         * 如果携带了则记录到通行证中，后面的请求如果指示aes或azdg加密，
         * 则使用这个密钥解开，响应也同样用aes或azdg加密
         */
        buff.put(Utils.base642bin(userKey));
        buff.put(acc);
        
        try {
            return Utils.bin2base64(AESUtil.aesEncrypt(buff.array(), authCode, WAFConfig.getInitVec()));
        } catch (Exception e) {
            LOG.error("Fail to encrypt passport", e);
        }
        return null;
    }
    
    @Override
    public String getLanguage(MethodContext context) {
        String lang = JsonUtil.getAsStr(context.getParameters(), LANGUAGE, null);
        if(!Utils.isStrEmpty(lang)) {
            return lang;
        }
        return context.getRequest().getLocale().toString();
    }
}
