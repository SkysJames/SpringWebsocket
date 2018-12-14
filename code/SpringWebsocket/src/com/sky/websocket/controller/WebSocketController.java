package com.sky.websocket.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sky.websocket.constants.WebSocketConstants;
import com.sky.websocket.handler.SpringWebSocketHandler;
import com.sky.websocket.service.RedisWebSocketService;

@Controller
@RequestMapping("/websocket")
public class WebSocketController {
	@Resource
	public SpringWebSocketHandler springWebSocketHandler;
	
	@Resource
	public RedisWebSocketService redisWebSocketService;
	
	
	// 服务端Spring MVC拦截该HTTP请求，将HTTP Session载入Websocket Session中，建立会话 
    @RequestMapping(value="/login")
    public String login(HttpSession session){
        System.out.println("用户登录建立Websocket连接");
//        session.setAttribute("userId", "shadiao");
        return "/websocket/home.jsp";
    }
      
    // 模拟服务端发送消息，其中可实现消息的广发或指定对象发送
    @RequestMapping(value = "/message")
    public String sendMessage(HttpSession session){
        double rand = Math.ceil(Math.random()*100);
        String msg = "Websocket测试消息" + rand;
        
        redisWebSocketService.sendMessage(session.getId(), WebSocketConstants.TEST_KEYID, msg);
        return "/websocket/message.jsp";
    }

}
