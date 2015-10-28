package com.nandanu.crawler.controller;

public class UrlSetter {
//	private String url = "http://10.0.2.2"; // IP android emulator
	private String url = "http://192.168.43.21"; // IP Laptop
//	private String url = "http://172.20.10.3"; // IP Laptop
	
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
