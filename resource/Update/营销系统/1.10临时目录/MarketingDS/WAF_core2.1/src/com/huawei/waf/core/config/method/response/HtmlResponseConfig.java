package com.huawei.waf.core.config.method.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.protocol.RetCode;

public class HtmlResponseConfig extends RDBResponseConfig {
    private static final Logger LOG = LogUtil.getInstance();
    private static final String CONFIG_TEMPLATE = "template";
    private static final String CONFIG_ERRORPAGES = "errorPages";
    
    private String template = null;
    private ErrorPage[] errorPages = null; 

    @Override
    protected boolean parseExt(String version, Map<String, Object> json, MethodConfig mc) {
        if(!super.parseExt(version, json, mc)) {
            return false;
        }
        
        template = JsonUtil.getAsStr(json, CONFIG_TEMPLATE, null);
        if(Utils.isStrEmpty(template)) {
            LOG.warn("Should set {} when html response", CONFIG_TEMPLATE);
            return false;
        }
        
        List<Object> errs = JsonUtil.getAsList(json, CONFIG_ERRORPAGES);
        if(errs != null) {
            int num = errs.size();
            List<ErrorPage> errPgs = new ArrayList<ErrorPage>();
            for(int i = 0; i < num; i++) {
                Map<String, Object> c = JsonUtil.getAsObject(errs, i);
                ErrorPage ep = ErrorPage.parse(c);
                if(ep == null) {
                    LOG.error("Fail to parse {}", CONFIG_ERRORPAGES);
                    return false;
                }
                errPgs.add(ep);
            }
            errorPages = errPgs.toArray(new ErrorPage[errPgs.size()]);
        }
        
        return true;
    }
    
    public ErrorPage[] getErrorPages() {
        return errorPages;
    }
    
    public String getTemplate(int retCode) {
        if(retCode == RetCode.OK || errorPages == null) {
            return template;
        }
        for(ErrorPage ep : errorPages) {
            if(ep.isCodeIn(retCode)) {
                return ep.getTemplate();
            }
        }
        return template;
    }
    
    public static abstract class ErrorPage {
        protected static final String CFG_CODE  = "code";
        protected static final String CFG_START = "start";
        protected static final String CFG_END   = "end";
        protected static final String CFG_LIST  = "list";
        
        private String template;
        
        abstract boolean isCodeIn(int code);
        abstract boolean parseExt(Map<String, Object> json);
        
        static final ErrorPage parse(Map<String, Object> json) {
            if(!json.containsKey(CONFIG_TEMPLATE)) {
                LOG.warn("You should set {} in error pages", CONFIG_TEMPLATE);
                return null;
            }
            
            ErrorPage errorPage = null;
            if(json.containsKey(CFG_CODE)) {
                errorPage = new CodeErrorPage();
            } else if(json.containsKey(CFG_START)) {
                errorPage = new ScopeErrorPage();
            } else if(json.containsKey(CFG_LIST)) {
                errorPage = new ListErrorPage();
            } else {
                LOG.error("{},{} or {} should be set in error-page", CFG_CODE, CFG_START, CFG_LIST);
                return null;
            }
            if(errorPage.parseExt(json)) {
                errorPage.template = JsonUtil.getAsStr(json, CONFIG_TEMPLATE);
                return errorPage;
            }
            return null;
        }
        
        public String getTemplate() {
            return template;
        }
    }
    
    private static class CodeErrorPage extends ErrorPage {
        int code = RetCode.INTERNAL_ERROR;
        
        @Override
        boolean isCodeIn(int code) {
            return code == this.code;
        }

        @Override
        boolean parseExt(Map<String, Object> json) {
            this.code = JsonUtil.getAsInt(json, CFG_CODE, RetCode.INTERNAL_ERROR);
            return true;
        }
    }
    
    private static class ScopeErrorPage extends ErrorPage {
        int start = 1;
        int end = Integer.MAX_VALUE;
        
        @Override
        boolean isCodeIn(int code) {
            return code >= start && code <= end;
        }

        @Override
        boolean parseExt(Map<String, Object> json) {
            this.start = JsonUtil.getAsInt(json, CFG_START, start);
            this.end = JsonUtil.getAsInt(json, CFG_END, end);
            if(this.end > this.start) {
                return true;
            }
            LOG.error("End {} should be bigger than start {} ", end, start);
            return false;
        }
    }
    
    private static class ListErrorPage extends ErrorPage {
        int[] list = null;
        
        @Override
        boolean isCodeIn(int code) {
            for(int c : list) {
                if(c == code) {
                    return true;
                } 
            }
            return false;
        }

        @Override
        boolean parseExt(Map<String, Object> json) {
            List<Object> l = JsonUtil.getAsList(json, CFG_LIST);
            if(l == null || l.size() <= 0) {
                LOG.error("Wrong error-page config, there are no list");
                return false;
            }
            
            list = new int[l.size()];
            int i = 0;
            for(Object o : l) {
                list[i++] = Utils.parseInt(o, -1);
            }
            return true;
        }
    }
}
