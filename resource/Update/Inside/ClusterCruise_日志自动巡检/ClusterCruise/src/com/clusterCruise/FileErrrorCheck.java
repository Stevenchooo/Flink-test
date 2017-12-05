package com.clusterCruise;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clusterCruise.config.*;

public class FileErrrorCheck {

	long lLastPos = 0;
	CfgItem1 cfgItem1;
	String pathName;
 

	public FileErrrorCheck(CfgItem1 item, String pathName1) throws IOException {
		cfgItem1 = item;
		pathName = pathName1;

	}

	public void run() {
		try {
			File file = new File(pathName);
			for (File f : file.listFiles()) {
				if (f.isFile()) {
					InputStreamReader read = new InputStreamReader(new FileInputStream(f)); // 考虑到编码格式
					BufferedReader bufferedReader = new BufferedReader(read);

					String line = bufferedReader.readLine();
					while (line != null) {
						applyTopRule(line);
						line = bufferedReader.readLine();
					}

					bufferedReader.close();
					read.close();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 顶级规则，抽出要检查的数据。
	 * 
	 * @param line
	 */
	public void applyTopRule(String line) {

		// item1 下面只有2个rnode
		ArrayList<CfgItem2> cfgItem2List = cfgItem1.getListRnode();
		CfgItem2 globalRnode = null;
		CfgItem2 detailRnode = null;
		
		for (CfgItem2 item2 : cfgItem2List) {
			if (item2.getScope().equals("startCheckFileError")) {
				globalRnode = item2;
			}
			else
			{
				detailRnode = item2;
			}
		}
		
		for (CfgItem3Rule i : globalRnode.getListRule()) {
			if (i.isInRule(line)) {
				//符合顶级规则的，是否已经配置在检查列表中了。
				//对于细则，只要一个符合，就表示满足了
				boolean bNotMatch= true ;
				for (CfgItem3Rule j : detailRnode.getListRule()) 
				{
					if ( j.isInRule(line)) {
						bNotMatch= false;
						break;
					}
						
				}
				if(bNotMatch)
				{
					FileUtil.WriteAppendLine(LogChecker.ERROR_RULE_CHECK_LOG, line);
				}
				
				
			}

		}

	}

	 

}
