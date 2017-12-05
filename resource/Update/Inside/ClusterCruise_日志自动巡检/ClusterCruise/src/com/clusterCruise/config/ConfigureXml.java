package com.clusterCruise.config;

import java.io.*;
import java.util.*;

import org.jdom2.*;
import org.jdom2.input.*;

import com.clusterCruise.LogChecker;

public class ConfigureXml {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName="D:/project/java/ClusterCruise/cfg.xml";
		ConfigureXml cx = new ConfigureXml();
		cx.parserXml(fileName);

	}

	/*
	public void parserXml(String fileName) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(fileName);
			
			
			Node project = document.getChildNodes().item(0);
			NodeList nodeList = project.getChildNodes();
			
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node rnode = nodeList.item(i);
				System.out.println("---"+rnode.getNodeName() +":" + rnode.getNodeValue());
				/*
				NodeList nodeinfo = project.getChildNodes();
				ruleNode.getNodeName();
				for (int j = 0; j < employeeInfo.getLength(); j++) {
					Node node = employeeInfo.item(j);
					NodeList employeeMeta = node.getChildNodes();
					for (int k = 0; k < employeeMeta.getLength(); k++) {
						System.out.println(
								employeeMeta.item(k).getNodeName() + ":" + employeeMeta.item(k).getTextContent());
					}
				}
				 
			}
			 
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
	*/
	
	 
	public static ArrayList<CfgItem1> parserXml(String fileName) {

		ArrayList<CfgItem1> configList = new ArrayList<CfgItem1>();
		SAXBuilder builder = new SAXBuilder(false);
		try {
			Document document = builder.build(new File(fileName));
			Element project = document.getRootElement();
			LogChecker.ALARM_SMS_RECEIVER = project.getChild("sms_receiver").getText();
			LogChecker.ALARM_EMAIL_RECEIVER = project.getChild("email_receiver").getText();

			List<Element> seriesList = project.getChildren("series");
			for (Element series : seriesList) {
				CfgItem1 item1 = new CfgItem1(series.getAttribute("name").getValue()) ;
				configList.add(item1);
				
				List<Element> rnodeList = series.getChildren("rnode");
				for (Element rnode : rnodeList) {
					// CfgItem
					// path
					Element ePath = rnode.getChild("path");
					//System.out.println();
					String scope = "";
					if (rnode.getAttribute("scope") != null) {
						scope = rnode.getAttribute("scope").getValue();
					}
					// System.out.println("--"+ ePath.getText() );
					CfgItem2 cfgItem2 = new CfgItem2(ePath.getText(), scope);
					item1.addListRnode(cfgItem2);
					// rules
					Element eRules = rnode.getChild("rules");
					for (Element rule : eRules.getChildren("rule")) {
						CfgItem3Rule itemRule = new CfgItem3Rule(rule.getChild("r").getText(),
								rule.getChild("period_second").getText(), rule.getChild("period_count").getText(),
								rule.getChild("send_msg").getText(), rule.getChild("auto_fix_cmd").getText(),
								rule.getChild("alarm_id").getText(), rule.getChild("should_alarm").getText());

						cfgItem2.addRule(itemRule);
					}

					// System.out.println(employee.getName());
				}

			}

		} catch (JDOMException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return configList;

	}   
	  
		 
}
