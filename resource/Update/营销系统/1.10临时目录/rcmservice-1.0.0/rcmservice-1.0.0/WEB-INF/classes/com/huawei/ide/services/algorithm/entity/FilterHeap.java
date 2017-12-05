package com.huawei.ide.services.algorithm.entity;

import com.huawei.ide.interceptors.res.rcm.CommonUtils;
import com.huawei.ide.interceptors.res.rcm.RcmServiceConstants;

/**
 * 
 * FilterHeap
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see  [相关类/方法]
 */
public class FilterHeap
{
    
    /**
    * 应用信息数组
    */
    private AppHeapEntity[] appHeap;
    
    /**
     * 堆的大小
     */
    private int size;
    
    /**
     * 堆的最大值
     */
    private int maxSize;
    
    /**
     * <默认构造函数>
     */
    public FilterHeap()
    {
        this.size = 0;
        
        String theMaxSize = CommonUtils.getSysConfigValueByKey(RcmServiceConstants.CONF_SIZEOFRECOMMENDEDLIST);
        
        int defaultMaxSize = Integer.valueOf(theMaxSize);
        this.maxSize = defaultMaxSize;
        this.appHeap = new AppHeapEntity[defaultMaxSize];
    }
    
    /**
     * 构造函数
     * @param n  n
     */
    public FilterHeap(int n)
    {
        this.size = 0;
        this.maxSize = n;
        this.appHeap = new AppHeapEntity[n];
    }
    
    /**
     * <插入一个应用>
     * @param app     一个应用
     * @see [类、类#方法、类#成员]
     */
    public void insert(AppHeapEntity app)
    {
        if (this.maxSize > this.size)
        {
            this.appHeap[this.size] = app;
            this.shiftUp(this.size++);
        }
        else
        {
            if (app.getWeight() > this.getHeapTop())
            {
                this.appHeap[0] = app;
                this.shiftDown(0);
            }
        }
    }
    
    private void shiftUp(int pos)
    {
        int parentIdx = this.getParentIndex(pos);
        while (pos != 0 && (this.appHeap[pos].getWeight() < this.appHeap[parentIdx].getWeight()))
        {
            //swap
            this.swap(pos, parentIdx);
            pos = parentIdx;
            parentIdx = this.getParentIndex(pos);
        }
    }
    
    private void shiftDown(int pos)
    {
        int leftChildIdx = this.getLeftChildIdx(pos);
        if (leftChildIdx >= this.size)
        {
            return;
        }
        int rightChildIdx = this.getRightChildIdx(pos);
        int toBeSwapIdx = leftChildIdx;
        if (rightChildIdx < this.size
            && this.appHeap[leftChildIdx].getWeight() > this.appHeap[rightChildIdx].getWeight())
        {
            toBeSwapIdx = rightChildIdx;
        }
        //swap
        if (this.appHeap[pos].getWeight() > this.appHeap[toBeSwapIdx].getWeight())
        {
            this.swap(pos, toBeSwapIdx);
            this.shiftDown(toBeSwapIdx);
        }
    }
    
    private double getHeapTop()
    {
        return this.appHeap[0].getWeight();
    }
    
    private int getParentIndex(int pos)
    {
        return (pos - 1) / 2;
    }
    
    private int getLeftChildIdx(int pos)
    {
        return pos * 2 + 1;
    }
    
    private int getRightChildIdx(int pos)
    {
        return pos * 2 + 2;
    }
    
    private void swap(int idx1, int idx2)
    {
        AppHeapEntity tmp = this.appHeap[idx1];
        this.appHeap[idx1] = this.appHeap[idx2];
        this.appHeap[idx2] = tmp;
    }
    
    /**
     * <将堆中的应用按照数组形式从大到小返回>
     * @return    最中TopN的应用数组
     * @see [类、类#方法、类#成员]
     */
    public AppHeapEntity[] generateList()
    {
        for (int i = this.size - 1; i > 0; i--)
        {
            this.size--;
            this.swap(0, i);
            this.shiftDown(0);
        }
        if (this.appHeap == null)
        {
            return new AppHeapEntity[] {};
        }
        return (AppHeapEntity[])this.appHeap.clone();
        // return this.appHeap;
    }
    
}
