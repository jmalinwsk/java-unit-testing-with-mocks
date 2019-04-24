package database;

import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDbServiceTest {
    @Mock
    UserDbService userDbService;

    private User user;

    @BeforeEach
    void init() {
        userDbService = mock(UserDbService.class);
        user = new User();
        user.setId(1);
        user.setEmail("sample@email.com");
    }

    @Test
    void validAddTest() {
        assertDoesNotThrow(() -> userDbService.add(user));
    }

    @Test
    void addThrowsWhenUserIsNull() {
        userDbService.add(null);
        doThrow(NullPointerException.class).when(userDbService).add(null);

        assertThrows(NullPointerException.class,
                () -> userDbService.add(null));
    }

    @Test
    void validGetTest() {
        when(userDbService.get(1)).thenReturn(user);

        User result = userDbService.get(1);

        assertEquals(user, result);
    }

    @Test
    void getThrowsWhenIdIsZero() {
        when(userDbService.get(0)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> userDbService.get(0));
    }

    @Test
    void getThrowsWhenIdIsNegative() {
        when(userDbService.get(-1)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> userDbService.get(-1));
    }

    @Test
    void getReturnsNullWhenUserIsNotFound() {
        when(userDbService.get(2)).thenReturn(null);

        assertNull(userDbService.get(2));
    }

    @Test
    void validUpdateTest() {
        assertDoesNotThrow(() -> userDbService.update(user));
    }

    @Test
    void updateThrowsWhenUserIsNull() {
        userDbService.update(null);
        doThrow(NullPointerException.class).when(userDbService).update(null);

        assertThrows(NullPointerException.class,
                () -> userDbService.update(null));
    }

    @Test
    void validDeleteTest() {
        assertDoesNotThrow(() -> userDbService.delete(1));
    }

    @Test
    void deleteThrowsWhenIdIsZero() {
        userDbService.delete(0);
        doThrow(IllegalArgumentException.class).when(userDbService).delete(0);

        assertThrows(IllegalArgumentException.class,
                () -> userDbService.delete(0));
    }

    @Test
    void deleteThrowsWhenIdIsNegative() {
        userDbService.delete(-1);
        doThrow(IllegalArgumentException.class).when(userDbService).delete(-1);

        assertThrows(IllegalArgumentException.class,
                () -> userDbService.delete(-1));
    }

    @Test
    void deleteThrowsWhenUserIsNotFound() {
        userDbService.delete(2);
        doThrow(NullPointerException.class).when(userDbService).delete(2);

        assertThrows(NullPointerException.class,
                () -> userDbService.delete(2));
    }

    @AfterEach
    void cleanup() {
        userDbService = null;
    }
}
