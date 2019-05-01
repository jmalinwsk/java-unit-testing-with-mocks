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
import static org.mockito.Mockito.doThrow;

class UserServiceTest {
    @Mock
    private IDatabaseContext databaseContext;
    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void init() {
        databaseContext = mock(IDatabaseContext.class);
        userService = new UserService(databaseContext);

        user = new User(1, "sample@email.com");
    }

    @Test
    void validAddTest() {
        when(databaseContext.getNextUserId()).thenReturn(user.getId());

        assertDoesNotThrow(() -> userService.add(user));
    }

    @Test
    void addThrowsWhenUserDoesntPassValidation() {
        user.setEmail("sample email@email.com");

        assertAll(
                () -> assertThrows(ValidationException.class,
                        () -> userService.add(user))
        );
    }

    @Test
    void addThrowsWhenUserIsNull() {
        assertThrows(ValidationException.class,
                () -> userService.add(null));
    }

    @Test
    void validGetTest() throws ElementNotFoundException {
        when(databaseContext.getUser(1)).thenReturn(user);

        assertEquals(user, userService.get(1));
    }

    @Test
    void getThrowsWhenIdIsZero() {
        when(databaseContext.getUser(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userService.get(0));
    }

    @Test
    void getThrowsWhenIdIsNegative() {
        when(databaseContext.getUser(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userService.get(-1));
    }

    @Test
    void getReturnsNullWhenUserIsNotFound() {
        when(databaseContext.getUser(2)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userService.get(2));
    }

    @Test
    void validUpdateTest() {
        when(databaseContext.getUser(user.getId())).thenReturn(user);

        assertDoesNotThrow(() -> userService.update(user));
    }

    @Test
    void updateThrowsWhenUserDoesntPassValidation() {
        user.setEmail("sample email");

        assertThrows(ValidationException.class,
                () -> userService.update(user));
    }

    @Test
    void updateThrowsWhenUserIsNull() {
        assertThrows(ValidationException.class,
                () -> userService.update(null));
    }

    @Test
    void validDeleteTest() {
        when(databaseContext.getUser(user.getId())).thenReturn(user);

        assertDoesNotThrow(() -> userService.delete(user.getId()));
    }

    @Test
    void deleteThrowsWhenIdIsZero() {
        when(databaseContext.getUser(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userService.delete(0));
    }

    @Test
    void deleteThrowsWhenIdIsNegative() {
        when(databaseContext.getUser(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userService.delete(-1));
    }

    @Test
    void deleteThrowsWhenUserIsNotFound() {
        when(databaseContext.getUser(2)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> userService.delete(2));
    }

    @AfterEach
    void cleanup() {
        databaseContext = null;
        userService = null;
        user = null;
    }
}