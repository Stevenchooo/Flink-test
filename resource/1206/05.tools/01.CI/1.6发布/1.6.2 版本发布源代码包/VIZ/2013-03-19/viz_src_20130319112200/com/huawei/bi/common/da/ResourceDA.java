package com.huawei.bi.common.da;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;

import com.huawei.bi.common.domain.Resource;
import com.huawei.bi.common.domain.TreeNode;
import com.huawei.bi.util.DBUtil;

public class ResourceDA
{
    /**
     * 
     * @param id
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Resource getResoruce(int id) throws ClassNotFoundException,
            SQLException
    {
        Resource result = null;
        
        String sql = "SELECT * FROM resource WHERE resourceId=?";
        Connection conn = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        
        try
        {
            conn = DBUtil.getConnection();
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, id);
            
            rs = pstat.executeQuery();
            
            if (rs.next())
            {
                result = new Resource();
                result.setId(rs.getInt("resourceId"));
                result.setType(rs.getString("resourceType"));
                result.setName(rs.getString("resourceName"));
                result.setPid(rs.getInt("parentId"));
                result.setFullPath(rs.getString("fullPath"));
                result.setUser(rs.getString("user"));
            }
        }
        finally
        {
            DBUtil.close(rs, pstat, conn);
        }
        
        return result;
    }
    
    public void updateName(int resourceId, String name) throws Exception
    {
        String sqlUpdate = "update resource set resourceName=? where resourceId=?";
        QueryRunner qr = new QueryRunner(DBUtil.getDataSource());
        qr.update(sqlUpdate, name,resourceId);
    }
    
    /**
     * 
     * @param resource
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int add(Resource resource, Resource folder)
            throws ClassNotFoundException, SQLException
    {
        int result = 0;
        String sqlInsert = "INSERT INTO resource (resourceType,resourceName,parentId,user) VALUES (?,?,?,?)";
        String sqlUpdate = "update resource set fullPath=? where resourceId=?";
        
        Connection conn = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        
        try
        {
            conn = DBUtil.getConnection();
            
            // insert
            pstat = conn.prepareStatement(sqlInsert,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pstat.setString(1, resource.getType());
            pstat.setString(2, resource.getName());
            pstat.setInt(3, folder.getId());
            pstat.setString(4, resource.getUser());
            
            pstat.executeUpdate();
            
            // get the id
            rs = pstat.getGeneratedKeys();
            if (rs.next())
            {
                result = rs.getInt(1);
            }
            
            if (result == 0)
            {
                throw new SQLException("Insert fails");
            }
            
            // update fullPath
            pstat = conn.prepareStatement(sqlUpdate);
            pstat.setString(1, folder.getFullPath() + "." + result);
            pstat.setInt(2, result);
            pstat.executeUpdate();
        }
        finally
        {
            DBUtil.close(rs, pstat, conn);
        }
        
        return result;
    }
    
    /**
     * get the resource root
     * 
     * @return
     * @throws Exception
     */
    public TreeNode getResourceRoot(String nodeType, String user) throws Exception
    {
        
        String sql = "SELECT * FROM resource WHERE user ='"+user+"' and resourceType IN ('folder','"
                + nodeType + "') ORDER BY fullPath";
        
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        
        TreeNode root = null;
        
        try
        {
            conn = DBUtil.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            
            root = popRoot(rs);
            
        }
        finally
        {
            DBUtil.close(rs, stat, conn);
        }
        
        return root;
    }
    
    /*
     * 
     */
    private TreeNode popRoot(ResultSet rs) throws SQLException
    {
        
        Map<String, TreeNode> map = new HashMap<String, TreeNode>();
        TreeNode root = null;
        while (rs.next())
        {
            
            TreeNode node = new TreeNode();
            
            int resourceId = rs.getInt("resourceId");
            String resourceType = rs.getString("resourceType");
            String resourceName = rs.getString("resourceName");
            int parentId = rs.getInt("parentId");
            String fullPath = rs.getString("fullPath");
            
            node.setIsParent("folder".equals(resourceType));
            node.setId(String.valueOf(resourceId));
            node.setPid(String.valueOf(parentId));
            node.setName(resourceName);
            node.setType(resourceType);
            
            map.put(fullPath, node);
            
            // add it to parent
            int idxOfLastDot = fullPath.lastIndexOf('.');
            if (idxOfLastDot > 0)
            {
                String parentFullPath = fullPath.substring(0, idxOfLastDot);
                TreeNode pNode = map.get(parentFullPath);
                node.setPname(pNode.getName());
                // add into parant node, happy...get home
                pNode.getChildren().add(node);
            }
            else
            {
                root = node;
            }
            
        }
        
        // TODO assume 1 is root
        if (root != null)
        {
            root.setOpen(true);
        }
        return root;
    }
    
    public void deleteByFullPath(String fullPath)
            throws ClassNotFoundException, SQLException
    {
        String sql = "delete FROM resource WHERE (fullPath = ? OR fullPath LIKE ?)";
        
        Connection conn = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        
        try
        {
            conn = DBUtil.getConnection();
            pstat = conn.prepareStatement(sql);
            
            pstat.setString(1, fullPath);
            pstat.setString(2, fullPath + ".%");
            
            pstat.executeUpdate();
        }
        finally
        {
            DBUtil.close(rs, pstat, conn);
        }
    }
}
