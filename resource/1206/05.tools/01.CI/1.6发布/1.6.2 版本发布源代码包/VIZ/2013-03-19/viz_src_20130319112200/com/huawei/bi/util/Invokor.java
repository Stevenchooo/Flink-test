package com.huawei.bi.util;

import java.lang.reflect.Method;

public class Invokor {

	public Invokor(String className, String methodName, Class[] paramTypes,
			Object[] params) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.paramTypes = paramTypes;
		this.params = params;
	}

	private String className;
	private String methodName;
	private Class[] paramTypes;
	private Object[] params;

	public Object run() throws Exception {
		Object obj = Class.forName(className).newInstance();
		Method m = obj.getClass().getMethod(methodName, paramTypes);
		return m.invoke(obj, params);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class[] getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(Class[] paramTypes) {
		this.paramTypes = paramTypes;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

}
