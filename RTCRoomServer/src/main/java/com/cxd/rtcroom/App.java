package com.cxd.rtcroom;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ws03089
 */
@SpringBootApplication
public class App extends SpringBootServletInitializer {

	@Bean
	public RestTemplate restTemplate(){

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(getMappingJackson2HttpMessageConverterJson());
		return restTemplate;
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(App.class);
	}

	@Bean
	public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverterJson() {
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		mediaTypes.add(MediaType.TEXT_PLAIN);
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes);
		return mappingJackson2HttpMessageConverter;
	}
	@Bean
	public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverterText() {
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.TEXT_PLAIN);
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes);
		return mappingJackson2HttpMessageConverter;
	}
}