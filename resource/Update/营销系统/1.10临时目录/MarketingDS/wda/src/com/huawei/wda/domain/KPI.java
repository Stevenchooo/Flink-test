package com.huawei.wda.domain;

/**
 * KPI.
 */
public class KPI
{
    /**
     * key.
     */
    private String key;

    /**
     * value.
     */
    private String value;

    /**
     * checked.
     */
    private boolean checked;

    /**
     * 获取key.
     * 
     * @return String
     */
    public String getKey()
    {
        return key;
    }

    /**
     * 设置key.
     * 
     * @param key
     *            key
     */
    public void setKey(String key)
    {
        this.key = key;
    }

    /**
     * 获取value.
     * 
     * @return String
     */
    public String getValue()
    {
        return value;
    }

    /**
     * 设置value.
     * 
     * @param value
     *            value
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * 是否checked.
     * 
     * @return boolean
     */
    public boolean isChecked()
    {
        return checked;
    }

    /**
     * 设置checked.
     * 
     * @param checked
     *            checked
     */
    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

}
