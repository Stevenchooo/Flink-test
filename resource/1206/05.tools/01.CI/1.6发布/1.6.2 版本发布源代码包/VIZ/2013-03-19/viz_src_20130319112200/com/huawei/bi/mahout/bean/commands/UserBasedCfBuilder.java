package com.huawei.bi.mahout.bean.commands;

import java.util.ArrayList;
import java.util.List;

import com.huawei.bi.mahout.domain.DataMiningJob;
import com.huawei.bi.util.Config;

public class UserBasedCfBuilder implements ICommandsBuilder {

	@Override
	public List<String[]> buildCommands(DataMiningJob job, String exeId) {

		if (job == null) {
			// TODO
		}

		// TODO get the settings
		String inputTableFullPath = (String) job.getAlgorithmOptionMap().get(
				"input_table");
		String outputTableFullPath = (String) job.getAlgorithmOptionMap().get(
				"output_table");

		String inputDb = null;
		String inputTable = null;
		if (inputTableFullPath.contains(".")) {
			inputDb = inputTableFullPath.split(".")[0];
			inputTable = inputTableFullPath.split(".")[1];
		} else {
			inputDb = "default";
			inputTable = inputTableFullPath;
		}

		String tempTable = "temp_" + job.getId() + "_" + exeId;
		String tempTableFullPath = inputDb + "." + tempTable;

		String[] step1 = new String[] {
				"hive",
				"...e",
				"create table "
						+ tempTableFullPath
						+ " (userid bigint,itemid bigint,rate double,unixtime bigint) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'" };

		String[] step2 = new String[] {
				"hive",
				"-e",
				"INSERT OVERWRITE TABLE " + tempTableFullPath
						+ " select * FROM " + inputTableFullPath };

		String inputDir = "/user/hive/warehouse/";
		if ("default".equals(inputDb)) {
			inputDir = "/user/hive/warehouse/" + tempTable;
		} else {
			inputDir = "/user/hive/warehouse/" + inputDb + ".db/" + tempTable;
		}

		String mahoutJarPath = Config.get("mahout.job.jar");
		String outputDir = "/mahout/" + job.getId() + "/" + exeId + "/output";
		String tempDir = "/mahout/" + job.getId() + "/" + exeId + "/temp";

		// input: hive table ratings
		String[] step3 = new String[] { "hadoop", "jar", mahoutJarPath,
				"org.apache.mahout.cf.taste.hadoop.item.RecommenderJob",
				"-Dmapred.input.dir=" + inputDir,
				"-Dmapred.output.dir=" + outputDir, "--tempDir", tempDir,
				"--similarityClassname", "SIMILARITY_PEARSON_CORRELATION" };

		String[] step4 = new String[] { "hive", "-e",
				"drop table " + tempTableFullPath };

		// TODO copy result into table
		// /mahout/11111/1111_8a296fb7_8b53_5634_0999_4fdd5728e707/output/part-r-00000

		// create the output table if not exist
		String[] step5 = new String[] {
				"hive",
				"-e",
				"create table IF NOT EXISTS "
						+ outputTableFullPath
						+ " (userid bigint,recomm string) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n'" };

		// copy the data into output table
		String[] step6 = new String[] {
				"hive",
				"-e",
				"LOAD DATA INPATH '" + outputDir
						+ "/part-r-*' OVERWRITE INTO TABLE "
						+ outputTableFullPath };

		// the command list
		List<String[]> list = new ArrayList<String[]>();
		list.add(step1);
		list.add(step2);
		list.add(step3);
		list.add(step4);
		list.add(step5);
		list.add(step6);

		return list;
	}

}
