package com.devinchung.util.http;

import org.junit.Test;

import com.devinchung.util.http.common.HttpConfig;

public class HttpUtilTest {
	
	@Test
	public void test4Get(){
		String url = "https://www.baidu.com/";
		String content = HttpUtil.getInstance().get(HttpConfig.custom().url(url));
		System.out.println(content);
	}
	
	@Test
	public void test4Post(){
	
	}
	
	@Test
	public void test4Put(){
		
	}
	
	@Test
	public void test4Delete(){
		
	}
}
