package com.huawei.bi.hive.bean;

import java.sql.SQLException;

import com.huawei.bi.common.da.ResourceDA;
import com.huawei.bi.common.domain.Resource;
import com.huawei.bi.hive.da.ScriptDA;
import com.huawei.bi.hive.domain.Script;

public class ScriptBean
{
    
    ScriptDA scriptDA = new ScriptDA();
    
    ResourceDA resourceDA = new ResourceDA();
    
    public int addFolder(int pfolderId, String folderName, String user) throws ClassNotFoundException, SQLException
    {
        Resource pfolder = resourceDA.getResoruce(pfolderId);
        if (null != pfolder && "folder".equals(pfolder.getType()) && user.equals(pfolder.getUser()))
        {
            Resource resource = new Resource();
            resource.setType("folder");
            resource.setName(folderName);
            resource.setUser(user);
            resource.setPid(pfolderId);
            
            // add it
            return resourceDA.add(resource,pfolder);
        }
        return 0;
    }
    
    public void updateResName(int resouceId, String name, String user) throws Exception
    {
        Resource resouce = resourceDA.getResoruce(resouceId);
        if (null != resouce && user.equals(resouce.getUser()))
        {
            resourceDA.updateName(resouceId,name);
        }
    }
    
    /**
     * 
     * @param folderId
     * @param scriptName
     * @param text
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public int add(int folderId, String scriptName, String text, String user)
        throws ClassNotFoundException, SQLException
    {
        // get parent
        Resource folder = resourceDA.getResoruce(folderId);
        if (null != folder && "folder".equals(folder.getType()) && user.equals(folder.getUser()))
        {
            // create resource first
            Resource resource = new Resource();
            resource.setType("script");
            resource.setName(scriptName);
            resource.setUser(user);
            resource.setPid(folderId);
            
            // add it
            int id = resourceDA.add(resource, folder);
            
            // create script object
            Script script = new Script();
            script.setResourceId(id);
            script.setText(text);
            
            // add script
            scriptDA.add(script);
            
            return id;
        }
        
        return 0;
    }
    
    public void updateCode(int resourceId, String text, String user)
        throws ClassNotFoundException, SQLException
    {
        //权限控制
        Resource resource = resourceDA.getResoruce(resourceId);
        if (null != resource && user.equals(resource.getUser()))
        {
            Script script = new Script();
            script.setResourceId(resourceId);
            script.setText(text);
            
            scriptDA.update(script);
        }
    }
    
    public Script getScript(int resourceId, String user)
        throws ClassNotFoundException, SQLException
    {
        //权限控制
        Resource resource = resourceDA.getResoruce(resourceId);
        if (null != resource && user.equals(resource.getUser()))
        {
            return scriptDA.get(resourceId);
        }
        else
        {
            return null;
        }
    }
    
    public void deleteResouce(int resouceId, String user)
        throws ClassNotFoundException, SQLException
    {
        Resource resource = resourceDA.getResoruce(resouceId);
        if (user.equals(resource.getUser()))
        {
            resourceDA.deleteByFullPath(resource.getFullPath());
            
            if("script".equals(resource.getType()))
            {
                scriptDA.delete(resouceId);
            }
        }
    }
}
