package com.cybernetics.meetr.dto.chat;

import com.cybernetics.meetr.dto.user.UserDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ChatBaseDto {
	private Long eventId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<UserDto> users;
}
