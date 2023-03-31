package amorepacific.todo.assignment.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class User extends AbstractEntity {
    @NotBlank(message = "이름은 필수 입력 값입니다")
    private String name;
}
