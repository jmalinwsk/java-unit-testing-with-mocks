package services;

import models.User;

public interface UserService {
    void add(User user);
    User get(int id);
    void update(User user);
    void delete(int id);
}
