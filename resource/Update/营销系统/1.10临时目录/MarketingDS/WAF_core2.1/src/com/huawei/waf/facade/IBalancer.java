package com.huawei.waf.facade;

import java.util.Collection;

public interface IBalancer {
    public String getNode(String key);
    public Collection<String> getNodes();
    public void setNodes(Collection<String> nodes);
    public void remove(String node);
}
