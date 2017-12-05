package com.clusterCruise.config;

import java.util.*;

public class CfgItem2 {
	String path ;
	String scope ;
	CfgItem1 parent;
	
	 
	public CfgItem1 getParent() {
		return parent;
	}
	public void setParent(CfgItem1 parent) {
		this.parent = parent;
	}
	public String getScope() {
		return scope;
	}
	public String getPath() {
		return path;
	}
	List<CfgItem3Rule> listCfgItemRule = new ArrayList<CfgItem3Rule>();

	CfgItem2(String path1, String scope1)
	{
		path = path1 ;
		scope = scope1;
	}
	public void addRule(CfgItem3Rule i)
	{
		i.setParent(this);
		listCfgItemRule.add(i);
	}
	public List<CfgItem3Rule> getListRule()
	{
		return listCfgItemRule ;
	 
	}
	public void initRuleStart()
	{
		long lStartTime =  System.currentTimeMillis();
		for(CfgItem3Rule i :  listCfgItemRule)
		{
			i.setlStartTime(lStartTime);
			i.setiOccurCount(0);
			i.setlCheckTime(lStartTime);
		}
	}
	
	
}
