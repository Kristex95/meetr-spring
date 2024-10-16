package com.cybernetics.meetr.util.mapper;

import com.cybernetics.meetr.dto.User.UserBaseDto;
import com.cybernetics.meetr.dto.User.UserDto;
import com.cybernetics.meetr.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User fromDto(UserBaseDto dto);

    UserBaseDto toBaseDto(User user);

    UserDto toDto(User user);
}
