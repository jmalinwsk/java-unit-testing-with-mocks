package services;

import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import mocks.DatabaseContext;
import models.Hotel;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class HotelServiceTest {
    private DatabaseContext databaseContext;
    private HotelService hotelService;

    private Hotel hotel;

    @BeforeEach
    void init() {
        databaseContext = new DatabaseContext();
        hotelService = new HotelService(databaseContext);
        hotel = new Hotel(1, "Sample name",
                new LocalTime(6), new LocalTime((22)));
    }

    @Test
    void validAddTest() throws ValidationException, ElementNotFoundException {
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
    void addThrowsWhenHotelDoesntPassValidation() {
        hotel.setOpenHour(null);

        assertThrows(ValidationException.class,
                () -> hotelService.add(hotel));
    }

    @Test
    void addThrowsWhenHotelIsNull() {
        assertThrows(ValidationException.class,
                () -> hotelService.add(null));
    }

    @Test
    void validGetTest() throws ElementNotFoundException, ValidationException {
        hotelService.add(hotel);

        assertEquals(hotel, hotelService.get(hotel.getId()));
    }

    @Test
    void getThrowsWhenIdIsZero() {
        assertThrows(ElementNotFoundException.class,
                () -> hotelService.get(0));
    }

    @Test
    void getThrowsWhenIdIsNegative() {
        assertThrows(ElementNotFoundException.class,
                () -> hotelService.get(-1));
    }

    @Test
    void getReturnsNullWhenHotelIsNotFound() {
        assertThrows(ElementNotFoundException.class,
                () -> hotelService.get(2));
    }

    @Test
    void validUpdateTest() throws ValidationException {
        hotelService.add(hotel);
        String name = hotel.getName();
        hotel.setName("New hotel name");

        assertAll(
                () -> assertDoesNotThrow(() -> hotelService.update(hotel)),
                () -> assertNotEquals(name, hotelService.get(hotel.getId()).getName())
        );
    }

    @Test
    void updateThrowsWhenHotelDoesntPassValidation() throws ValidationException {
        hotelService.add(hotel);
        hotel.setName(null);

        assertThrows(ValidationException.class,
                () -> hotelService.update(hotel));
    }

    @Test
    void updateThrowsWhenHotelIsNull() {
        assertThrows(ValidationException.class,
                () -> hotelService.update(null));
    }

    @Test
    void validDeleteTest() throws ValidationException {
        hotelService.add(hotel);

        assertAll(
                () -> assertDoesNotThrow(() -> hotelService.delete(hotel.getId())),
                () -> assertThrows(ElementNotFoundException.class,
                        () -> hotelService.get(hotel.getId()))
        );
    }

    @Test
    void deleteThrowsWhenIdIsZero() {
        assertThrows(ElementNotFoundException.class,
                () -> hotelService.get(0));
    }

    @Test
    void deleteThrowsWhenIdIsNegative() {
        assertThrows(ElementNotFoundException.class,
                () -> hotelService.get(-1));
    }

    @Test
    void deleteThrowsWhenHotelIsNotFound() {
        assertThrows(ElementNotFoundException.class,
                () -> hotelService.get(2));
    }

    @AfterEach
    void cleanup() {
        databaseContext = null;
        hotel = null;
    }
}
