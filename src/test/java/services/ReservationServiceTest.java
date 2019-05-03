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

import java.time.DateTimeException;
import java.util.HashMap;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {
    @Mock
    private IDatabaseContext databaseContextMockito;
    @InjectMocks
    private ReservationService reservationServiceMockito;

    private EasyMockSupport easyMockSupport;
    private IDatabaseContext databaseContextEasyMock;
    private ReservationService reservationServiceEasyMock;

    private Hotel hotel1;
    private Hotel hotel2;
    private Room room1;
    private Room room2;
    private User user1;
    private User user2;
    private User user3;
    private Reservation reservation1;
    private Reservation reservation2;
    private Reservation reservation3;

    @BeforeEach
    public void init() {
        easyMockSupport = new EasyMockSupport();
        databaseContextMockito = mock(IDatabaseContext.class);
        reservationServiceMockito = new ReservationService(databaseContextMockito);
        databaseContextEasyMock = easyMockSupport.mock(IDatabaseContext.class);
        reservationServiceEasyMock = new ReservationService(databaseContextEasyMock);
        hotel1 = new Hotel(1, "Sample name", new LocalTime(8), new LocalTime(23));
        hotel2 = new Hotel(2, "Sample name 2", new LocalTime(6), new LocalTime(20));
        room1 = new Room(1, hotel1, 200, 2);
        room2 = new Room(2, hotel2, 2, 1);
        user1 = new User(1, "test@test.com",
                "90b94d224ee82c837143ea6f0308c596f0142612678a036c65041b246d52df22");
        user2 = new User(2, "test2@test2.com",
                "f99d2fa7c0014582e2fcc0d1c4cd04d527a909753d95824b8ac4fec88639a191");
        user3 = new User(3, "test3@test3.com",
                "eef021ea8f0d213f5b4975f77d52061b4fb3ff5aa212106de6376da88399338f");
        reservation1 = new Reservation(1, new DateTime(2019, 5, 5, 11, 0),
                new DateTime(2019, 5, 6, 11, 0),
                user1, room1);
        reservation2 = new Reservation(2, new DateTime(2019, 6, 6, 11, 0),
                new DateTime(2019, 6, 9, 11, 0),
                user2, room2);
        reservation3 = new Reservation(3, new DateTime(2019, 8, 1, 11, 0),
                new DateTime(2019, 8, 2, 11, 0),
                user1, room2);
    }

    @Test
    @DisplayName("validation of reservation (valid)")
    public void reservationValidationTest() {
        boolean result = reservationServiceMockito.reservationValidation(reservation1);
        assertTrue(result);
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because of null argument)")
    public void reservationValidation2Test() {
        assertFalse(reservationServiceMockito.reservationValidation(null));
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because start date is after end date)")
    public void reservationValidation3Test() {
        reservation1.setEndDate(new DateTime(2019, 1, 1, 11, 0));
        assertFalse(reservationServiceMockito.reservationValidation(reservation1));
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because user in reservation is null)")
    public void reservationValidation4Test() {
        reservation1.setUser(null);
        assertFalse(reservationServiceMockito.reservationValidation(reservation1));
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because room in reservation is null)")
    public void reservationValidation5Test() {
        reservation1.setRoom(null);
        assertFalse(reservationServiceMockito.reservationValidation(reservation1));
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because user and room in reservation is null)")
    public void reservationValidation6Test() {
        reservation1.setUser(null);
        reservation1.setRoom(null);
        assertFalse(reservationServiceMockito.reservationValidation(reservation1));
    }

    @Test
    public void validAddTest() {
        when(databaseContextMockito.getUsers()).thenReturn(
                new HashMap<Integer, User>() {{
                    put(1, user1);
                    put(2, user2);
                    put(3, user3);
                }}
        );
        when(databaseContextMockito.getRooms()).thenReturn(
                new HashMap<Integer, Room>() {{
                    put(1, room1);
                    put(2, room2);
                }}
        );

        assertDoesNotThrow(() -> reservationServiceMockito.add(reservation1));
    }

    @Test
    public void addThrowsWhenReservationDoesntPassValidation() {
        reservation1.setEndDate(new DateTime(2019, 1, 1, 11, 0));

        assertThrows(ValidationException.class,
                () -> reservationServiceMockito.add(reservation1));
    }

    @Test
    public void addThrowsWhenRoomIsNull() {
        reservation1.setRoom(null);

        assertThrows(ValidationException.class,
                () -> reservationServiceMockito.add(reservation1));
    }

    @Test
    public void addThrowsWhenUserIsNotFound() {
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<>());

        assertThrows(ElementNotFoundException.class,
                () -> reservationServiceMockito.add(reservation1));
    }

    @Test
    public void addThrowsWhenRoomIsNotFound() {
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<Integer, User>() {{ put(1, user1); }});
        when(databaseContextMockito.getRooms()).thenReturn(new HashMap<>());

        assertThrows(ElementNotFoundException.class,
                () -> reservationServiceMockito.add(reservation1));
    }

    @Test
    public void addThrowsWhenReservationHasMinutesInDate() {
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<Integer, User>() {{ put(1, user1); }});
        when(databaseContextMockito.getRooms()).thenReturn(new HashMap<Integer, Room>() {{ put(1, room1); }});
        reservation1.setEndDate(new DateTime(2021, 1, 1, 11, 11));

        assertThrows(DateTimeException.class,
                () -> reservationServiceMockito.add(reservation1));
    }

    @Test
    public void addWhenThereAreOtherReservations() {
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<Integer, User>() {{ put(1, user1); }});
        when(databaseContextMockito.getRooms()).thenReturn(new HashMap<Integer, Room>() {{ put(1, room1); }});
        when(databaseContextMockito.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
            {{ put(1, reservation2); }});

        assertDoesNotThrow(() -> reservationServiceMockito.add(reservation1));
    }

    @Test
    public void addThrowsWhenRoomIsReservedByOtherPersonInTheSameTime() {
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<Integer, User>() {{
            put(1, user1);
            put(2, user2);
            put(3, user3);
        }});
        when(databaseContextMockito.getRooms()).thenReturn(new HashMap<Integer, Room>() {{
            put(1, room1);
            put(2, room2);
        }});
        when(databaseContextMockito.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
        {{ put(1, reservation1); }});

        Reservation newReservation = new Reservation(3, reservation1.getStartDate(), reservation1.getEndDate(),
                user1, room1);

        assertThrows(DateTimeException.class,
                () -> reservationServiceMockito.add(newReservation));
    }

    @Test
    public void addThrowsWhenRoomIsReservedByOtherPersonInTheSameTimeAndOtherRoom() {
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<Integer, User>() {{
            put(1, user1);
            put(2, user2);
            put(3, user3);
        }});
        when(databaseContextMockito.getRooms()).thenReturn(new HashMap<Integer, Room>() {{
            put(1, room1);
            put(2, room2);
        }});
        when(databaseContextMockito.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
        {{ put(1, reservation1); }});

        Reservation newReservation = new Reservation(3, reservation1.getStartDate(), reservation1.getEndDate(),
                user1, room2);

        assertDoesNotThrow(() -> reservationServiceMockito.add(newReservation));

    }

    @Test
    public void addThrowsWhenRoomIsReservedByOtherPersonInSimilarTime() {
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<Integer, User>() {{
            put(1, user1);
            put(2, user2);
            put(3, user3);
        }});
        when(databaseContextMockito.getRooms()).thenReturn(new HashMap<Integer, Room>() {{
            put(1, room1);
            put(2, room2);
        }});
        when(databaseContextMockito.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
        {{ put(1, reservation1); }});

        Reservation newReservation = new Reservation(3, new DateTime(2019, 5, 4, 11, 0),
                new DateTime(2019, 5, 6, 11, 0), user1, room1);

        assertThrows(DateTimeException.class,
                () -> reservationServiceMockito.add(newReservation));
    }

    @Test
    public void addThrowsWhenRoomIsReservedByOtherPersonInSimilarTime2() {
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<Integer, User>() {{
            put(1, user1);
            put(2, user2);
            put(3, user3);
        }});
        when(databaseContextMockito.getRooms()).thenReturn(new HashMap<Integer, Room>() {{
            put(1, room1);
            put(2, room2);
        }});
        when(databaseContextMockito.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
        {{ put(1, reservation1); }});

        Reservation newReservation = new Reservation(3, new DateTime(2019, 5, 5, 20, 0),
                new DateTime(2019, 5, 6, 8, 0), user1, room1);

        assertThrows(DateTimeException.class,
                () -> reservationServiceMockito.add(newReservation));
    }

    @Test
    public void validGetTest() throws ElementNotFoundException {
        when(databaseContextMockito.getReservation(reservation1.getId())).thenReturn(reservation1);

        assertEquals(reservation1, reservationServiceMockito.get(reservation1.getId()));
    }

    @Test
    public void getThrowsWhenIdIsZero() {
        when(databaseContextMockito.getReservation(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> reservationServiceMockito.get(0));
    }

    @Test
    public void getThrowsWhenIdIsNegative() {
        when(databaseContextMockito.getReservation(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> reservationServiceMockito.get(-1));
    }

    @Test
    public void getReturnsNullWhenReservationIsNotFound() {
        when(databaseContextMockito.getReservation(4)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> reservationServiceMockito.get(4));
    }

    @Test
    public void validGetAllTest() {
        when(databaseContextMockito.getReservations()).thenReturn(
                new HashMap<Integer, Reservation>() {{ put(1, reservation1); }});

        assertEquals(new HashMap<Integer, Reservation>() {{ put(1, reservation1); }}, reservationServiceMockito.get());
    }

    @Test
    public void getAllWhenListIsNull() {
        when(databaseContextMockito.getReservations()).thenReturn(new HashMap<>());

        assertEquals(new HashMap<>(), reservationServiceMockito.get());
    }

    @Test
    public void validUpdateTest() {
        when(databaseContextMockito.getUsers()).thenReturn(
                new HashMap<Integer, User>() {{
                    put(1, user1);
                    put(2, user2);
                    put(3, user3);
                }}
        );
        when(databaseContextMockito.getRooms()).thenReturn(
                new HashMap<Integer, Room>() {{
                    put(1, room1);
                    put(2, room2);
                }}
        );
        when(databaseContextMockito.getReservations()).thenReturn(
                new HashMap<Integer, Reservation>() {{
                    put(1, reservation1);
                    put(2, reservation2);
                    put(3, reservation3);
                }}
        );
        reservation1.setRoom(room2);

        assertDoesNotThrow(() -> reservationServiceMockito.update(reservation1));
    }

    @Test
    public void updateThrowsWhenReservationDoesntPassValidation() {
        reservation1.setStartDate(new DateTime(2019, 1, 1, 11, 0));
        reservation1.setEndDate(new DateTime(2018, 1, 1, 11, 0));

        assertThrows(ValidationException.class,
                () -> reservationServiceMockito.update(reservation1));
    }

    @Test
    public void updateThrowsWhenReservationIsNull() {
        assertThrows(ValidationException.class,
                () -> reservationServiceMockito.update(null));
    }

    @Test
    public void updateThrowsWhenUserIsNotFound() {
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<>());

        assertThrows(ElementNotFoundException.class,
                () -> reservationServiceMockito.update(reservation1));
    }

    @Test
    public void updateThrowsWhenRoomIsNotFound() {
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<Integer, User>() {{ put(1, user1); }});
        when(databaseContextMockito.getRooms()).thenReturn(new HashMap<>());

        assertThrows(ElementNotFoundException.class,
                () -> reservationServiceMockito.update(reservation1));
    }

    @Test
    public void updateThrowsWhenReservationHasMinutesInDate() {
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<Integer, User>() {{ put(1, user1); }});
        when(databaseContextMockito.getRooms()).thenReturn(new HashMap<Integer, Room>() {{ put(1, room1); }});
        reservation1.setEndDate(new DateTime(2021, 1, 1, 11, 11));

        assertThrows(DateTimeException.class,
                () -> reservationServiceMockito.update(reservation1));
    }

    @Test
    public void updateThrowsWhenReservationIsNotFound() {
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<Integer, User>() {{ put(1, user1); }});
        when(databaseContextMockito.getRooms()).thenReturn(new HashMap<Integer, Room>() {{ put(1, room1); }});
        when(databaseContextMockito.getReservations()).thenReturn(new HashMap<>());

        assertThrows(ElementNotFoundException.class,
                () -> reservationServiceMockito.update(reservation1));
    }

    @Test
    public void updateThrowsWhenRoomIsReservedByOtherPersonInTheSameTime() {
        Reservation newReservation = new Reservation(3, new DateTime(2019, 12, 12, 12, 0),
                new DateTime(2019, 12, 13, 12, 0),
                user1, room1);
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<Integer, User>() {{
            put(1, user1);
            put(2, user2);
            put(3, user3);
        }});
        when(databaseContextMockito.getRooms()).thenReturn(new HashMap<Integer, Room>() {{
            put(1, room1);
            put(2, room2);
        }});
        when(databaseContextMockito.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
        {{  put(1, reservation1);
            put(2, reservation2);
            put(3, newReservation);
        }});

        newReservation.setStartDate(reservation1.getStartDate());
        newReservation.setEndDate(reservation1.getEndDate());

        assertThrows(DateTimeException.class,
                () -> reservationServiceMockito.update(newReservation));
    }

    @Test
    public void updateThrowsWhenRoomIsReservedByOtherPersonInSimilarTime() {
        Reservation newReservation = new Reservation(3, new DateTime(2019, 12, 12, 12, 0),
                new DateTime(2019, 12, 13, 12, 0),
                user1, room1);
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<Integer, User>() {{
            put(1, user1);
            put(2, user2);
            put(3, user3);
        }});
        when(databaseContextMockito.getRooms()).thenReturn(new HashMap<Integer, Room>() {{
            put(1, room1);
            put(2, room2);
        }});
        when(databaseContextMockito.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
        {{  put(1, reservation1);
            put(2, reservation2);
            put(3, newReservation);
        }});

        newReservation.setStartDate(new DateTime(2019, 5, 4, 11, 0));
        newReservation.setEndDate(new DateTime(2019, 5, 6, 11, 0));

        assertThrows(DateTimeException.class,
                () -> reservationServiceMockito.update(newReservation));
    }

    @Test
    public void updateThrowsWhenRoomIsReservedByOtherPersonInTheSameTimeAndSameRoom() {
        Reservation newReservation = new Reservation(3, new DateTime(2019, 12, 12, 12, 0),
                new DateTime(2019, 12, 13, 12, 0),
                user1, room1);
        when(databaseContextMockito.getUsers()).thenReturn(new HashMap<Integer, User>() {{
            put(1, user1);
            put(2, user2);
            put(3, user3);
        }});
        when(databaseContextMockito.getRooms()).thenReturn(new HashMap<Integer, Room>() {{
            put(1, room1);
            put(2, room2);
        }});
        when(databaseContextMockito.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
        {{  put(1, reservation1);
            put(2, reservation2);
            put(3, newReservation);
        }});

        newReservation.setStartDate(reservation1.getStartDate());
        newReservation.setEndDate(reservation1.getEndDate());
        newReservation.setRoom(reservation1.getRoom());

        assertThrows(DateTimeException.class,
                () -> reservationServiceMockito.update(newReservation));
    }

    @Test
    public void validDeleteTest() {
        when(databaseContextMockito.getReservation(reservation1.getId())).thenReturn(reservation1);

        assertDoesNotThrow(() -> reservationServiceMockito.delete(reservation1.getId()));
    }

    @Test
    public void deleteThrowsWhenIdIsZero() {
        when(databaseContextMockito.getReservation(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> reservationServiceMockito.delete(0));
    }

    @Test
    public void deleteThrowsWhenIdIsNegative() {
        when(databaseContextMockito.getReservation(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> reservationServiceMockito.delete(-1));
    }

    @Test
    public void deleteThrowsWhenReservationIsNotFound() {
        when(databaseContextMockito.getReservation(4)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> reservationServiceMockito.delete(4));
    }

    @Test
    public void getEmptyReservationsOfUser() {
        when(reservationServiceMockito.getReservationsOfUser(user1)).thenReturn(new HashMap<>());

        assertEquals(new HashMap<>(), reservationServiceMockito.getReservationsOfUser(user1));
    }

    @Test
    public void getReservationsOfUser() {
        expect(databaseContextEasyMock.getReservations()).andReturn(
                new HashMap<Integer, Reservation>() {{
                    put(1, reservation1);
                    put(2, reservation2);
                    put(3, reservation3);
                }}
        );
        replay(databaseContextEasyMock);

        HashMap<Integer, Reservation> list = new HashMap<Integer, Reservation>()
        {{
            put(1, reservation1);
            put(2, reservation3);
        }};

        assertEquals(list, reservationServiceEasyMock.getReservationsOfUser(user1));
    }

    @Test
    public void getReservationsOfUserThrowsWhenUserIsNull() {
        expect(databaseContextEasyMock.getReservations()).andReturn(new HashMap<>());
        replay(databaseContextEasyMock);

        assertThrows(IllegalArgumentException.class,
                () -> reservationServiceEasyMock.getReservationsOfUser(null));
    }

    @AfterEach
    public void cleanup() {
        databaseContextEasyMock = null;
        reservationServiceEasyMock = null;
        databaseContextMockito = null;
        reservationServiceMockito = null;
        hotel1 = null;
        hotel2 = null;
        room1 = null;
        room2 = null;
        user1 = null;
        user2 = null;
        user3 = null;
        reservation1 = null;
        reservation2 = null;
        reservation3 = null;
    }
}