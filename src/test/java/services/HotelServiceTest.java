package services;

import database.IDatabaseContext;
import models.Hotel;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

class HotelServiceTest {
    private IDatabaseContext databaseContext;

    private Hotel hotel;

    @BeforeEach
    void init() {
        databaseContext = mock(IDatabaseContext.class);
        hotel = new Hotel(1, "Sample name",
                new LocalTime(6), new LocalTime((22)));
    }

    @Test
    void validAddTest() {
        assertDoesNotThrow(() -> databaseContext.add(hotel));
        when(databaseContext.getHotel(hotel.getId())).thenReturn(hotel);
        Hotel result = databaseContext.getHotel(hotel.getId());

        assertAll(
                () -> assertEquals(1, result.getId()),
                () -> assertEquals("Sample name", result.getName()),
                () -> assertEquals(new LocalTime(6), result.getOpenHour()),
                () -> assertEquals(new LocalTime(22), result.getCloseHour())
        );
    }

    @Test
    void addThrowsWhenHotelDoesntPassValidation() {
        databaseContext.add(hotel);
        doThrow(IllegalArgumentException.class).when(databaseContext).add(hotel);

        assertThrows(IllegalArgumentException.class,
                () -> databaseContext.add(hotel));
    }

    @Test
    void addThrowsWhenHotelIsNull() {
        databaseContext.add(null);
        doThrow(NullPointerException.class).when(databaseContext).add(null);

        assertThrows(NullPointerException.class,
                () -> databaseContext.add(null));
    }

    @Test
    void validGetTest() {
        when(databaseContext.getHotel(hotel.getId())).thenReturn(hotel);

        Hotel result = databaseContext.getHotel(hotel.getId());

        assertEquals(hotel, result);
    }

    @Test
    void getThrowsWhenIdIsZero() {
        when(databaseContext.getHotel(0)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> databaseContext.getHotel(0));
    }

    @Test
    void getThrowsWhenIdIsNegative() {
        when(databaseContext.getHotel(-1)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> databaseContext.getHotel(-1));
    }

    @Test
    void getReturnsNullWhenHotelIsNotFound() {
        when(databaseContext.getHotel(2)).thenReturn(null);

        assertNull(databaseContext.getHotel(2));
    }

    @Test
    void validUpdateTest() {
        assertDoesNotThrow(() -> databaseContext.update(hotel));
    }

    @Test
    void updateThrowsWhenHotelDoesntPassValidation() {
        databaseContext.update(hotel);
        doThrow(IllegalArgumentException.class).when(databaseContext).update(hotel);

        assertThrows(IllegalArgumentException.class,
                () -> databaseContext.update(hotel));
    }

    @Test
    void updateThrowsWhenHotelIsNull() {
        databaseContext.update(null);
        doThrow(NullPointerException.class).when(databaseContext).update(null);

        assertThrows(NullPointerException.class,
                () -> databaseContext.update(null));
    }

    @Test
    void validDeleteTest() {
        assertDoesNotThrow(() -> databaseContext.delete(1));
    }

    @Test
    void deleteThrowsWhenIdIsZero() {
        databaseContext.delete(0);
        doThrow(IllegalArgumentException.class).when(databaseContext).delete(0);

        assertThrows(IllegalArgumentException.class,
                () -> databaseContext.delete(0));
    }

    @Test
    void deleteThrowsWhenIdIsNegative() {
        databaseContext.delete(-1);
        doThrow(IllegalArgumentException.class).when(databaseContext).delete(-1);

        assertThrows(IllegalArgumentException.class,
                () -> databaseContext.delete(-1));
    }

    @Test
    void deleteThrowsWhenHotelIsNotFound() {
        databaseContext.delete(2);
        doThrow(NullPointerException.class).when(databaseContext).delete(2);

        assertThrows(NullPointerException.class,
                () -> databaseContext.delete(2));
    }

    @AfterEach
    void cleanup() {
        databaseContext = null;
        hotel = null;
    }
}
