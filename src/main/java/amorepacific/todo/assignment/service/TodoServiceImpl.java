package amorepacific.todo.assignment.service;

import amorepacific.todo.assignment.entity.Todo;
import amorepacific.todo.assignment.model.Priority;
import amorepacific.todo.assignment.repository.TodoRepository;
import amorepacific.todo.assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;

    @Override
    public Todo save(Todo todo) {
        todo.setDate(LocalDate.now());
        todo.setPriority(new Priority('B', 0));
//        userRepository.fi
//
//        todoRepository.save();




        return null;
    }
}
