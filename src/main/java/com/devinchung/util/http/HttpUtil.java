package com.devinchung.util.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devinchung.util.http.common.HCBuilder;
import com.devinchung.util.http.common.HttpConfig;
import com.devinchung.util.http.common.HttpMethod;
import com.devinchung.util.http.exception.HttpClientException;

/**
 * Http工具类， 支持HTTP、HTTPS访问
 * 
 * @author devin
 *
 */
public class HttpUtil {
	
	private final static Logger LOG = LoggerFactory.getLogger(HttpUtil.class);
	
	private static HttpUtil _instance;
	
	private CloseableHttpClient client;

	private HttpUtil(){
		try {
			//构建客户端
			client= HCBuilder.custom().timeout(5000).build();
		} catch (HttpClientException e) {
			LOG.error("HttpClient Init Fail...", e);
		}
	}
	
	public static HttpUtil getInstance(){
		if(null == _instance){
			synchronized (HttpUtil.class) {
				if(null == _instance){
					_instance = new HttpUtil();
				}
			}
		}
		return _instance;
	}
	
	/**
	 * GET请求
	 * 
	 * @param url
	 * @param headers
	 * @param context
	 * @param encoding
	 * @return
	 */
	public String get(String url, Header[] headers, HttpContext context, String encoding) {
		return send(HttpConfig.custom().url(url).headers(headers).context(context).encoding(encoding));
	}
	
	/**
	 * GET请求
	 * 
	 * @param config
	 * @return
	 */
	public String get(HttpConfig config){
		return send(config.method(HttpMethod.GET));
	}
	
	/**
	 * POST请求
	 * 
	 * @param url
	 * @param headers
	 * @param data
	 * @param context
	 * @param encoding
	 * @return
	 */
	public String post(String url, Header[] headers, Map<String, Object> data, HttpContext context, String encoding){
		return send(HttpConfig.custom().method(HttpMethod.POST).url(url).headers(headers).data(data).context(context).encoding(encoding));
	}
	
	/**
	 * POST请求
	 * 
	 * @param config
	 * @return
	 */
	public String post(HttpConfig config){
		return send(config.method(HttpMethod.POST));
	}
	
	/**
	 * PUT请求
	 * 
	 * @param url
	 * @param headers
	 * @param data
	 * @param context
	 * @param encoding
	 * @return
	 */
	public String put(String url, Header[] headers, Map<String, Object> data, HttpContext context, String encoding){
		return send(HttpConfig.custom().method(HttpMethod.PUT).url(url).headers(headers).data(data).context(context).encoding(encoding));
	}
	
	/**
	 * PUT请求
	 * 
	 * @param config
	 * @return
	 */
	public String put(HttpConfig config){
		return send(config.method(HttpMethod.PUT));
	}
	
	/**
	 * DELETE请求
	 * 
	 * @param url
	 * @param headers
	 * @param context
	 * @param encoding
	 * @return
	 */
	public String delete(String url, Header[] headers, HttpContext context, String encoding) {
		return send(HttpConfig.custom().method(HttpMethod.DELETE).url(url).headers(headers).context(context).encoding(encoding));
	}
	
	/**
	 * DELETE请求
	 * 
	 * @param config
	 * @return
	 */
	public String delete(HttpConfig config){
		return send(config.method(HttpMethod.DELETE));
	}
	
	/**
	 * 统一资源发送请求
	 * 
	 * @param config
	 * @return
	 */
	public String send(HttpConfig config){
		return format2String(execute(config), config.outenc());
	}
	
	/**
	 * 统一请求处理
	 * 
	 * @param config
	 * @return
	 */
	private HttpResponse execute(HttpConfig config){
		try {
			HttpResponse response = null;
			// 新建请求对象
			HttpRequestBase request = getRequest(config.url(), config.method());
			
			// 设置Headers
			request.setHeaders(config.headers());
			
			// 判断支持entity的请求(POST/PUT)
			if(HttpEntityEnclosingRequestBase.class.isAssignableFrom(request.getClass())){
				// 封装参数
				List<NameValuePair> nvps = dataConvert(config.data());
				HttpEntity entity = new UrlEncodedFormEntity(nvps, config.inenc());
				
				((HttpEntityEnclosingRequestBase)request).setEntity(entity);
			}
			
			LOG.info("do request :"+config.url());
			
			// 执行请求
			response = (null == config.context()) ? client.execute(request) : client.execute(request, config.context());
			
			// 是否需要返回响应头
			if(config.isReturnHeaders()){
				config.headers(response.getAllHeaders());
			}
			
			return response;
		} catch (UnsupportedEncodingException e) {
			throw new HttpClientException(e);
		} catch (ClientProtocolException e) {
			throw new HttpClientException(e);
		} catch (IOException e) {
			throw new HttpClientException(e);
		} 
	}
	
	private static HttpRequestBase getRequest(String url, HttpMethod method) {
		HttpRequestBase request = null;
		switch (method) {
			case GET:
				request = new HttpGet(url);
				break;
			case POST:
				request = new HttpPost(url);
				break;
			case PUT:
				request = new HttpPut(url);
				break;
			case DELETE:
				request = new HttpDelete(url);
				break;
			default:
				request = new HttpGet(url);
				break;
		}
		return request;
	}
	
	private static List<NameValuePair> dataConvert(Map<String, Object> params){
		List<NameValuePair> nvps = new LinkedList<NameValuePair>();
		Set<Entry<String, Object>> paramsSet= params.entrySet();
		for (Entry<String, Object> paramEntry : paramsSet) {
			nvps.add(new BasicNameValuePair(paramEntry.getKey(), String.valueOf(paramEntry.getValue())));
		}
		return nvps;
	}
	
	public static String format2String(HttpResponse response, String encoding){
		try {
			return EntityUtils.toString(response.getEntity(), encoding);
		} catch (ParseException e) {
			throw new HttpClientException(e);
		} catch (IOException e) {
			throw new HttpClientException(e);
		}
	}
}
