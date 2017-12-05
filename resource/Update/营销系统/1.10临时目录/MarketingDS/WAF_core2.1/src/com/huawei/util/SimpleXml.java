package com.huawei.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author l00152046
 * 针对简单xml的解析，非常快速，单核的情况，main中的测试用例可以达到30万TPS以上，
 * 比相同内容的fastjson的解析要快一倍左右。
 * 前提是，xml内容必须简单，只承载数据，类似json的目的
 */
public class SimpleXml {
    private static final String CDATA_HEAD = "[CDATA["; //<![CDATA[....]]>
    private static final int CDATA_HEAD_LEN = CDATA_HEAD.length() + 2;
    private static final String CDATA_TAIL = "]]>";
    private static final int CDATA_TAIL_LEN = CDATA_TAIL.length();
    
	private static class NodeInfo {
		public int start = 0;
		public int end = 0;
		public int totalEnd = 0;
		public String name = null;
		public Map<String, NodeInfo> subProps = null;
		public List<NodeInfo> subList = null;
		
		public NodeInfo(int start, String name) {
			this.start = start;
			this.name = name;
		}
		
		public boolean isSimple() {
			return subProps == null && subList == null;
		}
		
		public boolean isList() {
			return subList != null;
		}
		
		public boolean isObject() {
			return subList == null  && subProps != null;
		}
	}
	
	public static final class SimpleXmlException extends Exception {
		private static final long serialVersionUID = 1L;
		
		public SimpleXmlException(String info, int pos, String element) {
			super(info + ",pos:" + pos + ",element:" + element);
		}
	}
	
	private static final NodeInfo parse(String xml, int start, int len, NodeInfo parent) throws SimpleXmlException {
		int pos = start;
		char ch;
		String name;
		
		while(pos < len) {
			ch = xml.charAt(pos);
			if(ch != '<') {
				pos++;
				continue;
			}

			int i = pos + 1;
			for(; (ch = xml.charAt(i)) != '>' && i < len; i++);
			if(i >= len) {
				throw new SimpleXmlException("Fail to parse", pos, parent != null ? parent.name : "");
			}
			
			ch = xml.charAt(pos + 1);
			if(ch == '/') { //标签结束
				name = xml.substring(pos + 2, i);
				if(name.equals(parent.name)) {
				    if(parent.end <= 0) {
				        parent.end = pos;
				    }
					parent.totalEnd = i + 1;
					return parent;
				}
				throw new SimpleXmlException("Invalid end " + name, pos, parent.name);
			} 
			
			if(ch == '!') {
			    if(xml.charAt(pos + 2) == '-' && xml.charAt(pos + 3) == '-') { //<!-- ... -->
			        pos = i + 1;
			        parent.start = pos; //跳过注释内容
			        continue;
			    }
			    
                if(xml.indexOf(CDATA_HEAD, pos + 2) == pos + 2) { //<![CDATA[ ... ]]>
                    parent.start += CDATA_HEAD_LEN;
                    //CDATA中可能有<>等字符，所以必须找到真正的结束符
                    if(xml.charAt(i - 1) != ']' || xml.charAt(i - 2) != ']') {
                        i = xml.indexOf(CDATA_TAIL, i);
                        parent.end = i;
                        pos = i + CDATA_TAIL_LEN;
                    } else {
                        parent.end = i - CDATA_TAIL_LEN + 1;
                        pos = i + 1;
                    }
                    continue;
                }
                throw new SimpleXmlException("Invalid data", pos, parent.name);
			}
			
			name = xml.substring(pos + 1, i);

			NodeInfo node = parse(xml, i + 1, len, new NodeInfo(i + 1, name));
			if(parent != null) {
				if(parent.subProps == null) {
					parent.subProps = new HashMap<String, NodeInfo>();
					parent.subProps.put(name, node);
				} else {
					if(parent.subProps.containsKey(name)) {
						if(parent.subProps.size() != 1) {
							throw new SimpleXmlException("There are list and properties under the same node", pos, parent.name);
						}
						
						if(parent.subList == null) {
							parent.subList = new ArrayList<NodeInfo>();
							parent.subList.add(parent.subProps.get(name));							
						}
						parent.subList.add(node);
					} else if(parent.subList == null){
						parent.subProps.put(name, node);
					} else {
                        throw new SimpleXmlException("There are list and properties under the same node", pos, parent.name);
					}
				}
			} else {
				parent = node;
			}
			pos = node.totalEnd;
		}
		
		if(pos == len) {
			return parent;
		}
		
		throw new SimpleXmlException("Fail to parse", pos, parent != null ? parent.name : "");
	}
	
	private static final void genResult(String xml, NodeInfo ni, Map<String, Object> res) {
		if(ni.isList()) {
			List<Object> list = new ArrayList<Object>(ni.subList.size());
			genResult(xml, ni, list);
			res.put(ni.name, list);
			return;
		}
		
		if(ni.isObject()) {
			for(Map.Entry<String, NodeInfo> one : ni.subProps.entrySet()) {
				NodeInfo n = one.getValue();
				if(n.isSimple()) {
					res.put(n.name, xml.substring(n.start, n.end));
				} else if(n.isList()){
					List<Object> list = new ArrayList<Object>(n.subList.size());
					genResult(xml, n, list);
					res.put(n.name, list);
				} else {
					Map<String, Object> r = new HashMap<String, Object>();
					genResult(xml, n, r);
					res.put(n.name, r);
				}
			}
			return;
		}
		
		res.put(ni.name, xml.substring(ni.start, ni.end));
	}
	
