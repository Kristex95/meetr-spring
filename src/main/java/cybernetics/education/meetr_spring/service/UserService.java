package cybernetics.education.meetr_spring.service;

import cybernetics.education.meetr_spring.dto.UserBaseDto;
import cybernetics.education.meetr_spring.dto.UserDto;
import cybernetics.education.meetr_spring.model.User;
import cybernetics.education.meetr_spring.repository.UserRepository;
import cybernetics.education.meetr_spring.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

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
		final User user = userRepository.save(UserMapper.INSTANCE.fromDro(userDto));
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