/*
 * 文 件 名:  UploadFileRsp.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-6-3
 */
package com.huawei.devicecloud.platform.bi.odp.jersey.rsp;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Open Data Platform Service, 2013-6-3]
 * @see  [相关类/方法]
 */
public class UploadFileRsp
{
    /**
     * 返回值
     */
    private Integer resultCode;
    
    /**
     * 文件ID
     */
    private String fileId;
    
    /**
     * 记录数目
     */
    private int cnt;

    
    public Integer getResultCode()
    {
        return resultCode;
    }


    public void setResultCode(Integer resultCode)
    {
        this.resultCode = resultCode;
    }


    public String getFileId()
    {
        return fileId;
    }


    public void setFileId(String fileId)
    {
        this.fileId = fileId;
    }


    public int getCnt()
    {
        return cnt;
    }


    public void setCnt(int cnt)
    {
        this.cnt = cnt;
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("UploadFileRsp [resultCode=");
        builder.append(resultCode);
        builder.append(", fileId=");
        builder.append(fileId);
        builder.append(", cnt=");
        builder.append(cnt);
        builder.append("]");
        return builder.toString();
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + cnt;
        result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
        result = prime * result + ((resultCode == null) ? 0 : resultCode.hashCode());
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
        UploadFileRsp other = (UploadFileRsp)obj;
        if (cnt != other.cnt)
        {
            return false;
        }
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
        if (resultCode == null)
        {
            if (other.resultCode != null)
            {
                return false;
            }
        }
        else if (!resultCode.equals(other.resultCode))
        {
            return false;
        }
        return true;
    }


    /**
     * <返回文件上传响应消息体>
     * @return   文件上传响应消息体
     * @see [类、类#方法、类#成员]
     */
    public UploadFileRsp createUploadFileRsp()
    {
        UploadFileRsp rsp = new UploadFileRsp();
        rsp.setFileId(this.fileId);
        rsp.setResultCode(this.resultCode);
        rsp.setCnt(this.cnt);
        return rsp;
    }
    
    
}
