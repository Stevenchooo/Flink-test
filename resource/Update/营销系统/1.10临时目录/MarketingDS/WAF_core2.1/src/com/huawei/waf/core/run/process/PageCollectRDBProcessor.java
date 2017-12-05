package com.huawei.waf.core.run.process;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
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
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.PageCollectRDBProcessConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * 从各分库中查找符合条件的数据，可以支持分页
 * 
 * @author l00152046
 * @version [VMALL OMS V100R001C01, 2014-5-19]
 * @since [VMALL OMS]
 */
public class PageCollectRDBProcessor extends RDBProcessor {
	private static final Logger LOG = LogUtil.getInstance();

	private static final int DEFAULT_PAGESIZE = 20;

	private static final int DEFAULT_PAGENO = 1;

	private static String[] dbServers = null;
	
    private static ExecutorService THREAD_POOL = null;

	@Override
	public synchronized boolean init() {
		if(dbServers != null) {
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
		PageCollectRDBProcessConfig lqrpCfg = (PageCollectRDBProcessConfig) methodConfig.getProcessConfig();
		Map<String, Object> reqParameters = context.getParameters();
		int originalPageNo = JsonUtil.getAsInt(reqParameters, lqrpCfg.getPageNo(), DEFAULT_PAGENO);
		int originalPageSize = JsonUtil.getAsInt(reqParameters, lqrpCfg.getPageSize(), DEFAULT_PAGESIZE);

		if (originalPageNo < DEFAULT_PAGENO || originalPageSize < 0) {
			LOG.error("Wrong {}|{}, vaule={}|{}", lqrpCfg.getPageNo(), lqrpCfg.getPageSize(), originalPageNo, originalPageSize);
			return RetCode.WRONG_PARAMETER;
		}

		int serverNum = dbServers.length;
		Counter[] counters = new Counter[serverNum];
		CountDownLatch counter = new CountDownLatch(serverNum);

		// 计算总数，多个线程并发
		for (int i = 0; i < serverNum; i++) {
			counters[i] = new Counter(counter, dbServers[i], context);
			THREAD_POOL.execute(counters[i]);
		}

		try {
			counter.await();
		} catch (InterruptedException e) {
			LOG.error("Fail to wait", e);
			return RetCode.INTERNAL_ERROR;
		}

		int total = 0;
		for (Counter c : counters) {
			if (c.retCode != RetCode.OK) {
				continue; // 某一个库失败，仍然继续
			}
			total += c.count;
		}

		if (total > 0) {
			int startNo, pageSize;
			int startRow = (originalPageNo - 1) * originalPageSize; // pageNo从1开始
			int endRow = startRow + originalPageSize;

			/**
			 * |--db0--|---db1----|--db2---|---db3----| |<---------->|
			 */
			int count = 0;
			int curNum = 0;
			boolean first = true;
			List<Querier> queries = new ArrayList<Querier>();

			for (int i = 0; i < serverNum; i++) {
				if (count >= endRow) {// 查到最后一个库
					break;
				}

				//计数失败的，跳过
				if (counters[i].retCode != RetCode.OK || counters[i].count <= 0) {
					continue;
				}

				count += counters[i].count;
				if (count < startRow) { //前面的库，忽略
					continue;
				}

				if(first) { //第一个，开始行号不一定是0，要跳过前面已经查过的
					startNo = startRow - (count - counters[i].count);
					first = false;
				} else {
					startNo = 0; //中间的数据库，一定从0行开始
				}
				pageSize = counters[i].count - startNo; //当前库总数减去开始行号，可能大于pageSize

				//如果当前已取得数量加上要取得数量大于要求的pageSize，则只取剩下不足的数量，否则全取
				if ((curNum + pageSize) > originalPageSize) { 
					pageSize = originalPageSize - curNum;
				}
				curNum += pageSize;

				queries.add(new Querier(startNo, pageSize, dbServers[i], context));
			}

			counter = new CountDownLatch(queries.size());
			for (Querier q : queries) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Querier:dbServer={}, startNo={}, pageSize={}", q.dbSrv, q.startNo, q.pageSize);
				}
				q.setCounter(counter);
				THREAD_POOL.execute(q);
			}

			try {
				counter.await();
			} catch (InterruptedException e) {
				LOG.error("Fail to wait", e);
				return RetCode.INTERNAL_ERROR;
			}

			Map<String, Object> resp = context.getResults();
			for (Querier q : queries) {
				if (q.retCode != RetCode.OK) {
					continue; // 某一个库失败，仍然继续
				}
				JsonUtil.mergeList(resp, q.response);
			}
		}

