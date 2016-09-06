package com.devinchung.util.http.common;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.devinchung.util.http.exception.HttpClientException;

/**
 * HttpClient 构造器<br>
 * 	默认自带https
 * 
 * @author devin
 *
 */
public class HCBuilder extends HttpClientBuilder{
	
	// 默认连接池大小
	private int maxTotal = 400;
	
	private int defaultMaxPerRoute = 200;
	
	private SSL ssls;
	
	private HCBuilder(){}
	
	public static HCBuilder custom(){
		return new HCBuilder();
	}
	
	/**
	 * 设置超时
	 * 
	 * @param timeout  超时，单位-毫秒
	 * @return
	 */
	public HCBuilder timeout(int timeout){
		 try {
			// 配置请求的超时设置
			RequestConfig config = RequestConfig.custom()
			        .setConnectionRequestTimeout(timeout)
			        .setConnectTimeout(timeout)
			        .setSocketTimeout(timeout)
			        .build();
			return (HCBuilder) this.setDefaultRequestConfig(config);
		} catch (Exception e) {
			throw new HttpClientException(e);	
		}
	}

	@Override
	public CloseableHttpClient build() {
		try {
			//设置ssl安全链接
			if(null == ssls){
				ssls = SSL.custom();
			}
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
					.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", ssls.getSSLCONNSF()).build();
			//设置连接池大小
			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			connManager.setMaxTotal(maxTotal);
			connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);		
			this.setConnectionManager(connManager);
			
			return super.build();
		} catch (Exception e) {
			throw new HttpClientException(e);
		}
	}
	
	/**
	 * 设置连接池
	 * 
	 * @param maxTotal
	 * @param defaultMaxPerRoute
	 * @return
	 */
	public HCBuilder pool(int maxTotal, int defaultMaxPerRoute){
		this.maxTotal = maxTotal;
		this.defaultMaxPerRoute = defaultMaxPerRoute;
		return this;
	}
	
	/**
	 * 设置自定义sslcontext
	 * 
	 * @param keyStorePath		密钥库路径
	 * @return
	 * @throws HttpClientException
	 */
	public HCBuilder ssl(String keyStorePath){
		return ssl(keyStorePath,"nopassword");
	}
	/**
	 * 设置自定义sslcontext
	 * 
	 * @param keyStorePath		密钥库路径
	 * @param keyStorepass		密钥库密码
	 * @return
	 * @throws HttpClientException
	 */
	public HCBuilder ssl(String keyStorePath, String keyStorepass){
		this.ssls = SSL.custom().customSSL(keyStorePath, keyStorepass);
		return this;
	}
}
