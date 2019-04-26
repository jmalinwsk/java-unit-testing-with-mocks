package services;

import models.User;

public interface IUserService {
    void add(User user);
    User get(int id);
    void update(User user);
    void delete(int id);
}
