package services;

import models.Room;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

class RoomServiceTest {
    @Mock
    private RoomService roomService;

    private Room room;

    @BeforeEach
    void init() {
        roomService = mock(RoomService.class);
    }

    @Test
    void validAddTest() {
        assertDoesNotThrow(() -> roomService.add(room));
    }

    @Test
    void addThrowsWhenRoomDoesntPassValidation() {
        roomService.add(room);
        doThrow(IllegalArgumentException.class).when(roomService).add(room);

        assertThrows(IllegalArgumentException.class,
                () -> roomService.add(room));
    }

    @Test
    void addThrowsWhenRoomIsNull() {
        roomService.add(null);
        doThrow(NullPointerException.class).when(roomService).add(null);

        assertThrows(NullPointerException.class,
                () -> roomService.add(null));
    }

    @Test
    void validGetTest() {
        when(roomService.get(1)).thenReturn(room);

        Room result = roomService.get(1);

        assertEquals(room, result);
    }

    @Test
    void getThrowsWhenIdIsZero() {
        when(roomService.get(0)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> roomService.get(0));
    }

    @Test
    void getThrowsWhenIdIsNegative() {
        when(roomService.get(-1)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> roomService.get(-1));
    }

    @Test
    void getReturnsNullWhenRoomIsNotFound() {
        when(roomService.get(2)).thenReturn(null);

        assertNull(roomService.get(2));
    }

    @Test
    void validUpdateTest() {
        assertDoesNotThrow(() -> roomService.update(room));
    }

    @Test
    void updateThrowsWhenRoomDoesntPassValidation() {
        roomService.update(room);
        doThrow(IllegalArgumentException.class).when(roomService).update(room);

        assertThrows(IllegalArgumentException.class,
                () -> roomService.update(room));
    }

    @Test
    void updateThrowsWhenRoomIsNull() {
        roomService.update(null);
        doThrow(NullPointerException.class).when(roomService).update(null);

        assertThrows(NullPointerException.class,
                () -> roomService.update(null));
    }

    @Test
    void validDeleteTest() {
        assertDoesNotThrow(() -> roomService.delete(1));
    }

    @Test
    void deleteThrowsWhenIdIsZero() {
        roomService.delete(0);
        doThrow(IllegalArgumentException.class).when(roomService).delete(0);

        assertThrows(IllegalArgumentException.class,
                () -> roomService.delete(0));
    }

    @Test
    void deleteThrowsWhenIdIsNegative() {
        roomService.delete(-1);
        doThrow(IllegalArgumentException.class).when(roomService).delete(-1);

        assertThrows(IllegalArgumentException.class,
                () -> roomService.delete(-1));
    }

    @Test
    void deleteThrowsWhenRoomIsNotFound() {
        roomService.delete(2);
        doThrow(NullPointerException.class).when(roomService).delete(2);

        assertThrows(NullPointerException.class,
                () -> roomService.delete(2));
    }



    @AfterEach
    void cleanup() {
        roomService = null;
        room = null;
    }
}