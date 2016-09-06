package com.devinchung.util.http.exception;


/** 
 * 
 * @author devin
 * 
 */
public class HttpClientException  extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2749168865492921426L;

	public HttpClientException(Exception e){
		super(e);
	}

	/**
	 * @param string
	 */
	public HttpClientException(String msg) {
		super(msg);
	}
	
	/**
	 * @param message
	 * @param e
	 */
	public HttpClientException(String message, Exception e) {
		super(message, e);
	}
	
}
