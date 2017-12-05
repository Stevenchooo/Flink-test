package com.huawei.util;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheStoreHelper;
import net.sf.ehcache.Element;

import com.huawei.util.DBUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.DBUtil.DBQueryResult;
import com.huawei.util.Utils;
import com.huawei.waf.core.config.sys.WAFConfig;
import com.huawei.waf.protocol.RetCode;

public class CacheDBUtil extends LocalCacheUtil {
    private static final ConcurrentHashMap<String, Future<DBQueryResult>> syncTasks = new ConcurrentHashMap<String, Future<DBQueryResult>>();
    
	private static final Logger LOG = LogUtil.getInstance();
	private static Class<? extends CacheDBUtil> instanceClass = CacheDBUtil.class;
	private static CacheDBUtil instance = null; //单例
	
    private CacheFacade cacheFacade;
	
    /**
     * 返回cache对应的数据库的名称，默认为汇总库，应重载此函数返回不同的数据源名称
     * @return
     */
    protected String getCacheDBName() {
        return DBUtil.getCollectDbName();
    }
    
    /**
     * 设置工具类的真实实现类，默认为CacheDBUtil
     * @param instanceClass
     */
	public static void setInstanceClass(Class<? extends CacheDBUtil> cls) {
	    CacheDBUtil.instanceClass = cls;
	}
	
	/**
	 * 单例
	 * @return
	 */
	public static synchronized CacheDBUtil getInstance() {
	    if(instance == null) {
	        try {
                instance = instanceClass.newInstance();
            } catch (Exception e) {
                LOG.error("Fail to create instance of {}", instanceClass.getName(), e);
                return null;
            }
	        
	        if(instance.getCacheFacade() == null) {
	            //如果没有设置cacheFacade，则使用本地缓存作为默认缓存facade
	            instance.cacheFacade = new LocalCache();
	        }
	    }
	    return instance;
	}
	
	/**
	 * 通过重载此函数，改变cache的行为，默认使用LocalCache
	 * @return
	 */
	protected CacheFacade getCacheFacade() {
	    return cacheFacade;
	}
	
