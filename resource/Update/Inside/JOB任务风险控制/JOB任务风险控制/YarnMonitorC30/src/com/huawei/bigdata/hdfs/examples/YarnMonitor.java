package com.huawei.bigdata.hdfs.examples;

import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import com.huawei.hadoop.oi.colocation.DFSColocationAdmin;
import com.huawei.hadoop.oi.colocation.DFSColocationClient;
import com.huawei.hadoop.security.LoginUtil;

public class YarnMonitor extends Thread {

	private static final String TESTFILE_TXT = "/testfile.txt";
	private static final String COLOCATION_GROUP_GROUP01 = "gid01";
	private static final String ZOOKEEPER_SERVER_PRINCIPAL_KEY = "zookeeper.server.principal";
	private static final String ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL = "zookeeper/hadoop.hadoop.com";

	private static Configuration conf = new Configuration();
	private static DFSColocationAdmin dfsAdmin;
	private static DFSColocationClient dfs;

	private static String PRINCIPAL = "username.client.kerberos.principal";
	private static String KEYTAB = "username.client.keytab.file";

	// This is the principal name of keytab.

	private static String LOGIN_CONTEXT_NAME = "Client";

	public static ConcurrentHashMap mapJob = new ConcurrentHashMap();
	//killRule:default=50000,QueueA=9000
	public static ConcurrentHashMap<String, RuleQueueItem> mapRuleMapQueue = new ConcurrentHashMap<String, RuleQueueItem>();
	
	//hdfs_read:default=****,QueueA=****
	public static ConcurrentHashMap<String, RuleQueueItem> mapRuleHdfsRead = new ConcurrentHashMap<String, RuleQueueItem>();
	
	//hdfs_write:default=****,QueueA=****
	public static ConcurrentHashMap<String, RuleQueueItem> mapRuleHdfsWrite = new ConcurrentHashMap<String, RuleQueueItem>();

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

