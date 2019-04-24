package services;

import models.Reservation;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

class ReservationServiceTest {
    @Mock
    private ReservationService reservationService;

    private Reservation reservation;

    @BeforeEach
    void init() {
        reservationService = mock(ReservationService.class);
    }

    @Test
    void validAddTest() {
        assertDoesNotThrow(() -> reservationService.add(reservation));
    }

    @Test
    void addThrowsWhenReservationDoesntPassValidation() {
        reservationService.add(reservation);
        doThrow(IllegalArgumentException.class).when(reservationService).add(reservation);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.add(reservation));
    }

    @Test
    void addThrowsWhenRoomIsNull() {
        reservationService.add(null);
        doThrow(NullPointerException.class).when(reservationService).add(null);

        assertThrows(NullPointerException.class,
                () -> reservationService.add(null));
    }

    @Test
    void validGetTest() {
        when(reservationService.get(1)).thenReturn(reservation);

        Reservation result = reservationService.get(1);

        assertEquals(reservation, result);
    }

    @Test
    void getThrowsWhenIdIsZero() {
        when(reservationService.get(0)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.get(0));
    }

    @Test
    void getThrowsWhenIdIsNegative() {
        when(reservationService.get(-1)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.get(-1));
    }

    @Test
    void getReturnsNullWhenReservationIsNotFound() {
        when(reservationService.get(2)).thenReturn(null);

        assertNull(reservationService.get(2));
    }

    @Test
    void validUpdateTest() {
        assertDoesNotThrow(() -> reservationService.update(reservation));
    }

    @Test
    void updateThrowsWhenReservationDoesntPassValidation() {
        reservationService.update(reservation);
        doThrow(IllegalArgumentException.class).when(reservationService).update(reservation);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.update(reservation));
    }

    @Test
    void updateThrowsWhenReservationIsNull() {
        reservationService.update(null);
        doThrow(NullPointerException.class).when(reservationService).update(null);

        assertThrows(NullPointerException.class,
                () -> reservationService.update(null));
    }

    @Test
    void validDeleteTest() {
        assertDoesNotThrow(() -> reservationService.delete(1));
    }

    @Test
    void deleteThrowsWhenIdIsZero() {
        reservationService.delete(0);
        doThrow(IllegalArgumentException.class).when(reservationService).delete(0);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.delete(0));
    }

    @Test
    void deleteThrowsWhenIdIsNegative() {
        reservationService.delete(-1);
        doThrow(IllegalArgumentException.class).when(reservationService).delete(-1);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.delete(-1));
    }

    @Test
    void deleteThrowsWhenReservationIsNotFound() {
        reservationService.delete(2);
        doThrow(NullPointerException.class).when(reservationService).delete(2);

        assertThrows(NullPointerException.class,
                () -> reservationService.delete(2));
    }

    @AfterEach
    void cleanup() {
        reservationService = null;
        reservation = null;
    }
}