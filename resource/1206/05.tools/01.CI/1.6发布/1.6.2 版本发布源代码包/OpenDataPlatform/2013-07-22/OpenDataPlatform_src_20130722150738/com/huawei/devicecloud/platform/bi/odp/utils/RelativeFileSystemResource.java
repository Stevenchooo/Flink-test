/*
 * 文 件 名:  RelativeFileSystemResource.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-9-4
 */
package com.huawei.devicecloud.platform.bi.odp.utils;

import org.springframework.core.io.FileSystemResource;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-9-4]
 */
public class RelativeFileSystemResource extends FileSystemResource
{
    private static final String CLASS_PATH = RelativeFileSystemResource.class.getResource("/").getFile();
    /**
     * 默认构造函数
     * @param path 相对类路径的文件名，支持".."
     */
    public RelativeFileSystemResource(String path)
    {
        super(CLASS_PATH + path);
    }
}
