package com.huawei.util;

import java.security.SecureRandom;

/**
 * AzDG算法主要用在通行证运算中。
 * 效率远高于aes算法，且相同密码、明文，每次运行会得到不同的结果
 * @author Administrator
 *
 */
public class AzDGCrypt {
    private static final SecureRandom rand = new SecureRandom();
    /**  
    * 加密函数  
    * @param txt     string      等待加密的原字串  
    * @param key     string      私有密匙(用于解密和加密)  
    * @return        string      经过私有密匙加密后的结果  
    */  
    public static final String encrypt(String txt, String key){         
        //使用随机数发生器产生 0~32000的值并 MD5()
        String encryptKey = Utils.bin2hex(Utils.md5(int2Bytes((int)(rand.nextInt() * 32000))));   
        int ctr = 0;
        StringBuilder ciphertext = new StringBuilder();
        int len = txt.length();
        int ekLen = encryptKey.length();

        for(int i = 0; i < len; i++) {   
            ctr = ctr == ekLen ? 0 : ctr; //如果ctr = encryptKey 的长度，则ctr清零   
            char a1 = encryptKey.charAt(ctr);   
            char t1 = txt.charAt(i);
            char t2 = encryptKey.charAt(ctr++);   
            char a2 = (char)(t1 ^ t2);
            ciphertext.append(a1).append(a2);
        }   
        return Utils.bin2base64((passport_key(ciphertext.toString(), key)).getBytes());   
    }
       
    public static final byte[] encrypt(byte[] txt, byte[] key){         
        //使用随机数发生器产生 0~32000的值并 MD5()
        byte[] encryptKey = Utils.md5(int2Bytes((int)(rand.nextInt() * 32000)));   
        int ctr = 0, n = 0;
        int len = txt.length;
        int ekLen = encryptKey.length;
        byte[] cipher = new byte[len << 1];
        int a1, a2, t1, t2;

        for(int i = 0; i < len; i++) {   
            ctr = ctr == ekLen ? 0 : ctr; //如果ctr = encryptKey 的长度，则ctr清零   
            a1 = encryptKey[ctr];   
            t1 = txt[i];
            t2 = encryptKey[ctr++];   
            a2 = t1 ^ t2;
            cipher[n++] = (byte)a1;
            cipher[n++] = (byte)a2;
        }   
        return passport_key(cipher, key);   
    }

    /**  
    * 解密函数  
    * @param    string      加密后的字串  
    * @param    string      私有密匙(用于解密和加密)  
    * @return   string      字串经过私有密匙解密后的结果  
    * @throws Base64DecodingException   
    */  
    public static final byte[] decrypt(byte[] cipher, byte[] key) {   
        byte[] txt = passport_key(cipher, key);
        int len = txt.length;
        byte[] text = new byte[len >> 1];
        
        for (int i = 0, n = 0; i < len; i += 2) {
            //txt的第i位，与 txt的第i+1位取异或   
        	text[n++] = (byte)(((int)txt[i]) ^ ((int)txt[i + 1]));
        }   
        return text;   
    }
    
    public static final String decrypt(String ciphertext, String key) {   
        String txt = passport_key(new String(Utils.base642bin(ciphertext)), key);
        int len = txt.length();
        StringBuilder plaintext = new StringBuilder();
        
        for (int i = 0; i < len; i++) {   
            //txt的第i位，与 txt的第i+1位取异或   
            plaintext.append((char)(txt.charAt(i) ^ txt.charAt(++i)));   
        }   
        return plaintext.toString();   
    }
    
    /**  
    * 密匙处理函数  
    * @param    string      待加密或待解密的字串  
    * @param    string      私有密匙(用于解密和加密)  
    * @return   string      处理后的密匙  
    */  
    private static final String passport_key(String txt, String key){   
        String encryptKey = Utils.md5(key);   
        int ctr = 0;
        int len = txt.length();
        int ekLen = encryptKey.length();
        StringBuilder plaintext = new StringBuilder();
        
        for(int i = 0; i < len; i++) {   
            ctr = ctr == ekLen ? 0 : ctr;   
            plaintext.append((char)(txt.charAt(i) ^ encryptKey.charAt(ctr++)));   
        }   
        return plaintext.toString();   
    }
    
