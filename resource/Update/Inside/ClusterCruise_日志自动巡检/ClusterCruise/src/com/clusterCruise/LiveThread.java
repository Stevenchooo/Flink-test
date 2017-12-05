package com.clusterCruise;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clusterCruise.config.*;



public class LiveThread extends Thread {
	public String LOCAL_FILENAME_REG="";
	long lLastPos = 0 ;
	CfgItem2 cfg  ;
	String currentFileName; 
	
	public static void main(String[] args) {
		//chooseNewrestFile("D:/Test/f.*txt");
		

	}
	
	public  String chooseNewrestFile(String fileReg)
 {
		// 分出路径和文件名表达式
		int i1 = fileReg.lastIndexOf("/");

		String path = fileReg.substring(0, i1);
		LOCAL_FILENAME_REG = fileReg.substring(i1 + 1, fileReg.length());
		File f = new File(path);
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				Pattern rPattern;
				rPattern = Pattern.compile(LOCAL_FILENAME_REG);

				Matcher m = rPattern.matcher(name);

				if (m.matches()) {
					// System.out.println("matched name=" + name);
					return true;
				} else {
					return false;
				}

			}
		};
		File[] fs = f.listFiles(filter);
		if (fs != null && fs.length > 0) {
			Arrays.sort(fs, new Comparator<File>() {
				public int compare(File f1, File f2) {
					long diff = f1.lastModified() - f2.lastModified();
					if (diff > 0)
						return 1;
					else if (diff == 0)
						return 0;
					else
						return -1;
				}

				public boolean equals(Object obj) {
					return true;
				}

			});

			String newrestFile = null;
			if (fs.length > 0) {
				newrestFile = fs[fs.length - 1].getAbsolutePath();
			}

			// System.out.println("newrestFile =" + newrestFile );

			return newrestFile;
		}
		return null;
	}
	 
	public LiveThread(CfgItem2 item) throws IOException
	{
		cfg =item ;
		currentFileName = chooseNewrestFile(cfg.getPath() );
		
		System.out.println("current file name = " + currentFileName);
		//初始化文件开始位置
		RandomAccessFile rf=null;
		try {
			rf = new RandomAccessFile(currentFileName, "r");
			lLastPos = rf.length() ;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		finally
		{
			if(rf != null)
			{
			rf.close();
			}
		}
		
		//初始换规则检查时间和次数
		item.initRuleStart();
		
		
	}
	public void run()
	{
		while(true)
		{
			int iBufferSize =  1024000 ;
			boolean bShouldSleep= true ;
			byte[] buff = new byte[iBufferSize] ;
			RandomAccessFile rf=null;
			int realPos =0 ;
			try {
				//读完就关闭,避免影响业务转存日志
				String checkNewrestFile = chooseNewrestFile(cfg.getPath()) ;
				if(checkNewrestFile !=null && !currentFileName.equals(checkNewrestFile) )
				{
					System.out.println(System.currentTimeMillis() +" :change new file from  " + currentFileName + " to  " + checkNewrestFile);
					
					//最新的文件发生了变化，从头读
					currentFileName = checkNewrestFile ;
					lLastPos =0 ;
					
					
				}
				rf = new RandomAccessFile(currentFileName, "r");		
				//等待一分钟，确认文件是被截断了
				int iCountOut=0 ;
				boolean bOut = false;
				while(!bOut)
				{
					if(rf.length() < lLastPos )
					{
						
						iCountOut++;
						if(iCountOut > 6)
						{
							bOut = true;
						}
						
						try
						{
							sleep(60*1000);
						}
						catch(Exception e)
						{
							
						}
					}
					else
					{
						//文件长度正常，立即跳出
						bOut= true ;
					}
					
				}
				if(iCountOut > 6)
				{
					//等了360，确实文件发生了变化。文件转存了，从头读
					lLastPos=0 ;
					System.out.println(System.currentTimeMillis() +" :seek reset to 0. old size=" + lLastPos +", need size="+ rf.length());
					
				
				}
				
				rf.seek(lLastPos);
				
				
				realPos = rf.read(buff);
				rf.close();
			} 
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
			
			//是否读满4k
			if(realPos > 0)
			{
				//还有数据要处理，不用sleep
				bShouldSleep = false ;
			}
			else
			{
				bShouldSleep =true ;
			}
			
			String content = "" ;
			//索引到最后一个换行符
			 
			int linePos ;
			int beakPos = -1 ;
			for(linePos=realPos-1; linePos>=0;linePos-- )
			{
				if(buff[linePos] == 10)
				{
					beakPos = linePos ;
					break; 
				}
			}
			 
			if(beakPos >=0)
			{
				//记录中含换行
				lLastPos = lLastPos + (beakPos+1) ;
				 
				content = new String(buff,0,linePos);
				 
			}
			else
			{
				//没有换行，指针不移动，content没有
				content="";
				bShouldSleep =true ;
				
			}
			
			anaContent(content) ;
			
			
			//更新规则最后的时间
			updateRuleCheckTime(System.currentTimeMillis());
			
			//触发告警检查
			alarmCheckAndAction();
			
			//继续处理数据，还是sleep
			//System.out.println(System.currentTimeMillis() + " , lLastPos = " + lLastPos);
			if(bShouldSleep)
			{
				try {
					sleep(30*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 按行分析内容
	 * @param content
	 */
	public void anaContent(String content)
	{
		//System.out.println("anaContent content=" + content);
		//对应每行记录，检查所有规则
		BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));  
		String line;  
		try {
			while ( (line = br.readLine()) != null ) {  
				//
				applyRule(line);
			    //System.out.println(line);  
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			if(i.isShould_send_alarm())
			{
				i.applyRule(line);
			} 
		}
		
	}
	
	/**
	 * 更新需要告警的规则的最后检查时间
	 * @param l
	 */
	public void updateRuleCheckTime(long l)
	{
		for(CfgItem3Rule i :  cfg.getListRule())
		{
			if(i.isShould_send_alarm())
			{
				i.setlCheckTime(l);
			}
			  
		}
		
	}
	
	public void alarmCheckAndAction()
	{
		for(CfgItem3Rule i :  cfg.getListRule())
		{
			if(i.isShould_send_alarm())
			{
				i.judgeAlarm();
			}
		}
		
	}

}
