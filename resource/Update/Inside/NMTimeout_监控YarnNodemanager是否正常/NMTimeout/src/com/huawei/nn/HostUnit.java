package com.huawei.nn;

import java.util.*;

public class HostUnit {
	private int count;
	private ArrayList<TimeoutUnit> list = new ArrayList<TimeoutUnit>();
	public void addTimeoutUnit(TimeoutUnit tu)
	{
		boolean bShouldAdd = true;
		for(TimeoutUnit t :list)
		{
			if(t.getsAttempId().equals(tu.getsAttempId()))
			{
				//已经加入过了，不要再加进来
				bShouldAdd = false ;
				break;
					
			}
			
		}
		
		if(bShouldAdd)
		{
			list.add(tu);
		}
		
	}
	
	public int getCount()
	{
		return  list.size();
	}
	public String toString()
	{
		String result= "" ;
		for(TimeoutUnit t :list)
		{
			result += t.getsHostname() +"," + t.getsAttempId() +"," + t.getEndTime() +"\r\n";
		
		}
		
		return result ;
	}

}