	public static String ReadFile(String hdfs) throws Exception {

		 System.out.println("hdfs path=" + hdfs) ;

		byte[] ioBuffer = new byte[1024];
		int readLen = 0;
		StringBuffer result = new StringBuffer();
		FSDataInputStream hdfsInStream = null;
		try {
			dfsAdmin = new DFSColocationAdmin(conf);
			dfs = new DFSColocationClient();
			dfs.initialize(URI.create("hdfs://hacluster"), conf);
			
			hdfsInStream = dfs.open(new Path(hdfs));
			
			while (readLen != -1) {
				// System.out.write(ioBuffer, 0, readLen);
				result = result.append(new String(ioBuffer));
				readLen = hdfsInStream.read(ioBuffer);
				 
				if(result.length()> 1000000)
				{
					//不像mr任务，跳出
					break; 
				}
			}

		} catch (Exception e) {

		} finally {

			if(hdfsInStream !=null)
			{
				try{
					hdfsInStream.close();
				}
				catch(Exception e)
				{
					
				}
			}
			
			try{
				dfs.close();
			}
			catch(Exception e)
			{
				
			}
			
			try{

				dfsAdmin.close();
			}
			catch(Exception e)
			{
				
			}
			
			

		}
		return result.toString();

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

	/**
	 * delete group
	 * 
	 * @throws java.io.IOException
	 */
	private static void deleteGroup() throws IOException {
		dfsAdmin.deleteColocationGroup(COLOCATION_GROUP_GROUP01);
	}

	public static String anaJobXml2SQL(String content) {
		String flag = "hive.query.string</name><value>";
		int i1 = content.indexOf(flag);
		String result = "";
		if (i1 >= 0) {

			i1 = i1 + flag.length();
			int i2 = content.indexOf("</value>", i1);
			result = content.substring(i1, i2);

		} else {
			result = "";
		}
		return result;

	}

	public static void main_test(String[] args) throws IOException {
		String s = "<property><name>dfs.namenode.datanode.registration.ip-hostname-check</name><value>true</value><source>hdfs-default.xml</source></property>"
				+ "<property><name>dfs.image.transfer.chunksize</name><value>65536</value><source>hdfs-default.xml</source></property>"
				+ "<property><name>hive.query.string</name><value>select id from test_v2_01</value><source>programatically</source></property>"
				+ "<property><name>hive.cluster.delegation.token.store.class</name><value>org.apache.hadoop.hive.thrift.MemoryTokenStore</value><source>programatically</source><source>org.apache.hadoop.hive.conf.LoopingByteArrayInputStream@1bba2502</source></property>";
		String s1 = anaJobXml2SQL(s);
		System.out.println(s1);
	}

	public static String findByFlag(String fileConent, String flagBeging, String flagEnd)
	{
		String flag = flagBeging;
		
		int i1 = fileConent.indexOf(flag);
		String result = "";
		if (i1 >= 0) {

			i1 = i1 + flag.length();
			int i2 = fileConent.indexOf(flagEnd, i1);
			result = fileConent.substring(i1, i2);

		} else {
			result = "";
		}

		result = result.replace("\r", " ");
		result = result.replace("\n", " ");

		if(result.indexOf("N/A") >= 0)
		{
			result="";
		}
		return result;
	}
	/**
	 * 出来的结果不含回车、换行
	 * 
	 * @param sourceFilePath
	 * @param flagBeging
	 * @param flagEnd
	 * @return
	 */
	public static String anaStandared(String sourceFilePath, String flagBeging, String flagEnd) {
		String flag = flagBeging;
		String content = "";
		try {
			content = ReadFile(sourceFilePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int i1 = content.indexOf(flag);
		String result = "";
		if (i1 >= 0) {

			i1 = i1 + flag.length();
			int i2 = content.indexOf(flagEnd, i1);
			result = content.substring(i1, i2);

		} else {
			result = "";
		}

		result = result.replace("\r", " ");
		result = result.replace("\n", " ");

		return result;

	}
 
	public static String readYarnDataByFlag(String sourceFileName, String startFlag, String stopFlag)
	{
		String result = anaStandared(sourceFileName, startFlag , stopFlag);

		// FileUtil.WriteOverite(outputFileName, result);

		return result;
		
	}
	

	public static String HOME_PATH = "";
	public static String HDFS_PATH ="";

	// 参数: type , filepath, resultpath
	
	public static void initArgs(String[] args)
	{
		for(String s: args)
		{
			if(s.startsWith("mapSize:"))
			{ 
				System.out.println("mapSize:");
				fillRules(mapRuleMapQueue, s);
			}else if(s.startsWith("hdfsRead:"))
			{ 
				System.out.println("c");
				fillRules(mapRuleHdfsRead, s);
			}else if(s.startsWith("hdfsWrite:"))
			{ 
				System.out.println("hdfsWrite:");
				fillRules(mapRuleHdfsWrite, s);
			}else if(s.startsWith("hdfsPath"))
			{
				System.out.println("hdfsPath");
				HDFS_PATH= s.split("=")[1];
				System.out.println(HDFS_PATH);
			}
			
		}
		
	}
	
	public static void fillRules(ConcurrentHashMap<String, RuleQueueItem> mapRule, String rule)
	{
		rule = rule.substring(rule.indexOf(":") + 1, rule.length());
		String[] rs = rule.split(";");
		for (String s : rs) {
			String[] s2 = s.split("=");
			RuleQueueItem new1 = new RuleQueueItem(s2[0], s2[1]);
			mapRule.put(new1.getQueue(), new1) ;
			System.out.println("rule: " + new1.getQueue() + "," + new1.getIndexLimit());
		}
		
	}
	public static void main(String[] args) throws IOException {
		// init(args[1], args[2], args[3]);
		// File f= new File("./");
		// HOME_PATH = f.getAbsolutePath();

		HOME_PATH = args[0];
		initArgs(args);
		
		//map大的
		KillMapThread kt= new KillMapThread( mapJob) ;
		kt.start();
		
		
		
		//hdfspath=/tmp/hsdfa/
		ToHdfsThread hdfs = new ToHdfsThread(HDFS_PATH);
		hdfs.start();
		
	 
		// System.out.println("HOME_PATH=" + HOME_PATH);
		MapreduceThread mt = new MapreduceThread(mapJob);
		mt.start();
		
		//统计每个线程的文件和hdfs读写情况
		CounterThread ct = new CounterThread(mapJob);
		ct.start();
		
		 
		while (true) {
			try {
				sleep(30000);
			} catch (Exception e) {

			}
		}

		

	}

}
