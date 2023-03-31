package amorepacific.todo.assignment.repository;

import amorepacific.todo.assignment.entity.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final HashMap<Long, User> userMap = new HashMap<>();
    private static long id = 0L;

    @Override
    public User upsert(User user) {
        if (user.getId() == 0) {
            user.setId(++id);
        }
        userMap.put(user.getId(), user);

        return user;
    }

    @Override
    public Optional<List<User>> findAll() {
        List<User> users = new ArrayList<>();
        userMap.forEach((key, value) -> {
            users.add(value);
        });
        return Optional.ofNullable(users);
    }

    @Override
    public void deleteAll() {
        userMap.clear();
    }

}


