package com.cybernetics.meetr.controller;

import com.cybernetics.meetr.dto.message.MessageDto;
import com.cybernetics.meetr.dto.request.MessagesPaginatedRequest;
import com.cybernetics.meetr.dto.response.Response;
import com.cybernetics.meetr.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//TODO pagination
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

	private final MessageService messageService;

	@GetMapping
	public ResponseEntity<Response<Page<MessageDto>>> getMessagesPaginated(MessagesPaginatedRequest paginatedRequest) {
		return ResponseEntity.ok(Response.body(messageService.getPaginated(paginatedRequest)));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<Response<List<MessageDto>>> getAllMessagesByUserId(@PathVariable Long userId){
		return ResponseEntity.ok(Response.body(messageService.getAllMessagesByUserId(userId)));
	}
}
