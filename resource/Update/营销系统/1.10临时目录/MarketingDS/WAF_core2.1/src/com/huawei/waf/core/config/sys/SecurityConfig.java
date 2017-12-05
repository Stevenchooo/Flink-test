package com.huawei.waf.core.config.sys;

import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.EncryptUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;

public class SecurityConfig {
    public static final String DEFAULT_SALT = "1A2B3C4D5E6F";
    public static final int DEFAULT_ITERATIONCOUNT = 2;
    
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final String CFG_DATASECRET = "dataSecret";
    private static final String CFG_CONFIGSECRET = "configSecret";
    private static final String CFG_PASSWORDVALIDDAYS = "passwordValidDays";
    private static final String CFG_USERLOCKINTERVAL = "userLockInterval";
    private static final String CFG_PASSWORDREPETTIMES = "passwordRepetTimes";
    private static final String CFG_LOGINLOCKTYPE = "loginLockType";
    
    private static final String CFG_MAXRELOGINTIMES     = "maxReloginTimes";
    private static final String CFG_MINPASSWORDLEN      = "minPasswordLen";
    private static final String CFG_MAXPASSWORDLEN      = "maxPasswordLen";
    private static final String CFG_MAXDUPPWDNUM        = "maxDupPwdNum"; //最近密码重复个数
    private static final String CFG_MIN_CHARTYPE_NUM    = "minCharTypeNum";
    private static final String CFG_MIN_DIFFCHAR_NUM    = "minDiffCharNum";
    private static final String CFG_MAX_VALID_DAY       = "maxValidDayNum"; //密码最大超期天数
    private static final String CFG_VERIFYCODE_LEN      = "verifyCodeLen";
    private static final String CFG_VERIFYCODE_WIDTH    = "verifyCodeWidth";
    private static final String CFG_VERIFYCODE_HEIGHT   = "verifyCodeHeight";
    
    private static final String CFG_ITERATIONCOUNT = "iterationCount";
    private static final String CFG_SALT = "salt";
    
    private static String loginLockType = "user"; //按用户或ip锁定用户
	private static int passwordValidDays = 60; //密码有效期（天），默认60天
	private static int userLockInterval = 86400;//输错密码被锁后，锁定期（单位秒），默认1天
	private static int passwordRepetTimes = 3; //密码重试次数

	private static int maxReloginTimes = 3; //密码重试次数
	private static int minPasswordLen = 6; //密码最小长度
	private static int maxPasswordLen = 20; //密码最大长度
	
    private static int minCharTypeNum = 2;  //密码中字符的种类数|0-9|a-z|A-Z|其他|,最多4种
    private static int minDiffCharNum = 4; //密码中不重复的字符个数，比如aabb为2
    private static int maxValidDayNum = 60;
    
    private static int verifyCodeLen = 4;
    private static int verifyCodeWidth = 70;
    private static int verifyCodeHeight = 22;
        
	private static byte[] salt = Utils.hex2bin(DEFAULT_SALT); //用于ase加密
	private static int iterationCount = DEFAULT_ITERATIONCOUNT; //用于ase加密
	private static int maxDupPwdNum = 3;
	private static byte[] dataSecret = "wnD_XkPEeX4ROd1FWC4SDB".getBytes();
    private static byte[] configSecret = "4kjDMmOiW349NNAc-QJUnB".getBytes();
	
