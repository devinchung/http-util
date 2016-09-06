package com.devinchung.util.http.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;

import com.devinchung.util.http.exception.HttpClientException;

/**
 * ssl
 * 
 * @author devin
 */
public class SSL {

    private static final SSLHandler sslHandler = new SSLHandler();
	private static SSLSocketFactory sslFactory ;
	private static SSLConnectionSocketFactory sslConnFactory ;
	private SSLContext sc;
	
	public static SSL custom(){
		return new SSL();
	}

    // 重写X509TrustManager类的三个方法,信任服务器证书
    private static class SSLHandler implements  X509TrustManager, HostnameVerifier{
		
		@Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return new java.security.cert.X509Certificate[]{};
			//return null;
		}
		
		@Override
		public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
				String authType) throws java.security.cert.CertificateException {
		}
		
		@Override
		public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
				String authType) throws java.security.cert.CertificateException {
		}

		@Override
		public boolean verify(String paramString, SSLSession paramSSLSession) {
			return true;
		}
	};
    
	// 信任主机
    public static HostnameVerifier getVerifier() {
        return sslHandler;
    }
    
    public synchronized SSLSocketFactory getSSLSF() {
        if (sslFactory != null)
            return sslFactory;
		try {
			SSLContext sc = getSSLContext();
			sc.init(null, new TrustManager[] { sslHandler }, null);
			sslFactory = sc.getSocketFactory();
		} catch (KeyManagementException e) {
			throw new HttpClientException(e);
		}
        return sslFactory;
    }
    
    public synchronized SSLConnectionSocketFactory getSSLCONNSF() {
    	if (null != sslConnFactory)
    		return sslConnFactory;
    	try {
	    	SSLContext sc = getSSLContext();
	    	sc.init(null, new TrustManager[] { sslHandler }, new java.security.SecureRandom());
	    	sslConnFactory = new SSLConnectionSocketFactory(sc, sslHandler);
		} catch (KeyManagementException e) {
			throw new HttpClientException(e);
		}
    	return sslConnFactory;
    }
    
    public SSL customSSL(String keyStorePath, String keyStorepass){
    	FileInputStream instream =null;
    	KeyStore trustStore = null; 
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			instream = new FileInputStream(new File(keyStorePath));
			trustStore.load(instream, keyStorepass.toCharArray());
			// 相信自己的CA和所有自签名的证书
			sc= SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()) .build();
		} catch (KeyManagementException e) {
			throw new HttpClientException(e);
		} catch (KeyStoreException e) {
			throw new HttpClientException(e);
		} catch (FileNotFoundException e) {
			throw new HttpClientException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new HttpClientException(e);
		} catch (CertificateException e) {
			throw new HttpClientException(e);
		} catch (IOException e) {
			throw new HttpClientException(e);
		}finally{
			try {
				instream.close();
			} catch (IOException e) {}
		}
		return this;
    }
    
    public SSLContext getSSLContext(){
    	try {
    		if(sc==null){
    			sc = SSLContext.getInstance("SSLv3");
    		}
			return sc;
		} catch (NoSuchAlgorithmException e) {
			throw new HttpClientException(e);
		}
    }
}