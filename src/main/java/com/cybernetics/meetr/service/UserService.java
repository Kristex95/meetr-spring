package com.cybernetics.meetr.service;

import com.cybernetics.meetr.dto.user.UserBaseDto;
import com.cybernetics.meetr.dto.user.UserDto;
import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.repository.UserRepository;
import com.cybernetics.meetr.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public List<UserDto> getAllUsers() {
		return userRepository.findAll()
				.stream().map(UserMapper.INSTANCE::toDto)
				.toList();
	}

	public User getById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException(String.format("Cant find user with id: %s", id)));
	}

	public User getByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException(String.format("Cant find user with email: %s", email)));
	}

	public UserDto getUser(Long id) {
		return UserMapper.INSTANCE.toDto(getById(id));
	}

	public UserDto getUser(String email) {
		return UserMapper.INSTANCE.toDto(getByEmail(email));
	}

	public UserDto createUser(UserBaseDto userDto) {
		final User newUser = UserMapper.INSTANCE.fromDto(userDto);
		final User user = userRepository.save(newUser);
		return UserMapper.INSTANCE.toDto(user);
	}

	public UserDto updateUser(Long id, UserBaseDto userDetails) {
		final User user = getById(id);
		user.setUsername(userDetails.getUsername());
		user.setEmail(userDetails.getEmail());
		return UserMapper.INSTANCE.toDto(userRepository.save(user));
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	public UserDto registerUser(String username, String email, String password) {
		final User user = User.builder().username(username).email(email).password(passwordEncoder.encode(password)).build();
		return UserMapper.INSTANCE.toDto(userRepository.save(user));
	}
}