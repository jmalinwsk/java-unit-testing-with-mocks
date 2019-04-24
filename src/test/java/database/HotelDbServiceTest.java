package database;

import models.Hotel;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotelDbServiceTest {
    @Mock
    HotelDbService hotelDbService;

    private Hotel hotel;

    @BeforeEach
    void init() {
        hotelDbService = mock(HotelDbService.class);
        hotel = new Hotel();
        hotel.setId(1);
        hotel.setName("Sample name");
        hotel.setOpenHour(new LocalTime(8));
        hotel.setCloseHour(new LocalTime(22));
    }

    @Test
    void validAddTest() {
        assertDoesNotThrow(() -> hotelDbService.add(hotel));
    }

    @Test
    void addThrowsWhenHotelIsNull() {
        hotelDbService.add(null);
        doThrow(NullPointerException.class).when(hotelDbService).add(null);

        assertThrows(NullPointerException.class,
                () -> hotelDbService.add(null));
    }

    @Test
    void validGetTest() {
        when(hotelDbService.get(1)).thenReturn(hotel);

        Hotel result = hotelDbService.get(1);

        assertEquals(hotel, result);
    }

    @Test
    void getThrowsWhenIdIsZero() {
        when(hotelDbService.get(0)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> hotelDbService.get(0));
    }

    @Test
    void getThrowsWhenIdIsNegative() {
        when(hotelDbService.get(-1)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> hotelDbService.get(-1));
    }

    @Test
    void getReturnsNullWhenHotelIsNotFound() {
        when(hotelDbService.get(2)).thenReturn(null);

        assertNull(hotelDbService.get(2));
    }

    @Test
    void validUpdateTest() {
        assertDoesNotThrow(() -> hotelDbService.update(hotel));
    }

    @Test
    void updateThrowsWhenHotelIsNull() {
        hotelDbService.update(null);
        doThrow(NullPointerException.class).when(hotelDbService).update(null);

        assertThrows(NullPointerException.class,
                () -> hotelDbService.update(null));
    }

    @Test
    void validDeleteTest() {
        assertDoesNotThrow(() -> hotelDbService.delete(1));
    }

    @Test
    void deleteThrowsWhenIdIsZero() {
        hotelDbService.delete(0);
        doThrow(IllegalArgumentException.class).when(hotelDbService).delete(0);

        assertThrows(IllegalArgumentException.class,
                () -> hotelDbService.delete(0));
    }

    @Test
    void deleteThrowsWhenIdIsNegative() {
        hotelDbService.delete(-1);
        doThrow(IllegalArgumentException.class).when(hotelDbService).delete(-1);

        assertThrows(IllegalArgumentException.class,
                () -> hotelDbService.delete(-1));
    }

    @Test
    void deleteThrowsWhenHotelIsNotFound() {
        hotelDbService.delete(2);
        doThrow(NullPointerException.class).when(hotelDbService).delete(2);

        assertThrows(NullPointerException.class,
                () -> hotelDbService.delete(2));
    }

    @AfterEach
    void cleanup() {
        hotelDbService = null;
        hotel = null;
    }
}
