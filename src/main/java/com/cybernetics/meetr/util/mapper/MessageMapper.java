package com.cybernetics.meetr.util.mapper;

import com.cybernetics.meetr.dto.Message.MessageBaseDto;
import com.cybernetics.meetr.dto.Message.MessageDto;
import com.cybernetics.meetr.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {
	MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

	Message fromDto(MessageBaseDto dto);

	MessageBaseDto toBaseDto(Message message);

	@Mapping(source = "sender.id", target = "senderId")
	MessageDto toDto(Message message);
}
