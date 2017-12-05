package com.huawei.manager.base.servlet;

import com.huawei.manager.base.config.ManagerConfig;
import com.huawei.manager.base.config.process.AuthPageProcessConfig;
import com.huawei.manager.base.config.process.AuthRDBProcessConfig;
import com.huawei.manager.base.config.process.OMUploadProcessConfig;
import com.huawei.manager.base.protocol.RetCodeExt;
import com.huawei.manager.base.run.process.AuthPageProcessor;
import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.base.run.process.OMUploadProcessor;
import com.huawei.manager.base.run.process.VerifyAuthRDBProcessor;
import com.huawei.manager.base.run.response.AuthHtmlResponser;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.RDBProcessConfig;
import com.huawei.waf.core.config.method.request.JsonRequestConfig;
import com.huawei.waf.core.config.method.request.RestRequestConfig;
import com.huawei.waf.core.config.method.response.HtmlResponseConfig;
import com.huawei.waf.core.config.method.response.RDBResponseConfig;
import com.huawei.waf.core.config.sys.SysConfig;
import com.huawei.waf.core.run.request.JsonRequester;
import com.huawei.waf.core.run.request.UploadRequester;
import com.huawei.waf.core.run.response.JsonResponser;
import com.huawei.waf.core.run.response.TextJsonResponser;
import com.huawei.waf.servlet.InitListener;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see  [相关类/方法]
 */
public class ManagerInitListener extends InitListener
{
    /**
     * AUTHRDB
     */
    private static final String AUTHRDB = "AUTHRDB";
    
    /**
     * AUTHPAGE
     */
    private static final String AUTHPAGE = "AUTHPAGE";
    
    /**
     * OMUPLOAD
     */
    private static final String OMUPLOAD = "OMUPLOAD";
    
    /**
     * VERIFYAUTHRDB
     */
    private static final String VERIFYAUTHRDB = "VERIFYAUTHRDB";
    
    /**
     * 没有AUTHRDB权限
     */
    private static final int UN_AUTHRDB_AUTH = 10001;
    
    /**
     * 没有AUTHPAGE权限
     */
    private static final int UN_AUTHPAGE_AUTH = 10002;
    
    /**
     * 没有OMUPLOAD权限
     */
    private static final int UN_OMUPLOAD_AUTH = 10003;
    
    /**
     * 没有VERIFYAUTHRDB权限
     */
    private static final int UN_VERIFYAUTHRDB_AUTH = 10004;
    
    /**
     * <默认构造函数>
     */
    public ManagerInitListener()
    {
        initializer = new ServletInit()
        {
            /**
             * 初始化前
             * @param configPath  配置目录
             * @return            是否成功
             */
            @Override
            protected int beforeInit(String configPath)
            {
                int retCode = super.beforeInit(configPath);
                if (retCode != 0)
                {
                    return retCode;
                }
                ManagerConfig.parse(SysConfig.getConfig(ManagerConfig.getSysconfigManager()));
                boolean ret;
                ret = MethodConfig.addMethodType(AUTHRDB,
                    JsonRequestConfig.class,
                    AuthRDBProcessConfig.class,
                    RDBResponseConfig.class,
                    new JsonRequester(),
                    new AuthRDBProcessor(),
                    new JsonResponser());
                // 提交操作之前，需要校验权限
                if (!ret)
                {
                    return UN_AUTHRDB_AUTH;
                }
                ret = MethodConfig.addMethodType(AUTHPAGE,
                    JsonRequestConfig.class,
                    AuthPageProcessConfig.class,
                    HtmlResponseConfig.class,
                    new JsonRequester(),
                    new AuthPageProcessor(),
                    new AuthHtmlResponser());
                //打开页面之前，需要校验权限
                if (!ret)
                {
                    return UN_AUTHPAGE_AUTH;
                }
                MethodConfig.addMethodType(OMUPLOAD,
                    RestRequestConfig.class,
                    OMUploadProcessConfig.class,
                    RDBResponseConfig.class,
                    new UploadRequester(),
                    new OMUploadProcessor(),
                    new TextJsonResponser());
                // 上传文件
                if (!ret)
                {
                    return UN_OMUPLOAD_AUTH;
                }
                /**
                 * 以rest/json方式传递参数，返回json内容
                 * 从AuthRDBProcessor继承，在执行鉴权与请求之前，先判断验证码是否正确
                 * 用在新增数据中，防止脏数据攻击
                 */
                if (!MethodConfig.addMethodType(VERIFYAUTHRDB,
                    JsonRequestConfig.class,
                    RDBProcessConfig.class,
                    RDBResponseConfig.class,
                    new JsonRequester(),
                    new VerifyAuthRDBProcessor(),
                    new JsonResponser()))
                {
                    return UN_VERIFYAUTHRDB_AUTH;
                }
                RetCodeExt.init();
                MethodConfig.setDefaultAuthType(MethodConfig.AUTH_USER);
                return 0;
            }
            
            /**
             * 初始化后
             * @param configPath        配置目录
             * @return                  是否成功
             */
            @Override
            protected int afterInit(String configPath)
            {
                int retCode = super.afterInit(configPath);
                if (retCode != 0)
                {
                    return retCode;
                }
                AuthRDBProcessConfig.initRights();
                return 0;
            }
        };
    }
}