	/**
	 * 返回缓存数据，每隔一段时间重新读取数据库一次，其他时间从内存中读取
     * @param cf cache的facade
	 * @param sql 数据库查询
	 * @param parameters sql的参数，可以为null
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getList(CacheFacade cf, String sql, Object[] parameters) {
		String key = getSqlKey(sql, parameters, null);
		CacheElement ce = cf.get(key);
		List<Map<String, Object>> value = null;
		
		if(ce != null) {
		    value = (List<Map<String, Object>>)ce.value;
		    if(ce.valid) {
		        return value;
		    }
		}

		DBQueryResult result = syncQuery(key, sql, parameters);
		if(result == null || result.retCode != RetCode.OK) {
	        LOG.error("Fail to excuete {}", sql);
            return value == null ? new ArrayList<Map<String, Object>>() : value;//返回老数据，如果存在
		}
		
		List<Map<String,Object>> list = result.getResult(0);
		//加载成功才更换缓存的数据，否则用老的
		if(list != null && list.size() > 0) {
			cf.put(key, list);
			return list;
		}
		LOG.error("Fail to excuete {}, retCode={}", sql, result.retCode);
		
        return value == null ? new ArrayList<Map<String, Object>>() : value;//返回老数据，如果存在
	}
	
    /**
     * 返回配置数据，每隔一段时间重新读取数据库一次，其他时间从内存中读取
     * @param cf cache的facade
     * @param sql 数据库查询
     * @param parameters sql的参数，可以为null
     * @param cls 需要保存的对象类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends ObjCacheElement> List<T> getObjects(CacheFacade cf, String sql, Object[] parameters, Class<T> cls) {
        String key = getSqlKey(sql, parameters, null);
        CacheElement ce = cf.get(key);
        List<T> value = null;
        
        if(ce != null) {
            value = (List<T>)ce.value;
            if(ce.valid) {
                return value;
            }
        }

        DBQueryResult result = syncQuery(key, sql, parameters);
        if(result == null || result.retCode != RetCode.OK) {
            LOG.error("Fail to excuete {}", sql);
            return value == null ? new ArrayList<T>() : value;//返回老数据，如果存在
        }
        
        List<Map<String,Object>> list = result.getResult(0);
        //加载成功才更换缓存的数据，否则用老的
        if(list != null && list.size() > 0) {
            value = new ArrayList<T>(); 
            T one;
            int lineNo = 0; 
            
            for(Map<String, Object> line : list) {
                try {
                    one = cls.newInstance();
                    if(one.init(line)) {
                        value.add(one);
                    } else {
                        LOG.error("Fail to init data at line {}", lineNo);
                    }
                } catch (Exception e) {
                    LOG.error("Fail to create instance of {}", cls, e);
                }
                lineNo++;
            }
            cf.put(key, value);
            return value;
        }
        LOG.error("Fail to excuete {}, retCode={}", sql, result.retCode);
        
        return value == null ? new ArrayList<T>() : value;//返回老数据，如果存在
    }
    
	/**
	 * 获得map的map，指定colNames列为key，
	 * 如果colNames指定的列有重复值，则保存的是最后一个
     * @param cf cache的facade
	 * @param sql
	 * @param parameters
	 * @param colNames
     * @param cls 对象对应的class
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ObjCacheElement> Map<String, T> getObjects(CacheFacade cf, String sql, Object[] parameters, String[] colNames, Class<T> cls) {
        String key = getSqlKey(sql, parameters, colNames);
        CacheElement ce = cf.get(key);
        Map<String, T> value = null;
        
        if(ce != null) {
            value = (Map<String, T>)ce.value;
            if(ce.valid) {
                return value;
            }
        }

        DBQueryResult result = syncQuery(key, sql, parameters);
        if(result == null || result.retCode != RetCode.OK) {
            return value == null ? new HashMap<String, T>() : value;//返回老数据，如果存在
        }
        
        List<Map<String,Object>> list = result.getResult(0);
        //加载成功才更换缓存的数据，否则用老的
        if(list != null && list.size() > 0) {
            value = new HashMap<String, T>();
            T one;
            int lineNo = 0;
            String lineKey;
            
            for(Map<String, Object> line : list) {
                lineKey = getCompoundKey(line, colNames);
                one = value.get(lineKey);
                try {
                    if(one == null) {
                        one = cls.newInstance();
                    }
                    
                    if(one.init(line)) {
                        value.put(lineKey, one);
                    } else {
                        LOG.error("Fail to init object at line {}", lineNo);
                    }
                } catch (Exception e) {
                    LOG.error("Fail to create instance of {}", cls, e);
                }
                lineNo++;
            }
            cf.put(key, value);
            return value;
        }
        LOG.error("Fail to excuete {}, retCode={}", sql, result.retCode);
        
        return value == null ? new HashMap<String, T>() : value;//返回老数据，如果存在
	}
	
    /**
     * 获得map的map，指定colNames列为key，可以多列
     * 如果colNames指定的列有重复值，则保存的是最后一个
     * @param cf cache的facade
     * @param sql
     * @param parameters
     * @param colName
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Map<String, Object>> getMaps(CacheFacade cf, String sql, Object[] parameters, String[] colNames) {
        String key = getSqlKey(sql, parameters, colNames);
        CacheElement ce = cf.get(key);
        Map<String, Map<String, Object>> value = null;
        
        if(ce != null) {
            value = (Map<String, Map<String, Object>>)ce.value;
            if(ce.valid) {
                return value;
            }
        }

        DBQueryResult result = syncQuery(key, sql, parameters);
        if(result == null || result.retCode != RetCode.OK) {
            return value == null ? new HashMap<String, Map<String, Object>>() : value;//返回老数据，如果存在
        }
        
        List<Map<String,Object>> list = result.getResult(0);
        //加载成功才更换缓存的数据，否则用老的
        if(list != null && list.size() > 0) {
            value = new HashMap<String, Map<String, Object>>();
            for(Map<String, Object> line : list) {
                value.put(getCompoundKey(line, colNames), line);
            }
            cf.put(key, value);
            return value;
        }
        LOG.error("Fail to excuete {}, retCode={}", sql, result.retCode);
        
        return value == null ? new HashMap<String, Map<String, Object>>() : value;//返回老数据，如果存在
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, List<Map<String, Object>>> getLists(CacheFacade cf, String sql, Object[] parameters, String[] colNames) {
        String key = getSqlKey(sql, parameters, colNames);
        CacheElement ce = cf.get(key);
        Map<String, List<Map<String, Object>>> value = null;
        
        if(ce != null) {
            value = (Map<String, List<Map<String, Object>>>)ce.value;
            if(ce.valid) {
                return value;
            }
        }

        DBQueryResult result = syncQuery(key, sql, parameters);
        if(result == null || result.retCode != RetCode.OK) {
            return value == null ? new HashMap<String, List<Map<String, Object>>>() : value;//返回老数据，如果存在
        }
        
        List<Map<String,Object>> list = result.getResult(0);
        //加载成功才更换缓存的数据，否则用老的
        if(list != null && list.size() > 0) {
            String lineKey;
            List<Map<String, Object>> lines;
            
            value = new HashMap<String, List<Map<String, Object>>>();
            for(Map<String, Object> line : list) {
                lineKey = getCompoundKey(line, colNames);
                lines = value.get(lineKey);
                if(lines == null) {
                    lines = new ArrayList<Map<String, Object>>();
                }
                lines.add(line);
                value.put(lineKey, lines);
            }
            cf.put(key, value);
            return value;
        }
        LOG.error("Fail to excuete {}, retCode={}", sql, result.retCode);
        
        return value == null ? new HashMap<String, List<Map<String, Object>>>() : value;//返回老数据，如果存在
    }
    
	/**
	 * 获取string的列表
     * @param cf cache的facade
	 * @param sql
	 * @param parameters
     * @param colName 作为值得列名称
	 * @return
	 */
	public String[] getStrings(CacheFacade cf, String sql, Object[] parameters, String colName) {
	    String key = getSqlKey(sql, parameters, new String[]{colName});
	    
        CacheElement ce = cf.get(key);
        String[] value = null;
        
        if(ce != null) {
            value = (String[])ce.value;
            if(ce.valid) {
                return value;
            }
        }

        DBQueryResult result = syncQuery(key, sql, parameters);
        if(result == null || result.retCode != RetCode.OK) {
            LOG.info("Cache not hits, sql={}", sql);
            return value == null ? new String[0] : value;//返回老数据，如果存在
        }
        
        List<Map<String,Object>> list = result.getResult(0);
        //加载成功才更换缓存的数据，否则用老的
        if(list != null && list.size() > 0) {
            value = new String[list.size()];
            int no = 0;
            boolean specialCol = !Utils.isStrEmpty(colName);
            
            for(Map<String, Object> line : list) {
                if(specialCol) {
                    value[no++] = JsonUtil.getAsStr(line, colName, "");
                } else {
                    for(Map.Entry<String, Object> o : line.entrySet()) {
                        value[no++] = o.getValue().toString(); //只取第一个
                        break;
                    }
                }
            }
            cf.put(key, value);
            
            return value;
        }
        LOG.error("Fail to excuete {}, retCode={}", sql, result.retCode);
        
        return value == null ? new String[0] : value;//返回老数据，如果存在
	}
	
