package com.huawei.bi.task.bean.logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MonitorExeLogger implements IExeLogger {

	@Override
	public void writeDebug(String text) throws IOException {
		System.err.println(text);
	}

	@Override
	public void writeResult(String text) throws IOException {
		System.out.println(text);
	}

	@Override
	public List<String> readDebug() throws IOException {
		return null;
	}

	@Override
	public List<String> readResult() throws IOException {
		return null;
	}

	@Override
	public void flush() {

	}

	@Override
	public void delete() {

	}

    /**
     * @return
     * @throws IOException
     */
    @Override
    public InputStream readStreamResult() throws IOException
    {
        return null;
    }

    @Override
    public long getResultRows()
    {
        return 0;
    }

}
