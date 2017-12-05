package com.hdfs;

import java.io.*;

public class FileUtil {

	public static void ExecCmd(String cmd, boolean bPrint) throws IOException, InterruptedException
	{
		
		System.out.println("Start sh = " + cmd);
		//String[] cmdA = { "/bin/sh","-c",   cmd }; 
		Process copyProcess = Runtime.getRuntime().exec(cmd);
		 
		
		ReadConsoleThread  normalConsole = new ReadConsoleThread(copyProcess.getInputStream(), bPrint);
		normalConsole.start();
		
		ReadConsoleThread  errorConsole = new ReadConsoleThread(copyProcess.getErrorStream(), bPrint);
		errorConsole.start();
	 
		
		while(normalConsole.isAlive() || errorConsole.isAlive())
		{
			try
			{
			Thread.sleep(1000);
			}
			catch(Exception e)
			{
				
			}
			
		}
		 
		copyProcess.waitFor();
		copyProcess.destroy();
 
		
	}
	
	public static class ReadConsoleThread extends Thread
	{
		InputStream stream =null;
		boolean bPrint = false ;
		public ReadConsoleThread(InputStream stream1, boolean bPrint1)
		{
			stream = stream1 ;
			bPrint = bPrint1;
		}
		public  void run()
		{
			BufferedReader normalReader = new BufferedReader(
					new InputStreamReader(stream));

			String result = "";
			try {
				int iNormal = 0;
				while (iNormal != -1) {
					iNormal = normalReader.read();
					
					if(iNormal >33 && iNormal<128)
					{
						//System.out.print((char)iNormal +"--");
						result +=(char) iNormal ;
					}
					else
					{
						//System.out.print(""+ iNormal +"--");
					}
					
					 

				}
				if(bPrint)
				{
					System.out.println(result);
				}
				
			} catch (Exception e) {
				e.printStackTrace();

			} catch (Throwable t) {

			} finally {
				if (normalReader != null) {
					try {
						normalReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			// System.out.println("-----------End normal");

		}
	}
	
 
	private static void CreateFile(String fileName)
	{
		File f =  new File(fileName);
		if(!f.exists())
		{
			
			 
			try {
				Writer writer = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(fileName ), "UTF-8"));
				writer.write("");
				writer.flush();
				writer.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void WriteAppend(String fileName, int iData)
	{
		//不存在就新建一个
		CreateFile(fileName);
		//存在了就附加写
		try {
			Writer writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fileName,true ), "UTF-8"));
			writer.write(iData );
		  
			writer.flush();
			writer.close();
						 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static String Read1stLine(String fileName)
	{
		String result="";
		
		try {
			  
            File file=new File(fileName);
   
                InputStreamReader read = new InputStreamReader(new FileInputStream(file)); //考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                result= bufferedReader.readLine() ;
                
                bufferedReader.close(); 
                read.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result ;
	}
	
	public static void WriteAppend(String fileName, String data)
	{
		//不存在就新建一个
		CreateFile(fileName);
		//存在了就附加写
		try {
			Writer writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fileName,true ), "UTF-8"));
			writer.write(data  );
		  
			writer.flush();
			writer.close();
						 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void WriteOverite(String fileName, String data)
	{
		//不存在就新建一个
		CreateFile(fileName);
		//存在了就附加写
		try {
			Writer writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fileName,false ), "UTF-8"));
			writer.write(data  );
		  
			writer.flush();
			writer.close();
						 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String GetHomePath()
	{
		String homePath = new File("./").getAbsolutePath();
		homePath= homePath.substring(0, homePath.length()-2) ;
		
		return homePath ;
	}
           

}
