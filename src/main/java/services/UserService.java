package services;

import database.Database;
import models.User;

public class UserService {
    /** @implNote http://emailregex.com/ */
    private String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-" +
            "9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\" +
            "x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f]" +
            ")*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-" +
            "9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)" +
            "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:" +
            "[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x" +
            "01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";

    /** Validation of user.
     * @return true if user is valid or false if user is invalid
     */
    public boolean userValidation(User user) {
        if(user != null &&
            user.getEmail() != null &&
            user.getEmail().matches(emailPattern))
            return true;
        else return false;
    }

    /** Validates user value and if valid, adds user to the database.
     * @throws IllegalArgumentException when validation of user is wrong
     */
    public void addUserToDatabase(Database database, User newUser) {
        if (userValidation(newUser)) {
            if (database != null) {
                Integer id = database.getNextUserId();
                newUser.setId(id);

                database.getUsers().put(id, newUser);
            } else throw new NullPointerException();
        } else throw new IllegalArgumentException();
    }
}
