package com.ao.scanWebApp.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RedisLettuceConfig {
	
	@Value("${spring.redis.lettuce.pool.max-total}")
    private int maxTotal;
    @Value("${spring.redis.lettuce.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.lettuce.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.lettuce.pool.max-wait}")
    private long maxWait;
    


    @Bean
	public GenericObjectPoolConfig getRedisConfig() {
		GenericObjectPoolConfig config=new GenericObjectPoolConfig<>();
				
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		config.setMaxIdle(this.maxIdle);
		config.setMinIdle(this.minIdle);
		config.setMaxTotal(this.maxTotal);
		config.setMaxWaitMillis(this.maxWait);
		
		return config;
	}
 
}
