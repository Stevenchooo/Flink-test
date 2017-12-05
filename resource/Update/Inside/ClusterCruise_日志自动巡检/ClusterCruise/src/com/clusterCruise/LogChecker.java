package com.clusterCruise;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clusterCruise.config.*;

public class LogChecker {
	public static String EXE_ALARM ="alarm.sh";
	public static String DAY_REPORT_LOG="dayreport.log";
	public static String ERROR_RULE_CHECK_LOG="error_not_match.log";
	public static String CFG_FILE="cfg.xml"; 
	
	private static ArrayList<CfgItem1> configItem1List = new ArrayList<CfgItem1>();
	public static String ALARM_SMS_RECEIVER="";
	public static String ALARM_EMAIL_RECEIVER="";
	
	public static void main(String[] args) throws IOException {
		
		// arg[0]=startCheckAlarm
		File fileHome= new File("./");
		String homePath = fileHome.getAbsolutePath() +"/";
		EXE_ALARM =homePath +  EXE_ALARM ;
		DAY_REPORT_LOG= homePath+DAY_REPORT_LOG ;
		CFG_FILE =homePath + CFG_FILE ;
		ERROR_RULE_CHECK_LOG =homePath + ERROR_RULE_CHECK_LOG ;
		 
		//String action = "startCheckFileError"; 
		String action = args[0];
		configItem1List = ConfigureXml.parserXml(CFG_FILE);
		
		if(action.equals("startCheckAlarm"))
		{
			// java -jar ClusterCruise.jar startCheckAlarm
			// 将能告警的规则放进去，去掉startCheckFileError的rnode 全局检查
			System.out.println("begin startCheckAlarm");
			ArrayList<CfgItem2> alarmConfigList = new ArrayList<CfgItem2>();
			for (CfgItem1 item1 : configItem1List) {
				ArrayList<CfgItem2> listItem2 = item1.getListRnode();
				for (CfgItem2 item2 : listItem2) {
					if (item2.getScope() == null || item2.getScope().length() == 0) {
						alarmConfigList.add(item2);
					}
				}

			}
			startCheckAlarm(alarmConfigList);
		}
		else if(action.equals("startCheckFileError"))
		{
			//java -jar ClusterCruise.jar startCheckFileError  源文件路径     路径类型hive
			System.out.println("begin startCheckFileError");
			String checkPath=args[1];
			String fileType=args[2];
			//清空老的检查文件
			FileUtil.WriteOverite(LogChecker.ERROR_RULE_CHECK_LOG, "");

			startCheckFileError(checkPath,fileType);
		}
		else if(action.equals("DayReportThread"))
		{
			//java -jar ClusterCruise.jar  源原文件路径     路径类型hive
			System.out.println("begin DayReportThread");
			String checkPath=args[1];; 
			String fileType=args[2];
			startDayReportThread(checkPath,fileType);
		}
		else 
		{
			System.out.println("Input arg[0] is not right");
			System.exit(1);
		}			
		
		
		
		 
	}
	public static void  startCheckAlarm(ArrayList<CfgItem2> cAlarm) throws IOException
	{
		// 对于每个文件，启动一个线程， 线程最小读取周期是30秒
		for (CfgItem2 i1 : cAlarm) {
			LiveThread t1 = new LiveThread(i1);
			t1.start();
		}

		// 死也不退出
		while (true) {
			try {
				Thread.sleep(60 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public static void  startDayReportThread(String checkPath, String fileType)
	{
		ArrayList<CfgItem2> reportItem2List = new ArrayList<CfgItem2>();
		for (CfgItem1 item1 : configItem1List) 
		{
			if (item1.getName().equals(fileType)) {
				ArrayList<CfgItem2> listItem2 = item1.getListRnode();
				for (CfgItem2 item2 : listItem2) {
					if (item2.getScope() == null || item2.getScope().length() == 0) {
						reportItem2List.add(item2);
					}
				}
			}

		}
		
		//从node中取出文件
		for (CfgItem2 i1 : reportItem2List) 
		{
			try {
				DayReportThread fe = new DayReportThread(i1,checkPath);
				fe.run();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void  startCheckFileError(String checkPath ,String fileType)
	{
		//从node中取出文件
		for (CfgItem1 i1 : configItem1List) 
		{
			if(i1.getName().equals(fileType))
			{
			try {
				FileErrrorCheck fe = new FileErrrorCheck(i1, checkPath);
				fe.run();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
		}
		
	}
	
	public static void test1()
	{
		String s1 = "2017-01-02 13:55:52,460 | ERROR | HiveServer2-Handler-Pool: Thread-3366124 | MetaStoreClient lost connection, stop to connect. | org.apache.hadoop.hive.metastore.RetryingMetaStoreClient.<init>(RetryingMetaStoreClient.java:88)" ;
        String s2="2017-01-02 13:55:52,480 | ERROR | HiveServer2-Handler-Pool: Thread-3371278 | SASL negotiation failure | org.apache.thrift.t";
		Pattern rPattern = Pattern.compile(".*\\| ERROR \\|.*\\| SASL negotiation failure \\|.*");
		Matcher m =rPattern.matcher(s1);
		 
		
		if(m.matches())
		{
			System.out.println("match");
	    }
		else
		{
			System.out.println("no match");
		}
	}
	
	

}
