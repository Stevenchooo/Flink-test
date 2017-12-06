/*
 * 文 件 名:  Log2DBQueryParam.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2011-12-22
 */
package com.huawei.platform.tcc.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 日志查询参数
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2011-12-22]
 * @see  [相关类/方法]
 */
public class LongTimeShellParam
{
    /**
     * 分页开始索引
     */
    private Long startIndex;
    
    /**
     * 行数
     */
    private Integer rows;
    
    /**
     * 页数
     */
    private Integer page;
    
    /**
     * 阈值
     */
    private Integer threshold;
    
    /**
     * 开始时间
     */
    private String startTime;
    
    private List<String> visibleSTgs = new ArrayList<String>();
    
    /**
     * 结束时间
     */
    private String endTime;
    
    private Date currentTime;

    public Long getStartIndex()
    {
        return startIndex;
    }

    public void setStartIndex(Long startIndex)
    {
        this.startIndex = startIndex;
    }

    public Integer getRows()
    {
        return rows;
    }

    public void setRows(Integer rows)
    {
        this.rows = rows;
    }

    public Integer getPage()
    {
        return page;
    }

    public void setPage(Integer page)
    {
        this.page = page;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public Integer getThreshold()
    {
        return threshold;
    }

    public void setThreshold(Integer threshold)
    {
        this.threshold = threshold;
    }

    public Date getCurrentTime()
    {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime)
    {
        this.currentTime = currentTime;
    }

    public List<String> getVisibleSTgs()
    {
        return visibleSTgs;
    }

    public void setVisibleSTgs(List<String> visibleSTgs)
    {
        this.visibleSTgs = visibleSTgs;
    }
}
