package com.huawei.bi.task.domain;

import java.util.Date;
import java.util.Map;

public class Task {
	private String taskId;
	private String taskName;
	private String taskType;

	private Map<String, String> optionMap;

	private String options;

	private String owner;
	private Date lastModTime;
	
	private boolean tempTask = true;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Map<String, String> getOptionMap() {
		return optionMap;
	}

	public void setOptionMap(Map<String, String> optionMap) {
		this.optionMap = optionMap;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(Date lastModTime) {
		this.lastModTime = lastModTime;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public boolean isTempTask() {
		return tempTask;
	}

	public void setTempTask(boolean tempTask) {
		this.tempTask = tempTask;
	}

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Task [taskId=");
        builder.append(taskId);
        builder.append(", taskName=");
        builder.append(taskName);
        builder.append(", taskType=");
        builder.append(taskType);
        builder.append(", optionMap=");
        builder.append(optionMap);
        builder.append(", options=");
        builder.append(options);
        builder.append(", owner=");
        builder.append(owner);
        builder.append(", lastModTime=");
        builder.append(lastModTime);
        builder.append(", tempTask=");
        builder.append(tempTask);
        builder.append("]");
        return builder.toString();
    }

}
