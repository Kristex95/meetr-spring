package com.cybernetics.meetr.service;

import com.cybernetics.meetr.dto.message.MessageBaseDto;
import com.cybernetics.meetr.dto.message.MessageDto;
import com.cybernetics.meetr.repository.MessageRepository;
import com.cybernetics.meetr.util.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

	private final MessageRepository messageRepository;

	public List<MessageDto> getAllMessagesByUserId(Long userId){
		return messageRepository.findBySenderId(userId).stream().map(MessageMapper.INSTANCE::toDto).toList();
	}

	public void saveMessage(MessageBaseDto messageDto) {
		messageRepository.save(MessageMapper.INSTANCE.fromDto(messageDto));
	}
}
