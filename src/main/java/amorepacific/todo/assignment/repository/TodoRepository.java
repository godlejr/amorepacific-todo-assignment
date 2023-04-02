package amorepacific.todo.assignment.repository;

import amorepacific.todo.assignment.entity.Todo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoRepository {

    Todo upsert(Todo todo);

    Optional<List<Todo>> findAll();

    Optional<List<Todo>> findByUserId(long userId);

    Optional<List<Todo>> findByDate(LocalDate date);

    Optional<List<Todo>> findByUserIdAndDate(long userId, LocalDate date);

    Optional<Todo> findById(long id);

    void deleteById(long id);

    void deleteAll();
}
