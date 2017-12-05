package com.huawei.hdfs;
 
import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

/**删除hdfs上过期的文件*/

public class HdfsDelete {

	private static FileSystem fSystem; /* HDFS file system */
 
	
	private static final int DELETE_NUM_TO_SLEEP = 1000;
	
	private static final long SLEEP_TIME = 10 * 1000;
	
	private static int deleteNums = 0;
	

	
	static
	{
	    try {
            init(); //login from here
        } catch (IOException e) {
            System.err.println("Init hdfs filesystem failed.");
            System.exit(1);
        }
	}
	

 


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
	 * delete file path
	 * 
	 * @param filePath
	 * @return
	 */
	private static boolean deletePath(final Path filePath) {
		try {
		    
			if (!fSystem.exists(filePath)) {
				return true;
			}

			if(checkPath(filePath))
			{
			    System.out.println("Deleted " + filePath.toString());
			    deleteNums++;
			    fSystem.delete(filePath, true);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
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
    	
    	//test();
    	System.out.println("1.限制最早删除3天前的数据，不能少于3天");
    	System.out.println("2.限制文件目录长度必须大于20，目录最后的格式是hdfs://hacluster/**/***");
    	System.out.println("3.限制文件目录不能包含 . 和空格");
    	
	    
	    if(!checkArgs(args))
	    {
	        return;
	    }
	    
	    String path = args[0];
	    int day = 3;
	    try
	    {
	        day = Integer.valueOf(args[1]);
	        if(day < 3)
	        {
	            System.out.println("days is less than 3 ,use default value 3.");
	            day = 3;
	        }
	    }
	    catch(Exception exception)
	    {
	        System.out.println("Parse day value failed ,use default value 3.");
	    }
	    
	    clearOldLogFiles(path,day);
	    
	    try
        {
	        if(fSystem != null)
	        {
	            fSystem.close();
	        }
        }
        catch (IOException e)
        {
            System.out.println("Close fs failed.");
        }
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


    public static void test2()
    {
    	Path f = new Path("/tmp/c00239107");
        FileStatus[] status = null;
        try
        {
            status = fSystem.listStatus(f);
        }
        catch (IOException e)
        {
            System.out.println("Get file list failed for  . Nothing will be done for it.");
            return;
        }
        
        if(status != null)
        {
            
            for(FileStatus fStatus : status)
            {
                 
                    
                    Path delete = fStatus.getPath();
                    //delete.getParent().toUri()
                    //delete.getParent().getName()
                    System.out.println("list " + delete.getName() + "," + delete.toString() +"," + delete.toUri().toString());
                    
                 
            }
        }
    	
    }

    public static void clearOldLogFiles(String path, int day)
	{
        Path f = new Path(path);
        FileStatus[] status = null;
        try
        {
            status = fSystem.listStatus(f); 
        }
        catch (IOException e)
        {
            System.out.println("Get file list failed for " + path + ". Nothing will be done for it.");
            return;
        }
        
        if(status != null)
        {
            long timeline = System.currentTimeMillis() - day * 24 * 60 * 60 * 1000;
            for(FileStatus fStatus : status)
            {
                if(fStatus.getModificationTime() < timeline )
                {
                    if(deleteNums >= DELETE_NUM_TO_SLEEP )
                    {
                        try
                        {
                            Thread.sleep(SLEEP_TIME);
                            deleteNums = 0;
                        }
                        catch (InterruptedException e)
                        {
                            System.out.println("Sleep Failed.");
                        }
                    }
                    
                    Path delete = fStatus.getPath();
                    deletePath(delete);
                }
            }
        }
        
	}

}
