package com.hive;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import com.hive.FileUtil;

public class Main extends Thread {

	// 打印堆栈，搜出索引，放到stack1.log
	public static String CMD_PRINT_STATCK = "printHiveStack.sh";
	
	
	/* waiting to lock <0x99999> (a java.util)
	 * 
	 *  
	 */
	public static String FILE_STACK="stack.log" ;
	public static String CMD_RESTART="killHiveAndMetastore.sh";
	public static Map<String,String> mapCount = new HashMap<String,String>(); 
	public static String HOME_PATH = "" ;
	public int   ARG2_LOCK_LIMIT= 200 ;
	
	public Main(int iLockLimit)
	{
		
		ARG2_LOCK_LIMIT = iLockLimit ;
	}

	public static void readFileByChars(String fileName) {
		//清空数据
		mapCount.clear();
		try {

			// read file content from file
			StringBuffer sb = new StringBuffer("");
			FileReader reader = new FileReader(fileName);
			BufferedReader br = new BufferedReader(reader);
			String str = null;
			while ((str = br.readLine()) != null) {
				//waiting to lock <0x99999> (a java.util)
				String s1[]=str.split("<");
				if(s1.length>= 2)
				{
					//0x99999> (a java.util)
					String s2[] = s1[1].split(">") ;
					if(s2.length>= 2)
					{
						//0x99999
						String s3 = s2[0] ;
						String old= mapCount.get(s3) ;
						if(old != null)
						{
							mapCount.put(s3, ""+(Integer.parseInt(old) +1)) ;
						}
						else
						{
							mapCount.put(s3,"1") ;
						}
					}
				}
				 
			}

			br.close();
			reader.close();
			 
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				// 1.执行命令, 打印hiveserver 日志
				FileUtil.ExecCmd("sh " + HOME_PATH + "/" + CMD_PRINT_STATCK, true);

				// 2.分析日志
				readFileByChars(HOME_PATH + "/" + FILE_STACK);

				// 3.如果锁数量大, 执行命令
				analysisLock();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try
			{
				Thread.sleep(5*60*1000);
			}
			catch(Exception e)
			{
				
			}

		}

	}
	public void analysisLock()
	{
		String d0=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println("-----------------------" + d0 +"--lock > 10---------------------") ;
		
		for (Map.Entry<String, String> entry : mapCount.entrySet()) {
			String k1 = entry.getKey();
			String v1 = entry.getValue();
			if(Integer.parseInt(v1)> 10) 
			{
				System.out.println("lock=" + k1 +", count=" + v1);
			}
			
			if (Integer.parseInt(v1) > ARG2_LOCK_LIMIT) {
				try {
					String d=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
					  
					 
					System.out.println(d +" :kill lock " + k1 +" > " + ARG2_LOCK_LIMIT);
					
					// 重启mestore和hiveserver
					FileUtil.ExecCmd("sh " +HOME_PATH + "/" + CMD_RESTART, true);
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			}

		}
	}

	public static void main(String[] args) {
		File f1= new File("./");
		try {
			HOME_PATH = f1.getCanonicalPath() ;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("current path = " + HOME_PATH);
		
		Main m =  new Main(Integer.parseInt(args[0]));
		
		//m.readFileByChars(HOME_PATH + "/" + FILE_STACK);
		//m.analysisLock();
		m.start();


		while(true)
		{
			try
			{
				Thread.sleep(30*1000);
			}
			catch(Exception e)
			{
				
			}
			
		}
		
		

	}

}
