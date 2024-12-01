package com.cybernetics.meetr.repository;

import com.cybernetics.meetr.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChatRepository extends JpaRepository<Chat,Long>, JpaSpecificationExecutor<Chat> {
}
