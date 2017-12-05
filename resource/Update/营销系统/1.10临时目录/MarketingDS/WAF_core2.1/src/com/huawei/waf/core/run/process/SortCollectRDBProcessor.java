package com.huawei.waf.core.run.process;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;

import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.parameter.ParameterInfo;
import com.huawei.waf.core.config.method.process.SortCollectRDBProcessConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.facade.config.AbstractRequestConfig;
import com.huawei.waf.protocol.RetCode;

/**
 * 从各分库中查找符合条件的数据，可以支持分页
 * 
 * 需要一个汇总库，汇总库中需要按照每个接口返回结果的列表，创建一个合适的表，并按排序字段建立索引，
 * 汇总库中需要初始化database/sortquery目录下的所有内容。
 * 
 * 至少要返回两个结果集，一个是返回列表，一个是符合条件的数据总数。
 * 
 * 大致思路是：
 * 比如页长为20，当需要查询第一页20条时，从每个分库都排序查出20条，然后汇总到汇总库中，
 * 在汇总库中排序查询，这样一定能得到最前面的20条；
 * 当查询第二页20条时，只需要在每个分库中分别查处第二页的20条，汇总到汇总库中，
 * 这样一定能得到前面的20到40的记录。
 * 其他依此类推
 * 
 * @author l00152046
 * @version [VMALL OMS V100R001C01, 2014-5-19]
 * @since [VMALL OMS]
 */
public class SortCollectRDBProcessor extends RDBProcessor {
	private static final Logger LOG = LogUtil.getInstance();

	private static final int DEFAULT_PAGESIZE = 20;

    private static final int DATA_PREPARING_NUM = -100000;
    
    private static final int DATA_NULL_NUM = -100001;
    
    private static final int DATA_INVALID_NUM = -100002;
    
    private static final int DATA_SLEEP_TIME = 2000;
    
	private static String[] dbServers = null;
	
    private static ExecutorService THREAD_POOL = null;
    
	@Override
    public synchronized boolean init() {
		if (dbServers != null) {
			return true;
		}

		Collection<String> servers = DBUtil.getBalancer().getNodes();
		dbServers = servers.toArray(new String[servers.size()]);
		if(dbServers == null || dbServers.length <= 0) {
			return false;
		}
		
        THREAD_POOL = Executors.newFixedThreadPool(dbServers.length);

		return true;
	}

