package com.huawei.waf.core.run;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
import org.slf4j.Logger;

import com.huawei.util.LogUtil;
import com.huawei.waf.facade.IBalancer;

/**
 * @author l00152046
 * 改进的一致性hash，本质是一个指定未知的“环”，当某个库负担过重时，可以在它的后面添置一个服务器，
 * 根据节点名称获得位置值，要求名称必须是xxx_nnnn，其中n是0-9的数字
 * nnnn指定的值就是节点在hash环上的位置
 */
public class ConsistentHashBalancer implements IBalancer {
    private static final Logger LOG = LogUtil.getInstance();
    private final TreeMap<Integer, String> circle = new TreeMap<Integer, String>();
    private Collection<String> nodes;

    /**
     * @param nodes 节点名称列表
     */
    public ConsistentHashBalancer() {
    }

    public void setNodes(Collection<String> nodes) {
        List<String> nodeList = new ArrayList<String>();
        int seg;
        for (String node : nodes) {
            seg = getServerHash(node);
            if(seg < 0) {
                LOG.info("{} is not a balance database", node);
                continue;
            }
            circle.put(seg, node);
            nodeList.add(node);
        }
        
        Collections.sort(nodeList, new Comparator<String>() {
            @Override
            public int compare(String v1, String v2)
            {
                int i1 = getServerHash(v1);
                int i2 = getServerHash(v2);
                
                return i1 - i2;
            }
        }); //排序，避免在调试时，出现与期望的不一致的顺序，顺序不影响业务
        this.nodes = nodeList;
    }
    
    public void remove(String node) {
        int seg = getServerHash(node);
        if(seg < 0) {
            LOG.info("{} is not a balance database", node);
            return;
        } 
        circle.remove(getServerHash(node));
    }

    @Override
    public String getNode(String key) {
        int hash = getHash(key);
        if(hash < 0) {
            return "";
        }
        
        if (!circle.containsKey(hash)) { //因为只读，所以不考虑多线程互斥
            Integer lowerKey = circle.lowerKey(hash); 

            if(lowerKey == null) {
                hash = circle.lastKey();
            } else {
                hash = lowerKey;
            }
        }
        
        return circle.get(hash);
    }

    /**
     * 计算hash值，可以重载此函数，使用不同的hash算法
     * @param val
     * @return
     */
    protected int getHash(String val) {
        if(val == null) {
            return -1;
        }
        int v = val.hashCode();
        return v >= 0 ? v : -v;
    }
    
    /**
     * 根据节点名称获得值，要求名称必须是xxx_nnnn，其中n是0-9的数字
     * nnnn指定的值就是节点在hash环上的位置
     * @param val
     * @return
     */
    protected int getServerHash(String val) {
        int pos = val.lastIndexOf('_');
        if(pos < 0) {
            return -1;
        }
        String n = val.substring(pos + 1);
        try {
            return Integer.valueOf(n);
        } catch(NumberFormatException  e) {
        }
        return -1;
    }

    @Override
    public Collection<String> getNodes() {
        return nodes != null ? nodes : new ArrayList<String>();
    }
//  public static void main(String[] args) throws InterruptedException {
//      final int N = 10000000;
//      int serverNum = 24;
//      final List<String> servers = new ArrayList<String>();
//      final Map<String, Integer> rec = new HashMap<String, Integer>(serverNum);
//      String header = "db_server_";
//      int interval = Integer.MAX_VALUE / serverNum;
//      for(int i = 0; i < serverNum; i++) {
//          servers.add(header + (i * interval));
//      }
//      
//      for(String srv : servers) {
//          rec.put(srv, 0);
//      }
//      final ConsistentHash ch = new ConsistentHash(servers);
//      
//      int threadNum = Runtime.getRuntime().availableProcessors();
//      ExecutorService pool = Executors.newFixedThreadPool(threadNum);
//      final CountDownLatch counter = new CountDownLatch(threadNum);
//      
//      long t0 = System.currentTimeMillis();
//      for(int i = 0; i < threadNum; i++) {
//          pool.execute(new Runnable() {
//              public void run() {
//                  Random r = new Random();
//                  int v;
//                  String srv;
//                  
//                  for(int i = 0; i < N; i++) {
//                      srv = ch.get(Integer.toString(r.nextInt()));
//                      v = rec.get(srv) + 1;
//                      rec.put(srv, v); //因为多线程的原因，最终求和会小一些
//                  }
//                  counter.countDown();
//              }
//          });
//      }
//      
//      counter.await();
//      
//      int total = 0;
//      for(String s : servers) {
//          int v = rec.get(s);
//          total += v;
//          System.out.println(s + "=" + v);
//      }
//      long t = System.currentTimeMillis() - t0;
//      
//      System.out.println("speed=" + (1000L * N * threadNum / t) + ",total=" + total + ",N*threadNum=" + (N * threadNum));
//      System.exit(0);
//  }
}
