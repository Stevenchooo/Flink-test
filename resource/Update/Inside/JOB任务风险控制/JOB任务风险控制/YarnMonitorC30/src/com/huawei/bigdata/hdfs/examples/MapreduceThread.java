package com.huawei.bigdata.hdfs.examples;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapreduceThread extends Thread{
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmm");
 
	public static String CMD_JOB = "mkjob.sh";
	public static String DATA_JOB = "mapred_result.txt";

	ConcurrentHashMap<String, JobItem> map;

	MapreduceThread(ConcurrentHashMap mapList) {
		map = mapList;
	}

	public void run() {
		while (true) {

			// 得到运行列表xml
			try {
				FileUtil.ExecCmd(YarnMonitor.HOME_PATH + "/" + CMD_JOB, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/**
			 
			 */
			// 分解mapjob
			try {
				System.out.println("begin anaJob "  + System.currentTimeMillis());
				anaJob();
				
				System.out.println("begin add4Column "  + System.currentTimeMillis());
				add4Column();
				
				System.out.println("begin OutputJob "  + System.currentTimeMillis());
				//按小时 输出 |
				OutputJob();
				
				
				System.out.println("begin CleanJob "  + System.currentTimeMillis());
				//清理不在RUNNING/ACCEPTED的job
				CleanJob();
				System.out.println("end CleanJob "  + System.currentTimeMillis());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			try
			{
				sleep(60*1000);
			}
			catch(Exception e)
			{
				
			}
		}
	}
	public void CleanJob()
	{
		
		for(Map.Entry<String, JobItem> e: map.entrySet())
		{
			JobItem item= e.getValue();
			String state = item.getState() ;
			if(state.equals("RUNNING") || state.equals("ACCEPTED"))
			{
				
			}
			else
			{
				//
				if("".equals(item.getHDFS_READ_OPS()))
				{
					//读写次数还为空，暂时不要退出。
					
				}
				else
				{
					map.remove(item.getJobId()) ;
				}
			}
			
		}
		
	}
	public void OutputJob()
	{
		String t1=df.format(new Date()) ;
		String lines="" ;
		for(Map.Entry<String, JobItem> e: map.entrySet())
		{
			JobItem item= e.getValue();
			lines += t1+"|"+item.getOutput("|") +"\r\n";
		}
		
		//分钟最后一位去掉，yyyymmddhhmm。每10分钟1个新文件
		String fileName= df2.format(new Date()) ;
		fileName =  fileName.substring(0, fileName.length()-1) +"0.txt";
		FileUtil.WriteAppend(YarnMonitor.HOME_PATH+"/data/"+fileName , lines);
		
		
		
	}
	
	public void add4Column()
	{
		
		for(Map.Entry<String, JobItem> e: map.entrySet())
		{
			JobItem item= e.getValue();
		 
			 
			//sql可能为空， map不可能为空
			if(item.getExtTotalMap().length()==0)
			{
				//以前没拿到过sql,拼装 /tmp/hadoop-yarn/staging/用户名/.staging/jobid/job.xml
				//String sqlPath = "/tmp/hadoop-yarn/staging/" + item.getUserName()+"/.staging/"+item.getJobId()+"/"+item.getJobId()+"_1_conf.xml" ;
				String sqlPath = "/tmp/hadoop-yarn/staging/" + item.getUserName()+"/.staging/"+item.getJobId()+"/job.xml" ;
				String fileContent ="" ;
				try {
					  fileContent = YarnMonitor.ReadFile(sqlPath);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				String sql=YarnMonitor.findByFlag(fileContent, "hive.query.string</name><value>", "</value>");
				item.setExtSQL(sql);
				
				//map大小
				String m=YarnMonitor.findByFlag(fileContent, "mapreduce.job.maps</name><value>", "</value>");
				item.setExtTotalMap(m);
				
				//reduce大小
				
				String r=YarnMonitor.findByFlag(fileContent, "mapreduce.job.reduces</name><value>", "</value>");
				item.setMAPREDUCE_JOB_REDUCES(r);
				
				//每个map占的vcore
				String mvcore= YarnMonitor.findByFlag(fileContent,"mapreduce.map.cpu.vcores</name><value>", "</value>");
				item.setMAPREDUCE_MAP_CPU_VCORES(mvcore);
				
				//每个reduce占的vcore
				String rvcore= YarnMonitor.findByFlag(fileContent,"mapreduce.reduce.cpu.vcores</name><value>", "</value>");
				item.setMAPREDUCE_REDUCE_CPU_VCORES(mvcore);
			}
			
			 
			
		}
	}

	public void setJobOver()
	{
		for(Map.Entry<String, JobItem> e: map.entrySet())
		{
			JobItem item= e.getValue();
			item.setState("FINISH/FAIL/KILL");
			item.setZero();
			
		}
		
		
	}
	public void anaJob() throws Exception {
		
		//只能看到在运行的任务，其它状态的任务看不到。
		setJobOver();
		
		File file = new File(YarnMonitor.HOME_PATH + "/" + DATA_JOB);
		
		//File file = new File("d:\\joblist.txt");
		if (file.isFile() && file.exists()) { // 判断文件是否存在
			InputStreamReader read = new InputStreamReader(new FileInputStream(file));// 考虑到编码格式
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String[] s1 = lineTxt.split("\t");
				if (s1.length > 3 && s1[0].indexOf("JobId") < 0) {
					// 去掉第1行和第2行
					JobItem newItem = new JobItem(lineTxt);
					JobItem oldItem = map.get(newItem.getJobId());
					if(oldItem == null) 
					{
						//新来的，直接放进去
						map.put(newItem.getJobId(), newItem);
					}
					else
					{
						//更新部分数据，不是全部
						oldItem.updateJobState(newItem);
					}
					

				}
			}

		}

	}
	
	 public static void main(String[] args)  {
	
		 ConcurrentHashMap mapList = new ConcurrentHashMap();
		 MapreduceThread m1= new MapreduceThread(  mapList) ;
		 try {
			m1.anaJob();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }
	 
	 
}
