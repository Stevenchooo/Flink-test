package routines;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
public class CommonUtil {

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
	public static void checkFileName(String fileName) throws Exception {
		RecordSeparate.checkFileName(fileName);
	}

	public static void checkFileExist(String file) throws Exception {
		File userFile = new File(file);
		if (!userFile.exists()) {
			throw new Exception("File not exist " + file);
		}
	}

	/**��ȡ�����
	 * @param fileName �ļ���
	 * @param d_hour Լ������ʱ��
	 * @return �շ���ʱ��
	 * @throws Exception �쳣
	 */
	public static String getPartitionDate(String fileName, String d_hour)
			throws Exception {
		System.out.println("Beging mention partition....");
		
		int index = fileName.lastIndexOf('_');
		if (index < 0) {
			throw new Exception(fileName
					+ "is illegal,format like XXX_table_date_seq.txt.");
		}
		String firstPart = fileName.substring(0, index);
		index = firstPart.lastIndexOf('_');
		if (index < 0) {
			throw new Exception(fileName
					+ "is illegal,format like XXX_table_date_seq.txt.");
		}
		String date = firstPart.substring(index + 1);
		String pt_d = date.substring(0, 8);

		String recordDateStr = date.substring(0, 14);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		String currentDateStr = pt_d;
		Date recordDate = sdf.parse(recordDateStr);
		Date currentDate = sdf.parse(currentDateStr + d_hour);
		Calendar recordCalendar = Calendar.getInstance();
		recordCalendar.setTime(recordDate);
		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.setTime(currentDate);
		// 02:00ǰ���ص�DB�ļ�����һ���ȫ��������ݣ����뵽ǰһ�����
		if (recordCalendar.compareTo(currentCalendar) <= 0) {
			currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
			pt_d = sdf2.format(currentCalendar.getTime());
		}
		return pt_d;
	}
}
