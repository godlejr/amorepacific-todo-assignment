package amorepacific.todo.assignment.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AbstractEntity {

    @Builder
    public User(String name) {
        this.name = name;
    }

    @Schema(description = "담당자", example = "김동주")
    private String name;
}
