/*
 * 文 件 名:  Encoder.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  编码器
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-29
 */
package com.huawei.platform.tcc.utils;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对字符串进行编码
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-29]
 */
public class Encoder
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Encoder.class);
    
    /**
     * 最大字符串参数大小100-200M
     */
    private static final int MAX_STRING_LENGTH = 100 * 1024 * 1024;
    
    /**
     * 需要编码的字符
     */
    private Map<Character, String> encodeChars;
    
    /**
     * 构造函数
     * @param replaceChars 替换字符集合
     */
    public Encoder(Map<Character, String> replaceChars)
    {
        encodeChars = new HashMap<Character, String>();
        if (null != replaceChars && !replaceChars.isEmpty())
        {
            encodeChars.putAll(replaceChars);
        }
    }
    
    /**
     * 构造函数
     */
    public Encoder()
    {
        encodeChars = new HashMap<Character, String>();
    }
    
    /**
     * 编码后的字符
     */
    public static class EncodedChar
    {
        private char currChar;
        
        /**
         * 构造函数
         * @param c 元素字符
         */
        public EncodedChar(char c)
        {
            this.currChar = c;
        }
        
        public char getUnEncoded()
        {
            return currChar;
        }
    }
    
    /**
     * 编码reader 
     * 
     * @author  z00190465
     * @version [Device Cloud Base Platform Dept KPIAnalysis V100R100, 2013-5-22]
     */
    public static class EncodeReader
    {
        private String strInput;
        
        private int cur = -1;
        
        /**
         * 构造函数
         * @param text 待编码字符串
         */
        public EncodeReader(String text)
        {
            if (null == text)
            {
                this.strInput = "";
            }
            
            this.strInput = Normalizer.normalize(text, Form.NFKC);
        }
        
        /**
         * 判读是否还有下一个字符
         * @return 是否还有下一个字符
         */
        public boolean hasNext()
        {
            return cur + 1 < strInput.length();
        }
        
        /**
         * 获取编码后的字符
         * @return 编码后的字符
         */
        public EncodedChar getNextChar()
        {
            cur++;
            return peekNextCharacter(strInput.charAt(cur));
        }
        
        private EncodedChar peekNextCharacter(char currentChar)
        {
            if (Character.isDefined(currentChar))
            {
                return new EncodedChar(currentChar);
            }
            else
            {
                return new EncodedChar('.');
            }
        }
    }
    
    private String encode(String text)
    {
        if (StringUtils.isBlank(text))
        {
            return null;
        }
        
        StringBuilder sbEncoded = new StringBuilder();
        
        String replace;
        EncodedChar c;
        for (EncodeReader reader = new EncodeReader(text); reader.hasNext();)
        {
            c = reader.getNextChar();
            replace = encodeChars.get(c.getUnEncoded());
            sbEncoded.append(null == replace ? c.getUnEncoded() : replace);
        }
        
        return sbEncoded.toString();
    }
    
    /**
     * 编码参数
     * @param text 文本
     * @return 编码参数
     */
    public String truncatEncode(String text)
    {
        if (StringUtils.isBlank(text))
        {
            return null;
        }
        
        String s;
        if (text.length() > MAX_STRING_LENGTH)
        {
            LOGGER.warn("length[{}] of paramter is too long!", text.length());
            s = text.substring(0, MAX_STRING_LENGTH);
        }
        else
        {
            s = text;
        }
        
        return encode(s);
    }
}
