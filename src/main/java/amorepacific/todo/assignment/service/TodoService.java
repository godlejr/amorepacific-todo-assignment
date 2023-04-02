package amorepacific.todo.assignment.service;

import amorepacific.todo.assignment.entity.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    Todo addTodo(Todo todo);

    Optional<List<Todo>> getTodosByUserIdOrDateOrNothing(long userId, String date);

    Todo updateTodo(Todo todo);

    Todo deleteTodoByTodoId(long todoId);
}
