package com.cybernetics.meetr.dto.chat;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto extends ChatBaseDto{
	private Long id;
}
