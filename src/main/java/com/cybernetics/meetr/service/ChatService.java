package com.cybernetics.meetr.service;

import com.cybernetics.meetr.dto.chat.ChatBaseDto;
import com.cybernetics.meetr.dto.chat.ChatDto;
import com.cybernetics.meetr.repository.ChatRepository;
import com.cybernetics.meetr.util.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatRepository chatRepository;

	public List<ChatDto> getAllChats() {
		return chatRepository.findAll()
				.stream().map(ChatMapper.INSTANCE::toDto)
				.toList();
	}
}
