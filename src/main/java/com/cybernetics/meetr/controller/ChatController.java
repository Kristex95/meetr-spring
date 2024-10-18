package com.cybernetics.meetr.controller;

import com.cybernetics.meetr.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	//todo тестовый ендпоинт. должен принимать тело
	@PostMapping("/{chatId}/users/{userId}")
	public ResponseEntity<Void> addUserToChat(@PathVariable Long chatId, @PathVariable Long userId) {
		chatService.addUserToChat(chatId, userId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
