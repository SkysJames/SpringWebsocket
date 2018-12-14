package com.sky.websocket.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.sky.websocket.constants.WebSocketConstants;

@Component
public class SpringWebSocketHandler implements WebSocketHandler {

	//存储建立连接后的WebSocketSession列表，即session-websocket列表（相同的session可能会有多个websocket连接，即打开多个相同的页面）
	private static final Map<String, List<WebSocketSession>> sessionMap;
	
	static {
		sessionMap = new HashMap<String, List<WebSocketSession>>();
	}
	
	/**
	 * 连接成功时触发，同时会触发页面上onopen方法
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("------------------------成功建立websocket连接");
        String sessionKey = (String) session.getAttributes().get(WebSocketConstants.LISTENER_KEY);
        
        List<WebSocketSession> sessionList = null;
        if(sessionMap.containsKey(sessionKey)) {
        	sessionList = sessionMap.get(sessionKey);
        } else {
        	sessionList = new ArrayList<>();
        }
        sessionList.add(session);
        
        sessionMap.put(sessionKey, sessionList);
        System.out.println("sessionKey：" + sessionKey + "		sessionList数量：" + sessionList.size());
        System.out.println("总sessionMap数量：" + sessionMap.size());
	}
	
	/**
	 * 关闭连接时触发
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		System.out.println("------------------------关闭websocket连接");
        String sessionKey = (String) session.getAttributes().get(WebSocketConstants.LISTENER_KEY);
        
        List<WebSocketSession> sessionList = new ArrayList<>();
        if(sessionMap.containsKey(sessionKey)) {
        	sessionList = sessionMap.get(sessionKey);
        	sessionList.remove(session);
        }
        
        if(sessionList.isEmpty()) {
        	sessionMap.remove(sessionKey);
        }
        
        System.out.println("sessionKey：" + sessionKey + "		sessionList数量：" + sessionList.size());
        System.out.println("总sessionMap数量：" + sessionMap.size());
	}

	/**
	 * js调用websocket.send时候，会调用该方法处理消息
	 */
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        System.out.println("服务器收到消息：" + webSocketMessage);
//        String sessionKey = (String)session.getAttributes().get(WebSocketConstants.LISTENER_KEY);
        
//        redisTemplate.convertAndSend("websocket:sendMsgTopic", sessionKey + "_" + "服务器单发：" +webSocketMessage.getPayload());
        
	}

	/**
	 * 传输出现异常时触发
	 */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		if(session.isOpen()){
            session.close();
        }
		System.out.println("传输出现异常，关闭websocket连接.....");
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
	
	/**
     * 给session发送消息
     *
     * @param sessionKey
     * @param message
     */
    public void sendMessage(String sessionKey, WebSocketMessage<?> webSocketMessage) {
    	System.out.println("发送消息到session：" + sessionKey);
    	try {
	        if(sessionMap.containsKey(sessionKey)) {
	        	List<WebSocketSession> list = sessionMap.get(sessionKey);
	        	for(WebSocketSession session : list) {
	        		synchronized(session) {
	        			if (session.isOpen()) {
		        			session.sendMessage(webSocketMessage);
		                }
	        		}
	        	}
	        }
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
}