    /**
     * 获取string的列表，重复的使用最后一个
     * @param cf cache的facade
     * @param sql
     * @param parameters
     * @param colName 作为值得列名称
     * @return
     */
    @SuppressWarnings("unchecked")
    public Set<String> getStrSet(CacheFacade cf, String sql, Object[] parameters, String colName) {
        String key = getSqlKey(sql, parameters, new String[]{colName});
        
        CacheElement ce = cf.get(key);
        Set<String> value = null;
        
        if(ce != null) {
            value = (Set<String>)ce.value;
            if(ce.valid) {
                return value;
            }
        }

        DBQueryResult result = syncQuery(key, sql, parameters);
        if(result == null || result.retCode != RetCode.OK) {
            LOG.info("Cache not hits, sql={}", sql);
            return value == null ? new LinkedHashSet<String>() : value;//返回老数据，如果存在
        }
        
        List<Map<String,Object>> list = result.getResult(0);
        //加载成功才更换缓存的数据，否则用老的
        if(list != null && list.size() > 0) {
            value = new LinkedHashSet<String>();
            boolean specialCol = !Utils.isStrEmpty(colName);
            
            for(Map<String, Object> line : list) {
                if(specialCol) {
                    value.add(JsonUtil.getAsStr(line, colName, ""));
                } else {
                    for(Map.Entry<String, Object> o : line.entrySet()) {
                        value.add(o.getValue().toString()); //只取第一个
                        break;
                    }
                }
            }
            cf.put(key, value);
            
            return value;
        }
        LOG.error("Fail to excuete {}, retCode={}", sql, result.retCode);
        
        return value == null ? new LinkedHashSet<String>() : value;//返回老数据，如果存在
    }
    
