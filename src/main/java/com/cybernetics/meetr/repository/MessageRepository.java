package com.cybernetics.meetr.repository;

import com.cybernetics.meetr.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findBySenderId(Long userId);
}
