package routines;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;



import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;


import bi_3_0_vo.ActionDM;
import bi_3_0_vo.EventDM;
import bi_3_0_vo.VisitDM;



public class BI3JSONUtil {
	
	public static String[] formatJSON(String jsonStr) {
		List<VisitDM> visitDMs = new ArrayList<VisitDM>();
		List<ActionDM> actionDMs = new ArrayList<ActionDM>();
		List<EventDM> eventDMs = new ArrayList<EventDM>();
		List<String> errorJSON = new ArrayList<String>();
		
		JSONObject oneLine = null;
		//�ж����JSON��ʽ
		try {
			oneLine = JSONObject.fromObject(jsonStr);
		} catch (JSONException e) {
			System.out.println("ERROR JSON object, error message is: " + e.getMessage());
			errorJSON.add(jsonStr + "ERROR JSON object, error message is: " + e.getMessage());
			return formatValues(visitDMs, actionDMs, eventDMs, errorJSON);
		}
		
		JSONArray sArray = null;
		try {
			sArray = oneLine.getJSONArray("s");
		} catch (Exception e) {
			System.out.println("ERROR JSON object, error message is: " + e.getMessage());
			errorJSON.add(jsonStr + "ERROR JSON object, error message is: " + e.getMessage());
			return formatValues(visitDMs, actionDMs, eventDMs, errorJSON);
		}
		
		for(int sArrayIndex=0; sArrayIndex<sArray.size(); sArrayIndex++){
			String uuid = getUUID();
			VisitDM visitDM = null;
			try {
				visitDM = formatVisitDm((String)oneLine.get("g"));
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
				errorJSON.add(jsonStr + " " + e1.getMessage());
				return formatValues(visitDMs, actionDMs, eventDMs, errorJSON);
			}
			visitDM.visit_id = uuid;
			
			long firstActionTime = 0;
			long lastActionTime = 0;
			String entryActivityName = "";
			String exitActivityName = "";
			int totalActions = 0;
			int totalDuration = 0;
			
			JSONObject sObject = JSONObject.fromObject(sArray.get(sArrayIndex));
			if(sObject.has("b")){
				
				JSONArray bArray = null;
				try {
					bArray = sObject.getJSONArray("b");
				} catch (Exception e) {
					System.out.println("ERROR bJSONArray, error message is: " + e.getMessage());
					errorJSON.add(jsonStr + "ERROR bJSONArray, error message is: " + e.getMessage());
					return formatValues(visitDMs, actionDMs, eventDMs, errorJSON);
				}
				
				for(int bArrayIndex=0; bArrayIndex<bArray.size(); bArrayIndex++){
					ActionDM actionDM = new ActionDM();
					String[] bObjectArray = StringUtils.splitPreserveAllTokens(bArray.get(bArrayIndex).toString(), ',');
					
					//�ж�b�����Ƿ���3��ֵ
					if(bObjectArray.length != 3){
						System.out.println("ERROR: Can not recognize bObjectArray: " + Arrays.toString(bObjectArray));
						errorJSON.add(jsonStr + " ERROR: Can not recognize bObjectArray: " + Arrays.toString(bObjectArray));
						continue;
					}
					//����ʱ��
					if(Long.parseLong(bObjectArray[2])<0 || Long.parseLong(bObjectArray[2])>24*60*60){
						System.out.println("ERROR: Can not recognize bObjectArray: " + Arrays.toString(bObjectArray));
						errorJSON.add(jsonStr + " ERROR: Can not recognize bObjectArray: " + Arrays.toString(bObjectArray));
						continue;
					}
					try {
						actionDM.action_local_time = formatDate(bObjectArray[1]);
					} catch (Exception e) {
						System.out.println("ERROR: Can not recognize bObjectArray: " + Arrays.toString(bObjectArray));
						errorJSON.add(jsonStr + " ERROR: Can not recognize bObjectArray: " + Arrays.toString(bObjectArray));
						continue;
					}
					actionDM.activity_name = bObjectArray[0];
					actionDM.duration = Integer.parseInt(bObjectArray[2]);
					actionDM.app_package_name = visitDM.app_package_name;
					actionDM.device_id_md5 = visitDM.device_id_md5;
					actionDM.visit_id = visitDM.visit_id;
					
					actionDMs.add(actionDM);
					
					totalActions++;
					totalDuration += actionDM.duration;
					Date bObject1Date = null;
					try {
						bObject1Date = getDate(bObjectArray[1]);
					} catch (Exception e) {
						continue;
					}
					
					if(totalActions == 1){
						firstActionTime = bObject1Date.getTime();
						lastActionTime = bObject1Date.getTime();
						entryActivityName = actionDM.activity_name;
						exitActivityName = actionDM.activity_name;
					}else{
						if(bObject1Date.getTime() > lastActionTime){
							lastActionTime = bObject1Date.getTime();
							exitActivityName = actionDM.activity_name;
						}else if(bObject1Date.getTime() < firstActionTime){
							firstActionTime = bObject1Date.getTime();
							entryActivityName = actionDM.activity_name;
						}
					}
				}
			}
			if(sObject.has("h")){
				String hObject = sObject.getString("h");
				String[] hObjectArray = StringUtils.splitPreserveAllTokens(hObject, ',');
				if(hObjectArray.length < 2 || hObjectArray.length > 5 )
				{
					System.out.println("ERROR: Can not recognize hObjectArray: " + Arrays.toString(hObjectArray));
					errorJSON.add(jsonStr + " ERROR: Can not recognize hObjectArray: " + Arrays.toString(hObjectArray));
					continue;
				}
				if(hObjectArray[1].length() > 500){
					System.out.println("ERROR: Can not recognize hObjectArray: net_provider_name.length()>500");
					errorJSON.add("ERROR: Can not recognize hObjectArray: net_provider_name.length()>500");
					continue;
				}
				if(hObjectArray.length == 2)
				{
					visitDM.net_system_name = hObjectArray[0];
					visitDM.net_provider_name = hObjectArray[1];
				}
				else if(hObjectArray.length == 3)
				{
					visitDM.net_system_name = hObjectArray[0];
					visitDM.net_provider_name = hObjectArray[1] + "&" + hObjectArray[2];
				}
				else if(hObjectArray.length == 4)
				{
					visitDM.net_system_name = hObjectArray[0];
					visitDM.net_provider_name = hObjectArray[1];
					visitDM.session_id = hObjectArray[2];
					visitDM.ref_id = hObjectArray[3];
				}
				else if(hObjectArray.length == 5)
				{
					visitDM.net_system_name = hObjectArray[0];
					visitDM.net_provider_name = hObjectArray[1] + "&" + hObjectArray[2];
					visitDM.session_id = hObjectArray[3];
					visitDM.ref_id = hObjectArray[4];
				}
			}
			
			if(sObject.has("e")){
				JSONArray eArray = null;
				try {
					eArray = sObject.getJSONArray("e");
				} catch (Exception e) {
					System.out.println("ERROR eJSONArray, error message is: " + e.getMessage());
					errorJSON.add(jsonStr + "ERROR eJSONArray, error message is: " + e.getMessage());
					return formatValues(visitDMs, actionDMs, eventDMs, errorJSON);
				}
				
				for(int eArrayIndex=0; eArrayIndex<eArray.size(); eArrayIndex++){
					EventDM eventDM = new EventDM();
					String[] eObjectArray = StringUtils.splitPreserveAllTokens(eArray.get(eArrayIndex).toString(), ',');
					
					//�ж�e�����Ƿ���3��ֵ
					if(eObjectArray.length != 3){
						System.out.println("ERROR: Can not recognize eObjectArray: " + Arrays.toString(eObjectArray));
						errorJSON.add(jsonStr + " ERROR: Can not recognize eObjectArray: " + Arrays.toString(eObjectArray));
						continue;
					}
					
					eventDM.event_key = eObjectArray[0];
					eventDM.event_value = eObjectArray[1];
					try {
						eventDM.event_local_time = formatDate(eObjectArray[2]);
					} catch (Exception e) {
						System.out.println("ERROR: Can not recognize eObjectArray: " + Arrays.toString(eObjectArray));
						errorJSON.add(jsonStr + " ERROR: Can not recognize eObjectArray: " + Arrays.toString(eObjectArray));
						continue;
					}
					eventDM.app_package_name = visitDM.app_package_name;
					eventDM.device_id_md5 = visitDM.device_id_md5;
					
					eventDMs.add(eventDM);
				}
			}
			
			visitDM.visit_first_action_time = formatDate(new Date(firstActionTime));
			visitDM.visit_last_action_time = formatDate(new Date(lastActionTime));
			visitDM.visit_entry_activity_name = entryActivityName;
			visitDM.visit_exit_activity_name = exitActivityName;
			visitDM.visit_duration = totalDuration;
			visitDM.visit_total_actions = totalActions;
			visitDMs.add(visitDM);
		}
		
		return formatValues(visitDMs, actionDMs, eventDMs, errorJSON);
	}
	
