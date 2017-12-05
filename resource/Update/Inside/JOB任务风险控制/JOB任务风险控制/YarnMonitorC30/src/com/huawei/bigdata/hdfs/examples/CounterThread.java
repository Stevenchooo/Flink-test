package com.huawei.bigdata.hdfs.examples;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CounterThread extends Thread{
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmm");
 
	public static String CMD_COUNT = "countjob.sh";
	public static String DATA_COUNT = "count_result.txt";

	ConcurrentHashMap<String, JobItem> map;

	CounterThread(ConcurrentHashMap mapList) {
		map = mapList;
	}

	public void run() {
		while (true) {
			
			
			for(Map.Entry<String, JobItem> e: map.entrySet())
			{
				JobItem item= e.getValue();
				String jobid = item.getJobId() ;
				
				//运行得到结果，只有一行
				try {
					FileUtil.WriteOverite(YarnMonitor.HOME_PATH + "/" + CMD_COUNT, "mapred job2 -status " + jobid + " > " + YarnMonitor.HOME_PATH + "/" + DATA_COUNT);
					FileUtil.ExecCmd(YarnMonitor.HOME_PATH + "/" + CMD_COUNT, false);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
				//读出一行记录，更新
				String data=FileUtil.Read1stLine(YarnMonitor.HOME_PATH + "/" + DATA_COUNT);
				//System.out.println(System.currentTimeMillis() + "count  jobid " +jobid +"  " + data);
				item.setCounter(data);
			}

			 			
			try
			{
				//平均每4秒执行1个命令，不能休息久了
				sleep(1*1000);
			}
			catch(Exception e)
			{
				
			}
		}
	}  
	 
	 
	 
}
