/*
 * 文 件 名:  MoveControl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  移动hadoop目录策略控制类
 * 创 建 人:  z00190465
 * 创建时间:  2013-1-14
 */
package com.huawei.bi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.exception.ExecuteException;
import com.huawei.bi.util.ComparetorSize;
import com.huawei.bi.util.ExeCommand;
import com.huawei.bi.util.ReadConfig;

/**
 * 移动hadoop目录策略控制类
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept Disk Balance V100R100, 2013-1-14]
 */
public class MoveControl
{
    /**
     * sh壳
     */
    private static final String SH = "/bin/sh";
    
    /**
     * -c参数
     */
    private static final String C = "-c";
    
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MoveControl.class);
    
    private List<DiskInfo> diskInfos = new ArrayList<DiskInfo>();
    
    private List<DirInfo> moveOutDirs;
    
    private List<MovePolicy> movePolicys;
    
    //最大磁盘空间阈值(单位字节)
    private long thresholdMax;
    
    //最小磁盘空间阈值(单位字节)
    private long thresholdMin;
    
    //磁盘差异空间阈值(单位字节)
    private long thresholdDiff;
    
    /**
     * 默认构造函数
     */
    public MoveControl()
    {
        moveOutDirs = new ArrayList<DirInfo>();
        movePolicys = new ArrayList<MovePolicy>();
    }
    
    public void setDiskInfo(List<DiskInfo> diskInfo)
    {
        this.diskInfos = diskInfo;
    }
    
    public List<DirInfo> getMoveOutDirs()
    {
        return moveOutDirs;
    }
    
    public void setMoveOutDirs(List<DirInfo> moveOutDirs)
    {
        this.moveOutDirs = moveOutDirs;
    }
    
    public void setMovePolicy(List<MovePolicy> movePolicy)
    {
        this.movePolicys = movePolicy;
    }
    
    /**
     * 将节点从hadoop集群离线
     *  @throws Exception 异常
     */
    public void quitHadoop() throws Exception
    {
        LOGGER.info("step 4. offline current node.");
        String[] command = new String[] { SH, C,
                ReadConfig.getProperties("node.offline.cmd") };
        ExeCommand.exeCommand(command);
    }
    
    /**
     * 将节点从hadoop集群上线
     * @throws Exception 异常
     */
    public void enterHadoop() throws Exception
    {
        LOGGER.info("step 7. online current node.");
        String[] command = new String[] { SH, C,
                ReadConfig.getProperties("node.online.cmd") };
        ExeCommand.exeCommand(command);
    }
    
    /**
     * 获取磁盘信息
     * @throws Exception 异常
     */
    public void getDiskInfo() throws Exception
    {
        LOGGER.info("step 2. get disk informations.");
        String exeShell = ReadConfig.getProperties("node.check.cmd");
        String exeResult = ReadConfig.getProperties("node.check.result");
        String[] command = new String[] {SH, C, exeShell + " " + exeResult + ";" };
        ExeCommand.exeCommand(command);
        
        getDiskInfo(new FileInputStream(exeResult));
    }
    
    /**
     * 获取磁盘信息
     * @throws Exception 异常
     * @param input 输入流
     */
    public void getDiskInfo(InputStream input) throws Exception
    {
        Map<String, DiskInfo> diskMap = new HashMap<String, DiskInfo>();
        Map<String, DirInfo> dirMap = new HashMap<String, DirInfo>();
        
        //读取磁盘文件
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        DiskInfo disk;
        DirInfo dirInfo;
        int index;
        String line = "";
        while ((line = in.readLine()) != null && line.trim().length() > 0)
        {
            //文件格式
            String[] info = line.split(",");
            index = 0;
            Long dirSize = Long.valueOf(info[index++]);
            String dirName = info[index++];
            Long diskAvail = Long.valueOf(info[index++]);
            
            index = 1;
            String diskName = dirName.substring(0,
                    dirName.indexOf("/", index + 1));
            
            //获取磁盘名和可用空间值的映射
            if (!diskMap.containsKey(diskName))
            {
                disk = new DiskInfo();
                disk.setSubDirInfo(new ArrayList<DirInfo>());
                disk.setIdleSize(diskAvail);
                disk.setRootDir(diskName);
                diskMap.put(diskName, disk);
                
                diskInfos.add(disk);
            }
            
            //获取每个子目录的信息,目录不能重复
            if (!dirMap.containsKey(dirName))
            {
                dirInfo = new DirInfo();
                dirInfo.setDirName(dirName);
                dirInfo.setSize(dirSize);
                dirInfo.setState(DirState.INIT);
                
                //获取磁盘信息
                disk = diskMap.get(diskName);
                disk.getSubDirInfo().add(dirInfo);
                //设置所属磁盘
                dirInfo.setDisk(disk);
                dirMap.put(dirName, dirInfo);
            }
            
        }
    }
    
    /**
     * 是否需要平衡
     * @return 是否需要平衡
     */
    public boolean isNeedBalance()
    {
        LOGGER.info("step 3. determine whether do need to balance disks.");
        if (null == diskInfos || diskInfos.isEmpty())
        {
            //可以肯定发生了异常，或者没有磁盘
            LOGGER.error("no disk was found!");
            return false;
        }
        
        boolean successed = false;
        long sumIdleSize = 0;
        List<Long> sizeList = new ArrayList<Long>();
        for (DiskInfo disk : diskInfos)
        {
            sizeList.add(disk.getIdleSize());
            //统计磁盘空间
            sumIdleSize += disk.getIdleSize();
        }
        
        //数据量很小，使用排序来取最大最小值也可以
        Collections.sort(sizeList);
        
        //最小磁盘剩余空间大小
        long minIdleSize = sizeList.get(0);
        //最大磁盘剩余空间大小
        long maxIdleSize = sizeList.get(sizeList.size() - 1);
        //最大磁盘空间差值
        long maxSizeDiff = maxIdleSize - minIdleSize;
        
        //平均空闲大小(忽略小数部分)
        long averageIdleSize = sumIdleSize / sizeList.size();
        //是否满足执行平衡的条件
        if (maxIdleSize > thresholdMax && minIdleSize < thresholdMin
                && maxSizeDiff > thresholdDiff)
        {
            for (DiskInfo disk : diskInfos)
            {
                if (disk.getIdleSize() > averageIdleSize)
                {
                    //移入设置为0
                    disk.setType(DiskType.MOVE_IN);
                    //设置调整值
                    disk.setAdjustSize(disk.getIdleSize() - averageIdleSize);
                    disk.orderSubDirByName();
                    
                }
                else
                {
                    //移出设置为1
                    disk.setType(DiskType.MOVE_OUT);
                    //设置调整值
                    disk.setAdjustSize(averageIdleSize - disk.getIdleSize());
                    disk.orderSubDirBySize();
                }
            }
            
            //TODO 磁盘不用排序
            //TODO Collections.sort(diskInfos, new ComparetorAdjust());
            successed = true;
            LOGGER.info(String.format("  need balance.[minIdleSize=%,d,maxIdleSize=%,d,maxSizeDiff=%,d]",
                    minIdleSize,
                    maxIdleSize,
                    maxSizeDiff));
        }
        else
        {
            successed = false;
            LOGGER.info(String.format("  needn't balance.[minIdleSize=%,d,maxIdleSize=%,d,maxSizeDiff=%,d]",
                    minIdleSize,
                    maxIdleSize,
                    maxSizeDiff));
        }
        return successed;
    }
    
    /**
     * 设定阈值
     * thresholdMax：最大剩余空间小于该值不移动
     * thresholdMin：最小剩余空间大于该值不移动
     * thresholdDiff：最大最小剩余空间的差值小于该值不移动
     * @param lThresholdMax 最大剩余空间阈值
     * @param lThresholdMin 最小剩余空间阈值
     * @param lDiffThreshold 最大差异空间阈值
     */
    public void initialize(long lThresholdMax, long lThresholdMin,
            long lDiffThreshold)
    {
        LOGGER.info("step 1. initialize...");
        if (lThresholdMax > 0)
        {
            this.thresholdMax = lThresholdMax;
        }
        if (lDiffThreshold > 0)
        {
            this.thresholdDiff = lDiffThreshold;
        }
        if (lThresholdMin > 0)
        {
            this.thresholdMin = lThresholdMin;
        }
        
        printThresholds();
    }
    
    /**
     * 格式化显示阈值信息
     */
    public void printThresholds()
    {
        LOGGER.info("--------------------------thresholds information-------------------------------");
        LOGGER.info(String.format("%55s%20s",
                "the threshold[ceil] of minimum idle size",
                thresholdMin));
        LOGGER.info(String.format("%55s%20s",
                "the threshold[floor] of maximum idle size",
                thresholdMax));
        LOGGER.info(String.format("%55s%20s",
                "the threshold[floor] of maximum difference size",
                thresholdDiff));
        LOGGER.info("--------------------------------------------------------------------------");
    }
    
    /**
     * 获取目录移动策略
     * @return 移动策略集合
     * @throws ExecuteException 异常
     */
    public List<MovePolicy> getMovePolicies() throws ExecuteException
    {
        LOGGER.info("step 5. get the moving policies.");
        long size;
        for (DiskInfo disk : diskInfos)
        {
            //移出的磁盘
            if (disk.getType() == DiskType.MOVE_OUT)
            {
                LOGGER.info("  start to process disk[rootDir={},diskType={}].",
                        disk.getRootDir(),
                        DiskType.toString(disk.getType()));
                
                //将每块磁盘所有合规的子目录放入移出链表
                while (true)
                {
                    size = disk.getAdjustSize();
                    DirInfo dir = disk.getNearestSubDir(size);
                    
                    if (null != dir)
                    {
                        moveOutDirs.add(dir);
                        
                        //修改磁盘调整值
                        disk.setAdjustSize(size - dir.getSize());
                        LOGGER.info(String.format("    dir[%s][%,d] can be moved out. "
                                + "adjust size of disk[%s] changed to [%,d]",
                                dir.getDirName(),
                                dir.getSize(),
                                disk.getRootDir(),
                                disk.getAdjustSize()));
                    }
                    else
                    {
                        break;
                    }
                }
                
                LOGGER.info("  finish to process disk[rootDir={},diskType={}].",
                        disk.getRootDir(),
                        DiskType.toString(disk.getType()));
            }
            
        }
        
        //按大小排序
        Collections.sort(moveOutDirs, new ComparetorSize());
        
        long adjustMax;
        for (DirInfo dir : moveOutDirs)
        {
            adjustMax = 0;
            DiskInfo diskAdjustMax = null;
            //确定adjust值最大的移入磁盘
            for (DiskInfo disk : diskInfos)
            {
                if (disk.getType() == DiskType.MOVE_IN
                        && disk.getAdjustSize() > adjustMax)
                {
                    adjustMax = disk.getAdjustSize();
                    diskAdjustMax = disk;
                }
            }
            
            //不存在有任何空余空间的磁盘
            if (null == diskAdjustMax)
            {
                LOGGER.error("not find any disk[MOVE_IN]!");
                throw new ExecuteException("not find any disk[MOVE_IN]!");
            }
            
            if (dir.getSize() > diskAdjustMax.getAdjustSize())
            {
                LOGGER.warn(String.format("the dir[%s][%,d] can't move out to the maximum adjustSize disk[%s,%s][%,d]"
                        + " because of too big space size.",
                        dir.getDirName(),
                        dir.getSize(),
                        diskAdjustMax.getRootDir(),
                        DiskType.toString(diskAdjustMax.getType()),
                        diskAdjustMax.getAdjustSize()));
                dir.setState(DirState.CANNOT_MOVE_OUT);
                continue;
            }
            
            //找到移入磁盘，构造移动策略
            dir.setState(DirState.FOUND_MOVE_IN_DISK);
            MovePolicy moveInfo = new MovePolicy();
            
            moveInfo.setSrcDir(dir.getDirName());
            //获取目标磁盘的下一个分区目录
            String nextPartitionDir = diskAdjustMax.getNextPartitionDir();
            moveInfo.setDestDir(nextPartitionDir);
            // 移动链表
            movePolicys.add(moveInfo);
            // 刷新磁盘调整空间
            diskAdjustMax.setAdjustSize(diskAdjustMax.getAdjustSize()
                    - dir.getSize());
            //修改剩余空间大小
            diskAdjustMax.setIdleSize(diskAdjustMax.getIdleSize()
                    - dir.getSize());
            dir.getDisk().setIdleSize(dir.getDisk().getIdleSize()
                    + dir.getSize());
            LOGGER.info(String.format("get one movePolicy[%s --> %s][%,d], adjust size of disk[%s] changed to [%,d]",
                    moveInfo.getSrcDir(),
                    moveInfo.getDestDir(),
                    dir.getSize(),
                    diskAdjustMax.getRootDir(),
                    diskAdjustMax.getAdjustSize()));
            
            //TODO 无需做下面的信息维护
            DirInfo dirInfo = new DirInfo();
            dirInfo.setDirName(nextPartitionDir);
            dirInfo.setSize(dir.getSize());
            dirInfo.setState(DirState.INIT);
            diskAdjustMax.getSubDirInfo().add(dirInfo);
        }
        
        return movePolicys;
    }
    
    /**
     * 执行move命令
     * @throws Exception 异常
     */
    public void executeMove() throws Exception
    {
        LOGGER.info("step 6. execute the moving policies.");
        for (MovePolicy movePolicy : movePolicys)
        {
            //构造mv命令
            String moveCmd = String.format("mv %s %s",
                    movePolicy.getSrcDir(),
                    movePolicy.getDestDir());
            
            LOGGER.info("  start to execute cmd[{}]", moveCmd);
            String[] commands = new String[] { SH, C, moveCmd };
            ExeCommand.exeCommand(commands);
        }
    }
    
    /**
     * 格式化显示当前磁盘信息
     * @return 返回磁盘的平衡度
     */
    public double printDisks()
    {
        LOGGER.info("--------------------------disk information-------------------------------");
        LOGGER.info(String.format("%15s%15s%20s%20s",
                "root dir",
                "disk type",
                "idle size",
                "adjust size"));
        
        long sumIdleSize = 0;
        for (DiskInfo disk : diskInfos)
        {
            //统计磁盘空间
            sumIdleSize += disk.getIdleSize();
        }
        
        long averageIdleSize = sumIdleSize / diskInfos.size();
        double balanceVar = 0;
        long adjustSize;
        //显示磁盘当前剩余空间大小
        for (DiskInfo disk : diskInfos)
        {
            adjustSize = averageIdleSize - disk.getIdleSize();
            LOGGER.info(String.format("%15s%15s%,20d%,20d",
                    disk.getRootDir(),
                    DiskType.toString(adjustSize >= 0 ? DiskType.MOVE_OUT
                            : DiskType.MOVE_IN),
                    disk.getIdleSize(),
                    Math.abs(adjustSize)));
            balanceVar += (double) adjustSize * adjustSize;
        }
        
        //计算平衡度
        balanceVar = Math.sqrt(balanceVar) / diskInfos.size();
        LOGGER.info(String.format("  the measure of disk balance is %,.2f",
                balanceVar));
        LOGGER.info("--------------------------------------------------------------------------");
        
        return balanceVar;
    }
}
