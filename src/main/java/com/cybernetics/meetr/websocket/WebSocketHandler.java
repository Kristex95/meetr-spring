package com.cybernetics.meetr.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

	private final ObjectMapper objectMapper;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("New WebSocket connection established: " + session.getId());
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		System.out.println("Received JSON message: " + payload);

		// Deserialize the JSON to a Java object
		WsMessage myMessage = objectMapper.readValue(payload, WsMessage.class);
		System.out.println("Parsed Message: " + myMessage);

		// Prepare a response
		WsMessage response = new WsMessage("Hello, " + myMessage.getMessage(), "testSender"); //todo change sender
		String jsonResponse = objectMapper.writeValueAsString(response);

		// Send a response back as JSON
		session.sendMessage(new TextMessage(jsonResponse));
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("WebSocket connection closed: " + session.getId());
	}
}