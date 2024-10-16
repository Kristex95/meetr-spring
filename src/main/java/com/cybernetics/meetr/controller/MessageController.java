package com.cybernetics.meetr.controller;

import com.cybernetics.meetr.dto.Message.MessageDto;
import com.cybernetics.meetr.dto.response.Response;
import com.cybernetics.meetr.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

	private final MessageService messageService;

	@GetMapping("/{userId}")
	public ResponseEntity<Response<List<MessageDto>>> getAllMessagesByUserId(@PathVariable Long userId){
		return ResponseEntity.ok(Response.body(messageService.getAllMessagesByUserId(userId)));
	}
}
