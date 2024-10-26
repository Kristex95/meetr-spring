package com.cybernetics.meetr.util.mapper;

import com.cybernetics.meetr.dto.message.MessageBaseDto;
import com.cybernetics.meetr.dto.message.MessageDto;
import com.cybernetics.meetr.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {
	MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

	@Mapping(source = "senderId", target = "sender.id")
	@Mapping(source = "chatId", target = "chat.id")
	Message fromDto(MessageBaseDto dto);

	MessageBaseDto toBaseDto(Message message);

	@Mapping(source = "sender.id", target = "senderId")
	@Mapping(source = "chat.id", target = "chatId")
	MessageDto toDto(Message message);
}
