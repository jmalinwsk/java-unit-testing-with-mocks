package database;

import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDbServiceTest {
    @Mock
    UserDbService userDbService;

    private User user;

    @BeforeEach
    public void init() {
        userDbService = mock(UserDbService.class);
        user = new User();
        user.setId(1);
        user.setEmail("sample@email.com");
    }

    @Test
    public void validAddTest() {
        assertDoesNotThrow(() -> userDbService.add(user));
    }

    @Test
    public void addThrowsWhenUserIsNull() {
        userDbService.add(null);
        doThrow(NullPointerException.class).when(userDbService).add(null);

        assertThrows(NullPointerException.class,
                () -> userDbService.add(null));
    }

    @Test
    public void validGetTest() {
        when(userDbService.get(1)).thenReturn(user);

        User result = userDbService.get(1);

        assertEquals(user, result);
    }

    @Test
    public void getThrowsWhenIdIsZero() {
        when(userDbService.get(0)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> userDbService.get(0));
    }

    @Test
    public void getThrowsWhenIdIsNegative() {
        when(userDbService.get(-1)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> userDbService.get(-1));
    }

    @Test
    public void getReturnsNullWhenUserIsNotFound() {
        when(userDbService.get(2)).thenReturn(null);

        assertNull(userDbService.get(2));
    }

    @Test
    public void validUpdateTest() {
        assertDoesNotThrow(() -> userDbService.update(user));
    }

    @Test
    public void updateThrowsWhenUserIsNull() {
        userDbService.update(null);
        doThrow(NullPointerException.class).when(userDbService).update(null);

        assertThrows(NullPointerException.class,
                () -> userDbService.update(null));
    }

    @Test
    public void validDeleteTest() {
        assertDoesNotThrow(() -> userDbService.delete(1));
    }

    @Test
    public void deleteThrowsWhenIdIsZero() {
        userDbService.delete(0);
        doThrow(IllegalArgumentException.class).when(userDbService).delete(0);

        assertThrows(IllegalArgumentException.class,
                () -> userDbService.delete(0));
    }

    @Test
    public void deleteThrowsWhenIdIsNegative() {
        userDbService.delete(-1);
        doThrow(IllegalArgumentException.class).when(userDbService).delete(-1);

        assertThrows(IllegalArgumentException.class,
                () -> userDbService.delete(-1));
    }

    @Test
    public void deleteThrowsWhenUserIsNotFound() {
        userDbService.delete(2);
        doThrow(NullPointerException.class).when(userDbService).delete(2);

        assertThrows(NullPointerException.class,
                () -> userDbService.delete(2));
    }

    @AfterEach
    public void cleanup() {
        userDbService = null;
    }
}
