package com.huawei.util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.huawei.waf.protocol.Const;

public class JsonUtil {
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 把map或者是list转换成json字符串
     * @param map
     * @return 返回一个json
     */
    public static <T> byte[] mapToBytes(T obj){
        try{
            return JSON.toJSONBytes(obj);
        }catch(Exception e){
            LOG.error("Fail to convert into json", e);
            return null;
        }
    }
    
    /**
     * 把map或者是list转换成json字符串
     * @param map
     * @return 返回一个json
     */
    public static <T> String mapToJson(T obj){
        try{
            return JSON.toJSONString(obj);
        }catch(Exception e){
            LOG.error("Fail to convert into json", e);
            return null;
        }
    }


    /**
     * 把json字符串转成HashMap对象
     * @param jsonBytes json值
     * @return 返回一个HashMap对象
     */
    public static Map<String, Object> jsonToMap(byte[] jsonBytes){
        try{
            return JSON.parseObject(new String(jsonBytes, Const.DEFAULT_CHARSET), new TypeReference<Map<String,Object>>(){});
        } catch(Exception e){
            LOG.error("Fail to parse json bytes", e);
            return null;
        }
    }
    
    /**
     * 把json字符串转成HashMap对象
     * @param jsonStr json值
     * @return 返回一个HashMap对象
     */
    public static Map<String, Object> jsonToMap(String jsonStr){
        try{
            return JSON.parseObject(jsonStr, new TypeReference<Map<String,Object>>(){});
        } catch(Exception e){
            LOG.error("Fail to parse json string:{}", jsonStr, e);
            return null;
        }
    }
    
    /**
     * 把json字符串转成List对象
     * @param jsonStr json值
     * @return 返回一个List
     */
    public static List<Object> jsonToList(byte[] jsonBytes){
        try{
            return JSON.parseObject(new String(jsonBytes, Const.DEFAULT_CHARSET), new TypeReference<List<Object>>(){});
        } catch(Exception e){
            LOG.error("Fail to parse json bytes", e);
            return null;
        }
    }
    
    public static List<Object> jsonToList(String jsonStr){
        try{
            return JSON.parseObject(jsonStr, new TypeReference<List<Object>>(){});
        } catch(Exception e){
            LOG.error("Fail to parse json string:{}", jsonStr, e);
            return null;
        }
    }
    
    /**
     * 把json文件转成HashMap对象
     * @param jsonStr json值
     * @return 返回一个HashMap对象
     */
    public static Map<String, Object> jsonFileToMap(File jsonFile){
        try{
        	byte[] content = FileUtil.readFile(jsonFile);
        	if(content == null || content.length <= 0) {
        		return null;
        	}
        	String s = removeComment(new String(content, Const.DEFAULT_CHARSET));
            return jsonToMap(s);
        } catch(Exception e){
            LOG.error("Fail to parse json file:{}", jsonFile.getAbsolutePath(), e);
            return null;
        }
    }
    
    public static List<Object> jsonFileToList(File jsonFile){
        try{
        	byte[] content = FileUtil.readFile(jsonFile);
        	if(content == null || content.length <= 0) {
        		return null;
        	}
        	String s = removeComment(new String(content, Const.DEFAULT_CHARSET));
            return JSON.parseObject(s, new TypeReference<List<Object>>(){});
        } catch(Exception e){
            LOG.error("Fail to parse json file:{}", jsonFile.getAbsolutePath(), e);
            return null;
        }
    }
    
