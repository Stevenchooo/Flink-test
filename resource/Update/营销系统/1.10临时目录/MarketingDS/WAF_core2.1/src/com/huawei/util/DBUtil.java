package com.huawei.util;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;

import com.huawei.waf.core.config.sys.SysConfig;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.core.run.ConsistentHashBalancer;
import com.huawei.waf.facade.IBalancer;
import com.huawei.waf.protocol.RetCode;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author l00152046
 * 数据库工具类，包括连接池管理、简单的数据库接口等，
 * 支持简单的负载均衡，通常用在读写分离的情况，一个写入，多个读取
 */
@SuppressWarnings("unchecked")
public class DBUtil {
	private static final Logger LOG = LogUtil.getInstance();
    
	protected static final String DB_NAME = "name";
    protected static final String DB_SOURCES = "sources";
    protected static final String DB_DEFAULTSETTINGS = "defaultSettings";
    protected static final String DB_HOSTS = "hosts";
    protected static final String DB_BALANCER = "balancer";
    
    /**
     * 默认的连接池，如果method.process的配置中没有指定dataSource，
     * 则使用这里默认的数据库连接池。如果无default配置，则默认用第一个
     * 数据库连接池为默认连接池
     */
    protected static final String DB_DEFAULT_SOURCE = "default";
    
    /**
     * oracle/postgresql等数据库的存储过程不能直接select出结果集
     * 而需要通过在参数中增加一个out类型的cursor参数，依次来返回结果集，
     * 所以这种类型的数据库需要在连接池上配置resultSetRef，
     * 对于oracle配置为-10，postgresql配置成2006，
     * 其他数据库可以不用配置，默认为Integer.MIN_VALUE，即为无效值，
     * 如果是无效值，系统在接受结果集时将直接调用statement.getResultSet()
     */
    protected static final String DB_RESULTSET_REF = "resultSetRef";
    
    private static String defaultSource = null;
    
    private static IBalancer balancer = new ConsistentHashBalancer();

	private static final Map<String, DBSource> pools = new HashMap<String, DBSource>();
	
	/**
	 * 汇总数据库的名称，一般用在汇总排序查询中
	 */
    private static String collectDbName = "collect";
	
	public static boolean init(Map<String, Object> map) {
		//数据库负责均衡策略
        String balancerCls = JsonUtil.getAsStr(map, DB_BALANCER, null);
        if(!Utils.isStrEmpty(balancerCls)) {
        	if((balancer = loadBalancer(balancerCls)) == null) {
        		LOG.error("Fail to load balancer {}", balancerCls);
        		return false;
        	}
        }
        
		defaultSource = JsonUtil.getAsStr(map, DB_DEFAULT_SOURCE, null);
	    Map<String, Object> defaultSettings = JsonUtil.getAsObject(map, DB_DEFAULTSETTINGS);
	    if(defaultSettings == null) {
	        defaultSettings = new HashMap<String, Object>();
	    }
	    List<Object> configs = JsonUtil.getAsList(map, DB_SOURCES);
	    if(configs == null) {
	    	LOG.error("There are no {} config", DB_SOURCES);
	    	return false;
	    }

	    try {
	        for (Object cfg : configs) {
	            if(cfg == null || !(cfg instanceof Map<?,?>)) {
	                LOG.error("Wrong config item: {}", (cfg == null ? "null" : cfg.toString()));
	                return false;
	            }

	            Map<String, Object> cc = (Map<String, Object>)cfg;
	            Map<String, Object> c = new HashMap<String, Object>(defaultSettings);
	            c.putAll(cc); //合并配置项
                String name = JsonUtil.getAsStr(c, DB_NAME, Utils.genUUID_64());
                
                List<Object> srcs = JsonUtil.getAsList(c, DB_HOSTS);
                int num = srcs == null ? 0 : srcs.size();
                ComboPooledDataSource[] srcList = null;
                ComboPooledDataSource src;
                if(num == 0) { //可以不用配置多个
                    srcList = new ComboPooledDataSource[1];
                    if((src = buildSource(name, null, c)) == null) {
                        LOG.error("Fail to load db config {}", name);
                        return false;
                    }
                    srcList[0] = src;
                } else {
                    srcList = new ComboPooledDataSource[num];
    	            for(int i = 0; i < num; i++) {
                        if((src = buildSource(name, JsonUtil.getAsObject(srcs, i), c)) == null) {
                            LOG.error("Fail to load db config {}", name);
                            return false;
                        }
                        srcList[i] = src;
    	            }
                }
                
                if(defaultSource == null) {
                    defaultSource = name;
                }
                LOG.info("Add {} rdb source(s) for {}", srcList.length, name);
                pools.put(name, new DBSource(srcList, JsonUtil.getAsInt(c, DB_RESULTSET_REF, Integer.MIN_VALUE)));
	        }
            
        	balancer.setNodes(pools.keySet());
		} catch(Exception e) {
			LOG.error("Fail to load rdb config", e);
			return false;
		}
		return true;
	}

