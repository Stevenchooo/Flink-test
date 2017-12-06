/**
 * LoginAction.java
 */
package com.huawei.platform.um.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.huawei.platform.common.CException;
import com.huawei.platform.um.constants.ResultCode;
import com.huawei.platform.um.constants.UMConfig;
import com.huawei.platform.um.constants.type.AppType;
import com.huawei.platform.um.domain.FileDeployInfo;
import com.huawei.platform.um.entity.UserInfoEntity;
import com.huawei.platform.um.sftp.SFTPClientImpl;
import com.huawei.platform.um.utils.UMUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 用户登录
 * 
 * @author z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-4-26]
 */
public class UploadAction extends BaseAction
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3652349623107620907L;
    
    /**
     * LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAction.class);
    
    private File file;
    
    private String fileFileName;
    
    private String fileContentType;
    
    /**
     * 检查通道是否正确，以防止上传完毕后才发现通道不可用
     */
    public String checkChannelOK()
    {
        Map<String, Object> result;
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try
        {
            response.setCharacterEncoding("UTF-8");
            FileDeployInfo fileDeploy = parseRequest(request);
            SFTPClientImpl sftpClient = new SFTPClientImpl();
            sftpClient.setHost(fileDeploy.getHost());
            sftpClient.setUsername(fileDeploy.getUserName());
            sftpClient.setPassword(fileDeploy.getPassword());
            sftpClient.setPort(fileDeploy.getPort());
            //权限验证
            sftpClient.checkChannelOK(fileDeploy.getDir());
            
            result = UMUtil.buildReturnObj();
        }
        catch (Exception e)
        {
            LOGGER.error("checkChannelOK failed!", e);
            result = UMUtil.buildCommonException(e);
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(result).getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("checkChannelOK failed!", e);
        }
        
        return SUCCESS;
    }
    
    /**
     * 上传文件
     * @return 文件是否上传成功
     */
    public String upload()
    {
        Map<String, Object> result;
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try
        {
            response.setCharacterEncoding("UTF-8");
            if (null != file)
            {
                FileDeployInfo fileDeploy = parseRequest(request);
                fileDeploy.setFileName(fileFileName);
                
                SFTPClientImpl sftpClient = new SFTPClientImpl();
                sftpClient.setHost(fileDeploy.getHost());
                sftpClient.setUsername(fileDeploy.getUserName());
                sftpClient.setPassword(fileDeploy.getPassword());
                sftpClient.setPort(fileDeploy.getPort());
                
                //拷贝文件
                sftpClient.scp(file, fileDeploy.getDir(), fileDeploy.getFileName());
            }
            else
            {
                LOGGER.error("no file object!");
            }
            
            result = UMUtil.buildReturnObj();
        }
        catch (Exception e)
        {
            LOGGER.error("upload failed!", e);
            result = UMUtil.buildCommonException(e);
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(result).getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("upload failed!", e);
        }
        
        return SUCCESS;
    }
    
    /**
     * 从请求中提取部署信息
     * @param request 请求
     * @return 部署新型
     * @throws CException 异常
     */
    private FileDeployInfo parseRequest(HttpServletRequest request)
        throws CException
    {
        UserInfoEntity userInfo = new UserInfoEntity();
        userInfo.setNodeOrClusterId(UMConfig.getUmDeployNodeId());
        userInfo.setAppType(AppType.OS);
        userInfo.setUser(getOperator().getOperatorName());
        UserInfoEntity userDetail = getUmDao().getUserInfo(userInfo);
        
        //不存在用户报错
        if (null == userDetail)
        {
            LOGGER.error("userInfo[{}] not exist!", userInfo);
            throw new CException(ResultCode.USER_NOT_EXIST, new Object[] {userInfo.getUser()});
        }
        
        FileDeployInfo deployInfo = new FileDeployInfo();
        deployInfo.setHost("127.0.0.1");
        deployInfo.setUserName(userDetail.getUser());
        //查询用户密码
        deployInfo.setPassword(userDetail.getPassword());
        deployInfo.setDir("$HOME/mfs/share");
        
        return deployInfo;
    }
    
    public File getFile()
    {
        return file;
    }
    
    public void setFile(File file)
    {
        this.file = file;
    }
    
    public String getFileFileName()
    {
        return fileFileName;
    }
    
    public void setFileFileName(String fileFileName)
    {
        this.fileFileName = fileFileName;
    }
    
    public String getFileContentType()
    {
        return fileContentType;
    }
    
    public void setFileContentType(String fileContentType)
    {
        this.fileContentType = fileContentType;
    }
}
