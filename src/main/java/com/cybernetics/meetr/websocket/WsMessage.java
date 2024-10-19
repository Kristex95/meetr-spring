package com.cybernetics.meetr.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsMessage {
	private String message;
	private String sender;
}