	enum CommentState {NORMAL, COMMMENT_START, MULTI_COMMENT, SIGNLE_COMMENT, COMMENT_ENDING, COMMENT_END, STR, SINGLE_STR};
    /**
     * 删除与C/C++/JAVA类似语法的注释内容
     * 用于删除json文档中的注释内容
     * @param s
     * @return
     */
    public static String removeComment(String s) {
    	int len = s.length();
    	StringBuilder sb = new StringBuilder(len);
    	char c;
    	CommentState state = CommentState.NORMAL;
    	
    	for(int i = 0; i < len; i++) {
    		c = s.charAt(i);
    		switch(c) {
    		case '/':
    			if(state == CommentState.NORMAL) {
    				state = CommentState.COMMMENT_START;
    			} else if(state == CommentState.COMMMENT_START){
    				state = CommentState.SIGNLE_COMMENT;
    			} else if(state == CommentState.COMMENT_ENDING){
    				state = CommentState.COMMENT_END;
    			}
    			break;
    		case '*':
    			if(state == CommentState.COMMMENT_START) {
    				state = CommentState.MULTI_COMMENT;
    			} else if(state == CommentState.MULTI_COMMENT){
    				state = CommentState.COMMENT_ENDING;
    			}
    			break;
    		case '\r':
    		case '\n':
    			if(state == CommentState.SIGNLE_COMMENT) {
    				state = CommentState.NORMAL;
    			} else if(state == CommentState.COMMENT_ENDING){
    				state = CommentState.MULTI_COMMENT;
    			}
    			break;
    		case '"':
    			if(state == CommentState.NORMAL) {
    				state = CommentState.STR;
    			} else if(state == CommentState.STR) {
    				state = CommentState.NORMAL;
    			}
    			break;
    		case '\'':
    			if(state == CommentState.NORMAL) {
    				state = CommentState.SINGLE_STR;
    			} else if(state == CommentState.SINGLE_STR) {
    				state = CommentState.NORMAL;
    			}
    			break;
    		default:
				if(state == CommentState.COMMENT_ENDING){
					state = CommentState.MULTI_COMMENT;
				}
				break;
    		}
    		
    		if(state == CommentState.NORMAL
    		   || state == CommentState.STR
    		   || state == CommentState.SINGLE_STR) {
    			sb.append(c);
    		} else if(state == CommentState.COMMENT_END) {
    			state = CommentState.NORMAL;
    		}
    	}
    	
    	return sb.toString();
    }
    
    public static Map<String, Object> jsonStreamToMap(InputStream in){
        return jsonStreamToMap(in, false);
    }
    
    public static Map<String, Object> jsonStreamToMap(InputStream in, boolean strict){
        byte[] content = FileUtil.readStream(in);
        if(content == null || content.length <= 0) {
        	return new HashMap<String, Object>();
        }
        return jsonToMap(new String(content, Const.DEFAULT_CHARSET));
    }
    //-------------------------------------------------------------------------
    public static String getAsStr(Map<String, Object> map, String name) {
        return Utils.parseString(map.get(name), "");
    }
    
    public static String getAsStr(Map<String, Object> map, String name, String defVal) {
        return Utils.parseString(map.get(name), defVal);
    }
    
    public static int getAsInt(Map<String, Object> map, String name) {
    	return Utils.parseInt(map.get(name), 0);
    }
    
    public static int getAsInt(Map<String, Object> map, String name, int def) {
        return Utils.parseInt(map.get(name), def);
    }
    
    public static long getAsLong(Map<String, Object> map, String name) {
    	return Utils.parseLong(map.get(name), 0L);
    }
    
    public static long getAsLong(Map<String, Object> map, String name, long defVal) {
    	return Utils.parseLong(map.get(name), defVal);
    }

    public static float getAsFloat(Map<String, Object> map, String name) {
    	return Utils.parseFloat(map.get(name), 0f);
    }
    
    public static float getAsFloat(Map<String, Object> map, String name, float defVal) {
    	return Utils.parseFloat(map.get(name), defVal);
    }
    
    public static double getAsDouble(Map<String, Object> map, String name) {
    	return Utils.parseDouble(map.get(name), 0);
    }
    
    public static double getAsDouble(Map<String, Object> map, String name, double defVal) {
    	return Utils.parseDouble(map.get(name), defVal);
    }
    
    public static boolean getAsBool(Map<String, Object> map, String name) {
    	return Utils.parseBool(map.get(name), false);
    }
    
    public static boolean getAsBool(Map<String, Object> map, String name, boolean defVal) {
    	return Utils.parseBool(map.get(name), defVal);
    }
    
    @SuppressWarnings("unchecked")
    public static List<Object> getAsList(Map<String, Object> map, String name) {
        Object o = map.get(name);
        if(o == null) {
            return null;
        }
        
        if(o instanceof List<?>) {
        	return (List<Object>)o;
        }
        
        return null;
    }
    
