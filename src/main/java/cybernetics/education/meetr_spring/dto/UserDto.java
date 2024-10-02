package cybernetics.education.meetr_spring.dto;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends UserBaseDto {
    private Long id;
}
