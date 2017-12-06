/*
 * 文 件 名:  FileUploadRsp.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-4-13
 */
package com.huawei.devicecloud.platform.bi.odp.message.rsp;

import com.huawei.devicecloud.platform.bi.odp.domain.IModifyResult;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Open Data Platform Service, 2013-4-13]
 * @see  [相关类/方法]
 */
public class FileUploadRsp implements IModifyResult
{
    /**
     * 返回值
     */
    private Integer result_code;
    
    /**
     * 文件ID
     */
    private String file_id;

    public Integer getResult_code()
    {
        return result_code;
    }

    public void setResult_code(Integer result_code)
    {
        this.result_code = result_code;
    }

    public String getFile_id()
    {
        return file_id;
    }

    public void setFile_id(String file_id)
    {
        this.file_id = file_id;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((file_id == null) ? 0 : file_id.hashCode());
        result = prime * result + ((result_code == null) ? 0 : result_code.hashCode());
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
        FileUploadRsp other = (FileUploadRsp)obj;
        if (file_id == null)
        {
            if (other.file_id != null)
            {
                return false;
            }
        }
        else if (!file_id.equals(other.file_id))
        {
            return false;
        }
        if (result_code == null)
        {
            if (other.result_code != null)
            {
                return false;
            }
        }
        else if (!result_code.equals(other.result_code))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("FileUploadRsp [result_code=");
        builder.append(result_code);
        builder.append(", file_id=");
        builder.append(file_id);
        builder.append("]");
        return builder.toString();
    }
    
}
