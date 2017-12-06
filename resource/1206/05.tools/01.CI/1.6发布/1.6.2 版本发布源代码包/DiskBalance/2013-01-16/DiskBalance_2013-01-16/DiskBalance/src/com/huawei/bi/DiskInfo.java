/*
 * 文 件 名:  DiskInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  磁盘信息
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-14
 */
package com.huawei.bi;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.util.ComparetorName;
import com.huawei.bi.util.ComparetorSize;
import com.huawei.bi.util.ReadConfig;

/**
 * 
 * 磁盘信息
 * 
 * @author  z00190554
 * @version [Device Cloud Base Platform Dept Disk Balance V100R100, 2013-1-9]
 */
public class DiskInfo
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DiskInfo.class);
    
    private long adjustSize;
    
    private long idleSize;
    
    private Integer maxPartitionID;
    
    private String rootDir;
    
    private List<DirInfo> subDirInfos;
    
    private int type;
    
    public long getAdjustSize()
    {
        return adjustSize;
    }
    
    public void setAdjustSize(long adjustSize)
    {
        this.adjustSize = adjustSize;
    }
    
    public long getIdleSize()
    {
        return idleSize;
    }
    
    public void setIdleSize(long idleSize)
    {
        this.idleSize = idleSize;
    }
    
    public String getRootDir()
    {
        return rootDir;
    }
    
    public void setRootDir(String rootDir)
    {
        this.rootDir = rootDir;
    }
    
    public List<DirInfo> getSubDirInfo()
    {
        return subDirInfos;
    }
    
    public void setSubDirInfo(List<DirInfo> subDirs)
    {
        this.subDirInfos = subDirs;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    /**
     * 依次对降序的文件目录进行匹配，找到最接近的( < size)的目录
     * @param size 大小
     * @return DirInfo
     */
    public DirInfo getNearestSubDir(long size)
    {
        //subDirInfos目录需要按占用空间降序排列
        for (DirInfo dirInfo : subDirInfos)
        {
            if (dirInfo.getState() == DirState.INIT
                    && dirInfo.getSize() <= size)
            {
                dirInfo.setState(DirState.WOULD_MOVE_OUT);
                return dirInfo;
            }
        }
        
        return null;
    }
    
    /**
     *  获取该磁盘下一个移入目录的名字
     * @return 下一个移入的目录名
     */
    
    public int getNextPartitionID()
    {
        if (null == maxPartitionID)
        {
            int maxPID = 0;
            
            //子目录存在
            if (null != subDirInfos && !subDirInfos.isEmpty())
            {
                //中缀
                String infix = ReadConfig.getProperties("node.pdir.infix");
                for (DirInfo dir : subDirInfos)
                {
                    String dirName = dir.getDirName();
                    int pId = Integer.parseInt(dirName.substring(dirName.lastIndexOf(infix)
                            + infix.length(),
                            dirName.length()));
                    if (pId > maxPID)
                    {
                        maxPID = pId;
                    }
                }
            }
            else
            {
                LOGGER.info("disk[{}] don't have any partition subdirs!",
                        rootDir);
            }
            
            maxPartitionID = maxPID;
        }
        
        maxPartitionID += 1;
        
        return maxPartitionID;
    }
    
    /**
     * 获取磁盘的下一个分区目录
     * @return 磁盘的下一个分区目录
     */
    public String getNextPartitionDir()
    {
        //中缀
        String infix = ReadConfig.getProperties("node.pdir.infix");
        //只是对Name排序
        return String.format("%s%s%d", rootDir, infix, getNextPartitionID());
    }
    
    /**
     * 对子目录安装名字排序
     */
    public void orderSubDirByName()
    {
        Collections.sort(subDirInfos, new ComparetorName());
    }
    
    /**
     * 对子目录按大小进行降序排列
     */
    public void orderSubDirBySize()
    {
        Collections.sort(subDirInfos, new ComparetorSize());
    }
}
