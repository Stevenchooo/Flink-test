package com.huawei.manager.base.config.process;

/**
 * 
 * <一句话功能简述> <功能详细描述>
 * 
 * @author w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see [相关类/方法]
 */
public interface IAuthConfig
{
    /**
     * ScopePara
     */
    public static final String PARAMETER_SCOPE_PARA = "scopePara";

    /**
     * ScopeHonor
     */
    public static final String PARAMETER_SCOPE_HONOR = "scopeHonor";

    /**
     * 用于判断范围在当前范围内是否有权限， 上层管理员可以管理下层数据
     * 
     * @return String
     */
    public String getScopePara();

    /**
     * 用于判断范围在当前范围内是否有权限， 上层管理员可以管理下层数据
     * 
     * @return String
     */
    public String getScopeHonor();
}
