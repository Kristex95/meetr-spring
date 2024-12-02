package com.cybernetics.meetr.controller;

import com.cybernetics.meetr.dto.chat.ChatDto;
import com.cybernetics.meetr.dto.event.EventDto;
import com.cybernetics.meetr.dto.user.UserBaseDto;
import com.cybernetics.meetr.dto.user.UserDto;
import com.cybernetics.meetr.dto.response.Response;
import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.service.UserService;
import com.cybernetics.meetr.util.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<Response<List<UserDto>>> getAllUsers() {
		return ResponseEntity.ok(Response.body(userService.getAllUsers()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Response<UserDto>> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(Response.body(userService.getUser(id)));
	}

	@GetMapping("/email")
	public ResponseEntity<Response<UserDto>> getUserByEmail(@RequestBody String email){
		return ResponseEntity.ok(Response.body(userService.getUser(email)));
	}

	@Operation(
			summary = "Get user by JWT token",
			description = "Returns a User by jwtToken passed as Authorization in request headers"
	)
	@GetMapping("/token")
	public ResponseEntity<Response<UserDto>> getUserByJwtToken(@RequestHeader("Authorization") String token) {
		final String jwtToken = token.substring(7);
		return ResponseEntity.ok(
				Response.body(UserMapper.INSTANCE.toDto(userService.getUserDetailsFromToken(jwtToken)))
		);
	}

	@Operation(
			summary = "Get user's available chats",
			description = "Returns an array of Chats if jwtToken is passed as Authorization in request headers"
	)
	@GetMapping("/chats")
	public ResponseEntity<Response<List<ChatDto>>> getCurrentUserChats(@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(Response.body(userService.getAllChatsByUserId(user.getId())));
	}

	@Operation(
			summary = "Get user's available chats",
			description = "Returns an array of Chats if jwtToken is passed as Authorization in request headers"
	)
	@GetMapping("/events")
	public ResponseEntity<Response<List<EventDto>>> getCurrentUserEvents(@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(Response.body(userService.getAllEventsByUserId(user.getId())));
	}

	@PostMapping
	public ResponseEntity<Response<UserDto>> createUser(@RequestBody UserBaseDto user) {
		final UserDto createdUser = userService.createUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.body(createdUser));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Response<UserDto>> updateUser(@RequestBody UserBaseDto userDetails) {
		final UserDto updatedUser = userService.updateUser(userDetails);
		return ResponseEntity.ok(Response.body(updatedUser));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Response<Void>> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok(Response.message("Successfully deleted user"));
	}
}