    public static boolean init(Map<String, Object> config) {
        if(config == null) {
            LOG.info("No security config, use default");
            return true;
        }
        
        loginLockType = JsonUtil.getAsStr(config, CFG_LOGINLOCKTYPE, "user");
        if(!"ip".equals(loginLockType) && !"user".equals(loginLockType)) {
            LOG.error("Wrong {} config info for security, only ip and user permitted", CFG_LOGINLOCKTYPE);
            return false;
        }
        passwordValidDays = JsonUtil.getAsInt(config, CFG_PASSWORDVALIDDAYS, passwordValidDays);
        
        userLockInterval = JsonUtil.getAsInt(config, CFG_USERLOCKINTERVAL, userLockInterval);
        passwordRepetTimes = JsonUtil.getAsInt(config, CFG_PASSWORDREPETTIMES, passwordRepetTimes);
        
        maxReloginTimes = JsonUtil.getAsInt(config, CFG_MAXRELOGINTIMES, maxReloginTimes);
        minPasswordLen = JsonUtil.getAsInt(config, CFG_MINPASSWORDLEN, minPasswordLen);
        maxPasswordLen = JsonUtil.getAsInt(config, CFG_MAXPASSWORDLEN, maxPasswordLen);
        maxDupPwdNum = JsonUtil.getAsInt(config, CFG_MAXDUPPWDNUM, maxDupPwdNum);
        if(maxDupPwdNum > 5) {
            maxDupPwdNum = 5;
            LOG.warn("{} can't be bigger than 5", CFG_MAXDUPPWDNUM);
        }
        
        minCharTypeNum = JsonUtil.getAsInt(config, CFG_MIN_CHARTYPE_NUM, minCharTypeNum);
        if(minCharTypeNum <= 0 || minCharTypeNum > 4) {
            LOG.error("Wrong {} config info for security, must between (1-4)", CFG_MIN_CHARTYPE_NUM);
            return false;
        }
        
        minDiffCharNum = JsonUtil.getAsInt(config, CFG_MIN_DIFFCHAR_NUM, minDiffCharNum);
        if(minDiffCharNum <= 0 || minDiffCharNum > minPasswordLen) {
            LOG.error("Wrong {} config info for security, must between (1-{})", CFG_MIN_DIFFCHAR_NUM, minPasswordLen);
            return false;
        }
        maxValidDayNum = JsonUtil.getAsInt(config, CFG_MAX_VALID_DAY, maxValidDayNum);
        
        verifyCodeLen = JsonUtil.getAsInt(config, CFG_VERIFYCODE_LEN, verifyCodeLen);
        verifyCodeWidth = JsonUtil.getAsInt(config, CFG_VERIFYCODE_WIDTH, verifyCodeWidth);
        verifyCodeHeight = JsonUtil.getAsInt(config, CFG_VERIFYCODE_HEIGHT, verifyCodeHeight);
        
        iterationCount = JsonUtil.getAsInt(config, CFG_ITERATIONCOUNT, iterationCount);
        String saltStr = JsonUtil.getAsStr(config, CFG_SALT, DEFAULT_SALT);
        if ((saltStr.length() & 0x01) != 0 || !Utils.isStrMatch(saltStr, "[0-9|a-f|A-F]+")) {
            LOG.error("Length of salt must be even number, and must be [0-9|a-f|A-F]");
            return false;
        }
        salt = Utils.hex2bin(saltStr);
        
        //必须放在最后，因为解密时用到salt与iterationCount
        if(config.containsKey(CFG_DATASECRET)) {
            dataSecret = EncryptUtil.rootDecode(JsonUtil.getAsStr(config, CFG_DATASECRET)).getBytes();
        }
        if(config.containsKey(CFG_CONFIGSECRET)) {
            configSecret = EncryptUtil.rootDecode(JsonUtil.getAsStr(config, CFG_CONFIGSECRET)).getBytes();
        }
        
        return true;
    }
    
    public static final String getLoginLockType() {
        return loginLockType;
    }

    public static final int getPasswordValidDays() {
        return passwordValidDays;
    }

    public static final int getMaxDupPwdNum() {
        return maxDupPwdNum;
    }

    public static final int getUserLockInterval() {
        return userLockInterval;
    }

    public static final int getPasswordRepetTimes() {
        return passwordRepetTimes;
    }

    public static final int getMaxReloginTimes() {
        return maxReloginTimes;
    }

    public static final int getMinPasswordLen() {
        return minPasswordLen;
    }

    public static final int getMaxPasswordLen() {
        return maxPasswordLen;
    }
    
    public static final int getIterationCount() {
        return iterationCount;
    }
    
    public static final byte[] getSalt() {
        return salt;
    }
    
    public static final int getMinCharTypeNum() {
        return minCharTypeNum;
    }
    
    public static final int getMinDiffCharNum() {
        return minDiffCharNum;
    }
    
    /**
     * 验证码字符个数
     * @return
     */
    public static final int getVerifyCodeLen() {
        return verifyCodeLen;
    }
    
    /**
     * 验证码宽度
     * @return
     */
    public static final int getVerifyCodeWidth() {
        return verifyCodeWidth;
    }
    
    /**
     * 验证码高度
     * @return
     */
    public static final int getVerifyCodeHeight() {
        return verifyCodeHeight;
    }
    
    
    /**
     * 密码最大超期天数，超过后需要用户修改密码
     * @return
     */
    public static final int getMaxValidDayNum() {
        return maxValidDayNum;
    }
    
    /**
     * 用于加密数据库数据的密码，密码内容使用根密码加密
     * @return
     */
    public static final byte[] getDataSecret() {
        return dataSecret;
    }
    
    /**
     * 用于加密配置数据的密码，密码内容使用根密码加密
     * @return
     */
    public static final byte[] getConfigSecret() {
        return configSecret;
    }
    
    public static String toStr() {
        return "loginLockType:" + loginLockType
            + ",passwordValidDays:" + passwordValidDays
            + ",userLockInterval:" + userLockInterval
            + ",passwordRepetTimes:" + passwordRepetTimes
            + ",maxReloginTimes:" + maxReloginTimes
            + ",minPasswordLen:" + minPasswordLen
            + ",maxPasswordLen:" + maxPasswordLen
            + ",maxDupPwdNum:" + maxDupPwdNum
            + ",maxValidDayNum:" + maxValidDayNum
            + ",verifyCodeLen:" + verifyCodeLen
            + ",minCharTypeNum:" + minCharTypeNum
            + ",minDiffCharNum:" + minDiffCharNum;
    }
}
