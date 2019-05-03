package services;

import database.IDatabaseContext;
import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.User;
import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private IDatabaseContext databaseContextMockito;
    @InjectMocks
    private UserService userServiceMockito;

    private EasyMockSupport easyMockSupport;
    private IDatabaseContext databaseContextEasyMock;
    private UserService userServiceEasyMock;

    private User user;
    private HashMap<Integer, User> userList;

    @BeforeEach
    public void init() {
        easyMockSupport = new EasyMockSupport();
        databaseContextMockito = mock(IDatabaseContext.class);
        userServiceMockito = new UserService(databaseContextMockito);
        databaseContextEasyMock = easyMockSupport.mock(IDatabaseContext.class);
        userServiceEasyMock = new UserService(databaseContextEasyMock);

        user = new User(1, "sample@email.com",
                "90b94d224ee82c837143ea6f0308c596f0142612678a036c65041b246d52df22");
        userList = new HashMap<Integer, User>() {{ put(1, user); }};
    }

    @Test
    @DisplayName("validation of user (valid)")
    public void userValidationTest() {
        boolean result = userServiceMockito.userValidation(user);
        assertTrue(result);
    }

    @Test
    @DisplayName("validation of user " +
            "(returns false because of null argument)")
    public void userValidation2Test() {
        assertFalse(userServiceMockito.userValidation(null));
    }

    @Test
    @DisplayName("validation of user " +
            "(returns false because email is an empty string")
    public void userValidation3Test() {
        user.setEmail("");
        assertFalse(userServiceMockito.userValidation(user));
    }

    @Test
    @DisplayName("validation of user" +
            "(returns false because email is null")
    public void userValidation4Test() {
        user.setEmail(null);
        assertFalse(userServiceMockito.userValidation(user));
    }

    @Test
    @DisplayName("validation of user" +
            "(returns false because email is invalid")
    public void userValidation5Test() {
        user.setEmail("invalid email");
        assertFalse(userServiceMockito.userValidation(user));
    }

    @Test
    public void validAddTest() {
        when(databaseContextMockito.getNextUserId()).thenReturn(user.getId());

        assertDoesNotThrow(() -> userServiceMockito.add(user));
    }

    @Test
    public void addThrowsWhenUserDoesntPassValidation() {
        user.setEmail("sample email@email.com");

        assertAll(
                () -> assertThrows(ValidationException.class,
                        () -> userServiceMockito.add(user))
        );
    }

    @Test
    public void addThrowsWhenUserIsNull() {
        assertThrows(ValidationException.class,
                () -> userServiceMockito.add(null));
    }

    @Test
    public void validGetTest() throws ElementNotFoundException {
        when(databaseContextMockito.getUser(1)).thenReturn(user);

        assertEquals(user, userServiceMockito.get(1));
    }

    @Test
    public void getThrowsWhenIdIsZero() {
        when(databaseContextMockito.getUser(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userServiceMockito.get(0));
    }

    @Test
    public void getThrowsWhenIdIsNegative() {
        when(databaseContextMockito.getUser(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userServiceMockito.get(-1));
    }

    @Test
    public void getReturnsNullWhenUserIsNotFound() {
        when(databaseContextMockito.getUser(2)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userServiceMockito.get(2));
    }

    @Test
    public void validGetAllTest() {
        HashMap<Integer, User> userList = new HashMap<Integer, User>() {{
            put(1, user);
        }};
        when(databaseContextMockito.getUsers()).thenReturn(userList);

        assertEquals(userList, userServiceMockito.get());
    }

    @Test
    public void getAllWhenListIsNull() {
        when(databaseContextMockito.getUsers()).thenReturn(null);

        assertNull(userServiceMockito.get());
    }

    @Test
    public void validUpdateTest() {
        when(databaseContextMockito.getUser(user.getId())).thenReturn(user);

        assertDoesNotThrow(() -> userServiceMockito.update(user));
    }

    @Test
    public void updateThrowsWhenUserDoesntPassValidation() {
        user.setEmail("sample email");

        assertThrows(ValidationException.class,
                () -> userServiceMockito.update(user));
    }

    @Test
    public void updateThrowsWhenUserIsNull() {
        assertThrows(ValidationException.class,
                () -> userServiceMockito.update(null));
    }

    @Test
    public void updateThrowsWhenUserDoesNotExist() {
        when(databaseContextMockito.getUser(user.getId())).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userServiceMockito.update(user));
    }

    @Test
    public void validDeleteTest() {
        when(databaseContextMockito.getUser(user.getId())).thenReturn(user);

        assertDoesNotThrow(() -> userServiceMockito.delete(user.getId()));
    }

    @Test
    public void deleteThrowsWhenIdIsZero() {
        when(databaseContextMockito.getUser(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userServiceMockito.delete(0));
    }

    @Test
    public void deleteThrowsWhenIdIsNegative() {
        when(databaseContextMockito.getUser(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userServiceMockito.delete(-1));
    }

    @Test
    public void deleteThrowsWhenUserIsNotFound() {
        when(databaseContextMockito.getUser(2)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userServiceMockito.delete(2));
    }

    @Test
    public void properLoginTest() {
        expect(databaseContextEasyMock.getUsers()).andReturn(userList);
        replay(databaseContextEasyMock);

        assertTrue(userServiceEasyMock.login(user.getEmail(), user.getPassword()));
    }

    @Test
    public void failedLoginTest() {
        expect(databaseContextEasyMock.getUsers()).andReturn(new HashMap<>());
        replay(databaseContextEasyMock);

        assertFalse(userServiceEasyMock.login(user.getEmail(), user.getPassword()));
    }

    @AfterEach
    public void cleanup() {
        databaseContextMockito = null;
        userServiceMockito = null;
        user = null;
    }
}