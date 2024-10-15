package cybernetics.education.meetr_spring.util.mapper;

import cybernetics.education.meetr_spring.dto.Message.MessageBaseDto;
import cybernetics.education.meetr_spring.dto.Message.MessageDto;
import cybernetics.education.meetr_spring.model.Message;
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
