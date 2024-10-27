package com.cybernetics.meetr.controller;

import com.cybernetics.meetr.dto.request.RegistrationRequest;
import com.cybernetics.meetr.dto.request.auth.LoginRequest;
import com.cybernetics.meetr.dto.response.AuthResponse;
import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.service.UserService;
import com.cybernetics.meetr.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserService userService;

	private final BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest user) {
		userService.registerUser(user);
		return ResponseEntity.ok("User registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
		User user = userService.getByUsername(loginRequest.getUsername());
		if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			return ResponseEntity.status(401).body("Invalid username or password");
		}

		String token = JwtUtil.generateToken(user.getUsername());
		return ResponseEntity.ok(new AuthResponse(token));
	}
}
