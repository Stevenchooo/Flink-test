package com.huawei.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import com.huawei.waf.core.config.sys.SecurityConfig;
import com.huawei.waf.protocol.Const;

public class EncryptUtil {
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 密钥长度
     */
    private static final int SECRET_LEN = 16;
    
    /**
     * 使用根密码加密，返回base64方式的密文
     * @param text 明文
     * @param salt 盐值
     * @param iterationCount 迭代次数
     * @return
     */
    public static final String rootEncode(String text, byte[] salt, int iterationCount) {
        try {
            byte[] secret = execute(SECRET_LEN, l_btInitKey, l_btInitKey.length, salt, salt.length, iterationCount);
            byte[] encContent = AESUtil.aesEncrypt(text.getBytes(Const.DEFAULT_CHARSET), secret);
            return Base64.encodeBase64String(encContent);
        } catch (Exception e) {
            LOG.error("Fail to encode text", e);
        }
        
        return null;
    }
    
    public static final String rootEncode(String text) {
        return rootEncode(text, SecurityConfig.getSalt(), SecurityConfig.getIterationCount());
    }
    
    /**
     * 加密数据，密码是dataSecret
     * @param text
     * @return
     */
    public static final String encode(String text) {
        try {
            byte[] encContent = AESUtil.aesEncrypt(text.getBytes(Const.DEFAULT_CHARSET), SecurityConfig.getDataSecret());
            return Base64.encodeBase64String(encContent);
        } catch (Exception e) {
            LOG.error("Fail to encode text", e);
        }
        
        return "";
    }
    
    /**
     * 加密数据，密码是configSecret
     * @param text
     * @return
     */
    public static final String configEncode(String text) {
        try {
            byte[] encContent = AESUtil.aesEncrypt(text.getBytes(Const.DEFAULT_CHARSET), SecurityConfig.getConfigSecret());
            return Base64.encodeBase64String(encContent);
        } catch (Exception e) {
            LOG.error("Fail to encode text", e);
        }
        
        return "";
    }
    
    /**
     * 用于解密用根密码加密的数据
     * @param text 加密后的密码
     * @param salt 盐值
     * @param iterationCount 迭代次数
     * @return 解密后的密码
     */
    public static final String rootDecode(String text, byte[] salt, int iterationCount) {
        try {
            byte[] encryptData = Base64.decodeBase64(text);
            byte[] secret = execute(SECRET_LEN, l_btInitKey, l_btInitKey.length, salt, salt.length, iterationCount);
            byte[] decryptData = AESUtil.aesDecrypt(encryptData, secret);
            return new String(decryptData, Const.DEFAULT_CHARSET);
        } catch (Exception e) {
            LOG.error("Fail to decode text", e);
        }
        return null;
    }
    
    public static final String rootDecode(String text) {
        return rootDecode(text, SecurityConfig.getSalt(), SecurityConfig.getIterationCount());
    }
    
    /**
     * 用于解密数据的解密函数，用的密码是dataSecret
     * @param text
     * @return
     */
    public static final String decode(String text) {
        try {
            byte[] encryptData = Base64.decodeBase64(text);
            byte[] decryptData = AESUtil.aesDecrypt(encryptData, SecurityConfig.getDataSecret());
            return new String(decryptData, Const.DEFAULT_CHARSET);
        } catch (Exception e) {
            LOG.error("Fail to decode text", e);
        }
        return "";
    }
    
    /**
     * 用于解密配置的解密函数，用的密码是configSecret
     * @param text
     * @return
     */
    public static final String configDecode(String text) {
        try {
            byte[] encryptData = Base64.decodeBase64(text);
            byte[] decryptData = AESUtil.aesDecrypt(encryptData, SecurityConfig.getConfigSecret());
            return new String(decryptData, Const.DEFAULT_CHARSET);
        } catch (Exception e) {
            LOG.error("Fail to decode text", e);
        }
        return "";
    }
    
