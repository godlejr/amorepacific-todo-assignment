package amorepacific.todo.assignment.entity;

import amorepacific.todo.assignment.model.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import lombok.Data;


@Data
public class Todo {
    private long id;

    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private LocalDate date;
    private User user;
    private String task;
    private String description;
    private String status;
}
