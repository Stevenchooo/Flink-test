/*
 * 文 件 名:  IEmailServer.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-7-4
 */
package com.huawei.platform.tcc.server;

/**
 * 邮件服务接口
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-7-4]
 */
public interface IEmailServer
{
    /**
     * 给邮箱列表发送邮件
     * @param emailsTo 目的地址列表
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param from 邮件发件人（显示名）
     */
    public abstract void sendEmail(String from, final String[] emailsTo, final String subject, final String content);
}
