package com.cybernetics.meetr.websocket;

import com.cybernetics.meetr.dto.message.MessageBaseDto;
import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.service.MessageService;
import com.cybernetics.meetr.websocket.dto.WsChannel;
import com.cybernetics.meetr.websocket.dto.WsType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WebSocketMessagingHandler extends TextWebSocketHandler {

	private final ObjectMapper objectMapper;
	private final MessageService messageService;
	private final static String DATA_NODE = "data";
	private final static String CHANNEL_NODE = "channel";
	private final static String TYPE_NODE = "type";

	private final Map<Long, List<WebSocketSession>> chatSubscriptions = new HashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("New WebSocket connection established: " + session.getId());
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage rawMessage) throws Exception {
		User user = (User) session.getAttributes().get("user");
		String payload = rawMessage.getPayload();
		System.out.println("Message from user: " + user.getUsername() + " - " + payload);

		// Deserialize the JSON to a Java object
		final ObjectNode request;
		try {
			request = objectMapper.readValue(payload, ObjectNode.class);
		}
		catch (Exception exception){
			session.sendMessage(new TextMessage("Could not parse json. Error: " + exception.getMessage() ));
			return;
		}

		final String channel = request.get(CHANNEL_NODE).textValue();
		final String type = request.get(TYPE_NODE).textValue();
		final JsonNode data = request.get(DATA_NODE);

		if(!channel.equalsIgnoreCase(WsChannel.MESSAGE.getName())) return;

		if(type.equalsIgnoreCase(WsType.SUBSCRIBE.getName())){
			final Long chatId = data.get("chatId").asLong();
			subscribeToChat(chatId, session);
		}
		else if(type.equalsIgnoreCase(WsType.SEND.getName())){
			final MessageBaseDto message = objectMapper.convertValue(data, MessageBaseDto.class);
			saveMessage(message, user);
			sendMessageToSubscribers(message);
		}
	}

	private void saveMessage(MessageBaseDto message, User user) {
		message.setSenderId(user.getId());
		messageService.saveMessage(message);
		System.out.println("Saved Message: " + message);
	}

	private void subscribeToChat(Long chatId, WebSocketSession session) {
		chatSubscriptions.putIfAbsent(chatId, new ArrayList<>());
		chatSubscriptions.get(chatId).add(session); // Add the session to the list of subscribers
		System.out.println("User subscribed to chatId: " + chatId);
	}

	private void sendMessageToSubscribers(MessageBaseDto message) throws Exception {
		Long chatId = message.getChatId(); // Assuming message has a getChatId() method
		List<WebSocketSession> subscribers = chatSubscriptions.get(chatId);

		if (subscribers != null) {
			for (WebSocketSession subscriber : subscribers) {
				// Create a JSON representation of the message to send
				String messageJson = objectMapper.writeValueAsString(message);
				subscriber.sendMessage(new TextMessage(messageJson));
			}
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("WebSocket connection closed: " + session.getId());

		// Remove the session from all chat subscriptions
		for (Map.Entry<Long, List<WebSocketSession>> entry : chatSubscriptions.entrySet()) {
			List<WebSocketSession> sessions = entry.getValue();
			sessions.removeIf(s -> s.getId().equals(session.getId())); // Remove the session by ID
		}
	}
}