	private static ComboPooledDataSource buildSource(String name, Map<String, Object> host, Map<String, Object> srcCfg)
	        throws IllegalAccessException, InvocationTargetException {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        Map<String, Object> hostCfg = new HashMap<String, Object>(srcCfg); //先拷贝上层的配置信息
        if(host != null) {
            hostCfg.putAll(host); //再覆盖每个host的配置
        }
        
        if(!hostCfg.containsKey("jdbcUrl")) {
            LOG.error("No jdbcUrl in {}", name);
            return null;
        }
        
        String key;
        String val;
        for(Map.Entry<String, Object> prop : hostCfg.entrySet()) {
            key = prop.getKey();
            if(key.equals("password")) {
                val = prop.getValue().toString().trim();
                if(!Utils.isStrEmpty(val)) {
                    val = EncryptUtil.configDecode(val);
                	if(val == null) {
                		LOG.error("Fail to decode db user password");
                		return null;
                	}
                }
                BeanUtils.setProperty(cpds, key, val);
            } else if(key.equals("jdbcUrl")) {
                int pos = 0, end;
                val = prop.getValue().toString().trim();
                
                while((pos = val.indexOf('{')) >= 0) {
                    pos++;
                    end = val.indexOf('}', pos);
                    if(end <= 0) {
                        break;
                    }
                    String macro = val.substring(pos, end);
                    val = val.replace("{" + macro + '}', SysConfig.getConfigValue(macro));
                }
                BeanUtils.setProperty(cpds, key, val);
            } else {
                BeanUtils.setProperty(cpds, key, prop.getValue());
            }
        }
        
	    return cpds;
	}
	
	/**
	 * 关闭所有连接池
	 * @return
	 */
	public static boolean destroy() {
		try {
		    for(Map.Entry<String, DBSource> o : pools.entrySet()) {
		        LOG.info("Close ComboPooledDataSource {}", o.getKey());
		        o.getValue().destroy();
		    }
		} catch(Exception e) {
			LOG.error("Fail to destroy connection", e);
			return false;
		}
		return true;
	}
	
	public static DBConnection getConnection(boolean transaction) {
	    return getConnection(getDefaultSource(), transaction, -1);
	}
	
	public static final String getDefaultSource() {
	    return defaultSource;
	}
	
	/**
	 * 获取数据库连接池
	 * @param poolName
	 * @param transaction 是否启动事务
	 * @param dbNo 集群中的编号
	 * @return
	 */
	public static DBConnection getConnection(String poolName, boolean transaction, int dbNo) {
	    DBSource source = pools.get(poolName);
	    if(source == null) {
	        LOG.error("Fail to get source from {}", poolName);
	        return null;
	    }
	    
        DBConnection conn = source.getConnection(transaction, dbNo);
        if(conn == null) {
            LOG.error("Fail to get connection from {}", poolName);
        }
        return conn;
	}
	
	public static DBConnection getConnection(String poolName, boolean transaction) {
		return getConnection(poolName, transaction, -1);
	}
	
