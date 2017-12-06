/*
 * 文 件 名:  ExeInsert.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  修改操作
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-4
 */
package com.huawei.devicecloud.platform.bi.metasync;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.metasync.conn.DbBean;
import com.huawei.devicecloud.platform.bi.metasync.model.ColumnsMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.PartitionKeyValMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.PartitionKeysMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.PartitionMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.PartitionParmMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.SdsMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.SequenceInfo;
import com.huawei.devicecloud.platform.bi.metasync.model.SerdeParmMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.SerdesMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.TableMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.TableParmMeta;
import com.huawei.devicecloud.platform.bi.metasync.util.ReadConfig;

/**
 * 修改操作
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-6]
 */
public class ExeInsert
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExeInsert.class);
    
    private ExeQuery exeQuery;
    
    private Connection dstConn;
    
    /**
     * 构造函数
     * @param srcConn 源连接
     * @param dstConn 目标连接
     */
    public ExeInsert(Connection srcConn, Connection dstConn)
    {
        this.dstConn = dstConn;
        exeQuery = new ExeQuery(srcConn, dstConn);
    }
    
    /**
     * 同步列元数据
     * @param sdIds 源Sd标识集合
     * @param sdIdMapping Sd标识映射
     * @throws Exception 异常
     */
    public void syncColumnsMetas(List<Long> sdIds, Map<Long, Long> sdIdMapping)
        throws Exception
    {
        PreparedStatement ps = null;
        List<ColumnsMeta> cList = exeQuery.getColumnsMetas(sdIds);
        if (null == cList || cList.isEmpty())
        {
            return;
        }
        
        StringBuilder sql = new StringBuilder();
        sql.append("insert into columns VALUES");
        for (ColumnsMeta cM : cList)
        {
            sql.append('(');
            sql.append(sdIdMapping.get(cM.getSdId()));
            sql.append(',');
            sql.append(cM.getComment() == null ? "NULL" : "'" + cM.getComment() + "'");
            sql.append(',');
            sql.append(cM.getColumnName() == null ? "NULL" : "'" + cM.getColumnName() + "'");
            sql.append(',');
            sql.append(cM.getTypeName() == null ? "NULL" : "'" + cM.getTypeName() + "'");
            sql.append(',');
            sql.append(cM.getIntegerIdx());
            sql.append("),");
        }
        sql.deleteCharAt(sql.length() - 1);
        
        try
        {
            ps = dstConn.prepareStatement(sql.toString());
            ps.executeUpdate();
            LOGGER.info("Insert data into columns.");
        }
        catch (Exception e)
        {
            LOGGER.error("execute " + sql + " error.", e);
            throw e;
        }
        finally
        {
            DbBean.close(ps);
        }
    }
    
    /**
     * 预留Id
     * @param tblNum 预留的表个数
     * @param partNum 预留的分区个数
     * @return 序号信息
     * @throws Exception 异常
     */
    public SequenceInfo reserveSequence(int tblNum, int partNum)
        throws Exception
    {
        SequenceInfo seqInfo = new SequenceInfo();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "call reserveSequence(?,?)";
        try
        {
            ps = dstConn.prepareStatement(sql);
            int index = 1;
            ps.setInt(index++, tblNum);
            ps.setInt(index, partNum);
            
            rs = ps.executeQuery();
            
            if (rs.next())
            {
                seqInfo.setTId(rs.getLong("tId"));
                seqInfo.setPId(rs.getLong("pId"));
                seqInfo.setSerId(rs.getLong("serId"));
                seqInfo.setSdId(rs.getLong("sdId"));
                
                //回填到起始值
                seqInfo.setTId(seqInfo.getTId() - tblNum);
                seqInfo.setPId(seqInfo.getPId() - partNum);
                seqInfo.setSerId(seqInfo.getSerId() - tblNum - partNum);
                seqInfo.setSdId(seqInfo.getSdId() - tblNum - partNum);
                
                return seqInfo;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("execute " + sql + " error.", e);
            throw e;
        }
        finally
        {
            DbBean.close(ps);
        }
        
        return null;
    }
    
    /**
     * 同步分区键元数据
     * @param pkList 源表分区集合
     * @param tblIdMapping 表id映射
     * @throws Exception 异常
     */
    public void insertPartitionKeysMetas(List<PartitionKeysMeta> pkList, Map<Long, Long> tblIdMapping)
        throws Exception
    {
        PreparedStatement ps = null;
        if (null == pkList || pkList.isEmpty())
        {
            return;
        }
        
        StringBuilder sql = new StringBuilder();
        sql.append("insert into partition_keys(TBL_ID,PKEY_COMMENT,PKEY_NAME,PKEY_TYPE,INTEGER_IDX) VALUES");
        for (PartitionKeysMeta pk : pkList)
        {
            sql.append('(');
            sql.append(tblIdMapping.get(pk.getTblId()));
            sql.append(',');
            sql.append(pk.getPkeyComment() == null ? "NULL" : "'" + pk.getPkeyComment() + "'");
            sql.append(',');
            sql.append(pk.getPkeyName() == null ? "NULL" : "'" + pk.getPkeyName() + "'");
            sql.append(',');
            sql.append(pk.getPkeyType() == null ? "NULL" : "'" + pk.getPkeyType() + "'");
            sql.append(',');
            sql.append(pk.getIntegerIdx());
            sql.append("),");
        }
        sql.deleteCharAt(sql.length() - 1);
        
        try
        {
            ps = dstConn.prepareStatement(sql.toString());
            ps.executeUpdate();
            
            LOGGER.info("Insert data into partition_keys.");
        }
        catch (Exception e)
        {
            LOGGER.error("execute " + sql + " error.", e);
            throw e;
        }
        finally
        {
            DbBean.close(ps);
        }
    }
    
    /**
     * 插入分区键值元数据
     * @param partKVMetas 分区键值元数据
     * @param partIdMapping 分区Id映射
     * @throws Exception 异常
     */
    public void insertPartitionKeyVals(List<PartitionKeyValMeta> partKVMetas, Map<Long, Long> partIdMapping)
        throws Exception
    {
        if (null == partKVMetas || partKVMetas.isEmpty())
        {
            return;
        }
        
        PreparedStatement ps = null;
        StringBuilder sql = new StringBuilder();
        sql.append("insert into partition_key_vals(PART_ID,PART_KEY_VAL,INTEGER_IDX) VALUES");
        for (PartitionKeyValMeta pKV : partKVMetas)
        {
            sql.append('(');
            sql.append(partIdMapping.get(pKV.getPartId()));
            sql.append(',');
            sql.append(pKV.getPartKeyVal() == null ? "NULL" : "'" + pKV.getPartKeyVal() + "'");
            sql.append(',');
            sql.append(pKV.getIntegerIdx());
            sql.append("),");
        }
        sql.deleteCharAt(sql.length() - 1);
        
        try
        {
            ps = dstConn.prepareStatement(sql.toString());
            ps.executeUpdate();
            
            LOGGER.info("Insert data into partition_key_vals.");
        }
        catch (Exception e)
        {
            LOGGER.error("execute " + sql + " error.", e);
            throw e;
        }
        finally
        {
            DbBean.close(ps);
        }
    }
    
    /**
     * 同步serdesMeta数据
     * @param serIds  序列化标识集合
     * @param serIdMapping 序列化标识映射
     * @throws Exception 异常
     */
    public void syncSerdesMetas(List<Long> serIds, Map<Long, Long> serIdMapping)
        throws Exception
    {
        if (null == serIds || serIds.isEmpty())
        {
            return;
        }
        
        PreparedStatement ps = null;
        List<SerdesMeta> serdesMetas = exeQuery.getSerdesMetas(serIds);
        if (null == serdesMetas || serdesMetas.isEmpty())
        {
            return;
        }
        
        StringBuilder sql = new StringBuilder();
        sql.append("insert into serdes VALUES");
        for (SerdesMeta serdesMeta : serdesMetas)
        {
            sql.append('(');
            sql.append(serIdMapping.get(serdesMeta.getSerdeId()));
            sql.append(',');
            sql.append(serdesMeta.getName() == null ? "NULL" : "'" + serdesMeta.getName() + "'");
            sql.append(',');
            sql.append(serdesMeta.getSlib() == null ? "NULL" : "'" + serdesMeta.getSlib() + "'");
            sql.append("),");
        }
        sql.deleteCharAt(sql.length() - 1);
        
        try
        {
            ps = dstConn.prepareStatement(sql.toString());
            ps.executeUpdate();
            
            LOGGER.info("Insert data into serdes.");
        }
        catch (Exception e)
        {
            LOGGER.error("execute " + sql + " error.", e);
            throw e;
        }
        finally
        {
            DbBean.close(ps);
        }
    }
    
    /**
     * 同步Serde参数信息
     * @param serIds 源序列号Id列表
     * @param serIdMapping 映射
     * @throws Exception 异常
     */
    public void syncSerdeParmMetas(List<Long> serIds, Map<Long, Long> serIdMapping)
        throws Exception
    {
        if (null == serIds || serIds.isEmpty())
        {
            return;
        }
        
        PreparedStatement ps = null;
        List<SerdeParmMeta> spList = exeQuery.getSerdeParmMetas(serIds);
        if (null == spList || spList.isEmpty())
        {
            return;
        }
        
        StringBuilder sql = new StringBuilder();
        sql.append("insert into serde_params(SERDE_ID,PARAM_KEY,PARAM_VALUE) VALUES");
        for (SerdeParmMeta sp : spList)
        {
            sql.append('(');
            sql.append(serIdMapping.get(sp.getSerdeId()));
            sql.append(',');
            sql.append(sp.getParamKey() == null ? "NULL" : "'" + sp.getParamKey() + "'");
            sql.append(',');
            sql.append(sp.getParamValue() == null ? "NULL" : "'" + sp.getParamValue() + "'");
            sql.append("),");
        }
        sql.deleteCharAt(sql.length() - 1);
        
        try
        {
            ps = dstConn.prepareStatement(sql.toString());
            ps.executeUpdate();
            
            LOGGER.info("Insert data into serde_params.");
        }
        catch (Exception e)
        {
            LOGGER.error("execute " + sql + " error.", e);
            throw e;
        }
        finally
        {
            DbBean.close(ps);
        }
    }
    
    /**
     * 删除指定表数据
     * @param tblIds 表Id集合
     * @throws Exception 异常
     */
    public void deleteTblRelations(List<Long> tblIds)
        throws Exception
    {
        if (null == tblIds || tblIds.isEmpty())
        {
            return;
        }
        
        StringBuilder tblSB = new StringBuilder();
        for (Long tblId : tblIds)
        {
            tblSB.append('(');
            tblSB.append(tblId);
            tblSB.append("),");
        }
        tblSB.deleteCharAt(tblSB.length() - 1);
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "call deleteTblRelations('" + tblSB.toString() + "');";
        
        try
        {
            ps = dstConn.prepareStatement(sql);
            ps.executeUpdate();
        }
        catch (Exception e)
        {
            LOGGER.error("execute " + sql + " error.", e);
            throw e;
        }
        finally
        {
            DbBean.close(rs, ps);
        }
    }
    
    /**
     * 删除指定表参数
     * @param tblIds 表Id集合
     * @throws Exception 异常
     */
    public void deleteTableParamsMetas(List<Long> tblIds)
        throws Exception
    {
        if (null == tblIds || tblIds.isEmpty())
        {
            return;
        }
        
        StringBuilder tblSB = new StringBuilder();
        for (Long tblId : tblIds)
        {
            tblSB.append(tblId);
        }
        tblSB.deleteCharAt(tblSB.length() - 1);
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "delete from table_params WHERE tbl_id in (" + tblSB.toString() + ");";
        try
        {
            ps = dstConn.prepareStatement(sql);
            ps.executeUpdate();
        }
        catch (Exception e)
        {
            LOGGER.error("execute " + sql + " error.", e);
            throw e;
        }
        finally
        {
            DbBean.close(rs, ps);
        }
    }
    
    /**
     * 插入SdsMeta数据
     * @param sdsMetas  sds元数据
     * @param osUser os用户
     * @param sdIdMapping  sd标识映射
     * @param serIdMapping 序列化标识映射
     * @param existPTSdIdNames sd与表名映射
     * @throws Exception 异常
     */
    public void insertSdsMetas(String osUser, List<SdsMeta> sdsMetas, Map<Long, Long> sdIdMapping,
        Map<Long, Long> serIdMapping, Map<Long, String> existPTSdIdNames)
        throws Exception
    {
        PreparedStatement ps = null;
        if (null == sdsMetas || sdsMetas.isEmpty())
        {
            return;
        }
        
        String path = ReadConfig.get("hdfs.root.path");
        path = path.replace("$OS_USER", osUser);
        StringBuilder sql = new StringBuilder();
        sql.append("insert into sds VALUES");
        for (SdsMeta sdsMeta : sdsMetas)
        {
            sql.append('(');
            sql.append(sdIdMapping.get(sdsMeta.getSdId()));
            sql.append(',');
            sql.append(sdsMeta.getInputFormat() == null ? "NULL" : "'" + sdsMeta.getInputFormat() + "'");
            sql.append(',');
            sql.append(sdsMeta.getIsCompressed());
            sql.append(',');
            if (null != existPTSdIdNames && existPTSdIdNames.containsKey(sdsMeta.getSdId()))
            {
                //表sd
                sql.append("'" + path.concat(existPTSdIdNames.get(sdsMeta.getSdId())) + "'");
            }
            else
            {
                //分区sd
                sql.append(sdsMeta.getLocation() == null ? "NULL" : "'" + sdsMeta.getLocation() + "'");
            }
            
            sql.append(',');
            sql.append(sdsMeta.getNumBuckets());
            sql.append(',');
            sql.append(sdsMeta.getOutputFormat() == null ? "NULL" : "'" + sdsMeta.getOutputFormat() + "'");
            sql.append(',');
            sql.append(serIdMapping.get(sdsMeta.getSerdeId()));
            sql.append("),");
        }
        sql.deleteCharAt(sql.length() - 1);
        
        try
        {
            ps = dstConn.prepareStatement(sql.toString());
            ps.executeUpdate();
            
            LOGGER.info("Insert data into sds.");
        }
        catch (Exception e)
        {
            LOGGER.error("execute " + sql + " error.", e);
            throw e;
        }
        finally
        {
            DbBean.close(ps);
        }
    }
    
    /**
     * 插入tblMeta数据
     * @param tblMetas  表元数据列表
     * @param dstDbId 目标库Id
     * @param osUser os用户
     * @param tblIdMapping 表ID映射
     * @param sdIdMapping sd标识映射
     * @throws Exception 异常
     */
    public void insertTblMeta(List<TableMeta> tblMetas, Long dstDbId, String osUser, Map<Long, Long> tblIdMapping,
        Map<Long, Long> sdIdMapping)
        throws Exception
    {
        PreparedStatement ps = null;
        StringBuilder sql = new StringBuilder();
        sql.append("insert into tbls VALUES");
        for (TableMeta tblMeta : tblMetas)
        {
            sql.append('(');
            sql.append(tblIdMapping.get(tblMeta.getTblId()));
            sql.append(',');
            sql.append(tblMeta.getCreateTime());
            sql.append(',');
            sql.append(dstDbId);
            sql.append(',');
            sql.append(tblMeta.getLastAccessTime());
            sql.append(',');
            sql.append(osUser == null ? "NULL" : "'" + osUser + "'");
            sql.append(',');
            sql.append(tblMeta.getRetention());
            sql.append(',');
            sql.append(sdIdMapping.get(tblMeta.getSdId()));
            sql.append(',');
            sql.append(tblMeta.getTblName() == null ? "NULL" : "'" + tblMeta.getTblName() + "'");
            sql.append(',');
            sql.append(tblMeta.getTblType() == null ? "NULL" : "'" + tblMeta.getTblType() + "'");
            sql.append(',');
            sql.append(tblMeta.getViewExpandedText() == null ? "NULL" : "'" + tblMeta.getViewExpandedText() + "'");
            sql.append(',');
            sql.append(tblMeta.getViewOriginalText() == null ? "NULL" : "'" + tblMeta.getViewOriginalText() + "'");
            sql.append("),");
        }
        sql.deleteCharAt(sql.length() - 1);
        
        try
        {
            ps = dstConn.prepareStatement(sql.toString());
            ps.executeUpdate();
            LOGGER.info("Insert data into tbls.");
        }
        catch (Exception e)
        {
            LOGGER.error("execute " + sql + " error.", e);
            throw e;
        }
        finally
        {
            DbBean.close(ps);
        }
    }
    
    /**
     * 插入分区列表 
     * @param partions 分区集合
     * @param partIdMapping 分区Id映射
     * @param sdIdMapping 存储Id映射
     * @param tblIdMapping 表Id映射
     * @throws Exception 异常
     */
    public void insertPartitions(List<PartitionMeta> partions, Map<Long, Long> tblIdMapping,
        Map<Long, Long> partIdMapping, Map<Long, Long> sdIdMapping)
        throws Exception
    {
        if (null == partions || partions.isEmpty())
        {
            return;
        }
        
        PreparedStatement ps = null;
        StringBuilder sql = new StringBuilder();
        sql.append("insert into partitions(PART_ID,CREATE_TIME,LAST_ACCESS_TIME,PART_NAME,SD_ID,TBL_ID) values");
        for (PartitionMeta partiton : partions)
        {
            sql.append('(');
            sql.append(partIdMapping.get(partiton.getPartId()));
            sql.append(',');
            sql.append(partiton.getCreateTime());
            sql.append(',');
            sql.append(partiton.getLastAccessTime());
            sql.append(',');
            sql.append(partiton.getPartName() == null ? "NULL" : "'" + partiton.getPartName() + "'");
            sql.append(',');
            sql.append(sdIdMapping.get(partiton.getSdId()));
            sql.append(',');
            sql.append(tblIdMapping.get(partiton.getTblId()));
            sql.append("),");
        }
        sql.deleteCharAt(sql.length() - 1);
        
        try
        {
            ps = dstConn.prepareStatement(sql.toString());
            ps.executeUpdate();
        }
        catch (Exception e)
        {
            LOGGER.error("execute " + sql + " error.", e);
            throw e;
        }
        finally
        {
            DbBean.close(ps);
        }
    }
    
    /**
     * 同步表参数
     * @param srcTblIds 源表Id集合
     * @param tblIdMapping 表Id映射
     * @throws Exception 异常
     */
    public void syncTableParmMeta(List<Long> srcTblIds, Map<Long, Long> tblIdMapping)
        throws Exception
    {
        PreparedStatement ps = null;
        List<TableParmMeta> tPList = exeQuery.getTableParmMeta(srcTblIds);
        if (null == tPList || tPList.isEmpty())
        {
            return;
        }
        
        StringBuilder sql = new StringBuilder();
        sql.append("insert into table_params VALUES");
        for (TableParmMeta tP : tPList)
        {
            sql.append('(');
            sql.append(tblIdMapping.get(tP.getTblId()));
            sql.append(',');
            sql.append(tP.getParamKey() == null ? "NULL" : "'" + tP.getParamKey() + "'");
            sql.append(',');
            sql.append(tP.getParamValue() == null ? "NULL" : "'" + tP.getParamValue() + "'");
            sql.append("),");
        }
        sql.deleteCharAt(sql.length() - 1);
        
        try
        {
            ps = dstConn.prepareStatement(sql.toString());
            ps.executeUpdate();
            LOGGER.info("Insert data into table_params.");
        }
        catch (Exception e)
        {
            LOGGER.error("execute " + sql + " error.", e);
            throw e;
        }
        finally
        {
            DbBean.close(ps);
        }
    }
    
    /**
     * 同步分区参数
     * @param partIds 分区id集合
     * @param partIdMapping 分区Id映射
     * @throws Exception 异常
     */
    public void syncPartitionParms(List<Long> partIds, Map<Long, Long> partIdMapping)
        throws Exception
    {
        PreparedStatement ps = null;
        List<PartitionParmMeta> pPList = exeQuery.getPartitionParmMetas(partIds);
        
        if (null == pPList || pPList.isEmpty())
        {
            return;
        }
        
        StringBuilder sql = new StringBuilder();
        sql.append("insert into partition_params VALUES");
        for (PartitionParmMeta ppM : pPList)
        {
            sql.append('(');
            sql.append(partIdMapping.get(ppM.getPartId()));
            sql.append(',');
            sql.append(ppM.getParamKey() == null ? "NULL" : "'" + ppM.getParamKey() + "'");
            sql.append(',');
            sql.append(ppM.getParamValue() == null ? "NULL" : "'" + ppM.getParamValue() + "'");
            sql.append("),");
        }
        sql.deleteCharAt(sql.length() - 1);
        
        try
        {
            ps = dstConn.prepareStatement(sql.toString());
            ps.executeUpdate();
            
            LOGGER.info("Insert data into partition_params.");
        }
        catch (Exception e)
        {
            LOGGER.error("execute " + sql + " error.", e);
            throw e;
        }
        finally
        {
            DbBean.close(ps);
        }
    }
}