	/**
	 * 先计算急群众所有满足条件的数据的总数，然后根据分页号、页长，找到相应的数据库，执行查询操作 {@inheritDoc}
	 */
	@Override
	public int process(MethodContext context) {
		MethodConfig methodConfig = context.getMethodConfig();
		SortCollectRDBProcessConfig sortCfg = (SortCollectRDBProcessConfig) methodConfig.getProcessConfig();
		
		int startPageNo = sortCfg.getStartPageNo();
		String listName = sortCfg.getListResponseConfig().getName();
		
		Map<String, Object> reqParameters = context.getParameters();
		int pageNo = JsonUtil.getAsInt(reqParameters, sortCfg.getPageNo(), startPageNo);
		int pageSize = JsonUtil.getAsInt(reqParameters, sortCfg.getPageSize(), DEFAULT_PAGESIZE);

		if (pageNo < startPageNo || pageSize < 0) {
			LOG.error("Wrong {}|{}, vaule={}|{}", sortCfg.getPageNo(), sortCfg.getPageSize(), pageNo, pageSize);
			return RetCode.WRONG_PARAMETER;
		}

		String transaction = calculateTran(methodConfig, sortCfg, reqParameters);
		
		StringBuilder sb = new StringBuilder("insert into ");
		sb.append(sortCfg.getSortTable()).append("(tranId");

		ParameterInfo[] segments = sortCfg.getListResponseConfig().getSegmentList(); //读配置时已判断是否有效，所以这里无需判断 
		String values = " values('" + transaction + '\'';
		for (ParameterInfo pi : segments) {
			sb.append(',').append(pi.getName());
			values += ",?";
		}
		sb.append(')').append(values).append(')');
		
		String insertSql = sb.toString();

		// 取得汇总库连接
		DBConnection collectDbConn = DBUtil.getConnection(DBUtil.getCollectDbName(), false);
		if (collectDbConn == null) {
			LOG.error("Fail to get connection from {}", DBUtil.getCollectDbName());
			return RetCode.INTERNAL_ERROR;
		}

		int expireTime = sortCfg.getExpireTime();
		int curNum = DATA_NULL_NUM;
		int oldPageNo = startPageNo >= 1 ? (startPageNo - 1) : 0;
		
        try {
            if(expireTime > 0) { //设置了超期时间，表示需要缓存
                Map<String, Object> tranInfo = createTransaction(collectDbConn, transaction,
                        sortCfg.getSortTable(), startPageNo, expireTime);
                
                if(tranInfo != null) {
                    curNum = JsonUtil.getAsInt(tranInfo, "num", DATA_INVALID_NUM);
                    oldPageNo = JsonUtil.getAsInt(tranInfo, "pageNo", oldPageNo);
                }
                
                if(curNum == DATA_PREPARING_NUM) { //有其他线程在准备数据，等待
                    long start = System.currentTimeMillis();
                    for(int i = 0; i < sortCfg.getWaitTimes() && curNum == DATA_PREPARING_NUM; i++) { 
                        try {
                            Thread.sleep(DATA_SLEEP_TIME);
                        } catch (InterruptedException e) {
                            return RetCode.INTERNAL_ERROR;
                        }
                        curNum = getResultNum(collectDbConn, transaction);
                    }
                    
                    if(curNum < 0) {
                        if(curNum == DATA_PREPARING_NUM) {
                            LOG.error("Wait too long, {}ms, total is {}", System.currentTimeMillis() - start, curNum);
                        } else {
                            LOG.error("Internal error, wait {}ms, total is {}", System.currentTimeMillis() - start, curNum);
                        }
                        return context.setResult(RetCode.INTERNAL_ERROR, "Too busy");
                    }
                } else if(curNum == DATA_INVALID_NUM){
                    return RetCode.INTERNAL_ERROR;
                }
            }
            
            int total = 0;
            /**
             * >=0时，表示前面查过，且缓存了，=0表示前面没有查到数据，
             * 即使查过数据，也可能当前缓存的页号小于需要的页号，这时需要从补齐后面的数据
             */
            if(curNum == DATA_NULL_NUM || pageNo > oldPageNo) {
                long start = System.currentTimeMillis();
                LOG.debug("Collect data, transaction={}, total={}", transaction, curNum);
                
                //改变pageNo与pageSize的值，使得只执行增量的数据库查询
                context.setParameter(sortCfg.getPageNo(), oldPageNo * pageSize);
                context.setParameter(sortCfg.getPageSize(), (pageNo - oldPageNo) * pageSize);
                
                CountDownLatch counter = new CountDownLatch(dbServers.length);
                Querier[] queriers = new Querier[dbServers.length];
                for(int i = 0; i < dbServers.length; i++) {
                    queriers[i] = new Querier(counter, dbServers[i], insertSql, segments, context);
                    THREAD_POOL.execute(queriers[i]);
                }
                
                try {
                    counter.await();
                } catch (InterruptedException e) {
                    LOG.error("Fail to wait", e);
                    return RetCode.INTERNAL_ERROR;
                }
                
                //获取当前结果集的总数
                curNum = 0;
                for(Querier querier : queriers) {
                    curNum += querier.getResultNum();
                    total += querier.getTotal();
                }
                
                if(expireTime > 0) {
                    //不管是否存储成功，都可以，因为如果失败了，下次再查询一遍，影响性能，但不影响功能
                    DBUtil.execute(collectDbConn,
                        "update tbl_transaction set num=?,pageNo=?,total=?,expire=DATE_ADD(now(),INTERVAL ? SECOND) where id=?",
                        false, new Object[]{curNum, pageNo, total, expireTime, transaction});
                }
                LOG.info("Collect all {} data in {}ms", methodConfig.getName(), System.currentTimeMillis() - start);
            }
            
            int retCode = queryList(
                collectDbConn,
                sortCfg.getSortSql(),
                transaction,
                (pageNo - 1) * pageSize,
                pageSize,
                context);
           
            if(retCode == RetCode.OK) {
                //每个返回都需要携带起始页号，真实结果集的行数
                context.setResult(sortCfg.getPageNo(), pageNo);

                List<Object> sortList = JsonUtil.getAsList(context.getResults(), listName);
                if(sortList != null) {
                    context.setResult(sortCfg.getPageSize(), sortList.size());
                    context.setResult(listName, sortList);
                }
                context.setResult(sortCfg.getTotalName(), total);
            }
            
            if(expireTime <= 0 && curNum > 0) { //不缓存，立刻删除
                DBUtil.execute(collectDbConn,
                    "delete from " + sortCfg.getSortTable() + " where tranId=?",
                    false, new Object[]{transaction});
            }
        } catch (Exception e) {
            LOG.error("Fail to sort query", e);
            return RetCode.INTERNAL_ERROR;
        } finally {
            DBUtil.freeConnection(collectDbConn, false);
        }
        
        return RetCode.OK;
    }
    
