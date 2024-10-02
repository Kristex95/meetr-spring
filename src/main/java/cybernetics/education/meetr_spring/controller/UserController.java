package cybernetics.education.meetr_spring.controller;

import cybernetics.education.meetr_spring.dto.UserBaseDto;
import cybernetics.education.meetr_spring.dto.UserDto;
import cybernetics.education.meetr_spring.dto.response.Response;
import cybernetics.education.meetr_spring.model.User;
import cybernetics.education.meetr_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private UserService userService;

	@GetMapping
	public ResponseEntity<Response<List<UserDto>>> getAllUsers() {
		return ResponseEntity.ok(Response.body(userService.getAllUsers()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Response<UserDto>> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(Response.body(userService.getUser(id)));
	}

	@GetMapping("/email")
	public ResponseEntity<Response<UserDto>> getUserByEmail(@RequestParam String email){
		return ResponseEntity.ok(Response.body(userService.getUser(email)));
	}

	@PostMapping
	public ResponseEntity<Response<UserDto>> createUser(@RequestBody UserBaseDto user) {
		final UserDto createdUser = userService.createUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.body(createdUser));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Response<UserDto>> updateUser(@PathVariable Long id, @RequestBody UserBaseDto userDetails) {
		final UserDto updatedUser = userService.updateUser(id, userDetails);
		return ResponseEntity.ok(Response.body(updatedUser));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Response<Void>> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok(Response.message("Successfully deleted user"));
	}
}