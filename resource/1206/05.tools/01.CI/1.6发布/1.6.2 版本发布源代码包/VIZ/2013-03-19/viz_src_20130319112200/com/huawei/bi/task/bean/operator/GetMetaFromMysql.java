package com.huawei.bi.task.bean.operator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.bi.common.domain.TreeNode;
import com.huawei.bi.task.da.HiveMetaDA;
import com.huawei.bi.task.da.UserDA;
import com.huawei.bi.task.domain.User;
import com.huawei.bi.util.Constant;

public class GetMetaFromMysql
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GetMetaFromMysql.class);
    
    UserDA userDa = new UserDA();
    
    HiveMetaDA hiveMetaDA = new HiveMetaDA();
    
    public String getServerDesc(String user)
        throws Exception
    {
        StringBuilder sb = new StringBuilder();
        sb.append("user: " + user);
        sb.append("<br/>");
        
        return sb.toString();
    }
    
    public String getTableDesc(String user, String dbName, String tableName)
        throws Exception
    {
        return hiveMetaDA.getTableDesc(getConnId(user), dbName, tableName);
    }
    
    public String getDbDesc(String user, String dbName)
        throws Exception
    {
        return hiveMetaDA.getDbDesc(getConnId(user), dbName);
    }
    
    private Integer getConnId(String user)
        throws Exception
    {
        //用户不存在
        if (StringUtils.isBlank(user))
        {
            return null;
        }
        
        User userInfo = userDa.getUser(user);
        if (null == userInfo || null == userInfo.getConnId())
        {
            LOGGER.error("please config user[{}] in db!", user);
            return null;
        }
        
        return userInfo.getConnId();
    }
    
    public TreeNode getMetaDataTree(String user)
        throws Exception
    {
        //用户不存在
        if (StringUtils.isBlank(user))
        {
            return null;
        }
        
        User userInfo = userDa.getUser(user);
        if (null == userInfo || null == userInfo.getConnId())
        {
            LOGGER.error("please config user[{}] in db!", user);
            return null;
        }
        
        return getMetaDataTree(user, userInfo.getConnId(), userInfo.getHivedbs());
        
    }
    
    /**
     * 获取生成环境的元数据
     * @return 生成环境的元数据
     */
    public TreeNode getProdMetaDataTree()
    {
        return getMetaDataTree(null, Constant.MYSQL_CONN_PROD, null);
    }
    
    public TreeNode getMetaDataTree(String user, Integer connId, String hiveDbs)
    {
        if (null == connId)
        {
            return null;
        }
        
        String serverName = user;
        
        //构造默认值
        if (Constant.MYSQL_CONN_PROD == connId)
        {
            serverName = "ProdDB";
        }
        
        if (StringUtils.isBlank(hiveDbs))
        {
            hiveDbs = "default";
        }
        
        try
        {
            List<String> dbList = hiveMetaDA.getDBs(connId);
            
            // server's node
            TreeNode serverNode = new TreeNode();
            serverNode.setName(null == serverName ? hiveDbs : serverName);
            serverNode.setType(TreeNode.TYPE_SERVER);
            serverNode.setOpen(true);
            
            List<TreeNode> dbNodeList = new ArrayList<TreeNode>();
            //显示所有的dbs
            boolean listAllDBs = "*".equals(hiveDbs.trim());
            List<String> showDBs = Arrays.asList(hiveDbs.trim().split(","));
            for (String db : dbList)
            {
                if (!listAllDBs && !showDBs.contains(db))
                {
                    //不显示的树中
                    continue;
                }
                // database's node
                TreeNode dbNode = new TreeNode();
                dbNode.setName(db);
                dbNode.setPname(null == serverName ? hiveDbs : serverName);
                dbNode.setType(TreeNode.TYPE_DB);
                dbNode.setOpen(true);
                
                List<String> tableList = hiveMetaDA.getTables(connId, db);
                List<TreeNode> tableNodeList = new ArrayList<TreeNode>();
                for (String table : tableList)
                {
                    TreeNode node = new TreeNode();
                    node.setName(table);
                    node.setPname(db);
                    node.setType(TreeNode.TYPE_TABLE);
                    tableNodeList.add(node);
                }
                // add table's node into database's
                dbNode.setChildren(tableNodeList);
                
                dbNodeList.add(dbNode);
            }
            // add database's node into server's
            serverNode.setChildren(dbNodeList);
            
            return serverNode;
            
        }
        catch (Exception e)
        {
            LOGGER.error("error occured when read from prod-mysql:" + e);
        }
        
        return null;
    }
}
