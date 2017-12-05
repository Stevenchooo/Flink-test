package com.huawei.ide.interceptors.res.rcm;

/**
 * 
 * RcmServiceConstants
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
 * @see  [相关类/方法]
 */
public class RcmServiceConstants
{
    /**
     * 接口鉴权中使用的http消息头的Authorization字段中的appID
     */
    public static final String APP_ID = "appID";
    
    /**
     * 接口鉴权中使用的response
     */
    public static final String RESPONSE = "response";
    
    /**
     * 接口鉴权中使用的Authorization
     */
    public static final String AUTHORIZATION = "Authorization";
    
    /**
     * 根密钥partone
     */
    public static final String KEYWORDP1 = "KZZLDvMb";
    
    /**
     * system config
     */
    public static final String CONF_MAXITEMSTOCOMPARE = "maxItemsToCompare";
    
    /**
     * CONF_MAXDAYSEFFECTIVE
     */
    public static final String CONF_MAXDAYSEFFECTIVE = "maxDaysEffective";
    
    /**
     * 根密钥parttwo
     */
    public static final String CONF_KEYWORDTWO = "partkey";
    
    /**
     * 用户词表使用的下载app的个数
     */
    public static final String CONF_ITEMTOBACKTRACE = "itemToBacktrace";
    
    /**
     * 模型文件路径
     */
    public static final String CONF_MODELFILEPATH = "modelFilePath";
    
    /**
     * 用户词表文件路径
     */
    public static final String CONF_USERFILEPATH = "userFilePath";
    
    /**
     * 默认推荐列表的长度
     */
    public static final String CONF_SIZEOFRECOMMENDEDLIST = "sizeOfRecmmendedList";
    
    /**
     * 鉴权中使用的授权码
     */
    public static final String CONF_AUTHCODE = "authcode";
    
    /**
     * 后续文件路径
     */
    public static final String CONF_ORIGINLISTFILEPATH = "originListFilePath";
    
    /**
     * 鉴权开关
     */
    public static final String CONF_AUTH_SWITCH = "Authorization.switch";
    
    /**
     * hmac加密密钥密文
     */
    public static final String CONF_HMACCRYKEY = "hmacCryKey";
    
    /**
     * 用户词表imei号加密密钥密文
     */
    public static final String CONF_USERWORDKEY = "userWordKey";
    
    /**
     * 超时时间设置
     */
    public static final String CONF_OVERTIME = "overtime";
    
    /**
     * redis服务名
     */
    public static final String CONF_REDIS_SERVER = "redisServerName";
    
    /**
     * 祥云etc地址
     */
    public static final String CONF_REDIS_ETCDURL = "etcUrl";
    
    /**
     * 业务场景对应编号
     */
    public static final String CONF_RCM_SCENE = "10.51.30.31:2379";
}
