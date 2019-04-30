package services;

import database.IDatabaseContext;
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

    public UserService(IDatabaseContext databaseContext) {
        this.databaseContext = databaseContext;
    }

    private boolean userValidation(User user) {
        if(user != null &&
                user.getEmail() != null &&
                user.getEmail().matches(emailPattern))
            return true;
        else return false;
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
    public User get(int id) {
        User user = databaseContext.getUser(id);
        if(user != null) {
            return user;
        } else throw new NullPointerException();
    }

    @Override
    public HashMap<Integer, User> get() {
        return databaseContext.getUsers();
    }

    @Override
    public void update(User user) throws ValidationException {
        if(userValidation(user)) {
            databaseContext.update(user);
        }
        else throw new ValidationException(
                "Given hotel didn't pass validation!");
    }

    @Override
    public void delete(int id) {
        User user = databaseContext.getUser(id);
        if(user != null) {
            databaseContext.delete(user);
        } else throw new NullPointerException();
    }
}