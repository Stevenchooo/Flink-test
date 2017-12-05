package com.clusterCruise.config;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.clusterCruise.*;

public class CfgItem3Rule {
	String EXE_ALARM = "";
	
	String r ;
	Pattern rPattern ;
	int iPeriod_second ;
	int iPeriod_count ;
	String send_msg;
	boolean should_send_alarm;
	String auto_fix_cmd;
	String alarm_id ;
	
	//以下是用于统计告警
	long lStartTime =0 ;
	int iOccurCount =0 ;
	long lCheckTime= 0;
	
	CfgItem2 parent ;
	
	
	public CfgItem2 getParent() {
		return parent;
	}
	public void setParent(CfgItem2 parent) {
		this.parent = parent;
	}
	
	public void clearAlarm()
	{
		//5分钟内不要重复告警
		lStartTime = System.currentTimeMillis() + 5*60*1000;
		iOccurCount =0 ;
		lCheckTime = lStartTime ;
	}
	public String getLocalIP()
	{
		InetAddress ia=null;
		String ip="";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ip;
        
	}
	private void makeAlarm()
 {
		if (isShould_send_alarm()) {

			try {
				
				String contentPath = (new File("./")).getAbsolutePath() +"/send_content/" + this.getParent().getParent().getName()+System.currentTimeMillis();
				FileUtil.WriteOverite(contentPath, "path=" + this.getParent().getPath() 
						+ "\r\n regexp =" + getR());
				
				String paramTitle = "title=\"log alarm "+this.getParent().getParent().getName()+" " + getLocalIP() +" " + send_msg + ", "
						+ iOccurCount + " occures in " + (lCheckTime - lStartTime) / 1000 / 60 + " minute \"" ;
				
				FileUtil.ExecCmd(LogChecker.EXE_ALARM +" "+ paramTitle.replace(" ", "_")   
						+  " receiver=\""+ LogChecker.ALARM_EMAIL_RECEIVER +"\"" 
						+" content_file_path=\""+contentPath +"\"" , false);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 如果有动作，就执行动作
			if (auto_fix_cmd.length() > 0) {
				try {
					FileUtil.ExecCmd(auto_fix_cmd, false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// 告警完毕后，清除数据，重新计数。延迟5分钟，抑制告警
		clearAlarm();

	}
	public void judgeAlarm() 
	{
		if((lCheckTime - lStartTime)> 0 
				&& (iPeriod_second<=0) 
				&& (iOccurCount>=iPeriod_count) )
		{
			//按次告警，超过次数就告警
			makeAlarm();
			
		}
		if(iPeriod_second>0)
		{
			//按 次数/时间 告警
			if((lCheckTime - lStartTime)/1000 >= iPeriod_second
					&& iOccurCount>=iPeriod_count)
			{
				makeAlarm();
			}
		}
				
		
		
		
	}
	public String getR()
	{
		return r ;
	}
	public long getlStartTime() {
		return lStartTime;
	}

	public void setlStartTime(long lStartTime) {
		this.lStartTime = lStartTime;
	}

	public int getiOccurCount() {
		return iOccurCount;
	}

	public void setiOccurCount(int iOccurCount) {
		this.iOccurCount = iOccurCount;
	}
	private void increaseOccurCount(int i)
	{
		iOccurCount += i;
	}

	public long getlCheckTime() {
		return lCheckTime;
	}

	public void setlCheckTime(long lCheckTime) {
		this.lCheckTime = lCheckTime;
	}

	
	
	public CfgItem3Rule(String r1 ,
			String period_second1 ,
	String period_count1 ,
	String send_msg1,
	String auto_fix_cmd1,
	String alarm_id1,
	String should_send_alarm1)
	{
		  r = r1;
		  rPattern = Pattern.compile(r);
		  iPeriod_second = Integer.parseInt(period_second1) ;
		  iPeriod_count = Integer.parseInt(period_count1);
		  send_msg = send_msg1;
		  auto_fix_cmd=auto_fix_cmd1 ;
		  alarm_id = alarm_id1;
		  should_send_alarm = Boolean.parseBoolean(should_send_alarm1);
	}
	
	public boolean isShould_send_alarm() {
		return should_send_alarm;
	}
	public boolean  isInRule(String line)
	{
		Matcher m = rPattern.matcher(line);
		
		if(m.matches())
		{
			return true ;
		}
		else
		{
			return false;
		}
		
	}
	public boolean applyRule(String line)
	{
		Matcher m = rPattern.matcher(line);
		
		if(m.matches())
		{
			increaseOccurCount(1);
			return true ;
		}
		return false ;
	}

}
