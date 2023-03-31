package amorepacific.todo.assignment.service;

import amorepacific.todo.assignment.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<List<User>> getUsers();
    User addUser(User user);
}
