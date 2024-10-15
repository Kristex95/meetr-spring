package cybernetics.education.meetr_spring.controller;

import cybernetics.education.meetr_spring.dto.Message.MessageDto;
import cybernetics.education.meetr_spring.dto.response.Response;
import cybernetics.education.meetr_spring.service.MessageService;
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
