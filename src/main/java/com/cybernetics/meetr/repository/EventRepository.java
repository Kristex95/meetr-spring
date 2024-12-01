package com.cybernetics.meetr.repository;

import com.cybernetics.meetr.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
	Event findByCreatorId(Long id);
	List<Event> findByParticipantsId(Long participantId);
}
