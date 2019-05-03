package services;

import database.IDatabaseContext;
import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.User;

import java.util.HashMap;

public class UserService implements IUserService {
    private IDatabaseContext databaseContext;
    /** @implNote http://emailregex.com/ */
    private String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-" +
            "9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\" +
            "x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f]" +
            ")*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-" +
            "9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)" +
            "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:" +
            "[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x" +
            "01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";

    UserService(IDatabaseContext databaseContext) {
        this.databaseContext = databaseContext;
    }

    public boolean userValidation(User user) {
        return user != null &&
                user.getEmail() != null &&
                user.getEmail().matches(emailPattern);
    }

    @Override
    public void add(User user) throws ValidationException {
        if(userValidation(user)) {
            Integer id = databaseContext.getNextUserId();
            user.setId(id);

            databaseContext.add(user);
        }
        else throw new ValidationException(
                "Given hotel didn't pass validation!");
    }

    @Override
    public User get(int id) throws ElementNotFoundException {
        User user = databaseContext.getUser(id);
        if(user != null) {
            return user;
        } else throw new ElementNotFoundException(
                "User with id " + id  + " is not found.");
    }

    @Override
    public HashMap<Integer, User> get() {
        return databaseContext.getUsers();
    }

    @Override
    public void update(User user) throws ValidationException, ElementNotFoundException {
        if(userValidation(user)) {
            User checkIfUserExists = databaseContext.getUser(user.getId());
            if(checkIfUserExists != null) {
                databaseContext.update(user);
            } else throw new ElementNotFoundException(
                    "User with id " + user.getId() + " is not found.");
        }
        else throw new ValidationException(
                "Given hotel didn't pass validation!");
    }

    @Override
    public void delete(int id) throws ElementNotFoundException {
        User user = databaseContext.getUser(id);
        if(user != null) {
            databaseContext.delete(user);
        } else throw new ElementNotFoundException(
                "User with id " + id  + " is not found.");
    }

    @Override
    public boolean login(String email, String passwordHash) {
        for(User user : databaseContext.getUsers().values()) {
            if(user.getEmail().equals(email) && user.getPassword().equals(passwordHash))
                    return true;
        }
        return false;
    }
}