package com.cxd.rtcroom;

import com.cxd.rtcroom.util.WxMappingJackson2HttpMessageConverter;
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
		restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
		return restTemplate;
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(App.class);
	}

	@Bean
	public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		//设置中文编码格式
		List<MediaType> list = new ArrayList<>();
		list.add(MediaType.APPLICATION_JSON_UTF8);
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
		return mappingJackson2HttpMessageConverter;
	}
}