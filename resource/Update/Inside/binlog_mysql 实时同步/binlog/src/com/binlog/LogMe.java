package com.binlog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogMe {
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void debug(String info)
	{
		if(BinlogMain.b_debug)
		{
			System.out.println(df.format(new Date()) +":"+info);
		}
	}
	 

}
