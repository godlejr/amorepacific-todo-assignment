package amorepacific.todo.assignment.service;

import amorepacific.todo.assignment.entity.User;
import amorepacific.todo.assignment.repository.UserRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private UserServiceImpl userService;
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void beforeEach(){
        userRepository = new UserRepositoryImpl();
        userService = new UserServiceImpl(userRepository);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void save() {
        // given
        User user = new User("김동주");
        User user2 = new User("안지슬");
        User user3 = new User("김은동");
        User user4 = new User("박지우");
        User user5 = new User("김혜수");


        // when
        User addedUser = userService.addUser(user);
        User addedUser2 = userService.addUser(user2);
        User addedUser3 = userService.addUser(user3);
        User addedUser4 = userService.addUser(user4);
        User addedUser5 = userService.addUser(user5);

        List<User> addedUsers = new ArrayList<>();
        addedUsers.add(addedUser);
        addedUsers.add(addedUser2);
        addedUsers.add(addedUser3);
        addedUsers.add(addedUser4);
        addedUsers.add(addedUser5);

        // then
        List<User> users = userRepository.findAll().get();
        Assertions.assertThat(addedUsers).isEqualTo(users);

    }

}