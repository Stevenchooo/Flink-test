/*
 * 文 件 名:  SqlPretreat.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  预处理
 * 创 建 人:  z00190465
 * 创建时间:  2012-11-5
 */
package com.huawei.bi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * sql预处理
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-11-5]
 */
public class SqlPretreat
{
    private static final String STATEMENT = "^([^'\";]*(\"[^\"]*?(\"|$)|'[^']*?('|$))?[^'\";]*)*?($|;)";
    
    private static final String QUERY = "^(select)";
    
    private static final Pattern S_PAT = Pattern.compile(STATEMENT, Pattern.CASE_INSENSITIVE);
    
    private static final Pattern Q_PAT = Pattern.compile(QUERY, Pattern.CASE_INSENSITIVE);
    
    private static final String PRINT_HEADER = "set hive.cli.print.header=true;";
    
    private static final String NO_PRINT_HEADER = "set hive.cli.print.header=false;";
    
    /**
     * 在代码头部插入use db;语句 
     * @param defaultDB 默认db
     * @param code 代码
     * @return 代码
     */
    public String insertUseDB(final String defaultDB, String code)
    {
        //默认db名字
        String dbName = StringUtils.isBlank(defaultDB) ? "default" : defaultDB;
        //增加默认库
        return NO_PRINT_HEADER + "use " + dbName + ";" + code;
    }
    
    /**
     * 对sql语句进行预处理[移除注释、移除换行符、合并空白符为单空格]
     * @param sql 多条sql语句
     * @return sql预处理后的结构
     */
    public String pretreat(final String sql)
    {
        String result = sql;
        //移除注释
        if (!StringUtils.isEmpty(result))
        {
            result = removeNotation(result);
        }
        
        //移除换行符
        if (!StringUtils.isEmpty(result))
        {
            result = removeNewLineChar(result);
        }
        
        //将所有的空白符换成一个空格
        if (!StringUtils.isEmpty(result))
        {
            result = mergeSpaceChars(result);
        }
        
        StringBuilder codeSb = new StringBuilder();
        Matcher matcher;
        String statement;
        while (!StringUtils.isEmpty(result))
        {
            //获取第一行
            matcher = S_PAT.matcher(result);
            
            if (matcher.find())
            {
                //获取语句
                statement = matcher.group(0);
                //判断是否是select语句
                addPrintHeader(codeSb, statement);
                //移除处理过的语句
                result = matcher.replaceFirst("");
            }
            else
            {
                codeSb.append(PRINT_HEADER);
                codeSb.append(result);
                break;
            }
        }
        
        return codeSb.toString();
    }
    
    //是否是select语句
    public void addPrintHeader(StringBuilder sb, String statement)
    {
        Matcher matcher = Q_PAT.matcher(statement);
        //是查询语句,则开启print.header,否则关闭
        if (matcher.find())
        {
            sb.append(PRINT_HEADER);
        }
        else
        {
            sb.append(NO_PRINT_HEADER);
        }
        sb.append(statement);
    }
    
    //移除所有回车换行符
    public String removeNewLineChar(String sql)
    {
        return sql.replaceAll("[\\n\\r]", " ");
    }
    
    //移除所有的注释
    public String removeNotation(String sql)
    {
        boolean singleQuotaS = false;
        boolean doubleQuotaS = false;
        boolean notationS = false;
        
        StringBuilder result = new StringBuilder();
        //sql的当前字符
        char c;
        int index = 0;
        while (index < sql.length())
        {
            c = sql.charAt(index);
            if (notationS)
            {
                //去掉注释
                if (c == '\n' || c == '\r')
                {
                    //关闭注释
                    notationS = false;
                }
                index++;
                continue;
            }
            //当前字符时单引号而且前面的双引号已经配对
            else if (!doubleQuotaS && c == '\'')
            {
                if (singleQuotaS)
                {
                    //右侧配对单引号
                    singleQuotaS = false;
                }
                else
                {
                    //左侧单引号
                    singleQuotaS = true;
                }
                result.append(c);
            }
            //当前字符时双引号而且前面的单引号已经配对
            else if (!singleQuotaS && c == '"')
            {
                if (doubleQuotaS)
                {
                    //右侧配对双引号
                    doubleQuotaS = false;
                }
                else
                {
                    //左侧双引号
                    doubleQuotaS = true;
                }
                result.append(c);
            }
            //配对的双引号或者单引号之外
            else if (!singleQuotaS && !doubleQuotaS && (c == '-'))
            {
                //注释开头
                if (result.length() > 0 && '-' == result.charAt(result.length() - 1))
                {
                    notationS = true;
                    //移除前面的-字符
                    result.deleteCharAt(result.length() - 1);
                }
                else
                {
                    result.append(c);
                }
            }
            else
            {
                result.append(c);
            }
            
            index++;
        }
        return result.toString();
    }
    
    //合并所有的空白符,移除语句之间的非";"分隔符，合并重复的”;“语句分隔符
    public String mergeSpaceChars(String sql)
    {
        boolean singleQuotaS = false;
        boolean doubleQuotaS = false;
        boolean space = false;
        
        //如果最后一个字符不是分号，则自动添加分号
        if (sql.length() > 0 && ';' != sql.charAt(sql.length() - 1))
        {
            sql += ";";
        }
        
        StringBuilder result = new StringBuilder();
        //sql的当前字符
        char c;
        int index = 0;
        while (index < sql.length())
        {
            c = sql.charAt(index);
            //当前字符时单引号而且前面的双引号已经配对
            if (!doubleQuotaS && c == '\'')
            {
                if (singleQuotaS)
                {
                    //右侧配对单引号
                    singleQuotaS = false;
                }
                else
                {
                    //左侧单引号
                    singleQuotaS = true;
                }
                
                space = false;
                result.append(c);
            }
            //当前字符时双引号而且前面的单引号已经配对
            else if (!singleQuotaS && c == '"')
            {
                if (doubleQuotaS)
                {
                    //右侧配对双引号
                    doubleQuotaS = false;
                }
                else
                {
                    //左侧双引号
                    doubleQuotaS = true;
                }
                
                space = false;
                result.append(c);
            }
            //配对的双引号或者单引号之外
            else if (!singleQuotaS && !doubleQuotaS && (c == ' ' || c == '\t'))
            {
                if (!space)
                {
                    space = true;
                    //语句开头不允许有空格
                    if (result.length() > 0 && ';' != result.charAt(result.length() - 1))
                    {
                        //第一次出现空白符
                        result.append(' ');
                    }
                    
                }
                //其它情况不添加
            }
            //移除;之前的空格或者;号
            else if (!singleQuotaS && !doubleQuotaS && c == ';')
            {
                if (result.length() > 0
                    && (' ' == result.charAt(result.length() - 1) || ';' == result.charAt(result.length() - 1)))
                {
                    result.deleteCharAt(result.length() - 1);
                }
                
                result.append(';');
            }
            else
            {
                space = false;
                result.append(c);
            }
            
            index++;
        }
        return result.toString();
    }
}
