package com.huawei.ide.services.algorithm.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.dc.sdk.DCSDKException;
import com.huawei.ide.interceptors.res.rcm.CException;
import com.huawei.ide.interceptors.res.rcm.CommonUtils;
import com.huawei.ide.interceptors.res.rcm.RCMResultCodeConstants;
import com.huawei.ide.services.algorithm.entity.AppHeapEntity;
import com.huawei.ide.services.algorithm.entity.FilterHeap;
import com.huawei.ide.services.algorithm.entity.Items;
import com.huawei.ide.services.redis.DCClientService;

/**
 * 
 * RcmRedisExecutor <功能详细描述>
 * 
 * @author z00280396
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see [相关类/方法]
 */
public class RcmRedisExecutor
{
    /**
     * SEPARATOR
     */
    public static final String SEPARATOR = ":";
    
    /**
     * REDISVALUESEPARATOR
     */
    public static final String REDISVALUESEPARATOR = "#";
    
    private static final String RCM_SCENARIO_VALUE = "1";
    
    private static final String TEMPLATE_ID = "templateId";
    
    private static final String HIBOARD = "hiboard";
    
    /*
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RcmRedisExecutor.class);
    
    /**
     * getResultFRedis <功能详细描述>
     * 
     * @param dcClientService
     *            dcClientService
     * @param variables
     *            variables
     * @param algorithmName
     *            algorithmName
     * @return RcmApp[]
     * @see [类、类#方法、类#成员]
     */
    public static final Items[] getResultFRedis(DCClientService dcClientService, Map<String, Object> variables,
        String algorithmName)
    {
        LOGGER.info("getResultFRedis start..");
        String imei = variables.get("deviceID").toString().toLowerCase();
        String appKey = variables.get("appKey").toString();
        String rcmScenario = variables.get("rcmScenario").toString();
        String sceneName = appKey;
        String sceneId = CommonUtils.getSysConfigValueByKey(sceneName);
        String algorithmId = CommonUtils.getSysConfigValueByKey(algorithmName);
        String appScene = variables.get("showScene").toString();
        String showId = CommonUtils.getSysConfigValueByKey("showScene" + "_" + appScene);
        String redisKeyString = sceneId + SEPARATOR + algorithmId + SEPARATOR + imei + SEPARATOR + showId;
        // 前台传来的rcmCount
        int rcmCount = Integer.parseInt(variables.get("rcmCount").toString());
        Items[] redisResult =
            getRedisRcmList(dcClientService, redisKeyString, appScene, rcmScenario, rcmCount, sceneName);
        if (redisResult.length == 0)
        {
            variables.put("resultCode", RCMResultCodeConstants.REDIST_ERROR);
            LOGGER.error("Get rcmResult from redis is empty.");
            return new Items[] {};
            
        }
        
        else
        {
            variables.put("resultCode", 0);
            LOGGER.info("getResultFRedis end..");
            return redisResult;
        }
    }
    
    private static final Items[] getRedisRcmList(DCClientService dcClientService, String redisKeyString,
        String appScene, String rcmScenario, int count, String sceneName)
    {
        try
        {
            LOGGER.info("getRedisRcmList start..");
            String resultString = dcClientService.getRedisValue(redisKeyString);
            if (null == resultString || resultString.trim().length() == 0)
            {
                LOGGER.error("RcmRedisExecutor getRedisRcmList is empty.the key is " + redisKeyString);
                return new Items[] {};
            }
            String[] arr = resultString.split(REDISVALUESEPARATOR);
            if (arr.length == 0)
            {
                LOGGER.error("this imei have no rcmData in redis.");
                return new Items[] {};
                
            }
            int redisSize = arr.length;
            FilterHeap heap = new FilterHeap(redisSize);
            for (int i = 0; i < redisSize; i++)
            {
                AppHeapEntity redisRcmEntity = new AppHeapEntity();
                String[] redisAppWeight = arr[i].split(SEPARATOR);
                redisRcmEntity.setAppId(redisAppWeight[0]);
                redisRcmEntity.setWeight(Double.parseDouble(redisAppWeight[1]));
                heap.insert(redisRcmEntity);
            }
            Items[] rcmArr = getOrderTopAppList(heap, appScene, rcmScenario, sceneName);
            // 如果rcmcount大于redisSize，则用rcmArr，如果小于，就截取rcmArr中前count个元素的数组
            Items[] rcmResult = new Items[count];
            if (count >= redisSize)
            {
                rcmResult = rcmArr;
            }
            else
            {
                for (int i = 0; i < count; i++)
                {
                    rcmResult[i] = rcmArr[i];
                }
            }
            LOGGER.info("getRedisRcmList end..");
            return rcmResult;
        }
        catch (DCSDKException e)
        {
            LOGGER.error(RCMResultCodeConstants.REDIS_SDK_ERROR + "DCSDKException error! ");
            LOGGER.debug(e.getMessage(), e);
            return new Items[] {};
            
        }
        catch (CException e)
        {
            LOGGER.error(RCMResultCodeConstants.REDIS_MULL_ERROR + "this imei have no rcmData in redis. ");
            LOGGER.debug(e.getMessage(), e);
            return new Items[] {};
            
        }
    }
    
    private static final Items[] getOrderTopAppList(FilterHeap heap, String appScene, String rcmScenario,
        String sceneName)
    {
        LOGGER.info("getOrderTopAppList start..");
        // 获得top N个推荐应用的有序数组
        AppHeapEntity[] appArrOrdered = heap.generateList();
        // 以下生成返回结果
        List<Items> rcmAppList = new ArrayList<Items>();
        for (AppHeapEntity app : appArrOrdered)
        {
            if (null == app)
            {
                continue;
            }
            Items rcmRtn = new Items();
            rcmRtn.setItemId(app.getAppId());
            rcmRtn.setScore(String.valueOf(app.getWeight()));
            rcmRtn.setPubTime(appScene);
            
            if (RCM_SCENARIO_VALUE.equals(rcmScenario) && HIBOARD.equals(sceneName))
            {
                rcmRtn.setTemplateId(CommonUtils.getSysConfigValueByKey(TEMPLATE_ID));
            }
            
            rcmAppList.add(rcmRtn);
        }
        Items[] rcmArr = new Items[rcmAppList.size()];
        rcmAppList.toArray(rcmArr);
        LOGGER.info("getOrderTopAppList end..");
        return rcmArr;
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
