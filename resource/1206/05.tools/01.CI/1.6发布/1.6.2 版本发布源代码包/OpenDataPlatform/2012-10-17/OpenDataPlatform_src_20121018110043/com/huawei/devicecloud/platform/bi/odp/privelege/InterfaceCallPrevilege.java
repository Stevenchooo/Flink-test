/*
 * 文 件 名:  InterfaceCallPrevilege.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  接口调用权限
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-6
 */
package com.huawei.devicecloud.platform.bi.odp.privelege;

import org.apache.commons.lang.StringUtils;

import com.huawei.devicecloud.platform.bi.odp.constants.type.RolePreDef;

/**
 * 接口调用权限
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-6]
 */
public class InterfaceCallPrevilege
{
    /**
     * 权限字段的长度
     */
    public static final int PREVILEGE_LENGTH = 6;
    
    /**
     * 1字节中有效的bit位数
     */
    public static final int VAILD_BIT_NUM = 8;
    
    /**
     * 接口数
     */
    public static final int INTERFACES = VAILD_BIT_NUM * PREVILEGE_LENGTH;
    
    /**
     * push对接权限
     */
    private static final int PUSH_PREVILEGE = 0xFF;
    
    /**
     * 16进制基数
     */
    private static final int HEX_BASE = 16;
    
    /**
     * 2个16进制字符表示一个char
     */
    private static final int HEX_LENGTH = 2;
    
    /**
     * HIAD对接权限
     */
    private static final int HIAD_PREVILEGE = 0xFF;
    
    /**
     * 默认权限
     */
    private static final int DEFAULT_PREVILEGE = 0x00;
    
    /**
     * 默认的byte位掩码
     */
    private static final int DEFAULT_MASK = 0x80;
    
    /**
     * 通过roleId获取接口调用权限值
     * @param roleId 角色Id
     * @return 权限值
     */
    public char[] getInterfaceCallPrevilege(int roleId)
    {
        StringBuilder preSb = new StringBuilder();
        for (int i = 0; i < PREVILEGE_LENGTH; i++)
        {
            preSb.append(String.format("%02x", DEFAULT_PREVILEGE));
        }
        
        //权限字符串长度
        char[] previleges = new char[preSb.length()];
        preSb.getChars(0, preSb.length(), previleges, 0);
        if (RolePreDef.PUSH == roleId)
        {
            previleges[0] = String.format("%02x", PUSH_PREVILEGE).charAt(0);
            previleges[1] = String.format("%02x", PUSH_PREVILEGE).charAt(1);
        }
        else if (RolePreDef.HIAD == roleId)
        {
            previleges[0] = String.format("%02x", HIAD_PREVILEGE).charAt(0);
            previleges[1] = String.format("%02x", HIAD_PREVILEGE).charAt(1);
        }
        
        return previleges;
    }
    
    /**
     * 是否有权限访问指定接口
     * @param token token
     * @param interfaceId 接口Id号
     * @return 是否有权限
     */
    public boolean canCall(String token, int interfaceId)
    {
        //接口号限制
        if (interfaceId <= 0 || interfaceId > INTERFACES)
        {
            return false;
        }
        
        if (!StringUtils.isEmpty(token))
        {
            //接口权限存放的位置
            int charIndex = (interfaceId - 1) / VAILD_BIT_NUM;
            //每连个16进制字符表示一个256的字符值
            charIndex += charIndex;
            if (charIndex < PREVILEGE_LENGTH + PREVILEGE_LENGTH)
            {
                int preCharValue = Integer.parseInt(token.substring(charIndex, charIndex + HEX_LENGTH), HEX_BASE);
                
                int mask = DEFAULT_MASK;
                mask = mask >>> ((interfaceId - 1) % VAILD_BIT_NUM);
                
                //如果相应的位为1
                return (preCharValue & mask) != 0;
            }
        }
        
        //默认无权限
        return false;
    }
}
