import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.huawei.manager.mkt.info.AdDicMapInfo;
import com.huawei.manager.mkt.info.AdTemplateInfo;
import com.huawei.manager.mkt.info.LandTemplateInfo;
import com.huawei.manager.mkt.util.AdTemplateFileUtils;
import com.huawei.manager.mkt.util.LandTemplateFileUtils;
import com.huawei.manager.mkt.util.StringUtils;
import com.huawei.manager.mkt.util.checkUtil.AdInfoTemplateCheck;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-8]
 * @see  [相关类/方法]
 */
public class FieldCheckTest
{
    
    @Before
    public void setUp()
        throws Exception
    {
    }
    
    @After
    public void tearDown()
        throws Exception
    {
    }
    
    @Test
    public void test()
    {
//        String channel =
//            "11111111111111kkkkkkkkkkkzzzzzzzzzzzzzzzzddddddddddddddddddddddddddddddddddddddddddddddddddddddddpp";
//        FieldCheckInfo channelCheckInfo = StringUtils.checkField(channel, true, 100);
//        System.out.println(channelCheckInfo);
        
        boolean falg = Boolean.valueOf("ab");
        System.out.print(falg);
    }
    
    @Test
    public void test1()
    {
        AdTemplateInfo info = new AdTemplateInfo();
        
        //info.setDeliveryTimes("20150601-20150635");
        info.setDeliveryTimes("20150630-20150601");
        StringBuffer buffer = new StringBuffer();
        AdInfoTemplateCheck.checkDeliveryTimesField(info, buffer);
        System.out.println(buffer);
    }
    
    @Test
    public void test2()
    {
        String url = "https://www.baidu.com cid=302";
        boolean flag = StringUtils.isUrl(url);
        System.out.println(flag);
        url = "https://www.baidu.com  cid=302";
        flag = StringUtils.isUrl(url);
        System.out.println(flag);
    }
    
    @Test
    public void test3()
    {
        List<LandTemplateInfo> list = LandTemplateFileUtils.readTemplateFile("D:" + File.separator + "test.xls", true);
        for (LandTemplateInfo info : list)
        {
            System.out.println(info);
        }
    }
    
    @Test
    public void test4()
    {
        List<AdTemplateInfo> list = AdTemplateFileUtils.readTemplateFile("D:" + File.separator + "test3.xls");
        
        for (AdTemplateInfo info : list)
        {
            StringBuffer buffer = new StringBuffer();
            Connection conn = null;
            AdDicMapInfo dicMap = new AdDicMapInfo();
            //AdTemplateFileUtils.checkAdTemplateInfo(conn, dicMap, info, buffer);
            System.out.println(info);
            System.out.println(buffer);
        }
    }
    
    @Test
    public void test5()
    {
        AdTemplateInfo info = new AdTemplateInfo();
        info.setPublishPriceStr("2147483648");
        StringBuffer buffer = new StringBuffer();
        AdInfoTemplateCheck.checkPublishPriceField(info, buffer);
        System.out.println(buffer);
        Float float1 = Float.valueOf("9.999999999E9");
        System.out.println(float1);
        
        Float float2 = 9999999999f;
        System.out.println(float2.toString());
    }
    
    @Test
    public void test6()
    {
        String s = "2147483648";
        boolean flag = StringUtils.isValidInt(s);
        System.out.println(flag);
    }
    
    @Test
    public void test7()
    {
        
        AdTemplateInfo info = new AdTemplateInfo();
        info.setNetPriceStr("2147483648");
        info.setPublishPriceStr("2147483648");
        StringBuffer buffer = new StringBuffer();
        AdInfoTemplateCheck.checkNetPriceField(info, buffer);
        AdInfoTemplateCheck.checkPublishPriceField(info, buffer);
        System.out.println(buffer);
        
        
        File file = new File("D:" + File.separator + "xxx.txt");
        Collection<String> lines = new ArrayList<String>();
        String s = "a" + "\002" + "b";
        lines.add(s);
        try
        {
            FileUtils.writeLines(file, lines);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
}
