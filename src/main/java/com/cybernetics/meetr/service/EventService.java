package com.cybernetics.meetr.service;

import com.cybernetics.meetr.dto.event.EventBaseDto;
import com.cybernetics.meetr.dto.event.EventDto;
import com.cybernetics.meetr.dto.request.EventsPaginatedRequest;
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
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    //TODO refactor!!! reuse service instead
    private final ChatRepository chatRepository;

    public Event getById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Cant find event with id: %s", id)));
    }

    public Event getByCreatorId(Long id) {
        return eventRepository.findByCreatorId(id);
    }

    public Page<EventDto> getEventsPaginated(EventsPaginatedRequest paginatedRequest) {
        final PageRequest pageable = PageRequest.of(
                paginatedRequest.getPage(),
                paginatedRequest.getSize(),
                Sort.Direction.ASC,
				"id");

        final Page<Event> events = eventRepository.findAll(pageable);
        return events.map(EventMapper.INSTANCE::toDto);
    }

    public List<User> getEventUsers(Long id) {
        final Event event = getById(id);
        return event.getParticipants();
    }

	@Transactional
    public void createEvent(CreateEventRequest createEventRequest, User creator) {
		Hibernate.initialize(creator.getChatIds());
		final List<UserDto> participants = new ArrayList<>();
		participants.add(UserMapper.INSTANCE.toDto(creator));
		participants.addAll(createEventRequest.getParticipants());

        final EventBaseDto newEvent = EventBaseDto.builder()
                .name(createEventRequest.getName())
                .description(createEventRequest.getDescription())
                .creatorId(creator.getId())
                .participants(participants)
                .build();
        Event event = EventMapper.INSTANCE.fromDto(newEvent);
        event = eventRepository.save(event);

        final Chat chat = Chat.builder()
                .event(event)
                .users(List.of(creator))
                .build();
        chatRepository.save(chat);
    }

    @Transactional
    public void addUserToEvent(Long chatId, Long userId) {
        Event event = eventRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        User user = userService.getById(userId);

        if (!event.getParticipants().contains(user)) {
            event.getParticipants().add(user);
            eventRepository.save(event);
        }
    }

    public void removeUser(Long id, Long userId) {
        final Event event = getById(id);
        final User user = userService.getById(userId);
        user.getChatIds().removeIf(chatId -> {
            final Chat chat = chatRepository.findById(chatId).orElseThrow();
            return chat.getEvent() == event;
        });
        userRepository.save(user);
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
