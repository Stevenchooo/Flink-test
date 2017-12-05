package com.huawei.nn;

import java.text.SimpleDateFormat;
import java.util.*;

public class TimeoutUnit {
	
	private String sHostname;
	public String getsHostname() {
		return sHostname;
	}

	public void setsHostname(String sHostname) {
		this.sHostname = sHostname;
	}

	public String getsAttempId() {
		return sAttempId;
	}

	public void setsAttempId(String sAttempId) {
		this.sAttempId = sAttempId;
	}

	public String getEndTime() {
		return sEndTime;
	}

	public void setsEndTime(String sEndTime) {
		this.sEndTime = sEndTime;
	}

	public String getsError() {
		return sError;
	}

	public void setsError(String sError) {
		this.sError = sError;
	}

	public SimpleDateFormat getSdfDay() {
		return sdfDay;
	}

	public void setSdfDay(SimpleDateFormat sdfDay) {
		this.sdfDay = sdfDay;
	}

	private String sAttempId;
	private String sEndTime ;
	private String sError;
	SimpleDateFormat  sdfDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//new TimeoutUnit(hostName,attemptId, error, finishTime);
	
	public TimeoutUnit(String sHostname1,String sAttempId1, String sError1, String endTime1 )
	{
		sHostname =  sHostname1;
		sAttempId= sAttempId1;
		sEndTime=sdfDay.format(new Date(Long.parseLong(endTime1))) ;
		 
	}

}
