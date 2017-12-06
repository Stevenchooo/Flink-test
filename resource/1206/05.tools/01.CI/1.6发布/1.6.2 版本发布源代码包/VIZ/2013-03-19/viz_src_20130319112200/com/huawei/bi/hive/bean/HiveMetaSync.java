package com.huawei.bi.hive.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.huawei.bi.task.bean.logger.LogLogger;
import com.huawei.bi.task.bean.taskrunner.RemoteCommandExecute;
import com.huawei.bi.task.da.UserDA;

public class HiveMetaSync
{
    private static Logger logger = Logger.getLogger(HiveMetaSync.class);
    
    public boolean metaSyncJson(String user, String tabListJson, String defaultSrcDB, String defaultDstDB,
        String comFlag)
        throws JSONException
    {
        //user不能为空
        if (StringUtils.isBlank(user))
        {
            return false;
        }
        
        JSONArray jsonA = new JSONArray(tabListJson);
        JSONObject json;
        StringBuilder tabList = new StringBuilder();
        //解析json对象
        for (int i = 0; i < jsonA.length(); i++)
        {
            json = jsonA.getJSONObject(i);
            //json.get("dbname");
            tabList.append(json.get("tname"));
            tabList.append(';');
        }
        
        return metaSync(user, tabList.toString(), defaultSrcDB, defaultDstDB, comFlag);
    }
    
    public boolean metaSync(String user, String tabList, String defaultSrcDB, String defaultDstDB, String comFlag)
    {
        if (StringUtils.isEmpty(defaultSrcDB))
        {
            defaultSrcDB = "default";
        }
        
        if (StringUtils.isEmpty(defaultDstDB))
        {
            defaultDstDB = "default";
        }
        
        boolean flag = false;
        
        if (Integer.parseInt(comFlag) == 0)
        {
            try
            {
                String tblNames = tabList.replace(";", " ");
                RemoteCommandExecute rcE;
                String command = String.format("metasync.sh %s %s %s", defaultSrcDB, defaultDstDB, tblNames);
                rcE = new RemoteCommandExecute(new LogLogger(user, "***"));
                rcE.start(user, command);
                flag = true;
            }
            catch (Exception e)
            {
                logger.error("sync error!!!" + e);
            }
            
        }
        else if (Integer.parseInt(comFlag) == 1)
        {
            try
            {
                RemoteCommandExecute rcE;
                String[] tabNames = tabList.split(";");
                for (String tab : tabNames)
                {
                    String command = String.format("hive -e 'drop table %s'", tab);
                    rcE = new RemoteCommandExecute(new LogLogger(user, tab));
                    rcE.start(user, command);
                }
                flag = true;
            }
            catch (Exception e)
            {
                logger.error("sync error!!!" + e);
            }
            
        }
        return flag;
    }
    
    public boolean autoSync(String user)
        throws Exception
    {
        
        //user不能为空
        if (StringUtils.isBlank(user))
        {
            return false;
        }
        
        UserDA userDA = new UserDA();
        String tabs = userDA.getTab(user);
        if (StringUtils.isBlank(tabs))
        {
            return true;
        }
        
        String[] tblNames = tabs.split(";");
        boolean flag = false;
        if (tblNames != null && tblNames.length > 0)
        {
            String defaultSrcDB = "default";
            String defaultDstDB = "default";
            String comFlag = Integer.toString(0);
            StringBuilder tblSb = new StringBuilder();
            for (String table : tblNames)
            {
                String[] tabInfo = table.split(":");
                defaultSrcDB = tabInfo[0];
                defaultDstDB = tabInfo[1];
                String tabName = tabInfo[2];
                tblSb.append(tabName);
                tblSb.append(" ");
            }
            if (tblSb.length() > 0)
            {
                tblSb.deleteCharAt(tblSb.length() - 1);
            }
            
            flag = metaSync(user, tblSb.toString(), defaultSrcDB, defaultDstDB, comFlag);
        }
        else
        {
            flag = false;
        }
        return flag;
    }
    
    /**
     * 记录更新的表
     * @param user
     * @param tabListJson
     * @param comFlag
     * @return
     * @throws Exception
     */
    public boolean updateUserTabs(String user, String tabListJson, String defaultSrcDB, String defaultDstDB,
        String comFlag)
        throws Exception
    {
        if (StringUtils.isBlank(user))
        {
            return false;
        }
        
        if (StringUtils.isEmpty(defaultSrcDB))
        {
            defaultSrcDB = "default";
        }
        
        if (StringUtils.isEmpty(defaultDstDB))
        {
            defaultDstDB = "default";
        }
        
        UserDA userDA = new UserDA();
        String tables = userDA.getTab(user);
        // System.out.println(tables);
        StringBuilder autoTabs = new StringBuilder();
        
        List<String> tabs = new ArrayList<String>();
        if (tables != null && tables.length() > 0)
        {
            for (String table : tables.split(";"))
            {
                tabs.add(table);
            }
        }
        JSONArray jsonA = new JSONArray(tabListJson);
        JSONObject json;
        StringBuilder tabList = new StringBuilder();
        //解析json对象
        for (int i = 0; i < jsonA.length(); i++)
        {
            json = jsonA.getJSONObject(i);
            //json.get("dbname");
            tabList.append(defaultSrcDB + ":");
            tabList.append(defaultDstDB + ":");
            tabList.append(json.get("tname"));
            tabList.append(';');
        }
        String[] tabNames = tabList.toString().split(";");
        for (String tab : tabNames)
        {
            if (tabs.contains(tab) && Integer.parseInt(comFlag) == 0)
            {
                continue;
            }
            else if (tabs.contains(tab) && Integer.parseInt(comFlag) == 1)
            {
                tabs.remove(tab);
            }
            else if (!tabs.contains(tab) && Integer.parseInt(comFlag) == 0)
            {
                tabs.add(tab);
            }
            else
            {
                continue;
            }
        }
        
        for (String autoSyncTab : tabs)
        {
            autoTabs.append(autoSyncTab);
            autoTabs.append(';');
        }
        logger.debug("表为" + autoTabs.toString());
        
        return userDA.updateTabList(user, autoTabs.toString());
    }
}
