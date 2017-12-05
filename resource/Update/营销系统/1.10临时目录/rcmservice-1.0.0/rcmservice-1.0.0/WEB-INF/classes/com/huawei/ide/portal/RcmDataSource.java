package com.huawei.ide.portal;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * 
 * @author zWX301264
 *
 */
public class RcmDataSource extends BasicDataSource
{
    private static final String SECOND_KEY = "1g8Qnr";
    
    private static final String FIRST_KEY = "database.pass.key.1";
    
    @Override
    public void setPassword(String password)
    {
        String privateKey = CommonUtils.getSysConfig(FIRST_KEY) + SECOND_KEY;
        super.setPassword(CryptUtil.decryptForAESCBCStr(password.trim(), privateKey));
    }
    
}
