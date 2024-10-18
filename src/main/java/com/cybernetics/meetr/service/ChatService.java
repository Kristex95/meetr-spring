package com.cybernetics.meetr.service;

import com.cybernetics.meetr.entity.Chat;
import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.repository.ChatRepository;
import com.cybernetics.meetr.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatRepository chatRepository;
	private final UserRepository userRepository;

	@Transactional
	public void addUserToChat(Long chatId, Long userId) {
		Chat chat = chatRepository.findById(chatId)
				.orElseThrow(() -> new RuntimeException("Chat not found"));

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (!chat.getParticipants().contains(user)) {
			chat.getParticipants().add(user);
			chatRepository.save(chat);
		}
	}
}