    /**
     * 创建session，如果已存在，则返回当前记录的总数
     * @param collectDbConn
     * @param transaction
     * @param table
     * @param expireTime
     * @return
     *  >0:结果集数量
     *  -100000：有线程正在准备数据，请等待；
     *  -100001：session不存在，需要当前准备数据
     *  -100002：数据库操作错误，应该返回
     */
    private static Map<String,Object> createTransaction(DBConnection collectDbConn,
        String transaction, String table, int startPageNo, int expireTime) {
        Map<String,Object> result = DBUtil.queryMap(collectDbConn,
                "{call sp_createTransaction(?,?,?,?)}",
                false, new Object[]{transaction, expireTime, startPageNo, table});
        if(result == null) {
            LOG.error("Fail to get result from sp_createTransaction");
            return null;
        }
        
        return result;
    }
    
    /**
     * 获取session的结果集行数，不判断超期时间
     * @param collectDbConn
     * @param transaction
     * @return
     */
    private static int getResultNum(DBConnection collectDbConn, String transaction) {
        Map<String,Object> result = DBUtil.queryMap(collectDbConn,
            "select num from tbl_transaction where id=?",
            false, new Object[]{transaction});
        if(result == null) {
            return DATA_INVALID_NUM;
        }
        return JsonUtil.getAsInt(result, "num");
    }
    
	/**
	 * 从汇总库中直接查询，sql对应配置中的sortSql
	 * 此函数可以重载，以改变sql
	 * @param collectDbConn
	 * @param sql
	 * @param tranId
	 * @param startRow
	 * @param pageSize
	 * @param context
	 * @return
	 */
    protected int queryList(DBConnection collectDbConn, String sql,
			String tranId, int startRow, int pageSize, MethodContext context) {
		CallableStatement statement = null;
		int retCode = RetCode.OK;
		Connection conn = collectDbConn.getConnection();

		try {
			if ((statement = conn.prepareCall(sql)) == null) {
				return RetCode.INTERNAL_ERROR;
			}
			setStatement(statement, context, tranId, startRow, pageSize);
			statement.execute();

			retCode = getResults(context, statement, context.getResults(), collectDbConn.getResultSetRef(), false);
			if (retCode != RetCode.OK) {
				LOG.error("Fail to get result from sql {}", sql);
				return retCode;
			}
		} catch (Exception e) {
			LOG.error("Fail to execute {}", sql, e);
			retCode = RetCode.INTERNAL_ERROR;
		} finally {
			DBUtil.closeStatement(statement);
		}

		return retCode;
	}
    
    private String calculateTran(MethodConfig methodConfig,
        SortCollectRDBProcessConfig sortCfg, Map<String, Object> reqParamrs) {
        AbstractRequestConfig reqConfig = methodConfig.getRequestConfig();
        ParameterInfo[] reqPis = reqConfig.getParameters();
        StringBuilder sb = new StringBuilder(methodConfig.getName()); //每个接口产生的session不同
        String name;
        
        sb.append('|').append(methodConfig.getName());
        for(ParameterInfo pi : reqPis) {
            name = pi.getName();
            /**
             * 不作为数据库条件的，直接忽略，
             * pageNo不能作为id产生字段，否则id随着页码不同而变化
             */
            if(pi.getIndex() <= 0 || sortCfg.getPageNo().equals(name)) {
                continue;
            }
            sb.append('|')
              .append(name)
              .append('=')
              .append(JsonUtil.getAsStr(reqParamrs, name, ""));
        }
        
        return Utils.md5_base64(sb.toString());
    } 
    
