package com.sky.websocket.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.sky.websocket.constants.WebSocketConstants;
import com.sky.websocket.listener.WebsocketTopicListener;

@Component
public class RedisWebSocketConfig {
	
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		Integer database = 1;
		Integer port = 6379;
		String hostname = "192.168.17.132";
		String password = "123456";
		
		factory.setDatabase(database);
		factory.setHostName(hostname);
		factory.setPort(port);
		factory.setPassword(password);
		factory.setUsePool(true);
		
		return factory;
	}
	
	@Bean
	public RedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
		RedisTemplate redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		redisTemplate.setDefaultSerializer(new StringRedisSerializer());
		return redisTemplate;
	}
	
	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(JedisConnectionFactory jedisConnectionFactory, WebsocketTopicListener websocketTopicListener) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(jedisConnectionFactory);
		container.addMessageListener(websocketTopicListener, new ChannelTopic(WebSocketConstants.REDIS_TOPIC));
		return container;
	}
}
