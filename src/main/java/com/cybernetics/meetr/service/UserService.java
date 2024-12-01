package com.cybernetics.meetr.service;

import com.cybernetics.meetr.dto.chat.ChatDto;
import com.cybernetics.meetr.dto.event.EventDto;
import com.cybernetics.meetr.dto.request.RegistrationRequest;
import com.cybernetics.meetr.dto.user.UserBaseDto;
import com.cybernetics.meetr.dto.user.UserDto;
import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.repository.ChatRepository;
import com.cybernetics.meetr.repository.EventRepository;
import com.cybernetics.meetr.repository.UserRepository;
import com.cybernetics.meetr.util.mapper.ChatMapper;
import com.cybernetics.meetr.util.mapper.EventMapper;
import com.cybernetics.meetr.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.cybernetics.meetr.util.jwt.JwtUtil.extractUsername;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final ChatRepository chatRepository;
	private final EventRepository eventRepository;
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

	public User getByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException(String.format("Cant find user with username: %s", username)));
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

	public UserDto registerUser(RegistrationRequest registrationRequest) {
		Optional<User> existingUser = userRepository.findByUsername(registrationRequest.getUsername());
		existingUser = userRepository.findByEmail(registrationRequest.getEmail());
		if (existingUser.isEmpty()) {
			final User user = User.builder()
							.username(registrationRequest.getUsername())
							.email(registrationRequest.getEmail())
							.password(passwordEncoder.encode(registrationRequest.getPassword()))
							.build();
			return UserMapper.INSTANCE.toDto(userRepository.save(user));
		} else {
			throw new RuntimeException("User with this username or email already exists");
		}
	}

	public User getUserDetailsFromToken(String jwtToken) {
		final String username = extractUsername(jwtToken);
		return getByUsername(username);
	}

	public List<ChatDto> getAllChatsByUserId(Long userId) {
		final User user = getById(userId);
		final List<Long> chatIds = user.getChatIds();
		return chatIds.stream()
				.map(chatRepository::findById)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(ChatMapper.INSTANCE::toDto)
				.toList();
	}

	public List<EventDto> getAllEventsByUserId(Long id) {
		return eventRepository.findByParticipantsId(id).stream().map(EventMapper.INSTANCE::toDto).toList();
	}
}