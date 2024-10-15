package cybernetics.education.meetr_spring.service;

import cybernetics.education.meetr_spring.dto.Message.MessageBaseDto;
import cybernetics.education.meetr_spring.dto.Message.MessageDto;
import cybernetics.education.meetr_spring.repository.MessageRepository;
import cybernetics.education.meetr_spring.util.mapper.MessageMapper;
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
}
