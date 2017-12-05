package com.huawei.bigdata.hdfs.examples;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.huawei.hadoop.oi.colocation.DFSColocationAdmin;
import com.huawei.hadoop.oi.colocation.DFSColocationClient;

public class LzoIndexCheck {

	private static Configuration conf = new Configuration();
	private static DFSColocationAdmin dfsAdmin;
	private static DFSColocationClient dfs;
	private static FileSystem fSystem ;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ReadFile(args[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static String ReadFile(String path) throws Exception {

		// System.out.println("hdsf path=" + hdfs) ;

		byte[] ioBuffer = new byte[1024];
		int readLen = 0;
		StringBuffer result = new StringBuffer();
		FSDataInputStream hdfsInStream = null;
		try {
			
			//FileSystem.get
			fSystem = FileSystem.get(URI.create("hdfs://hacluster"),conf);
			//fSystem.deleteOnExit(f);
			Path destPath = new Path(path);
			/*
			dfsAdmin = new DFSColocationAdmin(conf);
			dfs = new DFSColocationClient();
			dfs.initialize(URI.create("hdfs://hacluster"), conf);
			*/
			//hdfsInStream = dfs.open(new Path(hdfs));
			iteratorShowFiles(fSystem,destPath);
			
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
	
	public static void iteratorShowFiles(FileSystem hdfs, Path path){
        try{
            if(hdfs == null || path == null){
                return;
            }
            //获取文件列表
            //hdfs.listFiles(f, recursive)
            FileStatus[] files = hdfs.listStatus(path);

            //展示文件信息
            for (int i = 0; i < files.length; i++) {
                try{
                    if(files[i].isDirectory()){
                        System.out.println(">>>" + files[i].getPath()
                                + ", dir owner:" + files[i].getOwner());
                        //递归调用
                        iteratorShowFiles(hdfs, files[i].getPath());
                    }else if(files[i].isFile() ){
                        System.out.println("   " + files[i].getPath()
                                + ", length:" + files[i].getLen()
                                + ", owner:" + files[i].getOwner());
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
