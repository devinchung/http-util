package com.devinchung.util.http.common;

import java.io.OutputStream;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.protocol.HttpContext;

/** 
 * HttpConfig
 * 
 * @author devin
 */
public class HttpConfig {
	
	private final static String ENCODING = "utf-8";
	
	private HttpConfig(){};
	
	/**
	 * 实例
	 * @return
	 */
	public static HttpConfig custom(){
		return new HttpConfig();
	}

	/**
	 * 资源url
	 */
	private String url;

	/**
	 * Header头信息
	 */
	private Header[] headers;
	
	/**
	 * 是否返回response的headers
	 */
	private boolean returnHeaders;

	/**
	 * 请求方法
	 */
	private HttpMethod method = HttpMethod.GET;
	
	/**
	 * 请求方法名称
	 */
	private String methodName;

	/**
	 * 用于cookie操作
	 */
	private HttpContext context;

	/**
	 * 传递参数
	 */
	private Map<String, Object> data;

	/**
	 * 输入输出编码
	 */
	private String encoding = ENCODING;

	/**
	 * 输入编码
	 */
	private String inenc;

	/**
	 * 输出编码
	 */
	private String outenc;
	
	/**
	 * 输出流对象
	 */
	private OutputStream out;
	
	/**
	 * 资源url
	 */
	public HttpConfig url(String url) {
		this.url = url;
		return this;
	}
	
	/**
	 * Header头信息
	 */
	public HttpConfig headers(Header[] headers) {
		this.headers = headers;
		return this;
	}
	
	/**
	 * Header头信息(是否返回response中的headers)
	 */
	public HttpConfig headers(Header[] headers, boolean returnHeaders) {
		this.headers = headers;
		this.returnHeaders=returnHeaders;
		return this;
	}
	
	/**
	 * 请求方法
	 */
	public HttpConfig method(HttpMethod method) {
		this.method = method;
		return this;
	}
	
	/**
	 * 请求方法
	 */
	public HttpConfig methodName(String methodName) {
		this.methodName = methodName;
		return this;
	}
	
	/**
	 * cookie操作相关
	 */
	public HttpConfig context(HttpContext context) {
		this.context = context;
		return this;
	}
	
	/**
	 * 传递参数
	 */
	public HttpConfig data(Map<String, Object> data) {
		this.data = data;
		return this;
	}
	
	/**
	 * 输入输出编码
	 */
	public HttpConfig encoding(String encoding) {
		//设置输入输出
		inenc(encoding);
		outenc(encoding);
		this.encoding = encoding;
		return this;
	}
	
	/**
	 * 输入编码
	 */
	public HttpConfig inenc(String inenc) {
		this.inenc = inenc;
		return this;
	}
	
	/**
	 * 输出编码
	 */
	public HttpConfig outenc(String outenc) {
		this.outenc = outenc;
		return this;
	}
	
	/**
	 * 输出流对象
	 */
	public HttpConfig out(OutputStream out) {
		this.out = out;
		return this;
	}

	public Header[] headers() {
		return headers;
	}

	public boolean isReturnHeaders() {
		return returnHeaders;
	}
	
	public String url() {
		return url;
	}

	public HttpMethod method() {
		return method;
	}

	public String methodName() {
		return methodName;
	}

	public HttpContext context() {
		return context;
	}

	public Map<String, Object> data() {
		return data;
	}

	public String encoding() {
		return encoding;
	}

	public String inenc() {
		return inenc == null ? encoding : inenc;
	}

	public String outenc() {
		return outenc == null ? encoding : outenc;
	}
	
	public OutputStream out() {
		return out;
	}
	
}