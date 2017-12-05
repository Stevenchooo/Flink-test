package com.huawei.bigdata.hdfs.examples;

public class RuleQueueItem {
	//private String 
	String queue;
	long indexLimit ;
	public RuleQueueItem(String queue1, String mapLimit1)
	{
		queue= queue1 ;
		indexLimit=Long.parseLong(mapLimit1) ;
	}
	public String getQueue() {
		return queue;
	}
	public void setQueue(String queue) {
		this.queue = queue;
	}
	public long getIndexLimit() {
		return indexLimit;
	}
	public void setMapLimit(int mapLimit) {
		this.indexLimit = mapLimit;
	}
	

}
