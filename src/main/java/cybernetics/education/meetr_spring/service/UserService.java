package cybernetics.education.meetr_spring.service;

import cybernetics.education.meetr_spring.model.User;
import cybernetics.education.meetr_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	public User createUser(User user) {
		return userRepository.save(user);
	}

	public User updateUser(Long id, User userDetails) {
		User user = userRepository.findById(id).orElseThrow();
		user.setUsername(userDetails.getUsername());
		user.setEmail(userDetails.getEmail());
		return userRepository.save(user);
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	public User registerUser(String username, String email, String password) {
		User user = User.builder().username(username).email(email).password(passwordEncoder.encode(password)).build();
		return userRepository.save(user);
	}

	public User findByEmail(String email){
		return userRepository.findByEmail(email).orElse(null);
	}
}