    /**
     * 返回缓存数据，每隔一段时间重新读取数据库一次，其他时间从内存中读取
     * @param cf cache的facade
     * @param sql 数据库查询
     * @param parameters sql的参数，可以为null
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMap(CacheFacade cf, String sql, Object[] parameters) {
        String key = getSqlKey(sql, parameters, null);
        CacheElement ce = cf.get(key);
        Map<String, Object> value = null;
        
        if(ce != null) {
            value = (Map<String, Object>)ce.value;
            if(ce.valid) {
                return value;
            }
        }

        DBQueryResult result = syncQuery(key, sql, parameters);
        if(result == null || result.retCode != RetCode.OK) {
            return value == null ? new HashMap<String, Object>() : value;//返回老数据，如果存在
        }
        
        List<Map<String,Object>> list = result.getResult(0);
        //加载成功才更换缓存的数据，否则用老的
        if(list != null && list.size() > 0) {
            value = list.get(0);
            cf.put(key, value);
            return value;
        }
        LOG.error("Fail to excuete {}, retCode={}", sql, result.retCode);
        
        return value == null ? new HashMap<String, Object>() : value;//返回老数据，如果存在
    }
    
    /**
     * 获取至少两列的数据，全部放到一个map中，可以有多行
     * @param cf cache的facade
     * @param sql
     * @param parameters
     * @param keyCols 作为键的列，可以多列
     * @param valCol 作为值的列
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMap(CacheFacade cf, String sql, Object[] parameters, String[] keyCols, String valCol) {
        int keyNum;
        if(keyCols == null || (keyNum = keyCols.length) <= 0) {
            LOG.error("There are no keyCols prompted");
            return null;
        }
        String[] extensions = new String[keyNum + 1];
        System.arraycopy(keyCols, 0, extensions, 0, keyNum);
        extensions[keyNum] = valCol;
        
        String key = getSqlKey(sql, parameters, extensions);
        CacheElement ce = cf.get(key);
        Map<String, Object> value = null;
        
        if(ce != null) {
            value = (Map<String, Object>)ce.value;
            if(ce.valid) {
                return value;
            }
        }

        DBQueryResult result = syncQuery(key, sql, parameters);
        if(result == null || result.retCode != RetCode.OK) {
            LOG.error("Fail to excuete {}", sql);
            return value == null ? new HashMap<String, Object>() : value;//返回老数据，如果存在
        }
        
        List<Map<String,Object>> list = result.getResult(0);
        //加载成功才更换缓存的数据，否则用老的
        if(list != null && list.size() > 0) {
            value = new HashMap<String, Object>();
            for(Map<String, Object> line : list) {
                value.put(getCompoundKey(line, keyCols), line.get(valCol));
            }
            cf.put(key, value);
            return value;
        }
        LOG.error("Fail to excuete {}, retCode={}", sql, result.retCode);
        
        return value == null ? new HashMap<String, Object>() : value;//返回老数据，如果存在
    }
    
    /**
     * 创建数据库操作futuretask，可以重载此函数，调用不同的数据库操作，
     * 默认使用simpleQuery，不支持存储过程的调用
     * @param sql
     * @param parameters
     * @return
     */
    protected FutureTask<DBQueryResult> createFutureTask(final String sql, final Object[] parameters) {
        return new FutureTask<DBQueryResult>(new Callable<DBQueryResult>() {
            @Override
            public DBQueryResult call() throws Exception {
                return DBUtil.simpleQuery(getCacheDBName(), sql, parameters);
            }
        });
    }
    
    /**
     * 在多线程并发时，相同的key的情况下，保证只有第一个进入的线程执行，
     * 这样在cache过期时，保证数据库中不突然大批量执行相同的操作
     * @param key
     * @param sql
     * @param parameters
     * @return
     */
    protected DBQueryResult syncQuery(String key, String sql, Object[] parameters) {
        /**
         * 以下实现没有使用synchronized实现，如果synchronized，则所有没有命中的cacheKey都会在此堵塞，
         * 这里使用cacheKey值方式同步，只有相同cacheKey的线程才会在这里同步
         */
        Future<DBQueryResult> f = syncTasks.get(key);
        if (f == null) { //抢执行权，保证只有第一个抢到的执行，其他的都在get处等待
            FutureTask<DBQueryResult> ft = createFutureTask(sql, parameters);
            
            //不会对相同key的值进行覆盖，避免了相同key的任务被重复计算
            f = syncTasks.putIfAbsent(key, ft); 
            if (f == null) {
                ft.run(); //执行计算
                f = ft;
            }
        }
        
        try {
            /**
             * 使用future，让后进的线程再次等候，
             * 直到第一个抢到执行权的线程执行完毕。
             */
            DBQueryResult result = f.get();
            if(result == null) {
                LOG.info("Cache not hits, fail to get result");
            } else if(result.retCode != RetCode.OK) {
                LOG.info("Cache not hits, fail to get result, retCode={}", result.retCode);
            } else if(result.result == null || result.result.size() <= 0) {
                LOG.info("Cache not hits, fail to get result, no result");
            }
            return result;
        } catch (Exception e){
            LOG.error("Fail to get result from future task", e);
            return null;
        } finally {
            syncTasks.remove(key); //删除任务，下次进入时，任务重新开始
        }
    }
    
