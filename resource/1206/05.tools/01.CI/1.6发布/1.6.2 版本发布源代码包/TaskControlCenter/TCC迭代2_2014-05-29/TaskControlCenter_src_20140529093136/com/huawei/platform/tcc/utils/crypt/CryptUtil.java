/*
 * 文 件 名:  CryptUtil.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  加密解密工具类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.platform.tcc.utils.crypt;

import it.sauronsoftware.base64.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.huawei.internet.platform.corp.idm.utils.crypt.thirdparty.AES128_ECB_HEX_GLY;

/**
 *
 * 加密解密工具
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public final class CryptUtil
{
    /**
     * 位应算量
     */
    private static final int HEX_0X_FF = 0xFF;
    
    /** 构造函数
     */
    private CryptUtil()
    {
    }
    
    /**
     * 执行AES128加密操作，空字符串表示加密异常
     * @param content 需要加密的内容
     * @param key 加密密钥
     * @return String  加密后的字符串
     */
    public static String encryptToAESStr(final String content, final String key)
    {
        final byte[] contentBytes = content.getBytes();
        
        final byte[] keyBytes = key.getBytes();
        
        String encodStr = null;
        
        try
        {
            encodStr = AES128_ECB_HEX_GLY.encode(contentBytes, 0, keyBytes, 0);
        }
        catch (Exception e)
        {
            encodStr = "";
        }
        
        return encodStr;
    }
    
    /**
     * 执行AES128解密操作，空字符串表示加密异常
     * @param content 待解密内容(字符串)
     * @param keyWord 解密密钥
     * @return String 解密后的字符串
     */
    public static String decryptForAESStr(final String content, final String keyWord)
    {
        String decodeStr = null;
        
        boolean successed = true;
        try
        {
            byte[] resultBytes = AES128_ECB_HEX_GLY.decode(content, keyWord.getBytes(), 0);
            decodeStr = new String(resultBytes);
        }
        catch (Exception e)
        {
            successed = false;
        }
        
        return successed ? decodeStr : "";
    }
    
    /**
     * 
     * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用SHA-256
     * @param strSrc 要加密的字符串
     * @param encName 加密类型
     * @return String 加密后的字符串
     * @throws NoSuchAlgorithmException 异常
     */
    public static String encryptToSHA(final String strSrc, final String encName)
        throws NoSuchAlgorithmException
    {
        MessageDigest messageDigest = null;
        String strDes = null;
        
        final byte[] srcBt = strSrc.getBytes();
        //加密算法名
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
    
    //将字节数组转换为16进制字符串
    private static String bytes2Hex(final byte[] bts)
    {
        final StringBuffer des = new StringBuffer();
        String tmp;
        for (int i = 0; i < bts.length; i++)
        {
            tmp = Integer.toHexString(bts[i] & HEX_0X_FF);
            if (tmp.length() == 1)
            {
                //TODO 是否应该加在前面
                des.append('0');
            }
            des.append(tmp);
        }
        return des.toString();
    }
    
    /**
     * 执行Base64编码
     * @param src 待编码字符串
     * @return 编码后的字符串
     */
    public static String encodeToBase64(final String src)
    {
        return Base64.encode(src);
    }
    
    /**
     * 执行Base64解码
     * @param decode 待解码字符串
     * @return 解码后的字符串
     */
    public static String decodeForBase64(final String decode)
    {
        return Base64.decode(decode);
    }
    
    /**  
     * 对一段String生成MD5加密信息  
     *   
     * @param message 要加密的String  
     * @return 生成的MD5信息  
     * @throws NoSuchAlgorithmException 异常
     * @throws UnsupportedEncodingException 异常
     */
    public static String getMD5HexString(final String message)
        throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        final byte[] digestBt = messageDigest.digest(message.getBytes("utf-8"));
        // byteToHexString(b);   
        return byteToHexStringSingle(digestBt);
    }
    
    /**  
     * 独立把byte[]数组转换成十六进制字符串表示形式  
     *   
     * @param byteArray  
     * @return  十六进制字符串表示形式 
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
    
    //    /**
    //     * 手动加密/解密使用
    //     * @param args
    //     * @see [类、类#方法、类#成员]
    //     */
    //    public static void main(String[] args)
    //    {
    //        String content = "验证加密";
    //        
    //        String aes128Key = "PkmJygVfrDxsDeeD";
    //        
    //        String result = encryptToAESStr(content, aes128Key);
    //        
    //        System.out.println("AES128 Encrypt result is " + result);
    //        
    //        System.out.println("AES128 Dencrypt result is "
    //                + decryptForAESStr(result, aes128Key));
    //        
    //        System.out.println("SHA256 encode result of " + "验证加密" + " is "
    //                + encryptToSHA("验证加密", null));
    //        
    //        String base64Result = encodeToBase64("验证加密");
    //        
    //        System.out.println("Base64 encode result of " + "验证加密" + " is "
    //                + base64Result);
    //        
    //        System.out.println("Base64 decode result of " + "验证加密" + " is "
    //                + decodeForBase64(base64Result));
    //    }
}
