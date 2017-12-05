package com.huawei.manager.mkt.info;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  s00359263
 * @version [Internet Business Service Platform SP V100R100, 2015-11-19]
 * @see  [相关类/方法]
 *  
 */
public class FreqExportInfo extends ExportInfo
{
    //媒体名称
    private String mediaName = "";
    //频次列表
    private List<Integer> userList = null;
    public String getMediaName()
    {
        return mediaName;
    }
    public void setMediaName(String mediaName)
    {
        this.mediaName = mediaName;
    }
    public List<Integer> getUserList()
    {
        return userList;
    }
    public void setUserList(List<Integer> userList)
    {
        this.userList = userList;
    }
    
}
