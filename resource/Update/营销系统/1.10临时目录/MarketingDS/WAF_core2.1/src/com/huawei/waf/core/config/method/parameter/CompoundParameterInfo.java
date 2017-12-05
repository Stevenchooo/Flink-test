package com.huawei.waf.core.config.method.parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.run.MethodContext;

/**
 * 复合的参数类型，里面可以包含多个基本参数、复合参数
 * 不能作为普通参数使用，定义的参数类型只能用在Object或Array中
 * 用object指定引用的CompoundParameterInfo的名字
 * @author l00152046
 *
 */
@SuppressWarnings("unchecked")
public class CompoundParameterInfo extends ParameterInfo {
	private static final Logger LOG = LogUtil.getInstance();
	private static final String CONFIG_SEGMENTS = "segments";
	private static final String CONFIG_NAME = "name";
	
	private String claim = "";
	
	private String name = "";
	private ParameterInfo[] segments = new ParameterInfo[0];
	
	public static final CompoundParameterInfo parse(String version, Map<String, Object> para, MethodConfig mc) {
		if(!para.containsKey(CONFIG_SEGMENTS) || !para.containsKey(CONFIG_NAME)) {
			LOG.error("No {} or {} in a typedefine", CONFIG_SEGMENTS, CONFIG_NAME);
			return null;
		}
		
		CompoundParameterInfo td = new CompoundParameterInfo();
		td.name = JsonUtil.getAsStr(para, CONFIG_NAME);
		ParameterInfo pi;
		List<Object> segs = JsonUtil.getAsList(para, CONFIG_SEGMENTS);
		List<ParameterInfo> segList = new ArrayList<ParameterInfo>();
		td.claim = "Compound parameter,segments[";
		int index = 1;
		
		for(Object o : segs) {
			if(!(o instanceof Map<?, ?>)) {
				LOG.error("Wrong segment definition of CompoundParameterInfo({})", td.name);
				return null;
			}
	
			if((pi = ParameterInfo.parse(version, (Map<String, Object>)o, mc, index)) == null) {
                LOG.error("Wrong segment definition of CompoundParameterInfo({})", td.name);
				return null;
			}
			index++;
			segList.add(pi);
			td.claim += ',' + pi.getName();
		}
		
		if(segList.size() <= 0) {
			LOG.error("There are no segments in CompoundParameterInfo({})", td.name);
			return null;
		}
		td.segments = segList.toArray(new ParameterInfo[0]);
		td.claim += ']';
		
		return td;
	}

    @Override
	public boolean check(MethodContext context, Object para) {
		if(para instanceof Map<?, ?>) {
			Map<String, Object> o = (Map<String, Object>)para;
			
			for(ParameterInfo pi : segments) {
				if(!pi.check(context, o.get(pi.getName()))) {
					return false;
				}
			}
			return true;
		}
		return segments[0].check(context, para); //简单类型，有且只有一个字段
	}
	
    @Override
	public String getName() {
	    return name;
	}

    @Override
    protected boolean parseExt(String version, Map<String, Object> para, MethodConfig mc) {
        return true;
    }

    @Override
    protected boolean checkExt(MethodContext context, Object ele) {
        return true;
    }

    @Override
    public void setToStatement(PreparedStatement statement, int idx, MethodContext context) throws SQLException {
    }

    @Override
    public Object getValue(ResultSet result, int idx) throws SQLException {
        return null;
    }

    @Override
    public Object getValue(MethodContext context) {
        return null;
    }

    @Override
    public String getClaim() {
        return claim;
    }
    
    @Override
    public boolean isPrimitive() {
        return false;
    }
    
    public ParameterInfo[] getSegments() {
        return segments;
    }
}
