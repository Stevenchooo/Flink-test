package com.binlog;

public class ColumnInfo {

	private String name ;
	private String type ;
	
	public ColumnInfo(String name1, String type1)
	{
		name= name1;
		type= type1;
		
	}
	public String getColumnName()
	{
		return name;
	}
	
	public String getColumnType()
	{
		return type ;
	}
}
