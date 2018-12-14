package com.sky.websocket.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import com.sky.websocket.handler.SpringWebSocketHandler;

@Component
public class WebsocketTopicListener implements MessageListener {

	@Autowired
    private RedisTemplate redisTemplate;
	@Autowired
	public SpringWebSocketHandler springWebSocketHandler;
	
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String itemValue = (String) redisTemplate.getValueSerializer().deserialize(body);
        String topic = (String) redisTemplate.getStringSerializer().deserialize(channel);

        System.out.println("-------------------------------------------收到redis的发布消息后发送");
        System.out.println(itemValue);
        System.out.println(topic);
        System.out.println();
        
        String[] aa = itemValue.split("_");
        springWebSocketHandler.sendMessage(aa[0], new TextMessage(aa[1]));
	}

}
