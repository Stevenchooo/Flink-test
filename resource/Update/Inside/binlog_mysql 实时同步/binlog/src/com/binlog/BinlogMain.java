package com.binlog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.github.shyiko.mysql.binlog.*;
import com.github.shyiko.mysql.binlog.BinaryLogClient.*;
import com.github.shyiko.mysql.binlog.event.*;
import com.huawei.etl.util.crypto.Aes128Util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;



public class BinlogMain {

	public static String CONFIG_FILE="./db.properties";
	public static String master_ip          ="";                             
	public static String master_user        ="";                             
	public static String master_passwd      ="";                             
	public static String master_dbname      ="";  
	public static boolean master_ignore_textblog = false;
	public static boolean b_debug=false;
	public static String master_timezone      =""; 
	//表名全部转换为小写
	public static HashMap<String, TableInfo> mapTableInfo =  new HashMap<String, TableInfo>()  ;
	public static HashMap<String,String> mapTableId = new HashMap<String,String>(); 
	
	public static ArrayList<String> listTablename = new ArrayList<String>();
	ArrayList<byte[]> listSQLByte = new ArrayList<byte[]>(); 

	public static String slave_ip           ="";                             
	public static String slave_user         ="";                             
	public static String slave_passwd       ="";                             
	public static String slave_dbname       ="";                             
	                       

