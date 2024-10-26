package com.cybernetics.meetr.dto.chat;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ChatBaseDto {
	private Long eventId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
