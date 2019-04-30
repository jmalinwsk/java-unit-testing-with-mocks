package services;

import exceptions.ValidationException;
import models.User;

import java.util.HashMap;

public interface IUserService {
    void add(User user) throws ValidationException;
    User get(int id);
    HashMap<Integer, User> get();
    void update(User user) throws ValidationException;
    void delete(int id);
}
