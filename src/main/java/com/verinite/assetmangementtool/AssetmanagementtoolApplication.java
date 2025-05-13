package com.verinite.assetmangementtool;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

@SpringBootApplication
@Configuration
@EnableScheduling
public class AssetmanagementtoolApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(AssetmanagementtoolApplication.class, args);
	}

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(AssetmanagementtoolApplication.class);
	}

}
