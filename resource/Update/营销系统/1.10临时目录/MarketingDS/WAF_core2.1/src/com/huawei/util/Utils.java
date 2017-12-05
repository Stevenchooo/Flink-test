package com.huawei.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.util.ByteArrayBuffer;
import org.slf4j.Logger;
import com.huawei.waf.protocol.Const;

public class Utils {
    private static final Logger LOG = LogUtil.getInstance();

    private static final char[] hexChars = {
    	'0', '1', '2', '3', '4', '5', '6', '7',
    	'8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    
    private static final char[] base64Chars = { 
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
		'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
		'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
		'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', '-', '_'
    };
    
    private static final char[] upcaseChars = { 
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z'
    };

    /**
     * 将一个对象转为整形数
     * 
     * @param o
     * @param defVal
     *            如果解析失败时，返回defVal
     * @return
     */
    public static int parseInt(Object o, int defVal) {
        if (o == null) {
            return defVal;
        }

        if (o instanceof Number) {
            return ((Number) o).intValue();
        }

        if (o instanceof String) {
            try {
                return Integer.parseInt((String) o);
            } catch (NumberFormatException e) {
            }
        }

        return defVal;
    }

    /**
     * 将一个byte数组转为整型数
     * 
     * @param o
     * @param defVal
     *            如果解析失败时，返回defVal
     * @return
     */
    public static int parseInt(byte[] b, int offset, int len) {
        if (b.length < offset + len || len > 4) {
            throw new NumberFormatException("Wrong int format");
        }

        int v = 0;
        for (int i = 0; i < len; i++) {
            v <<= 8;
            v |= ((int) b[i + offset]) & 0xff;
        }
        return v;
    }

    /**
     * 将byte数组转为整型
     * 
     * @param b
     * @return
     */
    public static int parseInt(byte[] b) {
        int v = 0;
        for (int i = 0; i < 4; i++) {
            v <<= 8;
            v |= ((int) b[i]) & 0xff;
        }
        return v;
    }

    public static boolean parseInt(int v, byte[] b, int offset) {
        if (b.length < offset + 4) {
            return false;
        }

        for (int i = 0; i < 4; i++) {
            b[offset++] = (byte) ((v >> ((3 - i) << 3)) & 0xff);
        }
        return true;
    }

    public static float parseFloat(Object o, float defVal) {
        if (o == null) {
            return defVal;
        }

        if (o instanceof Number) {
            return ((Number) o).floatValue();
        }

        if (o instanceof String) {
            try {
                return Float.parseFloat((String) o);
            } catch (NumberFormatException e) {
            }
        }

        return defVal;
    }

    public static double parseDouble(Object o, double defVal) {
        if (o == null) {
            return defVal;
        }

        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }

        if (o instanceof String) {
            try {
                return Double.parseDouble((String) o);
            } catch (NumberFormatException e) {
            }
        }

        return defVal;
    }

    public static boolean parseBool(Object o, boolean defVal) {
        if (o == null) {
            return defVal;
        }

        if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue();
        }

        if (o instanceof Number) {
            return (((Number) o).intValue() != 0);
        }

        if (o instanceof String) {
            return Boolean.parseBoolean((String) o);
        }

