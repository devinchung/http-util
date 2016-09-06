package com.devinchung.util.http.common;

/**
 * HttpMethod
 * 
 * @author devin
 */
public enum HttpMethod {

	/**
	 * GET
	 */
	GET(0, "GET"),

	/**
	 * POST
	 */
	POST(1, "POST"),

	/**
	 * PUT
	 */
	PUT(2, "PUT"),

	/**
	 * DELETE
	 */
	DELETE(3, "DELETE");

	private int code;

	private String name;

	private HttpMethod(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getCode() {
		return code;
	}
}