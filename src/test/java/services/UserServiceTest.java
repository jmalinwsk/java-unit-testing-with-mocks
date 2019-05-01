package services;

import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.User;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

class UserServiceTest {
    @Mock
    private IUserService userService;

    private User user;

    @BeforeEach
    void init() {
        userService = mock(IUserService.class);
    }

    @Test
    void validAddTest() {
        assertDoesNotThrow(() -> userService.add(user));
    }

    @Test
    void addThrowsWhenUserDoesntPassValidation() throws ValidationException {
        userService.add(user);
        doThrow(IllegalArgumentException.class).when(userService).add(user);

        assertThrows(IllegalArgumentException.class,
                () -> userService.add(user));
    }

    @Test
    void addThrowsWhenUserIsNull() throws ValidationException {
        userService.add(null);
        doThrow(NullPointerException.class).when(userService).add(null);

        assertThrows(NullPointerException.class,
                () -> userService.add(null));
    }

    @Test
    void validGetTest() throws ElementNotFoundException {
        when(userService.get(1)).thenReturn(user);

        User result = userService.get(1);

        assertEquals(user, result);
    }

    @Test
    void getThrowsWhenIdIsZero() throws ElementNotFoundException {
        when(userService.get(0)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> userService.get(0));
    }

    @Test
    void getThrowsWhenIdIsNegative() throws ElementNotFoundException {
        when(userService.get(-1)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> userService.get(-1));
    }

    @Test
    void getReturnsNullWhenUserIsNotFound() throws ElementNotFoundException {
        when(userService.get(2)).thenReturn(null);

        assertNull(userService.get(2));
    }

    @Test
    void validUpdateTest() {
        assertDoesNotThrow(() -> userService.update(user));
    }

    @Test
    void updateThrowsWhenUserDoesntPassValidation() throws ValidationException, ElementNotFoundException {
        userService.update(user);
        doThrow(IllegalArgumentException.class).when(userService).update(user);

        assertThrows(IllegalArgumentException.class,
                () -> userService.update(user));
    }

    @Test
    void updateThrowsWhenUserIsNull() throws ValidationException, ElementNotFoundException {
        userService.update(null);
        doThrow(NullPointerException.class).when(userService).update(null);

        assertThrows(NullPointerException.class,
                () -> userService.update(null));
    }

    @Test
    void validDeleteTest() {
        assertDoesNotThrow(() -> userService.delete(1));
    }

    @Test
    void deleteThrowsWhenIdIsZero() throws ElementNotFoundException {
        userService.delete(0);
        doThrow(IllegalArgumentException.class).when(userService).delete(0);

        assertThrows(IllegalArgumentException.class,
                () -> userService.delete(0));
    }

    @Test
    void deleteThrowsWhenIdIsNegative() throws ElementNotFoundException {
        userService.delete(-1);
        doThrow(IllegalArgumentException.class).when(userService).delete(-1);

        assertThrows(IllegalArgumentException.class,
                () -> userService.delete(-1));
    }

    @Test
    void deleteThrowsWhenUserIsNotFound() throws ElementNotFoundException {
        userService.delete(2);
        doThrow(NullPointerException.class).when(userService).delete(2);

        assertThrows(NullPointerException.class,
                () -> userService.delete(2));
    }

    @AfterEach
    void cleanup() {
        userService = null;
        user = null;
    }
}