	/**
	 * 释放数据库连接，如果成功、且是事务连接，则在释放是会提交事务，否则回滚
	 * @param conn
	 * @param success 执行是否成功
	 */
	public static boolean freeConnection(DBConnection conn, boolean success) {
	    if(conn == null) {
	        return false;
	    }
		return conn.free(success);
	}
	
	public static void closeStatement(Statement statement) {
		if(statement != null) {
			try {
				statement.close();
			} catch(Exception e) {
				LOG.error("Fail to close statement", e);
			}
		}
	}
	
    /**
     * @author l00152046
     * 提供轮询方式的负载均衡，通常用在读写分离，一个写入库，多个读取库
     */
    private static class DBSource {
        private AtomicInteger no = new AtomicInteger(0);
        private int resultSetRef = Integer.MIN_VALUE;
        private int num = 0;
        private ComboPooledDataSource[] sources = null;
        
        public DBSource(ComboPooledDataSource[] sources, int resultSetRef) {
            this.sources = sources;
            this.num = sources.length;
            this.resultSetRef = resultSetRef;
        }
        
        public DBConnection getConnection(boolean transaction, int dbNo) {
        	ComboPooledDataSource src = null;
        	if(dbNo < 0) {
        	    dbNo = no.getAndIncrement() % this.num; //轮询方式的负载均衡
        	}
            
        	src = this.sources[dbNo];
            if(LOG.isDebugEnabled()) {
                LOG.debug("Get connection from source {}", src.getJdbcUrl());
            }
            
            try {
                Connection conn = src.getConnection();
                return new DBConnection(conn, transaction, this.resultSetRef, src.getJdbcUrl(), dbNo);
            } catch (SQLException e) {
                LOG.error("Fail to get connection from {}", src.getJdbcUrl());
            }
            
            return null;
        }
        
        public void destroy() {
            for(ComboPooledDataSource src : sources) {
                src.close();
            }
        }
    }
    
	/**
	 * @author l00152046
	 * 连接管理类，记录了事务、不同数据库resultset的类型值
	 */
	public static class DBConnection {
	    private Connection conn;
	    
	    private boolean transaction = false; //是否启动了事务
	    
	    private int no;
	    
	    /**
	     * 当使用oracle、postgresql时，因为不能从存储过程中直接返回结果集，
	     * 而是用cursor方式作为参数返回，所以当设置存储过程参数时，需要设置它们，
	     * resultSetRef就是用来指定cursor类型的，oracle中为-10，postgresql为2006
	     * 其他的数据库不必设置
	     */
	    private int resultSetRef = Integer.MIN_VALUE;
	    
	    private String name;
	    
	    public DBConnection(Connection conn, boolean transaction, int resultSetRef, String name, int no) throws SQLException {
	        this.conn = conn;
	        this.transaction = transaction;
	        this.resultSetRef = resultSetRef;
	        if(this.transaction) {
	            this.conn.setAutoCommit(false);
	        }
	        this.name = name;
	        this.no = no;
	    }
	    
	    /**
	     * 是否连接，commit指示提交/回滚事务
	     * @param commit
	     */
	    public boolean free(boolean commit) {
            try {
    	        if(transaction) {
    	            if(commit) {
    	                this.conn.commit();
    	            } else {
    	                this.conn.rollback();
    	            }
    	        }
    	        this.conn.clearWarnings();
                this.conn.close(); // c3p0并不是真正关闭数据库连接
                return true;
            } catch (SQLException e) {
                LOG.error("Fail to free connection, commit({})", commit, e);
            }
            return false;
	    }
	    
	    public Connection getConnection() {
	        return this.conn;
	    }
	    
	    public static final boolean isResultSetInCursor(int resultSetRef) {
	        return !(resultSetRef == Integer.MIN_VALUE);
	    }
	    
	    public int getResultSetRef() {
	        return resultSetRef;
	    }
	    
	    public String getName() {
	    	return name;
	    }
	    
