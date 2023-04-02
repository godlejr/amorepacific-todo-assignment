package amorepacific.todo.assignment.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public abstract class AbstractEntity {

    @Schema(description = "ID", example = "1", required = true)
    private long id;
}
