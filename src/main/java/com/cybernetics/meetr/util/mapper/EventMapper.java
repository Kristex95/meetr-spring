package com.cybernetics.meetr.util.mapper;

import com.cybernetics.meetr.dto.event.EventBaseDto;
import com.cybernetics.meetr.dto.event.EventDto;
import com.cybernetics.meetr.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventMapper {
	EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

	@Mapping(source = "creatorId", target = "creator.id")
	Event fromDto(EventBaseDto dto);

	EventBaseDto toBaseDto(Event message);

	@Mapping(source = "creator.id", target = "creatorId")
	EventDto toDto(Event message);
}