		context.setResult(lqrpCfg.getTotal(), total);
		// 每个返回都需要携带起始页号，真实结果集的行数
		context.setResult(lqrpCfg.getPageNo(), originalPageNo);
		//不返回pageSize，因为从list中可以直接获得
		//resp.put(lqrpCfg.getPageSize(), allList.size());

		return RetCode.OK;
	}

	private class Querier implements Runnable {
		private static final String NAME = "PageCollect-Querier-";

		private CountDownLatch counter = null;

		private String dbSrv = null;

		private MethodContext parentContext = null;
		
		public int retCode = RetCode.OK;
		
		public int startNo = 0;
		
		public int pageSize = 0;
		
		public Map<String, Object> response = new HashMap<String, Object>();

		public Querier(int startNo, int pageSize, String dbSrv, MethodContext parentContext) {
			this.dbSrv = dbSrv;
			this.parentContext = parentContext;
			this.startNo = startNo;
			this.pageSize = pageSize;
		}
		
		public void setCounter(CountDownLatch counter) {
			this.counter = counter;
		}

		@Override
		public void run() {
			Thread curThread = Thread.currentThread();
			curThread.setName(NAME + curThread.getId());
			MethodConfig methodConfig = parentContext.getMethodConfig();
			PageCollectRDBProcessConfig lqrpCfg = (PageCollectRDBProcessConfig)methodConfig.getProcessConfig();

			DBConnection dbConn = DBUtil.getConnection(dbSrv, lqrpCfg.useTransaction());
			if (dbConn == null) {
				LOG.error("Fail to get rdb connection from {}", dbSrv);
				retCode = RetCode.DB_ERROR;
				counter.countDown(); // 如果忘记了，就堵死了
				return;
			}


			// 不可以直接使用context中的请求参数，因为这里会修改参数内容，多线程的情况，请求参数可能不是期望的
			Map<String, Object> reqParameters = new HashMap<String, Object>(parentContext.getParameters());
			reqParameters.put(lqrpCfg.getPageNo(), startNo);
			reqParameters.put(lqrpCfg.getPageSize(), pageSize);

			MethodContext context = new MethodContext(parentContext, reqParameters, this.response);
			
			try {
				retCode = process(context, dbConn);
				if(retCode != RetCode.OK) {
					LOG.error("Fail to get result from {}, retCode={}", dbConn.getName(), retCode);
				}
			} catch (Exception e) {
				LOG.error("Fail to count in {}", dbSrv, e);
				retCode = RetCode.INTERNAL_ERROR;
			} finally {
				DBUtil.freeConnection(dbConn, retCode == RetCode.OK);
				counter.countDown();
			}
		}
	}

	private class Counter implements Runnable {
		private static final String NAME = "PageCollect-Counter-";

		private CountDownLatch counter = null;

		public int count = 0;

		public int retCode = RetCode.OK;

		private String dbSrv = null;

		private MethodContext context = null;

		public Counter(CountDownLatch counter, String dbSrv, MethodContext context) {
			this.counter = counter;
			this.dbSrv = dbSrv;
			this.context = context;
		}

		@Override
		public void run() {
			Thread curThread = Thread.currentThread();
			curThread.setName(NAME + curThread.getId());

			DBConnection dbConn = DBUtil.getConnection(dbSrv, false);
			if (dbConn == null) {
				LOG.error("Fail to get rdb connection from {}", dbSrv);
				retCode = RetCode.DB_ERROR;
				counter.countDown(); // 如果忘记了，就堵死了
				return;
			}

			MethodConfig methodConfig = context.getMethodConfig();
			PageCollectRDBProcessConfig lqrpCfg = (PageCollectRDBProcessConfig)methodConfig.getProcessConfig();

			CallableStatement statement = null;
			ResultSet resultSet;
			Connection conn = dbConn.getConnection();

			try {
				if ((statement = conn.prepareCall(lqrpCfg.getCountSql())) == null) {
					LOG.error("Fail to create statement for {} in {}",
							lqrpCfg.getCountSql(), dbSrv);
					retCode = RetCode.DB_ERROR;
					return;
				}

				prepareStatement(statement, context, dbConn.getResultSetRef());
				statement.execute();

				if ((resultSet = statement.getResultSet()) == null
						|| !resultSet.next()) {
					LOG.warn("Fail to get count resultset from {}", dbSrv);
					return;
				}

				count = resultSet.getInt(1);
				if (LOG.isDebugEnabled()) {
					LOG.debug("count in {} is {}", dbSrv, count);
				}
				resultSet.close();
			} catch (Exception e) {
				LOG.error("Fail to count in {}", dbSrv, e);
				retCode = RetCode.INTERNAL_ERROR;
			} finally {
				DBUtil.closeStatement(statement);
				DBUtil.freeConnection(dbConn, true);
				counter.countDown();
			}
		}
	}
}
