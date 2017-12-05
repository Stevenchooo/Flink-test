package com.huawei.waf.core.run.process;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;

import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * 从各分库中查找符合条件的数据，可以支持分页
 * 
 * @author l00152046
 * @version [VMALL OMS V100R001C01, 2014-5-19]
 * @since [VMALL OMS]
 */
public class LoopCollectRDBProcessor extends RDBProcessor {
	private static final Logger LOG = LogUtil.getInstance();

	private static String[] dbServers = null;
	private static ExecutorService THREAD_POOL = null;

	@Override
	public synchronized boolean init() {
		if (dbServers != null) { // 只需调用一次
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
		Querier[] queriers = new Querier[dbServers.length];
		CountDownLatch counter = new CountDownLatch(queriers.length);
		for (int i = 0; i < dbServers.length; i++) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Querier:dbServer=", dbServers[i]);
			}

			queriers[i] = new Querier(counter, dbServers[i], context);
			THREAD_POOL.execute(queriers[i]);
		}

		try {
			counter.await();
		} catch (InterruptedException e) {
			LOG.error("Fail to wait", e);
			return RetCode.INTERNAL_ERROR;
		}

		Map<String, Object> resp = context.getResults();
		for (Querier q : queriers) {
			if (q.retCode != RetCode.OK) {
				continue; // 某一个库失败，仍然继续
			}
			JsonUtil.mergeList(resp, q.response);
		}

		return RetCode.OK;
	}

	private class Querier implements Runnable {
		private static final String NAME = "Querier-";

		private CountDownLatch counter = null;

		private String dbSrv = null;

		private MethodContext parentContext = null;

		private int retCode = RetCode.OK;
		
		public Map<String, Object> response = new HashMap<String, Object>();

		public Querier(CountDownLatch counter, String dbSrv, MethodContext context) {
			this.counter = counter;
			this.dbSrv = dbSrv;
			this.parentContext = context;
		}

		@Override
		public void run() {
			Thread curThread = Thread.currentThread();
			curThread.setName(NAME + curThread.getId());

			DBUtil.DBConnection dbConn = DBUtil.getConnection(dbSrv, false);
			if (dbConn == null) {
				LOG.error("Fail to get rdb connection from {}", dbSrv);
				this.retCode = RetCode.DB_ERROR;
				counter.countDown(); // 如果忘记了，就堵死了
				return;
			}

			try {
				MethodContext context = new MethodContext(parentContext, this.response);
				this.retCode = process(context, dbConn);
				if (this.retCode != RetCode.OK) {
					LOG.error("Fail to get result from {}, retCode={}", dbConn.getName(), this.retCode);
				}
			} catch (Exception e) {
				LOG.error("Fail to count in {}", dbSrv, e);
				this.retCode = RetCode.INTERNAL_ERROR;
			} finally {
				DBUtil.freeConnection(dbConn, this.retCode == RetCode.OK);
				counter.countDown();
			}
		}
	}
}
