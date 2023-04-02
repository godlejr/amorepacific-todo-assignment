package amorepacific.todo.assignment.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Priority {

    @Builder
    public Priority(char importance, int order) {
        this.importance = importance;
        this.order = order;
    }

    @Schema(description = "중요도", example = "B")
    private char importance;
    @Schema(description = "순서", example = "0")
    private int order;

}
