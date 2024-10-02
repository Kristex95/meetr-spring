package cybernetics.education.meetr_spring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private T body;
    private String message;

    public static <T> Response<T> message(String message) {
        return Response.<T>builder()
                .message(message)
                .build();
    }

    public static <T> Response<T> body(T body) {
        return Response.<T>builder()
                .body(body)
                .build();
    }
}
