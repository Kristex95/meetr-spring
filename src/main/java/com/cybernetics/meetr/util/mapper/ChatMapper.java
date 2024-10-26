package com.cybernetics.meetr.util.mapper;

import com.cybernetics.meetr.dto.chat.ChatBaseDto;
import com.cybernetics.meetr.dto.chat.ChatDto;
import com.cybernetics.meetr.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChatMapper {
	ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

	Chat fromDto(ChatBaseDto dto);

	ChatBaseDto toBaseDto(Chat message);

	@Mapping(source = "event.id", target = "eventId")
	ChatDto toDto(Chat message);
}
