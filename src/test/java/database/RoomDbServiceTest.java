package database;

import models.Hotel;
import models.Room;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomDbServiceTest {
    @Mock
    RoomDbService roomDbService;

    private Hotel hotel;
    private Room room;

    @BeforeEach
    void init() {
        roomDbService = mock(RoomDbService.class);
        hotel = new Hotel();
        hotel.setId(1);
        hotel.setName("Sample name");
        hotel.setOpenHour(new LocalTime(8));
        hotel.setCloseHour(new LocalTime(22));
        room = new Room();
        room.setId(1);
        room.setHotel(hotel);
        room.setAmountOfPeople(2);
        room.setNumberOfRoom(435);
    }

    @Test
    void validAddTest() {
        assertDoesNotThrow(() -> roomDbService.add(room));
    }

    @Test
    void addThrowsWhenHotelIsNull() {
        roomDbService.add(null);
        doThrow(NullPointerException.class).when(roomDbService).add(null);

        assertThrows(NullPointerException.class,
                () -> roomDbService.add(null));
    }

    @Test
    void validGetTest() {
        when(roomDbService.get(1)).thenReturn(room);

        Room result = roomDbService.get(1);

        assertEquals(room, result);
    }

    @Test
    void getThrowsWhenIdIsZero() {
        when(roomDbService.get(0)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> roomDbService.get(0));
    }

    @Test
    void getThrowsWhenIdIsNegative() {
        when(roomDbService.get(-1)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> roomDbService.get(-1));
    }

    @Test
    void getReturnsNullWhenHotelIsNotFound() {
        when(roomDbService.get(2)).thenReturn(null);

        assertNull(roomDbService.get(2));
    }

    @Test
    void validUpdateTest() {
        assertDoesNotThrow(() -> roomDbService.update(room));
    }

    @Test
    void updateThrowsWhenHotelIsNull() {
        roomDbService.update(null);
        doThrow(NullPointerException.class).when(roomDbService).update(null);

        assertThrows(NullPointerException.class,
                () -> roomDbService.update(null));
    }

    @Test
    void validDeleteTest() {
        assertDoesNotThrow(() -> roomDbService.delete(1));
    }

    @Test
    void deleteThrowsWhenIdIsZero() {
        roomDbService.delete(0);
        doThrow(IllegalArgumentException.class).when(roomDbService).delete(0);

        assertThrows(IllegalArgumentException.class,
                () -> roomDbService.delete(0));
    }

    @Test
    void deleteThrowsWhenIdIsNegative() {
        roomDbService.delete(-1);
        doThrow(IllegalArgumentException.class).when(roomDbService).delete(-1);

        assertThrows(IllegalArgumentException.class,
                () -> roomDbService.delete(-1));
    }

    @Test
    void deleteThrowsWhenHotelIsNotFound() {
        roomDbService.delete(2);
        doThrow(NullPointerException.class).when(roomDbService).delete(2);

        assertThrows(NullPointerException.class,
                () -> roomDbService.delete(2));
    }

    @AfterEach
    void cleanup() {
        roomDbService = null;
        room = null;
        hotel = null;
    }
}
