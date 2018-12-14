package com.sky.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.sky.websocket.handler.SpringWebSocketHandler;
import com.sky.websocket.interceptor.SpringWebSocketHandlerInterceptor;


@Configuration
@EnableWebSocket
public class SpringWebSocketConfig implements WebSocketConfigurer {

	@Autowired
	public SpringWebSocketHandler springWebSocketHandler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(springWebSocketHandler, "/websocket/socketServer.do").addInterceptors(new SpringWebSocketHandlerInterceptor());

		registry.addHandler(springWebSocketHandler, "/sockjs/socketServer.do").addInterceptors(new SpringWebSocketHandlerInterceptor()).withSockJS();
	}
	
}
