package com.huawei.waf.facade;

import java.util.TimerTask;

import com.huawei.waf.core.config.sys.TimerConfig;

public abstract class WAFTimerTask extends TimerTask {
	abstract public boolean init(TimerConfig tc);
}
