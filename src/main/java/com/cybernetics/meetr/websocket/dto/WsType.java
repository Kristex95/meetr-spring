package com.cybernetics.meetr.websocket.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum WsType {
	SUBSCRIBE("subscribe"), UNSUBSCRIBE("unsubscribe"), SEND("send");

	@JsonValue
	private final String name;

	WsType(String name) { this.name = name; }
}
