package services;

import database.IDatabaseContext;
import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.Hotel;
import models.Reservation;
import models.Room;
import models.User;
import org.easymock.EasyMockSupport;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceTest {
    @Mock
    private IDatabaseContext databaseContextMockito;
    @InjectMocks
    private RoomService roomServiceMockito;

    private EasyMockSupport easyMockSupport;
    @org.easymock.Mock
    private IDatabaseContext databaseContextEasyMock;
    private RoomService roomServiceEasyMock;

    private Hotel hotel;
    private Room room1;
    private Room room2;
    private HashMap<Integer, Hotel> hotelList;

    @BeforeEach
    public void init() {
        easyMockSupport = new EasyMockSupport();
        databaseContextEasyMock = easyMockSupport.mock(IDatabaseContext.class);
        roomServiceEasyMock = new RoomService(databaseContextEasyMock);
        databaseContextMockito = mock(IDatabaseContext.class);
        roomServiceMockito = new RoomService(databaseContextMockito);
        hotel = new Hotel(1, "Sample name", new LocalTime(7), new LocalTime(22));
        room1 = new Room(1, hotel, 400, 3);
        room2 = new Room(2, hotel, 202, 2);
        hotelList = new HashMap<Integer, Hotel>() {{ put(1, hotel); }};
    }

    @Test
    @DisplayName("validation of room1" +
            "(returns false because of null argument")
    public void roomValidation2Test() {
        assertFalse(roomServiceMockito.roomValidation(null));
    }

    @Test
    @DisplayName("validation of room1" +
            "(returns false because of negative number of room1)")
    public void roomValidation3Test() {
        room1.setRoomNumber(-1);
        assertFalse(roomServiceMockito.roomValidation(room1));
    }

    @Test
    @DisplayName("validation of room1" +
            "(returns false because of negative number of amount of people" +
            "in hotel room1)")
    public void roomValidation4Test() {
        room1.setNumberOfGuests(-1);
        assertFalse(roomServiceMockito.roomValidation(room1));
    }

    @Test
    @DisplayName("validation of room1" +
            "(returns false because of 0 number of amount of people" +
            "in hotel room1)")
    public void roomValidation5Test() {
        room1.setNumberOfGuests(0);
        assertFalse(roomServiceMockito.roomValidation(room1));
    }

    @Test
    @DisplayName("validation of room1" +
            "(returns false because hotel is null)")
    public void roomValidation6Test() {
        room1.setHotel(null);
        assertFalse(roomServiceMockito.roomValidation(room1));
    }

    @Test
    public void validAddTest() {
        when(databaseContextMockito.getHotels()).thenReturn(hotelList);
        when(databaseContextMockito.getNextRoomId()).thenReturn(room1.getId());

        assertDoesNotThrow(() -> roomServiceMockito.add(room1));
    }

    @Test
    public void addThrowsWhenRoomDoesntPassValidation() {
        room1.setNumberOfGuests(-5);

        assertThrows(ValidationException.class,
                        () -> roomServiceMockito.add(room1));
    }

    @Test
    public void addThrowsWhenHotelIsNotFound() {
        when(databaseContextMockito.getHotels()).thenReturn(new HashMap<>());

        assertThrows(ElementNotFoundException.class,
                () -> roomServiceMockito.add(room1));
    }

    @Test
    public void addThrowsWhenRoomIsNull() {
        assertThrows(ValidationException.class,
                () -> roomServiceMockito.add(null));
    }

    @Test
    public void validGetTest() throws ElementNotFoundException {
        when(databaseContextMockito.getRoom(room1.getId())).thenReturn(room1);

        Room result = roomServiceMockito.get(room1.getId());

        assertEquals(room1, result);
    }

    @Test
    public void getThrowsWhenIdIsZero() {
        when(databaseContextMockito.getRoom(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomServiceMockito.get(0));
    }

    @Test
    public void getThrowsWhenIdIsNegative() {
        when(databaseContextMockito.getRoom(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomServiceMockito.get(-1));
    }

    @Test
    public void getThrowsWhenRoomIsNotFound() {
        when(databaseContextMockito.getRoom(2)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomServiceMockito.get(2));
    }

    @Test
    public void validGetAllTest() {
        HashMap<Integer, Room> roomList = new HashMap<Integer, Room>() {{
           put(1, room1);
        }};
        when(databaseContextMockito.getRooms()).thenReturn(roomList);

        assertEquals(roomList, roomServiceMockito.get());
    }

    @Test
    public void getAllWhenListIsEmpty() {
        when(databaseContextMockito.getRooms()).thenReturn(new HashMap<>());

        assertEquals(new HashMap<>(), roomServiceMockito.get());
    }

    @Test
    public void validUpdateTest() {
        when(databaseContextMockito.getRoom(room1.getId())).thenReturn(room1);

        assertDoesNotThrow(() -> roomServiceMockito.update(room1));
    }

    @Test
    public void updateThrowsWhenRoomDoesntPassValidation() {
        room1.setNumberOfGuests(-5);

        assertThrows(ValidationException.class,
                        () -> roomServiceMockito.update(room1));
    }

    @Test
    public void updateThrowsWhenRoomIsNull() {
        assertThrows(ValidationException.class,
                () -> roomServiceMockito.update(null));
    }

    @Test
    public void updateThrowsWhenRoomIsNotFound() {
        when(databaseContextMockito.getRoom(room1.getId())).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomServiceMockito.update(room1));
    }

    @Test
    public void validDeleteTest() {
        when(databaseContextMockito.getRoom(room1.getId())).thenReturn(room1);

        assertDoesNotThrow(() -> roomServiceMockito.delete(room1.getId()));
    }

    @Test
    public void deleteThrowsWhenIdIsZero() {
        when(databaseContextMockito.getRoom(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomServiceMockito.delete(0));
    }

    @Test
    public void deleteThrowsWhenIdIsNegative() {
        when(databaseContextMockito.getRoom(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomServiceMockito.delete(-1));
    }

    @Test
    public void deleteThrowsWhenRoomIsNotFound() {
        when(databaseContextMockito.getRoom(2)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> roomServiceMockito.delete(2));
    }

    @Test
    public void getFreeRoomsTest() {
        HashMap<Integer, Room> rooms = new HashMap<>();
        rooms.put(1, room1);
        rooms.put(2, room2);
        expect(databaseContextEasyMock.getRooms()).andReturn(rooms);
        expect(databaseContextEasyMock.getReservations()).andReturn(new HashMap<Integer, Reservation>() {{
            put(1, new Reservation(1, new DateTime(2019, 1, 1, 11, 0),
                    new DateTime(2019, 1, 2, 11, 0), new User(), room2));
        }});
        replay(databaseContextEasyMock);

        HashMap<Integer, Room> result = new HashMap<>();
        result.put(1, room1);

        assertEquals(result, roomServiceEasyMock.getFreeRooms());
    }

    @Test
    public void getFreeRoomsWhenAllRoomsAreFree() {
        HashMap<Integer, Room> rooms = new HashMap<>();
        rooms.put(1, room1);
        expect(databaseContextEasyMock.getRooms()).andReturn(rooms);
        expect(databaseContextEasyMock.getReservations()).andReturn(new HashMap<>());
        replay(databaseContextEasyMock);

        assertEquals(rooms, roomServiceEasyMock.getFreeRooms());
    }

    @Test
    public void getFreeRoomsWhenThereAreNoRooms() {
        HashMap<Integer, Room> roomList = new HashMap<>();
        expect(databaseContextEasyMock.getRooms()).andReturn(roomList);
        expect(databaseContextEasyMock.getReservations()).andReturn(new HashMap<>());
        replay(databaseContextEasyMock);

        assertEquals(new HashMap<Integer, Room>(), roomServiceEasyMock.getFreeRooms());
    }

    @Test
    public void getFreeRoomsOfSpecificHotelWhenAllRoomsAreFree() {
        HashMap<Integer, Room> rooms = new HashMap<>();
        rooms.put(1, room1);
        expect(databaseContextEasyMock.getRooms()).andReturn(rooms);
        expect(databaseContextEasyMock.getReservations()).andReturn(new HashMap<>());
        replay(databaseContextEasyMock);

        assertEquals(rooms, roomServiceEasyMock.getFreeRooms(hotel));
    }

    @Test
    public void getFreeRoomsOfSpecificHotelWhenThereAreNoRooms() {
        HashMap<Integer, Room> roomList = new HashMap<>();
        expect(databaseContextEasyMock.getRooms()).andReturn(roomList);
        expect(databaseContextEasyMock.getReservations()).andReturn(new HashMap<>());
        replay(databaseContextEasyMock);

        assertEquals(new HashMap<Integer, Room>(), roomServiceEasyMock.getFreeRooms(hotel));
    }

    @AfterEach
    public void cleanup() {
        databaseContextMockito = null;
        roomServiceMockito = null;
        hotel = null;
        room1 = null;
        room2 = null;
        hotelList = null;
    }
}