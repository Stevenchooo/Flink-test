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
 * 逐个库的汇总查询(Obo=One by one)，不需要返回总行数
 * 
 * @author  l00152046
 * @version  [VMALL OMS V100R001C01, 2014-3-24]
 * @since  [VMALL OMS]
 */
public class OboCollectRDBProcessConfig extends RDBProcessConfig
{
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final String PARAMETER_PAGESIZE = "pageSize";
    
    private static final String PARAMETER_STARTPOSITION = "startPosition";
    
    private static final String PARAMETER_NEXTPOSITION = "nextPosition";

    private static final String PARAMETER_LIST_NAME = "listName";
    
    private static final String PARAMETER_STARTID_NAME = "startId"; //下一个开始id的名称
    
    private String startPosition = "startPosition"; //偏移字段名
    
    private String pageSize = "pageSize"; //最大返回行数
    
    private String nextPosition = "nextPosition"; //下一个位置的字段名，用于响应中
    
    private String listName = ""; //返回结果的名称
    
    private String startIdName = ""; //总数的结果集名称
    
    /**
     * <解析配置文件中关于数据库扩展配置项>
     * {@inheritDoc}
     */
    @Override
    protected boolean parseExt(String ver, Map<String, Object> json, MethodConfig mc) {
        if(!super.parseExt(ver, json, mc)) {
        	return false;
        }
        
        startPosition = JsonUtil.getAsStr(json, PARAMETER_STARTPOSITION, startPosition).trim();
        pageSize = JsonUtil.getAsStr(json, PARAMETER_PAGESIZE, pageSize).trim();
        nextPosition = JsonUtil.getAsStr(json, PARAMETER_NEXTPOSITION, nextPosition).trim();
        
        RDBResponseConfig rc = (RDBResponseConfig)mc.getResponseConfig();
        if(rc == null) {
            LOG.error("There are no response config, need not loop query");
            return false;
        }

        listName = JsonUtil.getAsStr(json, PARAMETER_LIST_NAME, listName).trim();
        
        ResultSetConfig[] rscs = rc.getResultSetConfigs();
        if(rscs != null && rscs.length == 2) {
            //没有配置的情况，认为第一个结果是列表，第二个是总数
            if(Utils.isStrEmpty(listName)) {
                listName = rscs[0].getName();
            }
            
            if(Utils.isStrEmpty(startIdName)) {
                startIdName = rscs[1].getName();
            }
        } else {
            if(Utils.isStrEmpty(listName) || Utils.isStrEmpty(startIdName)) {
                LOG.error("{} and {} should be prompted", PARAMETER_LIST_NAME, PARAMETER_STARTID_NAME);
                return false;
            }
        }

        return true;
    }
    
    public String getPageSize() {
        return pageSize;
    }
    
    public String getStartPosition() {
        return startPosition;
    }
    
    public String getNextPosition() {
        return nextPosition;
    }
	
    /**
     * 结果集列表的名称
     * @return
     */
    public String getListName() {
        return listName;
    }
    
    /**
     * 结果集中返回的startId的名称，下一次查询的开始id，一般为表自增id
     * @return
     */
    public String getStartIdName() {
        return startIdName;
    }
    
    /**
     * 不容许使用事务，否则多个库的情况，不容易回退，此类功能只能用于查询
     * {@inheritDoc}
     */
    @Override
    public boolean useTransaction() {
        return false;
    }
}
