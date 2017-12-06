package com.huawei.bi.mahout.bean;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.huawei.bi.mahout.bean.commands.CommandsBuilderFactory;
import com.huawei.bi.mahout.bean.commands.ICommandsBuilder;
import com.huawei.bi.mahout.da.DataMiningJobDA;
import com.huawei.bi.mahout.domain.DataMiningJob;

public class DataMiningJobBean {

	DataMiningJobDA jobDA = new DataMiningJobDA();

	public void run(String jobId, String exeId) throws Exception {
		// get the commands
		DataMiningJob job = this.getJob(jobId);
		ICommandsBuilder builder = CommandsBuilderFactory
				.getCommandsBuilder(job.getAlgorithm().getName());
		List<String[]> commands = builder.buildCommands(job, exeId);

		// run in thread
//		new Thread(new CommandsRunner(commands, exeId)).start();
	}

	public DataMiningJob getJob(String id) throws Exception {
		return jobDA.getJob(id);
	}

	/**
	 * @deprecated use resource tree builder
	 * @return
	 * @throws Exception
	 */
	public List<DataMiningJob> getJobList() throws Exception {
		return jobDA.getAllJobs();
	}

	/**
	 * @deprecated no use, use resource tree builder
	 * @return
	 * @throws Exception
	 */
	public String getJobListAsTable() throws Exception {
		StringBuilder resultSb = new StringBuilder();
		List<DataMiningJob> jobList = getJobList();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (DataMiningJob task : jobList) {
			resultSb.append("<tr>");
			resultSb.append("<td>" + task.getId() + "</td>");
			resultSb.append("<td>" + task.getName() + "</td>");
			resultSb.append("<td>" + task.getAlgorithm().getName() + "</td>");
			resultSb.append("<td>" + task.printAlgorithmOptInTable() + "</td>");
			resultSb.append("<td>" + task.getUser() + "</td>");
			resultSb.append("<td>" + sdf.format(task.getLastModificationTime())
					+ "</td>");
			resultSb.append("<td>" + task.getCurrentStatus() + "</td>");
			resultSb.append("<td>" + sdf.format(task.getLastRunTime())
					+ "</td>");
			resultSb.append("</tr>");
		}

		return resultSb.toString();
	}

}
