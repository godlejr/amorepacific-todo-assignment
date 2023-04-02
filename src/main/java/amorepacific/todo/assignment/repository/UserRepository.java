package amorepacific.todo.assignment.repository;

import amorepacific.todo.assignment.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<List<User>> findAll();

    User upsert(User user);

    Optional<User> findById(long id);

    void deleteAll();
}
