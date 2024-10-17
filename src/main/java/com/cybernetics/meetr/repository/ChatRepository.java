package com.cybernetics.meetr.repository;

import com.cybernetics.meetr.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
