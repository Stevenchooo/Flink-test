package routines;

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
public class ReturnUtil {

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
private String clientVer;
    
    private String network;
    
    private String operator;
    
    private String requestName;
    
    private String requestTime;
    
    private String responseName;
    
    private String responseTime;
    
    private String responseCode;

    public String getClientVer()
    {
        return clientVer;
    }

    public void setClientVer(String clientVer)
    {
        this.clientVer = clientVer;
    }

    public String getNetwork()
    {
        return network;
    }

    public void setNetwork(String network)
    {
        this.network = network;
    }

    public String getOperator()
    {
        return operator;
    }

    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getRequestName()
    {
        return requestName;
    }

    public void setRequestName(String requestName)
    {
        this.requestName = requestName;
    }

    public String getRequestTime()
    {
        return requestTime;
    }

    public void setRequestTime(String requestTime)
    {
        this.requestTime = requestTime;
    }

    public String getResponseName()
    {
        return responseName;
    }

    public void setResponseName(String responseName)
    {
        this.responseName = responseName;
    }

    public String getResponseTime()
    {
        return responseTime;
    }

    public void setResponseTime(String responseTime)
    {
        this.responseTime = responseTime;
    }

    public String getResponseCode()
    {
        return responseCode;
    }

    public void setResponseCode(String responseCode)
    {
        this.responseCode = responseCode;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(clientVer);
        builder.append("\001");
        builder.append(network);
        builder.append("\001");
        builder.append(operator);
        builder.append("\001");
        builder.append(requestName);
        builder.append("\001");
        builder.append(requestTime);
        builder.append("\001");
        builder.append(responseName);
        builder.append("\001");
        builder.append(responseTime);
        builder.append("\001");
        builder.append(responseCode);
        return builder.toString();
    }
}
