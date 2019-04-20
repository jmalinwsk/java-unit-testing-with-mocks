package services;

import database.Database;
import models.*;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.*;

import java.time.DateTimeException;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceTest {
    private Database database;
    private ReservationService reservationService;
    private RoomService roomService;
    private UserService userService;
    private HotelService hotelService;
    private Reservation reservation;
    private Reservation reservation2;
    private Reservation reservation3;
    private Room room;
    private Room room2;
    private User user;
    private User user2;
    private User user3;
    private Hotel hotel;
    private Hotel hotel2;

    @BeforeEach
    public void init() {
        database = new Database();
        reservationService = new ReservationService();
        roomService = new RoomService();
        userService = new UserService();
        hotelService = new HotelService();
        hotel = new Hotel("Sample name", new LocalTime(8), new LocalTime(23));
        hotel2 = new Hotel("Sample name 2", new LocalTime(6), new LocalTime(20));
        room = new Room(hotel, 200, 2);
        room2 = new Room(hotel2, 2, 1);
        user = new User("test@test.com");
        user2 = new User("test2@test2.com");
        user3 = new User("test3@test3.com");
        reservation = new Reservation(new DateTime(2019, 5, 5, 11, 0),
                new DateTime(2019, 5, 6, 11, 0),
                user, room);
        reservation2 = new Reservation(new DateTime(2019, 6, 6, 11, 0),
                new DateTime(2019, 6, 9, 11, 0),
                user2, room2);
        reservation3 = new Reservation(new DateTime(2019, 8, 1, 11, 0),
                new DateTime(2019, 8, 2, 11, 0),
                user, room2);
        hotelService.addHotelToDatabase(database, hotel);
        hotelService.addHotelToDatabase(database, hotel2);
        roomService.addRoomToDatabase(database, room);
        roomService.addRoomToDatabase(database, room2);
        userService.addUserToDatabase(database, user);
        userService.addUserToDatabase(database, user2);
        userService.addUserToDatabase(database, user3);
    }

    @Test
    @DisplayName("validation of reservation (valid)")
    public void reservationValidationTest() {
        boolean result = reservationService.reservationValidation(reservation);
        assertThat(result, is(anyOf(equalTo(true))));
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because of null argument)")
    public void reservationValidation2Test() {
        assertFalse(reservationService.reservationValidation(null));
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because start date is after end date)")
    public void reservationValidation3Test() {
        reservation.setEndDate(new DateTime(2019, 1, 1, 11, 0));
        assertFalse(reservationService.reservationValidation(reservation));
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because user in reservation is null)")
    public void reservationValidation4Test() {
        reservation.setUser(null);
        assertFalse(reservationService.reservationValidation(reservation));
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because room in reservation is null)")
    public void reservationValidation5Test() {
        reservation.setRoom(null);
        assertFalse(reservationService.reservationValidation(reservation));
    }

    @Test
    @DisplayName("validation of reservation " +
            "(returns false because user and room in reservation is null)")
    public void reservationValidation6Test() {
        reservation.setUser(null);
        reservation.setRoom(null);
        assertFalse(reservationService.reservationValidation(reservation));
    }

    @Test
    @DisplayName("adding reservation to database (valid)")
    public void addReservationToDatabaseTest() {
        assertTrue(database.getReservations().isEmpty());
        reservationService.addReservationToDatabase(database, reservation);
        reservationService.addReservationToDatabase(database, reservation2);

        HashMap<Integer, Reservation> reservationsTemp = new HashMap<>();
        reservationsTemp.put(1, reservation);
        reservationsTemp.put(2, reservation2);
        assertEquals(reservationsTemp, database.getReservations());
    }

    @Test
    @DisplayName("adding reservation to database " +
            "(throws NullPointerException when database is null")
    public void addReservationToDatabase2Test() {
        assertThrows(NullPointerException.class,
                () -> reservationService.addReservationToDatabase(null, reservation));
    }

    @Test
    @DisplayName("adding reservation to database " +
            "(throws IllegalArgumentException when reservation is null")
    public void addReservationToDatabase3Test() {
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.addReservationToDatabase(database, null));
    }

    @Test
    @DisplayName("adding reservation to database " +
            "(throws IllegalArgumentException when database and reservation are null")
    public void addReservationToDatabase4Test() {
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.addReservationToDatabase(null, null));
    }

    @Test
    @DisplayName("adding reservation to database" +
            "(throws IllegalArgumentException when reservation doesn't pass validation")
    public void addReservationToDatabase5Test() {
        reservation.setEndDate(null);
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.addReservationToDatabase(database, reservation));
    }

    @Test
    @DisplayName("adding reservation to database" +
            "(throws NullPointerException when room doesn't exist in database")
    public void addReservationToDatabase6Test() {
        reservation.setRoom(new Room(hotel, 666, 1));
        assertThrows(NullPointerException.class,
                () -> reservationService.addReservationToDatabase(database, reservation));
    }

    @Test
    @DisplayName("adding reservation to database" +
            "(throws NullPointerException when user doesn't exist in database")
    public void addReservationToDatabase7Test() {
        reservation.setUser(new User("example@example.pl"));
        assertThrows(NullPointerException.class,
                () -> reservationService.addReservationToDatabase(database, reservation));
    }

    @Test
    @DisplayName("adding reservation to database " +
            "(throws DateTimeException because selected (exactly the same) date is reserved by other person)")
    public void addReservationToDatabase8Test() {
        reservationService.addReservationToDatabase(database, reservation);
        reservationService.addReservationToDatabase(database, reservation2);
        Reservation newReservation = new Reservation(
                new DateTime(2019, 5, 5, 11, 0),
                new DateTime(2019, 5, 6, 11, 0),
                user3, room);

        assertThrows(DateTimeException.class,
                () -> reservationService.addReservationToDatabase(database, newReservation));
    }

    @Test
    @DisplayName("adding reservation to database " +
            "(throws DateTimeException because selected date (that covers other date) " +
            "is reserved by other person)")
    public void addReservationToDatabase9Test() {
        reservationService.addReservationToDatabase(database, reservation);
        reservationService.addReservationToDatabase(database, reservation2);
        Reservation newReservation = new Reservation(
                new DateTime(2019, 5, 6, 10, 0),
                new DateTime(2019, 5, 7, 10, 0),
                user3, room);

        assertThrows(DateTimeException.class,
                () -> reservationService.addReservationToDatabase(database, newReservation));
    }

    @Test
    @DisplayName("adding reservation to database" +
            "(checking if returned identificator is valid)")
    public void addReservationToDatabase10Test() {
        reservationService.addReservationToDatabase(database, reservation);
        String identificator = reservation.getStartDate().toString() +
                reservation.getEndDate().toString() +
                reservation.getRoom().getNumberOfRoom() +
                reservation.getRoom().getHotel().getName();

        assertEquals(identificator, database.getReservations().get(1).getIdentificator());
    }

    @Test
    @DisplayName("adding reservation to database" +
            "(throws DateTimeException because of minutes in start date with empty reservation list)")
    public void addReservationToDatabase11Test() {
        reservation.setEndDate(new DateTime(2020, 1, 1, 11, 11));

        assertThrows(DateTimeException.class,
                () -> reservationService.addReservationToDatabase(database, reservation));
    }

    @Test
    @DisplayName("adding reservation to database" +
            "(throws DateTimeException because of minutes in start date with non-empty reservation list)")
    public void addReservationToDatabase12Test() {
        reservation.setEndDate(new DateTime(2020, 1, 1, 11, 11));

        reservationService.addReservationToDatabase(database, reservation3);
        assertThrows(DateTimeException.class,
                () -> reservationService.addReservationToDatabase(database, reservation));
    }

    @Test
    @DisplayName("adding reservation to database" +
            "(throws DateTimeException because date of reservation is contained in other's date " +
            "of reservation with non-empty reservation list)")
    public void addReservationToDatabase13Test() {
        reservation2.setStartDate(new DateTime(2019, 4, 4, 10, 0));
        reservation2.setEndDate(new DateTime(2019, 4, 4, 11, 0));
        reservation3.setStartDate(new DateTime(2019, 4, 4, 9, 0));
        reservation3.setEndDate(new DateTime(2019, 4, 5, 9, 0));

        reservationService.addReservationToDatabase(database, reservation3);
        assertThrows(DateTimeException.class,
                () -> reservationService.addReservationToDatabase(database, reservation2));
    }

    @Test
    @DisplayName("getting reservations of specific user when hashmap of user's revervations is not empty")
    public void getReservationsOfUserTest() {
        HashMap<Integer, Reservation> reservations = new HashMap<>();
        reservations.put(1, reservation);
        reservationService.addReservationToDatabase(database, reservation);
        reservationService.addReservationToDatabase(database, reservation2);

        assertEquals(reservations, reservationService.getReservationsOfUser(database, user));
    }

    @Test
    @DisplayName("getting reservations of specific user when hashmap of user's revervation is empty")
    public void getReservationsOfUser2Test() {
        assertTrue(reservationService.getReservationsOfUser(database, user2).isEmpty());
    }

    @Test
    @DisplayName("getting reservations of specific user" +
            "(throws IllegalArgumentException when database is null")
    public void getReservationsOfUser3Test() {
        assertThrows(NullPointerException.class,
                () -> reservationService.getReservationsOfUser(null, user));
    }

    @Test
    @DisplayName("getting reservations of specific user" +
            "(throws IllegalArgumentException when user is null")
    public void getReservationsOfUser4Test() {
        assertThrows(NullPointerException.class,
                () -> reservationService.getReservationsOfUser(database, null));
    }

    @Test
    @DisplayName("getting reservations of specific user" +
            "(throws IllegalArgumentException when database and user are null")
    public void getReservationsOfUser5Test() {
        assertThrows(NullPointerException.class,
                () -> reservationService.getReservationsOfUser(null, null));
    }

    @AfterEach
    public void cleanup() {
        database = null;
        reservationService = null;
        userService = null;
        hotelService = null;
        roomService = null;
        reservation = null;
        reservation2 = null;
        reservation3 = null;
        room = null;
        room2 = null;
        user = null;
        user2 = null;
        user3 = null;
        hotel = null;
        hotel2 = null;
    }
}
