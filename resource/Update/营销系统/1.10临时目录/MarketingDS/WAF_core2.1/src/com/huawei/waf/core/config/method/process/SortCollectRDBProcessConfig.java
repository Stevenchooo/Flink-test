package com.huawei.waf.core.config.method.process;

import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.response.RDBResponseConfig;
import com.huawei.waf.core.config.method.response.ResultSetConfig;

/**
 * 汇总查询，只可以有一个结果集返回，且是多行的
 * 
 * @author l00152046
 * @version [VMALL OMS V100R001C01, 2014-3-24]
 * @since [VMALL OMS]
 */
public class SortCollectRDBProcessConfig extends RDBProcessConfig {
	private static final Logger LOG = LogUtil.getInstance();

	private static final String PARAMETER_PAGENO = "pageNo";
	
    private static final String PARAMETER_STARTPAGENO = "startPageNo";

	private static final String PARAMETER_PAGESIZE = "pageSize";

	private static final String PARAMETER_TOTAL = "totalCount";

	private static final String PARAMETER_LIST_NAME = "listName";

	private static final String PARAMETER_SORT_SQL = "sortSql"; // 在汇总库上执行的sql

	private static final String PARAMETER_SORT_TABLE = "sortTable"; // 用于临时汇总数据的表

	private static final String PARAMETER_EXPIRETIME = "expireTime"; // 临时汇总数据超时时间
	
    private static final String PARAMETER_BATCHNUM = "batchNum"; //批量插入，每次插入条数
    
    private static final String PARAMETER_WAITTIMES = "waitTimes";
    
	private String pageNo = "pageNo"; // 页号字段名

    private int startPageNo = 0; // 起始页号， 默认从0开始

	private String pageSize = "pageSize"; // 页长字段名

	private String sortSql = "";

	private String sortTable = "";

	private String total = "totalCount"; // 总行数字段名

	private int expireTime = 300; // 0表示不缓存
	
	private int batchNum = 1000;
    
    private int waitTimes = 100; //等待其他线程准备数据的最大等待次数
    
    private ResultSetConfig listConfig;
    
	/**
	 * <解析配置文件中关于数据库扩展配置项> {@inheritDoc}
	 */
	@Override
	protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc) {
		if (!super.parseExt(ver, json, mc)) {
			return false;
		}

		sortSql = JsonUtil.getAsStr(json, PARAMETER_SORT_SQL, "").trim();
		if (Utils.isStrEmpty(sortSql)) {
			LOG.error("There are no {} config in collect sort query", PARAMETER_SORT_SQL);
			return false;
		}

		sortTable = JsonUtil.getAsStr(json, PARAMETER_SORT_TABLE, "").trim();
		if (Utils.isStrEmpty(sortTable)) {
			LOG.error("There are no {} config in collect sort query", PARAMETER_SORT_TABLE);
			return false;
		}

		expireTime = JsonUtil.getAsInt(json, PARAMETER_EXPIRETIME, expireTime);
		pageNo = JsonUtil.getAsStr(json, PARAMETER_PAGENO, pageNo).trim();
        startPageNo = JsonUtil.getAsInt(json, PARAMETER_STARTPAGENO, startPageNo);
		pageSize = JsonUtil.getAsStr(json, PARAMETER_PAGESIZE, pageSize).trim();
		total = JsonUtil.getAsStr(json, PARAMETER_TOTAL, total).trim();
		batchNum = JsonUtil.getAsInt(json, PARAMETER_BATCHNUM, batchNum);
        waitTimes = JsonUtil.getAsInt(json, PARAMETER_WAITTIMES, waitTimes);

		//responseconfig与responseconfig是在processconfig之前解析的
		RDBResponseConfig rc = (RDBResponseConfig)mc.getResponseConfig();
		if (rc == null) {
			LOG.error("There are no response config, need not loop query");
			return false;
		}

		// 返回结果的名称
		String listName = JsonUtil.getAsStr(json, PARAMETER_LIST_NAME, "").trim();
		if (Utils.isStrEmpty(listName)) {
			LOG.error("There are no {} config in {}", PARAMETER_LIST_NAME, mc.getName());
			return false;
		}

		//responseconfig 是在processconfig之前就解析过了，所以这里可以使用
        ResultSetConfig[] resultSetCfgs = ((RDBResponseConfig)mc.getResponseConfig()).getResultSetConfigs();
        for (ResultSetConfig rsc : resultSetCfgs) {
            if (rsc.getName().equals(listName)) { // 一定得有相应结果集的配置，否则一定会异常
                this.listConfig = rsc;
                break;
            }
        }
        
        if(this.listConfig == null || this.listConfig.getSegmentList() == null) {
            LOG.error("Not a valid {} config in {}", PARAMETER_LIST_NAME, mc.getName());
            return false;
        }
		
		return true;
	}
	
	public ResultSetConfig getListResponseConfig() {
	    return listConfig;
	}

	public String getPageSize() {
		return pageSize;
	}

	public String getPageNo() {
		return pageNo;
	}

	/**
	 * 缓存的汇总数据保留时长 ，单位：秒
	 * 
	 * @return
	 */
	public int getExpireTime() {
		return expireTime;
	}

	/**
	 * 总行数名称，如果不配置，则说明不需要总数，在遍历数据库时，查满pageSize则终止，
	 * 如果设置了total，则从第一个到最后，即使查满了pageSize，然后往后面的库查询total
	 * 
	 * @return
	 */
	public String getTotalName() {
		return total;
	}

	public String getSortSql() {
		return sortSql;
	}

	public String getSortTable() {
		return sortTable;
	}
	
	/**
	 * 每次批量插入的数量，一般1000就可以了
	 * @return
	 */
	public int getBatchNum() {
		return batchNum;
	}
    
    public int getWaitTimes() {
        return waitTimes;
    }
    
    public int getStartPageNo() {
        return startPageNo;
    }
	/**
	 * 不容许使用事务，否则多个库的情况，不容易回退，此类功能只能用于查询 {@inheritDoc}
	 */
	@Override
	public boolean useTransaction() {
		return false;
	}
}
