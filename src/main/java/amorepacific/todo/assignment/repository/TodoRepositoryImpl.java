package amorepacific.todo.assignment.repository;

import amorepacific.todo.assignment.entity.Todo;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class TodoRepositoryImpl implements TodoRepository {
    private HashMap<Long, Todo> todoMap = new HashMap<>();
    private static long id = 0L;

    @Override
    public Todo upsert(Todo todo) {
        if (todo.getId() == 0) {
            todo.setId(++id);
            todo.setStatus("진행중");
        }
        todoMap.put(todo.getId(), todo);
        return todo;
    }

    @Override
    public Optional<List<Todo>> findByDate(LocalDate date) {
        List<Todo> list = new ArrayList<>();

        todoMap.forEach((key, value) -> {
            if (value.getDate().equals(date)) list.add(value);
        });
        return Optional.ofNullable(list);
    }

    @Override
    public Optional<Todo> findById(long id) {
        return Optional.ofNullable(todoMap.get(id));
    }

    @Override
    public void delete(long id) {
        todoMap.remove(id);
    }

    @Override
    public void deleteAll() {
        todoMap.clear();
    }


}
