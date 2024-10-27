package com.cybernetics.meetr.repository;

import com.cybernetics.meetr.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository extends JpaRepository<Event, Long> {
	Event findByCreatorId(Long id);
}
