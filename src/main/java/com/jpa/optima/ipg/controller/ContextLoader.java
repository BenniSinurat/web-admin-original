package com.jpa.optima.ipg.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

@Component
@PropertySource("/WEB-INF/app.properties")
public class ContextLoader {

	@Value("${core.ws.header.token}")
	private String headerToken;
	@Value("${host.ws.url}")
	private String HostWSUrl;
	@Value("${host.ws.port}")
	private String HostWSPort;

	public String getHeaderToken() {
		return headerToken;
	}

	public void setHeaderToken(String headerToken) {
		this.headerToken = headerToken;
	}

	public String getHostWSUrl() {
		return HostWSUrl;
	}

	public void setHostWSUrl(String hostWSUrl) {
		HostWSUrl = hostWSUrl;
	}

	public String getHostWSPort() {
		return HostWSPort;
	}

	public void setHostWSPort(String hostWSPort) {
		HostWSPort = hostWSPort;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