    static byte[] l_btInitKey = {(byte)0xD9, (byte)0xA0, (byte)0xFC, (byte)0xBD, (byte)0x12, (byte)0xFF, (byte)0xCA, (byte)0x84, (byte)0xA6,
        (byte)0x94, (byte)0x0E, (byte)0x85, (byte)0xFE, (byte)0xB5, (byte)0x97, (byte)0x86, (byte)0x8C, (byte)0xE1, (byte)0xC1, (byte)0xCB,
        (byte)0xEF, (byte)0xA6, (byte)0x9E, (byte)0xF9, (byte)0xB9, (byte)0xCA, (byte)0x0B, (byte)0x13, (byte)0xF6, (byte)0xA5, (byte)0xEB,
        (byte)0x9D, (byte)0xF5, (byte)0xF1, (byte)0xFF, (byte)0xE1, (byte)0xFE, (byte)0x98, (byte)0x9F, (byte)0x02, (byte)0xAF, (byte)0x00,
        (byte)0xB0, (byte)0x05, (byte)0xAB, (byte)0x80, (byte)0x8A, (byte)0x88, (byte)0xDF, (byte)0xBC, (byte)0xFA, (byte)0xFD, (byte)0x95,
        (byte)0x0B, (byte)0x83, (byte)0xE7, (byte)0x94, (byte)0x80, (byte)0x98, (byte)0x8C, (byte)0x04, (byte)0x8F, (byte)0xD4, (byte)0x0B,
        (byte)0xE9, (byte)0xB0, (byte)0x94, (byte)0xA5, (byte)0x9C, (byte)0x9F, (byte)0xDB, (byte)0x84, (byte)0x1D, (byte)0x12, (byte)0xC9,
        (byte)0xB8, (byte)0xE5, (byte)0xF6, (byte)0x16, (byte)0x0D, (byte)0x17, (byte)0x85, (byte)0xFE, (byte)0x8C, (byte)0xD1, (byte)0x95,
        (byte)0xFE, (byte)0x81, (byte)0xC8, (byte)0xDA, (byte)0x9F, (byte)0x1D, (byte)0xCC, (byte)0x04, (byte)0xE6, (byte)0x18, (byte)0x16,
        (byte)0x98, (byte)0x1B, (byte)0xE5, (byte)0xA4, (byte)0x1B, (byte)0x82, (byte)0xEF, (byte)0xFA, (byte)0xC0, (byte)0x88, (byte)0x82,
        (byte)0x9F, (byte)0xAF, (byte)0xFE, (byte)0xDD, (byte)0xAF, (byte)0xF5, (byte)0x8A, (byte)0x0A, (byte)0x99, (byte)0xB8, (byte)0xDC,
        (byte)0xAB, (byte)0xFE, (byte)0xF9, (byte)0x0E, (byte)0x18, (byte)0xB1, (byte)0x9C, (byte)0x1E, (byte)0xCA, (byte)0xBD, (byte)0x1D,
        (byte)0xB6, (byte)0x96, (byte)0xD0, (byte)0x15, (byte)0xA1, (byte)0xD4, (byte)0xA1, (byte)0xF7, (byte)0x16, (byte)0xE2, (byte)0xA1,
        (byte)0xDD, (byte)0xCE, (byte)0xBB, (byte)0x81, (byte)0x02, (byte)0x10, (byte)0xC5, (byte)0xB7, (byte)0xE0, (byte)0xD3, (byte)0xE7,
        (byte)0xC5, (byte)0x1B, (byte)0xF1, (byte)0xDE, (byte)0xCA, (byte)0x8A, (byte)0xC1, (byte)0x95, (byte)0xC3, (byte)0x1D, (byte)0xEC,
        (byte)0x01, (byte)0xFB, (byte)0xAD, (byte)0x1D, (byte)0xBB, (byte)0xFB, (byte)0xF1, (byte)0x1B, (byte)0x18, (byte)0x96, (byte)0x9A,
        (byte)0xDA, (byte)0xB9, (byte)0x14, (byte)0xA1, (byte)0xA5, (byte)0x9E, (byte)0xBE, (byte)0x99, (byte)0xB2, (byte)0xBD, (byte)0xD9,
        (byte)0x13, (byte)0xF2, (byte)0x85, (byte)0x16, (byte)0xDB, (byte)0xE7, (byte)0xAF, (byte)0x06, (byte)0xF6, (byte)0xF3, (byte)0xE0,
        (byte)0x01, (byte)0xDE, (byte)0xC9, (byte)0x1D, (byte)0x89, (byte)0xD1, (byte)0xAA, (byte)0x14, (byte)0xD0, (byte)0xF9, (byte)0x8D,
        (byte)0x17, (byte)0xC4, (byte)0xD0, (byte)0x8B, (byte)0xE2, (byte)0x8C, (byte)0x0D, (byte)0xB4, (byte)0xCB, (byte)0xE2, (byte)0xB9,
        (byte)0x1B, (byte)0x01, (byte)0x9B, (byte)0xE1, (byte)0xB4, (byte)0x12, (byte)0xDE, (byte)0xB5, (byte)0xFE, (byte)0xF2, (byte)0xB8,
        (byte)0x83, (byte)0xE5, (byte)0xB9, (byte)0xF6, (byte)0x12, (byte)0x95, (byte)0x04, (byte)0xD8, (byte)0x04, (byte)0x85, (byte)0x03,
        (byte)0x87, (byte)0x03, (byte)0xF0, (byte)0xB9, (byte)0xAC, (byte)0xD8, (byte)0xA1, (byte)0x99, (byte)0xBE, (byte)0x8D, (byte)0xCF,
        (byte)0x06, (byte)0xC8, (byte)0x0A, (byte)0xE6, (byte)0xE3, (byte)0xC6, (byte)0x9B, (byte)0xEF, (byte)0x82, (byte)0x07, (byte)0x8C,
        (byte)0xA1, (byte)0x10, (byte)0xF3, (byte)0x15, (byte)0xB8, (byte)0xAA, (byte)0x01, (byte)0x96, (byte)0x8F, (byte)0xF8, (byte)0x0A,
        (byte)0xE4, (byte)0xB4, (byte)0x85, (byte)0x0C, (byte)0x86, (byte)0xF0, (byte)0xE9, (byte)0xD1, (byte)0xC5, (byte)0xE2, (byte)0xB9,
        (byte)0xFE, (byte)0x09, (byte)0x1B, (byte)0x99, (byte)0x96, (byte)0xAD, (byte)0x00, (byte)0x0D, (byte)0xAD, (byte)0x94, (byte)0x9B,
        (byte)0xF4, (byte)0x04, (byte)0x90, (byte)0x1B, (byte)0xB5, (byte)0x1D, (byte)0x12, (byte)0xFF, (byte)0xD9, (byte)0x08, (byte)0xFE,
        (byte)0xA6, (byte)0x03, (byte)0xDC, (byte)0xB6, (byte)0xAE, (byte)0xFC, (byte)0x8F, (byte)0xA4, (byte)0xB5, (byte)0xB8, (byte)0x90,
        (byte)0x9D, (byte)0xFD, (byte)0xF1, (byte)0xBA, (byte)0xCD, (byte)0x9B, (byte)0xDE, (byte)0x06, (byte)0xE6, (byte)0xF9, (byte)0xC8,
        (byte)0xB1, (byte)0x18, (byte)0xC6, (byte)0xD4, (byte)0xF1, (byte)0x17, (byte)0xCA, (byte)0xDD, (byte)0x1C, (byte)0xD0, (byte)0x85,
        (byte)0xE9, (byte)0xDB, (byte)0xDD, (byte)0xD6, (byte)0x83, (byte)0x88, (byte)0xEC, (byte)0x9D, (byte)0xFB, (byte)0x03, (byte)0xE5,
        (byte)0x15, (byte)0xCC, (byte)0xD3, (byte)0x92, (byte)0xE8, (byte)0x8D, (byte)0xCF, (byte)0xDD, (byte)0x91, (byte)0xCB, (byte)0xC9,
        (byte)0x13, (byte)0xD5, (byte)0xDF, (byte)0xE6, (byte)0xAC, (byte)0x9A, (byte)0x16, (byte)0xE0, (byte)0xC5, (byte)0x9A, (byte)0xC6,
        (byte)0x08, (byte)0x19, (byte)0xE0, (byte)0xB5, (byte)0xEE, (byte)0xDE, (byte)0xCF, (byte)0xB1, (byte)0xE6, (byte)0x93, (byte)0xA4,
        (byte)0x9A, (byte)0xD0, (byte)0xB3, (byte)0xFC, (byte)0x8D, (byte)0x80, (byte)0x18, (byte)0xD5, (byte)0xA1, (byte)0xED, (byte)0x08,
        (byte)0xC4, (byte)0xB3, (byte)0x86, (byte)0xEA, (byte)0x8E, (byte)0x8C, (byte)0x9D, (byte)0x8F, (byte)0xC9, (byte)0x14, (byte)0x9B,
        (byte)0x85, (byte)0x9B, (byte)0xED, (byte)0xD5, (byte)0xA6, (byte)0xB8, (byte)0xF4, (byte)0x89, (byte)0x82, (byte)0x1B, (byte)0xAE,
        (byte)0x0E, (byte)0x03, (byte)0x94, (byte)0xAA, (byte)0xEA, (byte)0x9B, (byte)0x92, (byte)0x02, (byte)0xB4, (byte)0xBF, (byte)0x1D,
        (byte)0xD4, (byte)0x81, (byte)0xAB, (byte)0x1E, (byte)0xA0, (byte)0x0E, (byte)0x8D, (byte)0xE6, (byte)0xA9, (byte)0xF0, (byte)0x13,
        (byte)0x9C, (byte)0xF8, (byte)0xB2, (byte)0xB4, (byte)0xA9, (byte)0xB6, (byte)0xDF, (byte)0x0F, (byte)0xAB, (byte)0x01, (byte)0xB5,
        (byte)0x8A, (byte)0xBF, (byte)0xA1, (byte)0xAF, (byte)0x07, (byte)0xAB, (byte)0x05, (byte)0xA9, (byte)0x0E, (byte)0x91, (byte)0xC3,
        (byte)0xF0, (byte)0x18, (byte)0xCE, (byte)0xA9, (byte)0xC2, (byte)0xE1, (byte)0xD4, (byte)0x86, (byte)0xA4, (byte)0x16, (byte)0x8F,
        (byte)0xF2, (byte)0x0C, (byte)0x8E, (byte)0xD1, (byte)0xF8, (byte)0xF0, (byte)0x10, (byte)0xDB, (byte)0x95, (byte)0xE1, (byte)0xBC,
        (byte)0xE6, (byte)0xD3, (byte)0xB1, (byte)0x10, (byte)0x99, (byte)0xCF, (byte)0xC4, (byte)0xD5, (byte)0xE6, (byte)0xEA, (byte)0x94,
        (byte)0x86, (byte)0xDD, (byte)0x12, (byte)0xA0, (byte)0xAE, (byte)0xCB, (byte)0x82, (byte)0xF4, (byte)0x9C, (byte)0xEF, (byte)0xAA,
        (byte)0xAD, (byte)0x0D, (byte)0xC7, (byte)0x01, (byte)0xDC, (byte)0xE7, (byte)0x93, (byte)0xB2};
    
