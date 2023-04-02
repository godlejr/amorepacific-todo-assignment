package amorepacific.todo.assignment.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    @Schema(description = "에러 코드", example = "400 or 500")
    private int code;

    @Schema(description = "에러 메시지", example = "Validation 메시지 or 내부 오류")
    private String message;

    @Schema(description = "에러 로그", example = "Validation Exception 로그 or 다른 Exception 로그")
    private String log;
}