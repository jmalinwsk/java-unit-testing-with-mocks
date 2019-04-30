package services;

import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.User;

import java.util.HashMap;

public interface IUserService {
    void add(User user) throws ValidationException;
    User get(int id) throws ElementNotFoundException;
    HashMap<Integer, User> get();
    void update(User user) throws ValidationException;
    void delete(int id) throws ElementNotFoundException;
}
