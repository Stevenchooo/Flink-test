package com.huawei.ide.interceptors.res.rcm;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

//import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;

/**
 * <存放公共变量和公共方法>
 * 
 * @author w00202142 & z00227010 & z00280396
 * 
 */
public class CommonUtils
{

    private static WebApplicationContext webAppContext = ContextLoader
            .getCurrentWebApplicationContext();

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CommonUtils.class);

    private static final String REGEX = "[0-9]{13,13}";

    // 1461108600

    // public static void main(String args[]){
    // System.out.println(getTimeForReq("1461083400"));
    // }
    /**
     * 获取指定Bean 注意：容器未初始化前不能调用getBeanByID方法
     * 
     * @param beanID
     *            Bean定义的唯一标识
     * @return 指定名称Bean实例
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public static Object getBeanByID(String beanID)
    {
        if (null == webAppContext)
        {
            webAppContext = ContextLoader.getCurrentWebApplicationContext();
        }

        if (StringUtils.isBlank(beanID))
        {
            return null;

        }

        return webAppContext.getBean(beanID);
    }

    /**
     * getTimeForReq <功能详细描述>
     * 
     * @param timestamp
     *            timestamp
     * @return String
     * @see [类、类#方法、类#成员]
     */
    public static String getTimeForReq(String timestamp)
    {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        String date = sdf.format(new Date(Long.valueOf(timestamp)));

        String hour = date.substring(11, 13);

        return hour;
    }

    /**
     * 解析请求json消息体为相应的对象
     * 
     * @param body
     *            请求json消息体
     * @param reqClass
     *            请求json消息体对应的类
     * @param <T>
     *            泛型类型
     * @return 请求对象
     */
    public static <T extends Object> T parseObject(String body,
            Class<T> reqClass)
    {
        // 在json对象外部包装上req字段名，方便读取和转换对象
        JSONObject json = JSONObject.parseObject(String.format("{\"req\":%s}",
                body));
        T req = json.getObject("req", reqClass);
        return req;
    }

    /**
     * jsonToReq <功能详细描述>
     * 
     * @param body
     *            body
     * @return RcmForThemeUserReq
     * @throws UnsupportedEncodingException
     *             UnsupportedEncodingException
     * @throws JSONException
     *             JSONException
     * @see [类、类#方法、类#成员]
     */
    public static RcmForThemeUserReq jsonToReq(String body)
        throws UnsupportedEncodingException, JSONException
    {

        RcmForThemeUserReq req = new RcmForThemeUserReq();

        JSONObject jsonOriginal = JSONObject.parseObject(String.format(
                "{\"req\":%s}", body));

        JSONObject json = (JSONObject) jsonOriginal.get("req");

        ArrayList<String> confirmedParaments = new ArrayList<String>();
        confirmedParaments.add("appKey");
        confirmedParaments.add("appSecret");
        confirmedParaments.add("deviceId");
        confirmedParaments.add("reqId");
        confirmedParaments.add("ts");

        req.setAppKey(json.getString("appKey"));

        req.setAppSecret(json.getString("appSecret"));

        String deviceIdCrypt = json.getString("deviceId");

        String deviceIdKey = decryptKey("deviceCode");
        String deviceId = null;
        try
        {
            deviceId = CryptUtil
                    .decryptForAESCBCStr(deviceIdCrypt, deviceIdKey);
        }
        catch (Exception e)
        {
            LOGGER.error("the deviceId is invalid!");
        }

        req.setDeviceID(deviceId == null ? null : deviceId);

        req.setReqId(json.getString("reqId"));

        req.setTs(json.getString("ts"));

        HashMap<String, Object> paramMap = new HashMap<String, Object>();

        for (Entry<String, Object> entry : json.entrySet())
        {

            if (!(confirmedParaments.contains(entry.getKey())))
            {

                paramMap.put(entry.getKey(), entry.getValue());

            }

        }
        req.setParamMap(paramMap);
        return req;
    }

    /**
     * jsonToItemListReq <功能详细描述>
     * 
     * @param body
     *            body
     * @return RcmForItemListReq
     * @see [类、类#方法、类#成员]
     */
    public static RcmForItemListReq jsonToItemListReq(String body)
    {
        RcmForItemListReq req = new RcmForItemListReq();

        JSONObject jsonOriginal = JSONObject.parseObject(String.format(
                "{\"req\":%s}", body));

        JSONObject json = (JSONObject) jsonOriginal.get("req");

        ArrayList<String> confirmedParaments = new ArrayList<String>();

        confirmedParaments.add("appKey");
        confirmedParaments.add("rcmScenario");
        confirmedParaments.add("deviceId");
        confirmedParaments.add("reqId");
        confirmedParaments.add("ts");
        confirmedParaments.add("rcmCount");

        req.setAppKey(json.getString("appKey"));
        req.setRcmCount(json.getInteger("rcmCount"));
        req.setRcmScenario(json.getString("rcmScenario"));
        req.setDeviceId(json.getString("deviceId"));
        req.setReqId(json.getString("reqId"));
        req.setTs(json.getString("ts"));

        HashMap<String, Object> paramMap = new HashMap<String, Object>();

        for (Entry<String, Object> entry : json.entrySet())
        {

            if (!(confirmedParaments.contains(entry.getKey())))
            {

                paramMap.put(entry.getKey(), entry.getValue());

            }

        }
        req.setParamMap(paramMap);

        return req;
    }

    /**
     * 加密密钥解密 <功能详细描述>
     * 
     * @param parame
     *            parame
     * @return String
     * @see [类、类#方法、类#成员]
     */
    public static String decryptKey(String parame)
    {

        String keywordPart = CommonUtils
                .getSysConfigValueByKey(RcmServiceConstants.CONF_KEYWORDTWO);

        String encryptedSecretKey = CommonUtils.getSysConfigValueByKey(parame);

        String decryptKey = CryptUtil.decryptForAESCBCStr(encryptedSecretKey,
                RcmServiceConstants.KEYWORDP1 + keywordPart);

        return decryptKey;
    }

    /**
     * hiboard本地应用推荐验证签名字段appSecret <功能详细描述>
     * 
     * @param appSecret
     *            appSecret
     * @param timestamp
     *            timestamp
     * @return String
     * @see [类、类#方法、类#成员]
     */
    public static String checkSecret(String appSecret, String timestamp)
    {
        // TODO Auto-generated method stub
        String keywordPart = CommonUtils
                .getSysConfigValueByKey(RcmServiceConstants.CONF_KEYWORDTWO);

        // 消息头为空则鉴权失败，抛出异常
        if (null == appSecret || appSecret.isEmpty()
                || appSecret.length() > 256)
        {
            LOGGER.info("The appSecret is invalid!");
            return RCMResultCodeConstants.INVALID_APPSECRET;
        }

        String encryptedSecretKey = CommonUtils
                .getSysConfigValueByKey("authcode");

        String accessCode = CryptUtil.decryptForAESCBCStr(encryptedSecretKey,
                RcmServiceConstants.KEYWORDP1 + keywordPart);

        if (accessCode.trim().length() == 0)
        {
            LOGGER.error("error! decryptForAESStr result is null");
            throw new CException(RCMResultCodeConstants.SERVICE_AUTH_FAILED,
                    "Authorizatioin is failed for current operation.");
        }

        String value = null;
        // 待修改
        try
        {

            if (null == timestamp || timestamp.isEmpty()
                    || !timestamp.matches(REGEX))
            {
                LOGGER.info("The timestamp is invalid!");
                return RCMResultCodeConstants.INVALID_TIMESTAMP;
            }

            // 使用hamc-sha256加密算法机密
            value = CryptUtil.hmacSHAEncrypt(timestamp, accessCode,
                    "HmacSHA256");
        }
        catch (NoSuchAlgorithmException e)
        {
            LOGGER.error("encryptToSHA failed!");
            throw new CException(RCMResultCodeConstants.SERVICE_AUTH_FAILED, e);
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("encryptToSHAOne failed!");
            throw new CException(RCMResultCodeConstants.SERVICE_AUTH_FAILED, e);
        }

        catch (Exception e)
        {
            LOGGER.error("encryptToSHAOne failed!");
            throw new CException(RCMResultCodeConstants.SERVICE_AUTH_FAILED, e);
        }

        if (!(value.equals(appSecret)))
        {

            // 记录鉴权失败信息
            LOGGER.error(
                    "authentication failed! appID is {}, timestamp is {}, authInfo is {}",
                    new Object[]
                    {timestamp, appSecret});
            LOGGER.info("Do not pass the Authentication!");
            throw new CException(RCMResultCodeConstants.SERVICE_AUTH_FAILED);
        }
        return "";

    }

    /**
     * 认证消息头是否合法
     * 
     * @param requestId
     *            应用标识
     * @param timestamp
     *            时间戳(绝对秒表示)
     * @param authorization
     *            待比较hmacsha1加密串
     * @throws CException
     *             不合法异常
     * @throws UnsupportedEncodingException
     *             UnsupportedEncodingException
     */
    public static void authentication(final String authorization,
            final String body) throws CException, UnsupportedEncodingException
    {

        String keywordPart = CommonUtils
                .getSysConfigValueByKey(RcmServiceConstants.CONF_KEYWORDTWO);
        // 获取接口鉴权认证开关值
        int authorizationSwitch = 0;

        String authSwitch = CommonUtils
                .getSysConfigValueByKey(RcmServiceConstants.CONF_AUTH_SWITCH);
        if (!(null == authSwitch))
        {
            authorizationSwitch = Integer.parseInt(authSwitch);
        }
        else
        {
            LOGGER.warn("get the authswitch from file failed,please check!");
        }

        // 开关为0，表示要不使用认证
        if (0 == authorizationSwitch)
        {
            LOGGER.debug("The AuthorizationSwitch is off. ");
            return;
        }

        if (1 == authorizationSwitch)
        {
            LOGGER.debug("The AuthorizationSwitch is on. ");
            // 消息头为空则鉴权失败，抛出异常
            if (null == authorization || authorization.isEmpty())
            {
                LOGGER.error("The authorization header is null.");
                throw new CException(
                        RCMResultCodeConstants.SERVICE_AUTH_FAILED,
                        "Authorizatioin is failed for current operation.");
            }

            String encryptedSecretKey = CommonUtils
                    .getSysConfigValueByKey("authcode");

            String accessCode = CryptUtil.decryptForAESCBCStr(
                    encryptedSecretKey, RcmServiceConstants.KEYWORDP1
                            + keywordPart);

            if (accessCode.trim().length() == 0)
            {
                LOGGER.error("error! decryptForAESStr result is null");
                throw new CException(
                        RCMResultCodeConstants.SERVICE_AUTH_FAILED,
                        "Authorizatioin is failed for current operation.");
            }

            String value = null;
            String cryptKey = null;
            String decryptKey = null;
            // 待修改
            try
            {
                cryptKey = CommonUtils.getSysConfigValueByKey("hmacCryKey");
                decryptKey = CryptUtil.decryptForAESCBCStr(cryptKey,
                        RcmServiceConstants.KEYWORDP1 + keywordPart);

                // 使用hamc-sha1加密算法机密
                value = CryptUtil.hmacSHAEncrypt(
                        String.format("%s%s", accessCode, body), decryptKey,
                        "HmacSHA1");
            }
            catch (NoSuchAlgorithmException e)
            {
                LOGGER.error("encryptToSHA failed!");
                throw new CException(
                        RCMResultCodeConstants.SERVICE_AUTH_FAILED, e);
            }
            catch (UnsupportedEncodingException e)
            {
                LOGGER.error("encryptToSHAOne failed!");
                throw new CException(
                        RCMResultCodeConstants.SERVICE_AUTH_FAILED, e);
            }

            catch (Exception e)
            {
                LOGGER.error("encryptToSHAOne failed!");
                throw new CException(
                        RCMResultCodeConstants.SERVICE_AUTH_FAILED, e);
            }

            if (!(value.equals(authorization)))
            {

                // 记录鉴权失败信息
                LOGGER.error(
                        "authentication failed! appID is {}, timestamp is {}, authInfo is {}",
                        new Object[]
                        {body, authorization});
                LOGGER.info("Do not pass the Authentication!");
                throw new CException(RCMResultCodeConstants.SERVICE_AUTH_FAILED);
            }

        }
    }

    /**
     * <格式化imei号>
     * 
     * @param deviceId
     *            imei号
     * @return 格式化后的imei号
     * @see [类、类#方法、类#成员]
     */
    public static String formatDeviceId(String deviceId)
    {
        if (null == deviceId)
        {
            return null;
        }
        else
        {
            if (16 <= deviceId.length())
            {
                return deviceId.toLowerCase(Locale.US);
            }
            else
            {
                return ('0' + StringUtils.rightPad(deviceId, 15, '0'))
                        .toLowerCase(Locale.US);
            }
        }
    }

    /**
     * 根据异常码获取异常描述信息
     * 
     * @param errorCode
     *            异常码
     * @param fmtObjects
     *            待格式化的异常描述信息
     * @return 异常描述信息
     * @see [类、类#方法、类#成员]
     */
    public static String getErrorDesc(int errorCode, Object... fmtObjects)
    {
        // 资源文件获取配置的异常信息
        String cfgErrorDesc = getErrorDescFromResource(errorCode);

        // 如果未配置指定异常码对应的异常描述信息
        if (null == cfgErrorDesc || "".equals(cfgErrorDesc))
        {
            cfgErrorDesc = "Unkown exception or error occured.";
        }

        // 没有需要合入并格式化的参数信息则直接返回资源文件定义的异常信息描述
        if ((null == fmtObjects) || (fmtObjects.length < 1))
        {
            return cfgErrorDesc;
        }

        return MessageFormat.format(cfgErrorDesc, fmtObjects);
    }

    /**
     * 从错误码资源文件获取指定错误码对应的错误描述信息
     * 
     * @param errorCode
     *            异常错误码
     * @return 配置的错误描述信息
     * @see [类、类#方法、类#成员]
     */
    private static String getErrorDescFromResource(int errorCode)
    {
        ErrorDescConfigService exDefServ = (ErrorDescConfigService) getBeanByID("exDefConfigService");
        if (null != exDefServ)
        {
            return exDefServ.getExceptionDesc(String.valueOf(errorCode));
        }
        return "";
    }

    /**
     * 从系统配置文件中获取配置项信息
     * 
     * @param key
     *            配置项key值
     * @return 配置信息
     * @see [类、类#方法、类#成员]
     */
    public static String getSysConfigValueByKey(String key)
    {

        RcmServiceSysConfigService sysConfigService = RcmServiceSysConfigService
                .getConfigService();
        return (null == sysConfigService) ? null : sysConfigService
                .getSysConfigKey(key);
    }

    public static ParamCheckerConfigService getFieldValidService()
    {
        return (ParamCheckerConfigService) getBeanByID("fieldValidateCfgService");
    }

    /**
     * hashCode
     * 
     * @return int
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    /**
     * equals
     * 
     * @param obj
     *            obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }

}