        return defVal;
    }

    public static long parseLong(Object o, long defVal) {
        if (o == null) {
            return defVal;
        }

        if (o instanceof Number) {
            return ((Number) o).longValue();
        }

        if (o instanceof String) {
            try {
                return Long.parseLong((String) o);
            } catch (NumberFormatException e) {
            }
        }

        return defVal;
    }

    public static String parseString(Object o, String defVal) {
        if (o == null) {
            return defVal;
        }

        return o.toString();
    }

    /**
     * 产生uuid，已base64方式返回
     * 
     * @return
     */
    public static String genUUID_64() {
        return bin2base64(genUUID());
    }

    /**
     * 将byte数组转为base64方式的字符串
     * 
     * @param bytes
     * @param start
     * @param len
     * @return
     */
    public static final String bin2base64(byte[] bytes, int start, int len) {
        int left = 0;
        int end = start + len;
        int i, v = 0;
        StringBuilder sb = new StringBuilder();

        for (i = start; i < end; i++) {
            v |= ((((int) bytes[i]) & 0xff) << left);
            left += 8;
            while (left >= 6) {
                sb.append(base64Chars[v & 0x3f]);
                v >>>= 6;
                left -= 6;
            }
        }

        if (left > 0) {
            sb.append(base64Chars[v & 0x3f]);
        }

        return sb.toString();
    }

    public static final String bin2base64(byte[] bytes) {
        if(bytes == null || bytes.length == 0) {
            return "";
        }
        return bin2base64(bytes, 0, bytes.length);
    }

    public static final byte[] base642bin(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }

        int len = str.length();
        byte[] bytes = new byte[len * 6 / 8];
        int i, n = 0, v = 0;
        int left = 0;

        for (i = 0; i < len; i++) {
            v |= (getBase64CharVal(str.charAt(i)) << left);
            left += 6;
            while (left >= 8) {
                bytes[n] = (byte) (v & 0xff);
                v >>>= 8;
                left -= 8;
                n++;
            }
        }

        return bytes;
    }

    private static final int getBase64CharVal(char c) {
        if (c >= 'A' && c <= 'Z') {
            return c - 'A';
        } else if (c >= 'a' && c <= 'z') {
            return c - 'a' + 26;
        } else if (c >= '0' && c <= '9') {
            return c - '0' + 52;
        } else if (c == '-') {
            return 62;
        } else {
            return 63;
        }
    }

    /**
     * 产生uuid，以byte数组方式返回，共16字节
     * 
     * @return
     */
    public static byte[] genUUID() {
        UUID uuid = UUID.randomUUID();
        byte[] b = new byte[16];
        int i;
        long v;

        v = uuid.getLeastSignificantBits();
        for (i = 0; i < 8; i++) {
            b[i] = (byte) v;
            v >>>= 8;
        }

        v = uuid.getMostSignificantBits();
        for (i = 0; i < 8; i++) {
            b[i + 8] = (byte) v;
            v >>>= 8;
        }
        return b;
    }

    public static String bin2hex(byte[] bytes, int start, int len) {
        StringBuilder str = new StringBuilder(len << 1);
        int v;
        int end = start + len;

        for (int i = start; i < end; i++) {
            v = ((int) bytes[i]) & 0xff;
            str.append(hexChars[v >>> 4]);
            str.append(hexChars[v & 0x0f]);
        }
        return str.toString();
    }

    public static String bin2hex(byte[] bytes) {
        return bin2hex(bytes, 0, bytes.length);
    }

    public static byte[] hex2bin(String hex) {
        int len = hex.length();
        if((len & 1) != 0) {
            throw new IllegalArgumentException("Invalid hex string, length is " + len);
        }
        int n = len >> 1;
        byte[] bytes = new byte[n];
        int v, i;

        for (i = 0, n = 0; i < len; i += 2) {
            v = getHexCharVal((char) hex.charAt(i));
            v <<= 4;
            v += getHexCharVal((char) hex.charAt(i + 1));
            bytes[n++] = (byte) v;
        }
        return bytes;
    }

    private static final int getHexCharVal(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        } else if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        } else if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        return 0;
    }

    public static byte[] md5(byte[] src) {
        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            md5Digest.update(src);
            return md5Digest.digest();
        } catch (NoSuchAlgorithmException e) {
            LOG.debug("UnsupportedEncodingException", e);
        }
        return null;
    }

    public static String md5(String src) {
        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            md5Digest.update(src.getBytes(Const.DEFAULT_CHARSET));
            return bin2hex(md5Digest.digest());
        } catch (Exception e) {
            LOG.debug("md5 failed", e);
        }
        return null;
    }

    public static String md5_base64(String src) {
        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            md5Digest.update(src.getBytes(Const.DEFAULT_CHARSET));
            return bin2base64(md5Digest.digest());
        } catch (Exception e) {
            LOG.debug("md5 failed", e);
        }
        return null;
    }

    public static byte[] sha256(String src) {
        try {
            MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
            shaDigest.update(src.getBytes(Const.DEFAULT_CHARSET));
            return shaDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // impossible
        }
        return null;
    }
    
	
	private static final String HMAC_SHA1 = "HmacSHA1";

    /**
    * 生成签名数据
    * 
    * @param data 待加密的数据
    * @param key 加密使用的key
    * @return 生成MD5编码的字符串
    * @throws InvalidKeyException
    * @throws NoSuchAlgorithmException
    */
    public static byte[] hmacSHA1(byte[] data, byte[] key)
            throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data);
        
        return rawHmac;
    }

    /**
     * 将字符串由ISO-8859-1转为UTF-8
     * @param src
     * @return
     */
    public static final String strToUTF8(String src){
        try {
            return new String(src.getBytes("ISO-8859-1"), Const.DEFAULT_CHARSET);
        } catch (Exception e) {
            return src;
        }
    }
    
    /**
     * 隐藏字符串中部分字符串，用blurChar填充
     * @param srcStr
     * @param start
     * @param len
     * @param blurChar
     * @return
     */
    public static final String strBlur(String srcStr, int start, int len, char blurChar) {
        if (srcStr == null || srcStr.length() <= 0
            || start < 0 || len <= 0) {
            return srcStr;
        }

        int srcLen = srcStr.length();
        if(start >= srcLen) {
            return srcStr;
        }
        
        int blurLen = srcLen - start - len;
        if(blurLen < 0) {
            blurLen = srcLen - start;
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append(srcStr.substring(0, start));
        for (int i = 0; i < blurLen; i++) {
            sb.append(srcLen);
        }
        sb.append(srcStr.substring(start + blurLen));

        return sb.toString();
    }

    /**
     * 判断字符串为null，或为空串
     * @param s
     * @return
     */
    public static final boolean isStrEmpty(String s) {
        return s == null || s.equals("");
    }
    
    public static boolean isBlank(String s) {
        return isBlank(s, null);
    }
    
    public static boolean isBlank(String s, String[] blankStrs) {
        if ((s == null) || s.length() <= 0) {
            return true;
        }
        
        String ss = s.trim();
        if(ss.length() <= 0) {
            return true;
        }
        
        if(blankStrs != null) {
            for(String bs : blankStrs) {
                if(bs.equals(s)) {
                    return true;
                }
            }
        }
    
        return false;
    }


    /**
     * 将输入流中的内容转为byte数组
     * 
     * @param instream 输入流
     * @param len 输入流的长度，如果无法获得，可以输入0
     * @return
     * @throws IOException
     */
    public static byte[] streamToByteArray(final InputStream instream, int len) throws IOException {
        if (instream == null) {
            return null;
        }
        
        try {
            if (len <= 0) {
                len = 4096;
            }
            final ByteArrayBuffer buffer = new ByteArrayBuffer(len);
            final byte[] tmp = new byte[4096];
            int l;

            while ((l = instream.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
            return buffer.toByteArray();
        } finally {
            instream.close();
        }
    }

    /**
     * 将数组合并为一个字符串
     * 
     * @param arr
     * @return
     */
    public static final String joinArray(Collection<Object> arr, String spliter) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (Object o : arr) {
            if (!first) {
                sb.append(spliter);
            } else {
                first = false;
            }
            sb.append(o.toString());
        }
        return sb.toString();
    }

    /**
     * 将数组合并为一个字符串
     * 
     * @param arr
     * @return
     */
    public static final String joinArray(Object[] arr, String spliter) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (Object o : arr) {
            if (!first) {
                sb.append(spliter);
            } else {
                first = false;
            }
            sb.append(o.toString());
        }
        return sb.toString();
    }

    /**
     * 判断字符串是否满足正则表达式描述的规则
     * 
     * @param str
     * @param regular
     * @return
     */
    public static final boolean isStrMatch(String str, String regular) {
        Pattern pattern = Pattern.compile(regular);
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }

    /**
     * 暴力方式反射对象，BeanUtils不支持，
     * 无论对象属性是否为public，都可以获取
     * @param obj
     * @param property
     * @return
     */
    public static final Object getObjectProperty(Object obj, String property) {
        if (obj == null) {
            return null;
        }

        Field f;

        try {
            f = obj.getClass().getDeclaredField(property);
            if (f == null) {
                return null;
            }
            f.setAccessible(true);

            return f.get(obj);
        } catch (Exception e) {
        }
        
        return null;
    }
    
    /**
     * 合并两个数组，如果有一个为空，则返回另外一个，如果两个都为空，则返回空
     * 否则返回两个合并后的数组
     * @param a
     * @param b
     * @return
     */
    public static final <T> T[] concat(T[] a, T[] b) {
        if(a == null) {
            return b;
        }
        
        if(b == null) {
            return a;
        }
        
        T[] c = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        
        return c;
    }
    
    public static final Set<String> getLocalAddrs() {
        HashSet<String> hosts = new HashSet<String>(); 
        Enumeration<NetworkInterface> netInterfaces = null;
        
        try {  
            netInterfaces = NetworkInterface.getNetworkInterfaces();  
            while (netInterfaces.hasMoreElements()) {  
                NetworkInterface ni = netInterfaces.nextElement();  
                Enumeration<InetAddress> ips = ni.getInetAddresses();  
                while (ips.hasMoreElements()) {
                    hosts.add(ips.nextElement().getHostAddress());  
                }  
            }  
        } catch (Exception e) {
            LOG.error("Fail to get hosts from system", e);
        }
        
        return hosts;
    }
    
    public static final String getRandomStr(int len) {
        StringBuilder result = new StringBuilder();
        SecureRandom sr;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Fail to get SecureRandom instance", e);
            return null;
        }
        
        int sn, size = upcaseChars.length;
        for(int i = 0; i < len; i++){
            sn = ((int)Math.round(sr.nextDouble() * size)) % size;
            result.append(upcaseChars[sn]);
        }
        return result.toString();
    }
    
    /**
     * 获取签名数据（排序）
     * @param params
     * @param keys 字段列表
     * @param spChar key<->value之间的分隔符
     * @param connChar 不同kv之间的连接符
     * @return
     */
    public static final String getSignData(Map<String, Object> params, List<String> keys, char spChar, char connChar) {
        StringBuffer content = new StringBuffer();

        boolean first = true;
        // 按照key做排序
        Collections.sort(keys);

        for (String key : keys) {
            if (!first) {
                content.append(spChar);
            }
            content.append(key).append(connChar).append(parseString(params.get(key), ""));
            first = false;
        }

        return content.toString();
    }
    
    /**
     * 判断两个集合是否有交集
     * @param src
     * @param container
     * @return
     */
    public static final boolean isIntersected(Collection<String> src, Collection<String> container) {
        for (String s : src) {
            if (container.contains(s)) {
                return true;
            }
        }
        return false;
    }
//    
//    public static void main(final String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
//        byte[] authCode = "0A1B2C3D4E5F".getBytes();
//        
//        String s = "zY6LZMLLpeWbxSuzKPujZuJosj9P7ojm82JsvGn42vI";
//        byte[] b = Utils.base642bin(s);
//        System.out.println("org1:" + Utils.bin2hex(b));
//        b = AESUtil.ecbDecrypt(b, authCode);
//        System.out.println("dec1:" + Utils.bin2hex(b));
//        
//        s = "zY6LZMLLpeWbxSuzKPujZuJosj9P7ojm82JsvGn42";
//        b = Utils.base642bin(s);
//        System.out.println("org2:" + Utils.bin2hex(b));
//        b = AESUtil.ecbEncrypt(b, authCode);
//        System.out.println("dec2:" + Utils.bin2hex(b));
//        
//        System.exit(0);
//    }
}
