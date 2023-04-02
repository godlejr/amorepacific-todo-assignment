package amorepacific.todo.assignment.controller;


import amorepacific.todo.assignment.entity.AbstractEntity;
import amorepacific.todo.assignment.entity.User;
import amorepacific.todo.assignment.model.response.UsersResponse;
import amorepacific.todo.assignment.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "User 관련 기능 인터페이스")
public class UserController {

    private final UserService userService;

    @PostConstruct
    public void init() {
        User user = new User("김동주");
        User user2 = new User("안지슬");
        User user3 = new User("김은동");
        User user4 = new User("박지우");
        User user5 = new User("김혜수");

        User savedUser = userService.addUser(user);
        User savedUser2 = userService.addUser(user2);
        User savedUser3 = userService.addUser(user3);
        User savedUser4 = userService.addUser(user4);
        User savedUser5 = userService.addUser(user5);
    }

        @GetMapping
    @Operation(summary = "프로필 리스트 조회", description = "### 프로필 리스트 전체를 조회할 수 있다.")
    public UsersResponse getUsers()  {
        List<User> users = userService.getUsers().get();

        return new UsersResponse(HttpStatus.OK.value(), users);
    }
}
