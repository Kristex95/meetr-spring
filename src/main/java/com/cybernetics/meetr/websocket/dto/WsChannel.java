package com.cybernetics.meetr.websocket.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum WsChannel {
	MESSAGE("message");

	@JsonValue
	private final String name;

	WsChannel(String name) { this.name = name; }
}
