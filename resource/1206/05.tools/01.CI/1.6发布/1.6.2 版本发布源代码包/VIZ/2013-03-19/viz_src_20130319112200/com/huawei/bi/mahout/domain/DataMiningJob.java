package com.huawei.bi.mahout.domain;

import java.util.Date;
import java.util.Map;

import com.huawei.bi.common.domain.Resource;

public class DataMiningJob extends Resource {

	public DataMiningJob() {
		super();
		super.setType("DMJob");
	}

	private Algorithm algorithm;
	private Map<String, String> algorithmOptionMap;

	private String currentStatus;
	private Date lastRunTime;

	public String printAlgorithmOptInTable() {
		StringBuilder sb = new StringBuilder();
		for (String opt : algorithmOptionMap.keySet()) {
			sb.append(opt);
			sb.append(" : ");
			sb.append(algorithmOptionMap.get(opt));
			sb.append("<br/>");
		}

		String result = sb.toString();
		if (result.length() > 0) {
			result.substring(0, result.length() - 5);
		}

		return result;
	}

	/*
	 * getters & setters
	 */

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public Map<String, String> getAlgorithmOptionMap() {
		return algorithmOptionMap;
	}

	public void setAlgorithmOptionMap(Map<String, String> algorithmOptionMap) {
		this.algorithmOptionMap = algorithmOptionMap;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public Date getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(Date lastRunTime) {
		this.lastRunTime = lastRunTime;
	}

}