	public void InitData()
	{
		
		try 
		{ 
			Properties pro = new Properties();
			File f  = new File("./");
			System.out.println(f.getAbsolutePath()) ;
			FileInputStream in = new FileInputStream(CONFIG_FILE);
			pro.load(in);
		 
			master_ip=pro.getProperty("master_ip");
			master_user=pro.getProperty("master_user");
			master_passwd=pro.getProperty("master_passwd");
			master_passwd= new String(Aes128Util.decryptData(master_passwd)) ;
			
			
			master_dbname=pro.getProperty("master_dbname");
			
			//master_ignore_textblog = Boolean.parseBoolean( pro.getProperty("master_ignore_textblog"));
			b_debug =Boolean.parseBoolean( pro.getProperty("debug"));
			master_ignore_textblog = true;
			//System.out.println("master_ignore_textblog"+master_ignore_textblog);
			master_timezone=pro.getProperty("master_timezone");
			 
			slave_ip=pro.getProperty("slave_ip");
			slave_user=pro.getProperty("slave_user");
			slave_passwd=pro.getProperty("slave_passwd");
			slave_passwd = new String(Aes128Util.decryptData(slave_passwd ) );
			 
			slave_dbname=pro.getProperty("slave_dbname");
			
			//依赖slave机器的信息
			String master_tablename=pro.getProperty("master_tablename").toLowerCase();
			String s1[] =master_tablename.split(";");
			for(String s0 : s1)
			{
				if(s0.length()> 0)
				{
					mapTableInfo.put(s0, DBUtil.descTable(s0));
					listTablename.add(s0);
					
				}
			}
			 
			
			in.close();
			 
		}
		catch(Exception e)
		{
				e.printStackTrace();
		}
			
			
	}
	 
	 
	public void doWork()
	{
		BinaryLogClient client = new BinaryLogClient(master_ip, 3306, master_dbname,master_user,master_passwd );
		//因为重连后，binlog文件不会刷新，在发生一次主备切换，就挂了。所以要退出，从外边拉起
		client.setKeepAlive(false);
		
		 
		//new BinaryLogClient(null, 0, null, null, null)
	 
		client.registerEventListener(new EventListener() {

		    @Override
		    public void onEvent(Event event) {
		    	
		    	EventData data = event.getData();
	    		//System.out.println(data.getClass().getName());
	    		String s1 =  data.toString() ;
	    		 
	    		//System.out.println("-----------------------1--------------------------------");
	    		//System.out.println(event.getHeader().getEventType() +"--" + data.getClass().getName() +"--"+ data.toString());

	    		
	    		if(event.getHeader().getEventType()==EventType.EXT_UPDATE_ROWS)
	    		{
	    			//EXT_DELETE_ROWS
	    			UpdateRowsEventData d1= (UpdateRowsEventData)data ;
	    			
	    			long lTableId=d1.getTableId();
	    			//BitSet columnd1.getIncludedColumns()
	    			List<Map.Entry<Serializable[], Serializable[]>> updatedRows = 
	    	                d1.getRows(); 
	    			for( Map.Entry<Serializable[], Serializable[]> row :updatedRows)
	    			{
	    				Serializable[] skey = row.getKey();
	    				Serializable[] sValue =  row.getValue();
	    				 
	    				doUpdate(lTableId, skey ,  sValue );
	    			}
	    		}
	    		
	    		if(event.getHeader().getEventType()==EventType.EXT_WRITE_ROWS)
	    		{ 
	    			//EXT_WRITE_ROWS
	    			WriteRowsEventData d1= (WriteRowsEventData)data ;
	    			
	    			long lTableId=d1.getTableId();
	    			//BitSet columnd1.getIncludedColumns()
	    			List<Serializable[]> insertRows = d1.getRows(); 
	    			for(  Serializable[]  row :insertRows)
	    			{ 
	    				doInsert(lTableId, row );
	    			}

	    		}
	    		
	    		if(event.getHeader().getEventType()==EventType.EXT_DELETE_ROWS)
	    		{ 
	    			//EXT_WRITE_ROWS
	    			DeleteRowsEventData d1= (DeleteRowsEventData)data ;
	    			
	    			long lTableId=d1.getTableId();
	    			//BitSet columnd1.getIncludedColumns()
	    			List<Serializable[]> insertRows = d1.getRows(); 
	    			for(  Serializable[]  row :insertRows)
	    			{ 
	    				doDelete(lTableId, row );
	    			}

	    		}
		     
	    		if(event.getHeader().getEventType()==EventType.TABLE_MAP )
		    	{
	    			//凡是ROW模式，均有QUERY, TABLE_MAP,***,XID
	    			TableMapEventData tblData =(TableMapEventData)data ;
	    			//System.out.println("table name=" + tblData.getTable());
	    			mapTableId.put(""+tblData.getTableId(), tblData.getTable().toLowerCase()) ;
	    			 
		    	}
		    	if(event.getHeader().getEventType()==EventType.QUERY )
		    	{
		    		
		    		String s = ((QueryEventData)data).getSql() ;
		    		//System.out.println("-------------"+ s.substring(0,Math.min(30, s.length())));
		    		if(isNeedTable(s))
		    		{
		    			//连接数据库，更新数据
		    			//System.out.println("execute sql: " + s);
			    		DBUtil.udpateTable(s);		    			
		    		}
		    	}
		    	 
		    	 
		    }
		});
		
		try {
			client.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("come to ends");
		
	
	}
	
	
	public void doInsert(long lTableId, Serializable[] values)
	{
		TableInfo listColumnName=mapTableInfo.get(mapTableId.get(""+ lTableId));
		if(listColumnName != null)
		{
			//插入
			
			String sql= "insert into " + mapTableId.get(""+ lTableId)  ;
			String sqlColumn="";
			String sqlValue="" ;
			
			listSQLByte.clear();
			//update abc set *=*,*=* 
			String flagInterval = ",";
			for(int i=0; i < values.length; i++)
			{
				//sql += " "+columnName.getColumn(i).getColumnName()+"=";
				String columnName = listColumnName.getColumn(i).getColumnName();
				Serializable serValue = values[i];
				if(serValue==null)
				{
					//sqlColumn+= columnName + flagInterval ;
					//sqlValue += "null" +flagInterval;
				}
				else
				{ 
					String valueType=serValue.getClass().getTypeName(); 
					String realValue =value2String(valueType, serValue);
					if(realValue != null)
					{
						//要使用这一列 
						sqlColumn+= columnName + flagInterval ;
						sqlValue += realValue +flagInterval;
					}
								 
				}
				
				 
			}
			//去掉最后一个逗号
			sqlColumn = sqlColumn.substring(0, sqlColumn.length() - flagInterval.length());
			sqlValue = sqlValue.substring(0, sqlValue.length() - flagInterval.length());
			
			sql+= "(" +sqlColumn +") values(" + sqlValue +") " ;
			 
			LogMe.debug("insert sql: "+sql);
			DBUtil.udpateTable(sql);
			
			
		}
		else
		{
			//不用关注的表
			return ;
		}
	}
	public void doDelete(long lTableId, Serializable[] keys )
	{
		TableInfo columnName=mapTableInfo.get(mapTableId.get(""+ lTableId));
		if(columnName != null)
		{
			//是需要更新的表
			
			String sql= "delete from  " + mapTableId.get(""+ lTableId) +" ";
			listSQLByte.clear();
			 
		 
			
			//update abc set *=*,*=*  where
			sql += " where  ";
			String flagInterval = " and ";
			for(int i=0; i < keys.length; i++)
			{
				//sql += " "+columnName.getColumn(i).getColumnName()+"=";
				sql = generateSQLField(lTableId,  sql,  keys[i], i, columnName.getColumn(i).getColumnName(),flagInterval, " is null " ) ;
			 
			}
			//去掉最后一个and
			sql = sql.substring(0, sql.length() - flagInterval.length());
			//System.out.println("delete sql: "+sql);
			DBUtil.udpateTable(sql);
			
			
			
		}
		else
		{
			//不用关注的表
			return ;
		}
		
	}
	
	public void doUpdate(long lTableId, Serializable[] keys, Serializable[] values)
	{
		TableInfo columnName=mapTableInfo.get(mapTableId.get(""+ lTableId));
		if(columnName != null)
		{
			//是需要更新的表
			
			String sql= "update " + mapTableId.get(""+ lTableId) +" set ";
			listSQLByte.clear();
			//update abc set *=*,*=* 
			String flagInterval = ",";
			for(int i=0; i < values.length; i++)
			{
				//sql += " "+columnName.getColumn(i).getColumnName()+"=";
				sql = generateSQLField(lTableId,  sql,  values[i], i ,columnName.getColumn(i).getColumnName(),flagInterval, "=null ") ;
				 
			}
			//去掉最后一个逗号
			sql = sql.substring(0, sql.length() - flagInterval.length());
			
			//update abc set *=*,*=*  where
			sql += " where  ";
			flagInterval = " and ";
			for(int i=0; i < keys.length; i++)
			{
				//sql += " "+columnName.getColumn(i).getColumnName()+"=";
				sql = generateSQLField(lTableId,  sql,  keys[i], i, columnName.getColumn(i).getColumnName(),flagInterval, " is null " ) ;
			 
			}
			//去掉最后一个and
			sql = sql.substring(0, sql.length() - flagInterval.length());
			LogMe.debug("update sql: "+sql);
			DBUtil.udpateTable(sql);
			
			
			
		}
		else
		{
			//不用关注的表
			return ;
		}
		
	}
	
	public String value2String(String  valueType, Serializable serValue)
	{
		String result = null ;
		if("java.lang.String".equals(valueType))
		{
			result="'"+(String)serValue+"'" ;
		}
		else if("java.lang.Integer".equals(valueType))
		{
			result=""+  ((Integer)serValue).intValue()  ;
		}
		else if("java.lang.Long".equals(valueType))
		{
			result=""+ ((Long)serValue).longValue()  ;
		} 
		else if("byte[]".equals(valueType)  )
		{
			if(!master_ignore_textblog)
			{
				result= "?" ;
				listSQLByte.add((byte[])serValue);
			}
		}
		else if("java.util.Date".equals(valueType))
		{
			java.util.Date d1 = ((java.util.Date)serValue);
			//这里面的时间是GTM+0
			
			SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			myFmt.setTimeZone(TimeZone.getTimeZone(master_timezone));
			
			result= "'"+ myFmt.format(d1) +"'" ;
			
		} 
		else if("java.util.BitSet".equals(valueType))
		{
			java.util.BitSet b1 = ((java.util.BitSet)serValue);
			//格式是{233}
			String  s3=b1.toString() ;
			//System.out.println( "--"+b1.size() +", "+ b1.get(0) +"," + b1.get(1) );
			 
			result= ""+ bitSet2Int(b1);
			
		}
		
		else if("java.lang.Double".equals(valueType))
		{
			result= ""+ ((Double)serValue).doubleValue()  ;
			
		}
		else if("java.lang.Float".equals(valueType))
		{
			result= ""+ ((Float)serValue).floatValue()  ;
		}
		else
		{
			//默认带双引号
			System.out.println("ERROR:Dont know type "+ valueType    );
			result=  "'"+ serValue +"'" ;
		}
		
		
		return result ;
	}
	public String generateSQLField(long lTableId, String  sql,  Serializable serValue, int iColumnIndex, String columnName , String flagInterval, String nullvalue )
	{
		if(serValue==null)
		{
			//sql += columnName+ nullvalue +flagInterval;
		}
		else
		{ 
			String valueType=serValue.getClass().getTypeName();
			
			String realValue =value2String(valueType, serValue);
			if(realValue != null)
			{
				//要使用这一列
				sql +=columnName+ "=" + realValue +flagInterval;
			}
						 
		}
		
		return sql ;
		
	}
	
	public int bitSet2Int(BitSet bs)
	{
		long size =  bs.size();
		int result =0 ;
		for(int i=0;i< size;i++){
            if(bs.get(i))
            {
            	result += (1<<i) ;
            }
             
        }
		return result;

	}
	
	/**
	 * 对于statment的方式，sql语句的判断方法
	 * @param sql
	 * @return
	 */
	public boolean isNeedTable(String sql)
	{
		//每个表只有3种
		
		//update table_name
		//delete from table_name
		sql = sql.toLowerCase();
		String[] keys= sql.split(" ");
		sql = sql.replace(" ", "");
		//是否匹配insert
		for(String s1: listTablename)
		{
			if(sql.startsWith("insert"+s1))
			{
				return true;
			}
			if(sql.startsWith("insertinto"+s1))
			{
				return true;
			}
			if(sql.startsWith("update"+s1))
			{
				return true;
			}
			if(sql.startsWith("deletefrom"+s1))
			{
				return true;
			}
		}
		
		 
		
		return false;
		
	}
	
	public static void main(String args[])
	{
		 
		java.util.BitSet b1 = new java.util.BitSet()  ;
		b1.set(0B11);
		System.out.println(b1.cardinality());
		 
		
		BinlogMain t1= new BinlogMain();
		t1.InitData();
		t1.doWork();
	}
}
