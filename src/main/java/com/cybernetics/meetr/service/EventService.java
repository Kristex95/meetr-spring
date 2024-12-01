package com.cybernetics.meetr.service;

import com.cybernetics.meetr.dto.event.EventBaseDto;
import com.cybernetics.meetr.dto.event.EventDto;
import com.cybernetics.meetr.dto.request.event.CreateEventRequest;
import com.cybernetics.meetr.dto.user.UserDto;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final ChatRepository chatRepository;

	public Event getById(Long id) {
		return eventRepository.findById(id)
				.orElseThrow(() -> new RuntimeException(String.format("Cant find event with id: %s", id)));
	}

	public Event getByCreatorId(Long id) {
		return eventRepository.findByCreatorId(id);
	}

	public List<User> getEventUsers(Long id) {
		final Event event = getById(id);
		return event.getParticipants();
	}

	public void createEvent(CreateEventRequest createEventRequest) {
		final User user = userRepository.findById(createEventRequest.getCreatorId()).orElseThrow();

		final EventBaseDto newEvent = EventBaseDto.builder()
				.name(createEventRequest.getName())
				.description(createEventRequest.getDescription())
				.creatorId(createEventRequest.getCreatorId())
				.participants(List.of(UserMapper.INSTANCE.toDto(user)))
				.build();
		Event event = EventMapper.INSTANCE.fromDto(newEvent);
		event = eventRepository.save(event);

		final Chat chat = Chat.builder()
				.event(event)
				.users(List.of(user))
				.build();
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

	public List<EventDto> getAllEvents() {
		return eventRepository.findAll()
				.stream().map(EventMapper.INSTANCE::toDto)
				.toList();
	}

	public Chat getMainChatByEventId(Long id) {
		final List<Chat> chats = getAllChatsByEventId(id);
		return chats.get(0);
	}

	public List<Chat> getAllChatsByEventId(Long id) {
		final Event event = getById(id);
		return event.getChats();
	}
}
