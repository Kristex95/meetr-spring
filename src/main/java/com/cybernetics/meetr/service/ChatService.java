package com.cybernetics.meetr.service;

import com.cybernetics.meetr.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatRepository chatRepository;
}