	public static VisitDM formatVisitDm(String gObject) throws Exception {
		VisitDM visitDM = new VisitDM();
		String[] gArray = StringUtils.splitPreserveAllTokens(gObject, ',');
		int gLength = gArray.length;
		
		//��JSON�汾��֤gObjectԪ�ص�����
		if(!((gLength==9 && "1.0".equals(gArray[0])) || (gLength==10 && "2.0".equals(gArray[0])))){
			throw new Exception(gObject + " ERROR: version 1.0 gLength must equals 9 or version 2.0 gLength must equals 10!!!");
		}
		
		//�����ӭ���Ų���Ϊ��
		if("".equals(gArray[6]) || "".equals(gArray[7])){
			throw new Exception(gObject + " ERROR: device_id_md5 and app_package_name must ISNOTNULL!!!");
		}
		
		visitDM.terminal_os = gArray[1];
		visitDM.terminal_type = gArray[3];
		visitDM.rom_ver = gArray[4];
		visitDM.app_ver = gArray[5];
		visitDM.device_id_md5 = gArray[6];
		visitDM.app_package_name = gArray[7];
		
		if(gLength == 9){
			//version 1.0
			visitDM.dev_id = "";
			visitDM.biz_channel_name = gArray[8];
		}else if(gLength == 10){
			//version 2.0
			visitDM.dev_id = gArray[8];
			visitDM.biz_channel_name = gArray[9];
		}
		return visitDM;
	}
	
