/*
 * 文 件 名:  FileUploadReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-4-12
 */
package com.huawei.devicecloud.platform.bi.odp.message.req;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Open Data Platform Service, 2013-4-12]
 * @see  [相关类/方法]
 */
public class FileUploadReq
{
    //校验头
    private String authenInfo;
    
    //应用标识
    private String appId;
    
    //时间戳
    private String timestamp;
    
    //文件类型
    private String fileType;

    public String getAuthenInfo()
    {
        return authenInfo;
    }

    public void setAuthenInfo(String authenInfo)
    {
        this.authenInfo = authenInfo;
    }

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getFileType()
    {
        return fileType;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appId == null) ? 0 : appId.hashCode());
        result = prime * result + ((authenInfo == null) ? 0 : authenInfo.hashCode());
        result = prime * result + ((fileType == null) ? 0 : fileType.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
        FileUploadReq other = (FileUploadReq)obj;
        if (appId == null)
        {
            if (other.appId != null)
            {
                return false;
            }
        }
        else if (!appId.equals(other.appId))
        {
            return false;
        }
        if (authenInfo == null)
        {
            if (other.authenInfo != null)
            {
                return false;
            }
        }
        else if (!authenInfo.equals(other.authenInfo))
        {
            return false;
        }
        if (fileType == null)
        {
            if (other.fileType != null)
            {
                return false;
            }
        }
        else if (!fileType.equals(other.fileType))
        {
            return false;
        }
        if (timestamp == null)
        {
            if (other.timestamp != null)
            {
                return false;
            }
        }
        else if (!timestamp.equals(other.timestamp))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("FileUploadReq [authenInfo=");
        builder.append(authenInfo);
        builder.append(", appId=");
        builder.append(appId);
        builder.append(", timestamp=");
        builder.append(timestamp);
        builder.append(", fileType=");
        builder.append(fileType);
        builder.append("]");
        return builder.toString();
    }
    
}
