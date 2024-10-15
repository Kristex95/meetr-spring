package cybernetics.education.meetr_spring.dto.User;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends UserBaseDto {
    private Long id;
}
