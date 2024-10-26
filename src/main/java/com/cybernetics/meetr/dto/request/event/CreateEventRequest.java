package com.cybernetics.meetr.dto.request.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateEventRequest {
	private Long creatorId;
	private String name;
	private String description;
}
