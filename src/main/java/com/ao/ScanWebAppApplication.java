package com.ao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.ao.scanWebApp.authen.WebAppUserInfo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@SpringBootApplication
@ComponentScan(basePackages = {"com.ao","ao"})
public class ScanWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScanWebAppApplication.class, args);		
	}
}
