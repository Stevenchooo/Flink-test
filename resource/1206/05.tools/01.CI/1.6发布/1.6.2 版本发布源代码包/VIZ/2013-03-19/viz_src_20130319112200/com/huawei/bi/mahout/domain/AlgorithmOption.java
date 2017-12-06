package com.huawei.bi.mahout.domain;

public class AlgorithmOption {
	private String name;
	private String defaultValue;
	private boolean isOptional = false;
	
	// TODO possibleValues, dataType, ...
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

}
