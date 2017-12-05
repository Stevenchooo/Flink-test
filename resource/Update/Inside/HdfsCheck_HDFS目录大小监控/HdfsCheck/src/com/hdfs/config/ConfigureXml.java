package com.hdfs.config;

import java.io.*;
import java.util.*;

import org.jdom2.*;
import org.jdom2.input.*;
 

public class ConfigureXml {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName="D:/project/java/HdfsCheck/cfg.xml";
		ConfigureXml cx = new ConfigureXml();
		cx.parserXml(fileName);

	}
 
	 
	public static ArrayList<CfgItem1> parserXml(String fileName) {

		ArrayList<CfgItem1> configList = new ArrayList<CfgItem1>();
		SAXBuilder builder = new SAXBuilder(false);
		try {
			Document document = builder.build(new File(fileName));
			Element project = document.getRootElement();
			 

			List<Element> pathList = project.getChildren("path");
			for (Element series : pathList) {
				CfgItem1 item1 = new CfgItem1(series.getChildText( "name"), series.getChild("depth").getText()) ;
				configList.add(item1);
			}

		} catch (JDOMException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return configList;

	}   
	  
		 
}
