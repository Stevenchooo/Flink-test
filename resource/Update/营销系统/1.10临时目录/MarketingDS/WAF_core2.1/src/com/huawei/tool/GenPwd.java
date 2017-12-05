package com.huawei.tool;

import java.io.File;

import org.slf4j.Logger;

import com.huawei.util.EncryptUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.sys.SysConfig;

/**
 * @author l00152046
 * 产生数据库等配置文件中的密码
 */
public class GenPwd {
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 配置文件中不可以存放明文密码，比如数据库密码，
     * 必须通过此工具加密后才可以放在配置文件中
     * @param args
     */
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("genpwd src_password [root]");
            System.exit(0);
        }
        String srcPwd = args[0];
        
        LOG.info("\n===Load system.cfg");
        File f = new File("../conf/system.cfg");
        if(!SysConfig.readConfigs(f)) {
            System.out.println("Fail to parse system config:" + f.getAbsoluteFile());
            System.exit(0);
        }        
        
        String encodePwd = null;
        String root = null;
        if(args.length > 1) {
            root = args[1].toLowerCase();
            if(root.equals("root")) {
                encodePwd = EncryptUtil.rootEncode(srcPwd);
            } else {
                root = null;
            }
        }
        
        if(encodePwd == null) {
            encodePwd = EncryptUtil.configEncode(srcPwd);
        }
        System.out.println("=======================================================");
        System.out.println((root != null ? "Root-" : "") + "Encoded password: " + encodePwd);
        System.out.println("Source password: " + srcPwd);
        System.out.println("=======================================================");
        
        System.exit(0);
    }

}
