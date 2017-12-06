/**
 * LoginAction.java
 */
package com.huawei.platform.tcc.action;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.tcc.PrivilegeControl.Operator;
import com.huawei.platform.tcc.constants.type.NameStoredInSession;
import com.huawei.platform.tcc.domain.UsernameAndPasswordParam;
import com.huawei.platform.tcc.entity.OperatorInfoEntity;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 用户登录
 * 
 * @author z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-4-26]
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
    private String username;
    
    /**
     * 用户密码
     */
    private String password;
    
    /**
     * 错误返回信息
     */
    private String tip;
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getUsername()
    {
        return this.username;
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
     * 用户登录方法
     * 
     * @return 展示页面
     * @throws Exception
     *             异常
     */
    public String userLogin()
        throws Exception
    {
        if (null == this.username || null == this.password || this.username.equals("") || this.password.equals(""))
        {
            this.tip = "请输入用户名密码！";
            return "error";
        }
        UsernameAndPasswordParam param = new UsernameAndPasswordParam();
        param.setUsername(this.username);
        param.setPassword(this.password);
        OperatorInfoEntity operatorInfo = getOperatorMgnt().getOperatorInfo(param);
        if (null == operatorInfo)
        {
            this.tip = "用户名或密码错误！";
            return "error";
        }
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpSession session = request.getSession(true);
        Object clientIp = session.getAttribute(NameStoredInSession.CLIENT_IP);
        if (null == clientIp)
        {
            Map<String, String> mapIp = TccUtil.getRealIp(request);
            session.setAttribute(NameStoredInSession.CLIENT_IP, mapIp.get(NameStoredInSession.CLIENT_IP));
            session.setAttribute(NameStoredInSession.IP_HEADER_NAME, mapIp.get(NameStoredInSession.IP_HEADER_NAME));
            LOGGER.info("log clientIp[clientIp={},ipHeaderName={}] to session!",
                mapIp.get(NameStoredInSession.CLIENT_IP),
                mapIp.get(NameStoredInSession.IP_HEADER_NAME));
        }
        session.setAttribute(NameStoredInSession.USER_NAME, this.username);
        LOGGER.info("log user[userName={}] to session!", session.getAttribute(NameStoredInSession.USER_NAME));
        
        Operator operator = getOperator();
        
        if (null != operator)
        {
            if (operator.isSystemAdmin())
            {
                session.setAttribute(NameStoredInSession.IS_SYSADMIN, true);
                return TO_SYSADMIN;
            }
            else
            {
                session.setAttribute(NameStoredInSession.IS_SYSADMIN, false);
                return TO_OPERAROR;
            }
        }
        else
        {
            return ERROR;
        }
    }
    
    /**
     * 用户登出，清除会话
     */
    public void logout()
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpSession session = request.getSession(true);
        session.invalidate();
    }
    
    /**
     * 从会话中取得用户名
     * @return 查询成功标志位
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    public String getUserNameFromSession()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try
        {
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            HttpSession session = request.getSession(true);
            String userName = (String)session.getAttribute("userName");
            if (null != userName)
            {
                //记录日志
                LOGGER.info("getUserNameFromSession[{}] success.", userName);
                jsonObject.put("success", true);
                jsonObject.put("userName", userName);
            }
            else
            {
                //记录日志
                LOGGER.info("getUserNameFromSession fail due to userName is null");
                jsonObject.put("success", false);
            }
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            out.print(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")));
            return null;
        }
        catch (Exception e)
        {
            LOGGER.error("getUserNameFromSession fail", e);
            throw e;
        }
    }
}
