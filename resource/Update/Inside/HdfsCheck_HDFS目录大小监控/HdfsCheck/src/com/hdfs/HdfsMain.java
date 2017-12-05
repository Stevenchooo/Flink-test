package com.hdfs;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;

import com.hdfs.config.CfgItem1;
import com.hdfs.config.ConfigureXml;
import com.huawei.hadoop.oi.colocation.DFSColocationAdmin;
import com.huawei.hadoop.oi.colocation.DFSColocationClient;
import com.huawei.hadoop.security.LoginUtil;

public class HdfsMain extends Thread {

	private static final String TESTFILE_TXT = "/testfile.txt";
	private static final String COLOCATION_GROUP_GROUP01 = "gid01";
	private static final String ZOOKEEPER_SERVER_PRINCIPAL_KEY = "zookeeper.server.principal";
	private static final String ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL = "zookeeper/hadoop.hadoop.com";

	private static Configuration conf = new Configuration();
	private static DFSColocationAdmin dfsAdmin;
	private static DFSColocationClient dfs;

	private static String PRINCIPAL = "username.client.kerberos.principal";
	private static String KEYTAB = "username.client.keytab.file";

	private static ArrayList<CfgItem1> configItem1List = new ArrayList<CfgItem1>();
	public static String CFG_FILE="cfg.xml";
	
	// This is the principal name of keytab.

	private static String LOGIN_CONTEXT_NAME = "Client";
	private static String OUTPUT_FILE="/data/hdfs_data.txt";

	 

	private static void init(String PRNCIPAL_NAME, String PATH_TO_KEYTAB, String PATH_TO_KRB5_CONF) throws IOException {
		conf.set(KEYTAB, PATH_TO_KEYTAB);
		conf.set(PRINCIPAL, PRNCIPAL_NAME);

		LoginUtil.setJaasConf(LOGIN_CONTEXT_NAME, PRNCIPAL_NAME, PATH_TO_KEYTAB);
		LoginUtil.setZookeeperServerPrincipal(ZOOKEEPER_SERVER_PRINCIPAL_KEY, ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL);
		LoginUtil.login(PRNCIPAL_NAME, PATH_TO_KEYTAB, PATH_TO_KRB5_CONF, conf);
	}

	/**
	 * create and write file
	 * 
	 * @throws java.io.IOException
	 */
	private static void put() throws IOException {

		FSDataOutputStream out = dfs.create(new Path(TESTFILE_TXT), true, COLOCATION_GROUP_GROUP01, "lid01");
		// the data to be written to the hdfs.
		byte[] readBuf = "Hello World".getBytes("UTF-8");
		out.write(readBuf, 0, readBuf.length);
		out.close();
	}

	public void CountRecruise(Path path1, int iCurrentDepth, int iDepth) throws Exception {

		// System.out.println("hdsf path=" + hdfs) ;

		try {
			dfsAdmin = new DFSColocationAdmin(conf);
			dfs = new DFSColocationClient();
			dfs.initialize(URI.create("hdfs://hacluster"), conf);

			
			
			//先统计当前的情况
			ContentSummary summary = dfs.getContentSummary(path1);
			FileUtil.WriteAppend(OUTPUT_FILE, summary.getFileCount() + "|" + summary.getLength() + "|"
					+ summary.getSpaceConsumed() +"|"+ path1.toUri()  + "\n");
		 

			// 子目录
			FileStatus[] files = dfs.listStatus(path1);
			for (int i = 0; i < files.length; i++) {
				try {
					if (files[i].isDirectory()) {
						if(iCurrentDepth< iDepth )
						{
							if(files[i].getPath().toUri().toString().indexOf("pt_") < 0)
							{
								//pt_开头的目录不统计
								CountRecruise(files[i].getPath(),iCurrentDepth+1,iDepth);
							}
						}
						 
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {

		} finally {

			try {
				dfs.close();
			} catch (Exception e) {

			}

			try {

				dfsAdmin.close();
			} catch (Exception e) {

			}

		}

	}

	/**
	 * delete file
	 * 
	 * @throws java.io.IOException
	 */
	@SuppressWarnings("deprecation")
	private static void delete() throws IOException {
		dfs.delete(new Path(TESTFILE_TXT));
	}

	public static String HOME_PATH = "";

	// 参数: type , filepath, resultpath

	public static void main(String[] args) throws IOException {
		// init(args[1], args[2], args[3]);
		// File f= new File("./");
		// HOME_PATH = f.getAbsolutePath();
		
		File fileHome= new File("./");
		String homePath = fileHome.getAbsolutePath() +"/";

		CFG_FILE =homePath + CFG_FILE ;
		OUTPUT_FILE = homePath+ OUTPUT_FILE ;
		configItem1List = ConfigureXml.parserXml(CFG_FILE);
		//ToHdfsThread hdfs = new ToHdfsThread(args[2].split("=")[1]);
		//hdfs.start();
		
		HdfsMain hm = new HdfsMain();
		hm.checkPath();
		
	}
	
	public void checkPath()
	{
		for(CfgItem1 item: configItem1List)
		{
			String p1 = item.getName();
			try {
				//路径深度从1开始
				CountRecruise(new Path(p1),1,item.getDepth());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	 
	

}
