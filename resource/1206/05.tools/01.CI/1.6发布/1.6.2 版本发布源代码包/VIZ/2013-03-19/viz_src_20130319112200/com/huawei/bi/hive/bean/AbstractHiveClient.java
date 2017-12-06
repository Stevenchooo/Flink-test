package com.huawei.bi.hive.bean;

import java.util.StringTokenizer;

public abstract class AbstractHiveClient implements IHiveClient {
	protected ConnParam getConnMap(String conn) {
		StringTokenizer token = new StringTokenizer(conn, ":");
		String host = token.nextToken();
		int port = Integer.parseInt(token.nextToken());

		return new ConnParam(host, port);
	}
}

class ConnParam {

	public ConnParam(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public String host;
	public int port;
}