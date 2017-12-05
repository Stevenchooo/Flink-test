package com.huawei.waf.core.run.request;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.slf4j.Logger;

import com.huawei.util.FileUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.IUploadProcessConfig;
import com.huawei.waf.core.config.method.process.UploadProcessInfo;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.Const;
import com.huawei.waf.protocol.RetCode;

public class UploadRequester extends RestRequester {
    private static final Logger LOG = LogUtil.getInstance();

    @Override
    public int buildParas(MethodContext context, Map<String, Object> reqParameters) {
        int retCode = super.buildParas(context, reqParameters);
        if(retCode != RetCode.OK) {
            return retCode;
        }
        
        HttpServletRequest req = context.getRequest();
        if(!ServletFileUpload.isMultipartContent(req)) {
            return context.setResult(RetCode.WRONG_PARAMETER, "Not a multi-part request");
        }
        
        String fileName, type;
        String fieldName;
        MethodConfig methodConfig = context.getMethodConfig();
        IUploadProcessConfig cfg = (IUploadProcessConfig)methodConfig.getProcessConfig();
        UploadProcessInfo upi = cfg.getUploadProcessInfo();
        FileItemFactory factory = upi.getFactory();
        List<FileItem> items = null;
        
        try {
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setFileSizeMax(upi.getMaxFileSize());
            upload.setSizeMax(upi.getMaxTotalSize());
            items = upload.parseRequest(new ServletRequestContext(req));
            for(FileItem item : items) {
                fieldName = item.getFieldName();
                if(item.isFormField()) {
                    reqParameters.put(fieldName, item.getString(Const.DEFAULT_ENCODING));
                    continue;
                }
                
                fileName = FileUtil.getFileName(item.getName());
                type = FileUtil.getFileExtension(fileName);
                if(!upi.isRightType(type)) {
                    return context.setResult(RetCode.WRONG_PARAMETER, "Type '" + type + "' is not a valid file type");
                }
                
                reqParameters.put(fieldName, new FileInfo(fileName, item));
            }
            retCode = RetCode.OK;
        } catch (FileSizeLimitExceededException e) {
            retCode = context.setResult(RetCode.WRONG_PARAMETER, "File size bigger than " + upi.getMaxFileSize());
        } catch (SizeLimitExceededException e) {
            retCode = context.setResult(RetCode.WRONG_PARAMETER, "Too large request size");
        } catch (Exception e) {
            LOG.error("Fail to get parameters from multi-request", e);
            retCode = context.setResult(RetCode.WRONG_PARAMETER, "Fail to handle upload file");
        } finally {
            if(retCode != RetCode.OK && items != null) {
                for(FileItem item : items) {
                    try {
                        item.delete();
                    } catch (Exception e) {
                        LOG.error("Fail to delete file item {}", item.getName(), e);
                    }
                }
            }
        }
        
        return retCode;
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void destroy() {
    }
    
    public static class FileInfo {
        public String fileName;
        public FileItem item;
        
        public FileInfo(String fileName, FileItem item) {
            this.fileName = fileName;
            this.item = item;
        }
    }
}
