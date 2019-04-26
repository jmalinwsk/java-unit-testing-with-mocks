package services;

import models.Hotel;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

class HotelServiceTest {
    @Mock
    private IHotelService hotelService;
    
    private Hotel hotel;

    @BeforeEach
    void init() {
        hotelService = mock(IHotelService.class);
        hotel = new Hotel();
    }

    @Test
    void validAddTest() {
        assertDoesNotThrow(() -> hotelService.add(hotel));
    }

    @Test
    void addThrowsWhenHotelDoesntPassValidation() {
        hotelService.add(hotel);
        doThrow(IllegalArgumentException.class).when(hotelService).add(hotel);

        assertThrows(IllegalArgumentException.class,
                () -> hotelService.add(hotel));
    }

    @Test
    void addThrowsWhenHotelIsNull() {
        hotelService.add(null);
        doThrow(NullPointerException.class).when(hotelService).add(null);

        assertThrows(NullPointerException.class,
                () -> hotelService.add(null));
    }

    @Test
    void validGetTest() {
        when(hotelService.get(1)).thenReturn(hotel);

        Hotel result = hotelService.get(1);

        assertEquals(hotel, result);
    }

    @Test
    void getThrowsWhenIdIsZero() {
        when(hotelService.get(0)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> hotelService.get(0));
    }

    @Test
    void getThrowsWhenIdIsNegative() {
        when(hotelService.get(-1)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> hotelService.get(-1));
    }

    @Test
    void getReturnsNullWhenHotelIsNotFound() {
        when(hotelService.get(2)).thenReturn(null);

        assertNull(hotelService.get(2));
    }

    @Test
    void validUpdateTest() {
        assertDoesNotThrow(() -> hotelService.update(hotel));
    }

    @Test
    void updateThrowsWhenHotelDoesntPassValidation() {
        hotelService.update(hotel);
        doThrow(IllegalArgumentException.class).when(hotelService).update(hotel);

        assertThrows(IllegalArgumentException.class,
                () -> hotelService.update(hotel));
    }

    @Test
    void updateThrowsWhenHotelIsNull() {
        hotelService.update(null);
        doThrow(NullPointerException.class).when(hotelService).update(null);

        assertThrows(NullPointerException.class,
                () -> hotelService.update(null));
    }

    @Test
    void validDeleteTest() {
        assertDoesNotThrow(() -> hotelService.delete(1));
    }

    @Test
    void deleteThrowsWhenIdIsZero() {
        hotelService.delete(0);
        doThrow(IllegalArgumentException.class).when(hotelService).delete(0);

        assertThrows(IllegalArgumentException.class,
                () -> hotelService.delete(0));
    }

    @Test
    void deleteThrowsWhenIdIsNegative() {
        hotelService.delete(-1);
        doThrow(IllegalArgumentException.class).when(hotelService).delete(-1);

        assertThrows(IllegalArgumentException.class,
                () -> hotelService.delete(-1));
    }

    @Test
    void deleteThrowsWhenHotelIsNotFound() {
        hotelService.delete(2);
        doThrow(NullPointerException.class).when(hotelService).delete(2);

        assertThrows(NullPointerException.class,
                () -> hotelService.delete(2));
    }

    @AfterEach
    void cleanup() {
        hotelService = null;
        hotel = null;
    }
}
