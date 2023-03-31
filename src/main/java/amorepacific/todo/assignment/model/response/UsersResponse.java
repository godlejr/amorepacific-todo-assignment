package amorepacific.todo.assignment.model.response;


import amorepacific.todo.assignment.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UsersResponse {

    @Schema(description = "상태코드", example = "200")
    private int statusCode;
    @Schema(description = "유저 리스트")
    private List<User> users;
}