	public static final String[] getStrings(String sql, Object[] parameters, String colName) {
	    CacheDBUtil ins = getInstance();
		return ins.getStrings(ins.getCacheFacade(), sql, parameters, colName);
	}
	
    public static final String[] getStrings(String sql, Object[] parameters) {
        CacheDBUtil ins = getInstance();
        return ins.getStrings(ins.getCacheFacade(), sql, parameters, null);
    }
	
	public static final String[] getStrings(String sql, String colName) {
        CacheDBUtil ins = getInstance();
	    return ins.getStrings(ins.getCacheFacade(), sql, null, colName);
	}
	
    public static final String[] getStrings(String sql) {
        CacheDBUtil ins = getInstance();
        return ins.getStrings(ins.getCacheFacade(), sql, null, null);
    }
    
    public static final Set<String> getStrSet(String sql, Object[] parameters, String colName) {
        CacheDBUtil ins = getInstance();
        return ins.getStrSet(ins.getCacheFacade(), sql, parameters, colName);
    }
        
    public static final Set<String> getStrSet(String sql, String colName) {
        CacheDBUtil ins = getInstance();
        return ins.getStrSet(ins.getCacheFacade(), sql, null, colName);
    }
		
	public static final Map<String, Map<String, Object>> getMaps(String sql, String[] colNames) {
        CacheDBUtil ins = getInstance();
		return ins.getMaps(ins.getCacheFacade(), sql, null, colNames);
	}
	
    public static final <T extends ObjCacheElement> Map<String, T> getObjects(String sql, Object[] parameters, String[] colNames, Class<T> cls) {
        CacheDBUtil ins = getInstance();
        return ins.getObjects(ins.getCacheFacade(), sql, parameters, colNames, cls);
    }
    
    public static final Map<String, Map<String, Object>> getMaps(String sql, Object[] parameters, String[] colNames) {
        CacheDBUtil ins = getInstance();
        return ins.getMaps(ins.getCacheFacade(), sql, parameters, colNames);
    }
    
    public static final <T extends ObjCacheElement> List<T> getObjects(String sql, Object[] parameters, Class<T> cls) {
        CacheDBUtil ins = getInstance();
        return ins.getObjects(ins.getCacheFacade(), sql, parameters, cls);
    }
    
    /**
     * n分钟读取一次数据库，其他时间从内存中获取
     * @param name
     * @param sql
     * @param parameters
     * @return
     */
    public static final List<Map<String, Object>> getList(String sql, Object[] parameters) {
        CacheDBUtil ins = getInstance();
        return ins.getList(ins.getCacheFacade(), sql, parameters);
    }
    
    
    public static final List<Map<String, Object>> getList(String sql) {
        CacheDBUtil ins = getInstance();
        return ins.getList(ins.getCacheFacade(), sql, null);
    }
    
    public static final Map<String, List<Map<String, Object>>> getLists(String sql, Object[] parameters, String[] colNames) {
        CacheDBUtil ins = getInstance();
        return ins.getLists(ins.getCacheFacade(), sql, parameters, colNames);
    }
    
    public static final Map<String, List<Map<String, Object>>> getLists(String sql, String[] colNames) {
        CacheDBUtil ins = getInstance();
        return ins.getLists(ins.getCacheFacade(), sql, null, colNames);
    }
    
    public static final Map<String, Object> getMap(String sql, Object[] parameters) {
        CacheDBUtil ins = getInstance();
        return ins.getMap(ins.getCacheFacade(), sql, parameters);
    }
    
    public static final Map<String, Object> getMap(String sql) {
        CacheDBUtil ins = getInstance();
        return ins.getMap(ins.getCacheFacade(), sql, null);
    }
    
