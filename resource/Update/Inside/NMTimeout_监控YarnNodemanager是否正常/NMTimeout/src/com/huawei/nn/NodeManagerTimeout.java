package com.huawei.nn;
 
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

 
import com.huawei.hadoop.oi.colocation.DFSColocationAdmin;
import com.huawei.hadoop.oi.colocation.DFSColocationClient;

/**判断nodemanager 分配任务超时的情况，发现这种情况，需要重启yarn nodemanager服务*/

public class NodeManagerTimeout   {

	private static FileSystem fSystem; /* HDFS file system */
 
	 
	
	private static final long SLEEP_TIME = 5*60 * 1000;
	public static String HOME_PATH ="" ;
	public static String EMAIL_RECEIVER ="" ;
	public static int PERIOD_MINUTE=0 ;
	public static String FILE_NAME_ATTACH="/hostList.txt";
	public static String FILE_NAME_EMPTY="/empty.txt";
	public static String EXE_ALARM ="/alarm.sh";
	
	//主机名为key
	public static Map<String, HostUnit> mapHost=new HashMap<String, HostUnit >();
	 
	 
	/**
	 * init get a FileSystem instance
	 * 
	 * @throws IOException
	 */
	private static void init() throws IOException {

		Configuration conf = new Configuration();
		// conf file
		 

		// get filesystem
		try {
			fSystem = FileSystem.get(conf);
		} catch (IOException e) {
			throw new IOException("Get fileSystem failed.");
		}
	}
 

	/**
     * TODO
     * @param filePath
     * @return
     * @since 2017年2月27日
     */
    private static boolean checkPath(Path filePath)
    {
        String path = filePath.toString();
        // len("hdfs://hacluster/tmp") =20
        if(path.length() < 20)
        {
            return false;
        }
        if(path.contains(".") || path.contains(" "))
        {
            return false;
        }
        return true;
    }



    public static void main(String[] args) {
    	//String s ="{\"type\":\"REDUCE_ATTEMPT_FAILED\",\"event\":{\"org.apache.hadoop.mapreduce.jobhistory.TaskAttemptUnsuccessfulCompletion\":{\"taskid\":\"task_1495263744089_1007_r_000280\",\"taskType\":\"REDUCE\",\"attemptId\":\"attempt_1495263744089_1007_r_000280_0\",\"finishTime\":1495267626881,\"hostname\":\"lf2-bi-onedata-71-3-15\",\"port\":26009,\"rackname\":\"/default/rack8\",\"status\":\"FAILED\",\"error\":\"AttemptID:attempt_1495263744089_1007_r_000280_0 Timed out after 600 secs\",\"counters\":"; 
    	HOME_PATH = args[0];
    	EMAIL_RECEIVER=args[1];
    	PERIOD_MINUTE = Integer.parseInt(args[2]);
    	
    	FILE_NAME_ATTACH =HOME_PATH + FILE_NAME_ATTACH;
    	EXE_ALARM = HOME_PATH+ EXE_ALARM;
    	FILE_NAME_EMPTY = HOME_PATH+ FILE_NAME_EMPTY;
    	
    	//addNode(s,true);
	    NodeManagerTimeout nm  = new NodeManagerTimeout();
	    nm.run();
	    
	    
	   
	}
	
	/**
     * TODO
     * @param args
     * @return
     * @since 2017年2月27日
     */
    private static boolean checkArgs(String[] args)
    {
        if(args == null || args.length != 2)
        {
            System.out.println("Args should be 2!");
            return false;
        }
        
        if(args[0].contains(".") || args[0].contains(" "))
        {
            System.out.println("path can not contains '.' or ' ' !");
            return false;
        }
        
        if(args[0].length() < 15)
        {
            System.out.println("the length of path should greate than 15 !");
            return false;
        }
        return true;
    }


    
    
