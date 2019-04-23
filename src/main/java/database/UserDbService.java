package database;

import models.User;

public interface UserDbService {
    void add(User user);
    User get(int id);
    void update(User user);
    void delete(int id);
}
