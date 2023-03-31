package amorepacific.todo.assignment.entity;

import amorepacific.todo.assignment.model.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
public class Todo extends AbstractEntity {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotBlank(message = "담당자는 필수 입력 값입니다")
    private User user;

    private Priority priority;

    @NotBlank(message = "업무 제목은 필수 입력 값입니다")
    private String task;

    private String description;

    private String status;
}