    public static void setInitKey(int i, byte v) {
        l_btInitKey[i % 512] = v;
    }
    
    public static boolean setInitKey(byte[] v) {
        int len;
        
        if(v == null || (len = v.length) != 512) {
            return false;
        }
        
        for (int i = 0; i < len; i++) {
            l_btInitKey[i] = v[i];
        }
        
        return true;
    }
    
    public static byte[] execute(int iKeyLen, /* 需要导出的密钥的长度 */
        byte[] btInitKey, /* 初始密钥 */
        int iInitKeyLen, /* 初始密钥的长度 */
        byte[] btSalt, /* 盐值（属于干扰信息） */
        int iSaltLen, /* 盐值的长度 */
        int iIterationCount /* 重复次数 */
    ) throws  InvalidKeyException, NoSuchAlgorithmException {
        int ii;
        int jj;
        int kk;
        int mm;
        int l_iLoop;
        int l_iRemain;
        int l_iCount;
        byte[] l_btRst = null;
        byte[] l_btTmp0 = null;
        byte[] l_btTmp1 = null;
        Mac l_oMac = null;
        SecretKeySpec l_oKey = null;
        
        if((iKeyLen <= 0) || (btInitKey == null) || (btSalt == null)) {
            return null;
        }
        
        if((iInitKeyLen <= 0) || (iInitKeyLen > btInitKey.length)) {
            iInitKeyLen = btInitKey.length;
        }
        
        if((iSaltLen <= 0) || (iSaltLen > btSalt.length)) {
            iSaltLen = btSalt.length;
        }
        
        if(iIterationCount <= 0) {
            iIterationCount = 2048;
        }
        
        l_btRst = new byte[iKeyLen];
        
        l_iLoop = iKeyLen / 20;
        
        l_iRemain = iKeyLen % 20;
        
        if(l_iRemain != 0) {
            l_iLoop++;
        } else {
            l_iRemain = 20;
        }
        
        mm = 0;
        
        l_oKey = new SecretKeySpec(btInitKey, 0, iInitKeyLen, "HmacSHA1");
        
        l_oMac = Mac.getInstance("HmacSHA1");
        
        l_oMac.init(l_oKey);
        
        for (ii = 1; ii <= l_iLoop; ii++) {
            l_oMac.update(btSalt, 0, iSaltLen);
            
            l_btTmp0 = l_oMac.doFinal(IntToByteArray(ii));
            
            l_btTmp1 = l_btTmp0;
            
            for (jj = 1; jj < iIterationCount; jj++) {
                l_btTmp1 = l_oMac.doFinal(l_btTmp1);
                
                for (kk = 0; kk < 20; kk++) {
                    l_btTmp0[kk] ^= l_btTmp1[kk];
                }
            }
            
            if(ii < l_iLoop) {
                l_iCount = 20;
            } else {
                l_iCount = l_iRemain;
            }
            
            for (jj = 0; jj < l_iCount; jj++, mm++) {
                l_btRst[mm] = l_btTmp0[jj];
            }
            
            l_oMac.reset();
        }
        
        l_btTmp0 = null;
        l_btTmp1 = null;
        l_oMac = null;
        l_oKey = null;
        
        return l_btRst;
    }
    
    private static byte[] IntToByteArray(int iValye) {
        byte[] l_btByteArray = new byte[4];
        
        l_btByteArray[0] = (byte)((iValye >> 24) & 0xFF);
        l_btByteArray[1] = (byte)((iValye >> 16) & 0xFF);
        l_btByteArray[2] = (byte)((iValye >> 8) & 0xFF);
        l_btByteArray[3] = (byte)(iValye & 0xFF);
        
        return l_btByteArray;
    }
}
