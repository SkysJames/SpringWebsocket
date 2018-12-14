package com.sky.websocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.sky.websocket.constants.WebSocketConstants;

@Service
public class RedisWebSocketService {
	@Autowired
    private RedisTemplate redisTemplate;
	
	public void sendMessage(String userId, String archiveId, Object msg) {
		redisTemplate.convertAndSend(WebSocketConstants.REDIS_TOPIC, userId + archiveId + "_" + msg);
	}
	
}
