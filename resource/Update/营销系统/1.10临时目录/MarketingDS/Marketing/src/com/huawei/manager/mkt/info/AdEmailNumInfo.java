/*
 * 文 件 名:  AdEmailNumInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-10-14
 */
package com.huawei.manager.mkt.info;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-10-14]
 * @see  [相关类/方法]
 */
public class AdEmailNumInfo
{
    //总记录数目
    private Integer totalNum;
    
    //vmall记录数
    private Integer vmallNum;
    
    //荣耀官网记录数
    private Integer honorNum;
    
    //华为官网记录数
    private Integer huaweiNum;

    public Integer getTotalNum()
    {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum)
    {
        this.totalNum = totalNum;
    }

    public Integer getVmallNum()
    {
        return vmallNum;
    }

    public void setVmallNum(Integer vmallNum)
    {
        this.vmallNum = vmallNum;
    }

    public Integer getHonorNum()
    {
        return honorNum;
    }

    public void setHonorNum(Integer honorNum)
    {
        this.honorNum = honorNum;
    }

    public Integer getHuaweiNum()
    {
        return huaweiNum;
    }

    public void setHuaweiNum(Integer huaweiNum)
    {
        this.huaweiNum = huaweiNum;
    }
    
    
    
}
