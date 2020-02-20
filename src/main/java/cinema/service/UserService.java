package cinema.service;

import cinema.model.User;

public interface UserService {
    User add(User user);

    User getById(Long id);

    User findByEmail(String email);
}