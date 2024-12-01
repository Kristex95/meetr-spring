package com.cybernetics.meetr.controller;

import com.cybernetics.meetr.dto.chat.ChatDto;
import com.cybernetics.meetr.dto.message.MessageDto;
import com.cybernetics.meetr.dto.response.Response;
import com.cybernetics.meetr.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	@GetMapping
	public ResponseEntity<Response<List<ChatDto>>> getAllChats() {
		return ResponseEntity.ok(Response.body(chatService.getAllChats()));
	}

	@GetMapping("/{id}/messages")
	public ResponseEntity<Response<List<MessageDto>>> getChatMessages(@PathVariable Long id) {
		return ResponseEntity.ok(Response.body(chatService.getChatMessages(id)));
	}
}
