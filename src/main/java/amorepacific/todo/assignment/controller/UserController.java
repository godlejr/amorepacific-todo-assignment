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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "User 관련 기능 인터페이스 controller")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "프로필 리스트 전체", description = "프로필 리스트 전체를 조회할 수 있다")
    public UsersResponse getUsers()  {
        User user = new User("김동주");
//
//        User user2 = new User();
//        user2.setName("안지슬");
//
//        User user3 = new User();
//        user3.setName("김은동");
//
//        User user4 = new User();
//        user4.setName("박지우");
//
//        User user5 = new User();
//        user5.setName("김혜수");
//
//
//        // when
//        User savedUser = userService.add(user);
//        User savedUser2 = userService.add(user2);
//        User savedUser3 = userService.add(user3);
//        User savedUser4 = userService.add(user4);
//        User savedUser5 = userService.add(user5);

        List<User> users = userService.getUsers().get();

        return new UsersResponse(HttpStatus.OK.value(), users);
    }
}
