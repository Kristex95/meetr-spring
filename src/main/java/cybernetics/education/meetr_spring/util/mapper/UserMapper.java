package cybernetics.education.meetr_spring.util.mapper;

import cybernetics.education.meetr_spring.dto.UserBaseDto;
import cybernetics.education.meetr_spring.dto.UserDto;
import cybernetics.education.meetr_spring.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User fromDro(UserBaseDto dto);

    UserBaseDto toBaseDto(User user);

    UserDto toDto(User user);
}
