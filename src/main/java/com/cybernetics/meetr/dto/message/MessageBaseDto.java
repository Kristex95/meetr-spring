package com.cybernetics.meetr.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MessageBaseDto {
	private Long senderId;
	private Long chatId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
