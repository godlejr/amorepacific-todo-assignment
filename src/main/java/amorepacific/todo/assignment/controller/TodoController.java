package amorepacific.todo.assignment.controller;

import amorepacific.todo.assignment.entity.Todo;
import amorepacific.todo.assignment.model.request.TodoRequest;
import amorepacific.todo.assignment.model.response.ErrorResponse;
import amorepacific.todo.assignment.model.response.TodoResponse;
import amorepacific.todo.assignment.model.response.TodosResponse;
import amorepacific.todo.assignment.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("todo")
@RequiredArgsConstructor
@Tag(name = "TodoController", description = "Todo 관련 기능 인터페이스")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    @Operation(summary = "todo 생성", description = "\n" +
            "\n" +
            "### todo 를 생성할 수 있다.\n" +
            "\n" +
            "\n 1. Task(필수)과 Description(선택)을 작성할 수 있다.\n" +
            "\n 2. 프로필 중에서 담당자(필수)를 입력해야 한다.\n" +
            "\n 3. status : 초기값은 \"진행 중\" 임.\n" +
            "\n 4. 우선순위\n" +
            "\n    4-1). 같은 담당자의 동일 날짜 처음 생성되면 초기값은 B0 를 설정 함. \n" +
            "\n    4-2). 이후에 생성되는 우선 순위의 초기값은 동일날짜 todo 들 중 최하순위로 설정.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "todo 정보", content = @Content(schema = @Schema(implementation = TodoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "internal error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))}
    )
    public TodoResponse addTodo(@RequestBody @Valid final TodoRequest todoRequest) {
        //Request Object Variable Validation
        if (todoRequest.getUser().getId() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "담당자 id는 필수 입력 값입니다");
        }

        //Request Object to Entity
        Todo todo = new Todo(todoRequest.getDate(), todoRequest.getUser(), todoRequest.getPriority(), todoRequest.getTask(), todoRequest.getDescription(), todoRequest.getStatus());

        //Response Data
        Todo addedTodo = todoService.addTodo(todo);

        //Response
        return new TodoResponse(HttpStatus.OK.value(), addedTodo);
    }


    @GetMapping
    @Operation(summary = "todo 리스트 조회", description = "### " +
            "특정 담당자, 특정 일자의 todo list 를 중요도/우선순위 로 정렬하여 조회할 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "todo 리스트 정보", content = @Content(schema = @Schema(implementation = TodosResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "internal error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))}
    )
    public TodosResponse getTodosByUserIdOrDateOrNothing(@Parameter(description = "특정 담당자 id") @RequestParam(required = false, defaultValue = "0") long userId,
                                                         @Parameter(description = "특정 일정 : yyyy-MM-dd 형식(예시: 2023-04-01)") @RequestParam(required = false) String date) {

        //Response Data
        List<Todo> todos = todoService.getTodosByUserIdOrDateOrNothing(userId, date).get();

        //Response
        return new TodosResponse(HttpStatus.OK.value(), todos);
    }


    @PutMapping("/{todoId}")
    @Operation(summary = "todo 변경", description = "\n" +
            "\n" +
            "### todo 를 변경할 수 있다.\n" +
            "\n" +
            "\n 1. 특정 todo 의 task, description, 우선순위, 상태를 변경함.\n" +
            "\n 2. 특정인에게 todo 를 위임할 수 있다. - 입력 : user(id, name)에 담당자 정보를 기입, status에 \"위임\"이라 기재\n" +
            "\n    2-1). 위임한 담당자에게 todo 는 위임 + 위임 받은 사람으로 표시 된다. 예)\n" +
            "위임(담당자 A)\n" +
            "\n    2-2). 위임 받은 담당에게는 todo 가 A0 로 생성되며, 위임 한 담당자가\n" +
            "표시된다. 위임받은 담당에게 A0 가 이미 존재할 경우 하위 우선순위로\n" +
            "저장된다. \n" +
            "\n    2-3). 위임 받은 todo 는 재 위임할 수 없다.\n" +
            "\n 3. 위임 받은 todo 를 취소할 수 있다. - 입력 : status에 \"취소\"라 기재 \n" +
            "\n    3-1). 취소하면 원상 복귀된다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변경된 todo 정보", content = @Content(schema = @Schema(implementation = TodoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "internal error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))}
    )
    public TodoResponse updateTodo(@Parameter(description = "Todo Id") @PathVariable long todoId, @RequestBody @Valid final TodoRequest todoRequest) {
        //Request Object Variable Validation
        if (todoId < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todo id는 필수 입력 값입니다");
        }

        //Request Object to Entity
        Todo todo = new Todo(todoRequest.getDate(), todoRequest.getUser(), todoRequest.getPriority(), todoRequest.getTask(), todoRequest.getDescription(), todoRequest.getStatus());
        todo.setId(todoId);

        //Response Data
        Todo updatedTodo = todoService.updateTodo(todo);

        //Response
        return new TodoResponse(HttpStatus.OK.value(), updatedTodo);

    }


    @DeleteMapping("/{todoId}")
    @Operation(summary = "todo 삭제", description = "\n" +
            "\n" +
            "### todo 를 삭제할 수 있다.\n" +
            "\n" +
            "\n 1. 위임 관계도 모두 삭제된다. (위임 관계 Todo 전부 삭제됨)\n" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제된 Origin Todo 정보", content = @Content(schema = @Schema(implementation = TodoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "internal error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))}
    )
    public TodoResponse deleteTodo(@Parameter(description = "Todo Id") @PathVariable long todoId) {
        //Request Object Variable Validation
        if (todoId < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todo id는 필수 입력 값입니다");
        }

        //Response Data
        Todo deletedTodo = todoService.deleteTodoByTodoId(todoId);

        //Response
        return new TodoResponse(HttpStatus.OK.value(), deletedTodo);

    }
}
