package amorepacific.todo.assignment.model.response;

import amorepacific.todo.assignment.entity.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TodosResponse {
    @Schema(description = "상태코드", example = "200")
    private int statusCode;
    @Schema(description = "todo 리스트")
    private List<Todo> todos;
}
