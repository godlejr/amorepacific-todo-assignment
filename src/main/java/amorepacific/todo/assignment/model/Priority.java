package amorepacific.todo.assignment.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
public class Priority {
    @Schema(description = "중요도", example = "B")
    private char importance;
    @Schema(description = "순서", example = "0")
    private int order;

}
