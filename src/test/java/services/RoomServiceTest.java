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

class RoomServiceTest {
    @Mock
    private IDatabaseContext databaseContext;
    @InjectMocks
    private RoomService roomService;

    private Hotel hotel;
    private Room room;
    private HashMap<Integer, Hotel> hotelList;

    @BeforeEach
    public void init() {
        databaseContext = mock(IDatabaseContext.class);
        roomService = new RoomService(databaseContext);
        hotel = new Hotel(1, "Sample name", new LocalTime(7), new LocalTime(22));
        room = new Room(1, hotel, 400, 3);
        hotelList = new HashMap<Integer, Hotel>() {{ put(1, hotel); }};
    }

    @Test
    @DisplayName("validation of room" +
            "(returns false because of null argument")
    public void roomValidation2Test() {
        assertFalse(roomService.roomValidation(null));
    }

    @Test
    @DisplayName("validation of room" +
            "(returns false because of negative number of room)")
    public void roomValidation3Test() {
        room.setNumberOfRoom(-1);
        assertFalse(roomService.roomValidation(room));
    }

    @Test
    @DisplayName("validation of room" +
            "(returns false because of negative number of amount of people" +
            "in hotel room)")
    public void roomValidation4Test() {
        room.setAmountOfPeople(-1);
        assertFalse(roomService.roomValidation(room));
    }

    @Test
    @DisplayName("validation of room" +
            "(returns false because of 0 number of amount of people" +
            "in hotel room)")
    public void roomValidation5Test() {
        room.setAmountOfPeople(0);
        assertFalse(roomService.roomValidation(room));
    }

    @Test
    @DisplayName("validation of room" +
            "(returns false because hotel is null)")
    public void roomValidation6Test() {
        room.setHotel(null);
        assertFalse(roomService.roomValidation(room));
    }

    @Test
    public void validAddTest() {
        when(databaseContext.getHotels()).thenReturn(hotelList);
        when(databaseContext.getNextRoomId()).thenReturn(room.getId());

        assertDoesNotThrow(() -> roomService.add(room));
    }

    @Test
    public void addThrowsWhenRoomDoesntPassValidation() {
        room.setAmountOfPeople(-5);

        assertThrows(ValidationException.class,
                        () -> roomService.add(room));
    }

    @Test
    public void addThrowsWhenHotelIsNotFound() {
        when(databaseContext.getHotels()).thenReturn(new HashMap<>());

        assertThrows(ElementNotFoundException.class,
                () -> roomService.add(room));
    }

    @Test
    public void addThrowsWhenRoomIsNull() {
        assertThrows(ValidationException.class,
                () -> roomService.add(null));
    }

    @Test
    public void validGetTest() throws ElementNotFoundException {
        when(databaseContext.getRoom(room.getId())).thenReturn(room);

        Room result = roomService.get(room.getId());

        assertEquals(room, result);
    }

    @Test
    public void getThrowsWhenIdIsZero() {
        when(databaseContext.getRoom(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomService.get(0));
    }

    @Test
    public void getThrowsWhenIdIsNegative() {
        when(databaseContext.getRoom(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomService.get(-1));
    }

    @Test
    public void getThrowsWhenRoomIsNotFound() {
        when(databaseContext.getRoom(2)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomService.get(2));
    }

    @Test
    public void validGetAllTest() {
        HashMap<Integer, Room> roomList = new HashMap<Integer, Room>() {{
           put(1, room);
        }};
        when(databaseContext.getRooms()).thenReturn(roomList);

        assertEquals(roomList, roomService.get());
    }

    @Test
    public void getAllWhenListIsEmpty() {
        when(databaseContext.getRooms()).thenReturn(new HashMap<>());

        assertEquals(new HashMap<>(), roomService.get());
    }

    @Test
    public void validUpdateTest() {
        when(databaseContext.getRoom(room.getId())).thenReturn(room);

        assertDoesNotThrow(() -> roomService.update(room));
    }

    @Test
    public void updateThrowsWhenRoomDoesntPassValidation() {
        room.setAmountOfPeople(-5);

        assertThrows(ValidationException.class,
                        () -> roomService.update(room));
    }

    @Test
    public void updateThrowsWhenRoomIsNull() {
        assertThrows(ValidationException.class,
                () -> roomService.update(null));
    }

    @Test
    public void updateThrowsWhenRoomIsNotFound() {
        when(databaseContext.getRoom(room.getId())).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomService.update(room));
    }

    @Test
    public void validDeleteTest() {
        when(databaseContext.getRoom(room.getId())).thenReturn(room);

        assertDoesNotThrow(() -> roomService.delete(room.getId()));
    }

    @Test
    public void deleteThrowsWhenIdIsZero() {
        when(databaseContext.getRoom(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomService.delete(0));
    }

    @Test
    public void deleteThrowsWhenIdIsNegative() {
        when(databaseContext.getRoom(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomService.delete(-1));
    }

    @Test
    public void deleteThrowsWhenRoomIsNotFound() {
        when(databaseContext.getRoom(2)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomService.delete(2));
    }

    @Test
    @Disabled
    public void getFreeRoomsTest() {
        HashMap<Integer, Room> rooms = new HashMap<>();
        rooms.put(1, new Room());
        when(roomService.getFreeRooms()).thenReturn(rooms);

        assertEquals(rooms, roomService.getFreeRooms());
    }

    @Test
    @Disabled
    public void getFreeRoomsWhenThereAreNoRooms() {
        when(roomService.getFreeRooms()).thenReturn(null);

        assertNull(roomService.getFreeRooms());
    }

    @AfterEach
    public void cleanup() {
        databaseContext = null;
        roomService = null;
        hotel = null;
        room = null;
        hotelList = null;
    }
}