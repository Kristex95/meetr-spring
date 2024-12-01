package com.cybernetics.meetr.repository;

import com.cybernetics.meetr.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {
	Event findByCreatorId(Long id);
	List<Event> findByParticipantsId(Long participantId);
}