	private static final void genResult(String xml, NodeInfo ni, List<Object> res) {
		for(NodeInfo n : ni.subList) {
			if(n.isSimple()) {
				res.add(xml.substring(n.start, n.end));
			} else if(n.isList()){
                List<Object> list = new ArrayList<Object>(n.subList.size());
				genResult(xml, n, list);
				res.add(list);
			} else {
				Map<String, Object> r = new HashMap<String, Object>();
				genResult(xml, n, r);
				res.add(r);
			}
		}
	}
	
	public static final Map<String, Object> fromXml(String xml) throws SimpleXmlException {
		NodeInfo ni = parse(xml, 0, xml.length(), null);
		Map<String, Object> res = new HashMap<String, Object>();
		genResult(xml, ni, res);
		return res;
	}
	//-------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    private static final void form(List<Object> data, StringBuilder sb, String listItem) {
        for(Object o : data) {
            sb.append('<').append(listItem).append('>');
            if(o instanceof List) {
                form((List<Object>)o, sb, listItem);
            } else if(o instanceof Map) {
                form((Map<String, Object>)o, sb, listItem);
            } else {
                sb.append(o.toString());
            }
            sb.append("</").append(listItem).append('>');
        }
    }
    
    @SuppressWarnings("unchecked")
    private static final void form(Map<String, Object> data, StringBuilder sb, String listItem) {
        String name;
        
        for(Map.Entry<String, Object> one : data.entrySet()) {
            Object o = one.getValue();
            name = one.getKey();
            sb.append('<').append(name).append('>');
            if(o instanceof List) {
                form((List<Object>)o, sb, listItem);
            } else if(o instanceof Map) {
                form((Map<String, Object>)o, sb, listItem);
            } else {
                sb.append(o.toString());
            }
            sb.append("</").append(name).append('>');
        }
    }
    
    public static final String toXml(Map<String, Object> data, String container, String listItem) {
        StringBuilder sb = new StringBuilder();
        sb.append('<').append(container).append('>');
        form(data, sb, listItem);
        sb.append("</").append(container).append('>');
        return sb.toString();
    }
    
    public static final String toXml(Map<String, Object> data, String container) {
        return toXml(data, container, "i");
    }
    
    public static final String toXml(Map<String, Object> data) {
        return toXml(data, "xml", "i");
    }
	//-------------------------------------------------------------------------
//	public static void main(String[] args) throws SimpleXmlException {
//		String str = new String("<xml><ToUserName><!--test--><![CDATA[oia2TjjewbmiOUlr6X->\"1crbLOvLw]]></ToUserName><FromUserName><![CDATA[gh_7f083739789a]]></FromUserName><CreateTime>1407743423</CreateTime><MsgType><![CDATA[video]]></MsgType><Video><MediaId><![CDATA[eYJ1MbwPRJtOvIEabaxHs7TX2D-HV71s79GUxqdUkjm6Gs2Ed1KF3ulAOA9H1xG0]]></MediaId><Title><![CDATA[testCallBackReplyVideo]]></Title><Description><![CDATA[testCallBackReplyVideo]]></Description></Video></xml>");
//		
//		String str1 = new String("{"
//				+"\"ToUserName\":\"liguoyong\""
//				+",\"FromUserName\":\"chenyan\""
//				+",\"CreateTime\":1357290913"
//				+",\"MsgType\":\"picture\""
//				+",\"MediaId\":\"aaa\""
//				+",\"ThumbMediaId\":\"dddd\""
//				//+",\"MsgId\":\"1234567890123456\""
//				+",\"Msg\":{\"id\":\"1234567890123456\",\"content\":\"hello\"}"
//				+"}");
//		//str = "<xml><Msg><id>1234567890123456</id><content>hello</content></Msg><T><i>picture</i><i>text</i></T><MediaId>aaa</MediaId><CreateTime>1357290913</CreateTime><ToUserName>liguoyong</ToUserName><FromUserName>liguoyong</FromUserName><MsgType>picture</MsgType><ThumbMediaId>dddd</ThumbMediaId><M><i><a>a</a><b>b</b></i><i><a>c</a><b>d</b><c>e</c></i></M></xml>";
//		long beginTime = System.currentTimeMillis();
//		int N = 1000000;
//		Map<String, Object> map = SimpleXml.fromXml(str);
//		for(int i = 0; i < N; i++) {
//			map = SimpleXml.fromXml(str);
//			//map = XmlUtil.dom2Map(doc);
//			//map = JsonUtil.jsonToMap(str1);
//			//str1 = toXml(map, "xml");
//		}
//		System.out.println(map.toString());
//		System.out.println(str1);
//		long t = System.currentTimeMillis() - beginTime;
//		System.out.println("Use time:" + t + ",spead:" + (1000L * N / t));
//		
//		System.exit(0);
//	}
}
