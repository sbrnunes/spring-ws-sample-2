package com.bnpparibas.goe.service.edservice.http;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpClientParams;

public class MyHttpClient extends HttpClient {

	public MyHttpClient(HttpClientParams params, HttpConnectionManager httpConnectionManager) {
		super(params, httpConnectionManager);
	}
	
	public void setCredentials(Credentials credentials) {
		getState().setCredentials(AuthScope.ANY, credentials);
	}
	
}
