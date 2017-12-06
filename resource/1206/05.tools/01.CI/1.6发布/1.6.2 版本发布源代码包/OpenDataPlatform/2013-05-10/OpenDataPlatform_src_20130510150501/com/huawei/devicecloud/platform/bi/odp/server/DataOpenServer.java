/*
 * 文 件 名:  DataOpenServer.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  数据开发服务器
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-7
 */
package com.huawei.devicecloud.platform.bi.odp.server;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.CException;
import com.huawei.devicecloud.platform.bi.common.utils.CommonUtils;
import com.huawei.devicecloud.platform.bi.common.utils.datasource.OdpDataSource;
import com.huawei.devicecloud.platform.bi.common.utils.file.OdpFileUtils;
import com.huawei.devicecloud.platform.bi.common.utils.pool.ThreadPoolUtil;
import com.huawei.devicecloud.platform.bi.odp.constants.OdpConfig;
import com.huawei.devicecloud.platform.bi.odp.constants.ResultCode;
import com.huawei.devicecloud.platform.bi.odp.constants.type.FileDealFlagType;
import com.huawei.devicecloud.platform.bi.odp.constants.type.FileInterfaceDealType;
import com.huawei.devicecloud.platform.bi.odp.constants.type.OperatorType;
import com.huawei.devicecloud.platform.bi.odp.constants.type.ReservedFileStateType;
import com.huawei.devicecloud.platform.bi.odp.constants.type.ReservedStateType;
import com.huawei.devicecloud.platform.bi.odp.constants.type.UserAdFlagType;
import com.huawei.devicecloud.platform.bi.odp.dao.OdpDao;
import com.huawei.devicecloud.platform.bi.odp.domain.ColumnFieldMapping;
import com.huawei.devicecloud.platform.bi.odp.domain.ColumnFieldTypeMapping;
import com.huawei.devicecloud.platform.bi.odp.domain.ConfigInfo;
import com.huawei.devicecloud.platform.bi.odp.domain.DateRatioInfo;
import com.huawei.devicecloud.platform.bi.odp.domain.GroupInfo;
import com.huawei.devicecloud.platform.bi.odp.domain.RecordQueue;
import com.huawei.devicecloud.platform.bi.odp.domain.ReservedInfo;
import com.huawei.devicecloud.platform.bi.odp.domain.TempTableRecordProcessParam;
import com.huawei.devicecloud.platform.bi.odp.entity.AuditInfoEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.ControlFlagEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.DBServerAddressEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.FileUploadTableEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.ReservedInfoEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.RouteEntity;
import com.huawei.devicecloud.platform.bi.odp.entity.UserProfileEntity;
import com.huawei.devicecloud.platform.bi.odp.management.ConfigMgnt;
import com.huawei.devicecloud.platform.bi.odp.management.UserManagement;
import com.huawei.devicecloud.platform.bi.odp.message.req.QueryDataCountReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.ReserveBatchDataReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.RevokeReserveReq;
import com.huawei.devicecloud.platform.bi.odp.message.req.WGetFileReq;
import com.huawei.devicecloud.platform.bi.odp.message.rsp.FileUploadRsp;
import com.huawei.devicecloud.platform.bi.odp.parallel.ParallelCountor;
import com.huawei.devicecloud.platform.bi.odp.parallel.SelectSqlExector;
import com.huawei.devicecloud.platform.bi.odp.process.AuditProcess;
import com.huawei.devicecloud.platform.bi.odp.process.NormalUsersProcess;
import com.huawei.devicecloud.platform.bi.odp.process.TempTableRecordProcess;
import com.huawei.devicecloud.platform.bi.odp.process.TempTableRecordProducter;
import com.huawei.devicecloud.platform.bi.odp.process.UserProfileProcess;
import com.huawei.devicecloud.platform.bi.odp.remote.message.req.GroupsPrepareResultReq;
import com.huawei.devicecloud.platform.bi.odp.remote.message.req.UserQueryResultReq;
import com.huawei.devicecloud.platform.bi.odp.remote.service.client.DataQueryClient;
import com.huawei.devicecloud.platform.bi.odp.utils.OdpCommonUtils;
import com.huawei.devicecloud.platform.bi.odp.utils.TimeStatis;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 开发数据服务
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-7]
 */
