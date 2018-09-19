package com.ao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ao.scanElectricityBis.auth.IScanServerPrincipal;
import com.ao.scanElectricityBis.auth.ScanServerPrincipal;
import com.ao.scanWebApp.authen.WebAppUserInfo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@SpringBootApplication
@ComponentScan(basePackages = {"com.ao","ao"})
@EnableJpaAuditing
@EnableMongoRepositories
@EnableJpaRepositories
public class ScanWebAppApplication {
	@Autowired
	private PasswordEncoder encoder;

	public static void main(String[] args) {
		var context=SpringApplication.run(ScanWebAppApplication.class, args);		
		
	
		
		//var str=context.getBean(ScanWebAppApplication.class).encoder.encode("123456");
		//System.out.println(str);
	}
}
