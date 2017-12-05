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
 * 初始化监听处理
 * 
 * @author w00296102
 */
public class ManagerInitListener extends InitListener
{
    /**
     * 构造函数
     */
    public ManagerInitListener()
    {
        initializer = new ServletInitChild();
    }
    
    /**
     * 内部类
     */
    private class ServletInitChild extends ServletInit
    {
        @Override
        protected int beforeInit(String configPath)
        {
            int retCode = super.beforeInit(configPath);
            if (retCode != 0)
            {
                return retCode;
            }
            ManagerConfig.parse(SysConfig
                    .getConfig(ManagerConfig.SYSCONFIG_MANAGER));
            /**
             * 提交操作之前，需要校验权限
             */
            if (!MethodConfig.addMethodType("AUTHRDB",
                    JsonRequestConfig.class, AuthRDBProcessConfig.class,
                    RDBResponseConfig.class, new JsonRequester(),
                    new AuthRDBProcessor(), new JsonResponser()))
            {
                return 10001;
            }
            /**
             * 打开页面之前，需要校验权限
             */
            if (!MethodConfig.addMethodType("AUTHPAGE",
                    JsonRequestConfig.class, AuthPageProcessConfig.class,
                    HtmlResponseConfig.class, new JsonRequester(),
                    new AuthPageProcessor(), new AuthHtmlResponser()))
            {
                return 10002;
            }
            /**
             * 上传文件
             */
            if (!MethodConfig.addMethodType("OMUPLOAD",
                    RestRequestConfig.class, OMUploadProcessConfig.class,
                    RDBResponseConfig.class, new UploadRequester(),
                    new OMUploadProcessor(), new TextJsonResponser()))
            {
                return 10003;
            }

            /**
             * 以rest/json方式传递参数，返回json内容
             * 从AuthRDBProcessor继承，在执行鉴权与请求之前，先判断验证码是否正确 用在新增数据中，防止脏数据攻击
             */
            if (!MethodConfig.addMethodType("VERIFYAUTHRDB",
                    JsonRequestConfig.class, RDBProcessConfig.class,
                    RDBResponseConfig.class, new JsonRequester(),
                    new VerifyAuthRDBProcessor(), new JsonResponser()))
            {
                return 10004;
            }

            RetCodeExt.init();
            MethodConfig.setDefaultAuthType(MethodConfig.AUTH_USER);
            return 0;
        }

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
    }
}
