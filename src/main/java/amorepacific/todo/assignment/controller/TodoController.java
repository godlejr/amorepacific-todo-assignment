package amorepacific.todo.assignment.controller;

import amorepacific.todo.assignment.entity.AbstractEntity;
import amorepacific.todo.assignment.entity.Todo;
import amorepacific.todo.assignment.entity.User;
import amorepacific.todo.assignment.model.Priority;
import amorepacific.todo.assignment.model.request.TodoRequest;
import amorepacific.todo.assignment.model.response.TodoResponse;
import amorepacific.todo.assignment.model.response.TodosResponse;
import amorepacific.todo.assignment.service.TodoService;
import amorepacific.todo.assignment.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("todo")
@RequiredArgsConstructor
@Tag(name = "TodoController", description = "Todo 관련 기능 인터페이스 controller")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    @Operation(summary = "todo 생성", description = "todo 를 생성할 수 있다")
    @ApiResponse(
            code = 200
            , message = "todo 생성 정보"
            , response = TodoResponse.class
    )
    public TodoResponse save(@RequestBody @Valid final TodoRequest todoRequest) {

        Todo todo = new Todo(todoRequest.getDate(), todoRequest.getUser(), todoRequest.getPriority(), todoRequest.getTask(), todoRequest.getDescription(), todoRequest.getStatus());
//        Todo todo = todoRequest.getTodo();
        Todo savedTodo = todoService.save(todo);
        return new TodoResponse(HttpStatus.OK.value(), savedTodo);
    }


}
