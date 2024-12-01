package com.cybernetics.meetr.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	private String email;

	private String password;

	@ElementCollection
	@CollectionTable(
			name = "event_users",
			joinColumns = @JoinColumn(name = "user_id")
	)
	@Column(name = "event_id")
	private List<Long> eventIds;

	@ElementCollection
	@CollectionTable(
			name = "chat_users",
			joinColumns = @JoinColumn(name = "user_id")
	)
	@Column(name = "chat_id")
	private List<Long> chatIds = new ArrayList<>();

	@Column(nullable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}