package com.cybernetics.meetr.controller;

import com.cybernetics.meetr.dto.request.event.CreateEventRequest;
import com.cybernetics.meetr.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

	private final EventService eventService;

	//todo тестовый ендпоинт. должен принимать тело
	@PostMapping("/{eventId}/users/{userId}")
	public ResponseEntity<Void> addParticipantToEvent(@PathVariable Long eventId, @PathVariable Long userId) {
		eventService.addUserToEvent(eventId, userId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping
	public ResponseEntity<Void> createEvent(@RequestBody CreateEventRequest createRequest) {
		eventService.createEvent(createRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
//		deleteEventRequest.getUserKey() //todo check if user is a creator of event
		try {
			eventService.deleteEvent(id);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
