package com.cybernetics.meetr.config;

import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Arrays;
import java.util.List;
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
		// Extract query parameters from the URI
		String uri = request.getURI().toString();
		String jwtToken = null;

		// Parse the query parameters
		String[] queryParams = uri.split("\\?");
		if (queryParams.length > 1) {
			String[] pairs = queryParams[1].split("&");
			for (String pair : pairs) {
				String[] keyValue = pair.split("=");
				if (keyValue.length == 2 && "token".equals(keyValue[0])) {
					jwtToken = keyValue[1];
					break;
				}
			}
		}

		// Validate and process the JWT token
		if (jwtToken != null) {
			try {
				// Validate and extract user info from JWT
				User userDetails = userService.getUserDetailsFromToken(jwtToken);
				attributes.put("user", userDetails);
			} catch (Exception e) {
				// Handle invalid token
				System.err.println("Invalid JWT token: " + e.getMessage());
				return false;
			}
		}

		return super.beforeHandshake(request, response, wsHandler, attributes);
	}
}