    /**
     * 返回一个string的set，重复的被覆盖
     * @param map
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> getAsStrList(Map<String, Object> map, String name) {
        Object o = map.get(name);
        if(o == null || !(o instanceof List<?>)) {
            return null;
        }
        
        List<String> ss = new ArrayList<String>();
        List<Object> list = (List<Object>)o;
        for(Object l : list) {
            ss.add(Utils.parseString(l, ""));
        }
        
        return ss;
    }
    
    /**
     * 返回一个string的set，重复的被覆盖
     * @param map
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Set<String> getAsStrSet(Map<String, Object> map, String name) {
        Object o = map.get(name);
        if(o == null || !(o instanceof List<?>)) {
            return null;
        }
        
        Set<String> ss = new HashSet<String>();
        List<Object> list = (List<Object>)o;
        for(Object l : list) {
            ss.add(Utils.parseString(l, ""));
        }
        
        return ss;
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getAsObject(List<Object> list, int i) {
        if(i < 0 || i >= list.size()) {
            return null;
        }
        return (Map<String, Object>)list.get(i);
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getAsObject(Map<String, Object> map, String name) {
        Object o = map.get(name);
        if(o == null) {
            return null;
        }
        
        if(o instanceof Map<?, ?>) {
        	return (Map<String, Object>)o;
        }
        
        return null;
    }
    
    /**
     * 在一个多级的map中获取对象，多级以“.”分隔
     * @param map
     * @param name 对象名称，多级以"."分隔
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object getObject(Map<String, Object> map, String name) {
        String[] ss = name.split("\\.");
        int num = ss.length;
        
        Object o;
        Map<String, Object> subMap = map;
        
        for(int i = 0; ;){
            o = subMap.get(ss[i]);
            if(o == null) {
                return null;
            }
            
            if(i == num - 1) {
                return o;
            }
            
            if(o instanceof Map<?,?>) {
                subMap = (Map<String, Object>)o;
            }
            i++;
        }
    }
    
    /**
     * 合并多个map中的list，如果是非list，且dest中没有，则直接拷贝
     * @param dest
     * @param src
     */
    @SuppressWarnings("unchecked")
    public static void mergeList(Map<String, Object> dest, Map<String, Object> src) {
        Object val;
        String key;
        
        for(Map.Entry<String, Object> one : src.entrySet()) {
            val = one.getValue();
            key = one.getKey();
            if(val != null && (val instanceof List<?>)) {
                List<Object> srcList = (List<Object>)val;
                List<Object> list = (List<Object>)dest.get(key);
                if(list == null) {
                    list = new ArrayList<Object>(srcList);
                } else {
                    list.addAll(srcList);
                }
                dest.put(key, list);
            } else {
                if(!dest.containsKey(key)) { //没有的情况才合入
                    dest.put(key, val);
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public static Object getFromMap(Map<String, Object> map, String name) {
        String[] names = name.split("\\.");
        Object o;
        Map<String, Object> sub = map;
        int len = names.length - 1;
        
        for(int i = 0; i < len; i++) {
            o = sub.get(names[i]);
            if(o == null) {
                return null;
            }
            
            if(o instanceof Map<?,?>) {
                sub = (Map<String, Object>)o;
            }
        }
        
        return sub.get(names[len]); //最后一级
    }
    
//    
//    public static void main(String[] args) {
//        String s = "[{\"id\":\"917\",\"name\":\"xxx\",\"version\":\"xxxxx\",\"author\":\"xxxxx\"}]";
//        List<Object> list = jsonToList(s);
//        for(Object o : list) {
//            if(o instanceof Map<?,?>) {
//                Map<String, Object> one = (Map<String, Object>)o;
//                for(Map.Entry<String, Object> e : one.entrySet()) {
//                    System.out.println("key=" + e.getKey() + ",value=" + e.getValue());
//                }
//            }
//        }
//    	String s = removeComment("/*'''/\" */ com \"ddd\" /*test '*'*/");
//    	System.out.println(s);
//    	System.exit(0);
//    }
}
