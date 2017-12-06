package com.huawei.bi.mahout.domain;

import java.util.List;

public class Algorithm {
	private String name;

	private List<AlgorithmOption> possibleOptions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AlgorithmOption> getPossibleOptions() {
		return possibleOptions;
	}

	public void setPossibleOptions(List<AlgorithmOption> possibleOptions) {
		this.possibleOptions = possibleOptions;
	}

}
