package com.cybernetics.meetr.dto.event;

import com.cybernetics.meetr.dto.chat.ChatDto;
import com.cybernetics.meetr.dto.user.UserDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EventBaseDto {
	private String name;
	private Long creatorId;
	private String description;
	private List<ChatDto> chats;
	private LocalDateTime startDateTime;
	private List<UserDto> participants;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
