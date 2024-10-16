package com.cybernetics.meetr.dto.Message;

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
	private String content;
	private Long senderId;
//	private Long chatId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
