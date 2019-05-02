package services;

import database.IDatabaseContext;
import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.Hotel;
import models.Reservation;
import models.Room;
import models.User;
import org.joda.time.DateTime;
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
    private Room room1;
    private Room room2;
    private HashMap<Integer, Hotel> hotelList;

    @BeforeEach
    public void init() {
        databaseContext = mock(IDatabaseContext.class);
        roomService = new RoomService(databaseContext);
        hotel = new Hotel(1, "Sample name", new LocalTime(7), new LocalTime(22));
        room1 = new Room(1, hotel, 400, 3);
        room2 = new Room(2, hotel, 202, 2);
        hotelList = new HashMap<Integer, Hotel>() {{ put(1, hotel); }};
    }

    @Test
    @DisplayName("validation of room1" +
            "(returns false because of null argument")
    public void roomValidation2Test() {
        assertFalse(roomService.roomValidation(null));
    }

    @Test
    @DisplayName("validation of room1" +
            "(returns false because of negative number of room1)")
    public void roomValidation3Test() {
        room1.setNumberOfRoom(-1);
        assertFalse(roomService.roomValidation(room1));
    }

    @Test
    @DisplayName("validation of room1" +
            "(returns false because of negative number of amount of people" +
            "in hotel room1)")
    public void roomValidation4Test() {
        room1.setAmountOfPeople(-1);
        assertFalse(roomService.roomValidation(room1));
    }

    @Test
    @DisplayName("validation of room1" +
            "(returns false because of 0 number of amount of people" +
            "in hotel room1)")
    public void roomValidation5Test() {
        room1.setAmountOfPeople(0);
        assertFalse(roomService.roomValidation(room1));
    }

    @Test
    @DisplayName("validation of room1" +
            "(returns false because hotel is null)")
    public void roomValidation6Test() {
        room1.setHotel(null);
        assertFalse(roomService.roomValidation(room1));
    }

    @Test
    public void validAddTest() {
        when(databaseContext.getHotels()).thenReturn(hotelList);
        when(databaseContext.getNextRoomId()).thenReturn(room1.getId());

        assertDoesNotThrow(() -> roomService.add(room1));
    }

    @Test
    public void addThrowsWhenRoomDoesntPassValidation() {
        room1.setAmountOfPeople(-5);

        assertThrows(ValidationException.class,
                        () -> roomService.add(room1));
    }

    @Test
    public void addThrowsWhenHotelIsNotFound() {
        when(databaseContext.getHotels()).thenReturn(new HashMap<>());

        assertThrows(ElementNotFoundException.class,
                () -> roomService.add(room1));
    }

    @Test
    public void addThrowsWhenRoomIsNull() {
        assertThrows(ValidationException.class,
                () -> roomService.add(null));
    }

    @Test
    public void validGetTest() throws ElementNotFoundException {
        when(databaseContext.getRoom(room1.getId())).thenReturn(room1);

        Room result = roomService.get(room1.getId());

        assertEquals(room1, result);
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
           put(1, room1);
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
        when(databaseContext.getRoom(room1.getId())).thenReturn(room1);

        assertDoesNotThrow(() -> roomService.update(room1));
    }

    @Test
    public void updateThrowsWhenRoomDoesntPassValidation() {
        room1.setAmountOfPeople(-5);

        assertThrows(ValidationException.class,
                        () -> roomService.update(room1));
    }

    @Test
    public void updateThrowsWhenRoomIsNull() {
        assertThrows(ValidationException.class,
                () -> roomService.update(null));
    }

    @Test
    public void updateThrowsWhenRoomIsNotFound() {
        when(databaseContext.getRoom(room1.getId())).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomService.update(room1));
    }

    @Test
    public void validDeleteTest() {
        when(databaseContext.getRoom(room1.getId())).thenReturn(room1);

        assertDoesNotThrow(() -> roomService.delete(room1.getId()));
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
    public void getFreeRoomsTest() {
        HashMap<Integer, Room> rooms = new HashMap<>();
        rooms.put(1, room1);
        rooms.put(2, room2);
        when(databaseContext.getRooms()).thenReturn(rooms);
        when(databaseContext.getReservations()).thenReturn(new HashMap<Integer, Reservation>() {{
            put(1, new Reservation(1, new DateTime(2019, 1, 1, 11, 0),
                    new DateTime(2019, 1, 2, 11, 0), new User(), room2));
        }});

        HashMap<Integer, Room> result = new HashMap<>();
        result.put(1, room1);

        assertEquals(result, roomService.getFreeRooms());
    }

    @Test
    public void getFreeRoomsWhenAllRoomsAreFree() {
        HashMap<Integer, Room> rooms = new HashMap<>();
        rooms.put(1, room1);
        when(databaseContext.getRooms()).thenReturn(rooms);
        when(databaseContext.getReservations()).thenReturn(new HashMap<>());

        assertEquals(rooms, roomService.getFreeRooms());
    }

    @Test
    public void getFreeRoomsWhenThereAreNoRooms() {
        HashMap<Integer, Room> roomList = new HashMap<>();
        when(databaseContext.getRooms()).thenReturn(roomList);
        when(databaseContext.getReservations()).thenReturn(new HashMap<>());

        assertEquals(new HashMap<Integer, Room>(), roomService.getFreeRooms());
    }

    @AfterEach
    public void cleanup() {
        databaseContext = null;
        roomService = null;
        hotel = null;
        room1 = null;
        room2 = null;
        hotelList = null;
    }
}