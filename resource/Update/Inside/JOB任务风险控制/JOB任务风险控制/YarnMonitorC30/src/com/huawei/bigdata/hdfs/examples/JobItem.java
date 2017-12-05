package com.huawei.bigdata.hdfs.examples;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JobItem {
	String JobId;
	String State;	     
	String StartTime;	    
	String UserName	  ;     
	String Queue	 ; 
	String Priority	 ;
	String UsedContainers	; 
	String RsvdContainers	; 
	String UsedMem	 ;
	String RsvdMem	 ;
	String NeededMem	  ; 
	String AMinfo;
	
	String extTotalMap;
	String extSQL ;
 
	
	String countStatus;
	String FILE_BYTES_READ ;     
	String FILE_BYTES_WRITTEN   ;
	String FILE_READ_OPS     ;   
	String FILE_LARGE_READ_OPS  ;
	String FILE_WRITE_OPS      ; 
	String HDFS_BYTES_READ    ;  
	String HDFS_BYTES_WRITTEN ;  
	String HDFS_READ_OPS      ;  
	
	String HDFS_LARGE_READ_OPS;  
	String HDFS_WRITE_OPS;
	
	String MAPREDUCE_JOB_REDUCES;
	
	String MAPREDUCE_MAP_CPU_VCORES;
	String MAPREDUCE_REDUCE_CPU_VCORES;
	
	public String getHDFS_READ_OPS() {
		return HDFS_READ_OPS;
	}
	
	public String getMAPREDUCE_JOB_REDUCES() {
		return MAPREDUCE_JOB_REDUCES;
	}
	public void setMAPREDUCE_JOB_REDUCES(String mAPREDUCE_JOB_REDUCES) {
		MAPREDUCE_JOB_REDUCES = mAPREDUCE_JOB_REDUCES;
	}
	public String getMAPREDUCE_MAP_CPU_VCORES() {
		return MAPREDUCE_MAP_CPU_VCORES;
	}
	public void setMAPREDUCE_MAP_CPU_VCORES(String mAPREDUCE_MAP_CPU_VCORES) {
		MAPREDUCE_MAP_CPU_VCORES = mAPREDUCE_MAP_CPU_VCORES;
	}
	public String getMAPREDUCE_REDUCE_CPU_VCORES() {
		return MAPREDUCE_REDUCE_CPU_VCORES;
	}
	public void setMAPREDUCE_REDUCE_CPU_VCORES(String mAPREDUCE_REDUCE_CPU_VCORES) {
		MAPREDUCE_REDUCE_CPU_VCORES = mAPREDUCE_REDUCE_CPU_VCORES;
	}
	 
	public void setCounter(String data )
	{
		//jobid|status|****
		if(data!=null)
		{
			String[] fs=data.split("\t");
			if(fs.length>= 12)
			{
			countStatus = fs[1];
			FILE_BYTES_READ =fs[2] ;     
			FILE_BYTES_WRITTEN=fs[3]   ;
			FILE_READ_OPS  =fs[4]   ;   
			FILE_LARGE_READ_OPS =fs[5] ;
			FILE_WRITE_OPS    =fs[6]  ; 
			HDFS_BYTES_READ  =fs[7]  ;  
			HDFS_BYTES_WRITTEN =fs[8];  
			HDFS_READ_OPS    =fs[9]  ;  
			HDFS_LARGE_READ_OPS=fs[10];  
			HDFS_WRITE_OPS=fs[11];
			}
		}
		
	}
	public String getOutput(String fieldFlag)
	{
		String s =  JobId+fieldFlag+
		 State+fieldFlag+	     
		 StartTime+fieldFlag+	    
		 UserName	  +fieldFlag+     
		 Queue	 +fieldFlag+ 
		 Priority	 +fieldFlag+
		 UsedContainers	+fieldFlag+ 
		 RsvdContainers	+fieldFlag+ 
		 UsedMem	 +fieldFlag+
		 RsvdMem	 +fieldFlag+
		 NeededMem	  +fieldFlag+ 
		 AMinfo+fieldFlag+
		
		 extTotalMap+fieldFlag+
		 extSQL +fieldFlag+
		//data from mapred job2 -status
		countStatus+fieldFlag+
		FILE_BYTES_READ +fieldFlag+     
		FILE_BYTES_WRITTEN   +fieldFlag+
		FILE_READ_OPS     +fieldFlag+   
		FILE_LARGE_READ_OPS  +fieldFlag+
		FILE_WRITE_OPS      +fieldFlag+ 
		HDFS_BYTES_READ    +fieldFlag+  
		HDFS_BYTES_WRITTEN +fieldFlag+  
		HDFS_READ_OPS      +fieldFlag+  
		HDFS_LARGE_READ_OPS+fieldFlag+  
		HDFS_WRITE_OPS+fieldFlag+
		MAPREDUCE_JOB_REDUCES+fieldFlag+
		MAPREDUCE_MAP_CPU_VCORES+fieldFlag+
		MAPREDUCE_REDUCE_CPU_VCORES 
		;
		
		
		
		return s ;
	}
	public void updateJobState(JobItem newItem)
	{
		State= newItem.getState();
		UsedContainers= newItem.getUsedContainers();
		RsvdContainers = newItem.getRsvdContainers();
		UsedMem = newItem.getUsedMem();
		RsvdMem = newItem.getRsvdMem();
		NeededMem = newItem.getNeededMem();
		AMinfo = newItem.getAMinfo();
		
	}
	public String removeNA(String s)
	{
		if(s.indexOf("N/A") >=0)
		{
			//job处于prepare阶段，
			return "";
		}
		else
		{
			return s ;
		}
		
	}
	public JobItem(String line)
 {
		String s[] = line.split("\t");
		JobId = s[0].trim();
		State = s[1].trim();
		String date1 = s[2].trim();
		Date dateTime = new Date(Long.parseLong(date1));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StartTime = df.format(dateTime);
		UserName = s[3].trim();
		Queue = s[4].trim();
		Priority = s[5].trim();
		UsedContainers = removeNA(s[6].trim());
		RsvdContainers = removeNA(s[7].trim());
		UsedMem = removeNA(s[8].trim().replace("M", ""));
		RsvdMem = removeNA(s[9].trim().replace("M", ""));
		 
		NeededMem = removeNA(s[10].trim().replace("M", ""));
		 
		AMinfo = s[11].trim();
		extSQL = "";
		extTotalMap = "";
		
		countStatus = "";
		FILE_BYTES_READ = "";
		FILE_BYTES_WRITTEN = "";
		FILE_READ_OPS = "";
		FILE_LARGE_READ_OPS = "";
		FILE_WRITE_OPS = "";
		HDFS_BYTES_READ = "";
		HDFS_BYTES_WRITTEN = "";
		HDFS_READ_OPS = "";
		HDFS_LARGE_READ_OPS = "";
		HDFS_WRITE_OPS = "";

	}
	public void setZero()
	{
		UsedContainers="0" ;
		RsvdContainers="0";
		UsedMem="0" ;
		RsvdMem="0" ;
		NeededMem="0" ;
		
	}
	public String getJobId() {
		return JobId;
	}
	public void setJobId(String jobId) {
		JobId = jobId;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getQueue() {
		return Queue;
	}
	public void setQueue(String queue) {
		Queue = queue;
	}
	public String getPriority() {
		return Priority;
	}
	public void setPriority(String priority) {
		Priority = priority;
	}
	public String getUsedContainers() {
		return UsedContainers;
	}
	public void setUsedContainers(String usedContainers) {
		UsedContainers = usedContainers;
	}
	public String getRsvdContainers() {
		return RsvdContainers;
	}
	public void setRsvdContainers(String rsvdContainers) {
		RsvdContainers = rsvdContainers;
	}
	public String getUsedMem() {
		return UsedMem;
	}
	public void setUsedMem(String usedMem) {
		UsedMem = usedMem;
	}
	public String getRsvdMem() {
		return RsvdMem;
	}
	public void setRsvdMem(String rsvdMem) {
		RsvdMem = rsvdMem;
	}
	public String getNeededMem() {
		return NeededMem;
	}
	public void setNeededMem(String neededMem) {
		NeededMem = neededMem;
	}
	public String getAMinfo() {
		return AMinfo;
	}
	public void setAMinfo(String aMinfo) {
		AMinfo = aMinfo;
	}
	public String getExtSQL() {
		return extSQL;
	}
	public void setExtSQL(String extSQL1) {
		if(extSQL1 == null)
		{
			extSQL1="";
		}
		// | is output split flag
		extSQL1 =extSQL1.replace("|", "");
		// 将2个空格变成1个，多输出一点sql；
		extSQL1 = extSQL1.replace("  ", " ");
		extSQL1 = extSQL1.trim();
		//sql太长就截断
		if(extSQL1.length()> 500) 
		{
			extSQL1 = extSQL1.substring(0,500)+"...";
		}
		
		this.extSQL = extSQL1.trim();
	}
	
	public int getExtTotalMapInt() {
		int i =0 ;
		try
		{
			i= Integer.parseInt(extTotalMap);
		}
		catch(Exception e)
		{
			
		}
		return i;
	}
	
	public String getExtTotalMap() {
		return extTotalMap;
	}
	public void setExtTotalMap(String extTotalMap) {
		this.extTotalMap = extTotalMap;
	}


}
