package com.huawei.waf.core.config.method.response;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.response.HtmlResponser;
import com.huawei.waf.facade.config.AbstractResponseConfig;

public class StaticHtmlResponseConfig extends AbstractResponseConfig {
    private static final Logger LOG = LogUtil.getInstance();
    private static final String CONFIG_HTML = "html";
    private static final String CONFIG_CACHETIME = "cacheTime";
    
    private File html = null;
    private int cacheTime = 300; //缓存时间，单位秒 

    @Override
    protected boolean parseExt(String version, Map<String, Object> json, MethodConfig mc) {
    	String htmlName = JsonUtil.getAsStr(json, CONFIG_HTML, null);
        if(Utils.isStrEmpty(htmlName)) {
            LOG.error("Should set {} when static-html response", CONFIG_HTML);
            return false;
        }
        
        if(htmlName.charAt(0) == '/') {
        	htmlName = htmlName.substring(1);
        }

        this.html = new File(HtmlResponser.getHtmlPath(), htmlName);
        if(!this.html.exists()) {
        	LOG.error("{} not exists", this.html);
        	return false;
        }
        
        this.cacheTime = JsonUtil.getAsInt(json, CONFIG_CACHETIME, this.cacheTime) * 1000;
        
        return true;
    }
    
    public File getHtml() {
        return html;
    }
    
    public int getCacheTime() {
    	return cacheTime;
    }
}
