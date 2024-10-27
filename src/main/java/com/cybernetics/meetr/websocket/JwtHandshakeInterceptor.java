package com.cybernetics.meetr.websocket;

import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.service.UserService;
import com.cybernetics.meetr.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

	private final UserService userService;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request,
								   ServerHttpResponse response,
								   WebSocketHandler wsHandler,
								   Map<String, Object> attributes) throws Exception {
		String jwtToken = request.getHeaders().getFirst("Authorization");
		if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
			jwtToken = jwtToken.substring(7); // Remove "Bearer " prefix
			// Validate and extract user info from JWT
			User userDetails = userService.getUserDetailsFromToken(jwtToken);
			attributes.put("user", userDetails);
		}
		return super.beforeHandshake(request, response, wsHandler, attributes);
	}
}