	    /**
	     * 在集群中的序号
	     * @return
	     */
	    public int getNo() {
	    	return no;
	    }
	}
	
	/**
	 * 执行数据库，无结果集处理
	 * @param conn 数据库连接
	 * @param sql
	 * @param needOut 是否需要通过out型参数返回执行结果，只在存储过程的情况有效
	 * @param parameters
	 * @return
	 */
	public static final int execute(Connection conn, String sql, boolean needOut, Object[] parameters) {
        CallableStatement statement = null;
        int retCode = RetCode.OK;
        int idx = 1;

        try {
            if ((statement = conn.prepareCall(sql)) == null) {
                return RetCode.INTERNAL_ERROR;
            }
            
            if(needOut) {
                statement.registerOutParameter(idx++, Types.INTEGER);
            }
            
            if(parameters != null) {
                for (Object o : parameters) {
                    if(o != null) {
                        statement.setString(idx, o.toString());
                    } else {
                        statement.setNull(idx, java.sql.Types.VARCHAR);
                    }
                    idx++;
                }
            }
            printStatement(statement, LOG);
            statement.execute();
            
            if(needOut) {
                retCode = statement.getInt(1);
            }
        } catch (Exception e) {
            if(WAFConfig.isUtilLogException()) {
                LOG.error("Fail to execute {}", sql, e);
            } else {
                LOG.error("Fail to execute {}", sql);
            }
            retCode = RetCode.INTERNAL_ERROR;
        } finally {
            DBUtil.closeStatement(statement);
        }
        return retCode;
	}
	
    public static final int execute(DBConnection conn, String sql, boolean needOut, Object[] parameters) {
        return execute(conn.getConnection(), sql, needOut, parameters);
    }
    
    public static final int execute(String sql, boolean needOut, Object[] parameters) {
    	DBConnection conn = DBUtil.getConnection(false);
    	int retCode = RetCode.OK;
    	
    	try {
    		retCode = execute(conn, sql, needOut, parameters);
    	} finally {
    		DBUtil.freeConnection(conn, retCode == RetCode.OK);
    	}
    	
    	return retCode;
    }
    
    /**
     * @author l00152046
     * 数据库批量操作
     * @param <T>
     */
    public static abstract class BatchDBAction<T> {
        private List<T> data;
        public abstract boolean setStatement(PreparedStatement statement, T line) throws SQLException;
        
        public BatchDBAction(List<T> data) {
            this.data = data;
        }
        
        public List<T> getData() {
            return data;
        }
    }
    
