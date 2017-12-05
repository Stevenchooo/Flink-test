package com.huawei.tool;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.huawei.util.DBUtil;
import com.huawei.util.JsonUtil;
import com.huawei.util.DBUtil.DBQueryResult;
import com.huawei.waf.core.config.sys.SysConfig;

public class TestDBConfig {
    
    /**
     * 用于测试db配置是否正常
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("===Load system.cfg");
        File f = new File("../conf/system.cfg");
        if(!SysConfig.readConfigs(f)) {
            System.err.println("Fail to parse system config:" + f.getAbsoluteFile());
            System.exit(0);
        }
        
        System.out.println("===Load rdb config");
        Map<String, Object> cfgs = SysConfig.getRdbConfigs();
        if(cfgs == null || cfgs.size() <= 0) {
            System.err.println("No rdb config, so ignore it");
            System.exit(0);
        }
        
        if(!DBUtil.init(cfgs)) {
            System.err.println("--------------Platform fail to init RDB------------");
            System.exit(0);
        }
        
        String dataSource = DBUtil.getDefaultSource();
        if(args.length > 0) {
            dataSource = args[0];
        }
        String sql = "select 1 as no";
        if(args.length > 1) {
            sql = args[1];
        }
        System.out.println("===execute " + sql  + " @" + dataSource);
        DBQueryResult res = DBUtil.query(dataSource, sql, null);
        if(res == null) {
            System.err.println("--------------Fail to query RDB------------");
            System.exit(0);
        }
        List<List<Map<String,Object>>> lines = res.getResult();
        if(lines == null) {
            System.err.println("--------------Fail to query RDB------------");
            System.exit(0);
        }
        
        System.out.println(JsonUtil.mapToJson(lines));
        System.exit(0);
    }
}
