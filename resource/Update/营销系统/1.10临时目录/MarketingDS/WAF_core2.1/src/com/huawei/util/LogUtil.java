package com.huawei.util;


import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.JoranConfiguratorBase;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.sys.WAFConfig;

public class LogUtil {
    private static final String FQCN = LogUtil.class.getName();
    
	private LogUtil() {
	}
	
	public static boolean init(File cfgFile) {
	    if(!cfgFile.exists()) {
	        System.out.println(cfgFile.getAbsolutePath() + " not exists");
	        return false;
	    }
	    
	    if(!cfgFile.isFile()) {
            System.out.println(cfgFile.getAbsolutePath() + " is not a config file");
            return false;
	    }
        try {
    	    LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
            
            JoranConfiguratorBase configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            configurator.doConfigure(cfgFile);
            StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
        } catch (JoranException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public static Logger getInstance() {
		return getInstance(getClassName());
	}
	
    public static Logger getInstance(String name) {
        return LoggerFactory.getLogger(name);
    }
    
	private static final String getClassName() {
		String logClass;
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		for(int i = 2; i < trace.length; i++) {
			logClass = trace[i].getClassName();
			if(!logClass.equals(FQCN)) {
				return logClass; //返回堆栈中第一个不是当前类的类名
			}
		}
		return FQCN;
	}
	
	public static void logUtilException(Logger LOG, Throwable e, String format, Object...parameters) {
	    if(WAFConfig.isUtilLogException()) {
	        LOG.error(format, parameters, e);
	    } else {
            LOG.error(format, parameters);
	    }
	}
}