    /**
     * 批量执行sql，无返回码与结果集
     * @param conn
     * @param sql
     * @param action 数据库每行的行为
     * @return
     */
    public static final <T> int executeBatch(Connection conn, String sql, BatchDBAction<T> action) {
        CallableStatement statement = null;
        int retCode = RetCode.OK;
        boolean isAuto = true;

        try {
        	isAuto = conn.getAutoCommit();
        	if(isAuto) { //set时会向服务端发送请求，如果已经为false，则不必执行此操作
        		conn.setAutoCommit(false);
        	}
            if ((statement = conn.prepareCall(sql)) == null) {
                return RetCode.INTERNAL_ERROR;
            }
            
            List<T> data = action.getData(); 
            if(data != null && data.size() > 0) {
                for(T line : data) {
                    if(!action.setStatement(statement, line)) {
                        return RetCode.INTERNAL_ERROR;
                    }
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (Exception e) {
            if(WAFConfig.isUtilLogException()) {
                LOG.error("Fail to execute {}", sql, e);
            } else {
                LOG.error("Fail to execute {}", sql);
            }
            retCode = RetCode.INTERNAL_ERROR;
        } finally {
            DBUtil.closeStatement(statement);
            if(isAuto) {
            	try {
					conn.setAutoCommit(true);
				} catch (SQLException e) {
					LOG.error("Fail to set auto-commit", e);
				}
            }
        }
        return retCode;
    }
    
    /**
     * 批量执行sql，无返回码与结果集
     * @param source 数据源名称，在system.cfg的rdb中配置
     * @param sql
     * @param action 数据库每行的行为
     * @return
     */
    public static final <T> int executeBatch(String source, String sql, BatchDBAction<T> action) {
        DBUtil.DBConnection dbConn = null;
        int retCode = RetCode.OK;

        try {
            dbConn = DBUtil.getConnection(source, false);
            if (dbConn == null) {
                LOG.error("Fail to get rdb connection from {}", source);
                return RetCode.INTERNAL_ERROR;
            }
            retCode = executeBatch(dbConn.getConnection(), sql, action);
        }catch(Exception e) {
            if(WAFConfig.isUtilLogException()) {
                LOG.error("Fail to execute {} in {}", sql, source, e);
            } else {
                LOG.error("Fail to execute {} in {}", sql, source);
            }
            return RetCode.INTERNAL_ERROR;
        }finally {
            freeConnection(dbConn, retCode == RetCode.OK);
        }

        return retCode;
    }
    
    /**
     * 批量执行sql，无返回码与结果集
     * @param conn
     * @param sql
     * @param action 数据库每行的行为
     * @return
     */
    public static final <T> int executeBatch(DBConnection conn, String sql, BatchDBAction<T> action) {
        return executeBatch(conn.getConnection(), sql, action);
    }
    
    /**
     * 批量执行sql，无返回码与结果集
     * @param conn 数据库连接
     * @param sql
     * @param parameters 多行参数
     * @return
     */
    public static final int executeBatch(Connection conn, String sql, List<Object[]> parameters) {
        CallableStatement statement = null;
        int retCode = RetCode.OK;
        int idx = 1;

        try {
            if ((statement = conn.prepareCall(sql)) == null) {
                return RetCode.INTERNAL_ERROR;
            }
            
            if(parameters != null) {
                for(Object[] params : parameters) {
                    idx = 1;
                    for (Object o : params) {
                        if(o != null) {
                            statement.setString(idx, o.toString());
                        } else {
                            statement.setNull(idx, java.sql.Types.VARCHAR);
                        }
                        idx++;
                    }
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (Exception e) {
            if(WAFConfig.isUtilLogException()) {
                LOG.error("Fail to execute {}", sql, e);
            } else {
                LOG.error("Fail to execute {}", sql);
            }
            retCode = RetCode.INTERNAL_ERROR;
        } finally {
            DBUtil.closeStatement(statement);
        }
        return retCode;
    }
    
    /**
     * 批量执行sql，无返回码与结果集
     * @param source 数据源名称，在system.cfg的rdb中配置
     * @param sql
     * @param parameters
     * @return
     */
    public static final int executeBatch(String source, String sql, List<Object[]> parameters) {
        DBUtil.DBConnection dbConn = null;
        int retCode = RetCode.OK;

        try {
            dbConn = DBUtil.getConnection(source, false);
            if (dbConn == null) {
                LOG.error("Fail to get rdb connection from {}", source);
                return RetCode.INTERNAL_ERROR;
            }
            retCode = executeBatch(dbConn.getConnection(), sql, parameters);
        }catch(Exception e) {
            LOG.error("Fail to execute {} in {}", sql, source);
            return RetCode.INTERNAL_ERROR;
        }finally {
            freeConnection(dbConn, retCode == RetCode.OK);
        }

        return retCode;
    }
    
    /**
     * 批量执行sql，无返回码与结果集
     * @param conn 连接
     * @param sql
     * @param parameters
     * @return
     */
    public static final int executeBatch(DBConnection conn, String sql, List<Object[]> parameters) {
        return executeBatch(conn.getConnection(), sql, parameters);
    }
    
    /**
     * 执行数据库，无结果集处理
     * @param source 数据库连接池名称
     * @param sql
     * @param needOut 是否需要通过out型参数返回执行结果，只在存储过程的情况有效
     * @param parameters
     * @return
     */
    public static final int execute(String source, String sql, boolean needOut, Object[] parameters) {
        DBUtil.DBConnection dbConn = null;
        int retCode = RetCode.OK;

        try {
            dbConn = DBUtil.getConnection(source, false);
            if (dbConn == null) {
                LOG.error("Fail to get rdb connection from {}", source);
                return RetCode.INTERNAL_ERROR;
            }
            retCode = execute(dbConn.getConnection(), sql, needOut, parameters);
        }catch(Exception e) {
            if(WAFConfig.isUtilLogException()) {
                LOG.error("Fail to execute {} in {}", sql, source, e);
            } else {
                LOG.error("Fail to execute {} in {}", sql, source);
            }
            return RetCode.INTERNAL_ERROR;
        }finally {
            freeConnection(dbConn, retCode == RetCode.OK);
        }

        return retCode;
    }
    
    
    public static final String[] getMetaColumns(ResultSetMetaData meta) throws SQLException {
        int columnNum = meta.getColumnCount();
        String[] columnNames = new String[columnNum];
        
        for(int i = 0; i < columnNum; i++) {
            columnNames[i] = meta.getColumnLabel(i + 1);
        }
        return columnNames;
    }
    /**
     * 执行查询，可以支持多个结果集。
     * 如果DBQueryResult中retCode为非OK，则result中不一定有结果集
     * @param conn
     * @param sql
     * @param needOut
     * @param resultSetRef 
     *   当需要从存储过程中返回结果集时，
     *   如果是oracle传-10，如果是postgresql传2006，
     *   其他数据或不需要从存储过程返回结果集，传Integer.MIN_VALUE
     * @param parameters
     * @return
     */
    public static final DBQueryResult query(Connection conn, String sql, 
            boolean needOut, int resultSetRef, Object[] parameters) {
        CallableStatement statement = null;
        int idx = 1;
        DBQueryResult result = new DBQueryResult();

        try {
            process: {
                if ((statement = conn.prepareCall(sql)) == null) {
                    result.retCode = RetCode.INTERNAL_ERROR;
                    break process;
                }
                
                if(needOut) {
                    statement.registerOutParameter(idx++, Types.INTEGER);
                }
                
                if(parameters != null) {
                    for (Object o : parameters) {
                        if(o != null) {
                            statement.setString(idx, o.toString());
                        } else {
                            statement.setNull(idx, java.sql.Types.VARCHAR);
                        }
                        idx++;
                    }
                }
                printStatement(statement, LOG);
                statement.execute();
                
                idx = 1;
                if(needOut) {
                    int retCode = statement.getInt(idx++);
                    if(retCode != RetCode.OK) {
                        result.retCode = retCode;
                        break process;
                    }
                }
                
                ResultSet resultSet;
                boolean hasMoreResult = true;
                boolean resultInCursor = needOut && DBConnection.isResultSetInCursor(resultSetRef);
                
                while(hasMoreResult) {
                    if(resultInCursor) {
                        //oracle,postgresql对存储过程结果集的处理不同于mysql/sqlserver/sybase等
                        resultSet = (ResultSet) statement.getObject(idx++);
                    } else {
                        resultSet = statement.getResultSet();
                    }
                    
                    if(resultSet == null) {
                        break;
                    }
                    
                    ResultSetMetaData meta = resultSet.getMetaData();
                    String[] columnNames = getMetaColumns(meta);
                    List<Map<String, Object>> one = new ArrayList<Map<String, Object>>();
                    
                    while (resultSet.next()) {
                        one.add(getMapRow(resultSet, columnNames));
                    }
                    result.result.add(one);
                    resultSet.close();
                    if(!resultInCursor) {
                        hasMoreResult = statement.getMoreResults(); //必须调用，否则结果集不向后移动
                    }
                }
            }
        } catch (SQLException e) {
            if(WAFConfig.isUtilLogException()) {
                LOG.error("Fail to execute {}", sql, e);
            } else {
                LOG.error("Fail to execute {}", sql);
            }
            result.retCode = RetCode.INTERNAL_ERROR;
        } finally {
            DBUtil.closeStatement(statement);
        }
        
        return result;
    }

    /**
     * 执行查询，使用PreparedStatement，适合不支持CallableStatement的数据库。
     * 如果DBQueryResult中retCode为非OK，则result中不一定有结果集
     * @param conn
     * @param sql
     * @param parameters
     * @return
     */
    public static final DBQueryResult query(Connection conn, String sql, Object[] parameters) {
        PreparedStatement statement = null;
        int idx = 1;
        DBQueryResult result = new DBQueryResult();

        try {
            if ((statement = conn.prepareStatement(sql)) == null) {
                result.retCode = RetCode.INTERNAL_ERROR;
                return result;
            }
            
            if(parameters != null) {
                for (Object o : parameters) {
                    if(o != null) {
                        statement.setString(idx, o.toString());
                    } else {
                        statement.setNull(idx, java.sql.Types.VARCHAR);
                    }
                    idx++;
                }
            }
            printStatement(statement, LOG);
            statement.execute();
            
            idx = 1;
            ResultSet resultSet;
            boolean hasMoreResult = true;
            
            while(hasMoreResult) {
                if((resultSet = statement.getResultSet()) == null) {
                    LOG.warn("Result set is null");
                    break;
                }
                
                ResultSetMetaData meta = resultSet.getMetaData();
                String[] columnNames = getMetaColumns(meta);
                List<Map<String, Object>> one = new ArrayList<Map<String, Object>>();
                
                while (resultSet.next()) {
                    one.add(getMapRow(resultSet, columnNames));
                }
                result.result.add(one);
                resultSet.close();
                hasMoreResult = statement.getMoreResults(); //必须调用，否则结果集不向后移动
            }
        } catch (SQLException e) {
            if(WAFConfig.isUtilLogException()) {
                LOG.error("Fail to execute {}", sql, e);
            } else {
                LOG.error("Fail to execute {}", sql);
            }
            result.retCode = RetCode.INTERNAL_ERROR;
        } finally {
            DBUtil.closeStatement(statement);
        }
        
        return result;
    }

    public static final DBQueryResult query(String sql, boolean needOut, Object[] parameters, boolean transaction) {
    	DBConnection conn = DBUtil.getConnection(transaction);
    	DBQueryResult result = null;
    	
    	try {
    		result = query(conn.getConnection(), sql, needOut, conn.resultSetRef, parameters);
    	} finally {
    		DBUtil.freeConnection(conn, result != null && result.getRetCode() == RetCode.OK);
    	}
    	return result;
    }
    
    /**
     * 简单查询，不支持存储过程查询
     * @param dbSrcName system.cfg中配置的数据源的名称
     * @param sql SQL
     * @param parameters 参数
     * @return
     */
    public static final DBQueryResult simpleQuery(String dbSrcName, String sql, Object[] parameters) {
        DBConnection conn = DBUtil.getConnection(dbSrcName, false);
        DBQueryResult result = null;
        
        try {
            result = query(conn.getConnection(), sql, parameters);
        } finally {
            DBUtil.freeConnection(conn, true);
        }
        return result;
    }
    
    /**
     * @param dbSrcName system.cfg中配置的数据源的名称
     * @param sql SQL
     * @param needOut 是否有返回码
     * @param parameters 参数
     * @param transaction 是否需要事务
     * @return
     */
    public static final DBQueryResult query(String dbSrcName, String sql, Object[] parameters) {
    	DBConnection conn = DBUtil.getConnection(dbSrcName, false);
    	if(conn == null) {
    	    return null;
    	}
    	DBQueryResult result = null;
    	
    	try {
    		result = query(conn.getConnection(), sql, false, conn.resultSetRef, parameters);
    	} finally {
    		DBUtil.freeConnection(conn, result != null && result.getRetCode() == RetCode.OK);
    	}
    	return result;
    }
    
    public static final List<Map<String,Object>> query(DBConnection conn, String sql,
        boolean needOut, Object[] parameters) {
        DBQueryResult res = query(conn.getConnection(), sql, needOut, conn.getResultSetRef(), parameters);
        if(res.retCode != RetCode.OK) {
            return null;
        }
        
        List<List<Map<String,Object>>> list = res.getResult();
        if(list == null || list.size() <= 0) {
            return null;
        }
        
        return list.get(0);
    }
    
    public static final Map<String,Object> queryMap(DBConnection conn, String sql,
        boolean needOut, Object[] parameters) {
        List<Map<String,Object>> list = query(conn, sql, needOut, parameters);
        if(list == null || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    public static final Map<String, Object> getMapRow(ResultSet result, String[] columnNames) throws SQLException {
        Map<String, Object> respData = new HashMap<String, Object>();
        int columnNum = columnNames.length;

        for (int i = 0; i < columnNum; i++) {
            respData.put(columnNames[i], result.getString(i + 1));
        }
        return respData;
    }
    
    public static final Set<String> getNodes() {
    	return pools.keySet();
    }
    
    public static final boolean setBalancer(String balancerCls) {
		IBalancer balancer = DBUtil.loadBalancer(balancerCls);
		if(balancer == null) {
			LOG.error("Fail to load balancer {}", balancerCls);
			return false;
		}
		
		balancer.setNodes(DBUtil.getNodes());
    	DBUtil.balancer = balancer;
    	
    	return true;
    }
    
    public static final IBalancer getBalancer() {
    	return balancer;
    }
    
	public static IBalancer loadBalancer(String balancerClass) {
		IBalancer balancer;
        
		try {
			Class<? extends IBalancer> cls = (Class<? extends IBalancer>)Class.forName(balancerClass);
			balancer = cls.newInstance();
			return balancer;
		} catch (ClassNotFoundException e) {
			LOG.error("Class {} not found", balancerClass, e); 
		} catch (Exception e) {
			LOG.error("Fail to init balancer {}", balancerClass, e);
		}
		
        return null;
	}
    
    public static String getCollectDbName() {
		return collectDbName;
	}

	public static void setCollectDbName(String collectDbName) {
		DBUtil.collectDbName = collectDbName;
	}

	/**
     * @author l00152046
     * 记录查询结果，当为存储过程时，retCode中记录返回码，
     * result中记录多行结果，如果只有一行，list长度为0-1
     */
    public static class DBQueryResult {
        public int retCode = RetCode.OK;
        public List<List<Map<String,Object>>> result = new ArrayList<List<Map<String,Object>>>();
        
        public DBQueryResult() {
        }
        
        public DBQueryResult(int retCode) {
        	this.retCode = retCode;
        }
        
        public int getRetCode() {
        	return this.retCode;
        }
        
        public List<List<Map<String,Object>>> getResult() {
        	return result;
        }
        
        public List<Map<String,Object>> getResult(int no) {
        	if(no >= result.size()) {
        		return null;
        	}
        	return result.get(no);
        }
        
        public Map<String,Object> getLine(int resultSetNo, int lineNo) {
            if(result.size() <= resultSetNo) {
                return null;
            }
            
            List<Map<String,Object>> resultSet = result.get(resultSetNo);
            if(resultSet.size() <= lineNo) {
                return null;
            }
            
            return resultSet.get(lineNo);
        }
    }
    
    /**
     * C3P0中statement为NewProxyStatement，不能直接toString，
     * 所以从中反射得到inner，为实际driver实现的statement
     * @param statement
     * @param LOG
     */
    public static final void printStatement(Statement statement, Logger LOG) {
        if(!LOG.isDebugEnabled()) {
            return;
        }
        try {
            //C3P0 NewProxyStatement中inner为statement，为实际driver实现的statement
            Object inner = Utils.getObjectProperty(statement, "inner");
            if(inner != null) {
                LOG.debug("Statement:{}", inner.toString());
            }
        } catch (Exception e) {
            LOG.warn("Fail to get inner property", e);
        }
    }
}
