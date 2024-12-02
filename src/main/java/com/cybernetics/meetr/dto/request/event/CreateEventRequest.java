package com.cybernetics.meetr.dto.request.event;

import com.cybernetics.meetr.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateEventRequest {
	private String name;
	private String description;
	private List<UserDto> participants;
}
