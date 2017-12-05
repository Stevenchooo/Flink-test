/**
 * @(#)java 1.0 2012-10-30
 * @Copyright:  Copyright 2002 - 2012 Huawei Tech. Co. Ltd. All Rights Reserved.
 * @Description: 
 * 
 * Modification History:
 * Date:        2012-10-30
 * Author:      f00224216
 * Version:     1.0
 * Description: AES加解密工具类
 * Reviewer:    
 * Review Date: 
 */

package com.huawei.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.util.Utils;
import com.huawei.waf.protocol.Const;

public class AESUtil {
    /**
     * 加密算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 加密工作模式
     */
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final String ECB_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static final int IV_LEN = 16;
    
    /**
     * 密钥长度
     */
    public static final int AES_KEY_LEN = 16;
    public static final int AES_KEY_BIT_LEN = AES_KEY_LEN * 8; //bit

    private static Logger LOG = LoggerFactory.getLogger(AESUtil.class);

    /**
     * 用AES对字符串进行加密
     * @param plainStr 明文字符串
     * @param strKey 加密密钥
     * @param ivKey 初始向量，可增加加密算法的强度
     * @return 密文
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidAlgorithmParameterException 
     * @throws InvalidKeyException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     */
    public static byte[] aesEncrypt(byte[] plainStr, byte[] strKey, byte[] ivKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = Arrays.copyOf(strKey, AES_KEY_LEN);
        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, KEY_ALGORITHM);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        byte[] ivBytes = Arrays.copyOf(ivKey, AES_KEY_LEN);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        return cipher.doFinal(plainStr);
    }
    
    /**
     * cbc加密，使用随机iv，并将iv记录在加密内容的前面16字节
     * @param plainStr
     * @param strKey
     * @return
     */
    public static final byte[] aesEncrypt(byte[] plainStr, byte[] strKey) {
        byte[] iv = Utils.genUUID();
        
        try {
            byte[] encContent = AESUtil.aesEncrypt(plainStr, strKey, iv);
            byte[] bytes = new byte[iv.length + encContent.length];
            System.arraycopy(iv, 0, bytes, 0, IV_LEN);
            System.arraycopy(encContent, 0, bytes, IV_LEN, encContent.length);
            return bytes;
        } catch (Exception e) {
            LOG.error("Fail to encode text", e);
        }
        
        return null;
    }
    
    /**
     * 用AES对字符串进行加密
     * @param plainStr 明文字符串
     * @param strKey 加密密钥，必须是十六进制的字符
     * @param ivKey 初始向量，必须是十六进制的字符
     * @return 加密字符串 HEX
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws InvalidAlgorithmParameterException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public static String aesEncrypt(String plainStr, String strKey, String ivKey)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        byte[] encStr = aesEncrypt(plainStr.getBytes(Const.DEFAULT_CHARSET), Utils.hex2bin(strKey), Utils.hex2bin(ivKey));
        return Utils.bin2hex(encStr);
    }
    
    /**
     * 用AES对字符串进行解密
     * 
     * @param encryptedStr 加密过的字符串 
     * @param keyBytes 二进制方式的密码
     * @param ivKey 附加的密码，比如用户的临时密钥
     * @return 解密后的字符串
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidAlgorithmParameterException 
     * @throws InvalidKeyException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     */
    public static byte[] aesDecrypt(byte[] encryptedStr, byte[] strKey, byte[] ivKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = Arrays.copyOf(strKey, AES_KEY_LEN);
        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, KEY_ALGORITHM);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        byte[] ivBytes = Arrays.copyOf(ivKey, AES_KEY_LEN);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        return cipher.doFinal(encryptedStr);
    }
    
    /**
     * cbc解密，前16字节为iv
     * @param encryptData
     * @param strKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] aesDecrypt(byte[] encryptData, byte[] strKey) {
        try {
            byte[] iv = new byte[IV_LEN];
            byte[] encContent = new byte[encryptData.length - IV_LEN];
            System.arraycopy(encryptData, 0, iv, 0, IV_LEN);
            System.arraycopy(encryptData, IV_LEN, encContent, 0, encContent.length);
            return aesDecrypt(encContent, strKey, iv);
        } catch(Exception e) {
            LOG.error("Fail to decode data", e);
        }
        return null;
    }
    
    /**
     * 解密
     * @param encryptedStr 十六进制方式的密文
     * @param strKey 十六进制方式的密码
     * @param ivKey 十六进制方式的附加密码
     * @return
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws InvalidAlgorithmParameterException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public static String aesDecrypt(String encryptedStr, String strKey, String ivKey)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return new String(aesDecrypt(Utils.hex2bin(encryptedStr), Utils.hex2bin(strKey), Utils.hex2bin(ivKey)), Const.DEFAULT_CHARSET);
    }

    /**
     * 用AES对字符串进行加密
     * 
     * @param plainStr 明文字符串
     * @param strKey 加密密钥
     * @return 加密字符串
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     */
    public static byte[] ecbEncrypt(byte[] plainStr, byte[] strKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = Arrays.copyOf(strKey, AES_KEY_LEN); //不齐或截断成16字节

        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, KEY_ALGORITHM);

        Cipher cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(plainStr);
    }
    
    /**
     * 用AES对字符串进行加密
     * @param plainStr 明文字符串
     * @param strKey 加密密钥，必须是转换成十六进制的字符
     * @return 加密字符串
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public static String ecbEncrypt(String plainStr, String strKey)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return Utils.bin2hex(ecbEncrypt(plainStr.getBytes(Const.DEFAULT_CHARSET), Utils.hex2bin(strKey)));
    }

    public static final byte[] ecbDecrypt(byte[] encryptedStr, byte[] strKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = Arrays.copyOf(strKey, AES_KEY_LEN);

        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, KEY_ALGORITHM);

        Cipher cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM);

        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(encryptedStr);
    }

    /**
     * 用AES对字符串用ECB模式解密
     * 
     * @param encryptedStr 加密过的字符串，十六进制的字符
     * @param strKey 解密密钥，必须是转换成十六进制的字符
     * @return 解密后的字符串
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public static String ecbDecrypt(String encryptedStr, String strKey)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return new String(ecbDecrypt(Utils.hex2bin(encryptedStr), Utils.hex2bin(strKey)), Const.DEFAULT_CHARSET);
    }


    /**
     * 用默认密钥进行解密
     * 
     * @param encryptedStr HEX方式的密文
     * @param strKey
     * @return
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public static String ecbDecrypt(String encryptedStr)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return new String(ecbDecrypt(Utils.hex2bin(encryptedStr), new byte[AES_KEY_LEN]), Const.DEFAULT_CHARSET);
    }
    
    public static String ecbEncrypt(String plainStr)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return Utils.bin2hex(ecbEncrypt(plainStr.getBytes(Const.DEFAULT_CHARSET), new byte[AES_KEY_LEN]));
    }

    /**
     * 获取加密用的raw key
     * 
     * @return 十六进制raw key
     */
    public static byte[] getRawKey() {
        try {
            // Get the KeyGenerator
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
            kgen.init(AES_KEY_BIT_LEN); // 192和256位的需要其他支持

            // Generate the secret key specs.
            SecretKey skey = kgen.generateKey();
            return skey.getEncoded();
        } catch (Exception e) {
            LOG.error("Fail to get raw key", e);
        }
        
        byte[] defKey = new byte[AES_KEY_LEN];
        for(int i = 0; i < AES_KEY_LEN; i++) {
            defKey[i] = (byte)0;
        }
        return defKey;
    }
