package amorepacific.todo.assignment.service;

import amorepacific.todo.assignment.entity.User;
import amorepacific.todo.assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<List<User>> getUsers() {

        List<User> users = userRepository.findAll().get();
        return Optional.ofNullable(users);
    }

    @Override
    public User addUser(User user) {
        User addedUser = userRepository.upsert(user);
        return addedUser;
    }
}
