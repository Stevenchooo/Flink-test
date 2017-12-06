/**
 * LoginAction.java
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCode;
import com.huawei.platform.tcc.constants.type.NameStoredInSession;
import com.huawei.platform.tcc.constants.type.OperType;
import com.huawei.platform.tcc.detector.SSHConnectDetector;
import com.huawei.platform.tcc.domain.UsernameAndPasswordParam;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.entity.OperatorInfoEntity;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.utils.TccUtil;
import com.huawei.platform.tcc.utils.crypt.CryptUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 用户登录
 * 
 * @author z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-4-26]
 */
public class LoginAction extends BaseAction
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3652349623107620907L;
    
    /**
     * LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAction.class);
    
    /**
     * 转到系统管理主页
     */
    private static final String TO_OPERAROR = "tooperator";
    
    /**
     * 转到普通管理主页
     */
    private static final String TO_SYSADMIN = "tosysadmin";
    
    /**
     * 用户姓名
     */
    private String userName;
    
    /**
     * 用户密码
     */
    private String password;
    
    /**
     * 验证码
     */
    private String verifyCode;
    
    /**
     * 错误返回信息
     */
    private String tip;
    
    public String getVerifyCode()
    {
        return verifyCode;
    }
    
    public void setVerifyCode(String verifyCode)
    {
        this.verifyCode = verifyCode;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getUserName()
    {
        return this.userName;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getPassword()
    {
        return this.password;
    }
    
    public String getTip()
    {
        return tip;
    }
    
    public void setTip(String tip)
    {
        this.tip = tip;
    }
    
    /**
     * 是否发现问题
     * @return 会话是否有效
     */
    public String foundFatalProblem()
    {
        try
        {
            boolean hasProblem = SSHConnectDetector.getInstance().foundProblem();
            if (hasProblem)
            {
                LOGGER.error("found fatal problem when tcc connect ssh server!");
            }
            setInputStream(new ByteArrayInputStream(Boolean.toString(hasProblem).getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            LOGGER.error("isSessionValid fail", e);
        }
        
        return SUCCESS;
    }
    
    /**
     * 会话是否有效
     * @return 会话是否有效
     */
    public String isSessionValid()
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try
        {
            response.setCharacterEncoding("UTF-8");
            
            boolean bValid = false;
            String operatorName = OperatorMgnt.getOperatorName(request.getSession());
            if (null != operatorName)
            {
                bValid = true;
            }
            
            setInputStream(new ByteArrayInputStream(Boolean.toString(bValid).getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            LOGGER.error("isSessionValid fail", e);
        }
        
        return SUCCESS;
    }
    
    /**
     * 用户登录方法
     * 
     * @return 展示页面
     */
    public String userLogin()
    {
        try
        {
            this.userName = checkInput(this.userName);
            this.password = checkInput(this.password);
            this.verifyCode = checkInput(this.verifyCode);
            
            if (StringUtils.isEmpty(this.userName))
            {
                this.tip = "请输入用户名！";
                return ERROR;
            }
            if (StringUtils.isEmpty(this.password))
            {
                this.tip = "请输入密码！";
                return ERROR;
            }
            UsernameAndPasswordParam param = new UsernameAndPasswordParam();
            param.setUserName(this.userName);
            param.setPassword(CryptUtil.encryptToSHA(this.password, "SHA-256"));
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            HttpSession session = request.getSession(true);
            //校验验证码是否合法
            if (!checkVerifyCode(session))
            {
                return ERROR;
            }
            
            OperatorInfoEntity operatorInfo = getOperatorMgnt().getOperatorInfo(param);
            if (null == operatorInfo)
            {
                this.tip = "用户名或密码错误！";
                return ERROR;
            }
            
            Object clientIp = session.getAttribute(NameStoredInSession.CLIENT_IP);
            if (null == clientIp)
            {
                Map<String, String> mapIp = TccUtil.getRealIp(request);
                session.setAttribute(NameStoredInSession.CLIENT_IP, mapIp.get(NameStoredInSession.CLIENT_IP));
                session.setAttribute(NameStoredInSession.IP_HEADER_NAME, mapIp.get(NameStoredInSession.IP_HEADER_NAME));
            }
            session.setAttribute(NameStoredInSession.USER_NAME, this.userName);
            
            //日志中记录登陆的用户，IP
            TccUtil.startLogIPUser2Log();
            
            LOGGER.info("log clientIp[clientIp={},ipHeaderName={}] to session!",
                session.getAttribute(NameStoredInSession.CLIENT_IP),
                session.getAttribute(NameStoredInSession.IP_HEADER_NAME));
            //日志
            LOGGER.info("operator[userName={},ip={}] login!",
                session.getAttribute(NameStoredInSession.USER_NAME),
                session.getAttribute(NameStoredInSession.CLIENT_IP));
            
            Operator operator = getOperator();
            
            if (null != operator)
            {
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.LOGIN);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setOperParameters(operator.toString());
                getOperationRecord().writeOperLog(operateAuditInfo);
                
                if (operator.isSystemAdmin())
                {
                    return TO_SYSADMIN;
                }
                else
                {
                    return TO_OPERAROR;
                }
            }
            else
            {
                return ERROR;
            }
        }
        catch (CException e)
        {
            if (ResultCode.INPUT_INVALID == ((CException)e).getErrorCode())
            {
                this.tip = "输入包含不合法字符!";
                this.userName = "";
            }
            
            LOGGER.error("userLogin failed!", e);
        }
        catch (Exception e)
        {
            LOGGER.error("userLogin failed!", e);
        }
        finally
        {
            TccUtil.stopLogIPUser2Log();
        }
        
        return ERROR;
    }
    
    /**
     * 用户登出，清除会话
     */
    public void logout()
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        //记录审计信息
        OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
        operateAuditInfo.setOpType(OperType.LOGOUT);
        operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
        operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
        getOperationRecord().writeOperLog(operateAuditInfo);
        
        HttpSession session = request.getSession(true);
        
        //日志
        LOGGER.info("operator[userName={},ip={}] logout!",
            session.getAttribute(NameStoredInSession.USER_NAME),
            session.getAttribute(NameStoredInSession.CLIENT_IP));
        
        session.invalidate();
    }
    
    /**
     * 从会话中取得用户名
     * @return 查询成功标志位
     * @throws Exception 异常
     */
    public String getUserNameFromSession()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            HttpSession session = request.getSession(true);
            String opName = (String)session.getAttribute("userName");
            if (null != opName)
            {
                //记录日志
                LOGGER.debug("getUserNameFromSession[{}] success.", opName);
                jsonObject.put("success", true);
                jsonObject.put("userName", opName);
            }
            else
            {
                //记录日志
                LOGGER.debug("getUserNameFromSession fail due to userName is null");
                jsonObject.put("success", false);
            }
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            setInputStream(new ByteArrayInputStream(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
            ;
            return SUCCESS;
        }
        catch (Exception e)
        {
            LOGGER.error("getUserNameFromSession fail", e);
            throw e;
        }
    }
    
    private boolean checkVerifyCode(HttpSession session)
    {
        if (null == session)
        {
            this.tip = "会话超时！";
            return false;
        }
        
        String codeInput = this.verifyCode;
        this.verifyCode = null;
        String codeServer = (String)session.getAttribute("HWVerifyCode");
        
        if (codeInput == null)
        {
            codeInput = "";
        }
        if (codeServer == null)
        {
            codeServer = "";
        }
        
        //防止验证码多次有效，防止空验证码
        session.setAttribute("HWVerifyCode", "");
        if (codeInput.length() > 0 && codeInput.equalsIgnoreCase(codeServer))
        {
            return true;
        }
        else
        {
            this.tip = "验证码不正确！";
        }
        
        return false;
    }
    
    /**
     * 访问不存在的页面
     * @return 返回的页面
     */
    public String toDefault()
    {
        Operator operator = getOperator();
        if (null != operator)
        {
            if (operator.isSystemAdmin())
            {
                return TO_SYSADMIN;
            }
            else
            {
                return TO_OPERAROR;
            }
        }
        else
        {
            return ERROR;
        }
    }
}
