package com.huawei.util;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;

import com.huawei.waf.protocol.Const;

/**
 * 利用RSA签名的算法工具类
 * 
 * @author l00152046
 */
public class RSAUtil {
    private static final Logger LOG = LogUtil.getInstance();


    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 生成密钥对(公钥和私钥)
     * @return
     * @throws Exception
     */
    public static PVKey genKeyPair() throws Exception {
        return genKeyPair(true);
    }

    public static PVKey genKeyPair(boolean isBase64) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        if (isBase64) {
            return new PVKey(toBase64String(publicKey), toBase64String(privateKey));
        } else {
            return new PVKey(toHexString(publicKey), toHexString(privateKey));
        }
    }

    public static String toBase64String(Key key) {
        return Base64.encodeBase64String(key.getEncoded());
    }

    public static String toHexString(Key key) {
        return new String(Hex.encodeHex(key.getEncoded(), false));
    }

    private static Key toPublicKey(String sKey) throws Exception {
        return toKey(Hex.decodeHex(sKey.toCharArray()), true);
    }

    private static Key toPrivateKey(String sKey) throws Exception {
        return toKey(Hex.decodeHex(sKey.toCharArray()), false);
    }

    private static Key toKey(byte[] keyBytes, boolean isPublic) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        if (isPublic) {
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            return keyFactory.generatePublic(x509KeySpec);
        } else {
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            return keyFactory.generatePrivate(pkcs8KeySpec);
        }
    }

    /**
     * 用私钥对信息生成数字签名
     * @param data 待签名的数据
     * @param privateKey 私钥
     * @return
     */
    public static String sign(byte[] data, byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }

    public static String sign(byte[] data, String privateKey) throws Exception {
        return sign(data, Base64.decodeBase64(privateKey));
    }

    // =====签名与验证签名==========================================================================
    /**
     * 签名
     * 
     * @param content
     * @param privateKey
     * @return
     * @throws Exception 
     */
    public static String sign(String content, byte[] privateKey) throws Exception {
        return sign(content.getBytes(Const.DEFAULT_CHARSET), privateKey);
    }

    public static String sign(String content, String privateKey) throws Exception {
        return sign(content.getBytes(Const.DEFAULT_CHARSET), Base64.decodeBase64(privateKey));
    }

    /**
     * 计算签名
     * 
     * @param parameters
     * @param paramsList
     * @param privateKey
     * @return
     */
    public static final String sign(Map<String, Object> parameters, List<String> paramsList, byte[] privateKey) {
        String signData = Utils.getSignData(parameters, paramsList, '&', '=');
        String sign = "";
        
        try {
            sign = sign(signData.getBytes(Const.DEFAULT_CHARSET), privateKey);
            if (LOG.isDebugEnabled()) {
                LOG.debug("calculateSign,signData:{}, sign:{}", signData, sign);
            }
        } catch (Exception e) {
            LOG.error("Fail to sign {}", signData, e);
        }
        
        return sign;
    }
    
    /**
     * 校验数字签名
     * 
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     * 
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, byte[] publicKey, byte[] sign) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(sign);
    }
    
    /**
     * @param data 源数据
     * @param publicKey base64后的公钥
     * @param sign base64之后的签名
     * @return
     * @throws Exception
     */
    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        return verify(data.getBytes(Const.DEFAULT_CHARSET), Base64.decodeBase64(publicKey), Base64.decodeBase64(sign));
    }

    /**
     * 检查校验字段
     * 
     * @param parameters 参数全集
     * @param paramsList 需要参与校验的参数名称
     * @param sign 签名(Base64)
     * @param publicKey 公钥
     * @return
     */
    public static final boolean verify(Map<String, Object> parameters,
            List<String> paramsList, String sign, byte[] publicKey) {
        String signData = Utils.getSignData(parameters, paramsList, '&', '=');
        boolean checkResult;
        try {
            checkResult = verify(signData.getBytes(Const.DEFAULT_CHARSET), publicKey, Base64.decodeBase64(sign));
        } catch (Exception e) {
            LOG.error("Fail to verify {}", sign, e);
            return false;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("checkSign,signData:{}, sign:{}, checkResult:{}", signData, sign, checkResult);
        }

        return checkResult;
    }

    
    public static byte[] decryptByPublicKey(byte[] encryptedData, String sKey) throws Exception {
        Key key = toPublicKey(sKey);
        return doFinal(key, encryptedData, Cipher.DECRYPT_MODE, MAX_DECRYPT_BLOCK, null);
    }

    public static byte[] encryptByPrivateKey(byte[] data, String sKey) throws Exception {
        Key key = toPrivateKey(sKey);
        return doFinal(key, data, Cipher.ENCRYPT_MODE, MAX_ENCRYPT_BLOCK, null);
    }

    /**
     * 对数据执行解密
     * 
     * @param key
     * @param data
     * @param mode
     * @param maxBlock 每次解密处理的最大数据块
     * @param provider
     * @return
     * @throws Exception
     */
    private static byte[] doFinal(Key key, byte[] data, int mode, int maxBlock, Provider provider) throws Exception {
        Cipher cipher = null;
        if (provider != null) {
            cipher = Cipher.getInstance(KEY_ALGORITHM, provider);
        } else {
            cipher = Cipher.getInstance(KEY_ALGORITHM);
        }
        cipher.init(mode, key);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;

        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxBlock) {
                cache = cipher.doFinal(data, offSet, maxBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            offSet += maxBlock;
        }

        byte[] decryptedData = out.toByteArray();
        out.close();

        return decryptedData;
    }

    /**
     * 
     * 公私钥键值对对象
     * 
     * @version [版本号, 2014-1-27]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static class PVKey {
        private String publicKey;
        private String privateKey;

        public String getPublicKey() {
            return publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public PVKey(String publicKey, String privateKey) {
            super();
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        @Override
        public String toString() {
            return "PVKey [publicKey=" + publicKey + ", privateKey=" + privateKey + "]";
        }
    }

    /**
     * RSA:8 threads, 680tps, MD5:8 threads, more than 1000000 tps
     * 
     * @param args
     * @throws Exception
     */
    // public static void main(String[] args) throws Exception {
    // final int N = 1000;
    // int threadNum = Runtime.getRuntime().availableProcessors() * 2;
    // final java.util.concurrent.CountDownLatch counter = new
    // java.util.concurrent.CountDownLatch(threadNum);
    // java.util.concurrent.ExecutorService exectors =
    // java.util.concurrent.Executors.newFixedThreadPool(threadNum);
    //
    // long t1 = System.currentTimeMillis();
    // for(int j = 0; j < threadNum; j++) {
    // exectors.execute(new Runnable(){
    // public void run() {
    // System.out.println("start");
    // try {
    // for(int i = 0; i < N; i++) {
    // // String s1 = Utils.md5("sss" + "test");
    // // String s = Utils.md5("sss" + "test");
    // // if(s1.equals(s)) {
    // //
    // // }
    // String signData = sign("test",
    // "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANfH6zZRxpwsh6GnAIiyLU0qAW2mE0/AberI8GM+m0EO9AnQFYAKY5937JVuQwQhimDwIph4kvAHUx222p++YZ0Jkh3vMbF6nnXd1jhZLKVivpJlKtt8Yq0z8R4xC7eiEY0TchhZHpcbFeBcUYvCbshgD/0O1wrz4Xu2PkX0DzZtAgMBAAECgYEAgZ9l676aKa0SWQhiaLtoPUdzjjPKvAjjvruwtF2ilCVwcgifMciL6fr3kximh2/CmdMHZUJhOnGb+2ih0n5iUgJKvBUB6blzB+7TI98zEl05vBh4bBVak+LR125g3BBwJngzrQkB/AW7fZEZMSgBwYPSumqJ3RDIUqPtX7MsHSECQQDuZhHKryG/zOrWDtOMVeQPhxdUh4DaSL6EA3uY1jyiwOxHN5KKVB008NG6/jLc7G+X+YrhCIs6/M0/N0WIchuJAkEA57Za1qeYajN+1R3n8SL7MjIsKvoHoRbPQEzeyZnA+44Y+42tFd0taThrfVrZfX6XZ3bbscz3hYOOZiwt5wBWxQJAUQrjy3pDx9cakhBSPCfKsrii5rp1xD7sfSZN8wQJcu6QpBkxONMlZqTjN2VI1y+NNCXB34QvCKt9/pqEicD4uQJAE3ak4L0JN3qF3974ObAni4rdXOrNs0FYPV13pVWN+VNxU8gNzvVaOCluFgSImlsE2sMIJ0JBVGy9jGWPoYTMxQJBAIU/+Y4H+j28tIYX5U7EUttEfXR/JBEEqlWdiPhuMAupGVp5Zya5V21GBVNYu/X+y3Ys0QN1jXJFYujE/rqor3I=");
    // //System.out.println("signData=" + signData);
    // boolean result = checkSign("test", signData,
    // "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDXx+s2UcacLIehpwCIsi1NKgFtphNPwG3qyPBjPptBDvQJ0BWACmOfd+yVbkMEIYpg8CKYeJLwB1MdttqfvmGdCZId7zGxep513dY4WSylYr6SZSrbfGKtM/EeMQu3ohGNE3IYWR6XGxXgXFGLwm7IYA/9DtcK8+F7tj5F9A82bQIDAQAB");
    // //System.out.println("result=" + result);
    // }
    // } catch(Exception e) {
    // e.printStackTrace();
    // }
    // counter.countDown();
    // System.out.println("end");
    // }
    // });
    // }
    // counter.await();
    // long t2 = System.currentTimeMillis();
    // System.out.println("threadNum=" + threadNum
    // + ",time(ms)=" + (t2 - t1)
    // + ",speed=" + (1000L * threadNum * N / (t2 - t1)));
    // System.exit(0);
    // }
}
