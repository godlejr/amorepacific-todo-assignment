package amorepacific.todo.assignment.repository;

import amorepacific.todo.assignment.entity.Todo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoRepository {

    Todo upsert(Todo todo);

    Optional<List<Todo>> findByDate(LocalDate date);

    Optional<Todo> findById(long id);

    void delete(long id);

    void deleteAll();
}
