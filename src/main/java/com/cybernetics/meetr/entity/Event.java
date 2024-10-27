package com.cybernetics.meetr.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User creator;

	private String description;

	private LocalDateTime startDateTime;

	@OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Chat> chats;

	@ManyToMany
	@JoinTable(
			name = "event_users",
			joinColumns = @JoinColumn(name = "event_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private List<User> participants;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = true)
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