/*
 * 文 件 名:  FileUploadEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-4-21
 */
package com.huawei.devicecloud.platform.bi.odp.entity;

import java.util.Date;


/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Open Data Platform Service, 2013-4-21]
 * @see  [相关类/方法]
 */
public class FileUploadTableEntity
{
    //文件ID
    private String fileId;
    
    //上传时间
    private Date loadTime;
    
    //处理标识
    private Integer flag;

    public String getFileId()
    {
        return fileId;
    }

    public void setFileId(String fileId)
    {
        this.fileId = fileId;
    }

    public Date getLoadTime()
    {
        return loadTime;
    }

    public void setLoadTime(Date loadTime)
    {
        this.loadTime = loadTime;
    }

    public Integer getFlag()
    {
        return flag;
    }

    public void setFlag(Integer flag)
    {
        this.flag = flag;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
        result = prime * result + ((flag == null) ? 0 : flag.hashCode());
        result = prime * result + ((loadTime == null) ? 0 : loadTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        FileUploadTableEntity other = (FileUploadTableEntity)obj;
        if (fileId == null)
        {
            if (other.fileId != null)
            {
                return false;
            }
        }
        else if (!fileId.equals(other.fileId))
        {
            return false;
        }
        if (flag == null)
        {
            if (other.flag != null)
            {
                return false;
            }
        }
        else if (!flag.equals(other.flag))
        {
            return false;
        }
        if (loadTime == null)
        {
            if (other.loadTime != null)
            {
                return false;
            }
        }
        else if (!loadTime.equals(other.loadTime))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("FileUploadTableEntity [fileId=");
        builder.append(fileId);
        builder.append(", loadTime=");
        builder.append(loadTime);
        builder.append(", flag=");
        builder.append(flag);
        builder.append("]");
        return builder.toString();
    }

    
    
}
