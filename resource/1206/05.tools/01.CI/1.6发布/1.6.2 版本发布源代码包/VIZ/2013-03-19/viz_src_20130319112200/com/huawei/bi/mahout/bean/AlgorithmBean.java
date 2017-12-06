package com.huawei.bi.mahout.bean;

import java.util.List;

import com.huawei.bi.mahout.da.AlgorithmDA;
import com.huawei.bi.mahout.domain.Algorithm;

public class AlgorithmBean {

	AlgorithmDA algorithmDA = new AlgorithmDA();

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Algorithm> getAllAlgorithms() throws Exception {
		return algorithmDA.getAllAlgorithms();
	}
	
	
}
