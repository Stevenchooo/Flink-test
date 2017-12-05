package com.huawei.ide.interceptors.res.rcm;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * AES128CBC加密解密工具类
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see  [相关类/方法]
 */
public class AES128CBC
{
    private static final int AES_128_KEY_LEN = 16;
    
    private static final int OPMODE_ENCODE = 1;
    
    private static final int OPMODE_DECODE = 2;
    
    /**
     * 无参构造函数
     */
    public AES128CBC()
    {
    }
    
    private static byte[] getIV()
    {
        String uuid = UUID.randomUUID().toString();
        String iv = uuid.substring(0, AES_128_KEY_LEN);
        try
        {
            return iv.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            return new byte[0];
        }
    }
    
    /**
     * 加密
     * 
     * @param content
     *            明文
     * @param contentOffset
     *            明文偏移位置
     * @param key
     *            秘钥
     * @param keyOffset
     *            秘钥偏移地址
     * @return 密文
     * @throws CryptException
     *             加密异常
     */
    public static byte[] encode(byte[] content, int contentOffset, byte[] key, int keyOffset)
        throws CryptException
    {
        return encodeAnddecode(content, contentOffset, key, keyOffset, OPMODE_ENCODE);
    }
    
    /**
     * 解密
     * 
     * @param securityContent
     *            密文
     * @param scOffSet
     *            密文偏移位置
     * @param key
     *            秘钥
     * @param keyOffset
     *            秘钥偏移地址
     * @return 密文
     * @throws CryptException
     *             CryptException
     */
    public static byte[] decode(byte[] securityContent, int scOffSet, byte[] key, int keyOffset)
        throws CryptException
    {
        return encodeAnddecode(securityContent, scOffSet, key, keyOffset, OPMODE_DECODE);
    }
    
    private static byte[] encodeAnddecode(byte[] content, int contentOffset, byte[] key, int keyOffset, int opmode)
        throws CryptException
    {
        try
        {
            byte[] iv = null;
            // 如果是加密，就随机生成向量，如果是解密，就从密文的前16位获取向量
            if (opmode == OPMODE_ENCODE)
            {
                iv = getIV();
            }
            else
            {
                iv = Arrays.copyOf(content, AES_128_KEY_LEN);
                content = Arrays.copyOfRange(content, 16, content.length);
            }
            byte[] arrayOfByte = null;
            Cipher localCipher = null;
            if ((content == null) || (key == null))
            {
                return new byte[0];
            }
            if ((contentOffset <= 0) || (contentOffset > content.length))
            {
                contentOffset = content.length;
            }
            keyOffset = countKeyOffSet(keyOffset, key.length);
            arrayOfByte = new byte[AES_128_KEY_LEN];
            for (int i = 0; i < AES_128_KEY_LEN; i++)
            {
                arrayOfByte[i] = 0;
            }
            System.arraycopy(key, 0, arrayOfByte, 0, keyOffset);
            localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            
            localCipher.init(opmode,
                new SecretKeySpec(arrayOfByte, 0, AES_128_KEY_LEN, "AES"),
                new IvParameterSpec(iv));
            
            // 加密时将向量拼接在密文的前面
            if (opmode == OPMODE_ENCODE)
            {
                byte[] securityContent = localCipher.doFinal(content, 0, contentOffset);
                byte[] result = new byte[securityContent.length + AES_128_KEY_LEN];
                System.arraycopy(iv, 0, result, 0, AES_128_KEY_LEN);
                System.arraycopy(securityContent, 0, result, AES_128_KEY_LEN, securityContent.length);
                return result;
            }
            else
            {
                return localCipher.doFinal(content, 0, contentOffset);
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new CryptException(e);
        }
        catch (NoSuchPaddingException e)
        {
            throw new CryptException(e);
        }
        catch (InvalidKeyException e)
        {
            throw new CryptException(e);
        }
        catch (InvalidAlgorithmParameterException e)
        {
            throw new CryptException(e);
        }
        catch (IllegalBlockSizeException e)
        {
            throw new CryptException(e);
        }
        catch (BadPaddingException e)
        {
            throw new CryptException(e);
        }
    }
    
    private static int countKeyOffSet(int keyOffset, int length)
    {
        if (keyOffset <= 0 || keyOffset > length)
        {
            keyOffset = length;
        }
        if (keyOffset > AES_128_KEY_LEN)
        {
            keyOffset = AES_128_KEY_LEN;
        }
        return keyOffset;
    }
    
    /**
     * hashCode
     * @return int
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
    
    /**
     * equals
     * @param obj
     *        obj
     * @return  boolean
     */
    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }
}
