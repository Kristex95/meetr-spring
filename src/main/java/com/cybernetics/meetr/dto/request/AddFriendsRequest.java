package com.cybernetics.meetr.dto.request;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddFriendsRequest {
	List<Long> newFriendsIds;
}
