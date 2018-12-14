package com.sky.websocket.interceptor;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.sky.websocket.constants.WebSocketConstants;

@Component
public class SpringWebSocketHandlerInterceptor extends HttpSessionHandshakeInterceptor {
	
	@Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, 
    		WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        this.setListenerKey(request, attributes, WebSocketConstants.TEST_KEYID);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
 
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
    
    /**
     * 设置对应websocket的key值
     * @param request
     * @param attributes
     * @param preKey
     */
    protected void setListenerKey(ServerHttpRequest request, Map<String, Object> attributes, String keyId) {
    	if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            if (session != null && session.getId() != null) {
            	attributes.put(WebSocketConstants.LISTENER_KEY, session.getId() + keyId);
            }
        }
	}
    
}
