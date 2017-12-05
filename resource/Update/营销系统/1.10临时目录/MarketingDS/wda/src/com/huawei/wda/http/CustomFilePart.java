package com.huawei.wda.http;

import java.io.File;

/**
 * 要上传的文件
 * 
 * @author yWX289656
 *
 */
public class CustomFilePart
{
    /**
     * 字段名
     */
    private String name;

    /**
     * 文件
     */
    private File file;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 构造函数
     * 
     * @param name
     *            字段名
     * @param file
     *            文件
     * @param fileName
     *            文件名
     */
    public CustomFilePart(String name, File file, String fileName)
    {
        super();
        this.name = name;
        this.file = file;
        this.fileName = fileName;
    }

    /**
     * 无参构造函数
     * 
     */
    public CustomFilePart()
    {

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

}
