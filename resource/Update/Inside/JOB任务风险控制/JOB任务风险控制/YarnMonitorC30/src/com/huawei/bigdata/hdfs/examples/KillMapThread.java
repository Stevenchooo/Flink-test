package com.huawei.bigdata.hdfs.examples;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KillMapThread extends Thread {
	ConcurrentHashMap<String, JobItem> mapJob;
	 
	public static String CMD_KILL = "killjob.sh";
	// public static String DATA_KILL = "killjob.sh";

	public KillMapThread(ConcurrentHashMap mapJob1 ) {
		mapJob = mapJob1;
		 
	}
	
	public boolean judgeMapRule(String queue,long currentData, ConcurrentHashMap<String, RuleQueueItem> mapRule)
	{
		RuleQueueItem ruleItem=mapRule.get(queue);
		if(ruleItem !=null)
		{
			if (currentData >=ruleItem.getIndexLimit()) {
				return true;
			}
			
		}
		
		return false;
	}
	
	public boolean shouldKillJob(Map.Entry<String, JobItem> e)
	{

		JobItem item = e.getValue();

		// 执行时间超过8个小时的任务,杀，无参数配置
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lJobStartTime;
		try {
			lJobStartTime = df.parse(item.getStartTime()).getTime();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			lJobStartTime = System.currentTimeMillis();
		}
		long executeTimeHour = ((System.currentTimeMillis() - lJobStartTime) / 1000) / 3600;
		if (executeTimeHour >= 8) {
			System.out.println(e.getKey()+" is kill because execute hour is > 8" );
			return true;
		}

		// map大的杀
		if (judgeMapRule(item.getQueue(), item.getExtTotalMapInt(), YarnMonitor.mapRuleMapQueue)) {
			System.out.println(e.getKey()+" is kill because map "+item.getExtTotalMapInt()+" > limit in " + item.getQueue() );
			return true;
		}

		// hdfs read超限制，杀
		long hdfsByteRead = 0;
		try {
			hdfsByteRead = Long.parseLong(item.HDFS_BYTES_READ);
		} catch (Exception e2) {
		}

		if (judgeMapRule(item.getQueue(), hdfsByteRead, YarnMonitor.mapRuleHdfsRead)) {
			System.out.println(e.getKey()+" is kill because hdfsByteRead "+hdfsByteRead+" > limit in " + item.getQueue() );
			return true;
		}

		// hdfs write超限制，杀
		long hdfsByteWrite = 0;
		try {
			hdfsByteWrite = Long.parseLong(item.HDFS_BYTES_WRITTEN);
		} catch (Exception e2) {
		}

		if (judgeMapRule(item.getQueue(), hdfsByteWrite, YarnMonitor.mapRuleHdfsWrite)) {
			System.out.println(e.getKey()+" is kill because hdfsByteRead "+hdfsByteWrite+" > limit in " + item.getQueue() );
			return true;
		}

		return false;

	}

	public void run() {
		while (true) {
			try {
				String killSQL = "";
				for (Map.Entry<String, JobItem> e : mapJob.entrySet()) {
					JobItem item = e.getValue();
					
					if(shouldKillJob(e))
					{
						killSQL += "mapred job -kill  " + item.getJobId() + "\n";
					}
				
				}
				FileUtil.WriteOverite(YarnMonitor.HOME_PATH+"/" + CMD_KILL, killSQL);
				FileUtil.ExecCmd(YarnMonitor.HOME_PATH+"/" + CMD_KILL, false);
			} catch (Exception e) {
				e.printStackTrace();

			}

			try {
				sleep(60 * 1000);

			} catch (Exception e) {

			}
		}
	}
    private static boolean isNumeric(String str) { 
    	
	    Pattern pattern = Pattern.compile("^-?[0-9]+"); 
	    Matcher isNum = pattern.matcher(str);
	    
	    if(!isNum.matches()) {
	    	return false; 
	    } 
	    
	   return true; 
    }

}
