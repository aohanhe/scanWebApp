package com.ao.scanWebApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/**
 * WebSocket����
 * @author aohanhe
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	@Value("${emqtt.server}")
	private String emqttServer;
	@Value("${emqtt.port:61613}")
	private int emqttPort;
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// ע�⣬���ﲻ������emqtt������ʺ����룬��Ҫ��htmlҳ��ʹ���ʺŵ���
		registry.enableStompBrokerRelay("/topic")
			.setRelayHost(emqttServer)
			.setRelayPort(emqttPort)					
			;			
		
		registry.setApplicationDestinationPrefixes("/app");
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/stomp").setAllowedOrigins("*").withSockJS();
	}

}
