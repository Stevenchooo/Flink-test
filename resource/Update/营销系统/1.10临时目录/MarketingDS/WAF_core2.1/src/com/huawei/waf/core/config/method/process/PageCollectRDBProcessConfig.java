package com.huawei.waf.core.config.method.process;

import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.response.RDBResponseConfig;

/**
 * 分页汇总查询，需要返回总行数
 * @author l00152046
 */
public class PageCollectRDBProcessConfig extends RDBProcessConfig {
    private static final Logger LOG = LogUtil.getInstance();

    private static final String PARAMETER_PAGENO = "pageNo";

    private static final String PARAMETER_PAGESIZE = "pageSize";

    private static final String PARAMETER_TOTAL = "totalCount";

    private static final String PARAMETER_COUNT_SQL = "countSql";

    private String pageNo = "pageNo"; // 偏移字段名

    private String pageSize = "pageSize"; // 页长字段名

    private String countSql = ""; // 计算总数的sql

    private String total = "totalCount"; // 总行数字段名

    /**
     * <解析配置文件中关于数据库扩展配置项> {@inheritDoc}
     */
    @Override
    protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc) {
        if (!super.parseExt(ver, json, mc)) {
            return false;
        }

        countSql = JsonUtil.getAsStr(json, PARAMETER_COUNT_SQL, countSql).trim();
        if (Utils.isStrEmpty(countSql)) {
            LOG.error("There are no {} config in collect loop query", PARAMETER_COUNT_SQL);
            return false;
        }

        pageNo = JsonUtil.getAsStr(json, PARAMETER_PAGENO, pageNo).trim();
        pageSize = JsonUtil.getAsStr(json, PARAMETER_PAGESIZE, pageSize).trim();
        total = JsonUtil.getAsStr(json, PARAMETER_TOTAL, total).trim();

        RDBResponseConfig rc = (RDBResponseConfig)mc.getResponseConfig();
        if (rc == null) {
            LOG.error("There are no response config, need not loop query");
            return false;
        }

        return true;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getPageNo() {
        return pageNo;
    }

    /**
     * 总行数名称，如果不配置，则说明不需要总数，在遍历数据库时，查满pageSize则终止，
     * 如果设置了total，则从第一个到最后，即使查满了pageSize，然后往后面的库查询total
     * 
     * @return
     */
    public String getTotal() {
        return total;
    }

    public String getCountSql() {
        return countSql;
    }

    /**
     * 不容许使用事务，否则多个库的情况，不容易回退，此类功能只能用于查询 {@inheritDoc}
     */
    @Override
    public boolean useTransaction() {
        return false;
    }
}
