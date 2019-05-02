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

import java.time.DateTimeException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {
    @Mock
    private IDatabaseContext databaseContext;
    @InjectMocks
    private ReservationService reservationService;

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
    void init() {
        databaseContext = mock(IDatabaseContext.class);
        reservationService = new ReservationService(databaseContext);
        hotel1 = new Hotel(1, "Sample name", new LocalTime(8), new LocalTime(23));
        hotel2 = new Hotel(2, "Sample name 2", new LocalTime(6), new LocalTime(20));
        room1 = new Room(1, hotel1, 200, 2);
        room2 = new Room(2, hotel2, 2, 1);
        user1 = new User(1, "test@test.com");
        user2 = new User(2, "test2@test2.com");
        user3 = new User(3, "test3@test3.com");
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
    void reservationValidationTest() {
        boolean result = reservationService.reservationValidation(reservation1);
        assertTrue(result);
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because of null argument)")
    void reservationValidation2Test() {
        assertFalse(reservationService.reservationValidation(null));
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because start date is after end date)")
    void reservationValidation3Test() {
        reservation1.setEndDate(new DateTime(2019, 1, 1, 11, 0));
        assertFalse(reservationService.reservationValidation(reservation1));
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because user in reservation is null)")
    void reservationValidation4Test() {
        reservation1.setUser(null);
        assertFalse(reservationService.reservationValidation(reservation1));
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because room in reservation is null)")
    void reservationValidation5Test() {
        reservation1.setRoom(null);
        assertFalse(reservationService.reservationValidation(reservation1));
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because user and room in reservation is null)")
    void reservationValidation6Test() {
        reservation1.setUser(null);
        reservation1.setRoom(null);
        assertFalse(reservationService.reservationValidation(reservation1));
    }

    @Test
    void validAddTest() {
        when(databaseContext.getUsers()).thenReturn(
                new HashMap<Integer, User>() {{
                    put(1, user1);
                    put(2, user2);
                    put(3, user3);
                }}
        );
        when(databaseContext.getRooms()).thenReturn(
                new HashMap<Integer, Room>() {{
                    put(1, room1);
                    put(2, room2);
                }}
        );

        assertDoesNotThrow(() -> reservationService.add(reservation1));
    }

    @Test
    void addThrowsWhenReservationDoesntPassValidation() {
        reservation1.setEndDate(new DateTime(2019, 1, 1, 11, 0));

        assertThrows(ValidationException.class,
                () -> reservationService.add(reservation1));
    }

    @Test
    void addThrowsWhenRoomIsNull() {
        reservation1.setRoom(null);

        assertThrows(ValidationException.class,
                () -> reservationService.add(reservation1));
    }

    @Test
    void addThrowsWhenUserIsNotFound() {
        when(databaseContext.getUsers()).thenReturn(new HashMap<>());

        assertThrows(ElementNotFoundException.class,
                () -> reservationService.add(reservation1));
    }

    @Test
    void addThrowsWhenRoomIsNotFound() {
        when(databaseContext.getUsers()).thenReturn(new HashMap<Integer, User>() {{ put(1, user1); }});
        when(databaseContext.getRooms()).thenReturn(new HashMap<>());

        assertThrows(ElementNotFoundException.class,
                () -> reservationService.add(reservation1));
    }

    @Test
    void addThrowsWhenReservationHasMinutesInDate() {
        when(databaseContext.getUsers()).thenReturn(new HashMap<Integer, User>() {{ put(1, user1); }});
        when(databaseContext.getRooms()).thenReturn(new HashMap<Integer, Room>() {{ put(1, room1); }});
        reservation1.setEndDate(new DateTime(2021, 1, 1, 11, 11));

        assertThrows(DateTimeException.class,
                () -> reservationService.add(reservation1));
    }

    @Test
    void addWhenThereAreOtherReservations() {
        when(databaseContext.getUsers()).thenReturn(new HashMap<Integer, User>() {{ put(1, user1); }});
        when(databaseContext.getRooms()).thenReturn(new HashMap<Integer, Room>() {{ put(1, room1); }});
        when(databaseContext.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
            {{ put(1, reservation2); }});

        assertDoesNotThrow(() -> reservationService.add(reservation1));
    }

    @Test
    void addThrowsWhenRoomIsReservedByOtherPersonInTheSameTime() {
        when(databaseContext.getUsers()).thenReturn(new HashMap<Integer, User>() {{
            put(1, user1);
            put(2, user2);
            put(3, user3);
        }});
        when(databaseContext.getRooms()).thenReturn(new HashMap<Integer, Room>() {{
            put(1, room1);
            put(2, room2);
        }});
        when(databaseContext.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
        {{ put(1, reservation1); }});

        Reservation newReservation = new Reservation(3, reservation1.getStartDate(), reservation1.getEndDate(),
                user1, room1);

        assertThrows(DateTimeException.class,
                () -> reservationService.add(newReservation));
    }

    @Test
    void addThrowsWhenRoomIsReservedByOtherPersonInTheSameTimeAndOtherRoom() {
        when(databaseContext.getUsers()).thenReturn(new HashMap<Integer, User>() {{
            put(1, user1);
            put(2, user2);
            put(3, user3);
        }});
        when(databaseContext.getRooms()).thenReturn(new HashMap<Integer, Room>() {{
            put(1, room1);
            put(2, room2);
        }});
        when(databaseContext.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
        {{ put(1, reservation1); }});

        Reservation newReservation = new Reservation(3, reservation1.getStartDate(), reservation1.getEndDate(),
                user1, room2);

        assertDoesNotThrow(() -> reservationService.add(newReservation));

    }

    @Test
    void addThrowsWhenRoomIsReservedByOtherPersonInSimilarTime() {
        when(databaseContext.getUsers()).thenReturn(new HashMap<Integer, User>() {{
            put(1, user1);
            put(2, user2);
            put(3, user3);
        }});
        when(databaseContext.getRooms()).thenReturn(new HashMap<Integer, Room>() {{
            put(1, room1);
            put(2, room2);
        }});
        when(databaseContext.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
        {{ put(1, reservation1); }});

        Reservation newReservation = new Reservation(3, new DateTime(2019, 5, 4, 11, 0),
                new DateTime(2019, 5, 6, 11, 0), user1, room1);

        assertThrows(DateTimeException.class,
                () -> reservationService.add(newReservation));
    }

    @Test
    void addThrowsWhenRoomIsReservedByOtherPersonInSimilarTime2() {
        when(databaseContext.getUsers()).thenReturn(new HashMap<Integer, User>() {{
            put(1, user1);
            put(2, user2);
            put(3, user3);
        }});
        when(databaseContext.getRooms()).thenReturn(new HashMap<Integer, Room>() {{
            put(1, room1);
            put(2, room2);
        }});
        when(databaseContext.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
        {{ put(1, reservation1); }});

        Reservation newReservation = new Reservation(3, new DateTime(2019, 5, 5, 20, 0),
                new DateTime(2019, 5, 6, 8, 0), user1, room1);

        assertThrows(DateTimeException.class,
                () -> reservationService.add(newReservation));
    }

    @Test
    void validGetTest() throws ElementNotFoundException {
        when(databaseContext.getReservation(reservation1.getId())).thenReturn(reservation1);

        assertEquals(reservation1, reservationService.get(reservation1.getId()));
    }

    @Test
    void getThrowsWhenIdIsZero() {
        when(databaseContext.getReservation(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> reservationService.get(0));
    }

    @Test
    void getThrowsWhenIdIsNegative() {
        when(databaseContext.getReservation(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> reservationService.get(-1));
    }

    @Test
    void getReturnsNullWhenReservationIsNotFound() {
        when(databaseContext.getReservation(4)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> reservationService.get(4));
    }

    @Test
    void validGetAllTest() {
        when(databaseContext.getReservations()).thenReturn(
                new HashMap<Integer, Reservation>() {{ put(1, reservation1); }});

        assertEquals(new HashMap<Integer, Reservation>() {{ put(1, reservation1); }}, reservationService.get());
    }

    @Test
    void getAllWhenListIsNull() {
        when(databaseContext.getReservations()).thenReturn(new HashMap<>());

        assertEquals(new HashMap<>(), reservationService.get());
    }

    @Test
    void validUpdateTest() {
        when(databaseContext.getUsers()).thenReturn(
                new HashMap<Integer, User>() {{
                    put(1, user1);
                    put(2, user2);
                    put(3, user3);
                }}
        );
        when(databaseContext.getRooms()).thenReturn(
                new HashMap<Integer, Room>() {{
                    put(1, room1);
                    put(2, room2);
                }}
        );
        when(databaseContext.getReservations()).thenReturn(
                new HashMap<Integer, Reservation>() {{
                    put(1, reservation1);
                    put(2, reservation2);
                    put(3, reservation3);
                }}
        );
        reservation1.setRoom(room2);

        assertDoesNotThrow(() -> reservationService.update(reservation1));
    }

    @Test
    void updateThrowsWhenReservationDoesntPassValidation() {
        reservation1.setStartDate(new DateTime(2019, 1, 1, 11, 0));
        reservation1.setEndDate(new DateTime(2018, 1, 1, 11, 0));

        assertThrows(ValidationException.class,
                () -> reservationService.update(reservation1));
    }

    @Test
    void updateThrowsWhenReservationIsNull() {
        assertThrows(ValidationException.class,
                () -> reservationService.update(null));
    }

    @Test
    void updateThrowsWhenUserIsNotFound() {
        when(databaseContext.getUsers()).thenReturn(new HashMap<>());

        assertThrows(ElementNotFoundException.class,
                () -> reservationService.update(reservation1));
    }

    @Test
    void updateThrowsWhenRoomIsNotFound() {
        when(databaseContext.getUsers()).thenReturn(new HashMap<Integer, User>() {{ put(1, user1); }});
        when(databaseContext.getRooms()).thenReturn(new HashMap<>());

        assertThrows(ElementNotFoundException.class,
                () -> reservationService.update(reservation1));
    }

    @Test
    void updateThrowsWhenReservationHasMinutesInDate() {
        when(databaseContext.getUsers()).thenReturn(new HashMap<Integer, User>() {{ put(1, user1); }});
        when(databaseContext.getRooms()).thenReturn(new HashMap<Integer, Room>() {{ put(1, room1); }});
        reservation1.setEndDate(new DateTime(2021, 1, 1, 11, 11));

        assertThrows(DateTimeException.class,
                () -> reservationService.update(reservation1));
    }

    @Test
    void updateThrowsWhenReservationIsNotFound() {
        when(databaseContext.getUsers()).thenReturn(new HashMap<Integer, User>() {{ put(1, user1); }});
        when(databaseContext.getRooms()).thenReturn(new HashMap<Integer, Room>() {{ put(1, room1); }});
        when(databaseContext.getReservations()).thenReturn(new HashMap<>());

        assertThrows(ElementNotFoundException.class,
                () -> reservationService.update(reservation1));
    }

    @Test
    void updateThrowsWhenRoomIsReservedByOtherPersonInTheSameTime() {
        Reservation newReservation = new Reservation(3, new DateTime(2019, 12, 12, 12, 0),
                new DateTime(2019, 12, 13, 12, 0),
                user1, room1);
        when(databaseContext.getUsers()).thenReturn(new HashMap<Integer, User>() {{
            put(1, user1);
            put(2, user2);
            put(3, user3);
        }});
        when(databaseContext.getRooms()).thenReturn(new HashMap<Integer, Room>() {{
            put(1, room1);
            put(2, room2);
        }});
        when(databaseContext.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
        {{  put(1, reservation1);
            put(2, reservation2);
            put(3, newReservation);
        }});

        newReservation.setStartDate(reservation1.getStartDate());
        newReservation.setEndDate(reservation1.getEndDate());

        assertThrows(DateTimeException.class,
                () -> reservationService.update(newReservation));
    }

    @Test
    void updateThrowsWhenRoomIsReservedByOtherPersonInSimilarTime() {
        Reservation newReservation = new Reservation(3, new DateTime(2019, 12, 12, 12, 0),
                new DateTime(2019, 12, 13, 12, 0),
                user1, room1);
        when(databaseContext.getUsers()).thenReturn(new HashMap<Integer, User>() {{
            put(1, user1);
            put(2, user2);
            put(3, user3);
        }});
        when(databaseContext.getRooms()).thenReturn(new HashMap<Integer, Room>() {{
            put(1, room1);
            put(2, room2);
        }});
        when(databaseContext.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
        {{  put(1, reservation1);
            put(2, reservation2);
            put(3, newReservation);
        }});

        newReservation.setStartDate(new DateTime(2019, 5, 4, 11, 0));
        newReservation.setEndDate(new DateTime(2019, 5, 6, 11, 0));

        assertThrows(DateTimeException.class,
                () -> reservationService.update(newReservation));
    }

    @Test
    void updateThrowsWhenRoomIsReservedByOtherPersonInTheSameTimeAndSameRoom() {
        Reservation newReservation = new Reservation(3, new DateTime(2019, 12, 12, 12, 0),
                new DateTime(2019, 12, 13, 12, 0),
                user1, room1);
        when(databaseContext.getUsers()).thenReturn(new HashMap<Integer, User>() {{
            put(1, user1);
            put(2, user2);
            put(3, user3);
        }});
        when(databaseContext.getRooms()).thenReturn(new HashMap<Integer, Room>() {{
            put(1, room1);
            put(2, room2);
        }});
        when(databaseContext.getReservations()).thenReturn(new HashMap<Integer, Reservation>()
        {{  put(1, reservation1);
            put(2, reservation2);
            put(3, newReservation);
        }});

        newReservation.setStartDate(reservation1.getStartDate());
        newReservation.setEndDate(reservation1.getEndDate());
        newReservation.setRoom(reservation1.getRoom());

        assertThrows(DateTimeException.class,
                () -> reservationService.update(newReservation));
    }

    @Test
    void validDeleteTest() {
        when(databaseContext.getReservation(reservation1.getId())).thenReturn(reservation1);

        assertDoesNotThrow(() -> reservationService.delete(reservation1.getId()));
    }

    @Test
    void deleteThrowsWhenIdIsZero() {
        when(databaseContext.getReservation(0)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> reservationService.delete(0));
    }

    @Test
    void deleteThrowsWhenIdIsNegative() {
        when(databaseContext.getReservation(-1)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> reservationService.delete(-1));
    }

    @Test
    void deleteThrowsWhenReservationIsNotFound() {
        when(databaseContext.getReservation(4)).thenReturn(null);

        assertThrows(ElementNotFoundException.class,
                () -> reservationService.delete(4));
    }

    @Test
    void getEmptyReservationsOfUser() {
        when(reservationService.getReservationsOfUser(user1)).thenReturn(new HashMap<>());

        assertEquals(new HashMap<>(), reservationService.getReservationsOfUser(user1));
    }

    @Test
    void getReservationsOfUser() {
        when(databaseContext.getReservations()).thenReturn(
                new HashMap<Integer, Reservation>() {{
                    put(1, reservation1);
                    put(2, reservation2);
                    put(3, reservation3);
                }}
        );
        HashMap<Integer, Reservation> list = new HashMap<Integer, Reservation>()
        {{
            put(1, reservation1);
            put(2, reservation3);
        }};

        assertEquals(list, reservationService.getReservationsOfUser(user1));
    }

    @Test
    void getReservationsOfUserThrowsWhenUserIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.getReservationsOfUser(null));
    }

    @AfterEach
    void cleanup() {
        databaseContext = null;
        reservationService = null;
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