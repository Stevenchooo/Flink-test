package com.huawei.ide.services.algorithm.entity;

import java.util.Arrays;
import java.io.Serializable;
/**
 * 
 * data
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年6月27日]
 * @see  [相关类/方法]
 */
public class Data  implements  Serializable
{
	private static final long serialVersionUID = 5591461352992337784L;
    /*
     * cards 卡片列表
     */
    private Cards[] cards;
    
    public Cards[] getCards()
    {
        return cards;
    }
    
    public void setCards(Cards[] cards)
    {
        this.cards = cards;
    }
    
    /**
     * hashCode
     * @return  int
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(cards);
        return result;
    }
    
    /**
     * equals
     * @param obj
     *        obj
     * @return  boolean
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Data other = (Data)obj;
        if (!Arrays.equals(cards, other.cards))
        {
            return false;
        }
        return true;
    }
    
    /**
     * toString
     * @return  String
     */
    @Override
    public String toString()
    {
        return "data [cards=" + Arrays.toString(cards) + "]";
    }
    
}
