package services;

import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import mocks.DatabaseContext;
import models.Hotel;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class HotelServiceTest {
    private DatabaseContext databaseContext;
    private HotelService hotelService;
    private HashMap<Integer, Hotel> hotelList;

    private Hotel hotel;

    @BeforeEach
    public void init() {
        databaseContext = new DatabaseContext();
        hotelService = new HotelService(databaseContext);
        hotel = new Hotel(1, "Sample name",
                new LocalTime(6), new LocalTime((22)));
        hotelList = new HashMap<Integer, Hotel>() {{ put(1, hotel); }};
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
    public void validAddTest() throws ValidationException, ElementNotFoundException {
        hotelService.add(hotel);
        Hotel result = hotelService.get(hotel.getId());

        assertAll(
                () -> assertEquals(1, result.getId()),
                () -> assertEquals("Sample name", result.getName()),
                () -> assertEquals(new LocalTime(6), result.getOpenHour()),
                () -> assertEquals(new LocalTime(22), result.getCloseHour())
        );
    }

    @Test
    public void addThrowsWhenHotelDoesntPassValidation() {
        hotel.setOpenHour(null);

        assertThrows(ValidationException.class,
                () -> hotelService.add(hotel));
    }

    @Test
    public void addThrowsWhenHotelIsNull() {
        assertThrows(ValidationException.class,
                () -> hotelService.add(null));
    }

    @Test
    public void validGetTest() throws ElementNotFoundException, ValidationException {
        hotelService.add(hotel);

        assertEquals(hotel, hotelService.get(hotel.getId()));
    }

    @Test
    public void getThrowsWhenIdIsZero() {
        assertThrows(ElementNotFoundException.class,
                () -> hotelService.get(0));
    }

    @Test
    public void getThrowsWhenIdIsNegative() {
        assertThrows(ElementNotFoundException.class,
                () -> hotelService.get(-1));
    }

    @Test
    public void getReturnsNullWhenHotelIsNotFound() {
        assertThrows(ElementNotFoundException.class,
                () -> hotelService.get(2));
    }

    @Test
    public void validGetAllTest() throws ValidationException {
        hotelService.add(hotel);

        assertEquals(hotelList, hotelService.get());
    }

    @Test
    public void getAllWhenListIsNull() {
        assertEquals(new HashMap<>(), hotelService.get());
    }

    @Test
    public void validUpdateTest() throws ValidationException {
        hotelService.add(hotel);
        String name = hotel.getName();
        hotel.setName("New hotel name");

        assertAll(
                () -> assertDoesNotThrow(() -> hotelService.update(hotel)),
                () -> assertNotEquals(name, hotelService.get(hotel.getId()).getName())
        );
    }

    @Test
    public void updateThrowsWhenHotelDoesntPassValidation() throws ValidationException {
        hotelService.add(hotel);
        hotel.setName(null);

        assertThrows(ValidationException.class,
                () -> hotelService.update(hotel));
    }

    @Test
    public void updateThrowsWhenHotelIsNull() {
        assertThrows(ValidationException.class,
                () -> hotelService.update(null));
    }

    @Test
    public void updateThrowsWhenHotelDoesNotExist() {
        assertThrows(ElementNotFoundException.class,
                () -> hotelService.update(hotel));
    }

    @Test
    public void validDeleteTest() throws ValidationException {
        hotelService.add(hotel);

        assertAll(
                () -> assertDoesNotThrow(() -> hotelService.delete(hotel.getId())),
                () -> assertThrows(ElementNotFoundException.class,
                        () -> hotelService.get(hotel.getId()))
        );
    }

    @Test
    public void deleteThrowsWhenIdIsZero() {
        assertThrows(ElementNotFoundException.class,
                () -> hotelService.delete(0));
    }

    @Test
    public void deleteThrowsWhenIdIsNegative() {
        assertThrows(ElementNotFoundException.class,
                () -> hotelService.delete(-1));
    }

    @Test
    public void deleteThrowsWhenHotelIsNotFound() {
        assertThrows(ElementNotFoundException.class,
                () -> hotelService.delete(2));
    }

    @AfterEach
    public void cleanup() {
        databaseContext = null;
        hotelService = null;
        hotel = null;
        hotelList = null;
    }
}
