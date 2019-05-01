package services;

import database.IDatabaseContext;
import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.Hotel;
import models.Room;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

class RoomServiceTest {
    @Mock
    private IDatabaseContext databaseContext;
    @InjectMocks
    private RoomService roomService;

    private Hotel hotel;
    private Room room;

    @BeforeEach
    void init() {
        databaseContext = mock(IDatabaseContext.class);
        roomService = new RoomService(databaseContext);
        hotel = new Hotel(1, "Sample name", new LocalTime(7), new LocalTime(22));
        room = new Room(1, hotel, 400, 3);
    }

    @Test
    void validAddTest() {
        when(databaseContext.getHotels()).thenReturn(
                new HashMap<Integer, Hotel>() {{ put(1, hotel); }}
        );
        when(databaseContext.getNextRoomId()).thenReturn(room.getId());

        assertDoesNotThrow(() -> roomService.add(room));
    }

    @Test
    void addThrowsWhenRoomDoesntPassValidation() {
        room.setAmountOfPeople(-5);

        assertThrows(ValidationException.class,
                        () -> roomService.add(room));
    }

    @Test
    void addThrowsWhenRoomIsNull() {
        assertThrows(ValidationException.class,
                () -> roomService.add(null));
    }

    @Test
    void validGetTest() throws ElementNotFoundException {
        when(databaseContext.getRoom(room.getId())).thenReturn(room);

        Room result = roomService.get(room.getId());

        assertEquals(room, result);
    }

    @Test
    void getThrowsWhenIdIsZero() {
        when(databaseContext.getRoom(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomService.get(0));
    }

    @Test
    void getThrowsWhenIdIsNegative() {
        when(databaseContext.getRoom(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomService.get(-1));
    }

    @Test
    void getThrowsWhenRoomIsNotFound() {
        when(databaseContext.getRoom(2)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomService.get(2));
    }

    @Test
    void validUpdateTest() {
        when(databaseContext.getRoom(room.getId())).thenReturn(room);

        assertDoesNotThrow(() -> roomService.update(room));
    }

    @Test
    void updateThrowsWhenRoomDoesntPassValidation() {
        room.setAmountOfPeople(-5);

        assertThrows(ValidationException.class,
                        () -> roomService.update(room));
    }

    @Test
    void updateThrowsWhenRoomIsNull() {
        assertThrows(ValidationException.class,
                () -> roomService.update(null));
    }

    @Test
    void validDeleteTest() {
        when(databaseContext.getRoom(room.getId())).thenReturn(room);

        assertDoesNotThrow(() -> roomService.delete(room.getId()));
    }

    @Test
    void deleteThrowsWhenIdIsZero() {
        when(databaseContext.getRoom(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomService.delete(0));
    }

    @Test
    void deleteThrowsWhenIdIsNegative() {
        when(databaseContext.getRoom(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomService.delete(-1));
    }

    @Test
    void deleteThrowsWhenRoomIsNotFound() {
        when(databaseContext.getRoom(2)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomService.delete(2));
    }

    @Test
    @Disabled
    void getFreeRoomsTest() {
        HashMap<Integer, Room> rooms = new HashMap<>();
        rooms.put(1, new Room());
        when(roomService.getFreeRooms()).thenReturn(rooms);

        assertEquals(rooms, roomService.getFreeRooms());
    }

    @Test
    @Disabled
    void getFreeRoomsWhenThereAreNoRooms() {
        when(roomService.getFreeRooms()).thenReturn(null);

        assertNull(roomService.getFreeRooms());
    }

    @AfterEach
    void cleanup() {
        databaseContext = null;
        roomService = null;
        hotel = null;
        room = null;
    }
}