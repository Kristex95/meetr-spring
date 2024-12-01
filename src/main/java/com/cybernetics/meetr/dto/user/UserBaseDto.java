package com.cybernetics.meetr.dto.user;

import com.cybernetics.meetr.dto.event.EventDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserBaseDto {
    private String username;
    private String email;
    private String password;
	private List<Long> eventIds;
	private List<Long> chatIds;
}
