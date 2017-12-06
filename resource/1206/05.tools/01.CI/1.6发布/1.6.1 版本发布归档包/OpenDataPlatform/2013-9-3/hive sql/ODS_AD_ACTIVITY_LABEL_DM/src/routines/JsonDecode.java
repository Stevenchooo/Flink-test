package routines;

import net.sf.json.JSONObject;

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
public class JsonDecode {

	//输入key和json字符串，解析得到key对应的value
    public static String getJsonValue(String key,String json)
    {
    	//如果不存在json字符串，则返回长度为0的字符串,此举的目的为和portal统一，如{uid:'',uacc:'',imei:'',tt:''}解析出来的uid为""统一
        if(null == json || "" == json || "null" == json || "NULL" == json)
        {
        	String a = "";
        	return a;
        }
        else
        {
        	try
            {
                JSONObject jsonObj;
                jsonObj = JSONObject.fromObject(json);
                String a = jsonObj.getString(key);
                return a;
            }
            catch (Exception e)
            {
            	//解析异常则返回"error"，用来识别portal端JS的错误
                return "";
            }	
        }
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
}
