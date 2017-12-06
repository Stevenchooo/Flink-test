package com.huawei.bi.mahout.bean.commands;

public class CommandsBuilderFactory {

	public static ICommandsBuilder getCommandsBuilder(String algorithm) throws Exception {
		if ("UserBasedCf".equals(algorithm)) {
			return new UserBasedCfBuilder();
		} else {
			throw new Exception("not supported yet");
		}
	}
}
