package services;

import database.Database;
import models.Hotel;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class HotelServiceTest {
    private Database database;
    private HotelService hotelService;
    private Hotel hotel;

    @BeforeEach
    public void init() {
        database = new Database();
        hotelService = new HotelService();
        hotel = new Hotel("Sample name", new LocalTime(8), new LocalTime(22));
    }

    @Test
    @DisplayName("validation of hotel (valid)")
    public void hotelValidationTest() {
        boolean result = hotelService.hotelValidation(hotel);
        assertTrue(result);
    }

    @Test
    @DisplayName("validation of hotel " +
            "(returns false because of null argument)")
    public void hotelValidation2Test() {
        assertFalse(hotelService.hotelValidation(null));
    }

    @Test
    @DisplayName("validation of hotel " +
            "(returns false because of hotel name as null)")
    public void hotelValidation3Test() {
        hotel.setName(null);
        assertFalse(hotelService.hotelValidation(hotel));
    }

    @Test
    @DisplayName("validation of hotel " +
            "(returns false because hotel name is empty string)")
    public void hotelValidation4Test() {
        hotel.setName("");
        assertFalse(hotelService.hotelValidation(hotel));
    }

    @Test
    @DisplayName("validation of hotel " +
            "(returns false because open hour is null)")
    public void hotelValidation5Test() {
        hotel.setOpenHour(null);
        assertFalse(hotelService.hotelValidation(hotel));
    }

    @Test
    @DisplayName("validation of hotel " +
            "(returns false because close hour is null)")
    public void hotelValidation6Test() {
        hotel.setCloseHour(null);
        assertFalse(hotelService.hotelValidation(hotel));
    }

    @Test
    @DisplayName("validation of hotel " +
            "(returns false because open hour is after close hour)")
    public void hotelValidation7Test() {
        hotel.setOpenHour(new LocalTime(21));
        hotel.setCloseHour(new LocalTime(20));
        assertFalse(hotelService.hotelValidation(hotel));
    }

    @Test
    @DisplayName("adding hotel to database (valid)")
    public void addHotelToDatabaseTest() {
        assertEquals(new HashMap<Integer, Hotel>(), database.getHotels());

        hotelService.addHotelToDatabase(database, hotel);

        HashMap<Integer, Hotel> hotelsTemp = new HashMap<>();
        hotelsTemp.put(1, hotel);
        assertEquals(hotelsTemp, database.getHotels());
    }

    @Test
    @DisplayName("adding hotel to database " +
            "(throws NullPointerException when database is null)")
    public void addHotelToDatabase2Test() {
        assertThrows(NullPointerException.class,
                () -> hotelService.addHotelToDatabase(null, hotel));
    }

    @Test
    @DisplayName("adding hotel to database " +
            "(throws IllegalArgumentException when hotel is null)")
    public void addHotelToDatabase3Test() {
        assertThrows(IllegalArgumentException.class,
                () -> hotelService.addHotelToDatabase(database, null));
    }

    @Test
    @DisplayName("adding hotel to database " +
            "(throws IllegalArgumentException when database and hotel are null)")
    public void addHotelToDatabase4Test() {
        assertThrows(IllegalArgumentException.class,
                () -> hotelService.addHotelToDatabase(null, null));
    }

    @Test
    @DisplayName("adding hotel to database " +
            "(throws IllegalArgumentException when hotel doesn't pass validation)")
    public void addHotelToDatabase6Test() {
        assertThrows(IllegalArgumentException.class,
                () -> hotelService.addHotelToDatabase(database,
                        new Hotel(null, new LocalTime(8), new LocalTime(20))));
    }

    @AfterEach
    public void cleanup() {
        database = null;
        hotelService = null;
        hotel = null;
    }
}
