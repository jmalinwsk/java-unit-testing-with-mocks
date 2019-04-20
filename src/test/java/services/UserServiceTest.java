package services;

import database.Database;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {
    private Database database;
    private UserService userService;
    private User user;

    @BeforeEach
    public void init() {
        database = new Database();
        userService = new UserService();
        user = new User("test@test.com");
    }

    @Test
    @DisplayName("validation of user (valid)")
    public void userValidationTest() {
        boolean result = userService.userValidation(user);
        assertTrue(result);
    }

    @Test
    @DisplayName("validation of user " +
            "(returns false because of null argument)")
    public void userValidation2Test() {
        assertFalse(userService.userValidation(null));
    }

    @Test
    @DisplayName("validation of user " +
            "(returns false because email is an empty string")
    public void userValidation3Test() {
        user.setEmail("");
        assertFalse(userService.userValidation(user));
    }

    @Test
    @DisplayName("validation of user" +
            "(returns false because email is null")
    public void userValidation4Test() {
        user.setEmail(null);
        assertFalse(userService.userValidation(user));
    }

    @Test
    @DisplayName("validation of user" +
            "(returns false because email is invalid")
    public void userValidation5Test() {
        user.setEmail("invalid email");
        assertFalse(userService.userValidation(user));
    }

    @Test
    @DisplayName("adding user to database (valid)")
    public void addUserToDatabaseTest() {
        assertEquals(new HashMap<Integer, User>(), database.getUsers());

        userService.addUserToDatabase(database, user);

        HashMap<Integer, User> usersTemp = new HashMap<>();
        usersTemp.put(1, user);
        assertEquals(usersTemp, database.getUsers());
    }

    @Test
    @DisplayName("adding user to database" +
            "(throws NullPointerException when database is null")
    public void addUserToDatabase2Test() {
        assertThrows(NullPointerException.class,
                () -> userService.addUserToDatabase(null, user));
    }

    @Test
    @DisplayName("adding user to database" +
            "(throws IllegalArgumentException when user is null")
    public void addUserToDatabase4Test() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.addUserToDatabase(database, null));
    }

    @Test
    @DisplayName("adding user to database" +
            "(throws IllegalArgumentException when database and user are null")
    public void addUserToDatabase5Test() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.addUserToDatabase(null, null));
    }

    @Test
    @DisplayName("adding user to database " +
            "(throws IllegalArgumentException when user doesn't pass validation)")
    public void addUserToDatabase6Test() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.addUserToDatabase(database,
                        new User(null)));
    }



    @AfterEach
    public void cleanup() {
        database = null;
        userService = null;
        user = null;
    }
}
