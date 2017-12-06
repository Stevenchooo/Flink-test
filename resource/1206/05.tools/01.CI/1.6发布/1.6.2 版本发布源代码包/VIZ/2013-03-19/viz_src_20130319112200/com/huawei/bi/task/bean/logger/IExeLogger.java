package com.huawei.bi.task.bean.logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IExeLogger {
    
    long getResultRows();
    
	void writeDebug(String text) throws IOException;

	void writeResult(String text) throws Exception;

	List<String> readDebug() throws IOException;

	List<String> readResult() throws IOException;
	
	InputStream readStreamResult() throws IOException;
	
	void flush();

	void delete();
}
