package com.clusterCruise;

  
import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clusterCruise.config.*;

public class DayReportThread extends Thread {

	
	long lLastPos = 0 ;
	CfgItem2 cfg  ;
	String pathName; 
	
 
	
	 
	public DayReportThread(CfgItem2 item, String srcPathName) throws IOException
	{
		cfg = item;
		pathName =  srcPathName;
		
		//初始换规则检查时间和次数
		item.initRuleStart();
		
		
	}
	public void run()
	{
		try
		{
			//这个目录下的文件全都统计一遍
			File file = new File(pathName);
			for(File f: file.listFiles())
			{
				lLastPos = 0 ;
				if(f.isFile())
				{
					boolean bReadOver= false ;
					System.out.println("DayReportThread read " + f.getName());
					//---------------------------
					while(!bReadOver)
					{
						int iBufferSize = 10240000;

						byte[] buff = new byte[iBufferSize];
						RandomAccessFile rf = null;
						int realPos = 0;
						try {
							// 读完就关闭,避免影响业务转存日志
							rf = new RandomAccessFile(f, "r");
							rf.seek(lLastPos);
							realPos = rf.read(buff);
							rf.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

						// 是否读满4k
						if (realPos >0) {
							// 还有数据要处理
							bReadOver = false;
						} else {
							bReadOver = true;
						}

						String content = "";
						// 索引到最后一个换行符

						int linePos;
						for (linePos = realPos - 1; linePos >= 0; linePos--) {
							if (buff[linePos] == 10) {
								break;
							}
						}

						if (linePos > 0) {
							// 记录中含换行
							lLastPos = lLastPos + linePos;
							content = new String(buff, 0, linePos);
						} else {
							// 没有换行，最后一条记录了
							content = new String(buff);
							bReadOver= true;

						}
						System.out.println(System.currentTimeMillis() + " read length= "+ content.length() +", lLastPos =" + lLastPos );

						BufferedReader br = new BufferedReader(new InputStreamReader(
								new ByteArrayInputStream(content.getBytes(Charset.forName("utf8"))),
								Charset.forName("utf8")));
						String line2;
						try {
							while ((line2 = br.readLine()) != null) {
								 
								applyRule(line2);
							 
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						br.close();
					}
					
				}
			}
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//输出统计结果
		outputLog();
		
			
		 
	}
	public void outputLog()
	{
		for(CfgItem3Rule i :  cfg.getListRule())
		{
			FileUtil.WriteAppendLine(LogChecker.DAY_REPORT_LOG, i.getiOccurCount() +  "|" +i.isShould_send_alarm()+"|"+i.getR() );
			  
		}
	}
	
	/**
	 * 统计每个规则对应的数据， 然后告警
	 * @param line
	 */
	public void applyRule(String line)
	{
		 
		for(CfgItem3Rule i :  cfg.getListRule())
		{
			if(i.applyRule(line))
			{
				//匹配到了，就返回。
				return;
			}
			  
		}
		
	}
	
	/**
	 * 更新每条规则的最后检查时间
	 * @param l
	 */
	public void updateRuleCheckTime(long l)
	{
		for(CfgItem3Rule i :  cfg.getListRule())
		{
			i.setlCheckTime(l);;
			  
		}
		
	}
	
	 

}
