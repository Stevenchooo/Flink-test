/*
 * 文 件 名:  CryptUtil.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  加密解密工具类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.ide.portal;

import it.sauronsoftware.base64.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.ide.interceptors.res.rcm.CryptException;

/**
 *
 * 加密解密工具
 * 
 * @author z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public final class CryptUtil
{
    /**
     * 位应算量
     */
    private static final int HEX_0X_FF = 0xFF;
    
    /**
     * 日志接口类
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CryptUtil.class);
    
    /**
     * 构造函数
     */
    private CryptUtil()
    {
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
            contentBytes = content.getBytes(CommonUtils.DEFUALT_ENCODING);
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.debug("", e);
            return "";
        }
        
        byte[] keyBytes;
        try
        {
            keyBytes = key.getBytes(CommonUtils.DEFUALT_ENCODING);
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
            encodStr = new String(Base64.encode(aes), CommonUtils.DEFUALT_ENCODING);
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
        catch (Exception e)
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
            resultBytes =
                AES128CBC.decode(Base64.decode(content.getBytes(CommonUtils.DEFUALT_ENCODING)),
                    0,
                    keyWord.getBytes(CommonUtils.DEFUALT_ENCODING),
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
        catch (RuntimeException e)
        {
            LOGGER.debug("", e);
            return "";
        }
        catch (Exception e)
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
            decodeStr = new String(resultBytes, CommonUtils.DEFUALT_ENCODING);
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.debug("", e);
            return "";
        }
        
        return decodeStr;
    }
    
    /**
     * 
     * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用SHA-256
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
     */
    public static String encryptToSHA(final String strSrc, final String encName)
        throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest messageDigest = null;
        String strDes = null;
        
        final byte[] srcBt = strSrc.getBytes(CommonUtils.DEFUALT_ENCODING);
        messageDigest = MessageDigest.getInstance(encName);
        messageDigest.update(srcBt);
        // to HexString
        strDes = bytes2Hex(messageDigest.digest());
        return strDes;
    }
    
    /**
     * 
     * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用SHA-256
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
     */
    public static String encryptToSHABase64(final String strSrc, final String encName)
        throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest messageDigest = null;
        String strDes = null;
        
        final byte[] srcBt = strSrc.getBytes(CommonUtils.DEFUALT_ENCODING);
        messageDigest = MessageDigest.getInstance(encName);
        messageDigest.update(srcBt);
        // to HexString
        strDes = new String(Base64.encode(messageDigest.digest()), CommonUtils.DEFUALT_ENCODING);
        return strDes;
    }
    
    // 将字节数组转换为16进制字符串
    private static String bytes2Hex(final byte[] bts)
    {
        final StringBuffer des = new StringBuffer();
        String tmp;
        for (int i = 0; i < bts.length; i++)
        {
            tmp = Integer.toHexString(bts[i] & HEX_0X_FF);
            if (tmp.length() == 1)
            {
                // TODO 是否应该加在前面
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
     */
    public static String encodeToBase64(final String src)
    {
        return Base64.encode(src);
    }
    
    /**
     * 执行Base64解码
     * 
     * @param decode
     *            待解码字符串
     * @return 解码后的字符串
     */
    public static String decodeForBase64(final String decode)
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
    public static String byteToHexStringSingle(final byte[] byteArray)
    {
        final StringBuffer md5StrBuff = new StringBuffer();
        
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
