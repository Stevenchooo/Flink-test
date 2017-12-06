package routines;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class RecordSeparate {

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

	/**
	 * ���ı��в�ͬСʱ�ļ�¼�������
	 * 
	 * @param file
	 *            �ļ�ȫ·��
	 * @param index
	 *            �����ֶ����ڵ�����
	 * @param existTitle
	 *            �Ƿ���ڱ���
	 * @return ����ļ������
	 * @throws Exception �쳣
	 */
	public static List<String> separateRecord(String file, int index,
			boolean existTitle,String separator) throws Exception {

		List<String> fileNameList = new ArrayList<String>();
		long startTime = System.currentTimeMillis();
		int count = 1;
		BufferedReader reader = null;
		BufferedWriter write = null;
		File orignFile = new File(file);
		if (!orignFile.exists()) {
			throw new Exception("File not exist " + file);
		}
		try {
			String dir = file.substring(0, file.lastIndexOf(File.separator))
					+ File.separator;
			String fileName = file
					.substring(file.lastIndexOf(File.separator) + 1);
			int indexNum = fileName.lastIndexOf('_');
			if (indexNum < 0) {
				throw new Exception(fileName
						+ " is illegal,format like XXX_table_date_seq.txt.");
			}
			String firstPart = fileName.substring(0, indexNum);
			String lastPart = fileName.substring(indexNum);
			String seq = lastPart.substring(0, lastPart.indexOf('.'));
			indexNum = firstPart.lastIndexOf('_');
			if (indexNum < 0) {
				throw new Exception(fileName
						+ " is illegal,format like XXX_table_date_seq.txt.");
			}
			String datePart = firstPart.substring(indexNum + 1);
			String headPart = firstPart.substring(0, indexNum + 1);
			String fileNameDate = datePart.substring(0, 10);
			String fileDate = datePart.substring(0, 14);
			Map<String, String> createNewFile = new HashMap<String, String>();
			String newFileBegin = headPart + fileDate + seq + ".ods";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
			Date dateCur = sdf2.parse(fileDate);
			Calendar calendarRec = Calendar.getInstance();
			Calendar calendarCur = Calendar.getInstance();
			calendarCur.setTime(dateCur);
			String perFile = newFileBegin;
			reader = new BufferedReader(new FileReader(orignFile));
			String line = null;

			while ((line = reader.readLine()) != null) {
				if (existTitle && count == 1) {
					count++;
					continue;
				}
				try {
					String[] values = line.split(separator);
					String dateStr = values[index];
					Date date = sdf.parse(dateStr);
					calendarRec.setTime(date);
				} catch (Exception e1) {
					System.err.println(e1.getMessage());
					continue;
				}
				if (calendarRec.compareTo(calendarCur) < 0) {
					if (newFileBegin != perFile && write != null) {

						write.flush();
						write.close();
						write = null;
					}
					if (write == null) {
						if (createNewFile.get(fileNameDate) == null) {
							fileNameList.add(newFileBegin);
							createNewFile.put(fileNameDate, newFileBegin);
							System.out.println("Create new file "
									+ newFileBegin);
						}
						write = new BufferedWriter(new FileWriter(dir
								+ newFileBegin, true));
					}
					write.write(line.replaceAll(separator, "\001"));
					write.newLine();
					perFile = newFileBegin;
				} else {
					String date_rec = sdf2.format(calendarRec.getTime());
					String recFileDate = date_rec.substring(0, 10);
					String newFile = createNewFile.get(recFileDate);
					if (newFile == null) {
						newFile = headPart + date_rec + seq + ".ods";
						createNewFile.put(recFileDate, newFile);
						System.out.println("Create new file " + newFile);
					}
					if (!fileNameList.contains(newFile)) {
						fileNameList.add(newFile);
					}
					if (newFile != perFile && write != null) {
						write.flush();
						write.close();
						write = null;
					}
					if (write == null) {
						write = new BufferedWriter(new FileWriter(
								dir + newFile, true));
					}
					write.write(line.replaceAll(separator, "\001"));
					write.newLine();
					perFile = newFile;
				}

				count++;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (write != null) {
				try {
					write.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				write = null;
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				reader = null;
			}
		}
		long endTime = System.currentTimeMillis();
		long useTime = endTime - startTime;
		System.out.println(file + " speed " + count + ":" + useTime);
		return fileNameList;
	}

	/**
	 * ����ļ����Ƿ��Ϲ涨
	 * ����(\\w)+_((?:19|20)\\d\\d)(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])(0\\d|1\\d|2[0-3])(0\\d|[1-5]\\d)(0\\d|[1-5]\\d)_(\\d)+.([a-zA-Z0-9])+
	 * @param fileName �ļ���
	 * @throws Exception ������׳��쳣
	 */
	 public static void checkFileName(String fileName) throws Exception{
	    	Pattern pt = Pattern.compile("(\\w)+_((?:19|20)\\d\\d)(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])(0\\d|1\\d|2[0-3])(0\\d|[1-5]\\d)(0\\d|[1-5]\\d)_(\\d)+(.([a-zA-Z0-9])+)+");
	        Matcher m = pt.matcher(fileName);
	        if(!m.matches())
	        {
	        	throw new Exception("File name is illegal." + fileName);
	        }
	    }
}