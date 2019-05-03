package services;

import database.IDatabaseContext;
import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.User;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private IDatabaseContext databaseContext;
    @InjectMocks
    private UserService userService;

    private User user;
    private HashMap<Integer, User> userList;

    @BeforeEach
    public void init() {
        databaseContext = mock(IDatabaseContext.class);
        userService = new UserService(databaseContext);

        user = new User(1, "sample@email.com",
                "90b94d224ee82c837143ea6f0308c596f0142612678a036c65041b246d52df22");
        userList = new HashMap<Integer, User>() {{ put(1, user); }};
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
    public void validAddTest() {
        when(databaseContext.getNextUserId()).thenReturn(user.getId());

        assertDoesNotThrow(() -> userService.add(user));
    }

    @Test
    public void addThrowsWhenUserDoesntPassValidation() {
        user.setEmail("sample email@email.com");

        assertAll(
                () -> assertThrows(ValidationException.class,
                        () -> userService.add(user))
        );
    }

    @Test
    public void addThrowsWhenUserIsNull() {
        assertThrows(ValidationException.class,
                () -> userService.add(null));
    }

    @Test
    public void validGetTest() throws ElementNotFoundException {
        when(databaseContext.getUser(1)).thenReturn(user);

        assertEquals(user, userService.get(1));
    }

    @Test
    public void getThrowsWhenIdIsZero() {
        when(databaseContext.getUser(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userService.get(0));
    }

    @Test
    public void getThrowsWhenIdIsNegative() {
        when(databaseContext.getUser(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userService.get(-1));
    }

    @Test
    public void getReturnsNullWhenUserIsNotFound() {
        when(databaseContext.getUser(2)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userService.get(2));
    }

    @Test
    public void validGetAllTest() {
        HashMap<Integer, User> userList = new HashMap<Integer, User>() {{
            put(1, user);
        }};
        when(databaseContext.getUsers()).thenReturn(userList);

        assertEquals(userList, userService.get());
    }

    @Test
    public void getAllWhenListIsNull() {
        when(databaseContext.getUsers()).thenReturn(null);

        assertNull(userService.get());
    }

    @Test
    public void validUpdateTest() {
        when(databaseContext.getUser(user.getId())).thenReturn(user);

        assertDoesNotThrow(() -> userService.update(user));
    }

    @Test
    public void updateThrowsWhenUserDoesntPassValidation() {
        user.setEmail("sample email");

        assertThrows(ValidationException.class,
                () -> userService.update(user));
    }

    @Test
    public void updateThrowsWhenUserIsNull() {
        assertThrows(ValidationException.class,
                () -> userService.update(null));
    }

    @Test
    public void updateThrowsWhenUserDoesNotExist() {
        when(databaseContext.getUser(user.getId())).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userService.update(user));
    }

    @Test
    public void validDeleteTest() {
        when(databaseContext.getUser(user.getId())).thenReturn(user);

        assertDoesNotThrow(() -> userService.delete(user.getId()));
    }

    @Test
    public void deleteThrowsWhenIdIsZero() {
        when(databaseContext.getUser(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userService.delete(0));
    }

    @Test
    public void deleteThrowsWhenIdIsNegative() {
        when(databaseContext.getUser(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userService.delete(-1));
    }

    @Test
    public void deleteThrowsWhenUserIsNotFound() {
        when(databaseContext.getUser(2)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userService.delete(2));
    }

    @Test
    public void properLoginTest() {
        when(databaseContext.getUsers()).thenReturn(userList);

        assertTrue(userService.login(user.getEmail(), user.getPassword()));
    }

    @Test
    public void failedLoginTest() {
        when(databaseContext.getUsers()).thenReturn(new HashMap<>());

        assertFalse(userService.login(user.getEmail(), user.getPassword()));
    }

    @AfterEach
    public void cleanup() {
        databaseContext = null;
        userService = null;
        user = null;
    }
}