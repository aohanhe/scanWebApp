package com.ao.scanWebApp.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.context.request.RequestContextListener;

/**
 * 网站安全配置
 * 
 * @author aohanhe
 *
 */
@Configuration
public class ScanWebAppSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.requestMatchers().anyRequest()
			.and()
			.authorizeRequests()
			.antMatchers("/oauth/*","/api/test/**").permitAll();
	}

	
	
	
	
/*	@Bean
	protected UserDetailsService userDetailsService(){
	    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
	    var coder=passwordEncoder();
	    manager.createUser(User.withUsername("user_1").password(coder.encode("123456")).roles("R1").build());
	    manager.createUser(User.withUsername("user_2").password(coder.encode("123456")).authorities("USER").build());
	    
	    return manager;
	}*/
	
	@Bean 
	@Order(1)
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * 添加requestContextListener监听器，以方便系统取得request对象
	 * @return
	 */
	@Bean
	@Order(1)
	public RequestContextListener requestContextListener(){	
	    return new RequestContextListener();	
	} 

}
