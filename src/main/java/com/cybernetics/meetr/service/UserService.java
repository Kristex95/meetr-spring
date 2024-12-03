package com.cybernetics.meetr.service;

import com.cybernetics.meetr.dto.chat.ChatDto;
import com.cybernetics.meetr.dto.event.EventDto;
import com.cybernetics.meetr.dto.request.RegistrationRequest;
import com.cybernetics.meetr.dto.request.FriendsPaginatedRequest;
import com.cybernetics.meetr.dto.user.UserBaseDto;
import com.cybernetics.meetr.dto.user.UserDto;
import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.repository.ChatRepository;
import com.cybernetics.meetr.repository.EventRepository;
import com.cybernetics.meetr.repository.UserRepository;
import com.cybernetics.meetr.util.mapper.ChatMapper;
import com.cybernetics.meetr.util.mapper.EventMapper;
import com.cybernetics.meetr.util.mapper.UserMapper;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.cybernetics.meetr.util.jwt.JwtUtil.extractUsername;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final ChatRepository chatRepository;
	private final EventRepository eventRepository;
	private final PasswordEncoder passwordEncoder;

	public Page<UserDto> getFriends(FriendsPaginatedRequest paginatedRequest) {
		final Specification<User> spec = (root, query, criteriaBuilder) -> {
			// Join the 'friends' association of the User entity
			final Join<User, User> friendsJoin = root.join("friends");

			// Define the condition that the user must be a friend of the specified user ID

			// Create the base predicate that ensures the user is a friend of the specified user
			Predicate combinedPredicate = criteriaBuilder.equal(friendsJoin.get("id"), paginatedRequest.getUserId());

			// If search is provided, add the search predicate for username and email, case insensitive
			if (paginatedRequest.getSearch() != null && !paginatedRequest.getSearch().isEmpty()) {
				final String searchTerm = "%" + paginatedRequest.getSearch().toLowerCase() + "%";
				final Predicate searchPredicate = criteriaBuilder.or(
						criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), searchTerm),
						criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchTerm)
				);

				// Combine the search predicate with the friend condition
				combinedPredicate = criteriaBuilder.and(combinedPredicate, searchPredicate);
			}

			return combinedPredicate;
		};

		// Set the page size to 20 by default if it's null
		final int pageSize = (paginatedRequest.getSize() != null) ? paginatedRequest.getSize() : 20;
		final PageRequest pageable = PageRequest.of(
				paginatedRequest.getPage(),
				pageSize,
				Sort.Direction.ASC,
				"id"
		);

		// Apply the Specification along with pagination to the repository's findAll method
		final Page<User> users = userRepository.findAll(spec, pageable);

		// Convert the User entities to UserDto and return the result
		return users.map(UserMapper.INSTANCE::toDto);
	}

	public List<UserDto> getFriends(Long userId) {
		final Set<User> friends = userRepository.findFriendsByUserId(userId);
		return friends.stream().map(UserMapper.INSTANCE::toDto).toList();
	}

	//TODO list friends
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

	public List<User> findUsersByUsernamePart(String usernamePart) {
		final Pageable pageable = PageRequest.of(0, 20);
		return userRepository.findByUsernameContaining(usernamePart, pageable);
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

	//TODO finish
	public UserDto updateUser(User existingUser, UserBaseDto userDetails) {
		final User user = getByUsername(existingUser.getUsername());
		final String prevUsername = userDetails.getUsername();
		if(prevUsername != null) {
			final User userWithNewName = getByUsername(userDetails.getUsername());
			if(userWithNewName == null) {
				user.setUsername(userDetails.getUsername());
			}
			else {
				throw new IllegalArgumentException("Can't update user. User with username " + userDetails.getUsername() + " already already exists");
			}
		}
		final String email = userDetails.getEmail();
		if(email != null) {
			user.setEmail(userDetails.getEmail());
		}
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

	@Transactional
	public boolean addFriend(Long initiatorId, Long friendId) {
		try {
			User user = getById(initiatorId);
			User newFriend = getById(friendId);

			if (user == null || newFriend == null) {
				return false;
			}

			if(user == newFriend) {
				return false;
			}

			Set<User> friends = user.getFriends();
			if (friends == null) {
				friends = new HashSet<>();
			}

			if (!friends.add(newFriend)) { // Avoid duplicates
				return false; // Friend was already added
			}

			// Persist changes
			user.setFriends(friends);
			userRepository.save(user); // Ensure userRepository is used for saving

			return true;
		} catch (Exception ex) {
			// Log general error
			return false;
		}
	}

	public boolean addFriends(Long initiatorId, List<Long> newFriendsIds) {
		try {
			newFriendsIds.forEach(friend -> addFriend(initiatorId, friend));
			return true;
		}
		catch (Exception ex) {
			System.out.println(ex);
			return false;
		}
	}
}