	public static String getUUID(){
		String s = UUID.randomUUID().toString();
		//ȥ����-����� 
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
	}
	
	//�����ַ�ת��
	public static String transfStr(String str){
		str = str.replaceAll("\n", "\\\\n");
		return str;
	}
	
	public static String[] formatValues(List< VisitDM> visitDMs, List< ActionDM> actionDMs, List< EventDM> eventDMs, List<String> errorJSONs){
		String[] result = new String[4];
		StringBuffer visitDMsSB = new StringBuffer();
		for( VisitDM visitDM : visitDMs){
			String visitDMStr = transfStr(visitDM.toString());
			visitDMsSB.append(visitDMStr).append("\n");
		}
		if(visitDMsSB.lastIndexOf("\n") != -1){
			result[0] = visitDMsSB.substring(0, visitDMsSB.lastIndexOf("\n"));
		}else{
			result[0] = visitDMsSB.toString();
		}
		
		StringBuffer actionDMsSB = new StringBuffer();
		for( ActionDM actionDM : actionDMs){
			String actionDMStr = transfStr(actionDM.toString());
			actionDMsSB.append(actionDMStr).append("\n");
		}
		if(actionDMsSB.lastIndexOf("\n") != -1){
			result[1] = actionDMsSB.substring(0, actionDMsSB.lastIndexOf("\n"));
		}else{
			result[1] = actionDMsSB.toString();
		}
		
		StringBuffer eventDMsSB = new StringBuffer();
		for( EventDM eventDM : eventDMs){
			String eventDMStr = transfStr(eventDM.toString());
			eventDMsSB.append(eventDMStr).append("\n");
		}
		if(eventDMsSB.lastIndexOf("\n") != -1){
			result[2] = eventDMsSB.substring(0, eventDMsSB.lastIndexOf("\n"));
		}else{
			result[2] = eventDMsSB.toString();
		}
		
		StringBuffer errorJSONsSB = new StringBuffer();
		for( String errorJSON : errorJSONs){
			String errorJSONStr = transfStr(errorJSON);
			errorJSONsSB.append(errorJSONStr).append("\n");
		}
		if(errorJSONsSB.lastIndexOf("\n") != -1){
			result[3] = errorJSONsSB.substring(0, errorJSONsSB.lastIndexOf("\n"));
		}else{
			result[3] = errorJSONsSB.toString();
		}
		
		return result;
	}
	
	public static DateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
	public static DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static DateFormat df3 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	public static DateFormat df4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	
	public static Date getDate(String dateStr) throws Exception
	{
		if(null != dateStr && StringUtils.isNumeric(dateStr) && dateStr.length() == 14)
		{
			Date d = df1.parse(dateStr);
			return d;
		}
		else if(null != dateStr && StringUtils.isNumeric(dateStr) && dateStr.length()==17)
		{
			Date d = df3.parse(dateStr);
			return d;
		}
		else
		{
			throw new Exception("");
		}
		
	}
	
	public static String formatDate(Date d){
		return df2.format(d);
	}
	
	public static String formatDate(String d) throws Exception 
	{
		if(null == d)
		{
			return null;
		}
		if(d.length() == 14)
		{
			return df2.format(getDate(d));
		}
		else if(d.length() == 17)
		{
			return df2.format(getDate(d));
		}
		
		return null;
	}
}
