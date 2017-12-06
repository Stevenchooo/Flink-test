/*
 * 文 件 名:  ExeQuery.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  查询操作类
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-28
 */
package com.huawei.devicecloud.platform.bi.metasync;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.metasync.conn.DbBean;
import com.huawei.devicecloud.platform.bi.metasync.model.ColumnsMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.PartitionKeyValMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.PartitionKeysMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.PartitionMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.PartitionParmMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.SdsMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.SerdeParmMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.SerdesMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.TableMeta;
import com.huawei.devicecloud.platform.bi.metasync.model.TableParmMeta;

/**
 * 查询操作
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-3-6]
 */
public class ExeQuery
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExeQuery.class);
    
    private final transient Connection srcConn;
    
    private final transient Connection dstConn;
    
    /**
     * 构造函数
     * @param srcConn 源连接
     * @param dstConn 目标连接
     */
    public ExeQuery(final Connection srcConn, final Connection dstConn)
    {
        this.srcConn = srcConn;
        this.dstConn = dstConn;
    }
    
    /**
     * 获取分区列表 
     * @param tblId 表Id
     * @param parName 待比较分区名
     * @param minValue  最小分区值（不包含>）
     * @param maxValue 最大分区值（包含<=）
     * @return 分区列表
     * @throws Exception 异常
     */
    public List<PartitionMeta> getPartitionMetas(final Long tblId, final String parName, final String minValue,
        final String maxValue)
        throws Exception
    {
        List<PartitionMeta> partitionMetas = new ArrayList<PartitionMeta>();
        PartitionMeta partitionMeta;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql;
        if (StringUtils.isBlank(parName) || (StringUtils.isBlank(minValue) && StringUtils.isBlank(maxValue)))
        {
            sql = "SELECT * FROM partitions WHERE TBL_ID=?";
        }
        else if (StringUtils.isNotBlank(minValue) && StringUtils.isBlank(maxValue))
        {
            sql =
                "select distinct a.* from partitions a,partition_keys b,partition_key_vals c where a.TBL_ID=?"
                    + " and a.TBL_ID=b.TBL_ID and"
                    + " a.PART_ID=c.PART_ID and b.INTEGER_IDX=c.INTEGER_IDX and b.PKEY_NAME='" + parName
                    + "' and c.PART_KEY_VAL >'" + minValue + "';";
        }
        else if (StringUtils.isBlank(minValue) && StringUtils.isNotBlank(maxValue))
        {
            sql =
                "select distinct a.* from partitions a,partition_keys b,partition_key_vals c where a.TBL_ID=? "
                    + "and a.TBL_ID=b.TBL_ID and a.PART_ID=c.PART_ID and b.INTEGER_IDX=c.INTEGER_IDX and b.PKEY_NAME='"
                    + parName + "' and c.PART_KEY_VAL <='" + maxValue + "';";
        }
        else
        {
            sql =
                "select distinct a.* from partitions a,partition_keys b,partition_key_vals c where a.TBL_ID=?"
                    + " and a.TBL_ID=b.TBL_ID and a.PART_ID=c.PART_ID and b.INTEGER_IDX=c.INTEGER_IDX and b.PKEY_NAME='"
                    + parName + "' and c.PART_KEY_VAL>'" + minValue + "' and c.PART_KEY_VAL <='" + maxValue + "';";
        }
        
        try
        {
            ps = srcConn.prepareStatement(sql);
            ps.setLong(1, tblId);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                partitionMeta = new PartitionMeta();
                partitionMeta.setPartId(rs.getLong("PART_ID"));
                partitionMeta.setCreateTime(rs.getInt("CREATE_TIME"));
                partitionMeta.setLastAccessTime(rs.getInt("LAST_ACCESS_TIME"));
                partitionMeta.setPartName(rs.getString("PART_NAME"));
                partitionMeta.setSdId(rs.getLong("SD_ID"));
                partitionMeta.setTblId(rs.getLong("TBL_ID"));
                
                //添加到分区列表中
                partitionMetas.add(partitionMeta);
            }
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
        
        return partitionMetas;
    }
    
    /**
     * 获取分区值列表 
     * @param partIds 分区Id集合
     * @return 分区值列表 
     * @throws Exception 异常
     */
    public List<PartitionKeyValMeta> getPartitionKeyVals(List<Long> partIds)
        throws Exception
    {
        if (null == partIds || partIds.isEmpty())
        {
            return new ArrayList<PartitionKeyValMeta>(0);
        }
        
        List<PartitionKeyValMeta> partitionKVs = new ArrayList<PartitionKeyValMeta>();
        PartitionKeyValMeta partitionKV;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "select * from partition_key_vals where PART_ID in(" + concat(partIds, ',') + ");";
        
        try
        {
            ps = srcConn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                partitionKV = new PartitionKeyValMeta();
                partitionKV.setPartId(rs.getLong("PART_ID"));
                partitionKV.setPartKeyVal(rs.getString("PART_KEY_VAL"));
                partitionKV.setIntegerIdx(rs.getInt("INTEGER_IDX"));
                
                //添加到分区值中
                partitionKVs.add(partitionKV);
            }
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
        
        return partitionKVs;
    }
    
    /**
     * 获取db标识
     * @param src 从源或者目标数据库中查询
     * @param dbName 数据库名
     * @return db标识
     * @throws Exception 异常
     */
    public Long getDbId(final boolean src,final String dbName)
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT DB_ID FROM dbs where NAME=?;";
        //设置连接
        Connection conn = src ? srcConn : dstConn;
        
        try
        {
            ps = conn.prepareStatement(sql);
            ps.setString(1, dbName);
            rs = ps.executeQuery();
            
            if (rs.next())
            {
                return rs.getLong(1);
            }
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
        
        return null;
    }
    
    /**
     * 表元数据信息列表
     * @param src 是否是源
     * @param dbId db标识
     * @param tblNames 表名集合
     * @return 表元数据信息列表
     * @throws Exception 异常
     */
    public List<TableMeta> getTblMetas(boolean src, Long dbId, List<String> tblNames)
        throws Exception
    {
        if (null == tblNames || tblNames.isEmpty())
        {
            return null;
        }
        
        StringBuilder tblSB = new StringBuilder();
        for (String tbl : tblNames)
        {
            tblSB.append('\'');
            tblSB.append(tbl);
            tblSB.append("',");
        }
        tblSB.deleteCharAt(tblSB.length() - 1);
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        TableMeta tblMeta = null;
        List<TableMeta> tblMetas = new ArrayList<TableMeta>();
        String sql = "SELECT * from tbls WHERE db_id=" + dbId + " AND tbl_name in (" + tblSB.toString() + ");";
        Connection conn = src ? srcConn : dstConn;
        try
        {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                tblMeta = new TableMeta();
                //TBL表
                tblMeta.setTblId(rs.getLong("TBL_ID"));
                tblMeta.setCreateTime(rs.getInt("CREATE_TIME"));
                tblMeta.setDbId(rs.getLong("DB_ID"));
                tblMeta.setLastAccessTime(rs.getInt("LAST_ACCESS_TIME"));
                tblMeta.setOwner(rs.getString("OWNER"));
                tblMeta.setRetention(rs.getInt("RETENTION"));
                tblMeta.setSdId(rs.getLong("SD_ID"));
                tblMeta.setTblName(rs.getString("TBL_NAME"));
                tblMeta.setTblType(rs.getString("TBL_TYPE"));
                tblMeta.setViewExpandedText(rs.getString("VIEW_EXPANDED_TEXT"));
                tblMeta.setViewOriginalText(rs.getString("VIEW_ORIGINAL_TEXT"));
                
                tblMetas.add(tblMeta);
            }
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
        
        return tblMetas;
    }
    
    /**
     * 表元数据(仅包含tblId和tblName)信息列表
     * @param src 是否是源
     * @param dbId db标识
     * @param tblNames 表名集合
     * @return 表元数据信息列表
     * @throws Exception 异常
     */
    public List<TableMeta> getSingleTblMetas(boolean src, Long dbId, List<String> tblNames)
        throws Exception
    {
        if (null == tblNames || tblNames.isEmpty())
        {
            return null;
        }
        
        StringBuilder tblSB = new StringBuilder();
        for (String tbl : tblNames)
        {
            tblSB.append('\'');
            tblSB.append(tbl);
            tblSB.append("',");
        }
        tblSB.deleteCharAt(tblSB.length() - 1);
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        TableMeta tblMeta = null;
        List<TableMeta> tblMetas = new ArrayList<TableMeta>();
        String sql =
            "SELECT tbl_id,tbl_name from tbls WHERE db_id=" + dbId + " AND tbl_name in (" + tblSB.toString() + ");";
        Connection conn = src ? srcConn : dstConn;
        try
        {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                tblMeta = new TableMeta();
                //TBL表
                tblMeta.setTblId(rs.getLong("TBL_ID"));
                tblMeta.setTblName(rs.getString("TBL_NAME"));
                
                tblMetas.add(tblMeta);
            }
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
        
        return tblMetas;
    }
    
    /**
     * 获取存在的表名集合
     * @param src 是否是源
     * @param dbId db标识
     * @param tblNames 表名集合
     * @return 存在的表名集合
     * @throws Exception 异常
     */
    public List<String> getTblNames(boolean src, Long dbId, List<String> tblNames)
        throws Exception
    {
        if (null == tblNames || tblNames.isEmpty())
        {
            return null;
        }
        
        StringBuilder tblSB = new StringBuilder();
        for (String tbl : tblNames)
        {
            tblSB.append('\'');
            tblSB.append(tbl);
            tblSB.append("',");
        }
        tblSB.deleteCharAt(tblSB.length() - 1);
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> existTblNames = new ArrayList<String>();
        String sql = "SELECT tbl_name from tbls WHERE db_id=" + dbId + " AND tbl_name in (" + tblSB.toString() + ");";
        Connection conn = src ? srcConn : dstConn;
        try
        {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                existTblNames.add(rs.getString("TBL_NAME"));
            }
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
        
        return existTblNames;
    }
    
    private String concat(List<Long> ids, char split)
    {
        StringBuilder idsSb = new StringBuilder();
        for (Long tblId : ids)
        {
            idsSb.append(tblId);
            idsSb.append(split);
        }
        idsSb.deleteCharAt(idsSb.length() - 1);
        
        return idsSb.toString();
    }
    
    /**
     * 获取表的参数信息列表
     * @param srcTblIds 表ID集合
     * @return 表的参数信息列表
     * @throws Exception 异常
     */
    public List<TableParmMeta> getTableParmMeta(List<Long> srcTblIds)
        throws Exception
    {
        if (null == srcTblIds || srcTblIds.isEmpty())
        {
            return new ArrayList<TableParmMeta>(0);
        }
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TableParmMeta> tblParmList = new ArrayList<TableParmMeta>();
        
        String sql = "SELECT * FROM table_params WHERE tbl_id in(" + concat(srcTblIds, ',') + ");";
        try
        {
            ps = srcConn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                TableParmMeta tableParmMeta = new TableParmMeta();
                tableParmMeta.setTblId(rs.getLong("tbl_id"));
                tableParmMeta.setParamKey(rs.getString("param_key"));
                tableParmMeta.setParamValue(rs.getString("param_value"));
                tblParmList.add(tableParmMeta);
            }
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
        return tblParmList;
    }
    
    /**
     * 获取分区参数列表
     * @param partIds 分区id集合
     * @return 分区参数列表
     * @throws Exception 异常
     */
    public List<PartitionParmMeta> getPartitionParmMetas(List<Long> partIds)
        throws Exception
    {
        if (null == partIds || partIds.isEmpty())
        {
            return new ArrayList<PartitionParmMeta>(0);
        }
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PartitionParmMeta> pParmList = new ArrayList<PartitionParmMeta>();
        
        String sql = "SELECT * FROM partition_params where partition_params.PART_ID in (" + concat(partIds, ',') + ");";
        
        try
        {
            ps = srcConn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                PartitionParmMeta partitionParmMeta = new PartitionParmMeta();
                partitionParmMeta.setPartId(rs.getLong("PART_ID"));
                partitionParmMeta.setParamKey(rs.getString("PARAM_KEY"));
                partitionParmMeta.setParamValue(rs.getString("PARAM_VALUE"));
                pParmList.add(partitionParmMeta);
            }
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
        return pParmList;
    }
    
    /**
     * 获取存储描述元数据
     * @param sdIds sd标识集合 
     * @throws Exception 异常
     * @return 存储描述元数据
     */
    public List<SdsMeta> getSdsMetas(List<Long> sdIds)
        throws Exception
    {
        if (null == sdIds || sdIds.isEmpty())
        {
            return new ArrayList<SdsMeta>(0);
        }
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        SdsMeta sdsMeta;
        List<SdsMeta> sdsMetas = new ArrayList<SdsMeta>();
        
        String sql = "SELECT * FROM sds where sd_id in(" + concat(sdIds, ',') + ")";
        
        try
        {
            ps = srcConn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                sdsMeta = new SdsMeta();
                sdsMeta.setSdId(rs.getLong("SD_ID"));
                sdsMeta.setInputFormat(rs.getString("INPUT_FORMAT"));
                sdsMeta.setIsCompressed(rs.getInt("IS_COMPRESSED"));
                sdsMeta.setLocation(rs.getString("LOCATION"));
                sdsMeta.setNumBuckets(rs.getInt("NUM_BUCKETS"));
                sdsMeta.setOutputFormat(rs.getString("OUTPUT_FORMAT"));
                sdsMeta.setSerdeId(rs.getLong("SERDE_ID"));
                sdsMetas.add(sdsMeta);
            }
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
        return sdsMetas;
    }
    
    /**
     * 获取序列化参数信息
     * @param serIds 源序列号Id列表
     * @throws Exception 异常
     * @return 序列号参数信息集合
     */
    public List<SerdeParmMeta> getSerdeParmMetas(List<Long> serIds)
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SerdeParmMeta> serdeParmList = new ArrayList<SerdeParmMeta>();
        
        String sql = "SELECT * FROM serde_params where serde_id in (" + concat(serIds, ',') + ");";
        try
        {
            ps = srcConn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                SerdeParmMeta serdeParmMeta = new SerdeParmMeta();
                serdeParmMeta.setSerdeId(rs.getLong("SERDE_ID"));
                serdeParmMeta.setParamKey(rs.getString("PARAM_KEY"));
                serdeParmMeta.setParamValue(rs.getString("PARAM_VALUE"));
                serdeParmList.add(serdeParmMeta);
            }
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
        return serdeParmList;
    }
    
    /**
     * 获列元数据信息
     * 
     * @param sdIds 源表存储标识
     * @return 列元数据
     * @throws Exception 异常
     */
    public List<ColumnsMeta> getColumnsMetas(List<Long> sdIds)
        throws Exception
    {
        if (null == sdIds || sdIds.isEmpty())
        {
            return new ArrayList<ColumnsMeta>();
        }
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ColumnsMeta> cList = new ArrayList<ColumnsMeta>();
        
        String sql = "SELECT * FROM columns where sd_id in (" + concat(sdIds, ',') + ");";
        
        try
        {
            ps = srcConn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                ColumnsMeta columnsMeta = new ColumnsMeta();
                columnsMeta.setSdId(rs.getLong("SD_ID"));
                columnsMeta.setComment(rs.getString("COMMENT"));
                columnsMeta.setColumnName(rs.getString("COLUMN_NAME"));
                columnsMeta.setTypeName(rs.getString("TYPE_NAME"));
                columnsMeta.setIntegerIdx(rs.getInt("INTEGER_IDX"));
                cList.add(columnsMeta);
            }
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
        
        return cList;
    }
    
    /**
     * 获取序列化类信息
     * @param serIds 序列ID列表
     * @return 序列化类信息
     * @throws Exception 异常
     */
    public List<SerdesMeta> getSerdesMetas(List<Long> serIds)
        throws Exception
    {
        if (null == serIds || serIds.isEmpty())
        {
            return new ArrayList<SerdesMeta>(0);
        }
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        SerdesMeta serdesMeta = null;
        List<SerdesMeta> serdesMetas = new ArrayList<SerdesMeta>();
        
        String sql = "SELECT * FROM serdes WHERE serde_id in(" + concat(serIds, ',') + ")";
        
        try
        {
            ps = srcConn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                serdesMeta = new SerdesMeta();
                serdesMeta.setSerdeId(rs.getLong("SERDE_ID"));
                serdesMeta.setName(rs.getString("NAME"));
                serdesMeta.setSlib(rs.getString("SLIB"));
                serdesMetas.add(serdesMeta);
            }
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
        return serdesMetas;
    }
    
    /**
     * 获取分区键列表
     * @param tblIds 源表Id列表
     * @return 分区键列表
     * @throws Exception 异常
     */
    public List<PartitionKeysMeta> getPartitionKeys(List<Long> tblIds)
        throws Exception
    {
        if (null == tblIds || tblIds.isEmpty())
        {
            return new ArrayList<PartitionKeysMeta>(0);
        }
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        PartitionKeysMeta partitionKeys = null;
        List<PartitionKeysMeta> pkList = new ArrayList<PartitionKeysMeta>();
        
        String sql = "SELECT * FROM partition_keys WHERE TBL_ID in(" + concat(tblIds, ',') + ");";
        
        try
        {
            ps = srcConn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                partitionKeys = new PartitionKeysMeta();
                partitionKeys.setTblId(rs.getLong("TBL_ID"));
                partitionKeys.setPkeyComment(rs.getString("PKEY_COMMENT"));
                partitionKeys.setPkeyName(rs.getString("PKEY_NAME"));
                partitionKeys.setPkeyType(rs.getString("PKEY_TYPE"));
                partitionKeys.setIntegerIdx(rs.getInt("INTEGER_IDX"));
                pkList.add(partitionKeys);
            }
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
        return pkList;
        
    }
}
