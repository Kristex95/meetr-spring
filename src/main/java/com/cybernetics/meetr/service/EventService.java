package com.cybernetics.meetr.service;

import com.cybernetics.meetr.dto.event.EventBaseDto;
import com.cybernetics.meetr.dto.request.event.CreateEventRequest;
import com.cybernetics.meetr.entity.Chat;
import com.cybernetics.meetr.entity.Event;
import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.repository.ChatRepository;
import com.cybernetics.meetr.repository.EventRepository;
import com.cybernetics.meetr.repository.UserRepository;
import com.cybernetics.meetr.util.mapper.EventMapper;
import com.cybernetics.meetr.util.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final ChatRepository chatRepository;

	public void createEvent(CreateEventRequest createEventRequest) {
		final LocalDateTime timeNow = LocalDateTime.now();

		final User user = userRepository.findById(createEventRequest.getCreatorId()).orElseThrow();

		final EventBaseDto newEvent = EventBaseDto.builder()
				.name(createEventRequest.getName())
				.description(createEventRequest.getDescription())
				.creatorId(createEventRequest.getCreatorId())
				.createdAt(timeNow)
				.participants(List.of(UserMapper.INSTANCE.toDto(user)))
				.build();
		final Event event = EventMapper.INSTANCE.fromDto(newEvent);

		final Chat chat = Chat.builder()
				.event(event)
				.createdAt(timeNow)
				.build();

		eventRepository.save(event);
		chatRepository.save(chat);
	}

	@Transactional
	public void addUserToEvent(Long chatId, Long userId) {
		Event event = eventRepository.findById(chatId)
				.orElseThrow(() -> new RuntimeException("Event not found"));

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (!event.getParticipants().contains(user)) {
			event.getParticipants().add(user);
			eventRepository.save(event);
		}
	}

	public void deleteEvent(Long id) {
		final Event requestedEvent = eventRepository
				.findById(id)
				.orElseThrow();
		eventRepository.delete(requestedEvent);
	}
}
