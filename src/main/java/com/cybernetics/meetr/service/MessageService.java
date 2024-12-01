package com.cybernetics.meetr.service;

import com.cybernetics.meetr.dto.message.MessageBaseDto;
import com.cybernetics.meetr.dto.message.MessageDto;
import com.cybernetics.meetr.dto.request.MessagesPaginatedRequest;
import com.cybernetics.meetr.entity.Event;
import com.cybernetics.meetr.entity.Message;
import com.cybernetics.meetr.repository.MessageRepository;
import com.cybernetics.meetr.util.mapper.EventMapper;
import com.cybernetics.meetr.util.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

	private final MessageRepository messageRepository;

	public Page<MessageDto> getPaginated(MessagesPaginatedRequest paginatedRequest) {
		final PageRequest pageable = PageRequest.of(
				paginatedRequest.getPage(),
				paginatedRequest.getSize(),
				Sort.Direction.ASC,
				"createdAt");

		final Page<Message> events = messageRepository.findAll(pageable);
		return events.map(MessageMapper.INSTANCE::toDto);
	}

	public List<MessageDto> getAllMessagesByUserId(Long userId){
		return messageRepository.findBySenderId(userId).stream().map(MessageMapper.INSTANCE::toDto).toList();
	}

	public void saveMessage(MessageBaseDto messageDto) {
		messageRepository.save(MessageMapper.INSTANCE.fromDto(messageDto));
	}
}
