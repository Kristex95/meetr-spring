package cybernetics.education.meetr_spring.dto.Message;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto extends MessageBaseDto {
	private Long id;
}
