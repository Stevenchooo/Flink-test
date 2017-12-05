package com.clusterCruise.config;

import java.util.*;

public class CfgItem1 {
	ArrayList<CfgItem2> listRnode = new  ArrayList<CfgItem2>();
	String name ;
	
	CfgItem1(String name1)
	{
		name = name1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<CfgItem2> getListRnode() {
		return listRnode;
	}

	public void addListRnode( CfgItem2 rnode) {
		rnode.setParent(this);
		listRnode.add(rnode);
	}
	

}
