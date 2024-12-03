package ru.job4j.bmb.repository;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.User;

import java.util.List;

@Component
@Repository
public interface UserRepository {
    List<User> findAll();

    User findByClientId(Long clientId);
}