    public void run()
    {
		//while (true) 
    	{
			try {
				init(); // login from here
			} catch (IOException e) {
				System.err.println("Init hdfs filesystem failed.");
				e.printStackTrace();
			}
			
			readJhist();
			
			//打印统计
			FileUtil.WriteOverite( FILE_NAME_ATTACH, ""  );
			for(Map.Entry<String, HostUnit > entry :mapHost.entrySet())
			{
				FileUtil.WriteAppend( FILE_NAME_ATTACH, entry.getValue().toString() );
				
			}

			//执行自动切换的任务
			//发送邮件告警
			SimpleDateFormat  sdfDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(mapHost.size()>0)
			{
				try {
					FileUtil.ExecCmd(EXE_ALARM +" \"title=[fatal]YarnNM_timeout.host_count_is"+mapHost.size() +"\" receiver=\"" +EMAIL_RECEIVER+"\" " +" content_file_path=\"" +FILE_NAME_EMPTY +"\" attach_file_path=\""+FILE_NAME_ATTACH +"\"", true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			 
			
			try {
				if (fSystem != null) {
					fSystem.close();
				}
			} catch (IOException e) {
				System.out.println("Close fs failed.");
			}

		}

	}
    

    public  void readJhist(   )
	{
    	SimpleDateFormat  sdfDay = new SimpleDateFormat("yyyy/MM/dd");
    	SimpleDateFormat  sdfHour = new SimpleDateFormat("HH");
    	 Date currentTime = new Date();
    	 //检查10分钟前的数据
    	 long lCurrentTime = System.currentTimeMillis() - PERIOD_MINUTE * 60 * 1000;
    	 String dateString = sdfDay.format(currentTime);
    	 String hourString = sdfHour.format(currentTime);

        Path f = new Path("/mr-history/done/"+dateString);
        FileStatus[] status = null;
        try
        {
            status = fSystem.listStatus(f);
        }
        catch (IOException e)
        {
            System.out.println("Get file list failed for " + f.toString() );
            return;
        }
        
        if(status != null)
        {
            
            for(FileStatus fStatus : status)
            {
            	if(fStatus.isDirectory() )
            	{
            		//mr_hisotry 只有1级目录
            		checkFailTask(fStatus.getPath(),lCurrentTime);
            	}
                 
            }
        }
        
	}
    
    public void checkFailTask(Path path, long lBaseTime)
    {
    	 
        FileStatus[] status = null;
        try
        {
            status = fSystem.listStatus(path);
        }
        catch (IOException e)
        {
            //System.out.println("Get file list failed for " + path + ". Nothing will be done for it.");
            e.printStackTrace();
            return;
        }
        
        if(status != null)
        {
        	//最近10分钟前的文件
             
            for(FileStatus fStatus : status)
            {
            	if(fStatus.isFile() )
            	{
            		String fileName=fStatus.getPath().toString()  ;
            		 
            		
            		if(fStatus.getModificationTime() > lBaseTime  && fileName.endsWith("jhist"))
                    {
            			//System.out.println("check file:"+ fileName);
            			anaFile(fStatus.getPath());
                          
                    }
            	}
                
            }
        }
    	
    }
    
    public static void addNode(String line )
    {
    	 
    	String attemptId = getData(line, "attemptId");
    	String hostName = getData(line, "hostname");
    	String error = getData(line, "\"error\"");
    	String finishTime = getData(line, "finishTime");
    	TimeoutUnit tu = new TimeoutUnit(hostName,attemptId, error, finishTime);
    	HostUnit hu= mapHost.get(hostName);
    	//System.out.println("addNode " + attemptId);
    	if(hu == null)
    	{
    		HostUnit hu2= new HostUnit();
    		hu2.addTimeoutUnit(tu);
    		mapHost.put(hostName, hu2);
    	}
    	else
    	{
    		hu.addTimeoutUnit(tu);
    	}
    	//if(isMap)
    		
     
    	  
    }
    public static String getData(String line, String flag)
    {
    	int i1= line.indexOf(flag);
    	int i2 = line.indexOf(",",i1+flag.length()) ;
    	String data = line.substring(i1+flag.length()+2, i2 ).replace("\"","");
    	//System.out.println(data);
    	return data ;
    }
    void anaFile(Path jhistPath)
    {
    	byte[] ioBuffer = new byte[1024];
		int readLen = 0;
		StringBuffer result = new StringBuffer();
		FSDataInputStream hdfsInStream = null;
		//FSBufferedReader A= null;
		 
		try {
			 
			hdfsInStream = fSystem.open(jhistPath);
			String line ="" ;
			while (line != null) {
				// System.out.print(line);

				line=hdfsInStream.readLine();
				if(line !=null )
				{
					if(line.indexOf("MAP_ATTEMPT_FAILED")>=0 && line.indexOf("Timed out after")>=0 )
					{
						addNode(line );
					}
					if(line.indexOf("REDUCE_ATTEMPT_FAILED")>=0 && line.indexOf("Timed out after")>=0 )
					{
						addNode(line );
					}
					
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
			
		}
		 
    }

}
