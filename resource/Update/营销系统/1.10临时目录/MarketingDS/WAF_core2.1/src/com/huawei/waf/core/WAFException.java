package com.huawei.waf.core;

/**
 * waf异常
 * @author  l00152046
 * @version  [VMALL OMS V100R001C01, 2014年3月18日]
 * @since  [VMALL OMS]
 */
public class WAFException extends Exception
{
    private static final long serialVersionUID = 0xeee777ee1L;

    public WAFException(String msg)
    {
        super(msg);
    }
}
