package routines;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import sun.misc.BASE64Decoder;

/*
 * user specification: the function's comment should contain keys as follows: 1. write about the function's comment.but
 * it must be before the "{talendTypes}" key.
 * 
 * 2. {talendTypes} 's value must be talend Type, it is required . its value should be one of: String, char | Character,
 * long | Long, int | Integer, boolean | Boolean, byte | Byte, Date, double | Double, float | Float, Object, short |
 * Short
 * 
 * 3. {Category} define a category for the Function. it is required. its value is user-defined .
 * 
 * 4. {param} 's format is: {param} <type>[(<default value or closed list values>)] <name>[ : <comment>]
 * 
 * <type> 's value should be one of: string, int, list, double, object, boolean, long, char, date. <name>'s value is the
 * Function's parameter name. the {param} is optional. so if you the Function without the parameters. the {param} don't
 * added. you can have many parameters for the Function.
 * 
 * 5. {example} gives a example for the Function. it is optional.
 */
/**
 * @author temp
 * 
 */
public class Util {
	private String Increment;
	private String sql;
	private String Total_amount;
	private String id;
	private String tableName;
	private String Table_alias;

	public String getTable_alias() {
		return Table_alias;
	}

	public void setTable_alias(String table_alias) {
		Table_alias = table_alias;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTotal_amount() {
		return Total_amount;
	}

	public void setTotal_amount(String total_amount) {
		Total_amount = total_amount;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getIncrement() {
		return Increment;
	}

	public void setIncrement(String increment) {
		Increment = increment;
	}

	/**
	 * helloExample: not return value, only print "hello" + message.
	 * 
	 * 
	 * {talendTypes} String
	 * 
	 * {Category} User Defined
	 * 
	 * {param} string("world") input: The string need to be printed.
	 * 
	 * {example} helloExemple("world") # hello world !.
	 */

	public static void helloExample(String message) {
		if (message == null) {
			message = "World"; //$NON-NLS-1$
		}
		System.out.println("Hello " + message + " !"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/***
	 * ��txt�ļ����зָ��� ��ͷ ��Ϣ �Լ��ļ���Ĵ����
	 * 
	 * @param txtPath
	 *            txt�ļ���ȫ·��
	 * @param serviceName
	 *            ҵ����
	 * @param tableName
	 *            ����
	 * @param serviceID
	 *            ҵ����
	 * @throws Exception
	 *             �쳣
	 */
	public static void txtFormat(String tableDir, String txtPath,
			String serviceName, String tableName, String serviceID,
			String flag, String id, String confDir,
			Properties pro) throws Exception {
		File file = new File(txtPath);
		// �ļ������ڣ��׳��쳣
		if (!file.exists()) {
			throw new Exception("");
		}
		// �ļ�����ݣ��Ž���ת��������������
		if (file.length() != 0) {
			String[] array = txtPath.split("\\/");
			String dir = "";
			for (int i = 0; i < array.length - 1; i++) {
				dir = dir + array[i] + "/";
			}
			dir = dir + tableName + File.separator;
			// ȫ�����(����ϵͳʱ��)
			String time = null;
			if ("isTotal".equals(flag)) {
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.DAY_OF_MONTH, -1);
				Date d = c.getTime();
				String sysTime = format.format(d);
				time = sysTime.replaceAll("-", "");

			} else if (flag.equals("isCrement")) {// �������(�����ϴ�ִ��ʱ��)
				Properties hourProp = new Properties();
				hourProp.load(new FileInputStream(confDir));
				pro = hourProp;
				time = pro.getProperty(id).replaceAll("-", "").replaceAll(" ",
						"").replaceAll(":", "");
			}
			// ����ļ���
			// /home/hadoop/HIBI-Push/data-export/ODS_PIWIK_WA_INFO_DM/piwik_ODS_PIWIK_WA_INFO_DM_����_1.txt.tmp
			String lastName = tableDir + tableName + "/" + serviceName + "_"
					+ tableName + "_" + time + "_" + serviceID + ".tmp";
			// txtPath.replaceAll(".tmp", ".txt.tmp");
			// StringBuffer sb = new StringBuffer();
			File fileDir = new File(tableDir + tableName);
			while (!fileDir.exists()) {
				fileDir.mkdir();
			}
			File lastFile = new File(lastName);
			FileReader fr = new FileReader(file);
			FileWriter fw = new FileWriter(lastFile);
			BufferedReader br = new BufferedReader(fr);
			BufferedWriter bw = new BufferedWriter(fw);
			String line = "";
			int row = 0;
			while ((line = br.readLine()) != null) {
				if (row != 0) {
					StringBuffer sb = new StringBuffer();
					String newLine = line.replaceAll("\\\t", "\001")
							.replaceAll("null", "").replace("NULL", "");
					sb.append(newLine);
					// ����Ĵ����ÿ�м�¼����һ��DM����HM����ʱע�͵�
					/**
					 * if (needSite.equals("1")) { String[] arrays =
					 * tableName.split("\\_"); site = arrays[arrays.length - 1];
					 * sb.append("|").append(site); }
					 **/
					sb.append("\r\n");
					bw.write(new String(sb.toString().getBytes(), "UTF-8"));
				}
				row++;
			}
			br.close();
			bw.flush();
			bw.close();
			Date d = new Date();
			SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println(sDate.format(d)+" export table " + tableName + " successfully.");
		}
	}

	/***
	 * ��ƴsql���ĺ���
	 * 
	 * @param sqlSource
	 *            ʹ�÷������sql��䲿��
	 * @param confDir
	 *            �����ϴ�load��ݵ�ʱ���Լ����ڵ������ļ�ȫ·��
	 * @param flag
	 *            ȫ���������ı�ʾ�� 0��ȫ�� ��0������
	 * @return ������ִ�е�sql
	 * @throws Exception
	 *             �쳣
	 */
	public static String getQyerySQL(String sqlSource, String confDir,
			String id, Properties pro, String isDay) throws Exception {

		StringBuffer sb = new StringBuffer(sqlSource);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Properties hourProp = new Properties();
		hourProp.load(new FileInputStream(confDir));
		pro = hourProp;
		try {
			sb.append("  between  ").append("'").append(pro.getProperty(id))
					.append("'").append("  and  ").append("'");
		} catch (Exception e) {
			System.out.println("error");
		}
		if ("true".equals(isDay)) {
			try {
				String time = getTimeDay(pro, id);
				sb.append(time).append("'");
			} catch (Exception e) {
				System.out.println("error getTimeDay");
			}
		} else {
			try {
				String time = getTimeHour(pro, id);
				// Date d = format.parse(time);
				sb.append(time).append("'");
			} catch (Exception e) {
				System.out.println("error getTimeHour");
			}
		}
		return sb.toString();
	}

	/****
	 * ��ȡ�����Լ��ϴ�load��ݵ�ʱ��
	 * 
	 * @param confDir
	 *            �����ļ���ȫ·��
	 * @return ���� map
	 * @throws Exception
	 *             �쳣
	 */
	// public static Map<String, String> getParameter(String confDir)
	// throws Exception {
	// Properties prop = new Properties();
	// prop.load(new FileInputStream(confDir));
	// String lastTime = prop.getProperty("lastTime");
	// String cycle = prop.getProperty("cycle");
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("lastTime", lastTime);
	// map.put("cycle", cycle);
	// return map;
	// }

	/***
	 * ��ȡ����ִ�е�ʱ������(����ΪСʱ)
	 * 
	 * @param map
	 *            ��������Ϣ�Ĳ���
	 * @return ����ֵ
	 * @throws Exception
	 *             �쳣
	 */
	public static String getTimeHour(Properties pro, String id)
			throws Exception {
		String oldTime = pro.getProperty(id);
		String circle = pro.getProperty("circle");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date oldTimedate = format.parse(oldTime);
		Calendar oldTimeCal = Calendar.getInstance();
		oldTimeCal.setTime(oldTimedate);
		oldTimeCal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(circle));
		oldTimeCal.add(Calendar.SECOND, -1);
		return format.format(oldTimeCal.getTime());
	}

	public static String getTimeDay(Properties pro, String id) throws Exception {
		String oldTime = pro.getProperty(id);
		String circle = (String) pro.get("circle");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date oldTimedate = format.parse(oldTime);
		Calendar oldTimeCal = Calendar.getInstance();
		oldTimeCal.setTime(oldTimedate);
		oldTimeCal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(circle));
		return format.format(oldTimeCal.getTime());
	}

	/***
	 * ��ʱ��������һ��load��ݵ�ʱ��
	 * 
	 * @param confDir
	 *            �����ļ���ȫ·��
	 * @throws Exception
	 *             �쳣
	 */
	public static void updateParameter(String confDir, String id, String flag,
			String isDay) throws Exception {

		Properties prop = new Properties();
		prop.load(new FileInputStream(confDir));
		String latTimeId = prop.getProperty(id);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// ���������ϴ�ִ��ʱ�������
		if ("isCrement".equals(flag)) {
			Date lastDate = format.parse(latTimeId);
			String circle = prop.getProperty("circle");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(lastDate);
			if (isDay.equals("true")) {
				calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(circle));
			} else {
				calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(circle));
			}
			prop.setProperty(id, format.format(calendar.getTime()));
		} else if ("isTotal".equals(flag)) {// ȫ������ϵͳʱ��
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			prop.setProperty(id, format.format(cal.getTime()));
		}
		prop.store(new FileOutputStream(confDir), "=================");

	}

	/***
	 * ���ڶ���ݿ�����Ľ���
	 * 
	 * @param password
	 *            ʹ�������������(��������base64����֮���)
	 * @return ����base64����֮�������
	 * @throws Exception
	 *             �쳣
	 */
	public static String getPassword(String password) throws Exception {
		return getFromBASE64(password);
	}

	/****
	 * �����base64���ܹ��
	 * 
	 * @param s
	 *            �������
	 * @return ���ؽ���֮�������
	 * @throws Exception
	 *             �쳣
	 */
	public static String getFromBASE64(String s) throws Exception {
		if (s == null) {
			return null;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}

	/*****
	 * ��ȡָ��dir���������ļ���ȫ·��
	 * 
	 * @param fileDir
	 *            Ŀ¼·��
	 * @return ����map
	 */
	@SuppressWarnings("unused")
	public static Map<Integer, String> getLogName(String fileDir) {
		Map<Integer, String> pathMap = new HashMap<Integer, String>();
		if (pathMap == null) {
			pathMap = new HashMap<Integer, String>();
		}
		File file = new File(fileDir);
		// �ļ�
		if (!file.isDirectory()) {
			pathMap.put(pathMap.size(), file.getPath());
		} else if (file.isDirectory()) { // �����Ŀ¼�� ����������Ŀ¼ȡ�������ļ���
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(fileDir + "/" + filelist[i]);
				if (!readfile.isDirectory()) {
					pathMap.put(pathMap.size(), readfile.getPath());

				}
			}
		}
		return pathMap;
	}

	/***
	 * ��ȡtxt���ݽ�map��д���µ�txt
	 * 
	 * @param filePath
	 *            �ļ�ȫ·��
	 * @param timeFlag
	 *            ʱ���ֶε�����
	 * @param timeStyle
	 *            ʱ���ʽ
	 * @param SeparationFlag
	 *            �ָ���
	 * @param logDir
	 *            ��־�ļ���·��
	 * @param serviceName
	 *            ������
	 * @param logName
	 *            ��־�ļ���
	 * @param serviceID
	 *            ����ID
	 * @throws Exception
	 *             �쳣
	 */
	@SuppressWarnings("unchecked")
	public static void getAndWriteTxts(boolean bool, String filePath,
			int timeFlag, String timeStyle, String SeparationFlag,
			String logDir, String serviceName, String logName,
			String serviceID, String dirRoot, String logTableName)
			throws Exception {
		String date = null;
		int count = 1;
		int number = 1000;
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		File file = new File(filePath);
		FileReader fileReder = new FileReader(file);
		BufferedReader buffReader = new BufferedReader(fileReder);
		String line = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(timeStyle);
		String reline = null;
		while ((line = buffReader.readLine()) != null) {
			reline = line.trim();
			if (null != reline && !"".equals(reline)) {
				String[] array = reline.split(SeparationFlag);
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < array.length; i++) {
					sb.append(array[i] + "\001");
				}
				if (sb.length() != 0) {
					sb.deleteCharAt(sb.length() - 1);
				}
				line = sb.toString();
				int timeNum = timeFlag >= 1 ? timeFlag - 1 : timeFlag;
				if (timeNum <= array.length - 1) {
					String timeStr = array[timeNum + 1];
					date = dateFormat.format(dateFormat.parse(timeStr));
					if (map.containsKey(date)) {
						List<String> listLine = map.get(date);
						listLine.add(line);
						map.put(date, listLine);
					} else {
						List<String> noConList = new ArrayList<String>();
						noConList.add(line);
						map.put(date, noConList);
					}
				}
				count++;
			}
			// ���list����д�뵽ָ����txt
			if (count == number) {
				writeFile(bool, map, logDir, serviceName, logName, serviceID,
						dirRoot, logTableName);
				count = 0;
				map.clear();
			}
		}
		if (null == line) {
			writeFile(bool, map, logDir, serviceName, logName, serviceID,
					dirRoot, logTableName);
			count = 0;
			map.clear();
		}
		fileReder.close();
	}

	public static void writeFile(boolean bool, Map<String, List<String>> data,
			String logDir, String serviceName, String logName,
			String serviceID, String dirRoot, String logTableName)
			throws Exception {
		Iterator<String> it = data.keySet().iterator();
		while (it.hasNext()) {
			Object keyDate = it.next();
			StringBuffer txtPath = new StringBuffer();
			txtPath.append(dirRoot).append(logTableName).append("/").append(
					serviceName).append("_").append(logName).append("_")
					.append(
							keyDate.toString().replaceAll(" ", "").replaceAll(
									"-", "")).append("0000").append("_")
					.append(serviceID).append(".tmp");
			System.out.println(txtPath.toString());
			File newFile = new File(txtPath.toString());
			FileWriter write = new FileWriter(newFile, true);
			BufferedWriter buffWrite = new BufferedWriter(write);
			List<String> listContext = data.get(keyDate);
			for (int i = 0; i < listContext.size(); i++) {
				String newLine = listContext.get(i);
				buffWrite.write(newLine);
				buffWrite.write("\r\n");
			}
			buffWrite.flush();
			buffWrite.close();
		}
	}

	public static List<List<Util>> getTableInfo(String path) throws Exception {

		File file = new File(path);
		Document configDoc = parseConfig(new FileInputStream(file));
		Element configRoot = configDoc.getRootElement();
		Iterator<Element> tables = configRoot.elementIterator();
		List<List<Util>> tableList = new ArrayList<List<Util>>();
		while (tables.hasNext()) {
			List sqlList = new ArrayList();
			Element table = tables.next();
			Iterator sqls = table.elementIterator();
			while (sqls.hasNext()) {
				Util sqlInfo = new Util();
				Element sql = (Element) sqls.next();
				sqlInfo.setIncrement(sql.attributeValue("Increment"));
				sqlInfo.setTotal_amount(sql.attributeValue("Total_amount"));
				sqlInfo.setId(sql.attributeValue("id"));
				sqlInfo.setTable_alias(sql.attributeValue("Alias_name"));
				sqlInfo.setSql(sql.getStringValue());
				sqlList.add(sqlInfo);
			}
			tableList.add(sqlList);
		}
		return tableList;
	}

	/**
	 * dom4j���������ļ�
	 * 
	 * @param configFile
	 *            �����ļ�ȫ·��
	 * @return document����
	 * @throws DocumentException
	 *             �쳣
	 * @see [�ࡢ��#��������#��Ա]
	 */
	private static Document parseConfig(InputStream configFile)
			throws DocumentException {
		SAXReader saxReader = new SAXReader();
		Document cfgDocument = null;
		cfgDocument = saxReader.read(configFile);
		return cfgDocument;
	}

	/**
	 * 
	 * @param logDir
	 *            ��־�ļ�Ŀ¼
	 * @param tmpDir
	 *            ��ʱ�ļ�Ŀ¼
	 * @param propFile
	 *            ����ʱ�������ļ�
	 * @param circle
	 *            �������ڣ�D����H��ΪD������ʱ���һ�죬ΪH������ʱ���һСʱ
	 */
	public static void separateLog(String logDir, String propFile,
			String circle, int index, String serviceName, String logName,
			String serviceID, String dirRoot, String logTableName,
			String logString,String logDays) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> createNewFile = new HashMap<String, String>();
		// 1.������־�ļ�Ŀ¼�µ������ļ�����ʱĿ¼
		String tmpDir = dirRoot + logTableName + File.separator;
		try {
			(new File(tmpDir)).mkdirs(); // ����ļ��в����� �������ļ���
			// ���tmpDirĿ¼�µ������ļ�
			File t = new File(tmpDir);
			String[] tf = t.list();
			File tft = null;
			for (int m = 0; m < tf.length; m++) {
				tft = new File(tmpDir + tf[m]);
				tft.delete();
			}
			File a = new File(logDir);
			String[] file = a.list();
			File temp = null;
			Calendar cfile = Calendar.getInstance();
			//��ȡ��ǰʱ�䣬����ʱ���˻ص����õ�N��ǰ
			Calendar cnow = Calendar.getInstance();
			cnow.setTime(new Date());
			int days = Integer.parseInt(logDays);
			int backDays = 0 - days;
			cnow.add(Calendar.DAY_OF_MONTH, backDays);
			for (int i = 0; i < file.length; i++) {
				if (logDir.endsWith(File.separator)) {
					temp = new File(logDir + file[i]);
				} else {
					temp = new File(logDir + File.separator + file[i]);
				}
				// ����־Ŀ¼�µ��ļ�����ɸѡ��1.�ļ�����ض��ַ����"userprofile.operation.log"
				// 2.����޸�ʱ��Ϊ2����
				long modifyTime = temp.lastModified();
				cfile.setTimeInMillis(modifyTime);
				if ((file[i].contains(logString)) && (cfile.compareTo(cnow)>=0)) {
					if (temp.isFile()) {
						FileInputStream input = new FileInputStream(temp);
						FileOutputStream output = new FileOutputStream(tmpDir
								+ File.separator + (temp.getName()).toString());
						byte[] b = new byte[1024 * 5];
						int len;
						while ((len = input.read(b)) != -1) {
							output.write(b, 0, len);
						}
						output.flush();
						output.close();
						input.close();
					}
				}
			}
		} catch (Exception e) {
			System.out.println("������־�ļ��г���");
			e.printStackTrace();

		}

		// 2.��ȡ�����ļ����ϴθ���ʱ��
		Calendar cTemp = Calendar.getInstance();
		File f = new File(propFile);
		if (!f.exists()) {
			throw new Exception("�ļ�������");
		}
		Properties hourProp = new Properties();
		try {
			hourProp.load(new FileInputStream(propFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Properties pro = hourProp;
		String time = pro.getProperty("time");
		int circleInt = Integer.parseInt(pro.getProperty("circle"));
		Calendar calendarProp = Calendar.getInstance();
		calendarProp.setTime(sdf.parse(time));
		cTemp.setTime(sdf.parse(time));
		if (circle.equals("D")) {

			cTemp.add(Calendar.DAY_OF_MONTH, circleInt);
		} else {
			cTemp.add(Calendar.HOUR_OF_DAY, circleInt);
		}
		
		//������õ������ǰ�Сʱ���߰���������ļ���������ļ�����Ҫ��������ڸ�ʽ
		SimpleDateFormat sdfCircle;
		if(circle.equals("D"))
		{
			sdfCircle = new SimpleDateFormat("yyyyMMdd");
		}
		else
		{
			sdfCircle = new SimpleDateFormat("yyyyMMddHH");
		}

		// 3.������õĸ���ʱ�������־���
		Calendar calendarRec = Calendar.getInstance();
		BufferedReader reader = null;
		BufferedWriter write = null;
		File tmp = new File(tmpDir);
		String tmpFile[] = tmp.list();
		for (int j = 0; j < tmpFile.length; j++) {
			reader = new BufferedReader(new FileReader(tmpDir + tmpFile[j]));
			String line = null;
			while ((line = reader.readLine()) != null) {
				try {
					// ����־�ļ���¼��ݷָ����֣�Ȼ��ȡ��ʱ���ֶ�
					String[] values = line.split("\\|");
					String dateStr = values[index];
					Date date = sdf.parse(dateStr);
					calendarRec.setTime(date);
				} catch (Exception e1) {
					continue;
				}
				// ��־�ļ���¼��ʱ����������ļ���ʱ�䣬��Ϊ������ݣ����������д���Ӧ���ļ���

				if (calendarRec.compareTo(calendarProp) >= 0
						&& calendarRec.compareTo(cTemp) < 0) {
					String date_rec = sdfCircle.format(calendarRec.getTime());
					String recFileDate = date_rec;
					String newFile = createNewFile.get(recFileDate);
					if (newFile == null) {
						newFile = date_rec;
						createNewFile.put(recFileDate, newFile);
					}
					if (write != null) {
						write.flush();
						write.close();
						write = null;
					}
					if (write == null) {
						// ��������ǰ�Сʱ���߰���������ļ���
						String fileName = newFile;
						if(newFile.length() == 8)//day
						{
							fileName = fileName + "030000";
						}
						else
						{
							fileName = fileName + "0000";
						}
						write = new BufferedWriter(new FileWriter(tmpDir
								+ serviceName + "_" + logName + "_" + fileName
								+ "_" + serviceID + ".tmp", true));
					}
					write.write(line);
					write.newLine();
				}
			}
			if (write != null) {
				write.flush();
				write.close();
				write = null;
			}
			// ɾ���������ʱ�ļ�
			if (reader != null) {
				reader.close();
				reader = null;
			}
			File delFile = new File(tmpDir + tmpFile[j]);
			if (delFile.isFile()) {
				if (!delFile.delete()) {
					throw new Exception("ɾ����ʱ�ļ�ʧ��");
				}
			}
		}// for

		// 4.������εĸ���ʱ��
		Properties prop = new Properties();
		prop.load(new FileInputStream(propFile));
		prop.setProperty("time", sdf.format(cTemp.getTime()));
		prop.store(new FileOutputStream(propFile), "=================");

	}
}
