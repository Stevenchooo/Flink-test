package com.huawei.bi.mahout.da;

import java.util.ArrayList;
import java.util.List;

import com.huawei.bi.mahout.domain.Algorithm;
import com.huawei.bi.mahout.domain.AlgorithmOption;

public class AlgorithmDA {
	
	public List<Algorithm> getAllAlgorithms() throws Exception {
		List<Algorithm> allAlgorithmList = new ArrayList<Algorithm>();

		allAlgorithmList.add(getAlgorithm("cf"));

		return allAlgorithmList;
	}

	public Algorithm getAlgorithm(String name) throws Exception {

		if (!name.equals("cf")) {
			throw new Exception("not support yet");
		}

		Algorithm recommAlgorithm = new Algorithm();
		recommAlgorithm.setName("cf");

		List<AlgorithmOption> possibleOptions = new ArrayList<AlgorithmOption>();

		AlgorithmOption op1 = new AlgorithmOption();
		op1.setName("input_table");

		AlgorithmOption op2 = new AlgorithmOption();
		op2.setName("output_table");

		AlgorithmOption op3 = new AlgorithmOption();
		op3.setName("similarity method");
		op3.setDefaultValue("SIMILARITY_PEARSON_CORRELATION");

		possibleOptions.add(op1);
		possibleOptions.add(op2);
		possibleOptions.add(op3);

		recommAlgorithm.setPossibleOptions(possibleOptions);

		return recommAlgorithm;
	}
}
