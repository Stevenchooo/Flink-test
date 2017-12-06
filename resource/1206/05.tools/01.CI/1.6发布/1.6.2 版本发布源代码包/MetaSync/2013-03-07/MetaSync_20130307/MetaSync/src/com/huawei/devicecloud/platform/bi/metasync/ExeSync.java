/*
 * 文 件 名:  ExeSync.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  同步操作类
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-28
 */
package com.huawei.devicecloud.platform.bi.metasync;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.metasync.conn.DbBean;
import com.huawei.devicecloud.platform.bi.metasync.exception.ArgumentException;
import com.huawei.devicecloud.platform.bi.metasync.model.PartitionKeyValMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.PartitionKeysMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.PartitionMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.SdsMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.SequenceInfo;
import com.huawei.devicecloud.platform.bi.metasync.model.SyncArgs;
import com.huawei.devicecloud.platform.bi.metasync.model.TableMeta;

/**
 * 同步操作类
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-6]
 */
public class ExeSync
{
    private static String log4J = "conf/log4j.properties";
    
    static
    {
        PropertyConfigurator.configure(log4J);
    }
    
    private static final int TBL_IDX = 0;
    
    private static final int PT_IDX = 1;
    
    private static final int PT_MIN_VAL_IDX = 2;
    
    private static final int PT_MAX_VAL_IDX = 3;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ExeSync.class);
    
    private DbBean dbBean = new DbBean();
    
    private Connection srcConn = dbBean.getConnection(true);
    
    private Connection dstConn = dbBean.getConnection(false);
    
    private ExeQuery exeQuery = new ExeQuery(srcConn, dstConn);
    
    private ExeInsert exeInsert = new ExeInsert(srcConn, dstConn);
    
    private Long srcDbId;
    
    private Long dstDbId;
    
    private String osUser;
    
    /**
     * 默认构造函数
     * @param osUser 操作系统用户
     * @param srcDbName 源数据库名
     * @param dstDbName 目标数据库名
     * @throws Exception 异常
     */
    public ExeSync(String osUser, String srcDbName, String dstDbName)
        throws Exception
    {
        this.osUser = osUser;
        srcDbId = exeQuery.getDbId(true, srcDbName);
        dstDbId = exeQuery.getDbId(false, dstDbName);
        if (null == srcDbId || null == dstDbId)
        {
            LOGGER.error("srcDBName[{}] or dstDBName[{}] don't exist!", srcDbName, dstDbName);
            throw new ArgumentException("srcDBName[" + srcDbName + "] or dstDBName[" + dstDbName + "] don't exist!");
        }
    }
    
    /**
     * 获取可以同步的表元数据集合
     * @param filterParams 过滤参数结合
     * @param forceDel 是否强制删除存在的目标表
     * @return 可以同步的表元数据集合
     * @throws Exception 异常
     */
    private List<TableMeta> getCanSyncTblMetas(Map<String, FilterParam> filterParams, boolean forceDel)
        throws Exception
    {
        if (null == filterParams || filterParams.isEmpty())
        {
            return new ArrayList<TableMeta>(0);
        }
        
        List<String> tblNames = new ArrayList<String>();
        for (String tblName : filterParams.keySet())
        {
            tblNames.add(tblName);
        }
        
        TableMeta tblMetaVal = new TableMeta();
        //获取表元数据
        List<TableMeta> srcTblMetas = exeQuery.getTblMetas(true, srcDbId, tblNames);
        if (null == srcTblMetas || srcTblMetas.isEmpty())
        {
            LOGGER.info("no any source table exist! tblNames is {}.", tblNames);
            return new ArrayList<TableMeta>(0);
        }
        else
        {
            //输出日志信息
            for (String tblName : tblNames)
            {
                tblMetaVal.setTblName(tblName);
                if (!srcTblMetas.contains(tblMetaVal))
                {
                    LOGGER.info("source table[{}] not exist! ignore it.", tblName);
                }
            }
        }
        
        List<TableMeta> dstTblMetas = exeQuery.getSingleTblMetas(false, dstDbId, tblNames);
        //获取目标存在的表，提示警告
        if (null != dstTblMetas && !dstTblMetas.isEmpty())
        {
            Iterator<TableMeta> srcTblIter = srcTblMetas.iterator();
            String tblName;
            
            if (forceDel)
            {
                List<Long> existDstTblIds = new ArrayList<Long>();
                List<String> existDstTblNames = new ArrayList<String>();
                for (TableMeta tblMeta : dstTblMetas)
                {
                    existDstTblIds.add(tblMeta.getTblId());
                    existDstTblNames.add(tblMeta.getTblName());
                }
                
                LOGGER.info("force to delete destination tables[{}] startting.", existDstTblNames);
                //删除存在的表
                exeInsert.deleteTblRelations(existDstTblIds);
                LOGGER.info("finish to force to delete the destination tables finished.");
            }
            else
            {
                //从源表中删除
                while (srcTblIter.hasNext())
                {
                    tblName = srcTblIter.next().getTblName();
                    tblMetaVal.setTblName(tblName);
                    if (dstTblMetas.contains(tblMetaVal))
                    {
                        srcTblIter.remove();
                    }
                }
                
                //显示提示信息
                List<String> existDstTblNames = new ArrayList<String>();
                for (TableMeta tblMeta : dstTblMetas)
                {
                    existDstTblNames.add(tblMeta.getTblName());
                }
                LOGGER.info("please delete the destination tables[{}] first. ignore it!", existDstTblNames);
            }
        }
        
        return srcTblMetas;
    }
    
    /**
     * 执行同步
     * @param filterParams 同步过滤参数集合
     * @param forceDel 是否强制删除
     * @throws Exception 异常
     */
    public void exeSync(Map<String, FilterParam> filterParams, boolean forceDel)
        throws Exception
    {
        LOGGER.info("step 2. exeSync. filterParams is {}!", filterParams);
        
        LOGGER.info("step 2.1. get all can sync tableMetas. size of filterParams is {}!", filterParams.size());
        //获取可以同步的表元数据集合
        List<TableMeta> tblMetas = getCanSyncTblMetas(filterParams, forceDel);
        if (null == tblMetas || tblMetas.isEmpty())
        {
            LOGGER.info("no any source table needs sync!");
            return;
        }
        
        LOGGER.info("step 2.2. get all partition metas. size of tblMetas is {}!", tblMetas.size());
        FilterParam filterParam;
        List<PartitionMeta> partitions = new ArrayList<PartitionMeta>();
        List<PartitionMeta> singleTblParts;
        //为每个表读取分区
        for (TableMeta tblMeta : tblMetas)
        {
            filterParam = filterParams.get(tblMeta.getTblName());
            singleTblParts =
                exeQuery.getPartitionMetas(tblMeta.getTblId(),
                    filterParam.getComPartName(),
                    filterParam.getMinPartValue(),
                    filterParam.getMaxPartValue());
            partitions.addAll(singleTblParts);
        }
        
        //id映射集合
        Map<Long, Long> tblIdMapping = new HashMap<Long, Long>();
        Map<Long, Long> partIdMapping = new HashMap<Long, Long>();
        Map<Long, Long> sdIdMapping = new HashMap<Long, Long>();
        Map<Long, Long> serIdMapping = new HashMap<Long, Long>();
        
        LOGGER.info("step 2.3. reserve Sequence. tblNum={}, partNum={}!", tblMetas.size(), partitions.size());
        //获取预留的Id信息
        SequenceInfo fSeq = exeInsert.reserveSequence(tblMetas.size(), partitions.size());
        List<Long> tblIds = new ArrayList<Long>();
        List<Long> partIds = new ArrayList<Long>();
        List<Long> sdIds = new ArrayList<Long>();
        List<Long> serIds = new ArrayList<Long>();
        //构造Id映射关系
        for (TableMeta tblMeta : tblMetas)
        {
            tblIds.add(tblMeta.getTblId());
            sdIds.add(tblMeta.getSdId());
            
            tblIdMapping.put(tblMeta.getTblId(), fSeq.incAndGetTId());
            sdIdMapping.put(tblMeta.getSdId(), fSeq.incAndGetSdId());
        }
        
        //构造Id映射关系
        for (PartitionMeta partition : partitions)
        {
            partIds.add(partition.getPartId());
            sdIds.add(partition.getSdId());
            
            partIdMapping.put(partition.getPartId(), fSeq.incAndGetPId());
            sdIdMapping.put(partition.getSdId(), fSeq.incAndGetSdId());
        }
        
        LOGGER.info("step 2.4. get all metas of sds. size of sdIds is {}!", sdIds.size());
        List<SdsMeta> sdsMetas = exeQuery.getSdsMetas(sdIds);
        //构造Id映射关系
        for (SdsMeta sdsMeta : sdsMetas)
        {
            serIds.add(sdsMeta.getSerdeId());
            serIdMapping.put(sdsMeta.getSerdeId(), fSeq.incAndGetSerId());
        }
        
        //获取所有的分区
        List<PartitionKeysMeta> pks = exeQuery.getPartitionKeys(tblIds);
        //获取存在分区的表
        List<Long> existPTblIds = new ArrayList<Long>();
        for (PartitionKeysMeta pk : pks)
        {
            if (!existPTblIds.contains(pk.getTblId()))
            {
                existPTblIds.add(pk.getTblId());
            }
        }
        
        //分区表的sd与表名关系
        Map<Long, String> existPTSdIdNames = new HashMap<Long, String>();
        //构造Id映射关系
        for (TableMeta tblMeta : tblMetas)
        {
            if (existPTblIds.contains(tblMeta.getTblId()))
            {
                existPTSdIdNames.put(tblMeta.getSdId(), tblMeta.getTblName());
            }
        }
        
        LOGGER.info("step 2.5. sync metas of Serdes. size of serIds is {}!", serIds.size());
        //serdes表数据
        exeInsert.syncSerdesMetas(serIds, serIdMapping);
        
        LOGGER.info("step 2.6. sync metas of serde parm. size of serIds is {}!", serIds.size());
        //serde_params表
        exeInsert.syncSerdeParmMetas(serIds, serIdMapping);
        
        LOGGER.info("step 2.7. insert metas of sds. size of sdsMetas is {}!", sdsMetas.size());
        //sds表数据
        exeInsert.insertSdsMetas(this.osUser, sdsMetas, sdIdMapping, serIdMapping, existPTSdIdNames);
        
        LOGGER.info("step 2.8. insert metas of table. size of tblMetas is {}!", tblMetas.size());
        //tbls表数据
        exeInsert.insertTblMeta(tblMetas, dstDbId, this.osUser, tblIdMapping, sdIdMapping);
        
        LOGGER.info("step 2.9. sync metas of table param. size of tblIds is {}!", tblIds.size());
        //table_params表数据
        exeInsert.syncTableParmMeta(tblIds, tblIdMapping);
        
        LOGGER.info("step 2.10. sync metas of partition keys. size of tblIds is {}!", tblIds.size());
        //partition_key表数据
        exeInsert.insertPartitionKeysMetas(pks, tblIdMapping);
        
        LOGGER.info("step 2.11. sync metas of columns. size of sdIds is {}!", sdIds.size());
        //columns表
        exeInsert.syncColumnsMetas(sdIds, sdIdMapping);
        
        LOGGER.info("step 2.12. insert metas of partition. size of partitions is {}!", partitions.size());
        //partitions表数据
        exeInsert.insertPartitions(partitions, tblIdMapping, partIdMapping, sdIdMapping);
        
        LOGGER.info("step 2.13. insert metas of partition parm. size of partIds is {}!", partIds.size());
        //partition_params表
        exeInsert.syncPartitionParms(partIds, partIdMapping);
        
        LOGGER.info("step 2.14. get metas of partition key's value. size of tblIds is {}!", tblMetas.size());
        //partition_key_vals表数据
        //读取所有的key_val集合
        List<PartitionKeyValMeta> partKVMetas = exeQuery.getPartitionKeyVals(partIds);
        LOGGER.info("step 2.15. insert metas of partition key's value. size of partKVMetas is {}!", partKVMetas.size());
        exeInsert.insertPartitionKeyVals(partKVMetas, partIdMapping);
        
    }
    
    private static SyncArgs parse(String[] args)
        throws Exception
    {
        if (LOGGER.isInfoEnabled())
        {
            StringBuilder sb = new StringBuilder();
            if (null != args)
            {
                for (String str : args)
                {
                    sb.append(str);
                    sb.append(' ');
                }
            }
            LOGGER.info("step 1. parse args[{}]!", sb.toString());
        }
        
        SyncArgs syncArgs = new SyncArgs();
        syncArgs.setSrcDbName("default");
        syncArgs.setDstDbName("default");
        syncArgs.setOsUser(System.getProperty("user.name"));
        
        if (null == args || args.length == 0)
        {
            LOGGER.error("metasync: usage: java -jar metasync.jar [-f] [--src-db dbname]"
                + " [--dst-db dbname] table-ext1 [table-ext2 ...]");
            LOGGER.error("table-extn: table-name[,compare-pt,min-pt-value[,max-pt-value]]");
            
            //直接退出
            System.exit(1);
        }
        //参数存在
        else
        {
            int i = 0;
            String[] tableExt;
            while (i < args.length)
            {
                if ("-f".equalsIgnoreCase(args[i]))
                {
                    syncArgs.setForceDelete(true);
                }
                else if ("--src-db".equalsIgnoreCase(args[i]))
                {
                    if (i + 1 >= args.length)
                    {
                        LOGGER.warn("option[--src-db] needs the value. ignore it!");
                    }
                    else
                    {
                        i++;
                        syncArgs.setSrcDbName(args[i]);
                    }
                }
                else if ("--dst-db".equalsIgnoreCase(args[i]))
                {
                    if (i + 1 >= args.length)
                    {
                        LOGGER.warn("option[--dst-db] needs the value. ignore it!");
                    }
                    else
                    {
                        i++;
                        syncArgs.setDstDbName(args[i]);
                    }
                }
                else
                {
                    FilterParam filter = new FilterParam();
                    tableExt = args[i].split(",");
                    if (tableExt.length == TBL_IDX + 1)
                    {
                        if (StringUtils.isBlank(tableExt[TBL_IDX]))
                        {
                            LOGGER.error("error argument of table-ext{}[{}], ignore it!", i, args[i]);
                            continue;
                        }
                        
                        filter.setTblName(tableExt[TBL_IDX]);
                    }
                    else if (tableExt.length == PT_MIN_VAL_IDX + 1)
                    {
                        if (StringUtils.isBlank(tableExt[TBL_IDX]) || StringUtils.isBlank(tableExt[PT_IDX]))
                        {
                            LOGGER.error("error argument of table-ext{}[{}], ignore it!", i, args[i]);
                            continue;
                        }
                        
                        filter.setTblName(tableExt[TBL_IDX]);
                        filter.setComPartName(tableExt[PT_IDX]);
                        filter.setMinPartValue(tableExt[PT_MIN_VAL_IDX]);
                    }
                    else if (tableExt.length == PT_MAX_VAL_IDX + 1)
                    {
                        if (StringUtils.isBlank(tableExt[TBL_IDX]) || StringUtils.isBlank(tableExt[PT_IDX]))
                        {
                            LOGGER.error("error argument of table-ext{}[{}], ignore it!", i, args[i]);
                            continue;
                        }
                        
                        filter.setTblName(tableExt[TBL_IDX]);
                        filter.setComPartName(tableExt[PT_IDX]);
                        filter.setMinPartValue(tableExt[PT_MIN_VAL_IDX]);
                        filter.setMaxPartValue(tableExt[PT_MAX_VAL_IDX]);
                    }
                    else
                    {
                        LOGGER.error("error argument of table-ext{}[{}], ignore it!", i, args[i]);
                        continue;
                    }
                    
                    syncArgs.getFilters().put(filter.getTblName(), filter);
                    
                }
                
                i++;
            }
            
            if (syncArgs.getFilters().isEmpty())
            {
                LOGGER.error("metasync: usage: java -jar metasync.jar [-f] [--src-db dbname]"
                    + " [--dst-db dbname] table-ext1 [table-ext2 ...]");
                LOGGER.error("table-extn: table-name[,compare-pt,min-pt-value[,max-pt-value]]");
                
                //直接退出
                System.exit(1);
            }
        }
        
        return syncArgs;
    }
    
    /**
     * 入口
     * @param args 参数
     */
    public static void main(String[] args)
    {
        // 记录调度开始时间
        Long startTime = new Date().getTime();
        ExeSync exeSync = null;
        boolean success = true;
        try
        {
            //分离参数
            SyncArgs syncArgs = parse(args);
            exeSync = new ExeSync(syncArgs.getOsUser(), syncArgs.getSrcDbName(), syncArgs.getDstDbName());
            //执行同步
            exeSync.exeSync(syncArgs.getFilters(), syncArgs.isForceDelete());
        }
        catch (Exception e)
        {
            LOGGER.error("metasync failed!", e);
            success = false;
        }
        finally
        {
            if (null != exeSync)
            {
                DbBean.close(exeSync.srcConn);
                DbBean.close(exeSync.dstConn);
            }
        }
        
        // 记录调度结束时间
        Long endTime = new Date().getTime();
        Long executeDuration = endTime - startTime;
        LOGGER.info("execute {} ms!", executeDuration);
        
        if (success)
        {
            System.exit(0);
        }
        else
        {
            System.exit(1);
        }
    }
}
