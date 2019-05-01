package services;

import exceptions.ElementNotFoundException;
import exceptions.ValidationException;
import models.Reservation;
import models.User;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

class ReservationServiceTest {
    @Mock
    private IReservationService reservationService;

    private Reservation reservation;
    private User user;

    @BeforeEach
    void init() {
        reservationService = mock(IReservationService.class);
    }

    @Test
    void validAddTest() {
        assertDoesNotThrow(() -> reservationService.add(reservation));
    }

    @Test
    void addThrowsWhenReservationDoesntPassValidation() throws ValidationException, ElementNotFoundException {
        reservationService.add(reservation);
        doThrow(IllegalArgumentException.class).when(reservationService).add(reservation);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.add(reservation));
    }

    @Test
    void addThrowsWhenRoomIsNull() throws ValidationException, ElementNotFoundException {
        reservationService.add(null);
        doThrow(NullPointerException.class).when(reservationService).add(null);

        assertThrows(NullPointerException.class,
                () -> reservationService.add(null));
    }

    @Test
    void validGetTest() throws ElementNotFoundException {
        when(reservationService.get(1)).thenReturn(reservation);

        Reservation result = reservationService.get(1);

        assertEquals(reservation, result);
    }

    @Test
    void getThrowsWhenIdIsZero() throws ElementNotFoundException {
        when(reservationService.get(0)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.get(0));
    }

    @Test
    void getThrowsWhenIdIsNegative() throws ElementNotFoundException {
        when(reservationService.get(-1)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.get(-1));
    }

    @Test
    void getReturnsNullWhenReservationIsNotFound() throws ElementNotFoundException {
        when(reservationService.get(2)).thenReturn(null);

        assertNull(reservationService.get(2));
    }

    @Test
    void validUpdateTest() {
        assertDoesNotThrow(() -> reservationService.update(reservation));
    }

    @Test
    void updateThrowsWhenReservationDoesntPassValidation() throws ValidationException, ElementNotFoundException {
        reservationService.update(reservation);
        doThrow(IllegalArgumentException.class).when(reservationService).update(reservation);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.update(reservation));
    }

    @Test
    void updateThrowsWhenReservationIsNull() throws ValidationException, ElementNotFoundException {
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
    void deleteThrowsWhenIdIsZero() throws ElementNotFoundException {
        reservationService.delete(0);
        doThrow(IllegalArgumentException.class).when(reservationService).delete(0);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.delete(0));
    }

    @Test
    void deleteThrowsWhenIdIsNegative() throws ElementNotFoundException {
        reservationService.delete(-1);
        doThrow(IllegalArgumentException.class).when(reservationService).delete(-1);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.delete(-1));
    }

    @Test
    void deleteThrowsWhenReservationIsNotFound() throws ElementNotFoundException {
        reservationService.delete(2);
        doThrow(NullPointerException.class).when(reservationService).delete(2);

        assertThrows(NullPointerException.class,
                () -> reservationService.delete(2));
    }

    @Test
    void getEmptyReservationsOfUser() {
        when(reservationService.getReservationsOfUser(user)).thenReturn(new HashMap<>());

        assertEquals(new HashMap<>(), reservationService.getReservationsOfUser(user));
    }

    @Test
    void getReservationsOfUser() {
        HashMap<Integer, Reservation> list = new HashMap<>();
        list.put(1, new Reservation());
        when(reservationService.getReservationsOfUser(user)).thenReturn(list);

        assertEquals(list, reservationService.getReservationsOfUser(user));
    }

    @Test
    void getReservationsOfUserThrowsWhenUserIsNull() {
        when(reservationService.getReservationsOfUser(null))
                .thenThrow(new NullPointerException());

        assertThrows(NullPointerException.class,
                () -> reservationService.getReservationsOfUser(null));
    }

    @AfterEach
    void cleanup() {
        reservationService = null;
        reservation = null;
    }
}