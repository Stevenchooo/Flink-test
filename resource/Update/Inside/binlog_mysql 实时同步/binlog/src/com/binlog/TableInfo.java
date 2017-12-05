package com.binlog;

import java.util.ArrayList;

public class TableInfo {
	public String tname ;
	public ArrayList<ColumnInfo>  listColumn = new ArrayList<ColumnInfo>();
	
	public TableInfo(String s)
	{
		//t(a,b,c,e)
	}
	
	public void addColumn(String columName, String columnType)
	{
		
		ColumnInfo ci = new ColumnInfo(columName, columnType);
		listColumn.add(ci);
	}
	public ColumnInfo getColumn(int i)
	{
		return listColumn.get(i);
	}

}
