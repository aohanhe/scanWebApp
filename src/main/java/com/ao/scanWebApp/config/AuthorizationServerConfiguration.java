package com.ao.scanWebApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 认证服务器配置
 * 
 * @author aohanhe
 *
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private ScanServerAuthenticationProvider authentionProvider;
	
	@Autowired
	private ScanServerUserDetailsService detailsService;
	
	
	@Value("${System.config.clientPwd:be519c33b1b011e89c780235d2b38928}")
	private String clientPwd;

	

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			.withClient(ScanClients.MangerAppClient)
			.scopes("ScanWebApp")			
			.secret(encoder.encode(clientPwd))
			.authorizedGrantTypes("client_credentials", "refresh_token","password")	
			.resourceIds("ScanWebApp")
			.autoApprove(true).accessTokenValiditySeconds(60*10)
			.and()
			.withClient(ScanClients.WxMinAppClient)
			.scopes("ScanWebApp")			
			.secret(encoder.encode(clientPwd))
			.authorizedGrantTypes("client_credentials", "refresh_token","password")		
			.resourceIds("ScanWebApp").accessTokenValiditySeconds(60*10)
			.autoApprove(true)
			;		
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(new RedisTokenStore(redisConnectionFactory))
				.userDetailsService(detailsService)
				.authenticationManager(new AuthenticationManager() {					
					@Override
					public Authentication authenticate(Authentication authentication) throws AuthenticationException {
						
						return authentionProvider.authenticate(authentication);
					}
				});
	}
	
	
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.allowFormAuthenticationForClients();
	}

}
