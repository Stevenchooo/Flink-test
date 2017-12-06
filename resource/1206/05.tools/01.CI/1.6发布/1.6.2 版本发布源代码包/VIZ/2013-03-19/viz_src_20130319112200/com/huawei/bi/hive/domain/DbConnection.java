package com.huawei.bi.hive.domain;

public class DbConnection {

	int connId;
	String connName;
	String connType;
	String connDriver;
	String connUrl;
	String connUser;
	String connPassword;

	public int getConnId() {
		return connId;
	}

	public void setConnId(int connId) {
		this.connId = connId;
	}

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public String getConnType() {
		return connType;
	}

	public void setConnType(String connType) {
		this.connType = connType;
	}

	public String getConnDriver() {
		return connDriver;
	}

	public void setConnDriver(String connDriver) {
		this.connDriver = connDriver;
	}

	public String getConnUrl() {
		return connUrl;
	}

	public void setConnUrl(String connUrl) {
		this.connUrl = connUrl;
	}

	public String getConnUser() {
		return connUser;
	}

	public void setConnUser(String connUser) {
		this.connUser = connUser;
	}

	public String getConnPassword() {
		return connPassword;
	}

	public void setConnPassword(String connPassword) {
		this.connPassword = connPassword;
	}

}