	/**
	 * 设置statement参数，可以重载此函数
	 * 
	 * @param statement
	 * @param context
	 * @param tranId
	 * @param startRow
	 * @param pageSize
	 * @throws SQLException
	 */
	protected void setStatement(CallableStatement statement, MethodContext context,
		String tranId, int startRow, int pageSize) throws SQLException {
		int idx = 1;
		statement.setString(idx++, tranId);
		statement.setInt(idx++, startRow);
		statement.setInt(idx++, pageSize);
	}
    
	private class Querier implements Runnable {
		private static final String NAME = "SortCollect-Querier-";

		private CountDownLatch counter = null;

		private int resultNum = 0;

		private String dbSrv = null;

		private MethodContext parentContext = null;

		private String insertSql;
		
		private ParameterInfo[] segments;
		
		private int total = 0;

		public Querier(CountDownLatch counter, String dbSrv, String insertSql, ParameterInfo[] segments, MethodContext parentContext) {
			this.counter = counter;
			this.dbSrv = dbSrv;
			this.parentContext = parentContext;
			this.insertSql = insertSql;
			this.segments = segments;
		}

		@Override
		public void run() {
			Thread curThread = Thread.currentThread();
			curThread.setName(NAME + curThread.getId());
			MethodConfig methodConfig = parentContext.getMethodConfig();
			SortCollectRDBProcessConfig sortCfg = (SortCollectRDBProcessConfig)methodConfig.getProcessConfig();

			int retCode = RetCode.OK;
			DBConnection partDbConn = null;
			DBConnection collectDbConn = null;

			try {
				if ((partDbConn = DBUtil.getConnection(dbSrv, sortCfg.useTransaction())) == null) {
					LOG.error("Fail to get rdb connection from {}", dbSrv);
					return;
				}

				// 启动事务，批量执行
				if ((collectDbConn = DBUtil.getConnection(DBUtil.getCollectDbName(), true)) == null) {
					LOG.error("Fail to get rdb connection from {}", DBUtil.getCollectDbName());
					return;
				}

				Map<String, Object> resp = new HashMap<String, Object>();
				MethodContext context = new MethodContext(parentContext, resp);
				retCode = process(context, partDbConn); //调用单个数据库的操作
				if (retCode == RetCode.OK) {
				    this.total = JsonUtil.getAsInt(resp, sortCfg.getTotalName(), 0);
					
					//汇总、排序、分页查询，只能有一个结果集，多个结果集将不能插入到汇总表中
					List<Object> list = JsonUtil.getAsList(resp, sortCfg.getListResponseConfig().getName());
					if (list == null) {
                        LOG.warn("List is null in {}, db:{}, retCode={}", resp, dbSrv, retCode);
					} else {
						resultNum = list.size();

						// 批量插入数据到汇总库的排序表中
						retCode = batchExecSQL(context, collectDbConn.getConnection(), list, segments, insertSql, sortCfg.getBatchNum());
						if (retCode != RetCode.OK) {
							// 尽管发生错误，仍然继续往下做
							LOG.error("Fail to save result-set to {}, retCode={}", collectDbConn.getName(), retCode);
						}
					}
				} else {
					LOG.error("Fail to get result from {}, retCode={}", partDbConn.getName(), retCode);
				}
			} catch (Exception e) {
				LOG.error("Fail to count in {}", dbSrv, e);
				retCode = RetCode.INTERNAL_ERROR;
			} finally {
				DBUtil.freeConnection(partDbConn, retCode == RetCode.OK);
				DBUtil.freeConnection(collectDbConn, retCode == RetCode.OK);
				counter.countDown(); // 不能忘记，否则吊死
			}
		}

        public int getResultNum() {
            return resultNum;
        }
        
        public int getTotal() {
            return total;
        }
    }
}