public class DataOpenServer
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataOpenServer.class);
    
    //初始大小
    private static final int DB_SERVER_SIZE = 32;
    
    //配置信息
    private ConfigInfo configInfo;
    
    //数据库Id与用户信息数据存放数据库连接池的MAP
    private Map<Integer, BasicDataSource> dbServerDSs;
    
    //数据查询客户端，用于回调
    private DataQueryClient dataQueryClient;
    
    //用户信息处理
    private UserProfileProcess userProcess;
    
    //审计信息处理
    private AuditProcess auditProcess;
    
    //routine运行次数
    private long routineTimes = 0L;
    
    //userManagement对象
    private UserManagement userManagement;
    
    //用户信息表表名字
    private String tableName = OdpConfig.createOdpConfig().getUpTable();
    
    //用户上传文件导入数据表名
    private String upLoadFileTableName = OdpConfig.createOdpConfig().getUpLoadFileTable();
    
    //mappe文件中的结果集映射Id
    private String resultMapID = OdpConfig.createOdpConfig().getResultMapID();
    
    //Dao层调用对象
    private OdpDao odpDao;
    
    //用户信息数据源模板
    private OdpDataSource odpProfileDS;
    
    /**
     * 初始化
     */
    public void initialize()
    {
        try
        {
            //加载各种配置项
            ConfigMgnt configMgnt = new ConfigMgnt(odpDao);
            
            configMgnt.readConfig();
            this.configInfo = configMgnt.getConfigInfo();
            
            dbServerDSs = new HashMap<Integer, BasicDataSource>(DB_SERVER_SIZE);
            //初始化数据库连接池
            List<DBServerAddressEntity> dataDbServers = this.configInfo.getDataDBServers();
            OdpDataSource odpDS = null;
            for (DBServerAddressEntity dataDbServer : dataDbServers)
            {
                odpDS = new OdpDataSource();
                //复制数据源的配置
                odpDS.setDriverClassName(odpProfileDS.getDriverClassName());
                odpDS.setUrl(String.format(odpProfileDS.getUrl(),
                    dataDbServer.getIpAddress(),
                    dataDbServer.getPort(),
                    dataDbServer.getDbname()));
                odpDS.setUsername(dataDbServer.getUserName());
                odpDS.setPassword(dataDbServer.getPwd());
                odpDS.setDefaultAutoCommit(odpProfileDS.getDefaultAutoCommit());
                odpDS.setDefaultReadOnly(odpProfileDS.getDefaultReadOnly());
                odpDS.setInitialSize(odpProfileDS.getInitialSize());
                odpDS.setMaxActive(odpProfileDS.getMaxActive());
                odpDS.setMaxIdle(odpProfileDS.getMaxIdle());
                odpDS.setMaxWait(odpProfileDS.getMaxWait());
                odpDS.setValidationQuery(odpProfileDS.getValidationQuery());
                odpDS.setTestWhileIdle(odpProfileDS.getTestWhileIdle());
                odpDS.setTimeBetweenEvictionRunsMillis(odpProfileDS.getTimeBetweenEvictionRunsMillis());
                odpDS.setMinEvictableIdleTimeMillis(odpProfileDS.getMinEvictableIdleTimeMillis());
                odpDS.setRemoveAbandoned(odpProfileDS.getRemoveAbandoned());
                odpDS.setRemoveAbandonedTimeout(odpProfileDS.getRemoveAbandonedTimeout());
                odpDS.setLogAbandoned(odpProfileDS.getLogAbandoned());
                //加入到MAP中
                dbServerDSs.put(dataDbServer.getDbServerId(), odpDS);
            }
            
            //只有当初始化流量控制值开关开启时才进行初始化
            if (OdpConfig.createOdpConfig().isInitLoadCtrlValue())
            {
                //初始化当前流控值
                userProcess.initLoadCtrl();
            }
        }
        catch (Exception e)
        {
            //验证错误，服务器无法正常工作！
            LOGGER.error("initialize failed! DataOpenSever can't work normally!", e);
        }
        
    }
    
    /**
     * 每天运行一次清理占用的资源（包括过期临时表和临时文件）
     */
    public void routine()
    {
        try
        {
            LOGGER.info("routine run {} times!", ++routineTimes);
            int vaildServer = userProcess.getCurrentVaildServer();
            //集群中仅当前节点可以清理资源
            if (vaildServer != configInfo.getClusterId())
            {
                return;
            }
            
            //获取已经过期的预留信息列表
            List<ReservedInfoEntity> reservedInfos = userProcess.getAllExpiredReservedInfos();
            
            //循环清理已经过期的资源
            for (ReservedInfoEntity reserve : reservedInfos)
            {
                try
                {
                    userProcess.reclaimResource(reserve);
                    LOGGER.info("reclaim resource[{}] successfully!", reserve);
                }
                catch (Exception e)
                {
                    LOGGER.error("reclaim Resource failed! the reserve is {}", reserve, e);
                }
            }
            
            //新增删除数据文件，目录，数据表
            cleanFileInfo();
            
        }
        catch (Exception e)
        {
            LOGGER.error("routine failed!", e);
        }
    }
    
    private void cleanFileInfo()
        throws CException
    {
        //获取配置的天数
        int days = Integer.parseInt(CommonUtils.getSysConfig("file.del.days"));
        
        //获取文件id
        List<String> fileIdList = this.odpDao.getFileIdList(days);
        
        //删除数据表
        dealFileUploadTable(fileIdList);
        
        //删除文件与目录
        dealFileAndPath(fileIdList);
        
        //删除相应记录
        dealFileinfo(fileIdList);
        
    }
    
    /**
     * <删除数据表中文件相关记录>
     * @param fileIdList  文件ID列表
     * @throws CException  删除数据表中文件相关记录异常
     * @see [类、类#方法、类#成员]
     */
    private void dealFileinfo(List<String> fileIdList)
        throws CException
    {
        if (null != fileIdList && !fileIdList.isEmpty())
        {
            //删除相应记录
            this.odpDao.delFileInfo(fileIdList);
        }
        
    }
    
    /**
     * <删除系统中上传的文件>
     * @param fileIdList  待删除的文件id
     * @see [类、类#方法、类#成员]
     */
    private void dealFileAndPath(List<String> fileIdList)
    {
        if (null != fileIdList && !fileIdList.isEmpty())
        {
            for (String fileId : fileIdList)
            {
                try
                {
                    //获取文件名 与 文件夹名
                    String filePath = CommonUtils.getSysConfig("file_local_path");
                    
                    String fileName = filePath + File.separator + fileId + ".zip";
                    
                    String fileDir = filePath + File.separator + fileId;
                    
                    //删除文件
                    FileUtils.deleteQuietly(new File(fileName));
                    //删除文件夹
                    FileUtils.deleteQuietly(new File(fileDir));
                }
                catch (Exception e)
                {
                    LOGGER.error("dealFileAndPath failed! fileId is {} exception is {}", new Object[] {fileId, e});
                }
                
            }
            
        }
        
    }
    
    /**
     * <删除各个节点上的数据表>
     * @param fileIdList   待删除的文件id
     * @see [类、类#方法、类#成员]
     */
    private void dealFileUploadTable(List<String> fileIdList)
    {
        if (null != fileIdList && !fileIdList.isEmpty())
        {
            List<String> tableNameList = new ArrayList<String>();
            for (String fileId : fileIdList)
            {
                //删除数据表
                String table = getTempTableName(upLoadFileTableName, fileId);
                tableNameList.add(table);
            }
            
            //各个节点删数据表
            //获取数据表连接
            List<BasicDataSource> routeDataDSs = new ArrayList<BasicDataSource>();
            
            Set<Map.Entry<Integer, BasicDataSource>> set = dbServerDSs.entrySet();
            
            for (Iterator<Map.Entry<Integer, BasicDataSource>> it = set.iterator(); it.hasNext();)
            {
                Map.Entry<Integer, BasicDataSource> entry = (Map.Entry<Integer, BasicDataSource>)it.next();
                routeDataDSs.add(entry.getValue());
            }
            
            //循环处理数据
            for (BasicDataSource ds : routeDataDSs)
            {
                if (null != ds)
                {
                    //创建数据表，然后再导入数据
                    Connection conn = null;
                    PreparedStatement stmt = null;
                    ResultSet rs = null;
                    
                    try
                    {
                        //创建数据表
                        conn = ds.getConnection();
                        
                        for (String table : tableNameList)
                        {
                            try
                            {
                                String sql = "drop table " + table;
                                stmt = conn.prepareStatement(sql);
                                stmt.execute();
                            }
                            catch (Exception e)
                            {
                                LOGGER.error("exec drop table failed! table name is {}" + " exception is {}",
                                    new Object[] {table, e});
                            }
                            
                        }
                        
                    }
                    catch (Throwable e)
                    {
                        //执行失败
                        LOGGER.error("execSql failed! exception is {}", e);
                    }
                    finally
                    {
                        //关闭流
                        DbUtils.closeQuietly(conn, stmt, rs);
                    }
                }
            }
        }
        
    }
    
    //新增预留信息，获取预留Id
    private String createReservedInfo(String appId, int userAdFlag, Date useDate, Date expiredTime)
        throws CException
    {
        ReservedInfoEntity entity = new ReservedInfoEntity();
        entity.setCreateAppId(appId);
        entity.setUserType(userAdFlag);
        entity.setUseDate(useDate);
        entity.setExpiredTime(expiredTime);
        return odpDao.addReservedInfo(entity);
    }
    
    //获取临时表名
    private String getTempTableName(String dataTableName, String reservedId)
    {
        return String.format("%s_%s", dataTableName, reservedId);
    }
    
    /**
     * 获取文件对象
     * @param req 请求参数
     * @throws CException 异常
     * @return 获取文件对象
     */
    public File wgetFile(WGetFileReq req)
        throws CException
    {
        //获取文件对象
        File file = userProcess.getFile(req.getReserveId(), resultMapID);
        
        //记录审计信息
        AuditInfoEntity auditInfo = new AuditInfoEntity();
        auditInfo.setOperatorType(OperatorType.GET_FILE);
        auditInfo.setAppId(req.getAppId());
        auditInfo.setReserveId(req.getReserveId());
        auditProcess.logAuditInfo(auditInfo);
        
        return file;
    }
    
    /**
     * 取消预留数据
     * @param req 请求参数
     * @throws CException 异常
     */
    public void revokeReserve(RevokeReserveReq req)
        throws CException
    {
        //获取预留信息
        userProcess.reclaimResource(req.getAppId(), req.getReserveId());
        
        //记录审计信息
        AuditInfoEntity auditInfo = new AuditInfoEntity();
        auditInfo.setOperatorType(OperatorType.REVOKE_RESERVE);
        auditInfo.setAppId(req.getAppId());
        auditInfo.setReserveId(req.getReserveId());
        auditProcess.logAuditInfo(auditInfo);
    }
    
    /**
     * 分批预留数据
     * @param req 请求参数
     * @throws CException 异常
     */
    public void reserveBatchData(ReserveBatchDataReq req)
        throws CException
    {
        //流控检查
        //isNeedLoadCtrl();
        //为防止出现数据一致问题,将流控检查以及流合在一起处理
        userProcess.applyDataQuery(configInfo.getConfigEntity().getLoadCtrlThreshold());
        
        try
        {
            //获取控制相关配置信息
            ControlFlagEntity controlFlag = userProcess.getControlFlag();
            
            List<BasicDataSource> routeDataDSs =
                getRouteDSs(controlFlag.getCurrentRouteTable(), controlFlag.getCurrentTableID(), tableName);
            
            //用户想要获取值
            Long recordNum = req.getRecordNumber();
            
            //若用户没有设置上限，默认给系统配置最大值
            if (null == recordNum)
            {
                recordNum = configInfo.getConfigEntity().getMaxReturnRecords();
            }
            
            //获取日期
            Date date = CommonUtils.convertDateTimeFormat(req.getBatchInfo().get(0).getDate(), null);
            //获取周Id
            String mondayStr = OdpCommonUtils.getMondayDateStr(date);
            //创建crt表
            String crt = String.format("crt_%s", mondayStr);
            odpDao.createCRT(crt);
            Long gottedNum = this.odpDao.getCRTCount(crt);
            
            //至少需要获取的值
            Long minLimit = recordNum + gottedNum;
            
            //获取最大记录总数
            Long reMaxRecordCount = 0l;
            
            if (null != minLimit && minLimit > 0)
            {
                //取最小值
                reMaxRecordCount =
                    OdpCommonUtils.getMinValue(minLimit, configInfo.getConfigEntity().getMaxReturnRecords());
            }
            else
            {
                reMaxRecordCount = configInfo.getConfigEntity().getMaxReturnRecords();
            }
            
            //获取user_ad_flag标识
            int userAdFlag = OdpCommonUtils.extractUserAdFlag(req.getFilterStmt());
            
            //拼接sql并顺便检查列是否存在
            String selectSql =
                constructSelectSql(String.format("%s_%d", tableName, controlFlag.getCurrentTableID()),
                    userAdFlag,
                    req,
                    reMaxRecordCount);
            
            req.getBatchInfo();
            
            LOGGER.info("req is {}, selectSql is [{}]", req, selectSql);
            
            //获取预留天数
            Integer days = OdpCommonUtils.getMinValue(req.getDays(), configInfo.getConfigEntity().getMaxReservedDays());
            
            //使用线程池异步执行
            AsyncReserveBatchData asyncExector =
                new AsyncReserveBatchData(reMaxRecordCount, recordNum, days, selectSql, req, routeDataDSs);
            ThreadPoolUtil.submmitTask(asyncExector);
        }
        catch (Exception e)
        {
            LOGGER.error("reserveBatchData failed! req is {}", new Object[] {req, e});
            
            //释放查询资源
            userProcess.releaseDataQuery();
            
            //抛出封装的异常
            throw OdpCommonUtils.covertE2CE(e);
        }
    }
    
    /**
     * 获取所有的crt表名
     * @param dates 日期比例列表
     * @return 所有的crt表名
     * @throws CException 异常
     */
    public List<String> getDisCrts(List<DateRatioInfo> dates)
        throws CException
    {
        List<String> crts = new ArrayList<String>();
        String crt;
        Date rDate;
        for (DateRatioInfo date : dates)
        {
            rDate = CommonUtils.convertDateTimeFormat(date.getDate(), null);
            //获取周Id
            String mondayStr = OdpCommonUtils.getMondayDateStr(rDate);
            //创建crt表
            crt = String.format("crt_%s", mondayStr);
            if (!crts.contains(crt))
            {
                crts.add(crt);
            }
        }
        return crts;
    }
    
    /**
     * 查询用户总数
     * @param req 消息参数
     * @throws CException 异常
     */
    public void queryDataCount(QueryDataCountReq req)
        throws CException
    {
        //流控检查
        //isNeedLoadCtrl();
        //为防止出现数据一致性问题,将流控检查以及流量+1合在一起处理
        userProcess.applyDataQuery(configInfo.getConfigEntity().getLoadCtrlThreshold());
        try
        {
            //获取控制相关配置信息
            ControlFlagEntity controlFlag = userProcess.getControlFlag();
            
            List<BasicDataSource> routeDataDSs =
                getRouteDSs(controlFlag.getCurrentRouteTable(), controlFlag.getCurrentTableID(), tableName);
            
            //拼接sql
            String countSql = constructCountSql(tableName, controlFlag.getCurrentTableID(), req.getFilterStmt());
            
            LOGGER.info("req is {}, countSql is [{}]", req, countSql);
            
            //获取最大记录总数
            Long maxRecordCount =
                OdpCommonUtils.getMinValue(req.getRecordCount(), configInfo.getConfigEntity().getMaxReturnRecords());
            
            //使用线程池异步执行
            AsyncQueryDataCount asyncExector = new AsyncQueryDataCount(maxRecordCount, countSql, routeDataDSs, req);
            ThreadPoolUtil.submmitTask(asyncExector);
        }
        catch (Exception e)
        {
            LOGGER.error("queryDataCount failed! req is {}", new Object[] {req, e});
            
            //释放查询资源
            userProcess.releaseDataQuery();
            
            //抛出封装的异常
            throw OdpCommonUtils.covertE2CE(e);
        }
    }
    
    private List<BasicDataSource> getRouteDSs(int routeTableId, int currentTableId, String dataTableName)
        throws CException
    {
        ///获取表路由信息
        List<RouteEntity> tableRoutes =
            userProcess.getRoutes(routeTableId, String.format("%s_%d", dataTableName, currentTableId));
        
        List<BasicDataSource> routeDataDSs = new ArrayList<BasicDataSource>();
        BasicDataSource dataDs;
        //通过route找到表分布的数据源集合
        for (RouteEntity route : tableRoutes)
        {
            if (null != route.getDbServerId())
            {
                //获取数据源实例
                dataDs = dbServerDSs.get(route.getDbServerId());
                if (null != dataDs)
                {
                    routeDataDSs.add(dataDs);
                }
            }
        }
        
        return routeDataDSs;
    }
    
    /**
     * 构造查询总数Sql
     * @param dataTableName 待查询表表名
     * @param currentTableID 查询的表ID
     * @param filterStmt 过滤条件（where子句）
     * @return 查询总数Sql
     */
    private String constructCountSql(String dataTableName, Integer currentTableID, String filterStmt)
    {
        String countSql;
        if (!StringUtils.isBlank(filterStmt))
        {
            countSql = String.format("select count(*) from %s_%d where %s", dataTableName, currentTableID, filterStmt);
        }
        else
        {
            countSql = String.format("select count(*) from %s_%d", dataTableName, currentTableID);
        }
        
        return countSql;
    }
    
    /**
     * 构造查询Sql
     * @param dataTable 待查询表表名
     * @param int userAdFlag 用户标识
     * @param req 请求体
     * @param limit 限制总数
     * @return 查询Sql
     */
    private String constructSelectSql(String dataTable, int userAdFlag, ReserveBatchDataReq req, Long limit)
        throws CException
    {
        List<Integer> columns = req.getExtractList();
        
        String fileID = req.getImeiFileId();
        if (null == fileID)
        {
            StringBuilder selectSqlSb = new StringBuilder();
            selectSqlSb.append("select ");
            //构造查询的字段集合
            selectSqlSb.append(covert2QueryFields(columns));
            selectSqlSb.append(" from ");
            selectSqlSb.append(dataTable);
            
            //过滤条件不为空才拼接where
            if (!StringUtils.isBlank(req.getFilterStmt()))
            {
                selectSqlSb.append(" where ");
                selectSqlSb.append(req.getFilterStmt());
            }
            
            selectSqlSb.append(" limit ");
            selectSqlSb.append(limit);
            LOGGER.info("select sql is {}", selectSqlSb.toString());
            return selectSqlSb.toString();
        }
        else
        {
            //需要关联数据表
            StringBuilder selectSqlSb = new StringBuilder();
            String tabelName = getTempTableName(upLoadFileTableName, fileID);
            ;
            selectSqlSb.append("select ");
            selectSqlSb.append(finalCovert2QueryFields(columns));
            selectSqlSb.append(" from ( select ");
            //构造查询的字段集合
            selectSqlSb.append(covert2QueryFields(columns));
            selectSqlSb.append(" from ");
            selectSqlSb.append(dataTable);
            
            //过滤条件不为空才拼接where
            if (!StringUtils.isBlank(req.getFilterStmt()))
            {
                selectSqlSb.append(" where ");
                selectSqlSb.append(req.getFilterStmt());
            }
            
            selectSqlSb.append(" )t1 join ");
            selectSqlSb.append(tabelName + " t2");
            selectSqlSb.append(" on t1.device_id = t2.device_id");
            
            selectSqlSb.append(" limit ");
            selectSqlSb.append(limit);
            
            LOGGER.info("select sql is {}", selectSqlSb.toString());
            return selectSqlSb.toString();
        }
        
    }
    
    /**
     * 将列枚举列表转换","号分隔字段名字符串
     */
    private String covert2QueryFields(List<Integer> columns)
        throws CException
    {
        String allColumn = "*";
        if (null == columns || columns.isEmpty())
        {
            return allColumn;
        }
        else
        {
            StringBuilder columnSB = new StringBuilder();
            String columnName;
            for (Integer columnEnum : columns)
            {
                //获取列名
                columnName = CommonUtils.getColumnName(columnEnum);
                if (null != columnName)
                {
                    columnSB.append(columnName);
                    columnSB.append(',');
                }
                else
                {
                    LOGGER.error("column[{}] doesn't exist!", columnEnum);
                    throw new CException(ResultCode.COLUMN_NOT_EXIST, columnEnum);
                }
            }
            
            if (0 != columnSB.length())
            {
                //移除最后一个","号
                columnSB.deleteCharAt(columnSB.length() - 1);
                return columnSB.toString();
            }
            else
            {
                return allColumn;
            }
        }
    }
    
    /**
     * 将列枚举列表转换","号分隔字段名字符串
     */
    private String finalCovert2QueryFields(List<Integer> columns)
        throws CException
    {
        String allColumn = "*";
        if (null == columns || columns.isEmpty())
        {
            return allColumn;
        }
        else
        {
            StringBuilder columnSB = new StringBuilder();
            String columnName;
            for (Integer columnEnum : columns)
            {
                //获取列名
                columnName = CommonUtils.getColumnName(columnEnum);
                if (null != columnName)
                {
                    columnSB.append("t1." + columnName);
                    columnSB.append(',');
                }
                else
                {
                    LOGGER.error("column[{}] doesn't exist!", columnEnum);
                    throw new CException(ResultCode.COLUMN_NOT_EXIST, columnEnum);
                }
            }
            
            if (0 != columnSB.length())
            {
                //移除最后一个","号
                columnSB.deleteCharAt(columnSB.length() - 1);
                return columnSB.toString();
            }
            else
            {
                return allColumn;
            }
        }
    }
    
    /**
     * 将列枚举列表转换","号分隔字段名字符串
     */
    private ColumnFieldTypeMapping[] getColumnFieldTypeMappings(List<Integer> columnEnums, String resultMapId)
        throws CException
    {
        //不存在列返回所有列映射
        if (null == columnEnums || columnEnums.isEmpty())
        {
            return userProcess.getAllCFTMappings(resultMapId);
        }
        
        String[] columnNames = OdpCommonUtils.getColumnNams(columnEnums);
        
        //筛选指定列的映射关系数组
        return userProcess.getChoosedCFTMappings(resultMapId, columnNames);
        
    }
    
    /**
     * 查询记录总数执行器 
     * 
     * @author  z00190465
     * @version [Open Data Platform Service, 2012-8-9]
     */
    private class AsyncQueryDataCount extends ParallelCountor implements Runnable
    {
        //查询记录总数请求提
        private QueryDataCountReq qdcReq;
        
        /**
         * 认构造函数
         * @param callbackUrl 回调接口地址
         * @param maxRecordCount 最大记录总数
         * @param countSql 统计sql语句
         * @param transactionID 对话ID 
         * @param routeDataDSs 表分布的数据源集合
         */
        AsyncQueryDataCount(Long maxRecordCount, String countSql, List<BasicDataSource> routeDataDSs,
            QueryDataCountReq req)
        {
            super(countSql, maxRecordCount, routeDataDSs);
            this.qdcReq = req;
        }
        
        @Override
        public void run()
        {
            //初始化时间统计对象
            TimeStatis ts =
                new TimeStatis(String.format("[user_query(tid=%s,ts=%s)]  query the count of records asynchronously!",
                    qdcReq.getTransactionId(),
                    qdcReq.getTimestamp()));
            ts.startTiming();
            
            //构造queryDataCountResp方法的请求体
            UserQueryResultReq req = new UserQueryResultReq();
            req.setTransaction_id(qdcReq.getTransactionId());
            
            try
            {
                //并行汇总查询记录总数
                long recordCount = parallelCount(qdcReq.getTransactionId(), qdcReq.getTimestamp());
                req.setResult_code(0);
                req.setRecord_count(recordCount);
            }
            catch (CException e)
            {
                LOGGER.error("QueryDataCountExector run failed! this is {}", this);
                OdpCommonUtils.setResultByException(req, e);
            }
            catch (Exception e)
            {
                LOGGER.error("QueryDataCountExector run failed! this is {}", this);
                OdpCommonUtils.setResultByException(req, e);
            }
            finally
            {
                //释放数据查询资源
                userProcess.releaseDataQuery();
                
            }
            
            //初始化时间统计对象
            TimeStatis tsClien =
                new TimeStatis(String.format("[user_query(tid=%s,ts=%s)] step 2. callback the %s!",
                    qdcReq.getTransactionId(),
                    qdcReq.getTimestamp(),
                    qdcReq.getCallbackURL()));
            tsClien.startTiming();
            //交给稳定的服务发送，当前直接发送
            try
            {
                dataQueryClient.queryDataCountResp(qdcReq.getCallbackURL(), req);
            }
            catch (Exception e)
            {
                LOGGER.error("dataQueryClient.queryDataCountResp failed! hiacAddr is {}, req is {}.",
                    qdcReq.getCallbackURL(),
                    req);
            }
            tsClien.endTiming();
            
            //记录执行时间
            ts.endTiming();
        }
    }
    
    /**
     * 查询记录总数执行器 
     * 
     * @author  z00190465
     * @version [Open Data Platform Service, 2012-8-9]
     */
    private class AsyncReserveBatchData implements Runnable
    {
        //查询sql语句
        private String selectSql;
        
        //预留信息个数
        private int reservedNum;
        
        //记录总数限制
        private Long limit;
        
        //预处理的总记录数
        private Long relimit;
        
        //预留天数
        private Integer days;
        
        //是否写记录到数据库表和文件中
        private boolean writeRecords;
        
        //组Id集合
        private String[] groupIds;
        
        //执行selectSql的连接池列表
        private List<BasicDataSource> routeDataDSs;
        
        //预留批次请求体
        private ReserveBatchDataReq rbdReq;
        
        /**
         * 构造函数
         * @param relimit 预处理最大记录数
         * @param limit   最终的数据量
         * @param days 有效天数
         * @param selectSql 查询sql语句
         * @param rbdReq reserveBatchData请求参数
         * @param routeDataDSs 表分布的数据源集合
         */
        AsyncReserveBatchData(Long relimit, Long limit, Integer days, String selectSql, ReserveBatchDataReq rbdReq,
            List<BasicDataSource> routeDataDSs)
        {
            this.selectSql = selectSql;
            this.rbdReq = rbdReq;
            this.days = rbdReq.getDays();
            this.limit = limit;
            this.relimit = relimit;
            //保留天数，是不是应该现在最大保留天数
            writeRecords = days > 0;
            //失效时间只能介于0与最大预留天数之间
            if (days <= 0)
            {
                this.days = 0;
            }
            else
            {
                this.days = days;
            }
            
            reservedNum = rbdReq.getBatchInfo().size();
            this.routeDataDSs = routeDataDSs;
            groupIds = new String[reservedNum];
        }
        
        @Override
        public void run()
        {
            //初始化时间统计对象
            TimeStatis ts =
                new TimeStatis(String.format("[groups_prepare(tid=%s,ts=%s)] reserve batchData asynchronously!",
                    rbdReq.getTransactionId(),
                    rbdReq.getTimestamp()));
            ts.startTiming();
            
            //构造reserveBatchDataResp方法的请求体
            GroupsPrepareResultReq req = new GroupsPrepareResultReq();
            req.setTransaction_id(rbdReq.getTransactionId());
            RecordQueue<UserProfileEntity> recordQueue = null;
            try
            {
                //初始化时间统计对象
                TimeStatis tsPrepare =
                    new TimeStatis(String.format("[groups_prepare(tid=%s,ts=%s)] step 1. prepare!",
                        rbdReq.getTransactionId(),
                        rbdReq.getTimestamp()));
                tsPrepare.startTiming();
                
                //获取用户标识
                int userAdFlag = OdpCommonUtils.extractUserAdFlag(rbdReq.getFilterStmt());
                
                //预留信息数组
                ReservedInfo[] reservedInfos = getReservedInfo(rbdReq.getAppId(), rbdReq.getBatchInfo(), userAdFlag);
                
                //获取表字段名集合
                ColumnFieldTypeMapping[] cftMapping = getColumnFieldTypeMappings(rbdReq.getExtractList(), resultMapID);
                
                //创建结果表
                String[] resultTableNames = createResultTables(reservedInfos, cftMapping);
                
                //预留数据文件名
                String[] rfileNames = getReservedFileNames(reservedInfos);
                
                //获取字段名
                String columnNames = covert2QueryFields(rbdReq.getExtractList());
                
                SelectSqlExector exector = null;
                TempTableRecordProducter<UserProfileEntity> producter = null;
                TempTableRecordProcessParam<UserProfileEntity> param = null;
                
                List<SelectSqlExector> exectorThds = new ArrayList<SelectSqlExector>();
                
                recordQueue = new RecordQueue<UserProfileEntity>(relimit);
                
                //启动多个线程处理
                for (BasicDataSource dataDs : routeDataDSs)
                {
                    //创建sql执行器，并发执行查询语句
                    if (null != dataDs)
                    {
                        //构造参数
                        param = new TempTableRecordProcessParam<UserProfileEntity>();
                        param.setColumnFieldTypes(cftMapping);
                        param.setRecordClass(UserProfileEntity.class);
                        param.setRecordQueue(recordQueue);
                        param.setResultMapID(resultMapID);
                        param.setOdpDao(odpDao);
                        
                        //构造处理对象
                        producter = new TempTableRecordProducter<UserProfileEntity>(param);
                        exector =
                            new SelectSqlExector(selectSql, dataDs, producter, new TimeStatis(
                                String.format("[groups_prepare(tid=%s,ts=%s)] "
                                    + "step 2.X. select the records from database[%s]!",
                                    rbdReq.getTransactionId(),
                                    rbdReq.getTimestamp(),
                                    dataDs.getUrl())));
                        //ThreadPoolUtil.submmitTask(exector);
                        exectorThds.add(exector);
                        //启动线程
                        exector.start();
                    }
                }
                
                tsPrepare.endTiming();
                
                //记录查询的服务器地址
                if (routeDataDSs.isEmpty())
                {
                    LOGGER.error("query records from database server[{}]", routeDataDSs);
                }
                else
                {
                    LOGGER.info("query records from database server[{}]", routeDataDSs);
                }
                
                //等待所有执行器执行完毕
                for (SelectSqlExector exectorThd : exectorThds)
                {
                    exectorThd.join();
                }
                
                //检查select查询是否执行成功
                for (SelectSqlExector exectorThd : exectorThds)
                {
                    exectorThd.checkSucess();
                }
                
                //初始化时间统计对象
                TimeStatis tsProcess =
                    new TimeStatis(String.format("[groups_prepare(tid=%s,ts=%s)] step 3. process records!",
                        rbdReq.getTransactionId(),
                        rbdReq.getTimestamp()));
                tsProcess.startTiming();
                
                //处理查询到的记录并返回每批的实际记录数
                //参数一定是合法的，recordNums不可能为null，所以不用检查
                //即使recordNums为null，也会正常抛出异常
                long[] recordNums;
                //获取user_ad_flag标识
                
                if (UserAdFlagType.BLANK == userAdFlag)
                {
                    //白名单用户用于测试
                    recordNums = processRecords(cftMapping, recordQueue, resultTableNames, rfileNames);
                }
                else
                {
                    //不会出现红名单用户（参数校验时已经过滤）
                    //普通用户需要进行防打扰过滤
                    recordNums =
                        nProcessRecords(cftMapping, resultTableNames, rfileNames, rbdReq.getBatchInfo(), recordQueue);
                }
                
                //设置每批的记录总数
                for (int i = 0; i < reservedNum; i++)
                {
                    reservedInfos[i].setRecordCount(recordNums[i]);
                }
                
                //设置请求参数
                req.setResult_code(0);
                req.setGroup_info(cov2GroupInfos(reservedInfos));
                
                //更新临时表预留数据文件名
                saveReservedInfo(reservedInfos, columnNames, resultTableNames, rfileNames);
                
                //记录审计记录
                auditProcess.logAuditInfos(rbdReq.getAppId(),
                    OperatorType.RESERVE_DATA,
                    rbdReq.getAppTransactionKey(),
                    reservedInfos);
                
                //记录处理数据的时间
                tsProcess.endTiming();
            }
            catch (InterruptedException e)
            {
                //有任何一个线程终止，均认为执行出错
                LOGGER.error("SelectSqlExector interrupted!", e);
                //清理线程资源
                OdpCommonUtils.setResultByException(req, e);
            }
            catch (CException e)
            {
                //记录错误信息并设置返回码
                LOGGER.error("AsyncReserveBatchData run failed! this is {}", this, e);
                OdpCommonUtils.setResultByException(req, e);
            }
            catch (Exception e)
            {
                //记录错误信息并设置返回码
                LOGGER.error("AsyncReserveBatchData run failed! this is {}", this, e);
                OdpCommonUtils.setResultByException(req, e);
            }
            finally
            {
                //释放数据查询资源
                userProcess.releaseDataQuery();
            }
            
            //初始化时间统计对象
            TimeStatis tsClien =
                new TimeStatis(String.format("[groups_prepare(tid=%s,ts=%s)] step 4. callback the %s!",
                    rbdReq.getTransactionId(),
                    rbdReq.getTimestamp(),
                    rbdReq.getCallbackURL()));
            tsClien.startTiming();
            //交给稳定的服务发送，当前直接发送
            try
            {
                dataQueryClient.reserveBatchDataResp(rbdReq.getCallbackURL(), req);
            }
            catch (Exception e)
            {
                LOGGER.error("dataQueryClient.reserveBatchDataResp failed! callbackURL is {}, req is {}.",
                    rbdReq.getCallbackURL(),
                    req);
            }
            tsClien.endTiming();
            
            //统计执行时间
            ts.endTiming();
        }
        
        //转换预留信息为组信息
        private List<GroupInfo> cov2GroupInfos(ReservedInfo[] reservedInfos)
        {
            List<GroupInfo> groupInfos = new ArrayList<GroupInfo>();
            if (null != reservedInfos)
            {
                GroupInfo groupInfo = null;
                for (ReservedInfo reservedInfo : reservedInfos)
                {
                    groupInfo = new GroupInfo();
                    groupInfo.setDate(reservedInfo.getDate());
                    groupInfo.setGroup_id(reservedInfo.getReserveId());
                    groupInfo.setRecord_count(reservedInfo.getRecordCount());
                    groupInfos.add(groupInfo);
                }
            }
            
            return groupInfos;
        }
        
        /**
         * 保存预留信息数组、列名字，结果表名数组，文件名数组
         */
        private void saveReservedInfo(ReservedInfo[] reservedInfos, String columnNames, String[] resTableName,
            String[] rfileNames)
            throws CException
        {
            //更新失效时间
            ReservedInfoEntity reservedE = new ReservedInfoEntity();
            for (int i = 0; i < reservedNum; i++)
            {
                //更新预留信息的临时表名、字段列表、文件名、结束标识、过期时间
                reservedE.setReserveId(reservedInfos[i].getReserveId());
                if (writeRecords)
                {
                    reservedE.setResultTableName(resTableName[i]);
                    reservedE.setFileUrl(rfileNames[i]);
                }
                reservedE.setDays(days);
                reservedE.setColumnNameList(columnNames);
                reservedE.setState(ReservedStateType.FINISH);
                userProcess.updateReservedInfo(reservedE);
            }
        }
        
        /** 
         * 处理查询到的记录
         */
        private long[] processRecords(ColumnFieldMapping[] cfMappings, RecordQueue<UserProfileEntity> recordQueue,
            String[] resultTableName, String[] rfileNames)
            throws CException
        {
            //获取比例
            int[] ratios = getRatios(rbdReq.getBatchInfo());
            
            //消费者
            TempTableRecordProcess<UserProfileEntity> tableProcess =
                new TempTableRecordProcess<UserProfileEntity>(cfMappings, resultTableName, rfileNames, ratios,
                    recordQueue);
            //传递dao对象
            tableProcess.setOdpDao(odpDao);
            //将记录写入到临时表中和文件中
            return tableProcess.writeRecords(writeRecords, rbdReq.getTransactionId(), rbdReq.getTimestamp());
        }
        
        /** 
         * 处理查询到的记录[普通用户-过滤]
         */
        private long[] nProcessRecords(ColumnFieldMapping[] cfMappings, String[] resTableNames, String[] rfileNames,
            List<DateRatioInfo> batchs, RecordQueue<UserProfileEntity> recordQueue)
            throws CException
        {
            //消费者
            NormalUsersProcess<UserProfileEntity> tableProcess =
                new NormalUsersProcess<UserProfileEntity>(cfMappings, resTableNames, rfileNames, batchs, recordQueue);
            //传递dao对象
            tableProcess.setOdpDao(odpDao);
            //将记录写入到临时表中和文件中
            return tableProcess.writeRecords(writeRecords,
                this.limit,
                this.groupIds,
                rbdReq.getTransactionId(),
                rbdReq.getTimestamp());
        }
        
        /** 
         * 获取比例
         */
        private int[] getRatios(List<DateRatioInfo> batchs)
        {
            //获取比例数组
            int[] ratios = new int[reservedNum];
            DateRatioInfo dateRatio = null;
            for (int i = 0; i < reservedNum; i++)
            {
                dateRatio = batchs.get(i);
                ratios[i] = dateRatio.getRatio();
            }
            return ratios;
        }
        
        //获取预留信息
        private ReservedInfo[] getReservedInfo(String appId, List<DateRatioInfo> batchs, int userAdFlag)
            throws CException
        {
            //预留信息数组
            ReservedInfo[] reservedInfos = new ReservedInfo[reservedNum];
            
            ReservedInfo reservedInfo = null;
            DateRatioInfo dateRatio = null;
            Date date = null;
            //创建预留信息
            for (int i = 0; i < reservedNum; i++)
            {
                dateRatio = batchs.get(i);
                reservedInfo = new ReservedInfo();
                reservedInfo.setDate(dateRatio.getDate());
                date = toDay(dateRatio.getDate());
                //创建预留信息并获取预留ID号
                reservedInfo.setReserveId(createReservedInfo(appId,
                    userAdFlag,
                    date,
                    OdpCommonUtils.getNextMondayDateStr(date)));
                
                reservedInfos[i] = reservedInfo;
                
                groupIds[i] = reservedInfo.getReserveId();
            }
            
            return reservedInfos;
        }
        
        //转换成当天日期
        private Date toDay(String dateStr)
            throws CException
        {
            //转换日期
            Date date = CommonUtils.convertDateTimeFormat(dateStr, null);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            
            return cal.getTime();
        }
        
        /**
         * 创建结果表
         */
        private String[] createResultTables(ReservedInfo[] reservedInfos, ColumnFieldMapping[] columnFields)
            throws CException
        {
            String[] tempTableName = new String[reservedNum];
            
            for (int i = 0; i < reservedNum; i++)
            {
                //构建临时表名
                tempTableName[i] = getTempTableName(tableName, reservedInfos[i].getReserveId());
                if (writeRecords)
                {
                    //创建临时表
                    userProcess.createTempTable(tempTableName[i], columnFields);
                }
            }
            
            return tempTableName;
        }
        
        /**
         * 创建临时表
         */
        private String[] getReservedFileNames(ReservedInfo[] reservedInfos)
            throws CException
        {
            String[] reservedFiles = new String[reservedNum];
            if (writeRecords)
            {
                for (int i = 0; i < reservedNum; i++)
                {
                    //构建预留文件名
                    reservedFiles[i] =
                        OdpConfig.createOdpConfig().getRfilesDir()
                            + getTempTableName(tableName, reservedInfos[i].getReserveId());
                }
            }
            return reservedFiles;
        }
    }
    
    /**
     * 
     * <异步文件上传处理>
     * 
     * @author  w00190105
     * @version [Open Data Platform Service, 2013-4-21]
     * @see  [相关类/方法]
     */
    private class AsyncFileUpload implements Runnable
    {
        //文件ID
        private String fileId;
        
        //数据连接
        private OdpDao odpDao;
        
        //默认构造函数
        AsyncFileUpload(String fileId, OdpDao odpDao)
        {
            this.fileId = fileId;
            this.odpDao = odpDao;
        }
        
        @Override
        public void run()
        {
            FileUploadTableEntity entity = new FileUploadTableEntity();
            entity.setFileId(fileId);
            entity.setFlag(FileDealFlagType.BEGIN);
            
            try
            {
                //开始处理
                this.odpDao.updateFileUpLoadTableInfo(entity);
                
                String filePath = CommonUtils.getSysConfig("file_local_path");
                
                String fileName = filePath + File.separator + fileId + ".zip";
                
                String fileDir = filePath + File.separator + fileId;
                
                //文件解压并导入数据表
                OdpFileUtils.upZipFile(fileName, fileDir);
                
                //创建数据表
                String tmpTableName = getTempTableName(upLoadFileTableName, fileId);
                
                List<String> fileList = OdpFileUtils.getSubFiles(new File(fileDir));
                
                String createTableSql =
                    "CREATE TABLE `" + tmpTableName + "` (`device_id` varchar(255) NOT NULL DEFAULT '',"
                        + "PRIMARY KEY (`device_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8";
                //获取数据表连接
                List<BasicDataSource> routeDataDSs = new ArrayList<BasicDataSource>();
                
                Set<Map.Entry<Integer, BasicDataSource>> set = dbServerDSs.entrySet();
                
                for (Iterator<Map.Entry<Integer, BasicDataSource>> it = set.iterator(); it.hasNext();)
                {
                    Map.Entry<Integer, BasicDataSource> entry = (Map.Entry<Integer, BasicDataSource>)it.next();
                    routeDataDSs.add(entry.getValue());
                }
                
                //循环处理数据
                for (BasicDataSource ds : routeDataDSs)
                {
                    if (null != ds)
                    {
                        //创建数据表，然后再导入数据
                        Connection conn = null;
                        PreparedStatement stmt = null;
                        ResultSet rs = null;
                        
                        try
                        {
                            //创建数据表
                            conn = ds.getConnection();
                            stmt = conn.prepareStatement(createTableSql);
                            stmt.execute();
                            
                            //导入数据
                            for (String filelocalPath : fileList)
                            {
                                String tfilePathString = filelocalPath.replaceAll("\\\\", "\\\\\\\\");
                                String loadSql =
                                    "load data local infile '" + tfilePathString + "' into TABLE " + tmpTableName
                                        + " FIELDS TERMINATED BY '\001' ESCAPED BY '' "
                                        + "LINES TERMINATED BY '\r\n' ( @device_id ) "
                                        + "SET device_id = nullif(@device_id,'')";
                                
                                stmt.execute(loadSql);
                            }
                            
                        }
                        catch (Throwable e)
                        {
                            //执行失败
                            LOGGER.error("execSql failed! exception is {}", e);
                        }
                        finally
                        {
                            //关闭流
                            DbUtils.closeQuietly(conn, stmt, rs);
                        }
                    }
                }
                
                //处理成功
                entity.setFlag(FileDealFlagType.SUCCESS);
                this.odpDao.updateFileUpLoadTableInfo(entity);
            }
            catch (CException e)
            {
                LOGGER.error("AsyncFileUpload deal ERROR! Exception is {}", e);
                //处理失败
                entity.setFlag(FileDealFlagType.ERROR);
                try
                {
                    this.odpDao.updateFileUpLoadTableInfo(entity);
                }
                catch (Exception e2)
                {
                    LOGGER.error("update file upload error deal error! exception is {}", e2);
                }
                
            }
            
        }
    }
    
    /**
     * <检查文件ID是否合法>
     * @param fileId   文件ID
     * @throws CException 文件ID检查异常
     * @see [类、类#方法、类#成员]
     */
    public void checkFileId(String fileId)
        throws CException
    {
        if (null != fileId && !fileId.isEmpty())
        {
            int flag = getFileDealFlag(fileId);
            if (ReservedFileStateType.SUCCESS == flag)
            {
                return;
            }
            else if (ReservedFileStateType.UN_EXIST == flag)
            {
                //抛文件不存在异常
                throw new CException(ResultCode.FILE_UN_EXIST, "fileid is not exit");
            }
            else if (ReservedFileStateType.ERROR == flag)
            {
                //抛文件处理失败异常
                throw new CException(ResultCode.FILE_DEAL_ERROR, "file deal error");
            }
            else if (ReservedFileStateType.DEALING == flag)
            {
                //抛文件处理中异常
                throw new CException(ResultCode.FILE_DEAL_ING, "file dealing");
            }
        }
        
    }
    
    /**
     * <获取文件处理状态>
     * @param fileId   文件ID
     * @return 0 不存在；1 处理中；2 处理成功；3 处理失败
     * @see [类、类#方法、类#成员]
     */
    private int getFileDealFlag(String fileId)
    {
        try
        {
            FileUploadTableEntity entity = this.odpDao.getFileUploadTableInfo(fileId);
            if (null == entity)
            {
                return ReservedFileStateType.UN_EXIST;
            }
            int flag = entity.getFlag();
            
            if (FileDealFlagType.SUCCESS == flag)
            {
                return ReservedFileStateType.SUCCESS;
            }
            else if (FileDealFlagType.ERROR == flag)
            {
                return ReservedFileStateType.ERROR;
            }
            else
            {
                return ReservedFileStateType.DEALING;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("checkFileId error! fileid is {} exception is {}", new Object[] {fileId, e});
            return ReservedFileStateType.UN_EXIST;
        }
    }
    
    /**
     * <处理文件上传入库并生成ID>
     * @param rsp           统一封装的接口返回
     * @param fileItemList  fileItem列表
     * @throws CException   处理文件上传入库并生成ID异常
     * @see [类、类#方法、类#成员]
     */
    public void dealFileUpload(FileUploadRsp rsp, List<FileItem> fileItemList)
        throws CException
    {
        //生成文件ID
        String fileId = OdpFileUtils.getFileId();
        
        String filePath = CommonUtils.getSysConfig("file_local_path");
        
        String fileName = filePath + File.separator + fileId + ".zip";
        
        String fileDir = filePath + File.separator + fileId;
        
        //文件保存在本地
        for (int i = 0; i < fileItemList.size(); i++)
        {
            FileItem fileItem = fileItemList.get(i);
            if (!fileItem.isFormField())
            {
                
                if (fileItem.getSize() > 0)
                {
                    try
                    {
                        File file = new File(filePath);
                        if (!file.exists())
                        {
                            file.mkdir();
                        }
                        fileItem.write(new File(fileName));
                    }
                    catch (Exception e)
                    {
                        //记录日志抛出异常
                        LOGGER.error("load imei file to local error! exception is {}", new Object[] {e});
                        //抛异常
                        throw new CException(ResultCode.WRITE_FILE_ERROR, e);
                    }
                }
            }
        }
        
        //插入记录
        FileUploadTableEntity entity = new FileUploadTableEntity();
        entity.setFileId(fileId);
        entity.setLoadTime(new Date());
        entity.setFlag(FileDealFlagType.UN_BEGIN);
        this.odpDao.insertFileUploadTableInfo(entity);
        
        int dealType = Integer.parseInt(CommonUtils.getSysConfig("file.deal.switch"));
        if (FileInterfaceDealType.SYNC == dealType)
        {
            
            syncFileUpload(fileId);
        }
        else
        {
            //异步处理
            //提交线程池
            AsyncFileUpload asyncExector = new AsyncFileUpload(fileId, this.odpDao);
            ThreadPoolUtil.submmitTask(asyncExector);
        }
        
        rsp.setFile_id(fileId);
    }
    
    /**
     * <同步文件上传处理>
     * @param fileId  文件ID
     * @throws CException  同步文件上传处理异常
     * @see [类、类#方法、类#成员]
     */
    private void syncFileUpload(String fileId)
        throws CException
    {
        FileUploadTableEntity entity = new FileUploadTableEntity();
        entity.setFileId(fileId);
        String filePath = CommonUtils.getSysConfig("file_local_path");
        
        String fileName = filePath + File.separator + fileId + ".zip";
        
        String fileDir = filePath + File.separator + fileId;
        
        //同步处理,开始处理
        entity.setFlag(FileDealFlagType.BEGIN);
        this.odpDao.updateFileUpLoadTableInfo(entity);
        
        //文件解压并导入数据表
        OdpFileUtils.upZipFile(fileName, fileDir);
        
        //创建数据表
        String tmpTableName = getTempTableName(upLoadFileTableName, fileId);
        
        List<String> fileList = OdpFileUtils.getSubFiles(new File(fileDir));
        
        String createTableSql =
            "CREATE TABLE `" + tmpTableName + "` (`device_id` varchar(255) NOT NULL DEFAULT '',"
                + "PRIMARY KEY (`device_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8";
        //获取数据表连接
        List<BasicDataSource> routeDataDSs = new ArrayList<BasicDataSource>();
        
        Set<Map.Entry<Integer, BasicDataSource>> set = dbServerDSs.entrySet();
        
        for (Iterator<Map.Entry<Integer, BasicDataSource>> it = set.iterator(); it.hasNext();)
        {
            Map.Entry<Integer, BasicDataSource> entry = (Map.Entry<Integer, BasicDataSource>)it.next();
            routeDataDSs.add(entry.getValue());
        }
        
        //循环处理数据
        for (BasicDataSource ds : routeDataDSs)
        {
            if (null != ds)
            {
                //创建数据表，然后再导入数据
                Connection conn = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;
                
                try
                {
                    //创建数据表
                    conn = ds.getConnection();
                    stmt = conn.prepareStatement(createTableSql);
                    stmt.execute();
                    
                    //导入数据
                    for (String filelocalPath : fileList)
                    {
                        String tfilePathString = filelocalPath.replaceAll("\\\\", "\\\\\\\\");
                        String loadSql =
                            "load data local infile '" + tfilePathString + "' into TABLE " + tmpTableName
                                + " FIELDS TERMINATED BY '\001' ESCAPED BY '' "
                                + "LINES TERMINATED BY '\r\n' ( @device_id ) SET device_id = nullif(@device_id,'')";
                        
                        stmt.execute(loadSql);
                    }
                    
                }
                catch (Throwable e)
                {
                    //执行失败
                    LOGGER.error("execSql failed! exception is {}", e);
                    entity.setFlag(FileDealFlagType.ERROR);
                    this.odpDao.updateFileUpLoadTableInfo(entity);
                }
                finally
                {
                    //关闭流
                    DbUtils.closeQuietly(conn, stmt, rs);
                }
            }
        }
        
        entity.setFlag(FileDealFlagType.SUCCESS);
        this.odpDao.updateFileUpLoadTableInfo(entity);
        
    }
    
    /**
     * 获取用户信息处理对象
     * @return 用户信息处理对象
     */
    public UserProfileProcess getUserProcess()
    {
        return this.userProcess;
    }
    
    /**
     * 设置用户信息处理对象
     * @param userProcess 用户信息处理对象
     */
    public void setUserProcess(UserProfileProcess userProcess)
    {
        this.userProcess = userProcess;
    }
    
    /**
     * 获取数据查询客户端
     * @return 数据查询客户端
     */
    public DataQueryClient getDataQueryClient()
    {
        return dataQueryClient;
    }
    
    /**
     * 设置数据查询客户端
     * @param dataQueryClient 数据查询客户端
     */
    public void setDataQueryClient(DataQueryClient dataQueryClient)
    {
        this.dataQueryClient = dataQueryClient;
    }
    
    /**
     * 获取Odp用户信息数据库连接池
     * @return Odp用户信息数据库连接池
     */
    public OdpDataSource getOdpProfileDS()
    {
        return odpProfileDS;
    }
    
    /**
     * 设置Odp用户信息数据库连接池
     * @param odpProfileDS Odp用户信息数据库连接池
     */
    public void setOdpProfileDS(OdpDataSource odpProfileDS)
    {
        this.odpProfileDS = odpProfileDS;
    }
    
    /**
     * 获取dao对象
     * @return dao对象
     */
    public OdpDao getOdpDao()
    {
        return odpDao;
    }
    
    /**
     * 设置dao对象
     * @param odpDao dao对象
     */
    public void setOdpDao(OdpDao odpDao)
    {
        this.odpDao = odpDao;
    }
    
    /**
     * 获取审计处理对象
     * @return 审计处理对象
     */
    public AuditProcess getAuditProcess()
    {
        return auditProcess;
    }
    
    /**
     * 设置审计处理对象
     * @param auditProcess 审计处理对象
     */
    public void setAuditProcess(AuditProcess auditProcess)
    {
        this.auditProcess = auditProcess;
    }
    
    /**
     * 获取用户管理
     * @return 用户管理
     */
    public UserManagement getUserManagement()
    {
        return userManagement;
    }
    
    /**
     * 设置userManagement
     * @param userManagement 用户管理对象
     */
    public void setUserManagement(UserManagement userManagement)
    {
        this.userManagement = userManagement;
    }
}
