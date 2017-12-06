package com.huawei.bi.task.bean.taskrunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.huawei.bi.task.bean.logger.IExeLogger;

public class CommandsUtil {

	public static void run(List<String[]> commands, String exeId,
			IExeLogger exeLogger) {
		try {

			for (int i = 0; i < commands.size(); i++) {
				Process process = Runtime.getRuntime().exec(commands.get(i));

				exeLogger.writeDebug("<br/>");
				exeLogger.writeDebug("begin step " + i);

				// debug information is written here
				InputStream is1 = process.getErrorStream();
				BufferedReader rd1 = new BufferedReader(new InputStreamReader(
						is1));
				String line1 = null;
				while ((line1 = rd1.readLine()) != null) {
					exeLogger.writeDebug(line1);
				}

				exeLogger.writeDebug("end step " + i);
				exeLogger.writeDebug("<br/>");

				// result information is here
				exeLogger.writeResult("<br/>");
				exeLogger.writeResult("result of step " + i);
				InputStream is2 = process.getInputStream();
				BufferedReader rd2 = new BufferedReader(new InputStreamReader(
						is2));
				String line2 = null;
				while ((line2 = rd2.readLine()) != null) {
					exeLogger.writeResult(line2);
				}
				exeLogger.writeResult("result of step " + i + " end");
				exeLogger.writeResult("<br/>");
			}
		} catch (Exception e) {
			try {
				exeLogger.writeDebug("error occurs");
				exeLogger.writeDebug(e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}

	}

}
