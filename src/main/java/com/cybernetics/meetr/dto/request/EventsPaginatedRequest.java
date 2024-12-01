package com.cybernetics.meetr.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventsPaginatedRequest {
    private Integer page;
    private Integer size;
}
