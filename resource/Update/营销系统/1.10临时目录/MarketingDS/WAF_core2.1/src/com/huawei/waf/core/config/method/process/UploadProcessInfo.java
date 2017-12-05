package com.huawei.waf.core.config.method.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import org.apache.tomcat.util.http.fileupload.FileCleaningTracker;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;

public class UploadProcessInfo {
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final String PARAMETER_TODIR = "toDir";
    private static final String PARAMETER_MAX_MEMSIZE = "maxMemSize"; //bytes
    private static final String PARAMETER_MAX_FILESIZE = "maxFileSize"; //bytes
    private static final String PARAMETER_MAX_TOTALSIZE = "maxTotalSize"; //bytes
    private static final String PARAMETER_FILETYPE = "type"; //文件扩展名，可以多个，用逗号分隔
    
    private List<String> fileTypes = null;
    private DiskFileItemFactory factory = new DiskFileItemFactory();
    private long maxFileSize = -1; //无限制
    private long maxTotalSize = -1; //无限制
    
    public static UploadProcessInfo parse(String ver, Map<String, Object> json, MethodConfig mc) {
        UploadProcessInfo upi = new UploadProcessInfo();
        String toDir = JsonUtil.getAsStr(json, PARAMETER_TODIR, null);
        if(!Utils.isStrEmpty(toDir)) {
            try {
                File dir = new File(toDir);
                if(!dir.exists()) {
                    if(!dir.mkdirs()) {
                        LOG.error("Fail to make upload temporary directory {}", toDir);
                        return null;
                    }
                }
                upi.factory.setRepository(dir);
            }catch(Exception e) {
                LOG.error("Fail to init upload temporary directory {}", toDir, e);
                return null;
            }
        }
  
        /**
         * 在配置中已创建DiskFileItemFactory对象，通过它来解析请求，以及临时文件的删除
         */
        upi.factory.setSizeThreshold(JsonUtil.getAsInt(json, PARAMETER_MAX_MEMSIZE, DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD));
        //upi.factory.setFileCleaningTracker(new FileCleaningTracker()); //自动删除, tomcat8不支持
        
        upi.maxFileSize = JsonUtil.getAsLong(json, PARAMETER_MAX_FILESIZE, -1L);
        upi.maxTotalSize = JsonUtil.getAsLong(json, PARAMETER_MAX_TOTALSIZE, -1L);
        if(upi.maxTotalSize != -1 && (upi.maxFileSize > upi.maxTotalSize)) {
            LOG.error("{} should be less than {}", PARAMETER_MAX_FILESIZE, PARAMETER_MAX_TOTALSIZE);
            return null;
        }
        
        String str = JsonUtil.getAsStr(json, PARAMETER_FILETYPE, null);
        if(!Utils.isStrEmpty(str)) {
            String[] ss = str.split(",");
            if(ss.length > 0) {
	            upi.fileTypes = new ArrayList<String>();
	            for(String s : ss) {
	                s = s.trim();
	                if(!Utils.isStrEmpty(s)) {
	                    upi.fileTypes.add(s);
	                }
	            }
            }
        }
        
        return upi;
    }
    
    public long getMaxFileSize() {
        return maxFileSize;
    }
    
    public long getMaxTotalSize() {
        return maxTotalSize;
    }
    
    public FileItemFactory getFactory() {
        return factory;
    }
    
    public boolean isRightType(String type) {
        if(fileTypes == null) { //不限制
            return true;
        }
        
        if(Utils.isStrEmpty(type)) {
            return false;
        }
        
        return fileTypes.contains(type);
    }
}
