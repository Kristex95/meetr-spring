package com.cybernetics.meetr.service;

import com.cybernetics.meetr.dto.chat.ChatBaseDto;
import com.cybernetics.meetr.dto.chat.ChatDto;
import com.cybernetics.meetr.dto.message.MessageDto;
import com.cybernetics.meetr.entity.Chat;
import com.cybernetics.meetr.entity.Message;
import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.repository.ChatRepository;
import com.cybernetics.meetr.repository.UserRepository;
import com.cybernetics.meetr.util.mapper.ChatMapper;
import com.cybernetics.meetr.util.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatRepository chatRepository;
	private final UserService userService;
	private final UserRepository userRepository;

	public Chat getById(Long id) {
		return chatRepository.findById(id)
				.orElseThrow(() -> new RuntimeException(String.format("Chat with id: %s not found", id)));
	}

	public List<ChatDto> getAllChats() {
		return chatRepository.findAll()
				.stream().map(ChatMapper.INSTANCE::toDto)
				.toList();
	}

	public List<MessageDto> getChatMessages(Long chatId) {
		final Chat chat = getById(chatId);
		final List<Message> messages = chat.getMessages();
		return messages.stream()
				.sorted(Comparator.comparing(Message::getCreatedAt)) // Sort by createdAt
				.map(MessageMapper.INSTANCE::toDto)
				.toList();
	}

	//TODO need rework
	public void removeUser(Long id, Long userId) {
		final Chat chat = getById(id);
		final User user = userService.getById(userId);
		chat.getUsers().removeIf(u -> Objects.equals(u.getId(), userId));

		user.getChatIds().removeIf(chatId -> Objects.equals(chatId, id));
		chatRepository.save(chat);
		userRepository.save(user);
	}
}
