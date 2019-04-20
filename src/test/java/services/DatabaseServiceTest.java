package services;

import database.Database;
import models.Hotel;
import models.Reservation;
import models.Room;
import models.User;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseServiceTest {
    private DatabaseService databaseService;
    private Database database;
    private ReservationService reservationService;
    private RoomService roomService;
    private UserService userService;
    private HotelService hotelService;
    private Reservation reservation;
    private Reservation reservation2;
    private Reservation reservation3;
    private Room room;
    private User user;
    private User user2;
    private User user3;
    private Hotel hotel;

    @BeforeEach
    public void init() {
        databaseService = new DatabaseService();
        database = new Database();
        reservationService = new ReservationService();
        roomService = new RoomService();
        userService = new UserService();
        hotelService = new HotelService();
        hotel = new Hotel("Sample name", new LocalTime(8), new LocalTime(23));
        room = new Room(hotel, 200, 2);
        user = new User("test@test.com");
        user2 = new User("test2@test2.com");
        user3 = new User("test3@test3.com");
        reservation = new Reservation(new DateTime(2019, 5, 1, 11, 0),
                new DateTime(2019, 5, 2, 11, 0),
                user, room);
        reservation2 = new Reservation(new DateTime(2019, 6, 6, 11, 0),
                new DateTime(2019, 6, 9, 11, 0),
                user2, room);
        reservation3 = new Reservation(new DateTime(2018, 5, 2, 11, 0),
                new DateTime(2018, 5, 3, 11, 0),
                user, room);
        hotelService.addHotelToDatabase(database, hotel);
        roomService.addRoomToDatabase(database, room);
        userService.addUserToDatabase(database, user);
        userService.addUserToDatabase(database, user2);
        userService.addUserToDatabase(database, user3);
        reservationService.addReservationToDatabase(database, reservation);
        reservationService.addReservationToDatabase(database, reservation2);
        reservationService.addReservationToDatabase(database, reservation3);
    }

    @Test
    @DisplayName("returns number of day in week that is the most crowded by clients")
    public void theMostCrowdedDayOfTheWeekTest() {
        assertThat(databaseService.theMostCrowdedDayOfTheWeek(database)).isEqualTo(3);
    }

    @Test
    @DisplayName("counting the most crowded day of the week " +
            "(throws NullPointerException because database is null")
    public void theMostCrowdedDayOfTheWeek2Test() {
        assertThrows(NullPointerException.class,
                () -> databaseService.theMostCrowdedDayOfTheWeek(null));
    }

    @Test
    @DisplayName("counting the most crowded day of the week " +
            "(throws 0 because in database were 0 reservations)")
    public void theMostCrowdedDayOfTheWeek3Test() {
        Database emptyDatabase = new Database();
        assertEquals(0, databaseService.theMostCrowdedDayOfTheWeek(emptyDatabase));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data.csv")
    @DisplayName("generating statistics (valid)")
    public void generateStatisticsTest(int value, String string) {
        HashMap<String, Integer> statistics = databaseService.generateStatistics(database);

        assertEquals(value, statistics.get(string));
    }

    @Test
    @DisplayName("generating statistics" +
            "(throws NullPointerException because database is null)")
    public void generateStatistics2Test() {
        assertThrows(NullPointerException.class,
                () -> databaseService.generateStatistics(null));
    }

    @AfterEach
    public void cleanup() {
        databaseService = null;
        database = null;
        reservationService = null;
        userService = null;
        hotelService = null;
        roomService = null;
        reservation = null;
        reservation2 = null;
        reservation3 = null;
        room = null;
        user = null;
        user2 = null;
        user3 = null;
        hotel = null;
    }
}
