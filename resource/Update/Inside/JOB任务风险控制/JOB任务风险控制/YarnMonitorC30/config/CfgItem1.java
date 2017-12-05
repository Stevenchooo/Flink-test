package com.hdfs.config;

import java.util.*;

public class CfgItem1 {
	 
	String name ;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	int depth;
	
	CfgItem1(String name1,String depth1)
	{
		name = name1;
		depth = Integer.parseInt( depth1) ;
	}

	 
	

}
