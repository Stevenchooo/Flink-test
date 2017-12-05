package com.huawei.tool;

import java.io.IOException;

import com.huawei.util.SecureUtil;
import com.huawei.waf.protocol.RetCode;

public class GenUserPwd {
    
    /**
     * 生成加密后的密码，账号+密码后sha256
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if(args.length < 2) {
            System.out.println(SecureUtil.class.getName() + " account password");
            System.exit(0);
        }
        String account = args[0];
        String password = args[1];
        if(SecureUtil.isValidPassword(account, password) != RetCode.OK) {
            System.out.println(password + " is not a valid password");
            System.exit(0);
        }
        System.out.println(SecureUtil.encodePassword(account, password));
        System.exit(0);
    }
}
