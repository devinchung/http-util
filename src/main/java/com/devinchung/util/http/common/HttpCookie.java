package com.devinchung.util.http.common;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;

/** 
 * 封装Cookie
 * 
 * @author devin
 */
public class HttpCookie {

	/**
	 * 使用HttpContext，用于设置和携带Cookie
	 */
	private HttpClientContext context ;
	
	/**
	 * 储存Cookie
	 */
	private CookieStore cookieStore;
	
	public static HttpCookie custom(){
		return new HttpCookie();
	}
	
	private HttpCookie(){
		this.context = new HttpClientContext();
		this.cookieStore = new BasicCookieStore();
		this.context.setCookieStore(cookieStore);
	}
	
	public HttpClientContext getContext() {
		return context;
	}
	
	public HttpCookie setContext(HttpClientContext context) {
		this.context = context;
		return this;
	}
	
	public CookieStore getCookieStore() {
		return cookieStore;
	}
	
	public HttpCookie setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
		return this;
	}
	
	
}