    /**  
    * 密匙处理函数  
    * @param    string      待加密或待解密的字串  
    * @param    string      私有密匙(用于解密和加密)  
    * @return   string      处理后的密匙  
    */  
    private static final byte[] passport_key(byte[] txt, byte[] key){   
        byte[] encryptKey = Utils.md5(key);   
        int ctr = 0;
        int len = txt.length;
        int ekLen = encryptKey.length;
        byte[] result = new byte[len];
        
        for(int i = 0; i < len; i++) {   
            ctr = ctr == ekLen ? 0 : ctr;
            result[i] = (byte)(((int)txt[i]) ^ ((int)encryptKey[ctr++]));   
        }   
        return result;   
    }
    
    private static final byte[] int2Bytes(int x) {
        byte[] v = new byte[4];
        
        for(int i = 0; i < 4; i++) { //小端序，因为不需要还原
            v[i] = (byte)x;
            x >>= 8;
        }
        
        return v;
    }
//
//    public static void main(final String[] args) {
//        byte[] authCode = "0A1B2C3D4E5F".getBytes();
//        
//        String s = "3HJxMb0WWIOpmQKsgW5-Odzx26_4CHHf_nJ6B_D-Z409NSsjGxpF6Sqok6Mo_t_m3ojtYD";
//        byte[] b = Utils.base642bin(s);
//        System.out.println(Utils.bin2hex(b));
//        b = AzDGCrypt.decrypt(b, authCode);
//        System.out.println(Utils.bin2hex(b));
//        
//        s = "wM5CymXvN5sYr8cZw-h7gxKyutGHOwd20PRYy7A8W_C";
//        s = "1HJxMb0WWIOpmQKsgW5-Odzx26_4CHHf_nJ6B_D-Z409NSsjGxpF6Sqok6Mo_t_m3ojtYD";
//        b = Utils.base642bin(s);
//        System.out.println(Utils.bin2hex(b));
//        b = AzDGCrypt.decrypt(b, authCode);
//        System.out.println(Utils.bin2hex(b));
//        
//        System.exit(0);
//    }
//    
    /**
     * 8 threads, each run 1000000 times, encode and then decode,
     * 2 core, 2.0G, speed is 257000 tps, but then length will be more than twice of the original one 
     * @param args
     * @throws Exception
     */
//    public static void main(String[] args) throws Exception {
//        int threadNum = 8;
//        final int N = 10000000;
//        final java.util.concurrent.CountDownLatch counter = new java.util.concurrent.CountDownLatch(threadNum);
//        java.util.concurrent.ExecutorService exectors = java.util.concurrent.Executors.newFixedThreadPool(threadNum);  
//        final byte[] txt = "dddaa000000000000000aeee".getBytes();
//        final byte[] key = "ddd0000000000000000000".getBytes();
////        final String txt = "dddaa000000000000000aeee";
////        final String key = "ddd000000000000000000000";
//        
//        long t1 = System.currentTimeMillis();
//        final java.util.List<String> ss1 = new java.util.ArrayList<String>();
//        final String[] ss2 = new String[10];
//        for(int i = 0; i < 10; i++) {
//            ss1.add(Integer.toString(i));
//            ss2[i] = Integer.toString(i);
//        }
//        
//        
//        
//        for(int j = 0; j < threadNum; j++) {
//            exectors.execute(new Runnable(){
//                public void run() {
//                    byte[] s;
////                    String s;
//                    System.out.println("start");
//                    try {
//                        int a = 0;
//                        for(int i = 0; i < N; i++) {
//                            
//                        for(String ss : ss2) {
//                            a++;
////                            s = AzDGCrypt.encrypt(txt, key);
////                            System.out.println("txt:" + Utils.bin2base64(txt) + " key:" + Utils.bin2base64(key) + " txt after crypt:" + Utils.bin2base64(s));
////                            s = AzDGCrypt.decrypt(s, key);
////                            System.out.println("txt:" + Utils.bin2base64(txt) + " key:" + Utils.bin2base64(key) + " txt after decrypt:" + Utils.bin2base64(s));
//////                            s = AzDGCrypt.encrypt(txt, key);
//////                            System.out.println("txt:" + txt + " key:" + key + " txt after crypt:" + s);
//////                            s = AzDGCrypt.decrypt(s, key);
//////                            System.out.println("txt:" + txt + " key:" + key + " txt after decrypt:" + s);
//                        }
//                        }
//                    } catch(Exception e) {
//                        e.printStackTrace();
//                    }
//                    counter.countDown();
//                    System.out.println("end");
//                }
//            });
//        }
//        counter.await();
//        long t2 = System.currentTimeMillis();
//        System.out.println("speed=" + (1000 * (threadNum * N / (t2 - t1))));
//        System.exit(0);
//    }
} 
