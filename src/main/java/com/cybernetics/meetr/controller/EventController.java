package com.cybernetics.meetr.controller;

import com.cybernetics.meetr.dto.event.EventDto;
import com.cybernetics.meetr.dto.request.event.CreateEventRequest;
import com.cybernetics.meetr.dto.response.Response;
import com.cybernetics.meetr.entity.Event;
import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.repository.EventRepository;
import com.cybernetics.meetr.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

	private final EventService eventService;

	@GetMapping
	public ResponseEntity<Response<List<EventDto>>> getAllEvents() {
		return ResponseEntity.ok(Response.body(eventService.getAllEvents()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Response<EventDto>> getEventById(@PathVariable Long id) {
		return ResponseEntity.ok(Response.body(eventService.getEvent(id)));
	}

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
	public ResponseEntity<Void> deleteEvent(@PathVariable Long id, @AuthenticationPrincipal User user) {
		final Event event = eventService.getById(id);
		try {
			if(event != null && Objects.equals(event.getCreator().getId(), user.getId())) {
				eventService.deleteEvent(id);
				return ResponseEntity.status(HttpStatus.CREATED).build();
			}
			else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		}
		catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
