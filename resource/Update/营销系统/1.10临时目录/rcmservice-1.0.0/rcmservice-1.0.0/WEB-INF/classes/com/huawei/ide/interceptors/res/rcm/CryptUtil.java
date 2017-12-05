/*
 * 文 件 名:  CryptUtil.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2011-11-1
 */
package com.huawei.ide.interceptors.res.rcm;

import it.sauronsoftware.base64.Base64;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author w00190105
 * @version [Internet Business Service Platform SP V100R100, 2011-11-1]
 * @see [相关类/方法]
 */
public final class CryptUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CryptUtil.class);
    
    /**
     * 位应算量
     */
    private static final int HEX_0X_FF = 0xFF;
    
    private static final String DEFUALT_ENCODING = "utf-8";
    
    /**
     * 构造函数
     */
    private CryptUtil()
    {
    }
    
    /**
     * <一句话功能简述> <功能详细描述>
     * 
     * @param encryptText
     *            encryptText
     * @param encryptKey
     *            encryptKey
     * @param macName
     *         macName       
     * @return String
     * @throws UnsupportedEncodingException
     *             UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     *             NoSuchAlgorithmException
     * @throws InvalidKeyException
     *             InvalidKeyException
     * @see [类、类#方法、类#成员]
     */
    public static String hmacSHAEncrypt(String encryptText, String encryptKey, String macName)
        throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException
    {
        
        byte[] data = encryptKey.getBytes(DEFUALT_ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, macName);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(macName);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);
        
        byte[] text = encryptText.getBytes(DEFUALT_ENCODING);
        // 完成 Mac 操作
        byte[] finalText = mac.doFinal(text);
        
        byte[] encryptedBytes = Base64.encode(finalText);
        
        return new String(encryptedBytes, DEFUALT_ENCODING);
        
    }
    
    /**
     * 执行AES128 CBC 加密操作
     * 
     * @param content
     *            需要加密的内容
     * @param key
     *            加密密钥
     * @return String 加密后的字符串
     */
    public static String encryptToAESCBCStr(final String content, final String key)
    {
        if (null == content || "".equals(content) || null == key || "".equals(key))
        {
            LOGGER.warn("content or key is null or empty string");
            return "";
        }
        
        byte[] contentBytes;
        try
        {
            contentBytes = content.getBytes(DEFUALT_ENCODING);
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.debug("", e);
            return "";
        }
        
        byte[] keyBytes;
        try
        {
            keyBytes = key.getBytes(DEFUALT_ENCODING);
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.debug("", e);
            return "";
        }
        
        String encodStr = "";
        try
        {
            byte[] aes = AES128CBC.encode(contentBytes, 0, keyBytes, 0);
            if (null == aes)
            {
                LOGGER.warn("AES_CBC_128 encode return null");
                return "";
            }
            encodStr = new String(Base64.encode(aes), DEFUALT_ENCODING);
        }
        catch (CryptException e)
        {
            LOGGER.debug("", e);
            return "";
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.debug("", e);
            return "";
        }
        return encodStr;
    }
    
    /**
     * 执行AES128 CBC解密操作
     * 
     * @param content
     *            待解密内容(字符串)
     * @param keyWord
     *            解密密钥
     * @return String 解密后的字符串
     */
    public static String decryptForAESCBCStr(final String content, final String keyWord)
    {
        if (null == content || "".equals(content) || null == keyWord || "".equals(keyWord))
        {
            LOGGER.warn("content or keyWord is null or empty string");
            return "";
        }
        
        String decodeStr = null;
        
        byte[] resultBytes = null;
        
        try
        {
            resultBytes = AES128CBC.decode(Base64.decode(content.getBytes(DEFUALT_ENCODING)),
                0,
                keyWord.getBytes(DEFUALT_ENCODING),
                0);
        }
        catch (CryptException e)
        {
            LOGGER.debug("", e);
            return "";
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.debug("", e);
            return "";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.debug("", e);
            return "";
        }
        if (null == resultBytes)
        {
            return "";
        }
        try
        {
            decodeStr = new String(resultBytes, DEFUALT_ENCODING);
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.debug("", e);
            return "";
        }
        
        return decodeStr;
    }
    
    /**
     * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用SHA-256 <功能详细描述>
     * 
     * @param strSrc
     *            要加密的字符串
     * @param encName
     *            加密类型
     * @return String 加密后的字符串
     * @throws NoSuchAlgorithmException
     *             NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     *             UnsupportedEncodingException
     * @see [类、类#方法、类#成员]
     */
    public static String encryptToSHA(final String strSrc, final String encName)
        throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest messageDigest = null;
        String strDes = null;
        
        final byte[] srcBt = strSrc.getBytes(DEFUALT_ENCODING);
        // 加密算法名
        String encryptName;
        if (encName == null || "".equals(encName))
        {
            encryptName = "SHA-256";
        }
        else
        {
            encryptName = encName;
        }
        messageDigest = MessageDigest.getInstance(encryptName);
        messageDigest.update(srcBt);
        // to HexString
        strDes = bytes2Hex(messageDigest.digest());
        
        return strDes;
    }
    
    /**
     * <byte转hex>
     * 
     * @param bts
     *            byte数组
     * @return 字符串
     * @see [类、类#方法、类#成员]
     */
    private static String bytes2Hex(byte[] bts)
    {
        StringBuffer des = new StringBuffer();
        String tmp = null;
        for (int i = 0; i < bts.length; i++)
        {
            tmp = Integer.toHexString(bts[i] & HEX_0X_FF);
            if (tmp.length() == 1)
            {
                des.append('0');
            }
            des.append(tmp);
        }
        return des.toString();
    }
    
    /**
     * 执行Base64编码
     * 
     * @param src
     *            待编码字符串
     * @return 编码后的字符串
     * @see [类、类#方法、类#成员]
     */
    public static String encodeToBase64(String src)
    {
        return Base64.encode(src);
    }
    
    /**
     * 执行Base64解码
     * 
     * @param decode
     *            待解码字符串
     * @return 解码后的字符串
     * @see [类、类#方法、类#成员]
     */
    public static String decodeForBase64(String decode)
    {
        return Base64.decode(decode);
    }
    
    /**
     * 独立把byte[]数组转换成十六进制字符串表示形式
     * 
     * @param byteArray
     *            byteArray
     * @return 十六进制字符串表示形式
     */
    public static String byteToHexStringSingle(byte[] byteArray)
    {
        StringBuffer md5StrBuff = new StringBuffer();
        
        for (int i = 0; i < byteArray.length; i++)
        {
            if (Integer.toHexString(HEX_0X_FF & byteArray[i]).length() == 1)
            {
                md5StrBuff.append('0').append(Integer.toHexString(HEX_0X_FF & byteArray[i]));
            }
            else
            {
                md5StrBuff.append(Integer.toHexString(HEX_0X_FF & byteArray[i]));
            }
        }
        
        return md5StrBuff.toString();
    }
    
}
