package com.cybernetics.meetr.websocket;

import com.cybernetics.meetr.dto.message.MessageBaseDto;
import com.cybernetics.meetr.service.MessageService;
import com.cybernetics.meetr.websocket.dto.WsChannel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

	private final ObjectMapper objectMapper;
	private final MessageService messageService;
	private final static String DATA_NODE = "data";
	private final static String CHANNEL_NODE = "channel";

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("New WebSocket connection established: " + session.getId());
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage rawMessage) throws Exception {
		String payload = rawMessage.getPayload();
		System.out.println("Received JSON message: " + payload);

		// Deserialize the JSON to a Java object
		ObjectNode request = objectMapper.readValue(payload, ObjectNode.class);
		final Optional<String> channelOptional = Optional.ofNullable(request.get(CHANNEL_NODE))
				.map(JsonNode::textValue);
		if(channelOptional.isEmpty()) return;
		final String channel = channelOptional.get();

		final JsonNode data = request.get(DATA_NODE);

		if(channel.equalsIgnoreCase(WsChannel.MESSAGE.getName())){
			final MessageBaseDto message = objectMapper.convertValue(data, MessageBaseDto.class);
			messageService.saveMessage(message);
			System.out.println("Saved Message: " + message);
		}

		// Prepare a response
		WsResponse response = WsResponse.builder()
				.build(); //todo change sender
		String jsonResponse = objectMapper.writeValueAsString(response);

		// Send a response back as JSON
		session.sendMessage(new TextMessage(jsonResponse));
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("WebSocket connection closed: " + session.getId());
	}
}