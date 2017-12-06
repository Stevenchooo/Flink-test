package com.huawei.bi.mahout.da;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huawei.bi.mahout.domain.AlgorithmOption;
import com.huawei.bi.mahout.domain.DataMiningJob;

public class DataMiningJobDA {

	AlgorithmDA algorithmDA = new AlgorithmDA();

	public List<DataMiningJob> getAllJobs() throws Exception {
		List<DataMiningJob> allJobList = new ArrayList<DataMiningJob>();

		allJobList.add(getJob("11111"));

		return allJobList;
	}

	public DataMiningJob getJob(String id) throws Exception {

		DataMiningJob jobDemo = new DataMiningJob();
		jobDemo.setId(111);
		jobDemo.setName("name1");
		jobDemo.setDescription("this is a demo, and a demo");
		jobDemo.setUser("admin");
		jobDemo.setLastModificationTime(new Date());
		jobDemo.setCurrentStatus("Running");
		jobDemo.setLastRunTime(new Date());

		jobDemo.setAlgorithm(algorithmDA.getAlgorithm("cf"));
		
		Map<String, String> optionMap = new HashMap<String, String>();
		optionMap.put("input_table", "ratings");
		optionMap.put("output_table", "recommends");
		jobDemo.setAlgorithmOptionMap(optionMap);

		return jobDemo;
	}
}