//    public static void main(String[] args) throws Exception {
//        // 随机密钥
//        String aesKey = AESUtil.getRawKey();
//        String secretKey = AESUtil.aesEncrypt(aesKey, "test".getBytes(), "test1".getBytes());
//        String aesKey1 = AESUtil.aesDecrypt(secretKey, "test".getBytes(), "test1".getBytes());
//        
//        System.out.println("aesKey = " + aesKey + " secretKey=" + secretKey + " aesKey1=" + aesKey1);
//        System.exit(0);
//    }
    /**
     * 50 threads, each run 10000 times, about 33000 tps
     * PC: 4 CORES, 3G frequence
     */
//    public static void main(String[] args) throws Exception
//    {
//      int threadNum = 50;
//      final int runTimes = 10000;
//      final java.util.concurrent.CountDownLatch counter = new java.util.concurrent.CountDownLatch(threadNum * runTimes);
//      java.util.concurrent.ExecutorService THREAD_POOL = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
//
//      
//      long start = System.currentTimeMillis();
////      for(int i = 0; i < times; i++) {
////          client.get(task);
////      }
//      for(int i = 0; i < threadNum; i++) {
//        LOG.debug("thread {} start", i);
//        THREAD_POOL.execute(new Runnable(){
//            @Override
//            public void run() {
//                try {
//                    for(int i = 0; i < runTimes; i++) {
//                        // 随机密钥
//                        String aesKey = AESUtil.getRawKey();
//                        if (aesKey == null) {
//                            throw new WAFException("aesKey is null");
//                        }
//
//                        // 对随机密钥进行加密以便安全存储
//                        String secretKey = AESUtil.aesEncrypt(aesKey, "dddd", "dddd");
//                        if (Utils.isStrEmpty(secretKey)) {// 加密失败
//                            throw new WAFException("secretKey is null");
//                        }
//                        String sha = Utils.bin2base64(Utils.sha256("dddd"));
//                        //System.out.println(sha);
//                        String encryptedDeviceId = aesEncrypt("dddddd", aesKey, "dddd");
//                        String src = aesDecrypt(encryptedDeviceId, aesKey, "dddd");
//                        //System.out.println("enc=" + encryptedDeviceId + ", src=" + src);
//                        counter.countDown();
//                    }
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        });
//        LOG.debug("thread {} end", i);
//      }
//      
//      counter.await();
//
//      long interval = System.currentTimeMillis() - start;
//      System.out.println("speed:" + (1000L * threadNum * runTimes) / interval);
//      System.exit(0);
//  }
//    public static void main(String[] args) throws Exception {
//        final byte[] content = "test33333333333333333333333331".getBytes(Const.DEFAULT_CHARSET);
//        final byte[] key = "test2".getBytes(Const.DEFAULT_CHARSET);
//        final byte[] iv = "test3".getBytes(Const.DEFAULT_CHARSET);
//        com.huawei.tool.PerformanceTest.test(new com.huawei.tool.TestTask("test", 8, 40000, null){
//            @Override
//            public void test(Object runtimeData) {
//                byte[] enc;
//                try {
//                    //enc = aesEncrypt(content, key, iv);
//                    enc = ecbEncrypt(content, key);
//                    //System.out.println("enc:" + Utils.bin2hex(enc) + ",len:" + enc.length);
//                    //byte[] dec = aesDecrypt(enc, key, iv);
//                    byte[] dec = ecbDecrypt(enc, key);
//                    //System.out.println("dec:" + new String(dec, Const.DEFAULT_CHARSET));
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        });
//        System.exit(0);
//    }
}