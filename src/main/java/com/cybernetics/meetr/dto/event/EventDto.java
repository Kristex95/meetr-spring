package com.cybernetics.meetr.dto.event;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class EventDto extends EventBaseDto{
	private Long id;
}
