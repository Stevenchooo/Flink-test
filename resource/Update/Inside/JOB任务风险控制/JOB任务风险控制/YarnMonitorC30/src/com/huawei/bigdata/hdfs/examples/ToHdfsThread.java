package com.huawei.bigdata.hdfs.examples;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *按照文件的时间，放入到对应的pt_d分区表中
 * @author c00239107
 *
 */
public class ToHdfsThread extends Thread {
	static String CMD_MOVE_HDFS = "tohdfs.sh";
	public static SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
	String hdfsHome ;
	public ToHdfsThread(String hdfsHome1)
	{
		hdfsHome = hdfsHome1 ;
		
	}
	
	public void run()
 {
		while (true) {
			try {
				File f = new File(YarnMonitor.HOME_PATH + "/data");
				File[] fs = f.listFiles();

				String result = "cd " + YarnMonitor.HOME_PATH + "/data"+"\n";
				for (File f1 : fs) {
					long l1 = f1.lastModified();
					String pt_d = "pt_d=" + df2.format(new Date(l1));
					// 20分钟前的文件移入hdfs
					if (System.currentTimeMillis() - l1 > 20 * 60 * 1000) {

						result += "hdfs dfs -put " + f1.getName() + " " + hdfsHome + "/" + pt_d + "\n";
						result += "rm " + f1.getName() + "\n";
					}

				}
				FileUtil.WriteOverite(YarnMonitor.HOME_PATH + "/" + CMD_MOVE_HDFS, result);

				 FileUtil.ExecCmd(YarnMonitor.HOME_PATH+"/" + CMD_MOVE_HDFS,false);
			} catch (Exception e) {

			}
			
			try {
				sleep(20 * 60 * 1000);
			} catch (Exception e) {

			}
		}

	}

}
