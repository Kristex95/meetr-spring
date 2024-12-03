package com.cybernetics.meetr.repository;

import com.cybernetics.meetr.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	Optional<User> findByEmail(String email);
	Optional<User> findByUsername(String username);

	@Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :usernamePart, '%'))")
	List<User> findByUsernameContaining(@Param("usernamePart") String usernamePart, Pageable pageable);

	@Query("SELECT u.friends FROM User u WHERE u.id = :userId")
	Set<User> findFriendsByUserId(@Param("userId") Long userId);
}