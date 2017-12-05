package com.huawei.ide.portal;

/**
 * 
 * 系统配置文件操作类
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月15日]
 * @see  [相关类/方法]
 */
public class SystemConfigService extends AbstractConfigService
{
    private static volatile AbstractConfigService sysconfigService;
    
    /**
     * 资源文件路径
     */
    private static final String CONFIG_FILE_PATH =
        SystemConfigService.class.getResource("/").getFile() + "sysconfig.properties";
    
    /**
     * 单例模式，Double check保证多线程访问我的一致性。
     * 
     * @return 配置Service对象
     */
    public static AbstractConfigService createConfigService()
    {
        if (null == sysconfigService)
        {
            synchronized (SystemConfigService.class)
            {
                if (null == sysconfigService)
                {
                    sysconfigService = new SystemConfigService();
                    CONFIGSERVICE_SET.add(sysconfigService);
                    sysconfigService.initConfigInfo();
                }
            }
        }
        return sysconfigService;
    }
    
    @Override
    protected String getConfigFilePath()
    {
        return CONFIG_FILE_PATH;
    }
    
}
