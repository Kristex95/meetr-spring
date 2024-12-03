package com.cybernetics.meetr.controller;

import com.cybernetics.meetr.dto.chat.ChatDto;
import com.cybernetics.meetr.dto.event.EventDto;
import com.cybernetics.meetr.dto.request.AddFriendsRequest;
import com.cybernetics.meetr.dto.request.FriendsPaginatedRequest;
import com.cybernetics.meetr.dto.user.UserBaseDto;
import com.cybernetics.meetr.dto.user.UserDto;
import com.cybernetics.meetr.dto.response.Response;
import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.service.UserService;
import com.cybernetics.meetr.util.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

	@GetMapping("/find")
	public ResponseEntity<Response<List<UserDto>>> findUsersByUsernamePart(@RequestParam String username) {
		return ResponseEntity.ok(Response.body(
				userService.findUsersByUsernamePart(username).stream().map(UserMapper.INSTANCE::toDto).toList()
		));
	}

	@GetMapping("/friends")
	public ResponseEntity<Response<List<UserDto>>> getAllFriends(@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(Response.body(
				userService.getFriends(user.getId())
		));
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

	@PutMapping()
	public ResponseEntity<Response<UserDto>> updateUser(@AuthenticationPrincipal User user, @RequestBody UserBaseDto userDetails) {
		final User existingUser = userService.getById(user.getId());
		final UserDto updatedUser = userService.updateUser(existingUser, userDetails);
		return ResponseEntity.ok(Response.body(updatedUser));
	}

	@PostMapping("/addFriend/{friendId}")
	public ResponseEntity<Response<Void>> addFriend(@AuthenticationPrincipal User user, @PathVariable Long friendId) {
		final boolean isUpdated = userService.addFriend(user.getId(), friendId);
		if(isUpdated)
			return ResponseEntity.ok().build();
		else
			return ResponseEntity.internalServerError().build();
	}

	@PostMapping("/addFriends")
	public ResponseEntity<Response<Void>> addFriends(@AuthenticationPrincipal User user, @RequestBody AddFriendsRequest addFriendsRequest) {
		final boolean isUpdated = userService.addFriends(user.getId(), addFriendsRequest.getNewFriendsIds());
		if(isUpdated)
			return ResponseEntity.ok().build();
		else
			return ResponseEntity.internalServerError().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Response<Void>> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok(Response.message("Successfully deleted user"));
	}
}