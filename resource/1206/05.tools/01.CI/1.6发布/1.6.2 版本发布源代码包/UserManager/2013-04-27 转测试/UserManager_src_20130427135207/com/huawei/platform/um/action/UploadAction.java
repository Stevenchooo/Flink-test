/**
 * LoginAction.java
 */
package com.huawei.platform.um.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.huawei.platform.common.CException;
import com.huawei.platform.um.constants.ResultCode;
import com.huawei.platform.um.constants.type.UploadFileType;
import com.huawei.platform.um.domain.FileDeployInfo;
import com.huawei.platform.um.entity.NodeInfoEntity;
import com.huawei.platform.um.entity.ReportConfigEntity;
import com.huawei.platform.um.entity.UserInfoEntity;
import com.huawei.platform.um.entity.UserProfileEntity;
import com.huawei.platform.um.sftp.SFTPClientImpl;
import com.huawei.platform.um.utils.UMUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 用户登录
 * 
 * @author z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2012-4-26]
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
            FileDeployInfo fileDeploy = parseRequest(request, null);
            SFTPClientImpl sftpClient = new SFTPClientImpl();
            sftpClient.setHost(fileDeploy.getHost());
            sftpClient.setUsername(fileDeploy.getUserName());
            sftpClient.setPassword(fileDeploy.getPassword());
            sftpClient.setPort(fileDeploy.getPort());
            //权限验证
            List<String> existFiles = sftpClient.checkChannelOK(fileDeploy.getDir(), fileDeploy.getCheckFileNames());
            
            result = UMUtil.buildReturnObj("existFiles", existFiles);
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
                FileDeployInfo fileDeploy = parseRequest(request, fileFileName);
                SFTPClientImpl sftpClient = new SFTPClientImpl();
                sftpClient.setHost(fileDeploy.getHost());
                sftpClient.setUsername(fileDeploy.getUserName());
                sftpClient.setPassword(fileDeploy.getPassword());
                sftpClient.setPort(fileDeploy.getPort());
                
                //拷贝文件
                String realName =
                    sftpClient.scp(file, fileDeploy.getDir(), fileDeploy.getFileName(), fileDeploy.isRename());
                
                result = UMUtil.buildReturnObj("visitedUrl", fileDeploy.getVisitedUrl() + realName);
            }
            else
            {
                LOGGER.error("no file object!");
                result = UMUtil.buildReturnObj();
            }
            
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
    private FileDeployInfo parseRequest(HttpServletRequest request, String fileName)
        throws CException
    {
        UserInfoEntity userInfo = new UserInfoEntity();
        userInfo.setUserName(getUserName());
        UserInfoEntity userDetail = getUmDao().getUserInfo(userInfo);
        
        //不存在用户报错
        if (null == userDetail)
        {
            LOGGER.error("userInfo[{}] not exist!", userInfo);
            throw new CException(ResultCode.USER_NOT_EXIST, new Object[] {userInfo.getUserName()});
        }
        
        Integer fileType = Integer.parseInt(request.getParameter("fileType"));
        
        FileDeployInfo deployInfo = new FileDeployInfo();
        deployInfo.setUserName(userDetail.getUserName());
        //查询用户密码
        deployInfo.setPassword(UMUtil.decrypt(userDetail.getPassword()));
        deployInfo.setFileName(fileName);
        
        //是否对重名文件重命名
        deployInfo.setRename("true".equals(request.getParameter("rename")));
        
        String fileStr = request.getParameter("files");
        if (StringUtils.isNotBlank(fileStr))
        {
            //解析文件名列表
            String[] files = fileStr.split(";");
            deployInfo.setCheckFileNames(new HashSet<String>());
            for (String name : files)
            {
                if (StringUtils.isNotBlank(name))
                {
                    deployInfo.getCheckFileNames().add(name);
                }
            }
        }
        
        if (fileType == UploadFileType.CPT_TEMPLATE)
        {
            Integer nodeId = Integer.parseInt(request.getParameter("nodeId"));
            ReportConfigEntity reportCfig = getUmDao().getReportCfg(nodeId);
            UserProfileEntity profile = getUmDao().getUserProfile(getUserName());
            NodeInfoEntity node = getUmDao().getNodeInfo(nodeId);
            deployInfo.setHost(node.getHost());
            deployInfo.setPort(node.getPort());
            
            String dir;
            if (reportCfig.getCptDir().endsWith("/"))
            {
                //相对路径
                dir = reportCfig.getCptDir().concat(profile.getCptLocation());
            }
            else
            {
                dir = reportCfig.getCptDir().concat("/").concat(profile.getCptLocation());
            }
            
            deployInfo.setDir(dir);
            if (null != fileName)
            {
                String url = reportCfig.getAddressPrefix() + profile.getCptLocation().replace('/', '\\');
                if (!url.endsWith("/"))
                {
                    url = url.concat("/");
                }
                deployInfo.setVisitedUrl(url);
            }
        }
        else
        {
            deployInfo.setHost("127.0.0.1");
            deployInfo.setDir("$HOME/mfs/share");
        }
        
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
