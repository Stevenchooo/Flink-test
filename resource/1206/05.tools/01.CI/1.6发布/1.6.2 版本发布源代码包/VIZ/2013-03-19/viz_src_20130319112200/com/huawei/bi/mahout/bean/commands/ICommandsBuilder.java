package com.huawei.bi.mahout.bean.commands;

import java.util.List;

import com.huawei.bi.mahout.domain.DataMiningJob;

public interface ICommandsBuilder {
	List<String[]> buildCommands(DataMiningJob job, String exeId);
}