    public static final Map<String, Object> getMap(String sql, Object[] parameters, String[] keyCols, String valCol) {
        CacheDBUtil ins = getInstance();
        return ins.getMap(ins.getCacheFacade(), sql, parameters, keyCols, valCol);
    }
    
    public static final Map<String, Object> getMap(String sql, String[] keyCols, String valCol) {
        CacheDBUtil ins = getInstance();
        return ins.getMap(ins.getCacheFacade(), sql, null, keyCols, valCol);
    }
	
	/**
	 * 从多行的结果中获取某一行中某一列的值
	 * @param data
	 * @param line
	 * @param col
	 * @param defVal
	 * @return
	 */
	public static final Object getValue(Map<String, Map<String, Object>> data, String line, String col, Object defVal) {
		Map<String, Object> one = data.get(line);
		if(one == null) {
			return defVal;
		}
		return one.get(col);
	}
	
	/**
	 * @param sql
	 * @param parameters sql参数
	 * @param extensions 其他附加参数，如列名称
	 * @return
	 */
	public static final String getSqlKey(String sql, Object[] parameters, String[] extensions) {
        StringBuilder keyBuilder = new StringBuilder(sql);

    	if(parameters != null && parameters.length > 0) {
	    	for(Object o : parameters) {
                if(o != null) {
                    keyBuilder.append('&').append(o.toString());
                }
            }
    	}
    	
        if(extensions != null && extensions.length > 0) {
            for(String s : extensions) {
                if(s != null) {
                    keyBuilder.append('&').append(s);
                }
            }
        }
        
        return Utils.md5_base64(keyBuilder.toString());
	}
	
    /**
     * 根据列名称为line生成一个联合的key
     * @param line
     * @param cols 列名称列表
     * @return
     */
    public static final String getCompoundKey(Map<String, Object> line, String[] cols) {
        if(cols.length == 1) {
            return JsonUtil.getAsStr(line, cols[0]);
        }
        
        String ss = "";
        for(String col : cols) {
            ss += '_' + JsonUtil.getAsStr(line, col, "");
        }
        
        return Utils.md5_base64(ss);
    }
    
    /**
     * 根据多列的值生成一个联合key
     * @param colVals
     * @return
     */
    public static final String getCompoundKey(String[] colVals) {
        if(colVals.length == 1) {
            return colVals[0];
        }
        
        String ss = "";
        for(String col : colVals) {
            ss += '_' + col;
        }
        
        return Utils.md5_base64(ss);
    }
    
    /**
     * @author l00152046
     * 用于返回cache的值，屏蔽EHCache与LocalCache之间的差异
     */
    public static class CacheElement {
        public final boolean valid;
        public final Object value;
        
        public CacheElement(boolean valid, Object value) {
            this.valid = valid;
            this.value = value;
        }
    }
    
	public static interface CacheFacade {
	    public void put(String key, Object value);
        public CacheElement get(String key);
	}
	
    public static interface ObjCacheElement {
        /**
         * 将map中的数据设置到对象属性中
         * @param map
         * @return
         */
        public boolean init(Map<String, Object> map);
    }
    
	public static class LocalCache implements CacheFacade {
	    private final Map<String, LocalCacheEle> cache;
	    private final int validTime;
        
	    public LocalCache() {
            this(WAFConfig.getLocalCacheTime());
        }
	    
	    public LocalCache(int validTime) {
	        this.cache = localCache;
	        this.validTime = validTime;
	    }
	    
        @Override
        public void put(String key, Object value) {
            cache.put(key, new LocalCacheEle(value, validTime));
        }

        @Override
        public CacheElement get(String key) {
            LocalCacheEle lce = cache.get(key);
            if(lce != null) {
                return new CacheElement(!lce.isExpired(), lce.getData());
            }
            return null;
        }
	}
	
    public static class EhCache implements CacheFacade {
        private final Cache cache;
        private final CacheStoreHelper csh;
        
        public EhCache(Cache cache) {
            this.cache = cache;
            this.csh = new CacheStoreHelper(cache);
        }
        
        @Override
        public void put(String key, Object value) {
            cache.put(new Element(key, value));
        }

        @Override
        public CacheElement get(String key) {
            Element e = csh.getStore().get(key);
            if(e != null) {
                boolean valid = !cache.isExpired(e); 
                if(valid) {
                    cache.get(key); //有效的情况下，通过调用get方法更新统计信息 
                }
                return new CacheElement(valid, e.getObjectValue());
            }
            return null;
        }
    }
}
